package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.sample.services.SampleService;
import com.onushi.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import com.onushi.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import com.onushi.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest28SideEffectsOnArrayLists extends TestGeneratorServiceTest {
    @Test
    void generateTestWithSideEffectsOnArrayLists() {
        // Arrange
        List<Float> floatList = new ArrayList<>();
        floatList.add(0f);
        floatList.add(2f);
        floatList.add(3f);

        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(new SampleService())
                .methodName("changeArrayList")
                .arguments(Collections.singletonList(floatList))
                .build());
        floatList.add(1, 1f);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(42)
                .build());

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
                        "    //TODO rename the test to describe the use case\n" +
                        "    //TODO refactor the generated code to make it easier to understand\n" +
                        "    @Test\n" +
                        "    void changeArrayList() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        List<Float> arrayList1 = new ArrayList<>(Arrays.asList(0.0f, 2.0f, 3.0f));\n" +
                        "        SampleService sampleService = new SampleService();\n" +
                        "\n" +
                        "        // Act\n" +
                        "        Integer result = sampleService.changeArrayList(arrayList1);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(42, result);\n" +
                        "\n" +
                        "        // Side Effects\n" +
                        "        assertEquals(4, arrayList1.size());\n" +
                        "        assertEquals(1.0f, arrayList1.get(1));\n" +
                        "        assertEquals(2.0f, arrayList1.get(2));\n" +
                        "        assertEquals(3.0f, arrayList1.get(3));\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    // TODO IB !!!! I should put all tests here to eliminate warnings and to make sure the generate code is ok
    @Test
    void changeArrayList() throws Exception {
        // Arrange
        List<Float> arrayList1 = new ArrayList<>(Arrays.asList(0.0f, 2.0f, 3.0f));
        SampleService sampleService = new SampleService();

        // Act
        Integer result = sampleService.changeArrayList(arrayList1);

        // Assert
        assertEquals(42, result);

        // Side Effects
        assertEquals(4, arrayList1.size());
        assertEquals(1.0f, arrayList1.get(1));
        assertEquals(2.0f, arrayList1.get(2));
        assertEquals(3.0f, arrayList1.get(3));
    }
}
