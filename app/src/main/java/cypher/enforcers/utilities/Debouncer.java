package cypher.enforcers.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/*
 The purpose of this class is to prevent from making unnecessary calls by
 making sure a function only executes after a certain amount of time
 give an input, if the function executes before that time we need to only
 execute the function with the new input and remove the old version of that
 function with the old input. Useful for when searching through a data structure
 based on input given by key-presses.

 Credits to: https://stackoverflow.com/a/58404365.
 */
public class Debouncer {

    private static final Logger logger = LoggerFactory.getLogger(Debouncer.class);

    // To make sure the function executes after a certain amount of time.
    private final Timer timer;

    // Give the ability for tasks to be registered across different
    // threads.
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
        if (safetyChecks(key, interval)) return;

        // Clear all.
        clearPreviousTasks();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    function.call();
                } catch (Exception e) {
                    logger.warn("Failed execution. Cause: ", e);
                    e.printStackTrace();
                }
                clearPreviousTasks();
                tasks.clear();
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
        if (safetyChecks(key, interval)) return;

        if (tasks.containsKey(key)) {
            logger.warn("Function with key {} has already been registered.", key);
            return;
        }

        timer.schedule(task, interval);
        tasks.put(key, task);
    }

    /**
     * Perform a safety check for the key and interval before a function is
     * registered to this debouncer.
     *
     * @param key Key for the function.
     * @param interval Interval for the function.
     * @return True if the interval is greater than or equal to 0 and
     * the key's length is greater than 0 and is not blank, false otherwise.
     */
    private boolean safetyChecks(String key, long interval) {
        if (interval < 0) {
            logger.warn("Interval value {} must be greater than 0.", interval);
            return true;
        }

        if (key == null || key.isEmpty() || key.trim().length() < 1) {
            logger.warn("Bad key value {}.", key);
            return true;
        }
        return false;
    }

    /**
     * Turn off the debouncer.
     */
    public void tearDown() {
        logger.debug("Shutting down debouncer.");
        clearPreviousTasks();
        timer.cancel();
    }
}
