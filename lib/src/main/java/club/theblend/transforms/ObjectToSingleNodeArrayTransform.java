package club.theblend.transforms;

import java.util.ArrayList;
import java.util.List;

public class ObjectToSingleNodeArrayTransform implements Transform{
    private static ObjectToSingleNodeArrayTransform instance;
    public static ObjectToSingleNodeArrayTransform getInstance() {
        if(instance == null) {
            instance = new ObjectToSingleNodeArrayTransform();
        }
        return instance;
    }
    @Override
    public Object resolve(Object initialValue, String[] args) {
        List<Object> singleNodeArray = new ArrayList<>();
        singleNodeArray.add(initialValue);
        return singleNodeArray;
    }
}
