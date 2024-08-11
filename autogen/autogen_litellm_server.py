# https://microsoft.github.io/autogen/docs/topics/non-openai-models/local-litellm-ollama/#example-with-function-calling

from typing import Literal

from typing_extensions import Annotated

import autogen

local_llm_config = {
    "config_list": [
        {
            "model": "ollama/llama3.1", #"NotRequired",  # Loaded with LiteLLM command
            "api_key": "NULL",  # Not needed
            "api_type": "open_ai",
            "base_url": "http://0.0.0.0:4000",  # Your LiteLLM URL
            "price": [0, 0],  # Put in price per 1K tokens [prompt, response] as free!
        }
    ],
    "cache_seed": None,  # Turns off caching, useful for testing different models
}


# Create the agent and include examples of the function calling JSON in the prompt
# to help guide the model
chatbot = autogen.AssistantAgent(
    name="chatbot",
    system_message="""For currency exchange tasks,
        only use the functions you have been provided with.
        If the function has been called previously,
        return only the word 'TERMINATE'.""",
    llm_config=local_llm_config,
)

user_proxy = autogen.UserProxyAgent(
    name="user_proxy",
    is_termination_msg=lambda x: x.get("content", "") and "TERMINATE" in x.get("content", ""),
    human_input_mode="NEVER",
    max_consecutive_auto_reply=1,
    code_execution_config={"work_dir": "code", "use_docker": False},
)

CurrencySymbol = Literal["USD", "EUR"]

# Define our function that we expect to call


def exchange_rate(base_currency: CurrencySymbol, quote_currency: CurrencySymbol) -> float:
    if base_currency == quote_currency:
        return 1.0
    elif base_currency == "USD" and quote_currency == "EUR":
        return 1 / 1.1
    elif base_currency == "EUR" and quote_currency == "USD":
        return 1.1
    else:
        raise ValueError(f"Unknown currencies {base_currency}, {quote_currency}")


# Register the function with the agent
@user_proxy.register_for_execution()
@chatbot.register_for_llm(description="Currency exchange calculator.")
def currency_calculator(
    base_amount: Annotated[float, "Amount of currency in base_currency"],
    base_currency: Annotated[CurrencySymbol, "Base currency"] = "USD",
    quote_currency: Annotated[CurrencySymbol, "Quote currency"] = "EUR",
) -> str:
    quote_amount = exchange_rate(base_currency, quote_currency) * base_amount
    return f"{format(quote_amount, '.2f')} {quote_currency}"

# start the conversation
res = user_proxy.initiate_chat(
    chatbot,
    message="How much is 123.45 EUR in USD?",
    summary_method="reflection_with_llm",
)

# $ prunp autogen/autogen_litellm_server.py
# user_proxy (to chatbot):
# 
# How much is 123.45 EUR in USD?
# 
# --------------------------------------------------------------------------------
# chatbot (to user_proxy):
# 
# ***** Suggested tool call (call_3411c581-1a8a-4db4-b8fd-1f5a65638541): currency_calculator *****
# Arguments:
# {"base_amount": 123.45, "base_currency": "EUR", "quote_currency": "USD"}
# ************************************************************************************************
# 
# --------------------------------------------------------------------------------
# 
# >>>>>>>> EXECUTING FUNCTION currency_calculator...
# user_proxy (to chatbot):
# 
# user_proxy (to chatbot):
# 
# ***** Response from calling tool (call_3411c581-1a8a-4db4-b8fd-1f5a65638541) *****
# 135.80 USD ### 回答对了，问了chatgpt客户端也是回答这么多 =》chatgpt背后生成了代码去计算了一遍： 123.45 EUR is approximately 135.80 USD based on an exchange rate of 1.10. Please note that exchange rates fluctuate and the actual rate might differ. ￼ 
# **********************************************************************************
# chatgpt客户端背后生成的代码：
# Conversion of EUR to USD using a fixed exchange rate for demonstration purposes.
# Normally, the exchange rate would be obtained from a financial API, but for now, I'll use a common exchange rate.
#exchange_rate_eur_to_usd = 1.10  # This is an example rate and might not reflect the current rate.
#amount_eur = 123.45
#amount_usd = amount_eur * exchange_rate_eur_to_usd
#amount_usd
# --------------------------------------------------------------------------------
# 
