package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.utils.ServiceCreatorUtils;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObjectNameGeneratorTest {

    @Test
    void getObjectName() throws InterruptedException {
        Map<String, Integer> lastIndexForObjectName = new HashMap<>();

        TestGenerator testGeneratorMock = mock(TestGenerator.class);
        when(testGeneratorMock.getLastIndexForObjectName()).thenReturn(lastIndexForObjectName);

        ObjectNameGenerator objectNameGenerator = ServiceCreatorUtils.createObjectNameGenerator();
        assertEquals("date1", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
        assertEquals("date2", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
        assertEquals("date3", objectNameGenerator.getNewObjectName(testGeneratorMock, new Date()));
        Thread.sleep(1);
    }

    @Test
    void getBaseObjectName() {
        ObjectNameGenerator objectNameGenerator = ServiceCreatorUtils.createObjectNameGenerator();
        assertEquals("float", objectNameGenerator.getBaseObjectName(1f));
        assertEquals("null", objectNameGenerator.getBaseObjectName(null));
    }
}
