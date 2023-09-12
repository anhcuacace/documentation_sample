package tunanh.documentation.xs.extensions;

import java.util.List;
import java.util.Map;

 public class ObjectUtil {
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isAllEmpty(Object obj, Object obj2) {
        return isEmpty(obj) && isEmpty(obj2);
    }

    public static boolean isAnyEmpty(Object... objArr) {
        for (Object obj : objArr) {
            if (isEmpty(obj)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAnyNull(Object obj, Object obj2) {
        return isNull(obj) || isNull(obj2);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (!(obj instanceof String) || !((String) obj).trim().isEmpty()) {
            return obj instanceof Map ? ((Map) obj).isEmpty() : obj instanceof List ? ((List) obj).isEmpty() : (obj instanceof Object[]) && ((Object[]) obj).length == 0;
        }
        return true;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String toString(Object obj, String str) {
        return obj == null ? str : obj.toString();
    }
}