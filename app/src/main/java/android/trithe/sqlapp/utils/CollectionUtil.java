package android.trithe.sqlapp.utils;

import java.util.Collection;

public class CollectionUtil {
    public static boolean isEmpty(final Collection<?> c) {
        return c == null || c.isEmpty();
    }
}
