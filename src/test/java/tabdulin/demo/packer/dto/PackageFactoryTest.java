package tabdulin.demo.packer.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PackageFactoryTest {
    private PackageFactory packageFactory = new PackageFactory();

    @ParameterizedTest
    @ValueSource(doubles = {
            0.0001,
            63,
            99.9999
    })
    void create_validPackageWeight_ok(double weight) {
        packageFactory.create(weight, new ArrayList<>(0));
    }

    @ParameterizedTest
    @ValueSource(doubles = {
            -6.2,
            100.1
    })
    void create_invalidPackageWeight_throwIllegalArgumentException(double weight) {
        var ex = assertThrows(IllegalArgumentException.class, () -> packageFactory.create(weight, new ArrayList<>(0)));
        assertTrue(ex.getMessage().contains("weight"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)",
            "8 : (1,15.3,€34)",
            "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)",
            "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)"
    })
    void create_validPackages(String str) {
        packageFactory.create(str);
    }

    @Test
    void create_tooManyItems_throwIllegalArgumentException() {
        List<Item> items = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            items.add(new Item(i + 1, i, i));
        }

        assertThrows(IllegalArgumentException.class, () -> packageFactory.create(20, items));
    }

    @Test
    void create_validSingleItem_ok() {
        tabdulin.demo.packer.dto.Package pack = packageFactory.create("8 : (1,15.3,€34)");
        assertEquals(8, pack.getMaxWeight());

        assertEquals(1, pack.getCandidates().get(0).getIndex());
        assertEquals(15.3, pack.getCandidates().get(0).getWeight());
        assertEquals(34, pack.getCandidates().get(0).getCost());
    }

    @Test
    void create_validMultipleItems_ok() {
        tabdulin.demo.packer.dto.Package pack = packageFactory.create("81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3)");
        assertEquals(81, pack.getMaxWeight());
        assertEquals(3, pack.getCandidates().size());

        assertEqualsItem(1, 53.38, 45, pack.getCandidates().get(0));
        assertEqualsItem(2, 88.62, 98, pack.getCandidates().get(1));
        assertEqualsItem(3, 78.48, 3, pack.getCandidates().get(2));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            ":(1,2)(2,€98)",
            ":(,,)(,,)",
            "7 (",
            "a : (b,c,d)"
    })
    void create_invaliItems_throwIllegalArgumentException(String line) {
        assertThrows(IllegalArgumentException.class, () -> packageFactory.create(line));
    }


    private void assertEqualsItem(int expectedIndex,
                                  double expectedWeight,
                                  double expectedCost,
                                  Item actualItem) {
        assertEquals(expectedIndex, actualItem.getIndex());
        assertEquals(expectedWeight, actualItem.getWeight());
        assertEquals(expectedCost, actualItem.getCost());
    }

}