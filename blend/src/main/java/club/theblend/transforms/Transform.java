package club.theblend.transforms;

public interface Transform {
    Object resolve(Object initialValue, String[] args);
}
