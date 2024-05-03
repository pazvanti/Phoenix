package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.AssetElement;
import tech.petrepopescu.phoenix.parser.elements.Element;

import java.util.List;

@Component
public class AssetElementBuilder extends ElementBuilder {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWith(line, "@asset.path(");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new AssetElement(lines, lineNumber, elementFactory, builderName);
    }
}
