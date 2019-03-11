package org.guman.utils;

import org.objectweb.asm.MethodVisitor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

/**
 * @author duanhaoran
 * @since 2019/3/10 2:26 PM
 */
public class TypeUtil {

    private static final String GET_PREFIX = "get";
    private static final String SET_PREFIX = "set";
    private static final String IS_PREFIX = "is";
    private static final Map<Class<?>, Object> primitiveTypeDefaultValueMap = new HashMap<>(10);
    private static final Map<String, TypeHolder> primitiveType2BoxTypeInternalNameMap = new HashMap<>(10);

    static {
        primitiveTypeDefaultValueMap.put(byte.class, (byte) 0);
        primitiveTypeDefaultValueMap.put(short.class, (short) 0);
        primitiveTypeDefaultValueMap.put(int.class, 0);
        primitiveTypeDefaultValueMap.put(long.class, 0L);
        primitiveTypeDefaultValueMap.put(float.class, 0F);
        primitiveTypeDefaultValueMap.put(double.class, 0D);

        primitiveType2BoxTypeInternalNameMap.put(getInternalName(byte.class), new TypeHolder(byte.class, Byte.class, "byteValue"));
        primitiveType2BoxTypeInternalNameMap.put(getInternalName(short.class), new TypeHolder(short.class, Short.class, "shortValue"));
        primitiveType2BoxTypeInternalNameMap.put(getInternalName(int.class), new TypeHolder(int.class, Integer.class, "intValue"));
        primitiveType2BoxTypeInternalNameMap.put(getInternalName(long.class), new TypeHolder(long.class, Long.class, "longValue"));
        primitiveType2BoxTypeInternalNameMap.put(getInternalName(float.class), new TypeHolder(float.class, Float.class, "floatValue"));
        primitiveType2BoxTypeInternalNameMap.put(getInternalName(double.class), new TypeHolder(double.class, Double.class, "doubleValue"));
    }

    public static boolean isInterface(Class<?> type) {
        return type.isInterface();
    }

    public static List<Class<?>> getGenericTypes(Field field) {
        Type genericType = field.getGenericType();
        return getGenericTypes(genericType);
    }

    public static List<Class<?>> getGenericTypes(Type genericType) {
        List<Class<?>> types = new ArrayList<Class<?>>(2);
        if (genericType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                if (actualTypeArgument instanceof Class) {
                    types.add((Class<?>) actualTypeArgument);
                }
            }
        }
        return types;
    }

    public static Field getFieldByName(String propertyName, Class<?> beanType) throws NoSuchFieldException {
        Class<?> curType = beanType;
        while (curType != null && !curType.equals(Object.class)) {
            try {
                return curType.getDeclaredField(propertyName);
            } catch (NoSuchFieldException e) {
                //ignore
            }
            curType = curType.getSuperclass();
        }
        throw new NoSuchFieldException("No Such Field: ['" + propertyName + "'] in " + beanType);
    }

    public static List<Field> getAllFields(Class<?> beanType) {
        Class<?> curType = beanType;
        List<Field> allFields = new LinkedList<>();
        while (curType != null && !curType.equals(Object.class)) {
            allFields.addAll(Arrays.asList(curType.getDeclaredFields()));
            curType = curType.getSuperclass();
        }
        return allFields;
    }

    public static Method getGetterMethod(String fieldName, Class<?> type) {
        try {
            return type.getMethod(GET_PREFIX + StringUtil.capitalize(fieldName));
        } catch (NoSuchMethodException e) {
            try {
                return type.getMethod(IS_PREFIX + StringUtil.capitalize(fieldName));
            } catch (NoSuchMethodException ignore) {
                try {
                    return type.getMethod(GET_PREFIX + fieldName);
                } catch (NoSuchMethodException e1) {
                    try {
                        return type.getMethod(IS_PREFIX + fieldName);
                    } catch (NoSuchMethodException e2) {
                        return null;
                    }
                }
            }
        }
    }

    public static String getBeanUtilsClassSimpleName(Class<?> type) {
        return type.getSimpleName().replace("[]", "ARRAY");
    }

    public static Method getSetterMethod(String fieldName, Class<?> fieldType, Class<?> type) {
        try {
            return type.getMethod(SET_PREFIX + StringUtil.capitalize(fieldName), fieldType);
        } catch (NoSuchMethodException e) {
            try {
                return type.getMethod(SET_PREFIX + fieldName);
            } catch (NoSuchMethodException e1) {
                return null;
            }
        }
    }

    public static Object getDefaultTypeValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }
        return primitiveTypeDefaultValueMap.get(type);
    }

    public static String getInternalName(Class<?> type) {
        return org.objectweb.asm.Type.getInternalName(type);
    }

    public static boolean isPrimitiveType(String typeInternalName) {
        return primitiveType2BoxTypeInternalNameMap.containsKey(typeInternalName);
    }

    public static boolean isPrimitiveType(Class<?> type) {
        return primitiveTypeDefaultValueMap.containsKey(type);
    }

    public static boolean isPrimitiveToRefTypeMatch(Class<?> left, Class<?> right) {
        String primitiveInternal = org.objectweb.asm.Type.getInternalName(left);
        return isPrimitiveType(primitiveInternal)
                && getBoxingTypeByInternalName(primitiveInternal).getBoxTypeInternalName()
                .equals(org.objectweb.asm.Type.getInternalName(right));
    }

    public static boolean isRefToPrimitiveTypeMatch(Class<?> left, Class<?> right) {
        String primitiveInternal = org.objectweb.asm.Type.getInternalName(right);
        return isPrimitiveType(primitiveInternal)
                && getBoxingTypeByInternalName(primitiveInternal).getBoxTypeInternalName()
                .equals(org.objectweb.asm.Type.getInternalName(left));
    }

    public static TypeHolder getBoxingTypeByInternalName(String primitiveInternalName) {
        return primitiveType2BoxTypeInternalNameMap.get(primitiveInternalName);
    }

    public static class TypeHolder {
        private final String boxTypeInternalName;
        private final String convertMethod; //intValue
        private final String boxTypeDesc;
        private final String primTypeDesc;
        private final String primSimpleName;

        public TypeHolder(Class<?> primType, Class<?> boxType, String convertMethod) {
            this.primSimpleName = primType.getSimpleName();
            this.boxTypeInternalName = org.objectweb.asm.Type.getInternalName(boxType);
            this.boxTypeDesc = org.objectweb.asm.Type.getDescriptor(boxType);
            this.primTypeDesc = org.objectweb.asm.Type.getDescriptor(primType);
            this.convertMethod = convertMethod;

        }

        public void applyValueOfCode(MethodVisitor mv) {
            mv.visitMethodInsn(INVOKESTATIC, boxTypeInternalName, "valueOf", "(" + primTypeDesc + ")" + boxTypeDesc, false);
        }

        //调用引用类型的xxValue()得到原始类型，如: Integer.intValue()
        public void applyPrimValueCode(MethodVisitor mv) {
            mv.visitMethodInsn(INVOKEVIRTUAL,
                    getBoxTypeInternalName(),
                    primSimpleName + "Value",
                    "()" + primTypeDesc, false);
        }

        public String getBoxTypeInternalName() {
            return boxTypeInternalName;
        }

        public String getConvertMethod() {
            return convertMethod;
        }

        public String getBoxTypeDesc() {
            return boxTypeDesc;
        }

        public String getPrimTypeDesc() {
            return primTypeDesc;
        }
    }
}
