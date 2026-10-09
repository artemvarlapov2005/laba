import java.lang.reflect.Field;
import org.junit.Test;
 
import static org.junit.Assert.*;
 
public class TextLineTest {
    private char[] charactersOf(TextLine line) throws Exception {
        Field field = TextLine.class.getDeclaredField("characters");
        field.setAccessible(true);
        return (char[]) field.get(line);
    }
 
    @Test
    public void constructorStoresFields() throws Exception {
        TextLine line = new TextLine("Привет", "Русский");
        assertEquals(6, line.getLength());
        assertEquals("Русский", line.getLanguage());
        assertArrayEquals("Привет".toCharArray(), charactersOf(line));
    }
 
    @Test
    public void emptyTextHasZeroLength() throws Exception {
        TextLine line = new TextLine("", "Русский");
        assertEquals(0, line.getLength());
        assertArrayEquals(new char[0], charactersOf(line));
    }
 
    @Test
    public void languageCanBeChanged() {
        TextLine line = new TextLine("abc", "Русский");
        line.setLanguage("Английский");
        assertEquals("Английский", line.getLanguage());
        assertEquals(3, line.getLength());
    }
 
    @Test
    public void lengthCanBeReducedIncreasedAndSetToZero() throws Exception {
        TextLine line = new TextLine("abc", "Английский");
        line.setLength(2);
        assertEquals(2, line.getLength());
        assertArrayEquals(new char[] {'a', 'b'}, charactersOf(line));
        line.setLength(4);
        assertEquals(4, line.getLength());
        assertArrayEquals(new char[] {'a', 'b', '\0', '\0'}, charactersOf(line));
        line.setLength(0);
        assertEquals(0, line.getLength());
        assertArrayEquals(new char[0], charactersOf(line));
    }
 
    @Test
    public void appendCopiesCharactersAndRecalculatesLength() throws Exception {
        TextLine first = new TextLine("Привет, ", "Русский");
        TextLine second = new TextLine("world", "Английский");
        first.append(second);
        assertEquals(13, first.getLength());
        assertArrayEquals("Привет, world".toCharArray(), charactersOf(first));
        assertEquals("Русский", first.getLanguage());
        assertEquals(5, second.getLength());
        assertArrayEquals("world".toCharArray(), charactersOf(second));
    }
 
    @Test
    public void appendHandlesEmptyLines() throws Exception {
        TextLine line = new TextLine("", "Английский");
        line.append(new TextLine("abc", "Английский"));
        line.append(new TextLine("", "Английский"));
        assertEquals(3, line.getLength());
        assertArrayEquals("abc".toCharArray(), charactersOf(line));
    }
 
    @Test
    public void lineCanBeAppendedToItself() throws Exception {
        TextLine line = new TextLine("ab", "Английский");
        line.append(line);
        assertEquals(4, line.getLength());
        assertArrayEquals("abab".toCharArray(), charactersOf(line));
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsNullText() {
        new TextLine(null, "Русский");
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsNullLanguage() {
        new TextLine("abc", null);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void constructorRejectsEmptyLanguage() {
        new TextLine("abc", "");
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void setterRejectsBlankLanguage() {
        new TextLine("abc", "Русский").setLanguage("   ");
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void setterRejectsNullLanguage() {
        new TextLine("abc", "Русский").setLanguage(null);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void lengthCannotBeNegative() {
        new TextLine("abc", "Английский").setLength(-1);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void appendRejectsNull() {
        new TextLine("abc", "Английский").append(null);
    }
}