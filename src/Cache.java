import com.sun.istack.internal.NotNull;

import java.util.ArrayList;

public class Cache {

    // We can only search for characters that are a max of 65535 bytes away since this is represented with 16 bits
    public static int MAX_OFFSET = 65535;

    // We are representing 3-66 with 6 bits. This offset maps the given value with actual length.
    public static int LENGTH_OFFSET = 3;

    // The maximum length a cached sequence is allowed
    public static int MAX_SEQUENCE_LENGTH = 66;

    @NotNull
    private ArrayList<Node> cache;

    public Cache() {
        cache = new ArrayList<Node>();
    }

    public void insert(char c, int index) {
        if (cache.isEmpty()) {
            cache.add(new Node(c, index, 1));
        } else {
            int lastDepth = cache.get(cache.size() - 1).getDepth();
            Node nodeToAdd = new Node(c, index, lastDepth + 1);
            Node lastNode = cache.get(cache.size() - 1);
            lastNode.addChild(nodeToAdd);
            cache.add(nodeToAdd);
        }

        // Clearing cache of elements that are no longer required
        if (cache.size() > MAX_OFFSET) {
            cache.remove(0);
        }
    }

    @NotNull
    public ArrayList<Node> getMatchingNodes(char c) {
        ArrayList<Node> existingNodes = new ArrayList<>();

        for (Node n : cache) {
            if (n.getCharacter() == c) {
                existingNodes.add(n);
            }
        }

        return existingNodes;
    }
}
