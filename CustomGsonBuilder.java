import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Generic class for excluding fields and class names while converting POJOS/Value Objects to JSON format.
 *
 * @author Nagendra Kamisetti
 */
public class CustomGsonBuilder {

    /**
     * Returns Gson object with GsonBuilder to exclude classes.
     * Example: class structure
     * package com.job;
     * public class ContainerFoo {
     * public Foo1 foo1 = new Foo1();
     * public Foo2 foo2 = new Foo2();
     * }
     * package com.job;
     * public class Foo1 {
     * public String field1 = "field1Value";
     * }
     * package com.job;
     * public class Foo2 {
     * public String field2 = "field2Value";
     * }
     * <p>
     * Usage:
     * Set<String> excludeClasses = new HashSet();
     * excludeClasses.add(ContainerFoo.class.getName());
     * Gson gson = CustomGsonBuilder.newGson(excludeClasses);
     * gson.toString(new ContainerFoo());
     * <p>
     * Result: {"foo1":{"field1":"field1Value"}}
     * NOTE: Foo2 class information excluded as expected
     *
     * @param excludeClassNames pass class names to exclude
     * @return gson object
     */
    public static Gson newGson(Set<String> excludeClassNames) {
        return newGson(excludeClassNames, Collections.emptyMap());
    }

    /**
     * Returns Gson object with GsonBuilder to exclude fields from a particular class.
     * Example: class structure
     * package com.job;
     * public class Foo1 {
     * public String field1 = "field1Value";
     * public String field2 = "field2Value";
     * }
     * <p>
     * Usage:
     * Map<String, String> excludedFields = new HashMap<>();
     * excludedFields.put(Foo1.class.getName(), "field1");
     * Gson gson = CustomGsonBuilder.newGson(excludedFields);
     * gson.toString(new Foo1());
     * <p>
     * Result: {"field2":"field2Value"}
     * NOTE: field1 information excluded as expected
     *
     * @param excludeFieldNames pass class names to exclude
     * @return gson object
     */
    public static Gson newGson(Map<String, String> excludeFieldNames) {
        return newGson(Collections.emptySet(), excludeFieldNames);
    }

    /**
     * This combination of both,
     *
     * @param excludedClasses
     * @param excludedFields
     * @return
     * @see CustomGsonBuilder#newGson(java.util.Set)
     * @see CustomGsonBuilder#newGson(java.util.Map)
     */
    public static Gson newGson(Set<String> excludedClasses, Map<String, String> excludedFields) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                String className = f.getDeclaringClass().getName();
                if (excludedFields.containsKey(className)) {
                    String value = excludedFields.get(className);
                    return f.getName().equals(value);
                }
                return false;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return excludedClasses.contains(clazz.getName());
            }
        });
        return gsonBuilder.create();
    }

}
