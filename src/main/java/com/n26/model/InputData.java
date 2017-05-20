package com.n26.model;

/**
 * Created by melihgurgah on 19/05/2017.
 */
public class InputData {

    private Double amount;
    private Long timestamp;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp / 1000;
    }

    @Override
    public String toString() {
        return "amount=" + amount +
                ", timestamp=" + timestamp + '}';
    }
}
