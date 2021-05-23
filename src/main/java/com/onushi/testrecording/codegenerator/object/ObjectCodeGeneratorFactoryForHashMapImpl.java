package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryForHashMapImpl implements ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    public ObjectCodeGeneratorFactoryForHashMapImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject() instanceof HashMap<?, ?>) {
            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName());

            objectCodeGenerator.requiredImports = Arrays.asList("java.util.Map", "java.util.HashMap;");

            HashMap<Object, Object> hashMap = (HashMap<Object, Object>)context.getObject();

            List<Object> keys = hashMap.keySet()
                    .stream()
                    .sorted(Comparator.comparing(k -> {
                        if (k == null) {
                            return "";
                        } else {
                            return k.toString();
                        }
                    }))
                    .collect(Collectors.toList());
            List<Object> values = new ArrayList<>(hashMap.values());

            List<ObjectCodeGenerator> keyGenerators = keys.stream()
                    .map(element -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            List<ObjectCodeGenerator> valueGenerators = values.stream()
                    .map(element -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), element))
                    .collect(Collectors.toList());

            List<ObjectCodeGenerator> allDependencies = new ArrayList<>(keyGenerators);
            allDependencies.addAll(valueGenerators);

            objectCodeGenerator.dependencies = allDependencies;

            String keyClassName = getElementsDeclaringType(keyGenerators);
            String valueClassName = getElementsDeclaringType(valueGenerators);

            String elementsInlineCode = keys.stream()
                    .map(key ->  new StringGenerator()
                            .setTemplate("{{objectName}}.put({{key}}, {{value}});\n")
                            .addAttribute("objectName", context.getObjectName())
                            .addAttribute("key", objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), key).getInlineCode())
                            .addAttribute("value", objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), hashMap.get(key)).getInlineCode())
                            .generate())
                    .collect(Collectors.joining(""));


            objectCodeGenerator.initCode = new StringGenerator()
                    .setTemplate("Map<{{keyClassName}}, {{valueClassName}}> {{objectName}} = new HashMap<>();\n" +
                            "{{elementsInlineCode}}")
                    .addAttribute("keyClassName", keyClassName)
                    .addAttribute("valueClassName", valueClassName)
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", elementsInlineCode)
                    .generate();

            objectCodeGenerator.declareClassName = new StringGenerator()
                    .setTemplate("Map<{{keyClassName}}, {{valueClassName}}>")
                    .addAttribute("keyClassName", keyClassName)
                    .addAttribute("valueClassName", valueClassName)
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