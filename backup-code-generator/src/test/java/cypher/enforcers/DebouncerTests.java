package cypher.enforcers;

import cypher.enforcers.utilities.Debouncer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DebouncerTests {

    private Debouncer debounce;

    /**
     * Test that the debouncer returns the expected result.
     */
    @Test
    void debouncerReturnsResult() throws InterruptedException {
        debounce = new Debouncer();
        List<String> names = Collections.synchronizedList(
                new ArrayList<>(
                        List.of("hello", "how", "are", "you")
                )
        );
        debounce.registerFunction(
                "names",
                () -> {
                    names.replaceAll(String::toUpperCase);
                    return null;
                },
                1000 // Wait 1 second before executing.
        );

        assertEquals("hello", names.get(0));

        Thread.sleep(2000); // Wait for 2 seconds to pass.

        assertEquals("HELLO", names.get(0));
    }


    /**
     * Test that the debouncer ignores the previous function if a
     * new function is added before the first function is executed.
     */
    @Test
    void debouncerClearsOldResult() throws InterruptedException {
        debounce = new Debouncer();
        List<Integer> number = Collections.synchronizedList(
                new ArrayList<>(
                        List.of(3)
                )
        );

        // Square the number.
        debounce.registerFunction(
                "square",
                () -> {
                    number.set(0, (int) Math.pow(number.get(0), 2));
                    return null;
                },
                1000 // Wait for 1 second.
        );

        // Cube the number.
        debounce.registerFunction(
                "cube",
                () -> {
                    number.set(0, (int) Math.pow(number.get(0), 3));
                    return null;
                },
                1000 // Wait for 1 second.
        );

        // Nothing should have happened here.
        assertEquals(number.get(0), 3);

        Thread.sleep(2000); // Wait for 2 second.

        // Squared should be ignored.
        assertEquals(number.get(0), 27);
    }
}