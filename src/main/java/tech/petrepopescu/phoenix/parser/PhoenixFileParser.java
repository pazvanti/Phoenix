package tech.petrepopescu.phoenix.parser;

import tech.petrepopescu.phoenix.parser.elements.Element;
import tech.petrepopescu.phoenix.parser.elements.ImportElement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static tech.petrepopescu.phoenix.parser.PhoenixParser.VIEWS_BASE_PACKAGE;

public abstract class PhoenixFileParser {
    protected final File file;
    protected final ElementFactory elementFactory;
    protected final String basePackage;
    protected final List<Element> imports = new ArrayList<>();
    private Boolean parsed = false;

    protected PhoenixFileParser(File file, String basePackage, ElementFactory elementFactory) {
        this.file = file;
        this.elementFactory = elementFactory;
        this.basePackage = basePackage;
    }

    public void parse() throws IOException {
        if (Boolean.TRUE.equals(parsed)) {
            return;
        }
        doParse();
        parsed = true;
    }

    protected abstract void doParse() throws IOException;
    public abstract String write();

    void addMandatoryImports(String fileName) {
        Element phoenixFormatImport = new ImportElement("tech.petrepopescu.phoenix.format.*");
        Element phoenixViewImport = new ImportElement("tech.petrepopescu.phoenix.views.View");
        Element phoenixSpecialImport = new ImportElement("tech.petrepopescu.phoenix.special.*");
        Element escapeUtilsImport = new ImportElement("org.apache.commons.text.StringEscapeUtils");
        Element springImport = new ImportElement("org.springframework.web.servlet.support.ServletUriComponentsBuilder");
        Element mapImport = new ImportElement("java.util.Map");
        Element hashMapImport = new ImportElement("java.util.HashMap");
        Element functionImport = new ImportElement("java.util.function.Function");
        Element staticStringsImport = new ImportElement("static " + VIEWS_BASE_PACKAGE + ".StaticStrings.*");
        imports.add(phoenixFormatImport);
        imports.add(phoenixViewImport);
        imports.add(phoenixSpecialImport);
        imports.add(escapeUtilsImport);
        imports.add(springImport);
        imports.add(staticStringsImport);
        imports.add(mapImport);
        imports.add(hashMapImport);
        imports.add(functionImport);
        for (Element importElement:imports) {
            importElement.parse(fileName);
        }
    }

    public String getBasePackage() {
        return basePackage;
    }
}
