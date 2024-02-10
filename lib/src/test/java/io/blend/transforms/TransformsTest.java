package io.blend.transforms;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransformsTest {
    private static TransformsFactory transformsFactory;

    @BeforeAll
    static void init() {
        transformsFactory = ConcreteTransformFactory.createFactory();
    }

    @Test
    void testGetIllegalTransform() {
        assertThrows(IllegalArgumentException.class, () -> {
            Transform transform = transformsFactory.getTransform("some-transform-that-is-not-implemented");
        });
    }

    @Test
    void testStringToArrayTransform() {
        Transform transform = transformsFactory.getTransform("StringToArray");
        String initialValue = "12, 4th Cross Road, Bangalore";
        String[] args = new String[] {","};
        List<?> resolvedValue = (List<?>) transform.resolve(initialValue, args);
        assertEquals(3, resolvedValue.size());
    }

    @Test
    void testMapTransform() {
        Transform transform = transformsFactory.getTransform("Map");
        String initialValue1 = "RJCT";
        String[] argsWithDefault = new String[]{"ACCP:Accepted", "RJCT:Rejected", "*:Default-Value"};
        String[] argsWithoutDefault = new String[]{"ACCP:Accepted", "RJCT:Rejected"};
        String resolvedValue1 = String.valueOf(transform.resolve(initialValue1, argsWithDefault));
        String initialValue2 = "CMP";
        String resolvedValue2 = String.valueOf(transform.resolve(initialValue2, argsWithDefault));
        String resolvedValue3 = String.valueOf(transform.resolve(initialValue2, argsWithoutDefault));
        assertEquals("Rejected1", resolvedValue1);
        assertEquals("Default-Value", resolvedValue2);
        assertEquals(initialValue2, resolvedValue3);
    }

    @Test
    void testNewFieldTransform() {
        Transform transform = transformsFactory.getTransform("NewField");
        String[] args = new String[] {"some-default-value"};
        String resolvedValue = String.valueOf(transform.resolve(null, args));
        assertEquals("some-default-value", resolvedValue);
    }

    @Test
    void testObjectToSingleNodeArrayTansform() {
        Transform transform = transformsFactory.getTransform("ObjectToSingleNodeArray");
        HashMap<String, String> initialValue = new HashMap<>();
        initialValue.put("amount", "100");
        initialValue.put("currency", "INR");
        List<?> resolvedValue = (List<?>) transform.resolve(initialValue, null);
        assertEquals(1, resolvedValue.size());
    }

    @Test
    void testDateTransform() {
        Transform transform = transformsFactory.getTransform("Date");
        String initialValue = "2023-09-02";
        String[] args = new String[] {"yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"};
        String resolvedValue = String.valueOf(transform.resolve(initialValue, args));
        assertEquals("2023-09-02T00:00:00.000+05:30", resolvedValue);
    }
}
