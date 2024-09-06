import os
import autogen

config_list = [
    {
        "model": "gpt-4o",
        "api_key": os.environ.get("OPENAI_API_KEY"),
        "api_type": "open_ai",
    }
]


llm_config = {
    "request_timeout": 600,
    "seed": 42,
    "config_list": config_list,
    "temperature": 0
}

assistant = autogen.AssistantAgent(
    name="assistant",
    llm_config=llm_config
)

user_proxy = autogen.UserProxyAgent(
    name="user_proxy",
    human_input_mode="TERMINATE",
    max_consecutive_auto_reply=10,
    is_termination_msg=lambda x: x.get("content", ''). rstrip().endswit("TERMINATE"),
    code_execution_config ={"work_dir": "web"},
    llm_config=llm_config,
    system_message="""Reply TERMINATE if the task has been solved at full satisfaction
    Otherwise, reply CONTINUE, or the reason why the task is not solved yet."""
)

task = """
I Give me a summary of this article: https://apnews.com/article/school-shootings-talk-with-kids-cd21c1445cb6cfd89ed9184f030530ff
"""

user_proxy.initiate_chat(
    assistant,
    message=task
)
