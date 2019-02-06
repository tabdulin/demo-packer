package tabdulin.demo.packer.dto;

/**
 * Item object that can be packed into package.
 */
public class Item {
    private final int index;
    private final double weight;
    private final double cost;

    Item(int index, double weight, double cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
    }

    public int getIndex() {
        return index;
    }

    public double getWeight() {
        return weight;
    }

    public double getCost() {
        return cost;
    }
}
