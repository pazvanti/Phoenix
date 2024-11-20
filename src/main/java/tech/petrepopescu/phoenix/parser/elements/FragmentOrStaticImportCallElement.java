package tech.petrepopescu.phoenix.parser.elements;

import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.parser.ElementFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FragmentOrStaticImportCallElement extends NestedElement {
    public FragmentOrStaticImportCallElement(List<String> lines, int lineIndex, ElementFactory elementFactory, String builderName) {
        super(lines, lineIndex, elementFactory, "", builderName);
    }

    @Override
    public int parse(String fileName) {
        final String line = StringUtils.trim(this.lines.get(this.lineNumber));
        int indexOfParamStart = StringUtils.indexOf(line, "(");
        int indexOfParamEnd = StringUtils.indexOf(line, ")", indexOfParamStart + 1);
        String fragmentName = StringUtils.substring(line, 1, indexOfParamStart);
        if (elementFactory.isStaticImport(fragmentName)) {
            return parseStaticImport(line, indexOfParamEnd, fileName);
        }
        return parseFragment(line, indexOfParamStart, indexOfParamEnd, fragmentName);
    }

    private int parseStaticImport(String line, int indexOfParamEnd, String fileName) {
        appendWithContentBuilder(StringUtils.substring(line, 1, indexOfParamEnd + 1));
        this.contentBuilder.append("\n");
        discoverNextElement(StringUtils.substring(line, indexOfParamEnd + 1), fileName);
        return this.lineNumber;
    }

    private int parseFragment(String line, int indexOfParamStart, int indexOfParamEnd, String fragmentName) {
        int indexOfTemplateCall = StringUtils.indexOf(fragmentName, ".template");
        if (indexOfTemplateCall < 0) {
            // this is NOT a fragment call
            return this.lineNumber;
        }

        String contentVariableName = null;
        line = StringUtils.trim(line);
        if (StringUtils.endsWith(line ,"{")) {
            // We have fragment content
            contentVariableName = "content_" + StringUtils.remove(UUID.randomUUID().toString(), '-');
            String contentBuilderName = contentVariableName + "ContentBuilder";
            this.elementFactory.setBuilderName(contentBuilderName);
            parseContentInside(line, fragmentName);
            this.elementFactory.resetBuilderName();
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs))
                    .append("PhoenixContent ").append(contentVariableName).append(" = new PhoenixContent() {\n");
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1)).append("public String render() {\n");
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 2)).append("StringBuilder ")
                    .append(contentVariableName).append("ContentBuilder = new StringBuilder();\n");
            for (Element element:this.nestedElements) {
                this.contentBuilder.append(element.write());
            }
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 2)).append("return ")
                    .append(contentVariableName).append("ContentBuilder.toString();\n");
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs + 1)).append("}\n");
            this.contentBuilder.append(StringUtils.repeat('\t', this.numTabs)).append("};\n");
        }
        fragmentName = StringUtils.substring(fragmentName, 0, indexOfTemplateCall);
        elementFactory.potentialFragmentCall(fragmentName);
        StringBuilder call = new StringBuilder("View.of(\"" + fragmentName + "\"");
        if (indexOfParamStart + 1 == indexOfParamEnd) {
            // no arguments
            if (contentVariableName != null) {
                call.append(", ").append(contentVariableName);
            }
            call.append(")");
        } else {
            call.append(", ").append(StringUtils.substring(line, indexOfParamStart + 1, indexOfParamEnd));
            if (contentVariableName != null) {
                call.append(", ").append(contentVariableName);
            }
            call.append(")");
        }
        call.append(".getContent(specialElementsUtil)");
        appendWithContentBuilder(call.toString());
        return this.lineNumber;
    }

    @Override
    public StringBuilder write() {
        if (this.nextElement != null) {
            this.contentBuilder.append(this.nextElement.write());
        }
        return this.contentBuilder;
    }
}
