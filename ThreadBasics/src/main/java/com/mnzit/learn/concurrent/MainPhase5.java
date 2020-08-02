package com.mnzit.learn.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 */
@Slf4j
public class MainPhase5 {

    private static final int NUM_EXECUTIONS = 10000;

    // This Object ensures synchronization
    private static final Object mutexLock = new Object();

    private static void someLongOperation() {
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            log.error("Exception : {}", e);
        }
    }

    public static void main(String[] args) {

        final long[] numElements = {0};

        Thread incrementThread = new Thread() {

            @Override
            public void run() {
                for (long count = 0; count < NUM_EXECUTIONS; count++) {
                    someLongOperation();

                    synchronized (mutexLock) {
                        numElements[0] += 1;
                    }
                }
            }
        };

        Thread decrementThread = new Thread() {

            @Override
            public void run() {
                for (long count = 0; count < NUM_EXECUTIONS; count++) {
                    someLongOperation();

                    synchronized (mutexLock) {
                        numElements[0] -= 1;
                    }
                }
            }
        };

        // Start the threads
        incrementThread.start();
        decrementThread.start();

        // Wait for jobs to finish
        try {
            incrementThread.join();
            decrementThread.join();
        } catch (InterruptedException e) {
            log.error("Exception : {}", e);
        }

        // Print the result
        log.debug("Result is: {}", numElements[0]);
    }
}
