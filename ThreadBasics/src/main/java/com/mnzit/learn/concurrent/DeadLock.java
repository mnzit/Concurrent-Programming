package com.mnzit.learn.concurrent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 *
 * Simple scenario where the main thread will wait for the t0 thread to complete
 * and t0 will wait for main thread to complete which basically create a
 * deadlock scenario.
 */
@Slf4j
public class DeadLock {

    public static void main(String[] args) {
        log.debug("Start from the : {}", Thread.currentThread().getName());

        Thread t0 = new Thread(new DeadLocking(Thread.currentThread()));

        t0.start();

        try {
            t0.join();
        } catch (Exception e) {
            log.error("Exception : {}", e);
        }

        log.debug("End from the : {}", Thread.currentThread().getName());

    }
}

@Slf4j
@AllArgsConstructor
class DeadLocking implements Runnable {

    Thread mainThread;

    @Override
    public void run() {
        log.debug("Start from the : {}", Thread.currentThread().getName());

        try {
            mainThread.join();
        } catch (Exception e) {
            log.error("Exception : {}", e);
        }

        log.debug("End from the : {}", Thread.currentThread().getName());
    }

}
