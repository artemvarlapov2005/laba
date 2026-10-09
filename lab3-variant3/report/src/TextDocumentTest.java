import org.junit.Test;
import static org.junit.Assert.assertEquals;
 
public class TextDocumentTest {
    @Test
    public void emptyDocumentHasZeroLength() {
        assertEquals(0, new TextDocument().getLength());
    }
 
    @Test
    public void documentUsesLengthProvidedByStub() {
        TextDocument document = new TextDocument();
        document.addLine(new TextLineStub(7));
        assertEquals(7, document.getLength());
    }
 
    @Test
    public void documentSumsStubLengths() {
        TextDocument document = new TextDocument();
        document.addLine(new TextLineStub(3));
        document.addLine(new TextLineStub(0));
        document.addLine(new TextLineStub(5));
        assertEquals(8, document.getLength());
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void documentRejectsNullLine() {
        new TextDocument().addLine(null);
    }
}