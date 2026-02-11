import json
import os

class DataProcessor:
    def __init__(self):
        self.data_dir = os.getenv('DATA_DIR', 'data')
        self.train_data_file = os.getenv('TRAIN_DATA_FILE', 'train_data.jsonl')
        self.val_data_file = os.getenv('VAL_DATA_FILE', 'val_data.jsonl')
    
    def load_data(self, file_name):
        """
        加载数据文件
        """
        file_path = os.path.join(self.data_dir, file_name)
        data = []
        
        try:
            with open(file_path, 'r', encoding='utf-8') as f:
                for line in f:
                    line = line.strip()
                    if line:
                        try:
                            item = json.loads(line)
                            data.append(item)
                        except json.JSONDecodeError as e:
                            print(f"Error decoding JSON line: {e}")
            print(f"Loaded {len(data)} items from {file_name}")
            return data
        except FileNotFoundError:
            print(f"File not found: {file_path}")
            return []
    
    def load_train_data(self):
        """
        加载训练数据
        """
        return self.load_data(self.train_data_file)
    
    def load_val_data(self):
        """
        加载验证数据
        """
        return self.load_data(self.val_data_file)
    
    def validate_data(self, data):
        """
        验证数据格式
        """
        valid_data = []
        invalid_count = 0
        
        for i, item in enumerate(data):
            if self._is_valid_item(item):
                valid_data.append(item)
            else:
                invalid_count += 1
                print(f"Invalid item at index {i}: {item}")
        
        print(f"Validated data: {len(valid_data)} valid, {invalid_count} invalid")
        return valid_data
    
    def _is_valid_item(self, item):
        """
        检查单个数据项是否有效
        """
        if not isinstance(item, dict):
            return False
        
        if 'messages' not in item:
            return False
        
        messages = item['messages']
        if not isinstance(messages, list) or len(messages) < 2:
            return False
        
        for msg in messages:
            if not isinstance(msg, dict):
                return False
            if 'role' not in msg or 'content' not in msg:
                return False
            if msg['role'] not in ['system', 'user', 'assistant']:
                return False
        
        return True
    
    def prepare_fine_tuning_data(self):
        """
        准备微调数据
        """
        # 加载训练数据
        train_data = self.load_train_data()
        valid_train_data = self.validate_data(train_data)
        
        # 加载验证数据
        val_data = self.load_val_data()
        valid_val_data = self.validate_data(val_data)
        
        return valid_train_data, valid_val_data

if __name__ == "__main__":
    processor = DataProcessor()
    train_data, val_data = processor.prepare_fine_tuning_data()
    print(f"Prepared data: {len(train_data)} train, {len(val_data)} validation")
