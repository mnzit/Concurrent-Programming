package com.mnzit.learn.concurrent;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 */
public class ProducerConsumer {

    public static void main(String[] args) {
        List<Integer> taskQueue = new ArrayList<Integer>();
        int MAX_CAPACITY = 5;
        Thread tProducer = new Thread(new Producer(taskQueue, MAX_CAPACITY), "Producer");
        Thread tConsumer = new Thread(new Consumer(taskQueue), "Consumer");
        tProducer.start();
        tConsumer.start();
    }
}

@Slf4j
class Producer implements Runnable {

    private final List<Integer> taskQueue;
    private final int MAX_CAPACITY;

    public Producer(List<Integer> sharedQueue, int size) {
        this.taskQueue = sharedQueue;
        this.MAX_CAPACITY = size;
    }

    @Override
    public void run() {
        int counter = 0;
        while (true) {
            try {
                produce(counter++);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void produce(int i) throws InterruptedException {
        synchronized (taskQueue) {
            while (taskQueue.size() == MAX_CAPACITY) {
                log.debug("Queue is full, {} is waiting , size: {}", Thread.currentThread().getName(), taskQueue.size());
                taskQueue.wait();
            }

            Thread.sleep(1000);
            taskQueue.add(i);
            log.debug("Produced: {}", i);
            taskQueue.notifyAll();
        }
    }
}

@Slf4j
class Consumer implements Runnable {

    private final List<Integer> taskQueue;

    public Consumer(List<Integer> sharedQueue) {
        this.taskQueue = sharedQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                consume();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void consume() throws InterruptedException {
        synchronized (taskQueue) {
            while (taskQueue.isEmpty()) {
                log.debug("Queue is empty, {} is waiting , size: {}", Thread.currentThread().getName(), taskQueue.size());
                taskQueue.wait();
            }
            Thread.sleep(1000);
            int i = (Integer) taskQueue.remove(0);
            log.debug("Consumed: {}", i);
            taskQueue.notifyAll();
        }

    }
}
