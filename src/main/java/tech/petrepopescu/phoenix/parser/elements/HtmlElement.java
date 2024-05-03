package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.List;

public class HtmlElement extends Element {
    public HtmlElement(List<String> lines, int lineNumber, ElementFactory elementFactory, String builderName) {
        super(lines, lineNumber, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(lineNumber);
        if (StringUtils.contains(line, '@')) {
            int indexOfAt = StringUtils.indexOf(line, '@');
            String untilAt = StringUtils.substring(line, 0, indexOfAt);
            appendAsStringWithContentBuilder(untilAt);
            discoverNextElement(StringUtils.substring(line, indexOfAt), fileName);
        } else {
            appendAsStringWithContentBuilder(line);
        }
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        } else {
            appendAsStringWithContentBuilder("\n");
        }
        return this.contentBuilder;
    }
}
