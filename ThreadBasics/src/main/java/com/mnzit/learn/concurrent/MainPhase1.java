package com.mnzit.learn.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 */
@Slf4j
public class MainPhase1 {

    public static void main(String[] args) {

        try {
            log.debug("This is printed by : {}", Thread.currentThread().getName());

            Runnable thread1 = () -> {
                int i = 1;
                while (i <= 10000) {
                    log.debug("This is printed by : {} : {}", Thread.currentThread().getName(), i++);
                }
            };

            Thread thread1Runner = new Thread(thread1, "thread1");

            thread1Runner.start();

            /**
             * joins() Here main thread waits for the thread1 task to finish to do the
             * remaining task main thread
             * 
             * Try commenting out the join
             */
            thread1Runner.join();

            log.debug("This is printed by : {}", Thread.currentThread().getName());

        } catch (InterruptedException e) {
            log.error("Exception : {}:{}", e.getMessage(), e);
        }
    }
}
