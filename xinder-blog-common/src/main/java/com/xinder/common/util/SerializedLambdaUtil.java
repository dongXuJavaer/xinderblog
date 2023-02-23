package com.xinder.common.util;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author Xinder
 * @date 2023-02-23 12:20
 */
public class SerializedLambdaUtil {


    /**
     * 根据getter方法获取属性名
     *
     * @param sFunction
     * @param <K>
     * @param <R>
     * @return
     */
    public static  <K, R> String getFieldName(SFunction<K, R> sFunction) {

        SerializedLambda serializedLambda = getSerializedLambda(sFunction);
        // 取lambda表达式中，实现方法的方法名，然后截取，获取属性名（因驼峰，此时截取获得到的属性名首字母为大写）
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        return fieldName;
    }

    private static  <R, K> SerializedLambda getSerializedLambda(SFunction<K, R> sFunction) {
        Method writeReplaceMethod = null;
        SerializedLambda serializedLambda = null;
        try {
            writeReplaceMethod = sFunction.getClass().getDeclaredMethod("writeReplace");
            boolean accessible = writeReplaceMethod.isAccessible();
            writeReplaceMethod.setAccessible(true);
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(sFunction);
            writeReplaceMethod.setAccessible(accessible);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (writeReplaceMethod == null) {
            throw new IllegalArgumentException("sFunction must be lambda-class. writeReplaceObject should not be " + writeReplaceMethod);
        }
        return serializedLambda;
    }
}
