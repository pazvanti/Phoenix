package tech.petrepopescu.phoenix.parser.elements;

import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.List;

public class InsertAtElement extends AbstractContainerElement {
    public InsertAtElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        parseContentInside(line, fileName);
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