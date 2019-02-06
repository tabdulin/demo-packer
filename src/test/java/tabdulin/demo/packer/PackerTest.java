package tabdulin.demo.packer;

import org.junit.jupiter.api.Test;
import tabdulin.demo.packer.dto.Item;
import tabdulin.demo.packer.dto.ItemFactory;
import tabdulin.demo.packer.dto.Package;
import tabdulin.demo.packer.dto.PackageFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackerTest {
    private Packer packer = new Packer();
    private PackageFactory packageFactory = new PackageFactory();
    private ItemFactory itemFactory = new ItemFactory();

    @Test
    void pack_input1File_output1File() throws APIException, IOException {
        assertEquals(Files.lines(Path.of(getClass().getResource("1.output").getPath()))
                        .collect(Collectors.joining("\n")),
                Packer.pack(getClass().getResource("1.input").getPath()));
    }

    @Test
    void choose_noItems_nonChosen() {
        Package pack = packageFactory.create(60, Arrays.asList());
        assertEquals(Arrays.<Integer>asList(), packer.choose(pack));
    }

    @Test
    void choose_allItemsNotFit_someChosen() {
        List<Item> items = Arrays.asList(
                itemFactory.create(1, 91, 100),
                itemFactory.create(2, 15, 51),
                itemFactory.create(3, 12, 52),
                itemFactory.create(4, 5, 25)
        );

        List<Integer> chosen = Arrays.asList(2, 3, 4);
        Package pack = packageFactory.create(95, items);
        assertEquals(chosen, packer.choose(pack));
    }

    @Test
    void choose_equalCostsDifferentWeights_minWeightChosen() {
        List<Item> items = Arrays.asList(
                itemFactory.create(1, 75, 100),
                itemFactory.create(2, 15, 52),
                itemFactory.create(3, 12, 52),
                itemFactory.create(4, 5, 25)
        );

        List<Integer> chosen = Arrays.asList(1, 3, 4);
        Package pack = packageFactory.create(95, items);
        assertEquals(chosen, packer.choose(pack));
    }

    @Test
    void choose_eachItemWeightIsOverPackageMaxWeight_noneChosen() {
        List<Item> items = Arrays.asList(
                itemFactory.create(1, 91, 100),
                itemFactory.create(2, 15, 51),
                itemFactory.create(3, 12, 52),
                itemFactory.create(4, 5, 25)
        );

        List<Integer> chosen = Arrays.asList();
        Package pack = packageFactory.create(4, items);
        assertEquals(chosen, packer.choose(pack));
    }

    @Test
    void choose_allItemsFit_allChosen() {
        List<Item> items = Arrays.asList(
                itemFactory.create(1, 51, 100),
                itemFactory.create(2, 15, 51),
                itemFactory.create(3, 12, 52),
                itemFactory.create(4, 5, 25)
        );

        List<Integer> chosen = Arrays.asList(1, 2, 3, 4);
        Package pack = packageFactory.create(99, items);
        assertEquals(chosen, packer.choose(pack));
    }

}