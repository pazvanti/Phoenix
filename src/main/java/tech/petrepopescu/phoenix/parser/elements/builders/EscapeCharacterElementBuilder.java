package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.EscapeCharacterElement;

import java.util.List;

@Component
public class EscapeCharacterElementBuilder extends ElementBuilder{
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@@");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new EscapeCharacterElement(lines, lineNumber, elementFactory, builderName);
    }
}
