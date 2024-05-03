package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.EmptyElement;
import tech.petrepopescu.phoenix.parser.elements.CsrfInputElement;
import tech.petrepopescu.phoenix.parser.elements.CsrfMetaTagElement;

import java.util.List;

@Component
public class CsrfElementBuilder extends ElementBuilder{
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@csrf");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        String line = lines.get(lineNumber).trim();
        if (StringUtils.equals(line, "@csrf.input()")) {
            return new CsrfInputElement(lines, lineNumber, elementFactory, builderName);
        }
        if (StringUtils.startsWith(line, "@csrf.meta(")) {
            return new CsrfMetaTagElement(lines, lineNumber, elementFactory, builderName);
        }
        return new EmptyElement(lines, lineNumber, elementFactory, builderName);
    }
}
