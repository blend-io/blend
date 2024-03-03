package club.theblend.transforms;

public class ConcreteTransformFactory implements TransformsFactory {

    private static ConcreteTransformFactory instance;

    public static ConcreteTransformFactory createFactory() {
        if(instance == null) {
            instance = new ConcreteTransformFactory();
        }
        return instance;
    }
    @Override
    public Transform getTransform(String name) {
        switch(name) {
            case "Map":
                return MapTransform.getInstance();
            case "Date":
                return DateTransform.getInstance();
            case "NewField":
                return NewFieldTransform.getInstance();
            case "ObjectToSingleNodeArray":
                return ObjectToSingleNodeArrayTransform.getInstance();
            case "StringToArray":
                return StringToArrayTransform.getInstance();
            default:
                throw new IllegalArgumentException("Transform does not exist: " + name);

        }
    }
}
