package tech.petrepopescu.phoenix.parser.elements;

import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.ElementRegistry;

import java.util.ArrayList;
import java.util.List;

public class InsertAtElement extends AbstractContainerElement {
    private final List<Element> nestedElements;
    public InsertAtElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
        this.nestedElements = new ArrayList<>();
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        parseContentInside(line, this.nestedElements, fileName);
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        return this.contentBuilder;
    }
}
