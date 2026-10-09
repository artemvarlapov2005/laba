import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TextDocumentIntegrationTest {
    // Этап 2: заглушки заменены настоящими объектами базового класса.
    @Test
    public void documentWorksWithRealTextLines() {
        TextDocument document = new TextDocument();
        document.addLine(new TextLine("abc", "Английский"));
        document.addLine(new TextLine("de", "Английский"));
        assertEquals(5, document.getLength());
    }

    // Этап 3: подключены классы-наследники.
    @Test
    public void documentWorksWithBothSubclasses() {
        TextDocument document = new TextDocument();
        document.addLine(new RussianTextLine("Привет"));
        document.addLine(new EnglishTextLine("hello"));
        assertEquals(11, document.getLength());
    }

    @Test
    public void documentReflectsChangesInItsLines() {
        TextDocument document = new TextDocument();
        RussianTextLine line = new RussianTextLine("Привет");
        document.addLine(line);
        assertEquals(6, document.getLength());
        line.append(new RussianTextLine("!"));
        assertEquals(7, document.getLength());
        line.setLength(2);
        assertEquals(2, document.getLength());
    }
}
