import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class CompressedFormat {

    private char character;

    @Nullable
    private Placement placement;

    public CompressedFormat(char character) {
        this.character = character;
        this.placement = null;
    }

    public CompressedFormat(@NotNull Placement placement) {
        this.character = 0;
        this.placement = placement;
    }

    public char getCharacter() {
        return character;
    }

    @Nullable
    public Placement getPlacement() {
        return placement;
    }

    @Override
    public String toString() {
        if (placement != null) {
            return "(" + placement.getOffset() + "," + placement.getLength() + ")";
        }
        return String.valueOf(character);
    }
}
