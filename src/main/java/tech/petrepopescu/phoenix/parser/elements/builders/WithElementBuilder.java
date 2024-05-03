package tech.petrepopescu.phoenix.parser.elements.builders;

import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.WithElement;
import tech.petrepopescu.phoenix.parser.elements.Element;

import java.util.List;

@Component
public class WithElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return line.trim().startsWith("@with");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new WithElement(lines, lineNumber, elementFactory, builderName);
    }
}
