from typing_extensions import Annotated
import autogen
import os
import sys
import subprocess
import re

pyenv = "source /opt/anaconda3/etc/profile.d/conda.sh &&  conda activate dlcss  && cd /Users/clojure/EmacsPyPro/learn-autogen && "

local_llm_config = {
    "config_list": [
        {
            "model": "gpt-4o",
            "api_key": os.environ.get("OPENAI_API_KEY"),
            "api_type": "open_ai",
        }
    ],
    "cache_seed": None,
}

chatbot = autogen.AssistantAgent(
    name="chatbot",
    system_message="""For question answering tasks:
            1. call get_screen_question get question
            2. answer question output
            3. calling tool 'select_answer'.
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

@user_proxy.register_for_execution()
@chatbot.register_for_llm(description="Get the screen question.")
def get_screen_question() -> str:
    cmd = f"{pyenv} poetry run python jiaxiao_pyautogui_app/jiaxiao_ocr_read_qa.py"
    result = subprocess.run(
        cmd, shell=True, check=True, capture_output=True, text=True
    )
    return result.stdout.strip()

@user_proxy.register_for_execution()
@chatbot.register_for_llm(description="Click to select the answer on screen.")
def select_answer(answer: Annotated[str, "The answer to select"]) -> str:
    #answer = re.sub(r'[^\u4e00-\u9fff]', '', answer)
    answer = re.sub(r'[^\u4e00-\u9fff0-9]', '', answer)
    cmd = f'{pyenv} poetry run python jiaxiao_pyautogui_app/jiaxiao_ocr_click.py "{answer}"'
    print(f"Call cmd : {cmd}")
    result = subprocess.run(
        cmd, shell=True, check=True, capture_output=True, text=True
    )
    return result.stdout.strip()

def main(question):
    chat_history = []
    while True:
        print(f"Sending message to chatbot: {question}")
        response = user_proxy.initiate_chat(
            chatbot,
            message=question,
            clear_history=False
        )

        print(f"Received response: {response}")

        if response.chat_history:
            last_message = response.chat_history[-1]
            chat_history.append(last_message)
            print(f"Last message: {last_message}")

            if last_message.get('tool_calls'):
                print("Function call detected, executing...")
                tool_call = last_message['tool_calls'][0]
                function_name = tool_call['function']['name']
                function_args = tool_call['function']['arguments']
                print(f"Function to call: {function_name}")
                print(f"Function arguments: {function_args}")

                # Execute the function
                if function_name == 'get_screen_question':
                    result = get_screen_question()
                elif function_name == 'select_answer':
                    import json
                    args = json.loads(function_args)
                    result = select_answer(args['answer'])
                else:
                    result = "Unknown function"

                # Prepare the tool response message
                tool_response = {
                    "tool_call_id": tool_call['id'],
                    "role": "tool",
                    "name": function_name,
                    "content": result
                }

                # Send the tool response back to the chatbot
                question = f"Tool response: {json.dumps(tool_response)}"
            else:
                print("No function call detected, terminating...")
                break
        else:
            print("No messages in the response, terminating...")
            break

    return chat_history

if __name__ == "__main__":
    question = sys.argv[1] if len(sys.argv) > 1 else "Start the question answering process"
    main(question)

