package Annotations;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class JsonSerializer {
    public String serialize(Object object){
        Class<?> objectClass = requireNonNull(object).getClass();
        Map<String, String> jsonElements = new HashMap<>();

        for (Field field : objectClass.getDeclaredFields()){
            field.setAccessible(true);

            if(field.isAnnotationPresent(Documentation.JsonField.class)){
                try{
                    jsonElements.put(getSerializedKey(field), (String) field.get(object));
                } catch (IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }

        System.out.println(toJsonStr(jsonElements));
        return toJsonStr(jsonElements);
    }

    private String toJsonStr(Map<String, String> jsonMap){
        String elementsString = jsonMap.entrySet()
                .stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(","));
        return "[" + elementsString + "]";
    }

    private static String getSerializedKey(Field field){
        String annotationValue = field.getAnnotation(Documentation.JsonField.class).value();

        if (annotationValue.isEmpty()){
            return field.getName();
        } else {
            return annotationValue;
        }
    }
}
