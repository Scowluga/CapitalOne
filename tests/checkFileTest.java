import org.junit.Test;

import static org.junit.Assert.*;

public class checkFileTest {

    @Test
    public void quit() throws Exception {
        assertEquals(Main.checkFile("q"), Main.MESSAGE_QUIT);
    }

    @Test
    public void ignored() throws Exception {
        assertEquals(Main.checkFile(".ignored"), Main.MESSAGE_IGNORED);
    }

    @Test
    public void invalid() throws Exception {
        assertEquals(Main.checkFile("a.b.c"), Main.MESSAGE_INVALID);
        assertEquals(Main.checkFile("abc"), Main.MESSAGE_INVALID);
    }

    @Test
    public void valid() throws Exception {
        assertEquals(Main.checkFile("asdf.java"), Main.MESSAGE_SUCCESS);
    }

    @Test
    public void unsupported() throws Exception {
        assertEquals(Main.checkFile("test.py"), Main.MESSAGE_UNSUPPORTED);
        assertEquals(Main.checkFile("test.exe"), Main.MESSAGE_UNSUPPORTED);
    }
}