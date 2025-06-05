package tech.petrepopescu.phoenix.parser.elements.builders;

import tech.petrepopescu.phoenix.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.SectionElement;

import java.util.List;

@Component
public class SectionElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@section(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new SectionElement(lines, lineNumber, elementFactory, builderName);
    }
}
