
import requests
import json

url = "http://localhost:4000/v1/chat/completions"
headers = {"Content-Type": "application/json"}
data = {
    "model": "your_model_name",
    "messages": [{"role": "user", "content": "Write python code: for each print 1..10"}]
}

response = requests.post(url, headers=headers, data=json.dumps(data))
print(response.status_code)
print(response.json())

#=> {'id': 'chatcmpl-d1d5ed1e-cc8f-42f6-836c-8ed6bb14fe80', 'choices': [{'finish_reason': 'stop', 'index': 0, 'message': {'content': 'Here is the Python code that prints numbers from 1 to 10:\n\n```python\nfor i in range(1, 11):\n    print(i)\n```\n\nThis will output:\n```\n1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n```', 'role': 'assistant'}}], 'created': 1722655586, 'model': 'ollama/llama3.1', 'object': 'chat.completion', 'system_fingerprint': None, 'usage': {'prompt_tokens': 20, 'completion_tokens': 62, 'total_tokens': 82}}

