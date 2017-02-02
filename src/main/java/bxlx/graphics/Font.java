package bxlx.graphics;

/**
 * Created by qqcs on 2017.02.02..
 */
public class Font {
    private final String name;
    private final int size;
    private final boolean italic;
    private final boolean bold;

    public Font(String name, int size, boolean italic, boolean bold) {
        this.name = name;
        this.size = size;
        this.italic = italic;
        this.bold = bold;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isBold() {
        return bold;
    }

    public Font withSize(int size) {
        return new Font(name, size, italic, bold);
    }
}
