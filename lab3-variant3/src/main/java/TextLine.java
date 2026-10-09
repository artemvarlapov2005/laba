import java.util.Arrays;

public class TextLine {
    private int length;
    private char[] characters;
    private String language;

    public TextLine(String text, String language) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }
        setLanguage(language);
        characters = text.toCharArray();
        length = characters.length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative");
        }
        characters = Arrays.copyOf(characters, length);
        this.length = characters.length;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language must not be empty");
        }
        this.language = language;
    }

    public void append(TextLine other) {
        if (other == null) {
            throw new IllegalArgumentException("Other line must not be null");
        }
        char[] result = Arrays.copyOf(characters, length + other.length);
        System.arraycopy(other.characters, 0, result, length, other.length);
        characters = result;
        length = characters.length;
    }
}
