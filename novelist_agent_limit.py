import autogen
import requests
import json
import os

# OpenRouter API configuration
OPENROUTER_API_KEY = os.environ['OPENROUTER_API_KEY']

class OpenRouterLLM:
    def __init__(self, model="openai/gpt-4o-2024-08-06"):
        self.model = model
        
    def create_completion(self, messages):
        response = requests.post(
            url="https://openrouter.ai/api/v1/chat/completions",
            headers={
                "Authorization": f"Bearer {OPENROUTER_API_KEY}"
            },
            data=json.dumps({
                "model": self.model,
                "messages": messages,
                "max_tokens": 4000,
                "temperature": 0.7,
            })
        )
        return response.json()

llm = OpenRouterLLM()

story_planner_config = {
    "name": "StoryPlanner",
    "system_message": """You are a story planner specialized in game-related narratives.
    Your responsibilities:
    - Create detailed chapter outlines (at least 15 chapters)
    - Define key story beats and progression
    - Ensure each chapter contributes at least 700 words to reach the 10,000+ word goal
    - Coordinate with other agents to maintain story consistency
    - Track word count and story progress"""
}

character_developer_config = {
    "name": "CharacterDeveloper",
    "system_message": """You are a character development specialist.
    Your responsibilities:
    - Create detailed character profiles with backstories
    - Define complex character relationships and dynamics
    - Ensure character development spans the entire novel
    - Provide character details that support 10,000+ words of content"""
}

narrative_writer_config = {
    "name": "NarrativeWriter",
    "system_message": """You are a narrative writer focusing on game-related stories.
    Your responsibilities:
    - Write detailed, engaging prose and dialogue
    - Maintain consistent pacing and detailed descriptions
    - Ensure each chapter is sufficiently detailed (700+ words)
    - Track word count to meet the 10,000+ word goal
    - Use rich descriptions and well-developed scenes"""
}

class NovelProgress:
    def __init__(self):
        self.total_words = 0
        self.chapters = []
        self.target_words = 10000
        
    def add_content(self, content):
        words = len(content.split())
        self.total_words += words
        self.chapters.append({
            'content': content,
            'word_count': words
        })
        
    def is_complete(self):
        return self.total_words >= self.target_words
    
    def get_progress(self):
        return f"Current progress: {self.total_words}/{self.target_words} words ({len(self.chapters)} chapters)"

novel_progress = NovelProgress()

def create_messages(system_message, user_message):
    return [
        {"role": "system", "content": system_message},
        {"role": "user", "content": f"{user_message}\n\nCurrent progress: {novel_progress.get_progress()}"}
    ]

class CustomAssistantAgent(autogen.AssistantAgent):
    def generate_reply(self, sender=None, messages=None):
        if messages is None:
            messages = self._oai_messages
            
        if not messages:
            return None
            
        system_message = self.system_message
        
        last_message = messages[-1]
        if isinstance(last_message, dict):
            last_message_content = last_message.get('content', '')
        else:
            last_message_content = str(last_message)
        
        api_messages = create_messages(system_message, last_message_content)
        response = llm.create_completion(api_messages)
        
        try:
            reply = response['choices'][0]['message']['content']
            if self.name == "narrative_writer":
                novel_progress.add_content(reply)
            return reply
        except KeyError:
            return "I apologize, but I encountered an error processing your request."

story_planner = CustomAssistantAgent(
    name="story_planner",
    system_message=story_planner_config["system_message"]
)

character_developer = CustomAssistantAgent(
    name="character_developer",
    system_message=character_developer_config["system_message"]
)

narrative_writer = CustomAssistantAgent(
    name="narrative_writer",
    system_message=narrative_writer_config["system_message"]
)

user_proxy = autogen.UserProxyAgent(
    name="user_proxy",
    human_input_mode="TERMINATE",
    max_consecutive_auto_reply=10
)

groupchat = autogen.GroupChat(
    agents=[user_proxy, story_planner, character_developer, narrative_writer],
    messages=[],
    max_round=50
)

manager = autogen.GroupChatManager(groupchat=groupchat)

initial_message = """Let's write a novel about a professional esports player's journey.
Requirements:
1. The novel must be at least 10,000 words
2. Include detailed character development
3. Each chapter should be at least 700 words
4. Include rich descriptions and well-developed scenes
5. Cover the character's complete journey from amateur to professional

Please start by creating a detailed outline and character profile."""

# Continue generating content until word count is met
while not novel_progress.is_complete():
    if len(novel_progress.chapters) == 0:
        user_proxy.initiate_chat(manager, message=initial_message)
    else:
        next_chapter_prompt = f"""Continue writing the next chapter. 
Current progress: {novel_progress.get_progress()}
Make this chapter at least 700 words with rich details and character development."""
        user_proxy.initiate_chat(manager, message=next_chapter_prompt)

print(f"\nFinal novel statistics:\n{novel_progress.get_progress()}")

