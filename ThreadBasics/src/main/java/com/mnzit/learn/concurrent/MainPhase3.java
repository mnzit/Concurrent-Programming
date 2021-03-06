package com.mnzit.learn.concurrent;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Manjit Shakya <manjit.shakya@f1soft.com>
 */
@Slf4j
public class MainPhase3 {

    public static void main(String[] args) throws InterruptedException {

        log.debug("This is start printed by PUT: {}", Thread.currentThread().getName());

        List<Employee> employees = new ArrayList<>();

        Thread thread = new Thread(new RunnableDemo2(5L, employees));

        Thread.sleep(1000);

        thread.start();

        thread.join();

        Thread.sleep(1000);

        log.debug("Total Employees : {}", employees.size());

        log.debug("This is end printed by POP: {}", Thread.currentThread().getName());
    }

}

@Slf4j
class RunnableDemo2 implements Runnable {

    private Long level;
    private List<Employee> employees;

    public RunnableDemo2(Long level, List<Employee> employees) {
        this.level = level;
        this.employees = employees;
    }

    /**
     * Creating a chain main thread waits for thread to complete, the thread
     * creates another thread and it waits until the another thread completes
     * until the level reaches 1 Main Thread -> Thread 0 -> Thread 1 -> Thread 2
     * -> Thread 3
     *
     * LIFO
     */
    @Override
    public void run() {
        if (level >= 1) {
            level--;
            try {
                Thread.sleep(1000);

                employees.add(new Employee(level, Thread.currentThread().getName()));

                Thread thread = new Thread(new RunnableDemo2(level, employees));

                thread.start();

                log.debug("This is start printed by PUT: {}", Thread.currentThread().getName());
                thread.join();

                Thread.sleep(1000);

                employees.remove(employees.size() - 1);

                log.debug("This is end printed by POP: {}", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                log.error("Exception : {}:{}", e.getMessage(), e);
            }

        }
    }
}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class Employee {

    private Long id;
    private String name;
}
