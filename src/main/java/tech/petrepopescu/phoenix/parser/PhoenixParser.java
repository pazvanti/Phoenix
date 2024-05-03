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
    private static final String VIEWS_BASE_PACKAGE = "views.html";
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
        if (StringUtils.startsWith(phoenixConfiguration.getViews().getPath(), CLASSPATH)) {
            javaFileObjects.addAll(parseFilesFromResource());
        } else {
            viewsFolder = new File(phoenixConfiguration.getViews().getPath());
            javaFileObjects.addAll(parseFilesInFolder(viewsFolder, basePackage));
        }

        elementFactory.unknownFragmentsExists();
        compiler.compileAndLoad(javaFileObjects);
    }

    public void parse(File viewsFolder) {
        List<JavaFileObject> javaFileObjects = new ArrayList<>(routeGenerator.generateRoutes());
        javaFileObjects.addAll(parseFilesInFolder(viewsFolder, new StringBuilder(VIEWS_BASE_PACKAGE)));
        elementFactory.unknownFragmentsExists();
        compiler.compileAndLoad(javaFileObjects);
    }

    private List<JavaFileObject> parseFilesFromResource() {
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
        List<JavaFileObject> javaFileObjects = new ArrayList<>();
        for (Resource resource:resources) {
            if (resource.isReadable()) {
                File file = null;
                try {
                    file = resource.getFile();
                    String relativePath = getRelativePath(resource, rootFolderPath);
                    parseFile(file, relativePath, javaFileObjects);
                } catch (Exception e) {
                    if (file == null) {
                        log.error("There was an error reading a resource file");
                    } else {
                        log.error("There was an error parsing the file " + file.getName(), e);
                    }
                }
            }
        }

        return javaFileObjects;
    }

    public void parseFile(File file, String relativePath, List<JavaFileObject> javaFileObjects) throws IOException {
        if (StringUtils.isNotBlank(relativePath)) {
            relativePath = "." + relativePath;
        }
        TemplateFile templateFile = new TemplateFile(file, VIEWS_BASE_PACKAGE + relativePath, elementFactory, phoenixConfiguration);
        templateFile.parse();
        JavaFileObject javaFileObject = new SourceCodeObject(templateFile.className(), templateFile.write(), VIEWS_BASE_PACKAGE + relativePath, true);
        javaFileObjects.add(javaFileObject);
        if (!StringUtils.endsWith(templateFile.getBasePackage(), ".")) {
            elementFactory.defineNewFragment(templateFile.getBasePackage() + "." + templateFile.className());
        } else {
            elementFactory.defineNewFragment(templateFile.getBasePackage() + templateFile.className());
        }
    }

    private String getRelativePath(Resource resource, String rootFolderPath) throws IOException {
        String resourcePath = resource.getURL().getPath();
        String relativePath = StringUtils.substring(resourcePath,rootFolderPath.length() + 1);
        relativePath = StringUtils.substring(relativePath, 1, StringUtils.indexOf(relativePath, resource.getFilename()));

        // Replace path separators with dots (transform into package-like structure)
        relativePath = relativePath.replace("/", ".").replace("\\", ".");
        if (StringUtils.endsWith(relativePath, ".")) {
            return StringUtils.substring(relativePath, 0, relativePath.length() - 1);
        }
        return relativePath;
    }

    private List<JavaFileObject> parseFilesInFolder(File folder, StringBuilder basePackage) {
        if (!folder.exists() || folder.listFiles().length == 0) {
            log.warn("Nothing to parse in {}", folder.getName());
            return Collections.emptyList();
        }

        List<JavaFileObject> javaFileObjects = new ArrayList<>();
        final List<File> files = Arrays.stream(Objects.requireNonNull(folder.listFiles())).filter(f -> StringUtils.endsWith(f.getName(), phoenixConfiguration.getViews().getExtension())).toList();
        for (File file:files) {
            TemplateFile templateFile = new TemplateFile(file, basePackage.toString(), elementFactory, phoenixConfiguration);
            try {
                templateFile.parse();
                JavaFileObject javaFileObject = new SourceCodeObject(templateFile.className(), templateFile.write(), basePackage.toString(), true);
                javaFileObjects.add(javaFileObject);
                elementFactory.defineNewFragment(templateFile.getBasePackage() + "." + templateFile.className());
            } catch (Exception e) {
                log.error("There was an error parsing the file " + file.getName(), e);
            }
        }

        final List<File> folders = Arrays.stream(folder.listFiles()).filter(File::isDirectory).toList();
        for (File newFolder:folders) {
            javaFileObjects.addAll(parseFilesInFolder(newFolder, new StringBuilder(basePackage + "." + newFolder.getName())));
        }

        return javaFileObjects;
    }
}
