package org.example.korkiedp.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundWorker {

    private static final ExecutorService dbExecutor = Executors.newFixedThreadPool(4); // or cached/single depending on needs

    private BackgroundWorker() {}

    public static void submit(Runnable task) {
        dbExecutor.submit(task);
    }

    public static void shutdown() {
        dbExecutor.shutdown();
    }
}
