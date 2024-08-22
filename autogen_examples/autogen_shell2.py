import os
import sys
import autogen
import subprocess

def run_shell_command(command: str) -> str:
    """Runs a shell command and returns the output or error"""
    try:
        result = subprocess.run(command, shell=True, check=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        return result.stdout.decode('utf-8')
    except subprocess.CalledProcessError as e:
        return e.stderr.decode('utf-8')

# Define the configuration for the AI assistant
config_list = [
    {
        'model': 'ollama/llama3.1:latest',
        'api_base': 'http://localhost:11434',  # Adjust if your Ollama API is hosted elsewhere
        'api_type': 'open_ai',
        'api_key': 'ollama',  # Ollama doesn't require an API key, but autogen expects one
    }
]

# Create an AssistantAgent
assistant = autogen.AssistantAgent(
    name="Shell_Assistant",
    llm_config={
        "config_list": config_list,
        "timeout": 120,
    },
    system_message="You are an AI assistant capable of running shell commands. Use the run_shell_command function when you need to execute a command."
)

# Create a UserProxyAgent
user_proxy = autogen.UserProxyAgent(
    name="User_Proxy",
    human_input_mode="NEVER",
    max_consecutive_auto_reply=10,
    is_termination_msg=lambda x: x.get("content", "").rstrip().endswith("TERMINATE"),
    code_execution_config={
        "work_dir": "coding",
        "use_docker": False,
    },
    function_map={"run_shell_command": run_shell_command}
)

# Get the question from command line arguments
question = sys.argv[1]

# Initiate the conversation
user_proxy.initiate_chat(
    assistant,
    message=f"Please help me with this task: {question}. If you need to run a shell command, use the run_shell_command function."
)

# The conversation and results will be printed automatically by autogen

