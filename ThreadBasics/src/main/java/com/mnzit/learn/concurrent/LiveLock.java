package com.mnzit.learn.concurrent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 *
 * Prepare to see a toss between A and B
 *
 * Simple case of LiveLock
 */
public class LiveLock {

    public static void main(String args[]) {
        Integer x = 0;

        Thread incrementor = new Thread(() -> new LiveLockIncr(x).increment(), "incrementor");
        Thread decrementor = new Thread(() -> new LiveLockDecr(x).decrement(), "decrementor");

        incrementor.start();
        decrementor.start();
    }
}

@Slf4j
@AllArgsConstructor
class LiveLockIncr {

    Integer x;

    public void increment() {
        do {
            synchronized (x) {
                x++; // write
                log.debug("A toss the ball: {}", x); //read
            }
        } while (x < 10);
    }

}

@Slf4j
@AllArgsConstructor
class LiveLockDecr {

    Integer x;

    public void decrement() {
        do {
            synchronized (x) {
                x--; //write
                log.debug("B toss the ball: {}", x); //read
            }
        } while (x > -10);
    }

}
