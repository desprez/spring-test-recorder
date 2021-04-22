package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.classInfo.ClassInfoService;
import com.onushi.testrecording.analizer.methodrun.MethodRunInfo;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

// TODO IB use a templating engine here. see https://www.baeldung.com/thymeleaf-generate-pdf
@Service
public class TestGenService {
    public String generateTestString(TestGenInfo testGenInfo) {
        return getBeginMarkerString() +
                getPackageString(testGenInfo) +
                getImportsString(testGenInfo) +
                "\n" +
                getClassAndTestString(testGenInfo) +
                getEndMarkerString();
    }

    // TODO IB !!!! split into arrange, act, assert

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(TestGenInfo testGenInfo) {
        return String.format("package %s;%n%n", testGenInfo.getPackageName());
    }

    private String getImportsString(TestGenInfo testGenInfo) {
        return testGenInfo.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private StringBuilder getClassAndTestString(TestGenInfo testGenInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        String argumentsRequiredHelperObjects = testGenInfo.getRequiredHelperObjects().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining(""));
        String argumentsInit = testGenInfo.getObjectsInit().stream()
                .map(x -> String.format("        %s%n", x)).collect(Collectors.joining(""));
        String argumentsCode = String.join(", ", testGenInfo.getArgumentsInlineCode());

        // TODO IB create a result variable
        // TODO IB move expectedResult at end

        stringBuilder.append(String.format("class %sTest {%n", testGenInfo.getShortClassName()));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", testGenInfo.getMethodName()));
        stringBuilder.append(argumentsRequiredHelperObjects);
        stringBuilder.append(argumentsInit);
        stringBuilder.append(String.format("        %s %s = new %s();%n", testGenInfo.getShortClassName(), testGenInfo.getTargetObjectName(), testGenInfo.getShortClassName()));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n",
                testGenInfo.getTargetObjectName(), testGenInfo.getMethodName(), argumentsCode, testGenInfo.getResultObjectInfo().getInlineCode()));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
