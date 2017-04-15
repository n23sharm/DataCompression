import com.sun.istack.internal.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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

        Trie trie = new Trie();
        for (int i = 0; i < data.length; ++i) {
            char c = (char) data[i];
            trie.insert(c, i);
        }

        //trie.printTrie();

        Compressor compressor = new Compressor();
        char[] compressed = compressor.getCompressed(data);
        System.out.print(compressed);

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
}
