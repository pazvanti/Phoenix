package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.ForWithIteratorElement;
import tech.petrepopescu.phoenix.parser.elements.Element;

import java.util.List;

@Component
public class ForWithIteratorElementBuilder extends ElementBuilder{
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@for((") || StringUtils.startsWith(line, "@for ((");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ForWithIteratorElement(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int order() {
        return 100;
    }
}
