package tech.petrepopescu.phoenix.parser.elements.builders;

import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;

import java.util.List;

public abstract class ElementBuilder {
    public int order() {
        return 0;
    }
    public abstract boolean isValid(String line);
    public abstract Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName);
}
