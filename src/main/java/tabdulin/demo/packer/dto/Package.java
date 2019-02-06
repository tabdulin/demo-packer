package tabdulin.demo.packer.dto;

import java.util.*;

/**
 * Package that holds candidates to be packed into it
 */
public class Package {
    private final double maxWeight;
    private final List<Item> candidates;

    Package(double maxWeight, List<Item> candidates) {
        this.maxWeight = maxWeight;
        this.candidates = candidates;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public List<Item> getCandidates() {
        return candidates;
    }
}
