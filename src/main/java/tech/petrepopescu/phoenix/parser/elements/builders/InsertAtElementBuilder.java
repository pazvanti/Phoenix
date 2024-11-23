package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.InsertAtElement;

import java.util.List;

public class InsertAtElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@insertAt(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        String sectionName = StringUtils.substringBetween(lines.get(lineNumber), "insertAt(\"", "\")");
        return new InsertAtElement(lines, lineNumber, elementFactory, sectionName + "ContentBuilder");
    }
}
