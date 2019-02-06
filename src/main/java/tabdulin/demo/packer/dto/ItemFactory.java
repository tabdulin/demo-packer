package tabdulin.demo.packer.dto;

public class ItemFactory {
    final static char ITEM_START_CHARACTER = '(';
    final static char ITEM_END_CHARACTER = ')';
    final static char ITEM_CURRENCY_CHARACTER = '€';

    public final int MAX_INDEX;
    public final double MAX_WEIGHT;
    public final double MAX_COST;

    public ItemFactory() {
        this(15, 100, 100);
    }

    public ItemFactory(int maxIndex, double maxWeight, double maxCost) {
        this.MAX_INDEX = maxIndex;
        this.MAX_WEIGHT = maxWeight;
        this.MAX_COST = maxCost;
    }

    /**
     * Each package has it's index (0, 15], weight (0,100] and cost (0,100]
     *
     * @param index
     * @param weight
     * @param cost
     * @return
     */
    public Item create(int index, double weight, double cost) {
        final String SEPARATOR = "; ";
        StringBuilder errors = new StringBuilder();
        if (index <= 0 || index > MAX_INDEX) {
            errors.append("Invalid index: ").append(index).append(SEPARATOR);
        }

        if (weight <= 0 || weight > MAX_WEIGHT) {
            errors.append("Invalid weight: ").append(weight).append(SEPARATOR);
        }

        if (cost <= 0 || cost > MAX_COST) {
            errors.append("Invalid cost: ").append(cost).append(SEPARATOR);
        }

        if (errors.length() > 0) {
            throw new IllegalArgumentException(errors.toString());
        }

        return new Item(index, weight, cost);
    }

    /**
     * Builds Item object from it's string representation:
     * <pre>
     *     integer_max_weight : (integer_index,double_weight,double_cost) (...)
     * </pre>
     *
     * @param item String representation of the item
     * @return
     */
    public Item create(String item) {
        item = item.strip();
        if (!item.startsWith(String.valueOf(ITEM_START_CHARACTER)) || !item.endsWith(String.valueOf(ITEM_END_CHARACTER))) {
            throw new IllegalArgumentException("Invalid item brackets: " + item);
        }

        item = item.substring(1, item.length() - 1);
        String[] data = item.split(",");
        final int INDEX = 0;
        final int WEIGHT = 1;
        final int COST = 2;
        if (data.length != 3) {
            throw new IllegalArgumentException("Invalid item format: " + item);
        }

        int index = Integer.valueOf(data[INDEX].strip());
        double weight = Double.valueOf(data[WEIGHT].strip());
        String costStr = data[COST].strip();
        if (!costStr.startsWith(String.valueOf(ITEM_CURRENCY_CHARACTER))) {
            throw new IllegalArgumentException("Invalid item currency: " + item);
        }

        double cost = Double.valueOf(costStr.substring(1));

        return new Item(index, weight, cost);
    }
}
