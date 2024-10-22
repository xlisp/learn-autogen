import pyautogui
import cv2
import numpy as np
import easyocr
import time
import sys

reader = easyocr.Reader(["ch_sim", "en"])  # for Simplified Chinese and English

def get_coordinates_by_content(search_text, start_x, start_y, end_x, end_y):
    # Take a screenshot of the specified region
    screenshot = pyautogui.screenshot(
        region=(start_x, start_y, end_x - start_x, end_y - start_y)
    )
    screenshot = cv2.cvtColor(np.array(screenshot), cv2.COLOR_RGB2BGR)

    # Perform OCR on the screenshot
    results = reader.readtext(screenshot)

    # Find the match for the search text
    find_distance = list(filter(lambda item: search_text in item[1], results))
    
    # If a match is found, return the coordinates of the first match
    if find_distance:
        # The first match coordinates
        box = find_distance[0][0]  # [[x1, y1], [x2, y2], [x3, y3], [x4, y4]]
        # Calculate the center point of the box
        center_x = (box[0][0] + box[1][0]) // 2 + start_x
        center_y = (box[0][1] + box[3][1]) // 2 + start_y
        return center_x, center_y
    return None

search_text = sys.argv[1]  # The Chinese text you want to search for
start_x, start_y = 0, 219  # Starting coordinates of the region to search
end_x, end_y = 619, 453  # Ending coordinates of the region to search

# Get the coordinates of the first match
coordinates = get_coordinates_by_content(search_text, start_x, start_y, end_x, end_y)

if coordinates:
    print(f"Clicking at: {coordinates}")
    # Perform a click at the coordinates
    pyautogui.click(coordinates[0], coordinates[1])
    time.sleep(1)
    pyautogui.click(coordinates[0], coordinates[1])
else:
    print("Text not found.")

