package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.List;

public class CsrfInputElement extends Element {
    private String startingWhitespaces = "";
    public CsrfInputElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        startingWhitespaces = StringUtils.substring(this.lines.get(this.lineNumber),
                0,
                StringUtils.indexOf(this.lines.get(this.lineNumber), "@csrf.input()"));
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        appendAsStringWithContentBuilder(startingWhitespaces);
        appendAsStringWithContentBuilder("<input type=\"hidden\" name=\"");
        appendWithContentBuilder("specialElementsUtil.getCsrfTokenName()");
        appendAsStringWithContentBuilder("\" value=\"");
        appendWithContentBuilder("specialElementsUtil.getCsrfTokenValue()");
        appendAsStringWithContentBuilder("\"/>\n");
        return this.contentBuilder;
    }
}
