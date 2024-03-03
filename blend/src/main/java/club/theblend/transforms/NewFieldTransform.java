package club.theblend.transforms;

public class NewFieldTransform implements Transform{
    private static NewFieldTransform instance;

    public static NewFieldTransform getInstance() {
        if(instance == null) {
            instance = new NewFieldTransform();
        }
        return instance;
    }
    @Override
    public Object resolve(Object initialValue, String[] args) {
        return args[0];
    }
}
