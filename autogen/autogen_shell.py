# flow this code, rewrite it by python lib autogen: =>  jim-emacs-fun-py  master @ cat llama_index/local_agent_llama_index_shell.py

import autogen
from langfuse.llama_index import LlamaIndexCallbackHandler
import subprocess

# Set up Langfuse callback handler
langfuse_callback_handler = LlamaIndexCallbackHandler()
langfuse_callback_handler.set_trace_params(
    user_id="user-123",
    session_id="session-abc",
    tags=["ReAct shell"]
)

# Define functions for tools
def multiply(a: float, b: float) -> float:
    """Multiply two numbers and returns the product"""
    return a * b

def add(a: float, b: float) -> float:
    """Add two numbers and returns the sum"""
    return a + b

def run_shell_command(command: str) -> str:
    """Runs a shell command and returns the output or error"""
    try:
        result = subprocess.run(command, shell=True, check=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        return result.stdout.decode('utf-8')
    except subprocess.CalledProcessError as e:
        return e.stderr.decode('utf-8')

# Define the agent function
def agent_function(message):
    # Your agent logic here
    if "npm install" in message:
        result = run_shell_command("npm install")
        if "ERR!" not in result:
            return run_shell_command("npm list")
        else:
            return run_shell_command("node -v")
    else:
        return "I can only handle npm install commands for now."

# Configure the agent
agent = autogen.AssistantAgent(
    name="ReActAgent",
    llm_config={
        "config_list": [{"model": "llama3.1:latest"}],
        "request_timeout": 120.0
    },
    system_message="You are a helpful AI assistant capable of running shell commands and performing calculations.",
    function_map={
        "multiply": multiply,
        "add": add,
        "run_shell_command": run_shell_command
    }
)

# Configure the user proxy
user_proxy = autogen.UserProxyAgent(
    name="User",
    human_input_mode="TERMINATE",
    max_consecutive_auto_reply=10,
    is_termination_msg=lambda x: x.get("content", "").rstrip().endswith("TERMINATE"),
    code_execution_config={
        "work_dir": "coding",
        "use_docker": False
    },
    function_map={
        "agent_function": agent_function
    }
)

# Start the conversation
user_proxy.initiate_chat(
    agent,
    message="Execute the installation command npm install. If successful, it will return to npm list. If it fails, it will return to node -v."
)

# The conversation results will be stored in user_proxy.chat_messages
print(user_proxy.chat_messages)

