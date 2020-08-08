package com.mnzit.learn.concurrent.locks.unstructured;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 */
@Slf4j
public class UnstructuredLock {

    public static void main(String[] args) {

        ExecutorService service = null;

        TestUnStrucLock testUnStrucLock = new TestUnStrucLock();
        Integer ij = 10;

        try {
            service = Executors.newFixedThreadPool(1000);
            for (int i = 0; i < 2000; i++) {
                service.submit(() -> {
                    testUnStrucLock.print(ij);
                });
            }
        } finally {
            if (null != service) {
                service.shutdown();
            }
        }
    }
}

@Slf4j
class TestUnStrucLock {

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    public void print(int i) {
        if (lock1.tryLock()) {
            
            lock1.lock();
            log.debug("Locked from lock1");
            i++;
            log.debug("i after increment: {}", i);
            lock1.unlock();

        } else {
            print2(i);
        }
    }

    public void print2(int i) {
        if (lock2.tryLock()) {
            
            lock2.lock();
            log.debug("Locked from lock2");
            i--;
            log.debug("i after decrement : {}", i);
            lock2.unlock();

        } else {
            print(i);
        }
    }
}
