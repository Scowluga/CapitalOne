package dalu.capitalone;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Checks the static <code>hasTODO</code> method of AbstractCounter
 */
public class hasTODOTest {

    @Test
    public void checkLineStart() {
        assertTrue(AbstractCounter.hasTODO("TODO--"));
        assertTrue(AbstractCounter.hasTODO("TODO -"));
    }

    @Test
    public void checkLineEnd() {
        assertTrue(AbstractCounter.hasTODO("- TODO"));
        assertTrue(AbstractCounter.hasTODO("--TODO"));
    }

    @Test
    public void checkOnly() {
        assertTrue(AbstractCounter.hasTODO("TODO"));
    }

    @Test
    public void checkCases() {
        assertTrue(AbstractCounter.hasTODO("toDo"));
        assertTrue(AbstractCounter.hasTODO("ToDO"));
    }

    @Test
    public void checkSpecialChars() {
        assertTrue(AbstractCounter.hasTODO(" &TODO^ "));
        assertTrue(AbstractCounter.hasTODO(" //TODO) "));
        assertFalse(AbstractCounter.hasTODO(" TODOb"));
        assertFalse(AbstractCounter.hasTODO(" TODO_"));
    }

}