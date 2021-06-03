package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.analyzer.object.ObjectCreationAnalyzerService;
import com.onushi.testrecording.codegenerator.template.StringGenerator;

public class ObjectInfoFactoryWithNoArgsConstructorImpl extends ObjectInfoFactory {
    private final ObjectCreationAnalyzerService objectCreationAnalyzerService;

    public ObjectInfoFactoryWithNoArgsConstructorImpl(ObjectCreationAnalyzerService objectCreationAnalyzerService) {
        this.objectCreationAnalyzerService = objectCreationAnalyzerService;
    }

    @Override
    public ObjectInfo createObjectInfo(ObjectInfoCreationContext context) {
        if (objectCreationAnalyzerService.canBeCreatedWithNoArgsConstructor(context.getObject(), context.getObjectState())){
            ObjectInfo objectInfo = new ObjectInfo(context.getObject(), context.getObjectName(), context.getObjectName(), false);
            objectInfo.initCode = new StringGenerator()
                    .setTemplate("{{shortClassName}} {{objectName}} = new {{shortClassName}}();")
                    .addAttribute("shortClassName", context.getObject().getClass().getSimpleName())
                    .addAttribute("objectName", context.getObjectName())
                    .generate();

            objectInfo.requiredImports.add(context.getObject().getClass().getName());
            return objectInfo;
        } else {
            return null;
        }
    }
}