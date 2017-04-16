import com.sun.istack.internal.Nullable;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BinaryFileCompressor {

    public static void main(String[] args) {

        /**
         *  X Read file in binary format
         *  - Pass through Compressor
         *  - Pass compressed version through Decompressor
         */
        BinaryFileCompressor binaryFileCompressor = new BinaryFileCompressor();
        byte[] data = binaryFileCompressor.getFileDataInBinary();

        if (data == null) {
            return;
        }

        System.out.print("Data size = " + data.length);

        Compressor compressor = new Compressor();
        List<Byte> compressed = compressor.getCompressed(data);
        binaryFileCompressor.outputCompressedBinaryFile(compressed);

        System.out.print("Compressed size = " + compressed.size());

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

    private void outputCompressedBinaryFile(List<Byte> compressedData) {
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(new FileOutputStream("./compressed.bin"));
            for (Byte data : compressedData) {
                os.write(data);
            }
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
