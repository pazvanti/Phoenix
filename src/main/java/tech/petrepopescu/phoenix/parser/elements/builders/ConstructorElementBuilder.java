package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.ConstructorElement;
import tech.petrepopescu.phoenix.parser.elements.Element;

import java.util.List;

@Component
public class ConstructorElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWithAny(line, "@args(", "@args (");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ConstructorElement(lines, lineNumber, elementFactory, builderName);
    }
}
