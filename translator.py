
import autogen
from googletrans import Translator

# 配置 AI 助手
config_list = [
    {
        "model": "gpt-4",
        "api_key": "your-api-key-here"
    }
]

# 创建 Agent
user_proxy = autogen.UserProxyAgent(
    name="User_proxy",
    system_message="A human user who needs translation services.",
    human_input_mode="TERMINATE",
    max_consecutive_auto_reply=10,
)

translator_agent = autogen.AssistantAgent(
    name="Translator",
    llm_config={
        "config_list": config_list,
    },
    system_message="You are a translator agent. You use Google Translate to translate text.",
)

polisher_agent = autogen.AssistantAgent(
    name="Polisher",
    llm_config={
        "config_list": config_list,
    },
    system_message="You are a language polisher. You improve the quality and fluency of translations.",
)

custom_agent = autogen.AssistantAgent(
    name="Customizer",
    llm_config={
        "config_list": config_list,
    },
    system_message="You are a customization agent. You adapt translations to user preferences and habits.",
)

# 谷歌翻译函数
def google_translate(text, target_lang='en'):
    translator = Translator()
    result = translator.translate(text, dest=target_lang)
    return result.text

# 翻译流程
def translation_process(text, target_lang='en'):
    # 步骤1: 谷歌翻译
    translated_text = google_translate(text, target_lang)
    
    # 步骤2: 润色
    polisher_agent.initiate_chat(
        translator_agent,
        message=f"Please polish this translation: {translated_text}"
    )
    polished_text = polisher_agent.last_message()["content"]
    
    # 步骤3: 用户习惯修改
    custom_agent.initiate_chat(
        polisher_agent,
        message=f"Please customize this translation based on user preferences: {polished_text}"
    )
    final_text = custom_agent.last_message()["content"]
    
    return final_text

def main():
    user_proxy.initiate_chat(
        translator_agent,
        message="Translate the following text to English: '你好,世界!'"
    )
    
    original_text = "你好,世界!"
    result = translation_process(original_text)
    
    print(f"Original: {original_text}")
    print(f"Final translation: {result}")

if __name__ == "__main__":
    main()

