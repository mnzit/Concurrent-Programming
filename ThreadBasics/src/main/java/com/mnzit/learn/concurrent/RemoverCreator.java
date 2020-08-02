package com.mnzit.learn.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 *
 *
 * We also learned about wait() and notify() operations that can be used to
 * block and resume threads that need to wait for specific conditions. For
 * example, a creator thread performing an put() operation on a list
 * can call wait() when the list is at its max size, so that it is only unblocked
 * when a remover thread performing a remove() operation calls
 * notify(). Likewise, a remover thread performing a remove() operation on a
 * list can call wait() when the buffer is empty, so that it
 * is only unblocked when a producer thread performing an insert() operation
 * calls notify(). Structured locks are also referred to as intrinsic locks or
 * monitors.
 *
 */
@Slf4j
public class RemoverCreator {

    public static void main(String[] args) {
        Store store = new Store();

        Thread creator = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(new Random().nextInt(3000 - 100 + 100) + 100);
                    store.put(new Random().nextInt(10 - 1 + 1) + 1);
                } catch (Exception e) {
//                    log.error("Exception : {}", e);
                }
            }
        }, "creator");

        Thread remover = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(new Random().nextInt(3000 - 100 + 100) + 100);
                    store.remove();
                } catch (Exception e) {
//                    log.error("Exception : {}", e);
                }
            }
        }, "remover");

        Thread statusShower = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    log.debug("Collection : {}", store.getIntegers());
                } catch (Exception e) {
//                    log.error("Exception : {}", e);
                }
            }
        }, "status");

        creator.start();
        remover.start();
        statusShower.start();
    }

}

@Slf4j
@Data
class Store {

    private List<Integer> integers = new ArrayList<>();

    private int count = -1;

    public void put(int element) {
        final int MAX_SIZE = 3;
        try {
            synchronized (integers) {
                while (integers.size() == MAX_SIZE) {
                    log.debug("Waiting to shrink");
                    integers.wait();
                }
                log.debug("Inserting : {}", element);
                count++;
                integers.add(element);
                integers.notify();
            }
        } catch (Exception e) {
//            log.error("Exception : {}", e);
        }
    }

    public void remove() {

        try {

            synchronized (integers) {
                while (integers.isEmpty()) {
                    log.debug("Waiting to fill");
                    integers.wait();
                }
                log.debug("Removing : {}", integers.get(count));
                integers.remove(count);
                count--;
                integers.notify();
            }
        } catch (Exception e) {
//            log.error("Exception : {}", e);
        }
    }
}
