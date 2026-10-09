import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EnglishTextLineTest {
    @Test
    public void constructorSetsLanguageAndLength() {
        EnglishTextLine line = new EnglishTextLine("hello");
        assertEquals("Английский", line.getLanguage());
        assertEquals(5, line.getLength());
    }

    @Test
    public void emptyLineHasZeroLength() {
        assertEquals(0, new EnglishTextLine("").getLength());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsNullText() {
        new EnglishTextLine(null);
    }
}
