package tech.petrepopescu.phoenix.parser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import tech.petrepopescu.phoenix.parser.compiler.Compiler;
import tech.petrepopescu.phoenix.parser.compiler.SourceCodeObject;
import tech.petrepopescu.phoenix.parser.route.RouteGenerator;
import tech.petrepopescu.phoenix.spring.config.PhoenixConfiguration;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class PhoenixParser {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PhoenixParser.class);
    public static final String VIEWS_BASE_PACKAGE = "views.html";
    public static final String CLASSPATH = "classpath*:";
    private final ElementFactory elementFactory;
    private final RouteGenerator routeGenerator;
    private final Compiler compiler;
    private final PhoenixConfiguration phoenixConfiguration;

    public PhoenixParser(ElementFactory elementFactory, RouteGenerator routeGenerator, Compiler compiler,
                         PhoenixConfiguration phoenixConfiguration) {
        this.elementFactory = elementFactory;
        this.routeGenerator = routeGenerator;
        this.compiler = compiler;
        this.phoenixConfiguration = phoenixConfiguration;
    }

    public void parse() {
        List<JavaFileObject> javaFileObjects = new ArrayList<>(routeGenerator.generateRoutes());
        final File viewsFolder;
        final StringBuilder basePackage = new StringBuilder(VIEWS_BASE_PACKAGE);
        List<TemplateFile> templateFiles = new ArrayList<>();
        if (StringUtils.startsWith(phoenixConfiguration.getViews().getPath(), CLASSPATH)) {
            templateFiles.addAll(parseFilesFromResource());
        } else {
            viewsFolder = new File(phoenixConfiguration.getViews().getPath());
            templateFiles.addAll(parseFilesInFolder(viewsFolder, basePackage));
        }

        javaFileObjects.addAll(writeFiles(templateFiles));
        javaFileObjects.add(buildStaticStringsFile());

        elementFactory.unknownFragmentsExists();
        compiler.compileAndLoad(javaFileObjects);
    }

    private List<JavaFileObject> writeFiles(List<TemplateFile> templateFiles) {
        List<JavaFileObject> javaFileObjects = new ArrayList<>();
        for (TemplateFile templateFile:templateFiles) {
            JavaFileObject javaFileObject = new SourceCodeObject(templateFile.className(), templateFile.write(), templateFile.basePackage, true);
            javaFileObjects.add(javaFileObject);
            if (!StringUtils.endsWith(templateFile.getBasePackage(), ".")) {
                elementFactory.defineNewFragment(templateFile.getBasePackage() + "." + templateFile.className());
            } else {
                elementFactory.defineNewFragment(templateFile.getBasePackage() + templateFile.className());
            }
        }
        return javaFileObjects;
    }

    private JavaFileObject buildStaticStringsFile() {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(VIEWS_BASE_PACKAGE).append(";\n\n");
        builder.append("public class StaticStrings {\n");
        for (Map.Entry<String, String> staticVariables:VariableRegistry.getInstance().getStaticStrings().entrySet()) {
            builder.append("\tpublic static final String ").append(staticVariables.getKey()).append(" = \"").append(staticVariables.getValue()).append("\";\n");
        }
        builder.append("}");
        return new SourceCodeObject("StaticStrings", builder.toString(), VIEWS_BASE_PACKAGE, true);
    }

    public void parse(File viewsFolder) {
        List<JavaFileObject> javaFileObjects = new ArrayList<>(routeGenerator.generateRoutes());
        List<TemplateFile> templateFiles = parseFilesInFolder(viewsFolder, new StringBuilder(VIEWS_BASE_PACKAGE));
        javaFileObjects.addAll(writeFiles(templateFiles));
        elementFactory.unknownFragmentsExists();
        compiler.compileAndLoad(javaFileObjects);
    }

    private List<TemplateFile> parseFilesFromResource() {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        String rootFolder = StringUtils.substring(phoenixConfiguration.getViews().getPath(), CLASSPATH.length());
        Resource[] resources;
        String rootFolderPath;
        try {
            resources = resourcePatternResolver.getResources(CLASSPATH + rootFolder + "/**");
            if (resources.length == 0) {
                return Collections.emptyList();
            }
            rootFolderPath = resources[0].getFile().getParentFile().getPath();
        } catch (Exception e) {
            log.error("Error parsing classpath");
            return Collections.emptyList();
        }
        List<TemplateFile> templateFiles = new ArrayList<>();
        for (Resource resource:resources) {
            if (resource.isReadable()) {
                File file = null;
                try {
                    file = resource.getFile();
                    String relativePath = getRelativePath(resource, rootFolderPath);
                    parseFile(file, relativePath);
                } catch (Exception e) {
                    if (file == null) {
                        log.error("There was an error reading a resource file");
                    } else {
                        log.error("There was an error parsing the file " + file.getName(), e);
                    }
                }
            }
        }

        return templateFiles;
    }

    public TemplateFile parseFile(File file, String relativePath) throws IOException {
        if (StringUtils.isNotBlank(relativePath)) {
            relativePath = "." + relativePath;
        }
        TemplateFile templateFile = new TemplateFile(file, VIEWS_BASE_PACKAGE + relativePath, elementFactory, phoenixConfiguration);
        templateFile.parse();
        return templateFile;
    }

    private String getRelativePath(Resource resource, String rootFolderPath) throws IOException {
        String resourcePath = resource.getURL().getPath();
        String relativePath = StringUtils.substring(resourcePath,rootFolderPath.length() + 1);

        int startIndex = 0;
        if (relativePath.startsWith("/")) {
            startIndex = 1;
        }
        relativePath = StringUtils.substring(relativePath, startIndex, StringUtils.indexOf(relativePath, resource.getFilename()));

        // Replace path separators with dots (transform into package-like structure)
        relativePath = relativePath.replace("/", ".").replace("\\", ".");
        if (StringUtils.endsWith(relativePath, ".")) {
            return StringUtils.substring(relativePath, 0, relativePath.length() - 1);
        }
        return relativePath;
    }

    private List<TemplateFile> parseFilesInFolder(File folder, StringBuilder basePackage) {
        if (!folder.exists() || folder.listFiles().length == 0) {
            log.warn("Nothing to parse in {}", folder.getName());
            return Collections.emptyList();
        }

        List<TemplateFile> templateFiles = new ArrayList<>();
        final List<File> files = Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(f -> StringUtils.endsWith(f.getName(), phoenixConfiguration.getViews().getExtension())).toList();
        for (File file:files) {
            TemplateFile templateFile = new TemplateFile(file, basePackage.toString(), elementFactory, phoenixConfiguration);
            try {
                templateFile.parse();
                templateFiles.add(templateFile);
                elementFactory.defineNewFragment(templateFile.getBasePackage() + "." + templateFile.className());
            } catch (Exception e) {
                log.error("There was an error parsing the file " + file.getName(), e);
            }
        }

        final List<File> folders = Arrays.stream(folder.listFiles()).filter(File::isDirectory).toList();
        for (File newFolder:folders) {
            templateFiles.addAll(parseFilesInFolder(newFolder, new StringBuilder(basePackage + "." + newFolder.getName())));
        }

        return templateFiles;
    }
}
