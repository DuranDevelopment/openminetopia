package nl.openminetopia.utils.nms;

import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReflectionUtils {

    public static void setField(Object instance, Object value) throws NoSuchFieldException, IllegalAccessException {
        setField(instance.getClass(), instance, value);
    }

    public static void setStaticField(Class<?> target, Object value) throws NoSuchFieldException, IllegalAccessException {
        setField(target, null, value);
    }

    public static void setField(String fieldName, Object instance, Object value) throws NoSuchFieldException, IllegalAccessException {
        setField(fieldName, instance.getClass(), instance, value);
    }

    public static void setStaticField(String fieldName, Class<?> target, Object value) throws NoSuchFieldException, IllegalAccessException {
        setField(fieldName, target, null, value);
    }

    public static void setField(Class<?> targetClass, Object targetInstance, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field field = getField(targetClass, value.getClass());
        setField(field, targetInstance, value);
    }

    public static void setField(String fieldName, Class<?> targetClass, Object targetInstance, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field field = targetClass.getDeclaredField(fieldName);
        setField(field, targetInstance, value);
    }

    private static void setField(Field field, Object targetInstance, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(targetInstance, value);
    }

    public static Field getField(Class<?> targetClass, Class<?> fieldClass) throws NoSuchFieldException {
        List<Field> fields = Arrays.stream(targetClass.getDeclaredFields()).filter(
                f -> MethodType.methodType(f.getType()).wrap().returnType().equals(fieldClass)
        ).collect(Collectors.toList());
        if (fields.size() != 1) throw new NoSuchFieldException("Fields found: " + fields);
        fields.get(0).setAccessible(true);
        return fields.get(0);
    }

    public static Field getField(String fieldName, Class<?> targetClass) throws NoSuchFieldException {
        Field field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static <U> U get(Class<?> targetClass, Object targetInstance, Class<U> fieldClass) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(targetClass, fieldClass);
        return (U) field.get(targetInstance);
    }

    public static <U> U get(String fieldName, Class<?> targetClass, Object targetInstance) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(fieldName, targetClass);
        return (U) field.get(targetInstance);
    }

    public static <U> U getStatic(Class<?> targetClass, Class<U> fieldClass) throws NoSuchFieldException, IllegalAccessException {
        return get(targetClass, null, fieldClass);
    }

    public static <U> U getStatic(String fieldName, Class<?> targetClass) throws NoSuchFieldException, IllegalAccessException {
        return get(fieldName, targetClass, null);
    }

    public static void invoke(Object targetInstance, Object... parameters) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        invoke(targetInstance, void.class, parameters);
    }

    public static void invokeStatic(Class<?> target, Object... parameters) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        invokeStatic(target, void.class, parameters);
    }

    public static <U> U invoke(Object targetInstance, Class<U> returnType, Object... parameters) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return invoke(targetInstance.getClass(), targetInstance, returnType, parameters);
    }

    public static <U> U invokeStatic(Class<?> target, Class<U> returnType, Object... parameters) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return invoke(target, null, returnType, parameters);
    }


    private static <U> U invoke(Class<?> targetClass, Object targetInstance, Class<U> returnType, Object... parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getMethod(targetClass, returnType, Arrays.stream(parameters).map(Object::getClass).toArray(Class[]::new));
        return (U) method.invoke(targetInstance, parameters);
    }

    public static Object invoke(String methodName, Class<?> targetClass, Object targetInstance, Object... parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = targetClass.getDeclaredMethod(methodName, Arrays.stream(parameters).map(Object::getClass).toArray(Class[]::new));
        method.setAccessible(true);
        return method.invoke(targetInstance, parameters);
    }

    public static Method getMethod(Class<?> targetClass, Class<?> returnType, Class<?>... paramTypes) throws NoSuchMethodException {
        List<Method> methods = Arrays.stream(targetClass.getDeclaredMethods())
                .filter(m -> m.getParameterTypes().length == paramTypes.length &&
                        m.getReturnType() == returnType &&
                        IntStream.range(0, paramTypes.length)
                                .allMatch(i -> m.getParameterTypes()[i].isAssignableFrom(paramTypes[i])))
                .collect(Collectors.toList());
        if (methods.size() != 1) throw new NoSuchMethodException("Methods found: " + methods);

        methods.get(0).setAccessible(true);
        return methods.get(0);
    }
    public static Method getMethod(String methodName, Class<?> targetClass, Class<?>... paramTypes) throws NoSuchMethodException {
        Method method = targetClass.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method;
    }
}