package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryForHashSetImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    public ObjectCodeGeneratorFactoryForHashSetImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() instanceof HashSet<?>) {
            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

            objectCodeGenerator.requiredImports = Arrays.asList("java.util.Set", "java.util.HashSet");

            HashSet<Object> hashSet = (HashSet<Object>)context.getObject();

            List<Object> elements = Arrays.stream(hashSet.toArray())
                    .sorted(Comparator.comparing(k -> {
                        if (k == null) {
                            return "";
                        } else {
                            return k.toString();
                        }
                    }))
                    .collect(Collectors.toList());

            objectCodeGenerator.dependencies = elements.stream()
                    .map(element -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            String elementClassName = getElementsDeclaringType(objectCodeGenerator.dependencies);

            String elementsInlineCode = elements.stream()
                    .map(element ->  new StringGenerator()
                            .setTemplate("{{objectName}}.add({{element}});\n")
                            .addAttribute("objectName", context.getObjectName())
                            .addAttribute("element", objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element).getInlineCode())
                            .generate())
                    .collect(Collectors.joining(""));


            objectCodeGenerator.initCode = new StringGenerator()
                    .setTemplate("Set<{{elementClassName}}> {{objectName}} = new HashSet<>();\n" +
                            "{{elementsInlineCode}}")
                    .addAttribute("elementClassName", elementClassName)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            objectCodeGenerator.declareClassName = new StringGenerator()
                    .setTemplate("Set<{{elementClassName}}>")
                    .addAttribute("elementClassName", elementClassName)
                    .generate();

            return objectCodeGenerator;
        } else {
            return null;
        }
    }

    // TODO IB !!!! remove duplication
    public String getElementsDeclaringType(List<ObjectCodeGenerator> objectCodeGenerators) {
        List<String> distinct = objectCodeGenerators.stream()
                .filter(x -> !x.inlineCode.equals("null"))
                .map(ObjectCodeGenerator::getDeclareClassName)
                .distinct()
                .collect(Collectors.toList());
        if (distinct.size() == 1) {
            return distinct.get(0);
        } else {
            return "Object";
        }
    }
}