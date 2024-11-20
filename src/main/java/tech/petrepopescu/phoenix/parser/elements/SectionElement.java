package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;
import tech.petrepopescu.phoenix.parser.ElementRegistry;

import java.util.List;

public class SectionElement extends AbstractContainerElement {
    private String sectionName = null;
    public SectionElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, builderName);
    }

    @Override
    public int parse(String fileName) {
        String line = this.lines.get(this.lineNumber);
        sectionName = extractStatement(line);
        parseContentInside(line, fileName);
        ElementRegistry.getInstance().register(sectionName, this.nestedElements);
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        for (Element element:ElementRegistry.getInstance().getElementsForSection(sectionName)) {
            this.contentBuilder.append(element.write());
        }
        return this.contentBuilder;
    }

    @Override
    public String getSectionName() {
        if (StringUtils.isBlank(sectionName)) {
            return DEFAULT_SECTION_NAME;
        }
        return sectionName;
    }

    @Override
    public String getBuilderName() {
        if (StringUtils.isBlank(sectionName)) {
            return this.builderName;
        }
        return sectionName + "ContentBuilder";
    }


    private String extractStatement(String line) {
        return StringUtils.substring(line, StringUtils.indexOf(line, '(') + 2, StringUtils.lastIndexOf(line, ')') - 1);
    }
}
