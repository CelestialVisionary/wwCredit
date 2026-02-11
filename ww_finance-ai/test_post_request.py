#!/usr/bin/env python3
"""
测试发送POST请求到模拟API服务
"""

import http.client
import json

# 创建连接
conn = http.client.HTTPConnection("localhost", 8000)

# 准备请求数据
request_data = {
    "user_question": "我想了解如何理财",
    "account_info": "账户余额：10000元"
}

# 转换为JSON字符串
json_data = json.dumps(request_data)

# 设置请求头
headers = {
    "Content-Type": "application/json",
    "Content-Length": str(len(json_data))
}

# 发送POST请求
conn.request("POST", "/api/inference", json_data, headers)

# 获取响应
response = conn.getresponse()

# 打印响应状态
print(f"Status: {response.status} {response.reason}")

# 打印响应头
print("Headers:")
for key, value in response.getheaders():
    print(f"{key}: {value}")

# 打印响应内容
print("\nResponse:")
response_data = response.read()
print(response_data.decode("utf-8"))

# 关闭连接
conn.close()
