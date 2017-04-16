import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.HashSet;

public class Node {

    private char character;
    private int index;
    private int depth;

    @Nullable
    private Node child;

    public Node(char character, int index, int depth) {
        this.character = character;
        this.index = index;
        this.depth = depth;
    }

    public char getCharacter() {
        return character;
    }

    public int getIndex() {
        return index;
    }

    public int getDepth() {
        return depth;
    }

    public Node getChild() {
        return child;
    }

    public void addChild(@NotNull Node child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return character + "(" + index + ", " + depth + ")";
    }
}
