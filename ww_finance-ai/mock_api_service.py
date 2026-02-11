#!/usr/bin/env python3
"""
模拟API服务，用于在依赖项未安装的情况下测试服务启动
"""

import os
import sys
import time
from http.server import HTTPServer, BaseHTTPRequestHandler
import json

class MockAPIHandler(BaseHTTPRequestHandler):
    """
    模拟API请求处理器
    """
    
    def do_GET(self):
        """
        处理GET请求
        """
        if self.path == '/':
            # 健康检查
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {
                "status": "healthy",
                "service": "Finance AI API (Mock)",
                "version": "1.0.0"
            }
            self.wfile.write(json.dumps(response).encode('utf-8'))
        elif self.path == '/api/status':
            # 状态检查
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {
                "status": "idle",
                "model": "gpt-3.5-turbo",
                "fine_tuned": False
            }
            self.wfile.write(json.dumps(response).encode('utf-8'))
        elif self.path == '/api/models':
            # 模型列表
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {
                "base_model": "gpt-3.5-turbo",
                "fine_tuned_model": None,
                "status": "available"
            }
            self.wfile.write(json.dumps(response).encode('utf-8'))
        elif self.path == '/api/evaluate':
            # 评估模型
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {
                "status": "success",
                "message": "Evaluation completed successfully",
                "results": []
            }
            self.wfile.write(json.dumps(response).encode('utf-8'))
        else:
            # 404
            self.send_response(404)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {
                "error": "Not found"
            }
            self.wfile.write(json.dumps(response).encode('utf-8'))
    
    def do_POST(self):
        """
        处理POST请求
        """
        if self.path == '/api/fine-tune':
            # 微调模型
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {
                "status": "success",
                "message": "Fine-tuning completed successfully",
                "result": {
                    "base_model": "gpt-3.5-turbo",
                    "fine_tuned_model": "finance-assistant",
                    "status": "completed",
                    "epochs": 3,
                    "training_examples": 0
                }
            }
            self.wfile.write(json.dumps(response).encode('utf-8'))
        elif self.path == '/api/inference':
            # 模型推理
            content_length = int(self.headers['Content-Length'])
            post_data = self.rfile.read(content_length)
            
            try:
                request_data = json.loads(post_data.decode('utf-8'))
                # 生成模拟响应
                user_question = request_data.get('user_question', '')
                account_info = request_data.get('account_info', '')
                
                response_text = f"模拟响应: 您的问题是 '{user_question[:50]}...'，账户信息是 '{account_info[:50]}...'"
                
                response = {
                    "response": response_text,
                    "status": "success",
                    "processing_time": 0.123
                }
            except json.JSONDecodeError:
                response = {
                    "response": "JSON解析错误，请检查请求格式",
                    "status": "error",
                    "processing_time": 0.001
                }
            except Exception as e:
                response = {
                    "response": f"处理请求时出错: {str(e)}",
                    "status": "error",
                    "processing_time": 0.001
                }
            
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            self.wfile.write(json.dumps(response).encode('utf-8'))
        else:
            # 404
            self.send_response(404)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            response = {
                "error": "Not found"
            }
            self.wfile.write(json.dumps(response).encode('utf-8'))

def run_mock_server():
    """
    运行模拟服务器
    """
    host = os.getenv('API_HOST', '0.0.0.0')
    port = int(os.getenv('API_PORT', '8000'))
    
    server = HTTPServer((host, port), MockAPIHandler)
    print(f"启动模拟Finance AI API服务 on {host}:{port}")
    print(f"健康检查: http://{host}:{port}/")
    print(f"模拟API已就绪，可以接收请求")
    
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        print("\n正在关闭服务器...")
        server.shutdown()

if __name__ == "__main__":
    run_mock_server()
