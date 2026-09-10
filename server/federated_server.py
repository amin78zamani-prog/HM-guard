import numpy as np
import json

class FederatedServer:
    def __init__(self):
        self.global_weights = np.array([0.0, 0.0, 0.0])
        self.client_updates = []

    def receive_client_update(self, client_id, weights, is_malicious_suspected=False):
        weights_array = np.array(weights)
        
        # تشخیص ریسک حمله مسموم‌سازی مدل (Model Poisoning Detection)
        if self.detect_anomaly(weights_array):
            print(f"هشدار امنیتی: ترافیک یا وزن‌های ارسالی از کلاینت {client_id} به عنوان حمله شناسایی و ایزوله شد!")
            return False

        self.client_updates.append(weights_array)
        print(f"بروزرسانی از کلاینت {client_id} با موفقیت ثبت شد.")
        return True

    def detect_anomaly(self, weights_array):
        # بررسی ساده انحراف معیار بزرگ یا مقادیر نامتعارف به عنوان نشانه حمله مسموم‌سازی
        if np.any(np.abs(weights_array) > 5.0):
            return True
        return False

    def aggregate_weights(self):
        if not self.client_updates:
            return
        # تجمیع فدرال (FedAvg ساده)
        self.global_weights = np.mean(self.client_updates, axis=0)
        self.client_updates.clear()
        print(f"مدل سراسری آپدیت شد. وزن‌های جدید: {self.global_weights}")

# تست نمونه سرور
if __name__ == "__main__":
    server = FederatedServer()
    
    # شبیه‌سازی دریافت وزن سالم از اپلیکیشن H.M guard
    normal_weights = [0.05, 0.12, -0.02]
    server.receive_client_update("Client_Node_01", normal_weights)
    
    # شبیه‌سازی دریافت وزن مخرب (حمله مسموم‌سازی)
    poisoned_weights = [-9.5, 8.8, -7.5]
    server.receive_client_update("Client_Attacker_01", poisoned_weights)
    
    server.aggregate_weights()
