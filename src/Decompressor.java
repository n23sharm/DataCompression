import com.sun.istack.internal.NotNull;

import java.io.*;

public class Decompressor {
    private enum ReadState {
        PENDING,
        READING_CHAR,
        READING_COMPRESSED
    }

    private static final int LOOKBACK_BUFFER_SIZE = 65536;
    private static final int READ_BUFFER_SIZE = 4096;

    // Masks that give us only the last i bits of data where i is the index in
    // the array. Basically 2^i - 1 precomputed to save time.
    private static final int[] READ_MASKS = new int[] {0, 1, 3, 7, 15, 31, 63, 127, 255};

    private static final boolean DEBUG_PRINT = false;

    public void decompress(@NotNull String compressedFilename, @NotNull String decompressedFilename) {
        StringBuilder lookbackBuffer = new StringBuilder();

        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        DataOutputStream dataOutputStream = null;

        try {
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            inputStream = new FileInputStream(compressedFilename);
            fileOutputStream = new FileOutputStream(decompressedFilename);
            dataOutputStream = new DataOutputStream(fileOutputStream);

            int currentlyReadingData = 0;
            int availableInCurrentByte = 8;
            int needToRead = 1;
            ReadState readState = ReadState.PENDING;
            int read = 0;

            while ((read = inputStream.read(buffer)) != -1) {
                int bytesProcessed = 0;

                while (bytesProcessed < read) {
                    if (readState == ReadState.PENDING) {
                        // Read the next chunk of data and figure out if it's a char
                        // or a compressed chunk
                        int nextBit = (buffer[bytesProcessed] >> (availableInCurrentByte - 1)) & 1;

                        if (nextBit == 0) {
                            readState = ReadState.READING_CHAR;
                            needToRead = 8;
                        } else {
                            readState = ReadState.READING_COMPRESSED;
                            needToRead = 22;
                        }

                        availableInCurrentByte -= 1;
                        currentlyReadingData = nextBit;
                    } else {
                        int canRead = Math.min(needToRead, availableInCurrentByte);

                        int newlyReadData = buffer[bytesProcessed];

                        // Trim any trailing bits from the byte that aren't needed for this read
                        newlyReadData = newlyReadData >> (availableInCurrentByte - canRead);

                        // Trim any leading bits from the byte that aren't needed for this read
                        newlyReadData = newlyReadData & READ_MASKS[canRead];

                        // Add the read chunk to the end of the currently read data
                        currentlyReadingData = (currentlyReadingData << canRead) | newlyReadData;

                        availableInCurrentByte -= canRead;
                        if (availableInCurrentByte == 0) {
                            // Move onto the next byte
                            bytesProcessed += 1;
                            availableInCurrentByte = 8;
                        }

                        needToRead -= canRead;
                        if (needToRead == 0) {
                            if (DEBUG_PRINT) {
                                System.out.println("ABOUT TO PROCESS: " + Integer.toBinaryString(currentlyReadingData));
                            }

                            if (readState == ReadState.READING_CHAR) {
                                // Read the last 8 bits and that is a char
                                char readCharacter = (char)(currentlyReadingData & 0b11111111);

                                if (DEBUG_PRINT) {
                                    System.out.println("READ CHARACTER: " + readCharacter);
                                }

                                dataOutputStream.writeByte(readCharacter);

                                lookbackBuffer.append(readCharacter);
                                if (lookbackBuffer.length() > LOOKBACK_BUFFER_SIZE) {
                                    lookbackBuffer.delete(0, lookbackBuffer.length() - LOOKBACK_BUFFER_SIZE);
                                }
                            } else if (readState == ReadState.READING_COMPRESSED) {
                                int length = currentlyReadingData & 0b111111;
                                length += 3;

                                int offset = (currentlyReadingData >> 6) & 0b1111111111111111;

                                if (DEBUG_PRINT) {
                                    System.out.println("READ COMPRESSED. OFFSET: " + offset + " - LENGTH: " + length);
                                }

                                int lookbackStartIndex = lookbackBuffer.length() - offset;
                                String lookbackData = lookbackBuffer.substring(lookbackStartIndex, lookbackStartIndex + length);

                                if (DEBUG_PRINT) {
                                    System.out.println("READ COMPRESSED. DATA: " + lookbackData);
                                }

                                dataOutputStream.writeBytes(lookbackData);

                                lookbackBuffer.append(lookbackData);
                                if (lookbackBuffer.length() > LOOKBACK_BUFFER_SIZE) {
                                    lookbackBuffer.delete(0, lookbackBuffer.length() - LOOKBACK_BUFFER_SIZE);
                                }
                            }

                            currentlyReadingData = 0;
                            needToRead = 1;
                            readState = ReadState.PENDING;
                        }
                    }
                }
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
}