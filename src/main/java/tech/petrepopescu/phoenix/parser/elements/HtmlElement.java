package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringEscapeUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.VariableRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HtmlElement extends Element {
    private String variableName;
    public HtmlElement(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        super(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(lineNumber);
        if (StringUtils.contains(line, '@')) {
            int indexOfAt = StringUtils.indexOf(line, '@');
            String untilAt = StringUtils.substring(line, 0, indexOfAt);
            variableName = VariableRegistry.getInstance().getOrDefineStaticString(StringEscapeUtils.escapeJava(untilAt));
            appendWithContentBuilder(variableName);
            discoverNextElement(StringUtils.substring(line, indexOfAt), fileName);
        } else {
            variableName = VariableRegistry.getInstance().getOrDefineStaticString(StringEscapeUtils.escapeJava(line));
            appendWithContentBuilder(variableName);
        }
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        } else {
            appendWithContentBuilder(VariableRegistry.getInstance().getOrDefineStaticString(StringEscapeUtils.escapeJava("\n")));
        }
        return this.contentBuilder;
    }

    public boolean hasNextElement() {
        return this.nextElement != null;
    }

    public String getVariableName() {
        return this.variableName;
    }
}
