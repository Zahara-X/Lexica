package org.example;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    public void testApp() {
        int x = -4800;
        int zig = (x << 1) ^ (x >> 31);
        int ras = zig & 0xFFFFF;
        int ai = (ras >>> 1) ^ -(ras & 0x1);
        assertEquals(-4800, ai);

    }
}
