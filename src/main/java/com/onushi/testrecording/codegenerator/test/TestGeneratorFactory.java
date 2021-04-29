package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGenerator;
import com.onushi.testrecording.codegenerator.object.ObjectCodeGeneratorFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestGeneratorFactory {
    private final ObjectNameGenerator objectNameGenerator;
    private final ObjectCodeGeneratorFactory objectCodeGeneratorFactory;

    public TestGeneratorFactory(ObjectNameGenerator objectNameGenerator,
                                ObjectCodeGeneratorFactory objectCodeGeneratorFactory) {
        this.objectNameGenerator = objectNameGenerator;
        this.objectCodeGeneratorFactory = objectCodeGeneratorFactory;
    }

    public TestGenerator createTestGenerator(MethodRunInfo methodRunInfo) throws Exception {
        TestGenerator testGenerator = new TestGenerator();

        if (methodRunInfo.getArguments() == null) {
            throw new IllegalArgumentException("arguments");
        }
        if (methodRunInfo.getTarget() == null) {
            throw new IllegalArgumentException("target");
        }

        testGenerator.targetObjectCodeGenerator = objectCodeGeneratorFactory.getNamedObjectCodeGenerator(testGenerator,
                methodRunInfo.getTarget(),
                objectNameGenerator.getBaseObjectName(methodRunInfo.getTarget()));
        testGenerator.packageName = methodRunInfo.getTarget().getClass().getPackage().getName();
        testGenerator.shortClassName = methodRunInfo.getTarget().getClass().getSimpleName();
        testGenerator.methodName = methodRunInfo.getMethodName();

        testGenerator.argumentObjectCodeGenerators = methodRunInfo.getArguments().stream()
                .map(x -> objectCodeGeneratorFactory.getCommonObjectCodeGenerator(testGenerator, x))
                .collect(Collectors.toList());

        testGenerator.expectedResultObjectCodeGenerator = objectCodeGeneratorFactory.getNamedObjectCodeGenerator(testGenerator,
                methodRunInfo.getResult(), "expectedResult");
        testGenerator.resultType = methodRunInfo.getResultType();
        testGenerator.expectedException = methodRunInfo.getException();

        testGenerator.requiredImports = getRequiredImports(testGenerator);

        testGenerator.requiredHelperObjects = getRequiredHelperObjects(testGenerator);

        testGenerator.objectsInit = getObjectsInit(testGenerator.argumentObjectCodeGenerators);

        testGenerator.argumentsInlineCode = getArgumentsInlineCode(testGenerator);

        testGenerator.expectedResultInit = getObjectsInit(Collections.singletonList(testGenerator.expectedResultObjectCodeGenerator));

        return testGenerator;
    }

    private List<String> getRequiredImports(TestGenerator testGenerator) {
        List<String> result = new ArrayList<>();
        result.add("org.junit.jupiter.api.Test");
        result.add("static org.junit.jupiter.api.Assertions.*");
        result.addAll(testGenerator.getObjectCodeGeneratorCache().values().stream()
                .flatMap(x -> x.getRequiredImports().stream())
                .collect(Collectors.toList()));
        return result.stream().distinct().collect(Collectors.toList());
    }

    private List<String> getRequiredHelperObjects(TestGenerator testGenerator) {
        return testGenerator.getObjectCodeGeneratorCache().values().stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<String> getObjectsInit(List<ObjectCodeGenerator> objectCodeGenerators) {
        Set<String> objectInitsAlreadyAdded = new HashSet<>();

        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectCodeGenerator argumentObjectCodeGenerator : objectCodeGenerators) {
            List<String> objectsInit = getObjectsInit(argumentObjectCodeGenerator);
            for (String objectInit : objectsInit) {
                if (objectInitsAlreadyAdded.add(objectInit)) {
                    allObjectsInit.add(objectInit);
                }
            }
        }
        return allObjectsInit;
    }

    private List<String> getObjectsInit(ObjectCodeGenerator objectCodeGenerator) {
        if (objectCodeGenerator.isInitPrepared() || objectCodeGenerator.isInitDone()) {
            // to avoid cyclic traversal
            return new ArrayList<>();
        }
        objectCodeGenerator.setInitPrepared(true);
        List<String> allObjectsInit = new ArrayList<>();
        for (ObjectCodeGenerator dependency : objectCodeGenerator.getDependencies()) {
            allObjectsInit.addAll(getObjectsInit(dependency));
        }
        if (!objectCodeGenerator.getInitCode().equals("")) {
            allObjectsInit.add(objectCodeGenerator.getInitCode());
        }
        objectCodeGenerator.setInitDone(true);
        return allObjectsInit;
    }

    private List<String> getArgumentsInlineCode(TestGenerator testGenerator) {
        return testGenerator.argumentObjectCodeGenerators.stream()
                .map(ObjectCodeGenerator::getInlineCode)
                .collect(Collectors.toList());
    }
}
