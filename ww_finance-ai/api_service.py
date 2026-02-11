import os
import json
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel

# 导入自定义模块
from data_processor import DataProcessor
from fine_tuner import FineTuner

# 创建FastAPI应用
app = FastAPI(
    title="Finance AI API",
    description="金融大模型微调与推理服务",
    version="1.0.0"
)

# 配置CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 在生产环境中应该设置具体的域名
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# 数据模型
class FineTuneRequest(BaseModel):
    """
    微调请求模型
    """
    epochs: int = 3
    batch_size: int = 4
    learning_rate: float = 2e-5

class InferenceRequest(BaseModel):
    """
    推理请求模型
    """
    user_question: str
    account_info: str

class InferenceResponse(BaseModel):
    """
    推理响应模型
    """
    response: str
    status: str
    processing_time: float

class FineTuneResponse(BaseModel):
    """
    微调响应模型
    """
    status: str
    message: str
    result: dict = None

class EvaluateResponse(BaseModel):
    """
    评估响应模型
    """
    status: str
    message: str
    results: list = None

class StatusResponse(BaseModel):
    """
    状态响应模型
    """
    status: str
    model: str
    fine_tuned: bool

# 全局变量
fine_tuned_model = None
fine_tune_status = "idle"

# 初始化数据处理器和微调器
processor = DataProcessor()
tuner = FineTuner()

@app.get("/", tags=["health"])
def health_check():
    """
    健康检查
    """
    return {
        "status": "healthy",
        "service": "Finance AI API",
        "version": "1.0.0"
    }

@app.post("/api/fine-tune", response_model=FineTuneResponse, tags=["fine-tune"])
def start_fine_tune(request: FineTuneRequest):
    """
    启动模型微调
    """
    global fine_tune_status, fine_tuned_model
    
    try:
        fine_tune_status = "in_progress"
        
        # 加载数据
        train_data, val_data = processor.prepare_fine_tuning_data()
        
        if not train_data:
            fine_tune_status = "idle"
            return FineTuneResponse(
                status="error",
                message="No training data available"
            )
        
        # 设置微调参数
        tuner.epochs = request.epochs
        tuner.batch_size = request.batch_size
        tuner.learning_rate = request.learning_rate
        
        # 开始微调
        result = tuner.fine_tune(train_data)
        fine_tuned_model = result
        fine_tune_status = "completed"
        
        return FineTuneResponse(
            status="success",
            message="Fine-tuning completed successfully",
            result=result
        )
    except Exception as e:
        fine_tune_status = "error"
        return FineTuneResponse(
            status="error",
            message=f"Error during fine-tuning: {str(e)}"
        )

@app.get("/api/evaluate", response_model=EvaluateResponse, tags=["evaluate"])
def evaluate_model():
    """
    评估模型
    """
    try:
        # 加载验证数据
        _, val_data = processor.prepare_fine_tuning_data()
        
        if not val_data:
            return EvaluateResponse(
                status="error",
                message="No validation data available"
            )
        
        # 评估模型
        results = tuner.evaluate(val_data)
        
        return EvaluateResponse(
            status="success",
            message="Evaluation completed successfully",
            results=results
        )
    except Exception as e:
        return EvaluateResponse(
            status="error",
            message=f"Error during evaluation: {str(e)}"
        )

@app.post("/api/inference", response_model=InferenceResponse, tags=["inference"])
def inference(request: InferenceRequest):
    """
    模型推理
    """
    import time
    start_time = time.time()
    
    try:
        # 创建工作流
        app_workflow = tuner.create_workflow()
        
        # 运行推理
        state = {
            "user_question": request.user_question,
            "account_info": request.account_info
        }
        
        result = app_workflow.invoke(state)
        response = result.get('response', '')
        
        processing_time = time.time() - start_time
        
        return InferenceResponse(
            response=response,
            status="success",
            processing_time=processing_time
        )
    except Exception as e:
        processing_time = time.time() - start_time
        return InferenceResponse(
            response=f"Error during inference: {str(e)}",
            status="error",
            processing_time=processing_time
        )

@app.get("/api/status", response_model=StatusResponse, tags=["status"])
def get_status():
    """
    获取模型状态
    """
    global fine_tuned_model, fine_tune_status
    
    return StatusResponse(
        status=fine_tune_status,
        model=tuner.fine_tuned_model_name if fine_tuned_model else tuner.base_model,
        fine_tuned=fine_tuned_model is not None
    )

@app.get("/api/models", tags=["models"])
def get_models():
    """
    获取可用模型列表
    """
    return {
        "base_model": tuner.base_model,
        "fine_tuned_model": tuner.fine_tuned_model_name if fine_tuned_model else None,
        "status": "available"
    }

if __name__ == "__main__":
    import uvicorn
    
    host = os.getenv('API_HOST', '0.0.0.0')
    port = int(os.getenv('API_PORT', '8000'))
    
    print(f"Starting Finance AI API on {host}:{port}")
    print(f"Health check: http://{host}:{port}/")
    print(f"API documentation: http://{host}:{port}/docs")
    
    uvicorn.run(app, host=host, port=port)
