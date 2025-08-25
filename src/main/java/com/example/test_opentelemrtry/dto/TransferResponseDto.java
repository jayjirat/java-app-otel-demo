package com.example.test_opentelemrtry.dto;

import java.util.Optional;

public class TransferResponseDto {
    public String status;
    public String message;
    public Optional<String> transactionId;

    public Optional<String> getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = Optional.ofNullable(transactionId);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
