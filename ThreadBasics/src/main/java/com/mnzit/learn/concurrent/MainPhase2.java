package com.mnzit.learn.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 */
@Slf4j
public class MainPhase2 {

    public static void main(String[] args) {

        Thread thread1 = new Thread(new RunnableDemo(Thread.currentThread()));

        thread1.start();

        int i = 0;

        while (i <= 10000) {
            log.debug("This is printed by : {} : {}", Thread.currentThread().getName(), i++);
        }

    }
}

@Slf4j
class RunnableDemo implements Runnable {

    private Thread thread;

    public RunnableDemo(Thread thread) {
        this.thread = thread;
    }

    @Override
    public void run() {
        try {
            log.debug("This is printed by : {}", Thread.currentThread().getName());
            thread.join();
            log.debug("This is printed by : {}", Thread.currentThread().getName());
        } catch (InterruptedException e) {
            log.error("Exception : {}:{}", e.getMessage(), e);
        }
    }

}
