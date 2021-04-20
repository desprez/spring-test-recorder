package com.onushi.testrecording.generator;

import com.onushi.testrecording.analizer.object.ObjectInfo;
import com.onushi.testrecording.analizer.TestRunInfo;
import org.springframework.stereotype.Component;
import java.util.Locale;
import java.util.stream.Collectors;

// TODO IB !!!! use a templating engine here
// TODO IB !!!! see https://www.baeldung.com/thymeleaf-generate-pdf
// TODO IB ar trebui o faza de pre-generare in care sa analizez toate dependintele. sa vad duplicate, sa gasesc ordinea, sa dau nume la variabile
@Component
public class TestGenerator {

    public String getTestString(TestRunInfo testRunInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getBeginMarkerString());
        stringBuilder.append(getPackageString(testRunInfo.getPackageName()));
        stringBuilder.append(getImportsString(testRunInfo));
        stringBuilder.append("\n");
        stringBuilder.append(getClassAndTestString(testRunInfo));
        stringBuilder.append(getEndMarkerString());
        return stringBuilder.toString();
    }

    private String getBeginMarkerString() {
        return String.format("%nBEGIN GENERATED TEST =========%n%n");
    }

    private String getPackageString(String packageName) {
        return String.format("package %s;%n%n", packageName);
    }

    private String getImportsString(TestRunInfo testRunInfo) {
        return testRunInfo.getRequiredImports().stream()
                .map(x -> String.format("import %s;%n", x))
                .collect(Collectors.joining(""));
    }

    private StringBuilder getClassAndTestString(TestRunInfo testRunInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        String classNameVar = testRunInfo.getClassName().substring(0,1).toLowerCase(Locale.ROOT) + testRunInfo.getClassName().substring(1);
        String argumentsRequiredHelperObjects = testRunInfo.getArguments().stream()
                .flatMap(x -> x.getRequiredHelperObjects().stream())
                .map(x -> String.format("        %s%n", x))
                .distinct()
                .collect(Collectors.joining(""));
        String argumentsInit = testRunInfo.getArguments().stream().map(ObjectInfo::getInit).collect(Collectors.joining(""));
        String argumentsCode = testRunInfo.getArguments().stream().map(ObjectInfo::getInlineCode).collect(Collectors.joining(", "));

        stringBuilder.append(String.format("class %sTest {%n", testRunInfo.getClassName()));
        stringBuilder.append(String.format("    @Test%n"));
        stringBuilder.append(String.format("    void %s() throws Exception {%n", testRunInfo.getMethodName()));
        stringBuilder.append(argumentsRequiredHelperObjects);
        stringBuilder.append(argumentsInit);
        stringBuilder.append(String.format("        %s %s = new %s();%n", testRunInfo.getClassName(), classNameVar, testRunInfo.getClassName()));
        stringBuilder.append(String.format("        assertEquals(%s.%s(%s), %s);%n",
                classNameVar, testRunInfo.getMethodName(), argumentsCode, testRunInfo.getTestResult().getInlineCode()));
        stringBuilder.append(String.format("    }%n"));
        stringBuilder.append(String.format("}%n"));
        return stringBuilder;
    }

    private String getEndMarkerString() {
        return String.format("%nEND GENERATED TEST =========%n%n");
    }
}
