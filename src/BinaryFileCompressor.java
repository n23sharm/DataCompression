import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BinaryFileCompressor {

    public static void main(String[] args) {

        BinaryFileCompressor binaryFileCompressor = new BinaryFileCompressor();
        byte[] data = binaryFileCompressor.getFileDataInBinary();

        if (data == null) {
            return;
        }

        Compressor compressor = new Compressor();
        String compressed = compressor.getCompressed(data);
        binaryFileCompressor.outputCompressedBinaryFile(compressed);
    }

    @Nullable
    private byte[] getFileDataInBinary() {
        try {
            return Files.readAllBytes(Paths.get("./test.bin"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void outputCompressedBinaryFile(String compressedData) {
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(new FileOutputStream("./compressed.bin"));

            String[] pairs = compressedData.split("(?<=\\G\\d{8})");
            for (String p : pairs) {
                int val = Integer.parseInt(p, 2);
                byte b = (byte) val;
                os.write(b);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
