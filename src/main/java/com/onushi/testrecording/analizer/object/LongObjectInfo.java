package com.onushi.testrecording.analizer.object;

public class LongObjectInfo extends ObjectInfo {
    public LongObjectInfo(Object object, String objectName) {
        super(object, objectName, true, object + "L");
    }
}
