import org.junit.Test;

import static org.junit.Assert.*;

public class countTODOsTest {

    @Test
    public void checkLineStart() {
        assertEquals(Main.countTODOs("TODO--"), 1);
        assertEquals(Main.countTODOs("TODO -"), 1);
    }

    @Test
    public void checkLineEnd() {
        assertEquals(Main.countTODOs("- TODO"), 1);
        assertEquals(Main.countTODOs("--TODO"), 1);
    }

    @Test
    public void checkOnly() {
        assertEquals(Main.countTODOs("TODO"), 1);
    }

    @Test
    public void checkCases() {
        assertEquals(Main.countTODOs("toDo"), 1);
        assertEquals(Main.countTODOs("ToDO"), 1);
    }

    @Test
    public void checkSpecialChars() {
        assertEquals(Main.countTODOs(" &TODO^ "), 1);
        assertEquals(Main.countTODOs(" //TODO) "), 1);
        assertEquals(Main.countTODOs(" TODOb"), 0);
    }

}