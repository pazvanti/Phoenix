package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.List;

public class WithElement extends AbstractContainerElement {
    private String statement;

    public WithElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        this.statement = extractStatement(line);
        parseContentInside(line, fileName);

        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("{\n")
                .append(StringUtils.repeat('\t', this.numTabs + 1))
                .append("var ").append(statement).append(";\n");
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("}\n");
        return this.contentBuilder;
    }

    private String extractStatement(String line) {
        return StringUtils.substring(line, StringUtils.indexOf(line, '(') + 1, StringUtils.lastIndexOf(line, ')'));
    }
}
