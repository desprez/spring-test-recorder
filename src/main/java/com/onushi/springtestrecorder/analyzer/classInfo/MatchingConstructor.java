/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.analyzer.classInfo;

import com.onushi.springtestrecorder.analyzer.object.FieldValue;
import lombok.Builder;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.List;

@Getter
@Builder
public class MatchingConstructor {
    private Constructor<?> constructor;
    private List<FieldValue> argsInOrder;
    private boolean fieldsCouldHaveDifferentOrder;
}
