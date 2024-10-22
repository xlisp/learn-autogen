import pyautogui
import time
import easyocr
import numpy as np

reader = easyocr.Reader(['ch_sim', 'en'])  # 'ch_sim' for Simplified Chinese


def get_screen_text(region):
    screenshot = pyautogui.screenshot(region=region)
    screenshot_np = np.array(screenshot)
    results = reader.readtext(screenshot_np)
    return ' '.join([text for _, text, _ in results])

capture_region = (0, 219, 619, 453)  # Adjust these values as needed
text = get_screen_text(capture_region)

print(text)

