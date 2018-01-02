package Client.Network;

import junit.framework.TestCase;

public class ConnectionTest extends TestCase {
    Connection cn = new Connection("localhost", 5555);

    public void testStart() throws Exception {
        assertTrue(cn.start());
    }

    public void testConnected() throws Exception {
        assertNotNull(cn.connectionThread);
    }

}