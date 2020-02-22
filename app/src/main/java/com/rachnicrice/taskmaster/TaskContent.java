package com.rachnicrice.taskmaster;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class TaskContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Task> ITEMS = new ArrayList<Task>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Task> ITEM_MAP = new HashMap<String, Task>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createTask(i));
        }
    }

    private static void addItem(Task item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.title, item);
    }

    private static Task createTask(int position) {
        return new Task(String.valueOf(position), makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }
}
