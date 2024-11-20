package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.ElementRegistry;

import java.util.List;

public class InsertAtElement extends AbstractContainerElement {
    public InsertAtElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String sectionName = StringUtils.substring(this.builderName, 0, StringUtils.lastIndexOf(this.builderName, "ContentBuilder"));
        ElementRegistry.getInstance().register(sectionName, this);
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
