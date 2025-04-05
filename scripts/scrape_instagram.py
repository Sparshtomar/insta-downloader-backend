import sys
import json
from playwright.sync_api import sync_playwright

def scrape_instagram_reel_video(url):
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        context = browser.new_context()
        page = context.new_page()
        page.goto(url, timeout=60000)

        try:
            page.wait_for_selector("video", timeout=10000)
            video_element = page.query_selector("video")
            video_url = video_element.get_attribute("src")
            if video_url:
                print(video_url)
            else:
                print("⚠️ Video URL not found.")
        except Exception as e:
            print("Error:", e)
        browser.close()

if __name__ == "__main__":
    data = json.loads(sys.argv[1])
    scrape_instagram_reel_video(data["url"])
