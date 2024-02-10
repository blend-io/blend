package io.blend.transforms;

import java.util.List;

public class StringToArrayTransform implements Transform{
    private static StringToArrayTransform instance;

    public static StringToArrayTransform getInstance() {
        if(instance == null) {
            instance = new StringToArrayTransform();
        }
        return instance;
    }
    @Override
    public Object resolve(Object initialValue, String[] args) {
        String value = String.valueOf(initialValue);
        String delimiter = args[0];
        return List.of(value.split(delimiter));
    }
}
