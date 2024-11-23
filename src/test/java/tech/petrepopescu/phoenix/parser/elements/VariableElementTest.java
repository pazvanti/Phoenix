package tech.petrepopescu.phoenix.parser.elements;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.List;
import java.util.Set;

class VariableElementTest {
    @Test
    void singleVariable() {
        String line = "@a";
        VariableElement element = new VariableElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(a);\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void variableMethodCall() {
        String line = "@a.b()";
        VariableElement element = new VariableElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(Stringifier.toString(a.b()));\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void variableMultipleMethodCalls() {
        String line = "@a.b().c()";
        VariableElement element = new VariableElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(Stringifier.toString(a.b().c()));\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }

    @Test
    void variableCallsVariableMethodCall() {
        String line = "@a.b.c()";
        VariableElement element = new VariableElement(List.of(line), 0, new ElementFactory(Set.of()), ElementFactory.DEFAULT_BUILDER_NAME);
        element.parse("");

        String expected = "\t\tcontentBuilder.append(Stringifier.toString(a.b.c()));\n" +
                "\t\tcontentBuilder.append(\"\\n\");\n";

        Assertions.assertEquals(expected, element.write().toString());
    }
}
