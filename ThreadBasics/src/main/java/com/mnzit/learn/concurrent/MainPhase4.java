package com.mnzit.learn.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 */
@Slf4j
public class MainPhase4 {

    private AtomicInteger visitorCount = new AtomicInteger(0);

    public final Object lock1 = new Object();
    public final Object lock2 = new Object();

    /**
     * Synchronize block based on multiple locks make multi-threading possible
     *
     * So if you want to lock the whole object, use a synchronized method. If
     * you want to keep other parts of the object accessible to other threads,
     * use synchronized block.
     *
     * One of the issues of using synchronized method is that all of the members
     * of the class would use the same lock which will make your program slower.
     *
     * In your case synchronized method and block would execute no different.
     * what I'd would recommend is to use a dedicated lock and use a
     * synchronized block something like this.
     */
    public void visitAndPrint() {
        synchronized (lock1) {
            log.debug("Total Visitors: {}", visitorCount.incrementAndGet());
        }
    }

    public void print() {
        synchronized (lock2) {
            for (int i = 0; i < 10; i++) {
                log.debug("i is : {}", i);
            }
        }
    }

    public static void main(String... args) {
        ExecutorService service = null;
        MainPhase4 counter = new MainPhase4();
        try {
            service = Executors.newFixedThreadPool(14);
            for (int i = 0; i < 15; i++) {
                service.submit(() -> {
                    counter.print();
                    counter.visitAndPrint();
                });
            }
        } finally {
            if (null != service) {
                service.shutdown();
            }
        }
    }
}
