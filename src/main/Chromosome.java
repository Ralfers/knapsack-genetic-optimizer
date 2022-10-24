package main;

import java.util.*;

public class Chromosome {

    private final int knapsackCapacity;
    private final Map<Integer, Item> itemMap = new HashMap<>();

    private int totalValue = 0;
    private int totalWeight = 0;

    public Chromosome(int knapsackCapacity) {
        this.knapsackCapacity = knapsackCapacity;
    }

    public int getFitness() {
        return totalWeight > knapsackCapacity ? 0 : totalValue;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void addItem(Item item) {
        itemMap.put(item.getId(), item);
        totalValue += item.getValue();
        totalWeight += item.getWeight();
    }

    public void removeItem(Item item) {
        itemMap.remove(item.getId());
        totalValue -= item.getValue();
        totalWeight -= item.getWeight();
    }

    public Map<Integer, Item> getItems() {
        return itemMap;
    }

    public boolean contains(Item item) {
        return itemMap.containsKey(item.getId());
    }

    public boolean contains(int itemId) {
        return itemMap.containsKey(itemId);
    }

    public static Chromosome copy(Chromosome otherChromosome, int knapsackCapacity) {
        Chromosome chromosome = new Chromosome(knapsackCapacity);
        Map<Integer, Item> otherItems = otherChromosome.getItems();
        for (Item item : otherItems.values()) {
            chromosome.addItem(item);
        }

        return chromosome;
    }

    @Override
    public String toString() {
        return Arrays.toString(itemMap.values().toArray());
    }
}
