import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Compression is done with the following encoding:
 *
 * - 0 bit followed by 8 bits represents a single byte (9 bits total).
 * - 1 bit followed by 16 bits that store how many bytes ago we should start copying
 *   from, followed by 6 bits that store the number of bytes to copy (23 bits total)
 *
 *   Note since minimum cache length is 3, 6 bits represent lengths from 3-66 instead of 0-63
 *   via LENGTH_OFFSET.
 */
public class Compressor {

    @NotNull
    private Cache cache;

    @NotNull
    private BinaryUtils binaryUtils;

    private static final boolean DEBUG_PRINT = false;

    public Compressor() {
        cache = new Cache();
        binaryUtils = new BinaryUtils();
    }

    @NotNull
    public void compress(@NotNull String inputFilename, @NotNull String compressedFilename) {
        StringBuilder binarySB = new StringBuilder();

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        DataOutputStream dataOutputStream = null;

        try {
            inputStream = new FileInputStream(inputFilename);
            fileOutputStream = new FileOutputStream(compressedFilename);
            dataOutputStream = new DataOutputStream(fileOutputStream);

            byte[] data = Files.readAllBytes(Paths.get(inputFilename));
            if (data == null || data.length <= 0) {
                return;
            }

            List<CompressedFormat> compressedFormattedData = getCompressedFormattedData(data);

            for (CompressedFormat compressedFormat : compressedFormattedData) {
                if (compressedFormat.getPlacement() != null)  {
                    Placement placement = compressedFormat.getPlacement();

                    String offsetBinary = Integer.toBinaryString(placement.getOffset());

                    if (offsetBinary.length() < 16) {
                        int paddingLength = 16 - offsetBinary.length();
                        offsetBinary = binaryUtils.getZeroPadding(paddingLength) + offsetBinary;
                    }

                    String lengthBinary = Integer.toBinaryString(placement.getLength() - Cache.LENGTH_OFFSET);
                    if (lengthBinary.length() < 6) {
                        int paddingLength = 6 - lengthBinary.length();
                        lengthBinary = binaryUtils.getZeroPadding(paddingLength) + lengthBinary;
                    }

                    binarySB.append("1").append(offsetBinary).append(lengthBinary);
                } else {

                    String charBinary = Integer.toBinaryString(compressedFormat.getCharacter());
                    if (charBinary.length() < 8) {
                        int paddingLength = 8 - charBinary.length();
                        charBinary = binaryUtils.getZeroPadding(paddingLength) + charBinary;
                    }
                    binarySB.append("0").append(charBinary);
                }

                if (binarySB.length() > 8) {
                    dataOutputStream.writeBytes(binarySB.substring(0, 8));
                    binarySB.delete(0, 8);
                }
            }

            if (binarySB.length() > 8) {
                dataOutputStream.writeBytes(binarySB.substring(0));
            }

        } catch (Exception e) {
            if (DEBUG_PRINT) {
                System.out.println("ERROR FOUND! " + e.toString());
            }
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // Ignore
            }

            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException e) {
                // Ignore
            }

            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    @NotNull
    private List<CompressedFormat> getCompressedFormattedData(@NotNull byte[] data) {
        List<CompressedFormat> compressedFormat = new ArrayList<>();

        int index = 0;
        while(index < data.length) {
            char c = (char) data[index];

            List<Node> existingNodes = cache.getMatchingNodes(c);

            if (existingNodes.isEmpty()) {
                compressedFormat.add(new CompressedFormat(c));
                cache.insert(c, index);
                index++;

            } else {
                Placement nodePlacement = getNodePlacementForMaxSequence(existingNodes, index, data);

                if (nodePlacement != null && nodePlacement.getLength() > 0 && nodePlacement.getOffset() < Cache.MAX_OFFSET) {
                    for (int i = index; i < index + nodePlacement.getLength(); ++i) {
                        char cachedChar = (char) data[i];
                        cache.insert(cachedChar, i);
                    }

                    compressedFormat.add(new CompressedFormat(nodePlacement));
                    index = index + nodePlacement.getLength();
                } else {
                    compressedFormat.add(new CompressedFormat(c));
                    cache.insert(c, index);
                    index++;
                }
            }
        }

        return compressedFormat;
    }

    @Nullable
    private Placement getNodePlacementForMaxSequence(List<Node> existing, int index, byte[] data) {
        if (index >= data.length) {
            return null;
        }

        int overallMaxSequenceLength = 0;
        Node overallMaxSequenceNodeHead = existing.get(0);

        for (Node node : existing) {
            Node maxDepthMatchingNode = getFurthestMatchingNode(data, index, node);

            int sequenceLength = maxDepthMatchingNode.getDepth() - node.getDepth();
            if (sequenceLength > overallMaxSequenceLength && sequenceLength <= Cache.MAX_SEQUENCE_LENGTH - Cache.LENGTH_OFFSET) {
                overallMaxSequenceNodeHead = node;
                overallMaxSequenceLength = sequenceLength;
            }
        }

        // Only replace seen characters of length 3 or greater
        if (overallMaxSequenceLength < 3) {
            return null;
        }

        int offset = index - overallMaxSequenceNodeHead.getIndex();
        int length = overallMaxSequenceLength;

        return new Placement(offset, length);
    }

    @NotNull
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
