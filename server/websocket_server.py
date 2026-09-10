import asyncio
import json
import numpy as np
import websockets

class FederatedWebSocketServer:
    def __init__(self):
        self.global_weights = np.array([0.0, 0.0, 0.0])
        self.client_updates = []

    def detect_anomaly(self, weights_array):
        # بررسی انحراف غیرمتعارف برای شناسایی حملات مسموم‌سازی مدل (Model Poisoning)
        if np.any(np.abs(weights_array) > 5.0):
            return True
        return False

    async def handle_client(self, websocket):
        try:
            async for message in websocket:
                data = json.loads(message)
                client_id = data.get("client_id", "Unknown")
                weights = data.get("weights", [])
                
                weights_array = np.array(weights).flatten()

                if self.detect_anomaly(weights_array):
                    print(f"[هشدار امنیتی] حمله مسموم‌سازی از کلاینت {client_id} شناسایی شد!")
                    await websocket.send(json.dumps({
                        "status": "rejected",
                        "message": "Security Alert: Malicious weights detected and isolated."
                    }))
                    continue

                self.client_updates.append(weights_array)
                print(f"[موفقیت] به‌روزرسانی از کلاینت {client_id} ثبت شد.")

                # تجمیع ساده فدرال در صورت دریافت کافی به‌روزرسانی‌ها
                if len(self.client_updates) >= 2:
                    self.global_weights = np.mean(self.client_updates, axis=0)
                    self.client_updates.clear()
                    print(f"[تکمیل تجمیع] مدل سراسری به‌روز شد: {self.global_weights.tolist()}")

                await websocket.send(json.dumps({
                    "status": "accepted",
                    "message": "Weights successfully integrated into federated model."
                }))
                
        except websockets.exceptions.ConnectionClosed:
            print("[اتصال قطع شد] ارتباط کلاینت با سرور قطع گردید.")

async def main():
    server = FederatedWebSocketServer()
    async with websockets.serve(server.handle_client, "0.0.0.0", 8080):
        print("سرور فدرال H.M guard روی پورت 8080 فعال شد...")
        await asyncio.Future()  # اجرای مداوم سرور

if __name__ == "__main__":
    asyncio.run(main())
