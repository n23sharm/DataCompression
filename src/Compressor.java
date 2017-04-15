import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Compressor {

    @NotNull
    private Trie trie;

    private char[] compressed;

    public Compressor() {
        trie = new Trie();
    }

    public char[] getCompressed(@NotNull byte[] data) {
        compressed = new char[data.length];

        int index = 0;
        while(index < data.length) {
            char c = (char) data[index];

            List<Node> existing = trie.getNodes(c);

            if (existing.isEmpty()) {
                compressed[index] = c;
                trie.insert(c, index);
                index++;

            } else {
                int maxDepth = getMaxDepth(existing, index, data);

                if (maxDepth > 0) {
                    for (int i = index; i < index + maxDepth; ++i) {
                        char cachedChar = (char) data[i];
                        compressed[i] = Character.toUpperCase(cachedChar);
                        trie.insert(cachedChar, i);
                    }
                    index = index + maxDepth;
                } else {
                    compressed[index] = c;
                    trie.insert(c, index);
                    index++;
                }
            }
        }

        //trie.printTrie();
        return compressed;
    }

    private int getMaxDepth(List<Node> existing, int index, byte[] data) {

        if (index >= data.length) {
            return 0;
        }

        Node overallMaxDepthMatchingNode = existing.get(0);

        for (Node node : existing) {

            Node maxDepthMatchingNode = getMaxDepthMatchingNode(data, index, node);

            if (maxDepthMatchingNode.getDepth() > overallMaxDepthMatchingNode.getDepth()) {
                overallMaxDepthMatchingNode = maxDepthMatchingNode;
            }
        }

        // Only replace seen characters of length 3 or greater
        if (overallMaxDepthMatchingNode.getDepth() < 3) {
            return 0;
        }

        return overallMaxDepthMatchingNode.getDepth();
    }

    private Node getMaxDepthMatchingNode(byte[] data, int matchingIndex, Node node) {
        char match = (char) data[matchingIndex];
        Node childNode = node;
        Node maxDepthMatchingNode = node;

        while (childNode != null && childNode.getCharacter() == match) {
            maxDepthMatchingNode = childNode;
            childNode = childNode.getChild();
            matchingIndex++;
            if (matchingIndex >= data.length) {
                break;
            }
            match = (char) data[matchingIndex];
        }

        return maxDepthMatchingNode;
    }
}
