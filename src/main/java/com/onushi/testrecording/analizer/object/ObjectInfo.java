package com.onushi.testrecording.analizer.object;

// TODO IB create a class analyzer to check object fields, setters and constructors
// TODO IB I can use serialization to transform to Json and back
// TODO IB !!!! https://medium.com/analytics-vidhya/top-10-java-classes-from-utility-package-a4bebde7c267

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectInfo {
    protected final Object object;
    protected final String objectName;

    protected ObjectInfo(Object object, String objectName) {
        this.object = object;
        this.objectName = objectName;
    }

    public List<String> getRequiredImports() {
        return new ArrayList<>();
    }

    public List<String> getRequiredHelperObjects() {
        return new ArrayList<>();
    }

    public String getInit() {
        return "";
    }

    public abstract String getInlineCode();

    public abstract boolean isOnlyInline();

    public String getClassName() {
        return ObjectInfo.getClassName(object);
    }

    private static String getClassName(Object object) {
        if (object == null) {
            return "null";
        } else {
            return object.getClass().getName();
        }
    }

    public static ObjectInfo createObjectInfo(Object object, String objectName) {
        String className = getClassName(object);
            switch (className) {
                case "null":
                    return new NullObjectInfo(objectName);
                case "java.lang.Float":
                    return new FloatObjectInfo(object, objectName);
                case "java.lang.Long":
                    return new LongObjectInfo(object, objectName);
                case "java.lang.Byte":
                    return new ByteObjectInfo(object, objectName);
                case "java.lang.Short":
                    return new ShortObjectInfo(object, objectName);
                case "java.lang.Character":
                    return new CharacterObjectInfo(object, objectName);
                case "java.lang.String":
                    return new StringObjectInfo(object, objectName);
                case "java.util.Date":
                    return new DateObjectInfo(object, objectName);
                case "java.lang.Boolean":
                case "java.lang.Integer":
                case "java.lang.Double":
                default:
                    return new GenericObjectInfo(object, objectName);
            }
    }
}
