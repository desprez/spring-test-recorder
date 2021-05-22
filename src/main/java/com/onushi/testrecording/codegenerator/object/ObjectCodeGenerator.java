package com.onushi.testrecording.codegenerator.object;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObjectCodeGenerator {
    protected final Object object;
    protected final String objectName;
    protected final String inlineCode;
    protected String declareClassName;
    protected List<ObjectCodeGenerator> dependencies = new ArrayList<>();
    protected List<String> requiredImports = new ArrayList<>();
    protected final List<String> requiredHelperObjects = new ArrayList<>();
    protected String initCode = "";
    // these 2 are used to traverse the ObjectCodeGenerator graph
    private boolean initPrepared = false;
    private boolean initDone = false;

    public boolean isInitPrepared() {
        return initPrepared;
    }

    public boolean isInitDone() {
        return initDone;
    }

    public void setInitPrepared(boolean initPrepared) {
        this.initPrepared = initPrepared;
    }

    public void setInitDone(boolean initDone) {
        this.initDone = initDone;
    }

    // TODO IB !!!! how come here there is no declareClassName
    protected ObjectCodeGenerator(Object object, String objectName, String inlineCode) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
    }

    protected ObjectCodeGenerator(Object object, String objectName, String inlineCode, String declareClassName) {
        this.object = object;
        this.objectName = objectName;
        this.inlineCode = inlineCode;
        this.declareClassName = declareClassName;
    }
}

