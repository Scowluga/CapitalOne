import javafx.util.Pair;
import org.junit.Test;

import static org.junit.Assert.*;

public class nextOccurTest {

    @Test
    public void checkBasic() {
        assertEquals(
                AbstractCounter.nextOccur("abc", "a", "b", "c"),
                new Pair<Integer, Integer>(0, 0)
        );

        assertEquals(
                AbstractCounter.nextOccur("bac", "a", "b", "c"),
                new Pair<Integer, Integer>(1, 0)
        );

        assertEquals(
                AbstractCounter.nextOccur("cba", "a", "b", "c"),
                new Pair<Integer, Integer>(2, 0)
        );
    }
}