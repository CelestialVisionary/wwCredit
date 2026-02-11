#!/usr/bin/env python3
"""
启动Python微调服务的脚本
"""

import os
import sys
import json

# 检查是否安装了必要的依赖
try:
    from fastapi import FastAPI
    from fastapi.middleware.cors import CORSMiddleware
    from pydantic import BaseModel
    from langchain.chat_models import ChatOpenAI
    from langchain.prompts import ChatPromptTemplate
    from langchain.schema import StrOutputParser
    from langgraph.graph import StateGraph, END
    print("所有依赖已安装，启动服务...")
except ImportError as e:
    print(f"依赖未安装: {e}")
    print("正在以模拟模式启动服务...")
    # 继续运行，使用模拟实现
    pass

# 启动API服务
if __name__ == "__main__":
    try:
        import uvicorn
        from api_service import app
        
        host = os.getenv('API_HOST', '0.0.0.0')
        port = int(os.getenv('API_PORT', '8000'))
        
        print(f"启动Finance AI API on {host}:{port}")
        print(f"Health check: http://{host}:{port}/")
        print(f"API documentation: http://{host}:{port}/docs")
        
        uvicorn.run(app, host=host, port=port)
    except ImportError as e:
        print(f"无法导入必要的模块: {e}")
        print("启动模拟API服务...")
        # 启动模拟服务器
        from mock_api_service import run_mock_server
        run_mock_server()
    except Exception as e:
        print(f"启动服务失败: {e}")
        print("启动模拟API服务...")
        # 启动模拟服务器
        from mock_api_service import run_mock_server
        run_mock_server()
