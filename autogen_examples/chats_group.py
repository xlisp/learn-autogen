# Import the autogen package
import autogen

# Configure the large language model
llm_config = {
    "config_list": [{"model": "gpt-4-turbo", "api_key": os.environ['OPENAI_API_KEY']}],
}

# Define the tasks for running a flower e-commerce business
inventory_tasks = [
    """Check the current inventory of various flowers and report which ones are low in stock.""",
    """Based on the past month's sales data, predict which flowers will be in higher demand in the coming month.""",
]

market_research_tasks = ["""Analyze market trends to identify the most popular types of flowers and possible reasons for their popularity."""]

content_creation_tasks = ["""Using the provided information, write an engaging blog post about the most popular flowers and tips for selecting them."""]

# Create Agent roles
inventory_assistant = autogen.AssistantAgent(
    name="Inventory Management Assistant",
    llm_config=llm_config,
)
market_research_assistant = autogen.AssistantAgent(
    name="Market Research Assistant",
    llm_config=llm_config,
)
content_creator = autogen.AssistantAgent(
    name="Content Creation Assistant",
    llm_config=llm_config,
    system_message="""
        You are a professional writer known for insightful and engaging articles.
        You can transform complex concepts into compelling narratives.
        When everything is complete, please reply with 'End'.
        """,
)

# Create User Proxy Agents
user_proxy_auto = autogen.UserProxyAgent(
    name="User Proxy_Auto",
    human_input_mode="NEVER",
    is_termination_msg=lambda x: x.get("content", "") and x.get("content", "").rstrip().endswith("End"),
    code_execution_config={
        "last_n_messages": 1,
        "work_dir": "tasks",
        "use_docker": False,
    },
)

user_proxy = autogen.UserProxyAgent(
    name="User Proxy",
    human_input_mode="ALWAYS",
    is_termination_msg=lambda x: x.get("content", "") and x.get("content", "").rstrip().endswith("End"),
    code_execution_config={
        "last_n_messages": 1,
        "work_dir": "tasks",
        "use_docker": False,
    },
)

# Initiate conversations
chat_results = autogen.initiate_chats(
    [
        {
            "sender": user_proxy_auto,
            "recipient": inventory_assistant,
            "message": inventory_tasks[0],
            "clear_history": True,
            "silent": False,
            "summary_method": "last_msg",
        },
        {
            "sender": user_proxy_auto,
            "recipient": market_research_assistant,
            "message": market_research_tasks[0],
            "max_turns": 2,
            "summary_method": "reflection_with_llm",
        },
        {
            "sender": user_proxy,
            "recipient": content_creator,
            "message": content_creation_tasks[0],
            "carryover": "I would like to include a data table or chart in the blog post.",
        },
    ]
)
