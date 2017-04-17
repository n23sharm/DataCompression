import com.sun.istack.internal.NotNull;

public class Decompressor {

    @NotNull
    public String getDecompressedData(byte[] data, int totalLength) {
        StringBuilder decompressedBuilder = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            String binary = String.format("%8s", Integer.toBinaryString(data[i] & 0xFF)).replace(" ", "");

            // For the last number, ensure the total length matches compressed length
            if (i == data.length - 1) {
                int remainingLength = totalLength - decompressedBuilder.length();
                if (remainingLength > binary.length()) {
                    binary = getZeroPadding(remainingLength - binary.length()) + binary;
                }
            } else if (binary.length() < 8) {
                // Pad all binary numbers with 0 to ensure a length of size 8,
                binary = getZeroPadding(8 - binary.length()) + binary;
            }
            decompressedBuilder.append(binary);
        }

        String parsed = parseBinaryString(decompressedBuilder.toString());
        return parsed;
    }

    @NotNull
    private String getZeroPadding(int paddingLength) {
        StringBuilder zeroBuilder = new StringBuilder(paddingLength);
        for (int i = 0; i < paddingLength; i++) {
            zeroBuilder.append("0");
        }
        return zeroBuilder.toString();
    }

    @NotNull
    private String parseBinaryString(String binary) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int totalLength = binary.length();

        while (index < totalLength) {

            if (binary.charAt(index) == '0') {
                int endIndex = index + 9;
                String binaryChar;

                if (endIndex < totalLength) {
                    binaryChar = binary.substring(index + 1, endIndex);
                } else {
                    binaryChar = binary.substring(index + 1);
                }

                int parseInt = Integer.parseInt(binaryChar, 2);
                char c = (char)parseInt;
                sb.append(c);
                index = index + 9;

            } else {
                int endOffsetIndex = index + 17;
                int endLengthIndex = index + 23;

                if (endOffsetIndex <= totalLength && endLengthIndex <= totalLength) {
                    System.out.print("index = " + index);
                    String binaryOffset = binary.substring(index + 1, endOffsetIndex);
                    String binaryLength = binary.substring(index + 17, endLengthIndex);

                    int offset = Integer.parseInt(binaryOffset, 2);
                    int length = Integer.parseInt(binaryLength, 2);

                    String sbSoFar = sb.toString();
                    int startIndex = sb.length() - offset;

                    String characters = sbSoFar.substring(startIndex, startIndex + length);
                    sb.append(characters);

                } else {
                    String binaryChar = binary.substring(index + 1);
                    int parseInt = Integer.parseInt(binaryChar, 2);
                    char c = (char)parseInt;
                    sb.append(c);
                }
                index = index + 23;
            }
        }

        return sb.toString();
    }
}
