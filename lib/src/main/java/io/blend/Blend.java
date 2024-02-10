package io.blend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.blend.models.BlendConfig;
import io.blend.transforms.ConcreteTransformFactory;
import io.blend.transforms.Transform;
import io.blend.transforms.TransformsFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Blend<T> {

    BlendConfig blendConfig;

    public Blend(String path) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            this.blendConfig = objectMapper.readValue(new File(path), BlendConfig.class);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Unable to parse YAML!");
        }
    }

    public Blend(InputStream is) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            this.blendConfig = objectMapper.readValue(is, BlendConfig.class);
        } catch (IOException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Unable to parse YAML!");
        }
    }

    public <S> T convert (S source, Class<T> target) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode sourceJSON = objectMapper.valueToTree(source);
        return process(sourceJSON, target);
    }

    private T process(JsonNode sourceJSON, Class<T> target) {
        Map<String, Object> flattenedSource = flatten(sourceJSON, null);
        List<BlendConfig.Patch> regularPatches = new ArrayList<>();
        List<BlendConfig.Patch> postDeflattenPatches = new ArrayList<>();
        this.blendConfig.getPatches().forEach(patch -> {
            if(patch.isRegularPatch()) {
                regularPatches.add(patch);
            }
            else {
                postDeflattenPatches.add(patch);
            }
        });
        Map<String, Object> flattenedTarget = applyRegularPatches(regularPatches, flattenedSource);
        Map<String, Object> deflattenedTarget = deflatten(flattenedTarget);
        if(!postDeflattenPatches.isEmpty()) {
            applyPostDeflattenPatches(postDeflattenPatches, deflattenedTarget);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(deflattenedTarget, target);
    }

    @SuppressWarnings("unchecked")
    private void applyPostDeflattenPatches(List<BlendConfig.Patch> postDeflattenPatches, Map<String, Object> deflattenedTarget) {
        for(BlendConfig.Patch patch: postDeflattenPatches) {
            String fromField = patch.getFromField();
            Map<String, Object> currentMap = deflattenedTarget;
            String[] keys = fromField.split("\\.");
            boolean skip = false;
            for(int i = 0; i < keys.length; i++) {
                String key = keys[i];
                if(currentMap.containsKey(key)) {
                    Object next = currentMap.get(key);
                    if(next instanceof Map<?, ?>) {
                        currentMap = (Map <String, Object>) next;
                    }
                }
                else {
                    skip = true;
                    break;
                }
            }
            if(skip) {
                continue;
            }
            String targetKey = keys[keys.length - 1];
            Map<String, Object> initialValue = (Map<String, Object>) currentMap.get(targetKey);
            if (initialValue != null) {
                Object resolvedValue = resolveTransform(patch.getTransform(), initialValue);
                currentMap.put(targetKey, resolvedValue);
            }
        }
    }

    private Map<String, Object> flatten(JsonNode sourceJSON, String prefix) {
        Map<String, Object> resultMap =  new HashMap<>();
        Iterator<Map.Entry<String, JsonNode>> fields = sourceJSON.fields();
        while(fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            String newKey = prefix != null ? prefix + "." + key : key;
            if(value.isArray()) {
                resultMap.put(newKey, value);
            }
            else if (value.isObject()) {
                resultMap.putAll(flatten(value, newKey));
            }
            else {
                resultMap.put(newKey, value.asText());
            }
        }
        return resultMap;
    }

    private Object resolveTransform(BlendConfig.Patch.TransformConfig transformConfig, Object initialValue) {
        String name = transformConfig.getName();
        String[] args = transformConfig.getArgs();
        TransformsFactory transformsFactory = ConcreteTransformFactory.createFactory();
        Transform transform = transformsFactory.getTransform(name);
        return transform.resolve(initialValue, args);
    }

    private Map<String, Object> deflatten(Map<String, Object> flattenedMap) {
        Map<String, Object> resultMap = new HashMap<>();
        for(Map.Entry<String, Object> entry : flattenedMap.entrySet()) {
            String[] keys = entry.getKey().split("\\.");
            Map<String, Object> currentMap = resultMap;
            for(int i=0; i < keys.length; i++) {
                String key = keys[i];
                currentMap.putIfAbsent(key, new HashMap<>());
                currentMap = (Map<String, Object>) currentMap.get(key);
            }
            currentMap.put(keys[keys.length - 1], entry.getValue());
        }
        return resultMap;
    }

    private Map<String, Object> applyRegularPatches(List<BlendConfig.Patch> regularPatches, Map<String, Object> flattenedSource) {
        Map<String, Object> flattenedTarget = new HashMap<>();

        flattenedSource.forEach((key, value) -> {
            if(value != null && value != "null") {
                flattenedTarget.put(key, value);
            }
        });

        for(BlendConfig.Patch patch: regularPatches) {
            String fromField = patch.getFromField();
            String toField = patch.getToField();
            BlendConfig.Patch.TransformConfig transform = patch.getTransform();

            // Case 1: Addition of New Field
            if(transform != null && Objects.equals(transform.getName(), "NewField")) {
                flattenedTarget.put(toField, resolveTransform(transform, null));
            }

            // Case 2: Deletion of existing field
            else if(toField == null) {
                flattenedTarget.remove(fromField);
            }

            // Case 3: Patch `fromField` to `toField`. Resolve transform, if present.
            else {
                Object value = flattenedTarget.get(fromField);
                flattenedTarget.remove(fromField);
                if(patch.getTransform() != null && value != null) {
                    value = resolveTransform(patch.getTransform(), value);
                }
                flattenedTarget.put(toField, value);
            }
        }
        return flattenedTarget;
    }
}
