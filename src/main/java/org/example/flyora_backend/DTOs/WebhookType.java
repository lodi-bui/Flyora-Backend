package org.example.flyora_backend.DTOs;

public class WebhookType {
    // Thêm các trường phù hợp với dữ liệu webhook bạn nhận được
    // Ví dụ:
    private String event;
    private String data;

    public WebhookType() {}

    public String getEvent() {
        return event;
    }
    public void setEvent(String event) {
        this.event = event;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}

