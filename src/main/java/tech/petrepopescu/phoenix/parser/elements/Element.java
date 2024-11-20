package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringEscapeUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Element {
    public static final String DEFAULT_SECTION_NAME = "html";
    private static final List<Character> END_CHARACTERS = List.of('\n', '\r', ' ', '\'', '\"', '<', ';');

    protected final ElementFactory elementFactory;
    protected final List<String> lines;
    protected String builderName;
    protected int lineNumber;
    protected int numTabs = 2;
    protected StringBuilder contentBuilder = new StringBuilder();
    protected Element nextElement = null;

    protected Element(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        this.lines = lines;
        this.lineNumber = lineIndex;
        this.elementFactory = elementFactory;
        this.builderName = builderName;
    }

    protected void appendWithContentBuilder(String line) {
        appendWithContentBuilder(line, this.numTabs);
    }
    protected void appendWithContentBuilder(String line, int numTabs) {
        this.contentBuilder.append(StringUtils.repeat('\t', numTabs))
                .append(builderName)
                .append(".append(")
                .append(line)
                .append(");")
                .append("\n");
    }

    protected void appendAsStringWithContentBuilder(String line) {
        this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs))
                .append(builderName)
                .append(".append(").append('\"')
                .append(StringEscapeUtils.escapeJava(line)).append('\"')
                .append(");")
                .append("\n");
    }

    protected void appendAsCode(String code) {
        appendAsCode(code, this.numTabs);
    }

    protected void appendAsCode(String code, int tabs) {
        this.contentBuilder.append(StringUtils.repeat('\t', tabs))
                .append(code);
    }

    protected void discoverNextElement(String line, String fileName) {
        if (!StringUtils.isEmpty(line)) {
            Element element = this.elementFactory.getElement(line);
            element.tabs(this.numTabs);
            element.parse(fileName);
            this.nextElement = element;
        }
    }

    protected int discoverNextElement(String line, List<String> lines, int lineNumber, String fileName) {
        if (!StringUtils.isEmpty(line)) {
            Element element = this.elementFactory.getElement(lines, lineNumber);
            element.tabs(this.numTabs);
            int newLineNumber = element.parse(fileName);
            this.nextElement = element;
            return newLineNumber;
        }
        return lineNumber;
    }


    protected int indexOfElementEnd(String line, int start) {
        return indexOfElementEnd(line, start, false);
    }

    protected int indexOfElementEnd(String partialLine, int start, boolean checkQuote) {
        int end = start;
        int countOpenParentheses = 0;
        int countEndParentheses = 0;
        int countQuote = 0;
        while (end < partialLine.length()) {
            if (partialLine.charAt(end) == '(') {
                countOpenParentheses++;
            }
            if (partialLine.charAt(end) == ')') {
                countEndParentheses++;
            }
            if (partialLine.charAt(end) == '\"') {
                countQuote++;
            }
            if (END_CHARACTERS.contains(partialLine.charAt(end)) && countEndParentheses == countOpenParentheses) {
                if (partialLine.charAt(end) == ')') {
                    // If we end on `)` we need to include it as well since it is the end of a function call
                    end++;
                }
                if (checkQuote) {
                    if (countQuote % 2 == 0) {
                        return end;
                    }
                } else {
                    return end;
                }
            }
            end++;
        }

        return end;
    }

    public String getSectionName() {
        return DEFAULT_SECTION_NAME;
    }

    public void tabs(int numTabs) {
        this.numTabs = numTabs;
    }

    public void setBuilderName(String builderName) {
        this.builderName = builderName;
        if (this.nextElement != null) {
            this.nextElement.setBuilderName(builderName);
        }
    }

    /**
     * Parses the given element
     * @param fileName The base package and the file name which this element belongs to
     * @return The next line number
     */
    public abstract int parse(String fileName);

    public abstract StringBuilder write();

    public String getBuilderName() {
        return this.builderName;
    }
}
