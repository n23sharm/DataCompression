import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Compressor {

    @NotNull
    private Trie trie;

    public Compressor() {
        trie = new Trie();
    }

    public List<Byte> getCompressed(@NotNull byte[] data) {
        List<Byte> compressed = new ArrayList<>();
        List<CompressedFormat> compressedFormattedData = getCompressedFormattedData(data);

        for (CompressedFormat compressedFormat : compressedFormattedData) {
            if (compressedFormat.getCopy() != null)  {
                Copy copy = compressedFormat.getCopy();

                compressed.add(Byte.valueOf("1"));
                compressed.add((byte) copy.getOffset());
                compressed.add((byte) copy.getLength());

            } else {
                compressed.add(Byte.valueOf("0"));
                compressed.add((byte) compressedFormat.getCharacter());
            }
        }

        return compressed;
    }

    public List<CompressedFormat> getCompressedFormattedData(@NotNull byte[] data) {
        char[] compressed = new char[data.length];
        List<CompressedFormat> compressedBinary = new ArrayList<CompressedFormat>();

        int index = 0;
        while(index < data.length) {
            char c = (char) data[index];

            List<Node> existing = trie.getNodes(c);

            if (existing.isEmpty()) {
                compressed[index] = c;
                compressedBinary.add(new CompressedFormat(c));
                trie.insert(c, index);
                index++;

            } else {
                //Node maxDepthNode = getMaxDepthMatchingNode(existing, index, data);
                Copy nodeCopy = getNodeCopy(existing, index, data);

                if (nodeCopy != null && nodeCopy.getLength() > 0) {
                    for (int i = index; i < index + nodeCopy.getLength(); ++i) {
                        char cachedChar = (char) data[i];
                        compressed[i] = Character.toUpperCase(cachedChar);

                        trie.insert(cachedChar, i);
                    }
                    compressedBinary.add(new CompressedFormat(nodeCopy));
                    index = index + nodeCopy.getLength();
                } else {
                    compressed[index] = c;
                    compressedBinary.add(new CompressedFormat(c));
                    trie.insert(c, index);
                    index++;
                }
            }
        }

        System.out.println(compressedBinary);
        return compressedBinary;
    }

    @Nullable
    private Copy getNodeCopy(List<Node> existing, int index, byte[] data) {
        if (index >= data.length) {
            return null;
        }

        Node overallMaxDepthMatchingNode = existing.get(0);
        Node overallMaxDepthHead = existing.get(0);

        for (Node node : existing) {
            Node maxDepthMatchingNode = getFurthestMatchingNode(data, index, node);

            if (maxDepthMatchingNode.getDepth() > overallMaxDepthMatchingNode.getDepth()) {
                overallMaxDepthMatchingNode = maxDepthMatchingNode;
                overallMaxDepthHead = node;
            }
        }

        // Only replace seen characters of length 3 or greater
        if (overallMaxDepthMatchingNode.getDepth() < 3) {
            return null;
        }

        int offset = overallMaxDepthHead.getIndex() - index + 1;
        int length = overallMaxDepthMatchingNode.getDepth() - 1;
        return new Copy(offset, length);
    }

    private Node getFurthestMatchingNode(byte[] data, int matchingIndex, Node node) {
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
