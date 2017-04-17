import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Compressor {

    @NotNull
    private Cache cache;

    public Compressor() {
        cache = new Cache();
    }

    public String getCompressed(@NotNull byte[] data) {

        StringBuilder binarySB = new StringBuilder();

        List<CompressedFormat> compressedFormattedData = getCompressedFormattedData(data);

        for (CompressedFormat compressedFormat : compressedFormattedData) {
            if (compressedFormat.getCopy() != null)  {
                Copy copy = compressedFormat.getCopy();

                String offsetBinary = Integer.toBinaryString(copy.getOffset());
                String lengthBinary = Integer.toBinaryString(copy.getLength());

                binarySB.append("1").append(offsetBinary).append(lengthBinary);
            } else {

                String charBinary = Integer.toBinaryString(compressedFormat.getCharacter());
                binarySB.append("0").append(charBinary);
            }
        }

       return binarySB.toString();
    }

    public List<CompressedFormat> getCompressedFormattedData(@NotNull byte[] data) {
        List<CompressedFormat> compressedBinary = new ArrayList<CompressedFormat>();

        int index = 0;
        while(index < data.length) {
            char c = (char) data[index];

            List<Node> existing = cache.getNodes(c);

            if (existing.isEmpty()) {
                compressedBinary.add(new CompressedFormat(c));
                cache.insert(c, index);
                index++;

            } else {
                //Node maxDepthNode = getMaxDepthMatchingNode(existing, index, data);
                Copy nodeCopy = getNodeCopy(existing, index, data);

                if (nodeCopy != null && nodeCopy.getLength() > 0) {
                    for (int i = index; i < index + nodeCopy.getLength(); ++i) {
                        char cachedChar = (char) data[i];
                        cache.insert(cachedChar, i);
                    }
                    compressedBinary.add(new CompressedFormat(nodeCopy));
                    index = index + nodeCopy.getLength();
                } else {
                    compressedBinary.add(new CompressedFormat(c));
                    cache.insert(c, index);
                    index++;
                }
            }
        }

        //System.out.println(compressedBinary);
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

        int offset = index - overallMaxDepthHead.getIndex() - 1;
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
