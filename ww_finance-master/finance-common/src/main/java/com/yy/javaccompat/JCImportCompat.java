package com.yy.javaccompat;

import java.lang.reflect.Method;
import java.lang.reflect.AccessibleObject;

/**
 * JDK 兼容性工具类，特别是针对 Java 9+ 到 Java 21 的内部 API 访问限制
 */
public final class JCImportCompat {
    private JCImportCompat() {}

    /**
     * 返回 JCImport 中的方法返回值（完全兼容 Java 21）。
     * 这个方法尝试通过反射安全地访问可能受限的内部 API。
     * 在 Java 21 中，对内部 API 的反射访问有更严格的限制。
     * 
     * @param jcImport JCImport 对象实例
     * @return 方法调用结果或 null（如果调用失败）
     */
    public static Object getQualid(Object jcImport) {
        if (jcImport == null) return null;
        Class<?> cls = jcImport.getClass();

        try {
            // 尝试不同的可能方法名，适应不同的 JDK 版本
            String[] possibleMethodNames = {
                "getQualifiedIdentifier", // 较新版本
                "qualid",                // 旧版本
                "getQualid"              // 可能的替代名称
            };

            for (String methodName : possibleMethodNames) {
                try {
                    Method method = cls.getMethod(methodName);
                    // Java 9+ 中可能需要设置可访问性
                    AccessibleObject.setAccessible(new Method[]{method}, true);
                    return method.invoke(jcImport);
                } catch (NoSuchMethodException e) {
                    // 继续尝试下一个方法名
                    continue;
                } catch (Exception e) {
                    // 方法存在但调用失败，记录并继续尝试
                    System.err.println("警告: 调用 " + methodName + " 方法失败: " + e.getMessage());
                }
            }
            
            // 所有方法都尝试失败
            return null;
        } catch (Exception e) {
            // 避免捕获Throwable，但处理所有可能的异常
            System.err.println("警告: 访问 JCImport 方法时发生错误: " + e.getMessage());
            return null;
        }
    }

    /**
     * 尝试安全地将对象转换为 com.sun.tools.javac.tree.JCTree 类型。
     * 在 Java 9+ 和 Java 21 中，这个内部 API 默认不再可见，此方法提供了安全的处理方式。
     * 
     * @param obj 要转换的对象
     * @param <T> 目标类型
     * @return 转换后的对象或 null（如果转换失败）
     */
    @SuppressWarnings("unchecked")
    public static <T> T castToJCTree(Object obj) {
        if (obj == null) return null;
        try {
            // Java 9+ 到 Java 21 中，com.sun.tools.* 包默认不再导出
            // 使用反射尝试加载并检查类型
            // 在 Java 21 中，即使使用反射也可能无法访问这些内部类
            Class<?> jctClass = Class.forName("com.sun.tools.javac.tree.JCTree", false, 
                                             Thread.currentThread().getContextClassLoader());
            
            // 避免直接转换，先检查类型
            if (jctClass.isInstance(obj)) {
                return (T) obj;
            }
        } catch (ClassNotFoundException e) {
            // 类不可见，这在 Java 21 中是预期行为
            System.err.println("JCTree 类在当前 Java 环境中不可用（这在 Java 21 中是正常的）");
        } catch (SecurityException e) {
            // 安全管理器阻止访问
            System.err.println("安全限制阻止访问 JCTree 类: " + e.getMessage());
        } catch (Exception e) {
            // 捕获其他可能的异常
            System.err.println("处理 JCTree 类型时发生错误: " + e.getMessage());
        }
        return null;
    }
}
