package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Knapsack {

    private int capacity;
    private final List<Item> allItems = new ArrayList<>();

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Item> getAllItems() {
        return allItems;
    }

    public void addToAllItems(Item item) {
        allItems.add(item);
    }

    public static Knapsack fromFile(String fileName) throws IOException {
        Knapsack knapsack = new Knapsack();

        Path path = Paths.get(fileName);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            knapsack.setCapacity(Integer.parseInt(reader.readLine()));

            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
                String[] lineParts = line.split(" ");
                int itemValue = Integer.parseInt(lineParts[0]);
                int itemWeight = Integer.parseInt(lineParts[1]);
                Item item = new Item(i, itemValue, itemWeight);
                knapsack.addToAllItems(item);
                i++;
            }
        }

        return knapsack;
    }
}
