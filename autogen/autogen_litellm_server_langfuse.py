# https://microsoft.github.io/autogen/docs/topics/non-openai-models/local-litellm-ollama/#example-with-function-calling
# USE: prunp autogen/autogen_litellm_server_langfuse.py "How much is 923.45 EUR in USD?"

from typing import Literal
from typing_extensions import Annotated
import autogen
from langfuse import Langfuse

# from langfuse.decorators import trace # 老的接口，cl生成。=> 替换为新的@observe
# https://langfuse.com/docs/sdk/python/decorators
from langfuse.decorators import observe, langfuse_context
import os
import sys

# Langfuse initialization
langfuse = Langfuse(
    public_key=os.environ["LANGFUSE_PUBLIC_KEY"],
    secret_key=os.environ["LANGFUSE_SECRET_KEY"],
    host="https://us.cloud.langfuse.com",
)

local_llm_config = {
    "config_list": [
        {
            "model": "ollama/llama3.1",
            "api_key": "NULL",
            "api_type": "open_ai",
            "base_url": "http://0.0.0.0:4000",
            "price": [0, 0],
        }
    ],
    "cache_seed": None,
}

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
    is_termination_msg=lambda x: x.get("content", "")
    and "TERMINATE" in x.get("content", ""),
    human_input_mode="NEVER",
    max_consecutive_auto_reply=1,
    code_execution_config={"work_dir": "code", "use_docker": False},
)

CurrencySymbol = Literal["USD", "EUR"]


def exchange_rate(
    base_currency: CurrencySymbol, quote_currency: CurrencySymbol
) -> float:
    if base_currency == quote_currency:
        return 1.0
    elif base_currency == "USD" and quote_currency == "EUR":
        return 1 / 1.1
    elif base_currency == "EUR" and quote_currency == "USD":
        return 1.1
    else:
        raise ValueError(f"Unknown currencies {base_currency}, {quote_currency}")


## 这里加上了span了：SPAN currency_calculator
@user_proxy.register_for_execution()
@chatbot.register_for_llm(description="Currency exchange calculator.")
@observe()
def currency_calculator(
    base_amount: Annotated[float, "Amount of currency in base_currency"],
    base_currency: Annotated[CurrencySymbol, "Base currency"] = "USD",
    quote_currency: Annotated[CurrencySymbol, "Quote currency"] = "EUR",
) -> str:
    quote_amount = exchange_rate(base_currency, quote_currency) * base_amount
    # langfuse.event("currency_conversion_completed", 2.0)
    langfuse_context.score_current_observation(
        name="currency_conversion_completed",
        value=1,
        comment="I like how personalized the response is",
    )
    return f"{format(quote_amount, '.2f')} {quote_currency}"


@observe()
def main(question):
    res = user_proxy.initiate_chat(
        chatbot,
        message=question,
        summary_method="reflection_with_llm",
    )
    # langfuse.score("conversation_completed", 1.0) # => 打分这个先不要！TODO
    langfuse_context.score_current_trace(
        name="feedback-on-trace",
        value=1,
        comment="I like how personalized the response is, conversation_completed",
    )
    return res


if __name__ == "__main__":
    main(sys.argv[1])  # "How much is 123.45 EUR in USD?"
    langfuse.flush()

#
# ## 跑langfuse正常了！！！
# (emacspy) 坚持去λ化(中-易) jim-emacs-fun-py  master @ prunp autogen/autogen_litellm_server_langfuse.py
# user_proxy (to chatbot):
#
# How much is 123.45 EUR in USD?
#
# --------------------------------------------------------------------------------
# chatbot (to user_proxy):
#
# ***** Suggested tool call (call_069fbe1e-dd9e-4453-8c57-2876ea0d1ba6): currency_calculator *****
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
# ***** Response from calling tool (call_069fbe1e-dd9e-4453-8c57-2876ea0d1ba6) *****
# 135.80 USD
# **********************************************************************************
#
# --------------------------------------------------------------------------------
#
## https://us.cloud.langfuse.com/project/clxfdeck6000fl5tkkzg8ut7r/traces/17da6472-70b0-4bc6-afb5-752dc083807f?observation=571b09cc-c59c-453e-9133-8ed0e6f6b641
# {
#     "args": [],
#     "kwargs": {
#         "base_amount": 123.45,
#         "base_currency": "EUR",
#         "quote_currency": "USD"
#     }
# }
