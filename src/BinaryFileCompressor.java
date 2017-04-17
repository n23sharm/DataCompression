import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BinaryFileCompressor {

    public static void main(String[] args) {

        BinaryFileCompressor binaryFileCompressor = new BinaryFileCompressor();

        Compressor compressor = new Compressor();
        byte[] dataToCompress = binaryFileCompressor.getFileData("./test.bin");
        if (dataToCompress == null) {
            return;
        }
        String compressed = compressor.getCompressedData(dataToCompress);
        System.out.println("compressed = " + compressed);
        binaryFileCompressor.outputCompressedBinaryFile(compressed);

        Decompressor decompressor = new Decompressor();
        byte[] dataToDecompress = binaryFileCompressor.getFileData("./compressed.bin");
        if (dataToDecompress == null) {
            return;
        }
        String decompressed = decompressor.getDecompressedData(dataToDecompress, compressed.length());
        binaryFileCompressor.outputDecompressedBinaryFile(decompressed);
    }

    @Nullable
    private byte[] getFileData(String filename) {
        try {
            return Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void outputCompressedBinaryFile(@NotNull String compressedData) {
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(new FileOutputStream("./compressed.bin"));

            String[] splits = compressedData.split("(?<=\\G\\d{8})");
            for (String s : splits) {
                int val = Integer.parseInt(s, 2);
                byte b = (byte) val;
                System.out.print(" " + b);
                os.write(b);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void outputDecompressedBinaryFile(@NotNull String decompressedData) {
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(new FileOutputStream("./decompressed.bin"));
            os.writeBytes(decompressedData);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
