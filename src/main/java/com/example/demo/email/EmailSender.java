package com.example.demo.email;

public interface EmailSender {
    void send(String receiverAddress, String message);
}
