package ClientTest;

import static org.junit.Assert.*;
import Client.Main;

public class MainTest {
    @org.junit.Test
    public void addStrings() throws Exception {
        assertEquals("ab", Main.addStrings("a", "b"));
    }


}