# DataCompression

This program is built to compress and decompress binary files. 

Compression is done by identifying and replacing repeated sequences. To keep track of encountered sequences, a cache is built while traversing the data of the input file. If a character from the data is found in the cache, subsequent characters are checked to determine whether a sequence of size 3 or greater can be built from elements in the cache. The cache itself stores the characters and all the possible links (i.e connected characters) to the character. Due to encoding restrictions, only sequences that are 65535 bytes away or less are considered (since 16-bits are allocated for such offset).

The Compressor reads in the input data and converts the data into a String of the encoded binary representation of the read bytes, writing the bytes to a compressed binay file. The Decompressor reads in the data outputted by the Compressor bit by bit and depending on either '0' or '1' bit, reads the next 8 or 22 bits respectively. The 8 bits represent a character and the 22 bits represent the offset and length. A lookbackBuffer is used to keep track of characters seen so far and is used to determine the character to write based on the current offset and length. Since the offset can be a max size of 65636 (representing 16 bits), the lookbackBuffer is trimmed when it reaches that size for memory efficiency.

There are 4 binary input files provided - input1.bin, input2.bin, input3.bin, input4.bin. The inputs range in size, and content. Running the code will compress and decompress all four files. This will also create corresponding compressed and decompressed binary files. The compressed binary files should be of smaller size than their input counterparts. 
