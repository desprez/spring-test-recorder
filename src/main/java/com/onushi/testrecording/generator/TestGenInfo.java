package com.onushi.testrecording.generator;

import com.onushi.testrecording.generator.object.ObjectInfo;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO IB !!!! Write tests for all the code
@Getter
public class TestGenInfo {
    protected ObjectInfo targetObjectInfo;
    protected String packageName;
    protected String shortClassName;
    protected String methodName;
    protected List<ObjectInfo> argumentObjectInfos;
    protected ObjectInfo resultObjectInfo;

    protected List<String> requiredImports;
    protected List<String> requiredHelperObjects;
    protected List<String> objectsInit;
    protected List<String> argumentsInlineCode;
    protected String resultInit;

    protected final Map<Object, String> objectNames = new HashMap<>();
    protected final Map<String, Integer> lastIndexForObjectName = new HashMap<>();

    protected TestGenInfo() {}

    protected Map<Object, String> getObjectNames() {
        return objectNames;
    }

    protected Map<String, Integer> getLastIndexForObjectName() {
        return lastIndexForObjectName;
    }
}