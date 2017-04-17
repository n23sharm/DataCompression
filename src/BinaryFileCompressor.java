import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BinaryFileCompressor {

    public static void main(String[] args) {

        BinaryFileCompressor binaryFileCompressor = new BinaryFileCompressor();
        Decompressor decompressor = new Decompressor();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 1 ~~~~~~~~~~~~~~");
        String inputFile1 = "./input1.bin";
        String compressedFile1 = "./compressed1.bin";
        String decompressedFile1 = "./decompressed1.bin";
        int compressed1Length = binaryFileCompressor.compress(inputFile1, compressedFile1);
        decompressor.decompress(compressedFile1, decompressedFile1);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 2 ~~~~~~~~~~~~~~");
        String inputFile2 = "input2.bin";
        String compressedFile2 = "./compressed2.bin";
        String decompressedFile2 = "./decompressed2.bin";
        int compressed2Length = binaryFileCompressor.compress(inputFile2, compressedFile2);
        decompressor.decompress(compressedFile2, decompressedFile2);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 3 ~~~~~~~~~~~~~~");
        String inputFile3 = "input3.bin";
        String compressedFile3 = "./compressed3.bin";
        String decompressedFile3 = "./decompressed3.bin";
        int compressed3Length = binaryFileCompressor.compress(inputFile3, compressedFile3);
        decompressor.decompress(compressedFile3, decompressedFile3);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 4 ~~~~~~~~~~~~~~");
        String inputFile4 = "input4.bin";
        String compressedFile4 = "./compressed4.bin";
        String decompressedFile4 = "./decompressed4.bin";
        int compressed4Length = binaryFileCompressor.compress(inputFile4, compressedFile4);
        decompressor.decompress(compressedFile4, decompressedFile4);
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
        System.out.println(compressedBinary);
        outputCompressedBinaryFile(compressedBinary, compressedFilename);
        return compressedBinary.length();
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
                if (s.length() < 8) {
                    s = String.format("%-8s", s).replace(' ', '0');
                }
                int val = Integer.parseInt(s, 2);
                byte b = (byte) val;
                os.write(b);
            }
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
