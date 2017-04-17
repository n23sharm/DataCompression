public class BinaryFileCompressor {

    public static void main(String[] args) {

        BinaryFileCompressor binaryFileCompressor = new BinaryFileCompressor();
        Compressor compressor = new Compressor();
        Decompressor decompressor = new Decompressor();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 1 ~~~~~~~~~~~~~~");
        String inputFile1 = "./input1.bin";
        String compressedFile1 = "./compressed1.bin";
        String decompressedFile1 = "./decompressed1.bin";
        compressor.compress(inputFile1, compressedFile1);
        decompressor.decompress(compressedFile1, decompressedFile1);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 2 ~~~~~~~~~~~~~~");
        String inputFile2 = "input2.bin";
        String compressedFile2 = "./compressed2.bin";
        String decompressedFile2 = "./decompressed2.bin";
        compressor.compress(inputFile2, compressedFile2);
        decompressor.decompress(compressedFile2, decompressedFile2);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 3 ~~~~~~~~~~~~~~");
        String inputFile3 = "input3.bin";
        String compressedFile3 = "./compressed3.bin";
        String decompressedFile3 = "./decompressed3.bin";
        compressor.compress(inputFile3, compressedFile3);
        decompressor.decompress(compressedFile3, decompressedFile3);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();

        System.out.println("~~~~~~~~~~~~~ Compressing Input 4 ~~~~~~~~~~~~~~");
        String inputFile4 = "input4.bin";
        String compressedFile4 = "./compressed4.bin";
        String decompressedFile4 = "./decompressed4.bin";
        compressor.compress(inputFile4, compressedFile4);
        decompressor.decompress(compressedFile4, decompressedFile4);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~ Done  ~~~~~~~~~~~~~~~~~~~~");
        System.out.println();
        System.out.println();
    }
}
