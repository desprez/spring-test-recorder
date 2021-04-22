package com.onushi.testrecording.analizer;

import com.onushi.testrecording.analizer.test.ObjectNameService;
import com.onushi.testrecording.analizer.test.ObjectNameServiceImpl;
import com.onushi.testrecording.analizer.test.TestInfo;
import com.onushi.testrecording.analizer.utils.ClassService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

// TODO IB how to check test coverage?
@ExtendWith(MockitoExtension.class)
class ObjectNameServiceImplTest {
    @Mock
    TestInfo testInfoMock;

    @Test
    void generateObjectName() throws InterruptedException {
        Map<Object, String> objectNames = new HashMap<>();
        Map<String, Integer> lastIndexForObjectName = new HashMap<>();

        when(testInfoMock.getObjectNames()).thenReturn(objectNames);
        when(testInfoMock.getLastIndexForObjectName()).thenReturn(lastIndexForObjectName);

        ClassService classService = new ClassService();
        ObjectNameService objectNameService = new ObjectNameServiceImpl(classService);
        assertEquals(objectNameService.generateObjectName(testInfoMock, new Date()), "date1");
        Thread.sleep(1);
        assertEquals(objectNameService.generateObjectName(testInfoMock, new Date()), "date2");
        Thread.sleep(1);
        assertEquals(objectNameService.generateObjectName(testInfoMock, new Date()), "date3");
        Thread.sleep(1);
        Date sameDate = new Date();
        assertEquals(objectNameService.generateObjectName(testInfoMock, sameDate), "date4");
        assertEquals(objectNameService.generateObjectName(testInfoMock, sameDate), "date4");

        List<String> list = new ArrayList<>();
        assertEquals(objectNameService.generateObjectName(testInfoMock, list), "arrayList1");
        assertEquals(objectNameService.generateObjectName(testInfoMock, list), "arrayList1");
    }
}
