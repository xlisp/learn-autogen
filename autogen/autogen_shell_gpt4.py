## 跑起来了！： prunp autogen/autogen_shell_gpt4.py  "Execute the installation command npm install. If successful, it will return to npm list. If it fails, it will return to node -v."

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
        'model': 'gpt-4o',
        'api_key': os.environ.get('OPENAI_API_KEY'),
    }
]

# Create an AssistantAgent
assistant = autogen.AssistantAgent(
    name="Shell_Assistant",
    llm_config={
        "config_list": config_list,
        "temperature": 0,  # Set to 0 for more deterministic outputs
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

## -------------------------- log 
# (emacspy) 坚持去λ化(中-易) jim-emacs-fun-py  master @ prunp autogen/autogen_shell_gpt4.py  "Execute the installation command npm install. If successful, it will return
#  to npm list. If it fails, it will return to node -v."
# 
# User_Proxy (to Shell_Assistant):
# 
# Please help me with this task: Execute the installation command npm install. If successful, it will return to npm list. If it fails, it will return to node -v.. If you need to run a shell command, use the run_shell_command function.
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# Let's start by running the `npm install` command.
# 
# ```shell
# npm install
# ```
# 
# I will execute this command now.
# 
# --------------------------------------------------------------------------------
# 
# >>>>>>>> EXECUTING CODE BLOCK 0 (inferred language is shell)...
# User_Proxy (to Shell_Assistant):
# 
# exitcode: 254 (execution failed)
# Code output:
# npm error code ENOENT
# npm error syscall open
# npm error path /Users/emacspy/EmacsPyPro/jim-emacs-fun-py/coding/package.json
# npm error errno -2
# npm error enoent Could not read package.json: Error: ENOENT: no such file or directory, open '/Users/emacspy/EmacsPyPro/jim-emacs-fun-py/coding/package.json'
# npm error enoent This is related to npm not being able to find a file.
# npm error enoent
# npm error A complete log of this run can be found in: /Users/emacspy/.npm/_logs/2024-08-03T00_46_02_953Z-debug-0.log
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It looks like the `npm install` command failed because it couldn't find the `package.json` file in the specified directory. As per your instructions, I will now run the `node -v` command to check the Node.js version.
# 
# ```shell
# node -v
# ```
# 
# I will execute this command now.
# 
# --------------------------------------------------------------------------------
# 
# >>>>>>>> EXECUTING CODE BLOCK 0 (inferred language is shell)...
# User_Proxy (to Shell_Assistant):
# 
# exitcode: 0 (execution succeeded)
# Code output:
# v22.3.0
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# The `node -v` command executed successfully, and the Node.js version is `v22.3.0`.
# 
# To summarize:
# - The `npm install` command failed because the `package.json` file was not found.
# - The Node.js version installed on your system is `v22.3.0`.
# 
# If you need further assistance, such as creating a `package.json` file or troubleshooting the issue, please let me know!
# 
# --------------------------------------------------------------------------------
# User_Proxy (to Shell_Assistant):
# 
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It seems like you didn't provide any additional instructions. If you need further assistance, please let me know what you would like to do next. Here are a few options:
# 
# 1. **Create a `package.json` file**: If you need help creating a `package.json` file, I can guide you through the process.
# 2. **Troubleshoot the issue**: If you want to troubleshoot why the `package.json` file is missing, I can help with that.
# 3. **Run other commands**: If you have other commands you need to run, please specify them.
# 
# Let me know how you would like to proceed!
# 
# --------------------------------------------------------------------------------
# User_Proxy (to Shell_Assistant):
# 
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It seems like you haven't provided any additional instructions. If you need further assistance, please let me know what you would like to do next. Here are a few options:
# 
# 1. **Create a `package.json` file**: If you need help creating a `package.json` file, I can guide you through the process.
# 2. **Troubleshoot the issue**: If you want to troubleshoot why the `package.json` file is missing, I can help with that.
# 3. **Run other commands**: If you have other commands you need to run, please specify them.
# 
# Let me know how you would like to proceed!
# 
# --------------------------------------------------------------------------------
# User_Proxy (to Shell_Assistant):
# 
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It seems like you haven't provided any additional instructions. If you need further assistance, please let me know what you would like to do next. Here are a few options:
# 
# 1. **Create a `package.json` file**: If you need help creating a `package.json` file, I can guide you through the process.
# 2. **Troubleshoot the issue**: If you want to troubleshoot why the `package.json` file is missing, I can help with that.
# 3. **Run other commands**: If you have other commands you need to run, please specify them.
# 
# Let me know how you would like to proceed!
# 
# --------------------------------------------------------------------------------
# User_Proxy (to Shell_Assistant):
# 
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It seems like you haven't provided any additional instructions. If you need further assistance, please let me know what you would like to do next. Here are a few options:
# 
# 1. **Create a `package.json` file**: If you need help creating a `package.json` file, I can guide you through the process.
# 2. **Troubleshoot the issue**: If you want to troubleshoot why the `package.json` file is missing, I can help with that.
# 3. **Run other commands**: If you have other commands you need to run, please specify them.
# 
# Let me know how you would like to proceed!
# 
# --------------------------------------------------------------------------------
# User_Proxy (to Shell_Assistant):
# 
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It seems like you haven't provided any additional instructions. If you need further assistance, please let me know what you would like to do next. Here are a few options:
# 
# 1. **Create a `package.json` file**: If you need help creating a `package.json` file, I can guide you through the process.
# 2. **Troubleshoot the issue**: If you want to troubleshoot why the `package.json` file is missing, I can help with that.
# 3. **Run other commands**: If you have other commands you need to run, please specify them.
# 
# Let me know how you would like to proceed!
# 
# --------------------------------------------------------------------------------
# User_Proxy (to Shell_Assistant):
# 
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It seems like you haven't provided any additional instructions. If you need further assistance, please let me know what you would like to do next. Here are a few options:
# 
# 1. **Create a `package.json` file**: If you need help creating a `package.json` file, I can guide you through the process.
# 2. **Troubleshoot the issue**: If you want to troubleshoot why the `package.json` file is missing, I can help with that.
# 3. **Run other commands**: If you have other commands you need to run, please specify them.
# 
# Let me know how you would like to proceed!
# 
# --------------------------------------------------------------------------------
# User_Proxy (to Shell_Assistant):
# 
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It seems like you haven't provided any additional instructions. If you need further assistance, please let me know what you would like to do next. Here are a few options:
# 
# 1. **Create a `package.json` file**: If you need help creating a `package.json` file, I can guide you through the process.
# 2. **Troubleshoot the issue**: If you want to troubleshoot why the `package.json` file is missing, I can help with that.
# 3. **Run other commands**: If you have other commands you need to run, please specify them.
# 
# Let me know how you would like to proceed!
# 
# --------------------------------------------------------------------------------
# User_Proxy (to Shell_Assistant):
# 
# 
# 
# --------------------------------------------------------------------------------
# Shell_Assistant (to User_Proxy):
# 
# It seems like you haven't provided any additional instructions. If you need further assistance, please let me know what you would like to do next. Here are a few options:
# 
# 1. **Create a `package.json` file**: If you need help creating a `package.json` file, I can guide you through the process.
# 2. **Troubleshoot the issue**: If you want to troubleshoot why the `package.json` file is missing, I can help with that.
# 3. **Run other commands**: If you have other commands you need to run, please specify them.
# 
# Let me know how you would like to proceed!
# 
# --------------------------------------------------------------------------------
# (emacspy) 坚持去λ化(中-易) jim-emacs-fun-py  master @
# 
