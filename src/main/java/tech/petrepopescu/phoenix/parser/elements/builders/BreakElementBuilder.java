package tech.petrepopescu.phoenix.parser.elements.builders;

import tech.petrepopescu.phoenix.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.BreakElement;
import tech.petrepopescu.phoenix.parser.elements.Element;

import java.util.List;

@Component
public class BreakElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWithAny(line, "@break");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new BreakElement(lines, lineNumber, elementFactory, builderName);
    }
}
