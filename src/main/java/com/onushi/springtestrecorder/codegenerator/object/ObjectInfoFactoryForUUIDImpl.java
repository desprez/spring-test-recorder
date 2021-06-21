package com.onushi.springtestrecorder.codegenerator.object;

import com.onushi.springtestrecorder.codegenerator.template.StringGenerator;
import com.onushi.springtestrecorder.codegenerator.test.TestRecordingPhase;

import java.util.Collections;

public class ObjectInfoFactoryForUUIDImpl extends ObjectInfoFactory {
    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        String fullClassName = context.getObject().getClass().getName();
        if (fullClassName.equals("java.util.UUID")) {
            ObjectInfo objectInfo =
                    new ObjectInfo(context, context.getObjectName());

            objectInfo.initCode = new StringGenerator()
                    .setTemplate("UUID {{objectName}} = UUID.fromString(\"{{uuid}}\");")
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("uuid", context.getObject().toString())
                    .generate();


            objectInfo.declareRequiredImports.add("java.util.UUID");

            takeSnapshot(objectInfo, context);
            if (context.getTestGenerator().getCurrentTestRecordingPhase() != TestRecordingPhase.AFTER_METHOD_RUN) {
                objectInfo.toRunAfterMethodRun = () -> takeSnapshot(objectInfo, context);
            }

            return objectInfo;
        }
        return null;
    }

    private void takeSnapshot(ObjectInfo objectInfo, ObjectInfoCreationContext context) {
        String value = new StringGenerator()
                .setTemplate("UUID.fromString(\"{{uuid}}\")")
                .addAttribute("uuid", context.getObject().toString())
                .generate();

        addVisiblePropertySnapshot(objectInfo, "", context.getTestGenerator().getCurrentTestRecordingPhase(),
                VisiblePropertySnapshot.builder()
                        .value(PropertyValue.fromString(value))
                        .requiredImports(Collections.singletonList(context.getObject().getClass().getName()))
                        .build());
    }
}
