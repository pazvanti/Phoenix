package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.InsertOnceElement;

import java.util.List;

@Component
public class InsertOnceElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@insertOnce(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        String sectionName = StringUtils.substringBetween(lines.get(lineNumber), "insertOnce(\"", "\")");
        return new InsertOnceElement(lines, lineNumber, elementFactory, sectionName + "ContentBuilder");
    }
}
