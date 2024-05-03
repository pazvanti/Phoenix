package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.ElseElement;

import java.util.List;

@Component
public class ElseElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWithAny(line," else {", "else{", " else{", "else {") ||
                StringUtils.startsWithAny(line,"} else {", "}else{", "} else{", "}else {") ;
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ElseElement(lines, lineNumber, elementFactory, builderName);
    }
}
