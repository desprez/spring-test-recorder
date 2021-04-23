package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import com.onushi.testrecording.generator.object.ObjectInfo;
import com.onushi.testrecording.generator.object.ObjectInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class TestGenFactory {
    private final ObjectInfoService objectInfoService;
    private final ObjectNamesService objectNamesService;
    private final ClassInfoService classInfoService;

    public TestGenFactory(ObjectInfoService objectInfoService, ObjectNamesService objectNamesService, ClassInfoService classInfoService) {
        this.objectInfoService = objectInfoService;
        this.objectNamesService = objectNamesService;
        this.classInfoService = classInfoService;
    }

    public TestGenInfo createTestGenInfo(MethodRunInfo methodRunInfo) {
        TestGenInfo testGenInfo = new TestGenInfo();

        testGenInfo.targetObjectInfo = objectInfoService.createObjectInfo(methodRunInfo.getTarget(),
                classInfoService.getObjectNameBase(methodRunInfo.getTarget()));
        testGenInfo.packageName = classInfoService.getPackageName(methodRunInfo.getTarget());
        testGenInfo.shortClassName = classInfoService.getShortClassName(methodRunInfo.getTarget());
        testGenInfo.methodName = methodRunInfo.getMethodName();

        testGenInfo.argumentObjectInfos = methodRunInfo.getArguments().stream()
                .map(x -> objectInfoService.createObjectInfo(x, objectNamesService.generateObjectName(testGenInfo, x)))
                .collect(Collectors.toList());

        testGenInfo.resultObjectInfo = objectInfoService.createObjectInfo(methodRunInfo.getResult(), "expectedResult");


        setRequiredImports(testGenInfo);

        setRequiredHelperObjects(testGenInfo);

        setObjectsInit(testGenInfo);

        setArgumentsInlineCode(testGenInfo);

        testGenInfo.resultInit = testGenInfo.resultObjectInfo.getInit();

        return testGenInfo;
    }

    private void setRequiredImports(TestGenInfo testGenInfo) {
        testGenInfo.requiredImports = new ArrayList<>();
        testGenInfo.requiredImports.add("org.junit.jupiter.api.Test");
        testGenInfo.requiredImports.add("static org.junit.jupiter.api.Assertions.*");
        testGenInfo.requiredImports.addAll(testGenInfo.argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredImports().stream()).collect(Collectors.toList()));
        testGenInfo.requiredImports.addAll(testGenInfo.resultObjectInfo.getRequiredImports());
        testGenInfo.requiredImports = testGenInfo.requiredImports.stream().distinct().collect(Collectors.toList());
    }

    private void setRequiredHelperObjects(TestGenInfo testGenInfo) {
        testGenInfo.requiredHelperObjects = testGenInfo.argumentObjectInfos.stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .collect(Collectors.toList());
        testGenInfo.requiredHelperObjects.addAll(testGenInfo.resultObjectInfo.getRequiredHelperObjects());
        testGenInfo.requiredHelperObjects = testGenInfo.requiredHelperObjects.stream().distinct().collect(Collectors.toList());
    }

    private void setObjectsInit(TestGenInfo testGenInfo) {
        testGenInfo.objectsInit = testGenInfo.argumentObjectInfos.stream()
                .map(ObjectInfo::getInit).collect(Collectors.toList());
        testGenInfo.objectsInit = testGenInfo.objectsInit.stream()
                .filter(x -> !x.equals(""))
                .distinct()
                .collect(Collectors.toList());
    }

    private void setArgumentsInlineCode(TestGenInfo testGenInfo) {
        testGenInfo.argumentsInlineCode = testGenInfo.argumentObjectInfos.stream()
                .map(ObjectInfo::getInlineCode)
                .collect(Collectors.toList());
    }
}
