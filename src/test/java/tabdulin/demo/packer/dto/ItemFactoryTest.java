package tabdulin.demo.packer.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ItemFactoryTest {
    private ItemFactory itemFactory = new ItemFactory();

    @ParameterizedTest
    @ValueSource(strings = {
            "(1,1.0,€1.0)",
            "  (1,1.0,€1.0) ",
            "  (  1  ,   1.0  ,   €1.0 ) "
    })
    void create_validItemStringsWithExtraSpaces_ok(String str) {
        var item = itemFactory.create(str);
        assertEquals(1, item.getIndex());
        assertEquals(1.0, item.getWeight());
        assertEquals(1.0, item.getCost());
    }

    static Stream<Arguments> invalidItems() {
        return Stream.of(
                arguments(16, 1.0, 1.0, asList("index")),
                arguments(0, 1.0, 1.0, asList("index")),
                arguments(3, 150.0, 2.0, asList("weight")),
                arguments(18, 150, 300, asList("index", "weight", "cost"))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidItems")
    void create_invalidItems_throwsIllegalArgumentException(int index, double weight, double cost, List<String> errors) {
        var ex = assertThrows(IllegalArgumentException.class, () -> itemFactory.create(index, weight, cost));
        for (String error : errors) {
            assertTrue(ex.getMessage().contains(error));
        }
    }

    static Stream<Arguments> validItems() {
        return Stream.of(
                arguments(1, 1, 1),
                arguments(2, 2.1, 2.3)
        );
    }

    @ParameterizedTest
    @MethodSource("validItems")
    void create_validItems_ok(int index, double weight, double cost) {
        itemFactory.create(index, weight, cost);
    }
}