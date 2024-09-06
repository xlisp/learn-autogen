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

## ----- Run --------
#  @ prunp autogen/summarize_single_task.py
# Skipping virtualenv creation, as specified in config file.
# /opt/anaconda3/lib/python3.11/site-packages/pydantic/_internal/_config.py:341: UserWarning: Valid config keys have changed in V2:
# * 'allow_population_by_field_name' has been renamed to 'populate_by_name'
# * 'smart_union' has been removed
#   warnings.warn(message, UserWarning)
# [autogen.oai.client: 09-06 16:18:22] {164} WARNING - The API key specified is not a valid OpenAI format; it won't work with the OpenAI-hosted model.
# [autogen.oai.client: 09-06 16:18:22] {164} WARNING - The API key specified is not a valid OpenAI format; it won't work with the OpenAI-hosted model.
# user_proxy (to assistant):
# 
# 
# I Give me a summary of this article: https://apnews.com/article/school-shootings-talk-with-kids-cd21c1445cb6cfd89ed9184f030530ff
# 
# 
# --------------------------------------------------------------------------------
# assistant (to user_proxy):
# 
# To provide a summary of the article, I will first need to fetch its content. I will use Python to download and print the content of the webpage.
# 
# ```python
# import requests
# from bs4 import BeautifulSoup
# 
# # URL of the article
# url = "https://apnews.com/article/school-shootings-talk-with-kids-cd21c1445cb6cfd89ed9184f030530ff"
# 
# # Fetch the content of the webpage
# response = requests.get(url)
# if response.status_code == 200:
#     # Parse the content using BeautifulSoup
#     soup = BeautifulSoup(response.content, 'html.parser')
# 
#     # Extract the main content of the article
#     article_content = soup.find_all('p')
#     article_text = "\n".join([paragraph.get_text() for paragraph in article_content])
# 
#     # Print the content of the article
#     print(article_text)
# else:
#     print(f"Failed to retrieve the article. Status code: {response.status_code}")
# ```
# 
# Please execute the above code to fetch and print the content of the article. Once you provide the content, I will summarize it for you.
# 
# --------------------------------------------------------------------------------
# 
# >>>>>>>> USING AUTO REPLY...
# 
# >>>>>>>> EXECUTING CODE BLOCK 0 (inferred language is python)...
# user_proxy (to assistant):
# 
# exitcode: 0 (execution succeeded)
# Code output:
# Copyright 2024 The Associated Press. All Rights Reserved.
# Mourners pray during a candlelight vigil for the slain students and teachers at Apalachee High School, Wednesday, Sept. 4, 2024, in Winder, Ga. (AP Photo/Mike Stewart)
# Mourners pray during a candlelight vigil for the slain students and teachers at Apalachee High School, Wednesday, Sept. 4, 2024, in Winder, Ga. (AP Photo/Mike Stewart)
# Chimain Douglas, of Grayson, Ga., kneels near Apalachee High School as she mourns for the slain students and teachers on Thursday, Sept. 5, 2024, in Winder, Ga. (AP Photo/Brynn Anderson)
# 
# 
# 
# 
# 
# 
# The U.S. is dealing with another school shooting: Two students and two teachers were killed Wednesday at a school in Georgia. At least nine other people — eight students and one teacher — were taken to hospitals with injuries.
# The effects of a shooting on a community are felt long after the day’s tragedy. But a shooting like the most recent one in Winder, Georgia, can have physical, emotional and behavioral effects on all kids.
# Many health experts, including psychologists and grief counselors, are reminding people there are resources to support students’ mental and emotional health as they grieve and process.
# Here’s how they say families should address traumatic experiences with their kids.
# It takes time to process emotions, regardless of age, so adults should start by taking care of themselves. That said, experts encourage parents to have conversations with their children and not avoid the topic, if kids indicate a willingness to talk about it.
# “If they are not hearing about it from you as their parent, they will hear about it from their friends at school,” says Emilie Ney, director of professional development at the National Association of School Psychologists.
# It’s OK for caregivers to say they don’t have all the answers and not force the conversation, according to guidance from the National Child Traumatic Stress Network. Being available and patient is key.
# This article is part of AP’s Be Well coverage, focusing on wellness, fitness, diet and mental health. Read more Be Well.
# This isn’t just a job for parents and guardians. All adults should remember to be available for the kids in their life. After all, not all children have trusted adults they can speak with, said Crystal Garrant, chief program officer at Sandy Hook Promise, a nonprofit group that works to prevent suicides and mass shootings.
# 
# 
# For instance, she said, adults who work in before-school or after-school programs should ask the kids in their care open-ended questions, do community-building activities or provide kids with other opportunities to share openly. They may not have the opportunity to do so otherwise.
# It depends on the child, but development levels will vary in the way children are able to understand a situation, Ney said.
# “There is no specific age target for these conversations,” said Garrant, who has a 9-year-old daughter. “But make sure that younger children understand the word that you’re using. When we say safety, what does it mean to feel safe? How does it feel in your body? What does it sound like when you’re not safe?”
# Some children may have emotional and behavioral responses to traumatic events, such as anxiety, nightmares or difficulty concentrating.
# Younger children need simple information and reassurances their schools and homes are safe, guidance from the National Association of School Psychologists notes. Older children have a deeper capacity for understanding and could benefit from hearing about what agency they might have to keep themselves safe.
# Recognizing, acknowledging and validating children’s emotions are key, said Beverly Warnock, executive director of the National Organization of Parents of Murdered Children based in Cincinnati.
# “You need to get those feelings out and be honest,” she said. “Don’t try to squash the feelings or not talk about it. It’s something that will be with you for the rest of your life.”
# The process of navigating emotions after a shooting can be confusing and frustrating for people, Ney said.
# “The stages of grief are not necessarily sequential. People may go in and out of the various different phases, and it may be that it doesn’t really hit someone until a week later,” Ney said.
# Psychologists hope to reassure people their feelings are normal and they don’t have to pretend they are unaffected.
# “Even if you didn’t know anyone involved, even if they were very far away from you, it is okay to grieve,” Ney said. “It shows that you care about others.”
# After acknowledging the emotional response, Warnock said, there is comfort in knowing life goes on.
# “You will find a coping skill, and you will be able to enjoy life again,” she said. “You may not feel that way now, but it does happen. It’s just going to take some time.”
# If you or someone you know are experiencing distress because of a mass shooting, you can call the 24/7 National Disaster Distress Helpline. The number is 1-800-985-5990, and Spanish speakers can press “2” for bilingual support. To connect directly to a crisis counselor in American Sign Language, call 1-800-985-5990 from your videophone.
# The Associated Press’ education coverage receives financial support from multiple private foundations. AP is solely responsible for all content. Find AP’s standards for working with philanthropies, a list of supporters and funded coverage areas at AP.org.
# Copyright 2024 The Associated Press. All Rights Reserved.
# 
# 
# --------------------------------------------------------------------------------
# assistant (to user_proxy):
# 
# ### Summary of the Article
# 
# The article discusses the aftermath of a recent school shooting in Winder, Georgia, where two students and two teachers were killed, and at least nine others were injured. The piece emphasizes the long-term physical, emotional, and behavioral effects such tragedies have on children and communities.
# 
# Health experts, including psychologists and grief counselors, stress the importance of addressing these traumatic experiences with children. They advise parents and caregivers to engage in open conversations with their kids, even if they don't have all the answers, and to be patient and available. The article highlights that all adults, not just parents, should be supportive and available for children, especially those who may not have trusted adults to talk to.
# 
# The article also provides guidance on how to talk to children of different ages about such events, noting that younger children need simple reassurances about safety, while older children can handle more complex discussions about their agency in staying safe. Recognizing and validating children's emotions is crucial, and adults are encouraged to help children express their feelings honestly.
# 
# The piece concludes with a message of hope, reassuring that while the grieving process is complex and non-linear, people will eventually find coping mechanisms and be able to enjoy life again. It also provides information on the National Disaster Distress Helpline for those experiencing distress due to mass shootings.
# 
# TERMINATE
# 
# --------------------------------------------------------------------------------
# Please give feedback to assistant. Press enter or type 'exit' to stop the conversation: exit
# 
