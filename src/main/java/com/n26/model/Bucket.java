package com.n26.model;

/**
 * Created by melihgurgah on 19/05/2017.
 */
public class Bucket {

    private long time;
    private double min;
    private double max;
    private double sum;
    private long count;
    private Bucket next;

    public Bucket(long time, double amount) {
        this.time = time;
        initializeBucket(amount);
    }

    private void initializeBucket(double amount){
        min = amount;
        max = amount;
        sum = amount;
        count = 1;
    }

    public void addData(double amount){
        if(count == 0){
           initializeBucket(amount);
        }
        else{
            if(amount > max){
                setMax(amount);
            }
            if(amount < min ){
                setMin(amount);
            }
            setSum(getSum() + amount);
            setCount(getCount() + 1);
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time / 1000;
    }

    public Bucket getNext() {
        return next;
    }

    public void setNext(Bucket next) {
        this.next = next;
    }
}
