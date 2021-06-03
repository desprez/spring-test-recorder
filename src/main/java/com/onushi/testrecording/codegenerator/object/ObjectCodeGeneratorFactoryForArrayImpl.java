package com.onushi.testrecording.codegenerator.object;

import com.onushi.testrecording.codegenerator.template.StringGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectCodeGeneratorFactoryForArrayImpl extends ObjectCodeGeneratorFactory {
    private final ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager;

    public ObjectCodeGeneratorFactoryForArrayImpl(ObjectCodeGeneratorFactoryManager objectCodeGeneratorFactoryManager) {
        this.objectCodeGeneratorFactoryManager = objectCodeGeneratorFactoryManager;
    }

    private static class ArrayAsList {
        public final Class<?> elementClass;
        public final List<?> list;

        public ArrayAsList(Class<?> elementClass, List<?> list) {
            this.elementClass = elementClass;
            this.list = list;
        }
    }

    @Override
    public ObjectCodeGenerator createObjectCodeGenerator(ObjectCodeGeneratorCreationContext context) {
        if (context.getObject().getClass().getName().startsWith("[")) {

            ObjectCodeGenerator objectCodeGenerator = new ObjectCodeGenerator(context.getObject(), context.getObjectName(), context.getObjectName(), false);

            ArrayAsList arrayAsList = getElementList(context.getObject());

            objectCodeGenerator.elements = arrayAsList.list
                    .stream()
                    .map(fieldValue -> objectCodeGeneratorFactoryManager.getCommonObjectCodeGenerator(context.getTestGenerator(), fieldValue))
                    .collect(Collectors.toList());

            objectCodeGenerator.dependencies = objectCodeGenerator.elements.stream()
                    .distinct()
                    .collect(Collectors.toList());

            objectCodeGenerator.actualClassName = new StringGenerator()
                    .setTemplate("{{elementClassShort}}[]")
                    .addAttribute("elementClassShort", arrayAsList.elementClass.getSimpleName())
                    .generate();

            objectCodeGenerator.initCode = new StringGenerator()
                    .setTemplate("{{elementClassShort}}[] {{objectName}} = {{{elementsInlineCode}}};")
                    .addAttribute("elementClassShort", arrayAsList.elementClass.getSimpleName())
                    .addAttribute("objectName", context.getObjectName())
                    .addAttribute("elementsInlineCode", getElementsInlineCode(objectCodeGenerator.elements))
                    .generate();

            return objectCodeGenerator;
        } else {
            return null;
        }
    }

    private String getElementsInlineCode(List<ObjectCodeGenerator> dependencies) {
        return dependencies.stream()
                .map(ObjectCodeGenerator::getInlineCode)
                .collect(Collectors.joining(", "));
    }

    private ArrayAsList getElementList(Object arrayObject) {
        String arrayClassName = arrayObject.getClass().getName();
        switch(arrayClassName) {
            case "[Z":
                boolean[] booleanArray = (boolean[]) arrayObject;
                List<Boolean> booleanList = new ArrayList<>();
                for (boolean element : booleanArray) {
                    booleanList.add(element);
                }
                return new ArrayAsList(boolean.class, booleanList);
            case "[B":
                byte[] byteArray = (byte[]) arrayObject;
                List<Byte> byteList = new ArrayList<>();
                for (byte element : byteArray) {
                    byteList.add(element);
                }
                return new ArrayAsList(byte.class, byteList);
            case "[C":
                char[] charArray = (char[]) arrayObject;
                List<Character> charList = new ArrayList<>();
                for (char element : charArray) {
                    charList.add(element);
                }
                return new ArrayAsList(char.class, charList);
            case "[D":
                double[] doubleArray = (double[]) arrayObject;
                return new ArrayAsList(double.class, Arrays.stream(doubleArray).boxed().collect(Collectors.toList()));
            case "[F":
                float[] floatArray = (float[]) arrayObject;
                List<Float> floatList = new ArrayList<>();
                for (float element : floatArray) {
                    floatList.add(element);
                }
                return new ArrayAsList(float.class, floatList);
            case "[I":
                int[] intArray = (int[]) arrayObject;
                return new ArrayAsList(int.class, Arrays.stream(intArray).boxed().collect(Collectors.toList()));
            case "[J":
                long[] longArray = (long[]) arrayObject;
                return new ArrayAsList(long.class, Arrays.stream(longArray).boxed().collect(Collectors.toList()));
            case "[S":
                short[] shortArray = (short[]) arrayObject;
                List<Short> shortList = new ArrayList<>();
                for (short element : shortArray) {
                    shortList.add(element);
                }
                return new ArrayAsList(short.class, shortList);
            default:
                try {
                    // arrays of arrays have double L here "[LL..."
                    String elementClassName = arrayClassName.substring(2, arrayClassName.length() - 1);
                    Class<?> clazz = Class.forName(elementClassName);
                    return new ArrayAsList(clazz, Arrays.stream((Object[]) arrayObject).map(clazz::cast).collect(Collectors.toList()));
                } catch (ClassNotFoundException e) {
                    return new ArrayAsList(Object.class, Arrays.stream((Object[]) arrayObject).collect(Collectors.toList()));
                }
        }
    }
}


