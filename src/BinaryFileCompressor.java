import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BinaryFileCompressor {

    public static void main(String[] args) {

        BinaryFileCompressor binaryFileCompressor = new BinaryFileCompressor();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 1 ~~~~~~~~~~~~~~");
        String inputFile1 = "./input1.bin";
        String compressedFile1 = "./compressed1.bin";
        String decompressedFile1 = "./decompressed1.bin";
        int compressed1Length = binaryFileCompressor.compress(inputFile1, compressedFile1);
        binaryFileCompressor.decompress(compressedFile1, decompressedFile1, compressed1Length);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 2 ~~~~~~~~~~~~~~");
        String inputFile2 = "input2.bin";
        String compressedFile2 = "./compressed2.bin";
        String decompressedFile2 = "./decompressed2.bin";
        int compressed2Length = binaryFileCompressor.compress(inputFile2, compressedFile2);
        binaryFileCompressor.decompress(compressedFile2, decompressedFile2, compressed2Length);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 3 ~~~~~~~~~~~~~~");
        String inputFile3 = "input3.bin";
        String compressedFile3 = "./compressed3.bin";
        String decompressedFile3 = "./decompressed3.bin";
        int compressed3Length = binaryFileCompressor.compress(inputFile3, compressedFile3);
        binaryFileCompressor.decompress(compressedFile3, decompressedFile3, compressed3Length);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 4 ~~~~~~~~~~~~~~");
        String inputFile4 = "input4.bin";
        String compressedFile4 = "./compressed4.bin";
        String decompressedFile4 = "./decompressed4.bin";
        int compressed4Length = binaryFileCompressor.compress(inputFile4, compressedFile4);
        binaryFileCompressor.decompress(compressedFile4, decompressedFile4, compressed4Length);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();
    }

    /**
     *
     * @return Length of compressed string
     */
    public int compress(@NotNull String inputFilename, @NotNull String compressedFilename) {
        Compressor compressor = new Compressor();
        byte[] dataToCompress = getFileData(inputFilename);
        if (dataToCompress == null || dataToCompress.length <= 0) {
            return 0;
        }
        String compressedBinary = compressor.getCompressedBinaryRepresentation(dataToCompress);
        outputCompressedBinaryFile(compressedBinary, compressedFilename);
        return compressedBinary.length();
    }

    public void decompress(@NotNull String compressedFilename, @NotNull String decompressedFilename, int compressedLength) {
        Decompressor decompressor = new Decompressor();
        byte[] dataToDecompress = getFileData(compressedFilename);
        if (dataToDecompress == null) {
            return;
        }
        String decompressed = decompressor.getDecompressedData(dataToDecompress, compressedLength);
        outputDecompressedBinaryFile(decompressed, decompressedFilename);
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

    private void outputCompressedBinaryFile(@NotNull String compressedData, @NotNull String filename) {
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(new FileOutputStream(filename));

            String[] splits = compressedData.split("(?<=\\G\\d{8})");
            for (String s : splits) {
                int val = Integer.parseInt(s, 2);
                byte b = (byte) val;
                os.write(b);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void outputDecompressedBinaryFile(@NotNull String decompressedData, @NotNull String filename) {
        DataOutputStream os = null;
        try {
            os = new DataOutputStream(new FileOutputStream(filename));
            os.writeBytes(decompressedData);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
