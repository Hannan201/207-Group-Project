package controllers.utilities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/*
 The purpose of this class is to prevent from making necessary calls by
 making sure a function only executes after a certain amount of time
 give an input, if the function executes before that time we need to only
 execute the function with the new input and remove the old version of that
 function with the old input. Useful for when searching through a data structure
 based on input given by key-presses.
 */
public class Debouncer {

    // To make sure the function executes after a certain amount of time.
    private final Timer timer;

    // Give the ability for tasks to be registered across different
    // threads,
    private final ConcurrentHashMap<String, TimerTask> tasks;


    /**
     * Create a new debouncer.
     */
    public Debouncer() {
        this.timer = new Timer();
        this.tasks = new ConcurrentHashMap<>();
    }

    /**
     * Register a function with a key and interval to this debouncer. If
     * another function is registered before the first function has executed,
     * the first function will be removed and not execute.
     *
     * @param key Key for the function.
     * @param function Function to execute.
     * @param interval How long to wait before the function is executed.
     */
    public void registerFunction(String key, Callable<Void> function, long interval) {
        // Safety checks.
        if (key == null || key.isEmpty() || key.trim().length() < 1 || interval < 0) {
            return;
        }

        // Clear all.
        clearPreviousTasks();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    function.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                clearPreviousTasks();
                tasks.clear();
                timer.cancel();
            }
        };

        registerTask(key, task, interval);
    }


    /**
     * Clear any previous tasks.
     */
    private void clearPreviousTasks() {
        if (!tasks.isEmpty()) {
            tasks.forEachEntry(
                    1000,
                    entry -> entry.getValue().cancel()
            );
        }

        tasks.clear();
    }


    /**
     * Add a new timer task with a key and interval to execute.
     *
     * @param key Key for the task.
     * @param task The timer task.
     * @param interval How long to wait before the task is executed.
     */
    private void registerTask(String key, TimerTask task, long interval) {
        if (key == null || key.isEmpty() || key.trim().length() < 1 || interval < 0) {
            return;
        }

        if (tasks.containsKey(key)) {
            return;
        }

        timer.schedule(task, interval);
        tasks.put(key, task);
    }
}
