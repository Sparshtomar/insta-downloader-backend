import json
import sys
from playwright.sync_api import sync_playwright

def get_video_url(page, url):
    try:
        page.goto(url, timeout=30000)
        page.wait_for_selector("video", timeout=30000)
        video = page.query_selector("video")
        return video.get_attribute("src") if video else None
    except Exception:
        return None

def main():
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        context = browser.new_context(
            user_agent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36"
        )
        context.route("**/*", lambda route: route.abort() if route.request.resource_type in ["image", "font", "stylesheet"] else route.continue_())
        page = context.new_page()

        print("ready")
        sys.stdout.flush()

        for line in sys.stdin:
            try:
                data = json.loads(line.strip())
                url = data.get("url")
                if not url:
                    print("⚠️ No URL provided")
                    sys.stdout.flush()
                    continue

                print(f"🎯 Fetching video from: {url}")

                video_url = get_video_url(page, url)

                if not video_url:
                    page.reload()
                    video_url = get_video_url(page, url)

                print(video_url if video_url else "⚠️ Video not found")
            except Exception as e:
                print("❌ Error: " + str(e))
            sys.stdout.flush()

if __name__ == "__main__":
    main()
