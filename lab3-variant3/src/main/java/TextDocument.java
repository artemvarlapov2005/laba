import java.util.ArrayList;
import java.util.List;

public class TextDocument {
    private final List<TextLine> lines = new ArrayList<>();

    public void addLine(TextLine line) {
        if (line == null) {
            throw new IllegalArgumentException("Line must not be null");
        }
        lines.add(line);
    }

    public int getLength() {
        int length = 0;
        for (TextLine line : lines) {
            length += line.getLength();
        }
        return length;
    }
}
