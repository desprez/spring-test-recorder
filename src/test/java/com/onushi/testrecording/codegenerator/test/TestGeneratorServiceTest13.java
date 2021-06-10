package com.onushi.testrecording.codegenerator.test;

import com.onushi.sample.services.SampleService;
import com.onushi.testrecording.analyzer.methodrun.RecordedMethodRunInfo;
import com.onushi.testrecording.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest13 extends TestGeneratorServiceTest {
    @Test
    void generateTestWithRepeatedArgs() {
        // Arrange
        int[] intArray = {3, 4, 3};
        List<Float> floatList = Arrays.asList(3.0f, 3.0f);
        RecordedMethodRunInfo recordedMethodRunInfo = RecordedMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("repeatedArgs")
                .arguments(Arrays.asList(intArray, floatList))
                .result(42)
                .dependencyMethodRuns(new ArrayList<>())
                .build();
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(recordedMethodRunInfo);

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package com.onushi.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "import java.util.List;\n" +
                        "import java.util.Arrays;\n" +
                        "\n" +
                        "class SampleServiceTest {\n" +
                        testGeneratorService.COMMENT_BEFORE_TEST +
                        "    @Test\n" +
                        "    void repeatedArgs() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        int[] array1 = {3, 4, 3};\n" +
                        "        List<Float> arrayList1 = Arrays.asList(3.0f, 3.0f);\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.repeatedArgs(array1, arrayList1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST =========\n"),
                StringUtils.prepareForCompare(testString));
    }
}
