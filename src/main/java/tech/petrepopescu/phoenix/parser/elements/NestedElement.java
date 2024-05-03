package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.ArrayList;
import java.util.List;

public class NestedElement extends AbstractContainerElement {
    protected String statement;
    protected final String type;
    protected final List<Element> nestedElements;

    public NestedElement(List<String> fileLines, int lineIndex, ElementFactory elementFactory, String type, String builderName) {
        super(fileLines, lineIndex, elementFactory, builderName);
        this.type = type;
        this.nestedElements = new ArrayList<>();
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        this.statement = extractStatement(line);
        parseContentInside(line, this.nestedElements, fileName);

        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append(type).append(" (").append(statement).append(") {\n");
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("}\n");
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }

    protected String extractStatement(String line) {
        return StringUtils.substring(line, StringUtils.indexOf(line, '(') + 1, StringUtils.lastIndexOf(line, ')'));
    }
}
