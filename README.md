# DataCompression

This program is built to compress and decompress binary files. 

Compression is done by identifying and replacing repeated sequences. To keep track of encountered sequences, a cache is built while traversing the data of the input file. If a character from the data is found in the cache, subsequent characters are checked to determine whether a sequence of size 3 or greater can be built from elements in the cache. The cache itself stores the characters and all the possible links (i.e connected characters) to the character. Due to encoding restrictions, only sequences that are 65535 bytes away or less are considered (since 16-bits are allocated for such offset).
