package dalu.capitalone;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Checks the static <code>cleanEscapedQuotes</code> method of AbstractCounter
 */
public class cleanEscapedQuotesTest {

    /**
     * Are all double escapes removed?
     */
    @Test
    public void testDoubleEscape() {
        assertEquals(
                "Hello World!",
                AbstractCounter.cleanEscapedQuotes("\\\\\\\\Hello \\\\World!")
        );
    }

    /**
     * Are all escaped quotes removed?
     */
    @Test
    public void testEscapedQuotes() {
        assertEquals(
                " ",
                AbstractCounter.cleanEscapedQuotes("\\\" \\\'")
        );
    }
}