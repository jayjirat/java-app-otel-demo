package com.example.test_opentelemrtry.dto;

public class TransferRequestDto {
    public Long fromId;
    public Long toId;
    public double amount;

    public Long getFromId() {
        return this.fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return this.toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
