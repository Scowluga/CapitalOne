package dalu.capitalone;

import dalu.capitalone.counters.CStyleCounter;
import dalu.capitalone.counters.PythonStyleCounter;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * Performs general checks on implementations of AbstractCounter
 */
public class overallTest {

    @Test
    public void checkCStyleCounter() throws FileNotFoundException {
        CStyleCounter counter = new CStyleCounter("test1.java");
        assertEquals(counter.getnTotalLines(), 60);
        assertEquals(counter.getnCommentLines(), 28);
        assertEquals(counter.getnSingleComments(), 6);
        assertEquals(counter.getnMultiCommentLines(), 22);
        assertEquals(counter.getnMultiComments(), 2);
        assertEquals(counter.getnTODOs(), 1);

        counter = new CStyleCounter("test2.ts");
        assertEquals(counter.getnTotalLines(), 40);
        assertEquals(counter.getnCommentLines(), 23);
        assertEquals(counter.getnSingleComments(), 5);
        assertEquals(counter.getnMultiCommentLines(), 18);
        assertEquals(counter.getnMultiComments(), 4);
        assertEquals(counter.getnTODOs(), 1);

        counter = new CStyleCounter("stress_test.java");
        assertEquals(counter.getnTotalLines(), 2);
        assertEquals(counter.getnCommentLines(), 2);
        assertEquals(counter.getnSingleComments(), 2);
        assertEquals(counter.getnMultiCommentLines(), 1);
        assertEquals(counter.getnMultiComments(), 2);
        assertEquals(counter.getnTODOs(), 2);

    }

    @Test
    public void checkPythonStyleCounter() throws FileNotFoundException {
        PythonStyleCounter counter = new PythonStyleCounter("test3.py");
        assertEquals(counter.getnTotalLines(), 61);
        assertEquals(counter.getnCommentLines(), 19);
        assertEquals(counter.getnSingleComments(), 9);
        assertEquals(counter.getnMultiCommentLines(), 10);
        assertEquals(counter.getnMultiComments(), 3);
        assertEquals(counter.getnTODOs(), 3);

        counter = new PythonStyleCounter("stress_test.py");
        assertEquals(counter.getnTotalLines(), 17);
        assertEquals(counter.getnCommentLines(), 8);
        assertEquals(counter.getnSingleComments(), 5);
        assertEquals(counter.getnMultiCommentLines(), 3);
        assertEquals(counter.getnMultiComments(), 1);
        assertEquals(counter.getnTODOs(), 1);
    }
}