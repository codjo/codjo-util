package net.codjo.util.system;
/**
 *
 */
public class ClassUtil {
    private ClassUtil() {
    }


    public static String getMainClassVersion(Class mainClass) {
        if (mainClass == null) {
            mainClass = getMainClass();
        }
        if (mainClass == null) {
            return null;
        }
        return mainClass.getPackage().getImplementationVersion();
    }


    public static String getMainClassVersion() {
        return getMainClassVersion(null);
    }


    public static Class getMainClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length == 0) {
            return null;
        }
        for (StackTraceElement stackTraceElement : stackTrace) {
            if ("main".equals(stackTraceElement.getMethodName())) {
                String startClassName = stackTraceElement.getClassName();
                try {
                    return Class.forName(startClassName);
                }
                catch (ClassNotFoundException e) {
                    return null;
                }
            }
        }
        return null;
    }
}
