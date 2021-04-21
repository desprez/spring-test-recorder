package com.onushi.testrecording.analizer.object;

// TODO IB create a class analyzer to check object fields, setters and constructors
// TODO IB I can use serialization to transform to Json and back
// TODO IB https://medium.com/analytics-vidhya/top-10-java-classes-from-utility-package-a4bebde7c267

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectInfo {
    protected final Object object;
    protected final String objectName;
    protected List<String> requiredImports = new ArrayList<>();
    protected List<String> requiredHelperObjects = new ArrayList<>();
    protected String init = "";
    protected String inlineCode;
    protected boolean isOnlyInline;

    protected ObjectInfo(Object object, String objectName, boolean isOnlyInline, String inlineCode) {
        this.object = object;
        this.objectName = objectName;
        this.isOnlyInline = isOnlyInline;
        this.inlineCode = inlineCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public List<String> getRequiredImports() {
        return requiredImports;
    }

    public List<String> getRequiredHelperObjects() {
        return requiredHelperObjects;
    }

    public String getInit() {
        return init;
    }

    public String getInlineCode() {
        return inlineCode;
    }

    public boolean isOnlyInline() {
        return isOnlyInline;
    }
}

