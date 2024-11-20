package tech.petrepopescu.phoenix.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.List;
import java.util.Set;

class EscapeCharacterElementTest {
    @Test
    void characterIsEscaped() {
        final String line = "@@domain.com";
        EscapeCharacterElement element = new EscapeCharacterElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);

        element.parse("");

        final String expected = "\t\thtmlContentBuilder.append(\"@domain.com\");\n";
        Assertions.assertEquals(expected, element.write().toString());
    }
}
