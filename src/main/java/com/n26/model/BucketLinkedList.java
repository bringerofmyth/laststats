package com.n26.model;

import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

/**
 * Created by melihgurgah on 19/05/2017.
 */

@Service
public class BucketLinkedList {

    //store only last 60 buckets
    private final int MAX_SIZE = 60;
    private volatile Bucket root = null;
    private final static Logger LOGGER = Logger.getLogger(BucketLinkedList.class.getName());
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public void insertData(long timeThreshold, InputData data) {
        try {
            readWriteLock.writeLock().lock();
            add(data.getTimestamp(), data.getAmount(), timeThreshold);
            flushOldData(timeThreshold);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    public OutputData calculateStats(long timeThreshold) {
        readWriteLock.readLock().lock();

        try {
            OutputData output = new OutputData();
            if (root == null || root.getTime() < timeThreshold) {
                return output;
            }

            double min = root.getMin();
            double max = root.getMax();
            long count = root.getCount();
            double sum = root.getSum();

            Bucket temp = root.getNext();

            while (temp != null && temp.getTime() >= timeThreshold) {
                if (temp.getMax() > max)
                    max = temp.getMax();
                if (temp.getMin() < min)
                    min = temp.getMin();
                count = count + temp.getCount();
                sum = sum + temp.getSum();
                temp = temp.getNext();
            }

            output.setCount(count);
            output.setMax(max);
            output.setMin(min);
            output.setSum(sum);

            if (count > 0) {
                output.setAvg(sum / (double) count);
            }

            return output;

        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    private void flushOldData(long timeThreshold) {
        if (root == null) return;
        int i = 0;
        Bucket bucket = root;

        while (i <= MAX_SIZE && bucket != null && bucket.getTime() >= timeThreshold) {
            bucket = bucket.getNext();
            i++;
        }
        if (bucket != null && bucket.getNext()!=null) {
            bucket.setNext(null);
            LOGGER.info("Flushed old data");
        }

    }

    private void add(long time, double amount, long threshold) {

        if (threshold > time){
            LOGGER.info("Cannot be added since data is old.");
            return;
        }

        Bucket bucket = new Bucket(time, amount);
        if (root == null) {
            root = bucket;
        } else {
            if (time > root.getTime()) {
                bucket.setNext(root);
                root = bucket;
            } else if (time == root.getTime()) {
                root.addData(amount);
            } else {
                Bucket temp = root;
                Bucket tempAfter = root.getNext();

                while (tempAfter != null) {
                    if (temp.getTime() == time) {
                        temp.addData(amount);
                        return;
                    } else {
                        if (time > tempAfter.getTime()) {
                            break;
                        }
                    }
                    temp = tempAfter;
                    tempAfter = tempAfter.getNext();

                }
                bucket.setNext(temp.getNext());
                temp.setNext(bucket);

            }
        }
    }

}
