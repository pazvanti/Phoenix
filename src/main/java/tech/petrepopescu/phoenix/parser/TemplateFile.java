package tech.petrepopescu.phoenix.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import tech.petrepopescu.phoenix.exception.ParsingException;
import tech.petrepopescu.phoenix.parser.elements.*;
import tech.petrepopescu.phoenix.spring.config.PhoenixConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class TemplateFile extends PhoenixFileParser {
    private final List<Element> lineElements = new ArrayList<>();
    private ConstructorElement constructorElement;
    private final StringBuilder builder = new StringBuilder();
    private final PhoenixConfiguration configuration;

    public TemplateFile(File file, String basePackage, ElementFactory elementFactory, PhoenixConfiguration configuration) {
        super(file, basePackage, elementFactory);
        this.configuration = configuration;
    }

    @Override
    protected void doParse() throws IOException {
        final List<String> lines = FileUtils.readLines(file, Charset.defaultCharset())
                .stream().filter(StringUtils::isNotBlank).toList();
        final String fileName = file.getName().substring(0, file.getName().indexOf(configuration.getViews().getExtension()));

        addMandatoryImports(fileName);

        int lineNumber = 0;
        int maxLines = lines.size();
        while (lineNumber < maxLines) {
            Element element = elementFactory.getElement(lines, lineNumber);
            if (element instanceof ImportElement) {
                imports.add(element);
            } else if(element instanceof ConstructorElement cElement) {
                cElement.setClassName(fileName);
                constructorElement = cElement;
            } else {
                lineElements.add(element);
            }
            lineNumber = element.parse(this.basePackage + "." + this.file.getName()) + 1;
        }

        if (constructorElement == null) {
            throw new ParsingException("No constructor defined for " + file.getName());
        }
    }

    public String className() {
        return this.constructorElement.getClassName();
    }

    @Override
    public String write() {
        if (!StringUtils.isEmpty(builder)) {
            return builder.toString();
        }

        builder.append("package ").append(basePackage).append(";\n\n");
        for (Element importElement:imports) {
            builder.append(importElement.write());
        }
        builder.append("\n");

        builder.append(this.constructorElement.write());

        builder.append("\n");
        builder.append("\tprivate void populateSectionCalls() {\n");
        builder.append("\t\tcontentBySections.put(\"html\", this::getContentForHtml);\n");
        builder.append("\t}\n");

        builder.append("\tpublic String getContent(PhoenixSpecialElementsUtil specialElementsUtil) {\n");
        builder.append("\t\t").append(" return getContentForSection(\"html\", specialElementsUtil);\n");
        builder.append("\t}\n");

        builder.append("\tpublic String getContentForSection(String sectionName, PhoenixSpecialElementsUtil specialElementsUtil) {\n");
        builder.append("\t\tif (!contentBySections.containsKey(sectionName)) {\n");
        builder.append("\t\t\treturn \"\";\n");
        builder.append("\t\t}\n");
        builder.append("\t\treturn contentBySections.get(sectionName).apply(specialElementsUtil);\n");
        builder.append("\t}\n\n");

        builder.append("\tpublic String getContentForHtml(PhoenixSpecialElementsUtil specialElementsUtil) {\n");
        builder.append("\t\t").append("final StringBuilder contentBuilder = new StringBuilder();\n");

        List<Element> nonInsertAtElements = lineElements.stream()
                .filter(element -> !(element instanceof InsertAtElement)).toList();
        for (Element element : nonInsertAtElements) {
            builder.append(element.write());
        }
        builder.append("\t\treturn contentBuilder.toString();\n");
        builder.append("\t}\n");
        builder.append("}\n");

        return builder.toString();
    }
}
