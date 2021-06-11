package com.onushi.testrecorder.analyzer.classInfo;

import com.onushi.testrecorder.analyzer.object.FieldValue;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.List;

@Data
@Builder
public class MatchingConstructor {
    private Constructor<?> constructor;
    private List<FieldValue> argsInOrder;
    private boolean fieldsCouldHaveDifferentOrder;
}