
# https://microsoft.github.io/autogen/docs/notebooks/agentchat_logging/
import json

import pandas as pd

import autogen
from autogen import AssistantAgent, UserProxyAgent

# Setup API key. Add your own API key to config file or environment variable
llm_config = {
    "config_list": autogen.config_list_from_json(
        env_or_file="OAI_CONFIG_LIST",
    ),
    "temperature": 0.9,
}

# Start logging
logging_session_id = autogen.runtime_logging.start(config={"dbname": "logs.db"})
print("Logging session ID: " + str(logging_session_id))

# Create an agent workflow and run it
assistant = AssistantAgent(name="assistant", llm_config=llm_config)
user_proxy = UserProxyAgent(
    name="user_proxy",
    code_execution_config=False,
    human_input_mode="NEVER",
    is_termination_msg=lambda msg: "TERMINATE" in msg["content"],
)

user_proxy.initiate_chat(
    assistant, message="What is the height of the Eiffel Tower? Only respond with the answer and terminate"
)
autogen.runtime_logging.stop()
