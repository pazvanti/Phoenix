package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.VariableRegistry;

import java.util.List;

public class VariableElement extends Element {
    private String evalExpression;
    public VariableElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = lines.get(lineNumber).trim();
        int elementEnd = indexOfElementEnd(line, 0);
        String variableName = StringUtils.substring(line, 1, elementEnd);
        if ("String".equals(VariableRegistry.getInstance().getType(fileName, variableName))) {
            this.evalExpression = "StringEscapeUtils.escapeHtml4(" + variableName + ")";
        } else if (StringUtils.contains(variableName, "(")) {
            this.evalExpression = "StringEscapeUtils.escapeHtml4(String.valueOf(" + variableName + "))";
        } else {
            this.evalExpression = variableName;
        }
        appendWithContentBuilder(this.evalExpression);
        discoverNextElement(StringUtils.substring(line, elementEnd), fileName);
        return this.lineNumber;
    }

    public String getEvalExpression() {
        return this.evalExpression;
    }

    @Override
    public StringBuilder write() {
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
