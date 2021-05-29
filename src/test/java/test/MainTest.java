package test;

import static org.junit.Assert.assertTrue;

import com.slavsquatsuperstar.Main;
import org.junit.Test;

import com.slavsquatsuperstar.mayonez.Vector2;

public class MainTest {
    public static void main(String[] args) {
        Vector2 a = new Vector2(6, 4);
        Vector2 b = new Vector2(8, 1);
        Vector2 proj = b.mul(a.dot(b) / b.lengthSquared());
        System.out.println(proj);
    }
}
