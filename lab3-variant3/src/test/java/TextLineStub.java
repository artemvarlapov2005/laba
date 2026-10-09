// Программная заглушка: заменяет настоящую строку в тестах документа.
public class TextLineStub extends TextLine {
    private final int fixedLength;

    public TextLineStub(int fixedLength) {
        super("", "Заглушка");
        this.fixedLength = fixedLength;
    }

    @Override
    public int getLength() {
        return fixedLength;
    }
}
