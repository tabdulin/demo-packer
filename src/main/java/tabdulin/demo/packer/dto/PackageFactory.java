package tabdulin.demo.packer.dto;

import java.util.LinkedList;
import java.util.List;

public class PackageFactory {
    final int MAX_ITEMS_NUMBER;
    final double MAX_PACKAGE_WEIGHT;

    private ItemFactory itemFactory;

    public PackageFactory() {
        this(15, 100, new ItemFactory());
    }

    public PackageFactory(int maxItemsNumber, double maxWeight, ItemFactory itemFactory) {
        this.MAX_ITEMS_NUMBER = maxItemsNumber;
        this.MAX_PACKAGE_WEIGHT = maxWeight;
        this.itemFactory = itemFactory;
    }

    /**
     * Creates Package object
     *
     * @param maxWeight maximum weight of the package (total weight of packed items)
     * @param candidates  item candidates to be packed
     *
     * @return created package+
     *
     * @see Package
     */
    public Package create(double maxWeight, List<Item> candidates) {
        final String SEPARATOR = "; ";
        StringBuilder errors = new StringBuilder();
        if (maxWeight <= 0 || maxWeight > MAX_PACKAGE_WEIGHT) {
            errors.append("Invalid max weight: ").append(maxWeight).append(SEPARATOR);
        }

        if (candidates.size() > MAX_ITEMS_NUMBER) {
            errors.append("Invalid items number: ").append(candidates.size()).append(SEPARATOR);
        }

        if (errors.length() > 0) {
            throw new IllegalArgumentException(errors.toString().strip());
        }

        return new Package(maxWeight, candidates);
    }

    /**
     * Creates Package object from string representation such as
     * <pre>
     *     integer_max_weight : (integer_index,double_weight,double_cost) (...)
     * </pre>
     * @param line String representation of the package
     * @return
     */
    public Package create(String line) {
        String[] weightAndItems = line.split(":");
        if (weightAndItems.length != 2) {
            throw new IllegalArgumentException("Invalid package line: no single semicolon found: " + line);
        }

        double maxWeight = Double.valueOf(weightAndItems[0]);

        boolean itemStarted = false,
                itemEnded = false;
        List<Item> items = new LinkedList<>();
        StringBuilder item = new StringBuilder();
        for (char c : weightAndItems[1].toCharArray()) {
            if (ItemFactory.ITEM_START_CHARACTER == c) {
                if (itemStarted || itemEnded) throw new IllegalArgumentException("Invalid item brackets: " + line);
                itemStarted = true;
            } else if (ItemFactory.ITEM_END_CHARACTER == c) {
                if (itemEnded) throw new IllegalArgumentException("Invalid item brackets: " + line);
                itemEnded = true;
            }

            item.append(c);
            if (itemStarted && itemEnded) {
                items.add(itemFactory.create(item.toString()));
                item = new StringBuilder();
                itemStarted = false;
                itemEnded = false;
            }
        }

        return new Package(maxWeight, items);
    }
}
