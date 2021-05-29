package test;

import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.util.MathUtil;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MathTests {
    @Test
    public void minFloatEqualsZero() {
        assertTrue(MathUtil.equals(0f, Float.MIN_VALUE));
    }

    @Test
    public void unitVectorOfZero() {
        Vector2 v = new Vector2();
        assertTrue(v.unit().equals(v));
    }
}
