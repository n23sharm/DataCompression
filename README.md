# DataCompression

This program is built to compress and decompress binary files. 

Compression is done by identifying and replacing repeated sequences. To keep track of encountered sequences, a cache is built while traversing the data of the input file. If a character from the data is found in the cache, subsequent characters are checked to determine whether a sequence of size 3 or greater can be built from elements in the cache. The cache itself stores the characters and all the possible links (i.e connected characters) to the character. Due to encoding restrictions, only sequences that are 65535 bytes away or less are considered (since 16-bits are allocated for such offset).

The Compressor reads in the input data and converts the data into a String of the encoded binary representation of the read bytes. The Decompressor reads in the data outputted by the Compressor and re-builds that same binary representation String. That String is then parsed back into the original input data by iterating over the String, checking for 0 or 1 bits and determining the character to be written based on the encoding rules. 

One optimization that can be made is reading and writing directly to files instead of saving the String binary represenation in memory. That would avoid the need of large contiguous block of memory and reducing the risk of Out of Memory errors on large input data.

There are 4 binary input files provided - input1.bin, input2.bin, input3.bin, input4.bin. The inputs range in size, and content. Running the code will compress and decompress all four files. This will also create corresponding compressed and decompressed binary files. The compressed binary files should be of smaller size than their input counterparts. 
