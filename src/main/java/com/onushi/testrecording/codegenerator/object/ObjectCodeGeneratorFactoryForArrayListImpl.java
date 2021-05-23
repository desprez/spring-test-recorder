package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.classInfo.ClassInfoService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryForArrayListImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;
    private final ClassInfoService classInfoService;

    public ObjectCodeGeneratorFactoryForArrayListImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager,
                                                      ClassInfoService classInfoService) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
        this.classInfoService = classInfoService;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() instanceof List<?>) {
            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

            objectCodeGenerator.requiredImports = Arrays.asList("java.util.List", "java.util.Arrays");

            // TODO IB this part is repeated
            List<ObjectCodeGenerator> elements = ((List<Object>) context.getObject()).stream()
                    .map(element -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            objectCodeGenerator.dependencies = elements.stream()
                    .distinct()
                    .collect(Collectors.toList());

            String elementClassSimpleName = classInfoService.getElementClassSimpleName((List<Object>) context.getObject());

            String elementsInlineCode = elements.stream()
                    .map(ObjectCodeGenerator::getInlineCode).collect(Collectors.joining(", "));

            objectCodeGenerator.initCode = new StringGenerator()
                    .setTemplate("List<{{elementClassSimpleName}}> {{objectName}} = Arrays.asList({{elementsInlineCode}});")
                    .addAttribute("elementClassSimpleName", elementClassSimpleName)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            objectCodeGenerator.declareClassName = new StringGenerator()
                    .setTemplate("List<{{elementClassSimpleName}}>")
                    .addAttribute("elementClassSimpleName", elementClassSimpleName)
                    .generate();

            return objectCodeGenerator;
        } else {
            return null;
        }
    }
}
