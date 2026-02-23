from http.server import BaseHTTPRequestHandler, HTTPServer
import json


class SimpleHTTPRequestHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        # 获取请求头中的 Content-Length
        content_length = int(self.headers['Content-Length'])

        # 读取请求体
        post_data = self.rfile.read(content_length)

        # 将请求体解码为字符串
        post_data_str = post_data.decode('utf-8')

        # 打印请求内容到控制台
        print("Received POST data:")
        print(post_data_str)

        # 发送响应
        self.send_response(200)
        self.send_header('Content-type', 'text/plain; charset=UTF-8')
        self.end_headers()
        self.wfile.write(b'Received your message')


def run(server_class=HTTPServer, handler_class=SimpleHTTPRequestHandler, port=6666):
    server_address = ('127.0.0.1', port)
    httpd = server_class(server_address, handler_class)
    print(f'Starting httpd server on {port}...')
    httpd.serve_forever()


if __name__ == '__main__':
    run()