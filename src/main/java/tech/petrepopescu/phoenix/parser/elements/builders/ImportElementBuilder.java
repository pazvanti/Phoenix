package tech.petrepopescu.phoenix.parser.elements.builders;

import tech.petrepopescu.phoenix.utils.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.ImportElement;

import java.util.List;

@Component
public class ImportElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@import ");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ImportElement(lines, lineNumber, elementFactory, builderName);
    }
}
