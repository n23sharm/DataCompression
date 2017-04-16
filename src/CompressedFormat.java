import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class CompressedFormat {

    private char character;

    @Nullable
    private Copy copy;

    public CompressedFormat(char character) {
        this.character = character;
        this.copy = null;
    }

    public CompressedFormat(@NotNull Copy copy) {
        this.character = 0;
        this.copy = copy;
    }

    public char getCharacter() {
        return character;
    }

    @Nullable
    public Copy getCopy() {
        return copy;
    }

    @Override
    public String toString() {
        if (copy != null) {
            return "(" + copy.getOffset() + "," + copy.getLength() + ")";
        }
        return String.valueOf(character);
    }
}
