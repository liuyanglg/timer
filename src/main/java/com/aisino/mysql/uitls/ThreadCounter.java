package com.aisino.mysql.uitls;

public class ThreadCounter {
    private int count;

    public ThreadCounter(int count) {
        this.count = count;
    }

    public synchronized void countDown() {
        count--;
    }

    public synchronized boolean hasNext() {
        return (count > 0);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}