package io.blend.transforms;

public interface Transform {
    Object resolve(Object initialValue, String[] args);
}
