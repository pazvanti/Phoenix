package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;

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
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        return write(List.of());
    }

    public StringBuilder write(List<FragmentOrStaticImportCallElement> fragments) {
        for (Element element:this.nestedElements) {
            this.contentBuilder.append(element.write());
        }
        for (FragmentOrStaticImportCallElement fragment:fragments) {
            appendWithContentBuilder(fragment.getContentForSection(this.sectionName).toString());
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
