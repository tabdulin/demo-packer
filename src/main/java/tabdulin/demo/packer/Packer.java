package tabdulin.demo.packer;

import tabdulin.demo.packer.dto.Item;
import tabdulin.demo.packer.dto.PackageFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Packer {
    private static final String NO_ITEM_CHOSEN = "-";
    private static final String ITEM_DELIMITER = ",";
    private static final String PACKAGE_DELiMITER = "\n";
    private static final PackageFactory PACKAGE_FACTORY = new PackageFactory();

    private int intWeight(double weight) {
        return (int) weight * 100;
    }

    /**
     * Packs item candidates.
     *
     * Dynamic programming approach is used to find optimal combination of items to be packed.
     * Time complexity: O(nw), where n - number of items, w - max weight
     * Since weight is a double number with 2 digits precision it's converted to integer
     *
     * @return List of indexes of item candidates
     */
    public List<Integer> choose(tabdulin.demo.packer.dto.Package pack) {
        List<Item> candidates = pack.getCandidates();

        Collections.sort(candidates, Comparator.comparingDouble(Item::getWeight));
        final int MAX_WEIGHT = intWeight(pack.getMaxWeight());
        final int ITEMS_NUMBER = candidates.size();
        double[][] memo = new double[ITEMS_NUMBER + 1][MAX_WEIGHT + 1];

        for (int i = 0; i <= ITEMS_NUMBER; i++) {
            for (int w = 0; w <= MAX_WEIGHT; w++) {
                if (i == 0 || w == 0) {
                    memo[i][w] = 0;
                    continue;
                }

                Item item = candidates.get(i - 1);
                if (intWeight(item.getWeight()) <= w) {
                    memo[i][w] = Math.max(item.getCost() + memo[i - 1][w - intWeight(item.getWeight())], memo[i - 1][w]);
                } else {
                    memo[i][w] = memo[i - 1][w];
                }
            }
        }

        double cost = memo[ITEMS_NUMBER][MAX_WEIGHT];
        List<Integer> chosen = new ArrayList<>();
        for (int i = ITEMS_NUMBER, w = MAX_WEIGHT; i > 0 && cost > 0; i--) {
            if (cost != memo[i - 1][w]) {
                Item item = candidates.get(i - 1);
                chosen.add(item.getIndex());
                cost -= item.getCost();
                w -= intWeight(item.getWeight());
            }
        }

        Collections.sort(chosen);
        return chosen;
    }

    public static String pack(String absoluteFilePath) throws APIException {
        try {
            Packer packer = new Packer();
            return Files.lines(Path.of(absoluteFilePath))
                    .map(PACKAGE_FACTORY::create)
                    .map(packer::choose)
                    .map(chosen -> chosen.isEmpty()
                            ? NO_ITEM_CHOSEN
                            : chosen.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(ITEM_DELIMITER)))
                    .collect(Collectors.joining(PACKAGE_DELiMITER));
        } catch (Exception e) {
            throw new APIException(e);
        }
    }
}
