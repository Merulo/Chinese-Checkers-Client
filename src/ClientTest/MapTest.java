package ClientTest;

import Client.Map.Map;
import Client.Network.Connection;
import javafx.scene.layout.Pane;
import junit.framework.TestCase;

public class MapTest extends TestCase {
    Connection cn = new Connection("localhost", 5555);
    double [][]colors=new double[1][3];

    Map map = new Map(2, cn, new Pane(), 1, colors);

    public void testPawnNumbers() throws Exception {
        assertEquals(map.pawnNumber, 3);
    }

    public void testSize() throws Exception {
        assertEquals(map.size, 9);
    }

    public void testFieldSize() throws Exception {
        assertEquals(map.FIELDSIZE, 35);
    }

    public void testSacingSize() throws Exception {
        assertEquals(map.SPACINGSIZE, 5);
    }

    public void testSent() throws Exception {
        assertNotNull(map.isSent);
    }

    public void testRadius() throws Exception {
        assertEquals(map.myColor.getRadius(), 20.0);
    }
}