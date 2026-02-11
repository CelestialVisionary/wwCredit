import os
import json
from langchain.chat_models import ChatOpenAI
from langchain.prompts import ChatPromptTemplate
from langchain.schema import StrOutputParser
from langgraph.graph import StateGraph, END
from langgraph.runtime import Graph

class FineTuner:
    def __init__(self):
        self.api_key = os.getenv('OPENAI_API_KEY')
        self.base_model = os.getenv('BASE_MODEL', 'gpt-3.5-turbo')
        self.fine_tuned_model_name = os.getenv('FINE_TUNED_MODEL_NAME', 'finance-assistant')
        self.epochs = int(os.getenv('EPOCHS', '3'))
        self.batch_size = int(os.getenv('BATCH_SIZE', '4'))
        self.learning_rate = float(os.getenv('LEARNING_RATE', '2e-5'))
    
    def create_model(self):
        """
        创建OpenAI模型实例
        """
        if not self.api_key:
            raise ValueError("OpenAI API key is required")
        
        model = ChatOpenAI(
            api_key=self.api_key,
            model=self.base_model,
            temperature=0.7
        )
        return model
    
    def create_prompt_template(self):
        """
        创建提示词模板
        """
        template = """
        你是一个专业的金融助手，负责为用户提供金融建议、风险评估和投资建议。请根据用户的具体情况，提供专业、准确、有针对性的回答。
        
        用户问题：
        {user_question}
        
        用户账户情况：
        {account_info}
        
        请为用户提供详细、专业、有针对性的回答，避免使用过于专业的术语，确保用户能够理解。
        """
        
        prompt = ChatPromptTemplate.from_template(template)
        return prompt
    
    def create_chain(self):
        """
        创建LangChain链
        """
        model = self.create_model()
        prompt = self.create_prompt_template()
        
        chain = prompt | model | StrOutputParser()
        return chain
    
    def create_workflow(self):
        """
        创建LangGraph工作流
        """
        model = self.create_model()
        
        # 定义状态
        class State:
            user_question: str
            account_info: str
            response: str
        
        # 定义节点
        def generate_response(state):
            prompt = ChatPromptTemplate.from_template(
                """
                你是一个专业的金融助手，负责为用户提供金融建议、风险评估和投资建议。请根据用户的具体情况，提供专业、准确、有针对性的回答。
                
                用户问题：
                {user_question}
                
                用户账户情况：
                {account_info}
                
                请为用户提供详细、专业、有针对性的回答，避免使用过于专业的术语，确保用户能够理解。
                """
            )
            
            response = (prompt | model | StrOutputParser()).invoke({
                "user_question": state.user_question,
                "account_info": state.account_info
            })
            
            return {"response": response}
        
        # 创建图
        workflow = StateGraph(State)
        workflow.add_node("generate", generate_response)
        workflow.set_entry_point("generate")
        workflow.add_edge("generate", END)
        
        # 编译图
        app = workflow.compile()
        return app
    
    def fine_tune(self, train_data):
        """
        微调模型
        """
        print(f"Starting fine-tuning with {len(train_data)} training examples")
        print(f"Base model: {self.base_model}")
        print(f"Fine-tuned model name: {self.fine_tuned_model_name}")
        print(f"Epochs: {self.epochs}")
        print(f"Batch size: {self.batch_size}")
        print(f"Learning rate: {self.learning_rate}")
        
        # 这里使用模拟的微调过程
        # 在实际应用中，需要使用OpenAI的微调API或其他微调方法
        
        print("Fine-tuning in progress...")
        
        # 模拟微调过程
        for epoch in range(self.epochs):
            print(f"Epoch {epoch + 1}/{self.epochs}")
            
            # 处理批量数据
            for i in range(0, len(train_data), self.batch_size):
                batch = train_data[i:i + self.batch_size]
                print(f"Processing batch {i // self.batch_size + 1}/{(len(train_data) + self.batch_size - 1) // self.batch_size}")
                
                # 这里可以添加实际的微调逻辑
                # 例如，使用OpenAI的微调API上传数据并开始微调
        
        print("Fine-tuning completed!")
        
        # 返回微调后的模型信息
        return {
            "base_model": self.base_model,
            "fine_tuned_model": self.fine_tuned_model_name,
            "status": "completed",
            "epochs": self.epochs,
            "training_examples": len(train_data)
        }
    
    def evaluate(self, val_data):
        """
        评估微调后的模型
        """
        print(f"Evaluating model with {len(val_data)} validation examples")
        
        # 创建工作流
        app = self.create_workflow()
        
        # 评估结果
        results = []
        
        for i, item in enumerate(val_data):
            print(f"Evaluating example {i + 1}/{len(val_data)}")
            
            # 提取用户问题和账户信息
            messages = item.get('messages', [])
            user_message = None
            account_info = ""
            
            for msg in messages:
                if msg.get('role') == 'user':
                    user_message = msg.get('content', '')
                    # 从用户消息中提取账户信息
                    if '用户账户情况' in user_message:
                        start = user_message.find('用户账户情况：')
                        end = user_message.find('\n\n用户的查询问题：')
                        if start != -1 and end != -1:
                            account_info = user_message[start:end]
            
            if user_message:
                # 运行工作流
                state = {
                    "user_question": user_message,
                    "account_info": account_info
                }
                
                try:
                    result = app.invoke(state)
                    results.append({
                        "input": state,
                        "output": result.get('response', '')
                    })
                except Exception as e:
                    print(f"Error evaluating example {i + 1}: {e}")
                    results.append({
                        "input": state,
                        "output": "Error generating response",
                        "error": str(e)
                    })
        
        print(f"Evaluation completed. Processed {len(results)} examples")
        return results

if __name__ == "__main__":
    from data_processor import DataProcessor
    
    # 加载数据
    processor = DataProcessor()
    train_data, val_data = processor.prepare_fine_tuning_data()
    
    if train_data:
        # 微调模型
        tuner = FineTuner()
        fine_tune_result = tuner.fine_tuned_result = tuner.fine_tune(train_data)
        print("Fine-tune result:", json.dumps(fine_tune_result, indent=2, ensure_ascii=False))
        
        # 评估模型
        if val_data:
            evaluation_result = tuner.evaluate(val_data)
            print(f"Evaluation completed. Results: {len(evaluation_result)} examples processed")
            
            # 保存评估结果
            with open('evaluation_results.json', 'w', encoding='utf-8') as f:
                json.dump(evaluation_result, f, indent=2, ensure_ascii=False)
            print("Evaluation results saved to evaluation_results.json")
    else:
        print("No training data available")
