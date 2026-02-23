import requests




# 按装订区域中的绿色按钮以运行脚本。
if __name__ == '__main__':
    json = {
        "name": "xxx"
    }

    url = "http://192.168.21.132:8078/json"

    proxies = {
        "http": "http://127.0.0.1:8020",
        "https": "http://127.0.0.1:8020"
    }

    resp = requests.post(url, json=json, proxies=proxies)
    print(resp.text)
