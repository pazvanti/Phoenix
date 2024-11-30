package tech.petrepopescu.phoenix.parser.elements.builders;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.ElseIfElement;

import java.util.List;

@Component
public class ElseIfElementBuilder extends ContainerElementBuilders {
    @Override
    public boolean isValid(String line) {
        return StringUtils.startsWithAny(line, " else if (", "else if(", " else if(", "else if (") ||
                StringUtils.startsWithAny(line, "} else if (", "}else if(", "} else if(", "}else if (");
    }

    @Override
    public Element buildFromLine(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        return new ElseIfElement(lines, lineNumber, elementFactory, builderName);
    }
}
