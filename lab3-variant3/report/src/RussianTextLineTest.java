import org.junit.Test;
import static org.junit.Assert.assertEquals;
 
public class RussianTextLineTest {
    @Test
    public void constructorSetsLanguageAndLength() {
        RussianTextLine line = new RussianTextLine("Привет");
        assertEquals("Русский", line.getLanguage());
        assertEquals(6, line.getLength());
    }
 
    @Test
    public void emptyLineHasZeroLength() {
        assertEquals(0, new RussianTextLine("").getLength());
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsNullText() {
        new RussianTextLine(null);
    }
}