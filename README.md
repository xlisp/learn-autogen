# Learn AutoGen

## Class Relationship

look full graph: [autogen_class_refs.pdf](./autogen_class_refs.pdf)

![](./autogen_class_show.png)

## Basic Definition
```python
from typing_extensions import Annotated
import autogen
import os
import sys
import subprocess

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

## system_message is your task description: 
chatbot = autogen.AssistantAgent(
    name="chatbot",
    system_message="""For (your task description) tasks,
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

## If you need define new tools, you can like this define:
@user_proxy.register_for_execution()
@chatbot.register_for_llm(description="Calculate distance between two addresses.")
def calculate_distance(
    start_address: Annotated[str, "Starting address"],
    end_address: Annotated[str, "Destination address"],
) -> str:
    .... (your function detail) ....

def main(question):
    res = user_proxy.initiate_chat(
        chatbot,
        message=question,
        summary_method="reflection_with_llm",
    )
    return res

question = sys.argv[1]
main(question)
```
## Tools Definition
```python
function_map not effect, you must use this way , like this:
'''python
@user_proxy.register_for_execution()
@chatbot.register_for_llm(description="Calculate distance between two addresses.")
def calculate_distance(
    start_address: Annotated[str, "Starting address"],
    end_address: Annotated[str, "Destination address"],
) -> str:
      .... function detail ....
'''
```
## ReAct Definition
```python
from autogen import AssistantAgent, UserProxyAgent, ConversableAgent
import os

assistant = AssistantAgent(
    name="assistant",
    llm_config={
        "config_list": [{"model": "gpt-4o", "api_key": os.environ.get("OPENAI_API_KEY")}]
    }
)

user_proxy = UserProxyAgent(
    name="user_proxy",
    human_input_mode="NEVER",
    max_consecutive_auto_reply=10,
    code_execution_config={"work_dir": "coding"}
)

task = "Analyze the following data and create a visualization: [Your data is  current path all log file]"

user_proxy.initiate_chat(
    assistant,
    message=task
)
```

## More examples

https://github.com/chanshunli/learn-autogen/tree/main/autogen_examples

