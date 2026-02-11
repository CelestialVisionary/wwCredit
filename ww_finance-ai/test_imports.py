#!/usr/bin/env python3
"""
测试导入必要的模块
"""

import sys

print(f"Python版本: {sys.version}")
print(f"Python路径: {sys.path}")

# 尝试导入必要的模块
try:
    from dotenv import load_dotenv
    print("✓ 成功导入 dotenv")
except ImportError as e:
    print(f"✗ 导入 dotenv 失败: {e}")

try:
    from fastapi import FastAPI
    print("✓ 成功导入 fastapi")
except ImportError as e:
    print(f"✗ 导入 fastapi 失败: {e}")

try:
    from pydantic import BaseModel
    print("✓ 成功导入 pydantic")
except ImportError as e:
    print(f"✗ 导入 pydantic 失败: {e}")

try:
    from langchain.chat_models import ChatOpenAI
    print("✓ 成功导入 langchain")
except ImportError as e:
    print(f"✗ 导入 langchain 失败: {e}")

try:
    from langgraph.graph import StateGraph, END
    print("✓ 成功导入 langgraph")
except ImportError as e:
    print(f"✗ 导入 langgraph 失败: {e}")

print("测试完成")
