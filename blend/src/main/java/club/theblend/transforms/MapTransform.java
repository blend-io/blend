package club.theblend.transforms;

import java.util.HashMap;
import java.util.Map;

public class MapTransform implements Transform{

    private static MapTransform instance;

    public static MapTransform getInstance() {
        if(instance == null) {
            instance = new MapTransform();
        }
        return instance;
    }
    @Override
    public Object resolve(Object initialValue, String[] args) {
        String key = String.valueOf(initialValue);
        Map<String, String> referenceMap = new HashMap<>();
        for(String arg: args) {
            String[] entry = arg.split(":");
            String originalValue = entry[0];
            String targetValue = entry[1];
            referenceMap.put(originalValue, targetValue);
        }
        if(referenceMap.containsKey(key)) {
            return referenceMap.get(key);
        }
        else if(referenceMap.containsKey("*")) {
            return referenceMap.get("*");
        }
        return key;
    }
}
