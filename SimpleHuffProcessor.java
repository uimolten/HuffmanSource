/*  Student information for assignment:
 *
 *  On <MY|OUR> honor, <NAME1> and <NAME2), this programming assignment is <MY|OUR> own work
 *  and <I|WE> have not provided this code to any other student.
 *
 *  Number of slip days used:
 *
 *  Student 1 (Student whose Canvas account is being used)
 *  UTEID:
 *  email address:
 *  Grader name:
 *
 *  Student 2
 *  UTEID:
 *  email address:
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class SimpleHuffProcessor implements IHuffProcessor {

    private IHuffViewer myViewer;
    private Map<Integer, String> chunkMap;
    private TreeNode root;
    private int[] freqs;
    private int savedBits;
    private int headerFormat;

    /**
     * Preprocess data so that compression is possible ---
     * count characters/create tree/store state so that
     * a subsequent call to compress will work. The InputStream
     * is <em>not</em> a BitInputStream, so wrap it int one as needed.
     * 
     * @param in           is the stream which could be subsequently compressed
     * @param headerFormat a constant from IHuffProcessor that determines what kind
     *                     of
     *                     header to use, standard count format, standard tree
     *                     format, or
     *                     possibly some format added in the future.
     * @return number of bits saved by compression or some other measure
     *         Note, to determine the number of
     *         bits saved, the number of bits written includes
     *         ALL bits that will be written including the
     *         magic number, the header format number, the header to
     *         reproduce the tree, AND the actual data.
     * @throws IOException if an error occurs while reading from the input file.
     */
    public int preprocessCompress(InputStream in, int headerFormat) throws IOException {
        this.headerFormat = headerFormat;

        BitInputStream bits = new BitInputStream(in);
        int totalBits = 0;
        freqs = new int[256];
        int currByte = bits.readBits(BITS_PER_WORD);

        while (currByte != -1) {
            freqs[currByte]++;
            totalBits += BITS_PER_WORD;
            currByte = bits.readBits(BITS_PER_WORD);
        }

        HuffPriorityQ pq = new HuffPriorityQ();

        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] != 0) {
                System.out.println("FREQ FOR " + i + " :: " + freqs[i]);
                pq.enqueue(new TreeNode(i, freqs[i]));
            }
        }
        pq.enqueue(new TreeNode(PSEUDO_EOF, 1));

        huffTree(pq);
        root = pq.peek();

        chunkMap = new HashMap<>();

        createMap(chunkMap, "", pq.dequeue());
        // Magic number and header-format identifier.
        int compressedBits = 2 * BITS_PER_INT;

        if (headerFormat == STORE_COUNTS) {
            compressedBits += ALPH_SIZE * BITS_PER_INT;
        } else if (headerFormat == STORE_TREE) {
            compressedBits += BITS_PER_INT + treeSize(root);
        } else {
            throw new IOException("Unknown header format.");
        }
        for (int i = 0; i < freqs.length; i++) {
            if (freqs[i] > 0) {
                compressedBits += freqs[i] * chunkMap.get(i).length();
                showString("Value: " + i + " | Frequency: " + freqs[i]
                        + " | Code: " + chunkMap.get(i));
            }
        }
        showString("PSEUDO_EOF: " + PSEUDO_EOF
                + " | Frequency: 1"
                + " | Code: " + chunkMap.get(PSEUDO_EOF));

        compressedBits += chunkMap.get(PSEUDO_EOF).length();

        savedBits = totalBits - compressedBits;
        return savedBits;
    }

    private void huffTree(HuffPriorityQ pq) {
        while (pq.size() >= 2) {
            TreeNode left = pq.dequeue();
            TreeNode right = pq.dequeue();
            int freq = left.getFrequency() + right.getFrequency();
            TreeNode temp = new TreeNode(left, freq, right);
            pq.enqueue(temp);
        }
    }

    private void createMap(Map<Integer, String> chunkMap, String code, TreeNode n) {
        if (n.isLeaf()) {
            chunkMap.put(n.getValue(), code);
            return;
        }
        createMap(chunkMap, code + "0", n.getLeft());
        createMap(chunkMap, code + "1", n.getRight());

    }

    /**
     * Compresses input to output, where the same InputStream has
     * previously been pre-processed via <code>preprocessCompress</code>
     * storing state used by this call.
     * <br>
     * pre: <code>preprocessCompress</code> must be called before this method
     * 
     * @param in    is the stream being compressed (NOT a BitInputStream)
     * @param out   is bound to a file/stream to which bits are written
     *              for the compressed file (not a BitOutputStream)
     * @param force if this is true create the output file even if it is larger than
     *              the input file.
     *              If this is false do not create the output file if it is larger
     *              than the input file.
     * @return the number of bits written.
     * @throws IOException if an error occurs while reading from the input file or
     *                     writing to the output file.
     */
    public int compress(InputStream in, OutputStream out, boolean force) throws IOException {

        if (savedBits < 0 && force == false) {
            return 0;
        }
        BitInputStream bits = new BitInputStream(in);
        BitOutputStream encode = new BitOutputStream(out);
        int compressCount = 0;

        // 1. magic number

        encode.writeBits(BITS_PER_INT, IHuffConstants.MAGIC_NUMBER);
        compressCount += BITS_PER_INT;

        // 2. store header format
        encode.writeBits(BITS_PER_INT, headerFormat);
        compressCount += BITS_PER_INT;

        // 3. information to recreate HuffTree

        if (headerFormat == STORE_COUNTS) {
            for (int k = 0; k < IHuffConstants.ALPH_SIZE; k++) {
                encode.writeBits(BITS_PER_INT, freqs[k]);
                compressCount += BITS_PER_INT;
            }
        } else if (headerFormat == STORE_TREE) {
            int size = treeSize(root);

            encode.writeBits(BITS_PER_INT, size);
            treeBuilder(root, encode);

            compressCount += BITS_PER_INT + size;
        }

        // 4. compressed data

        int currByte = bits.readBits(BITS_PER_WORD);
        while (currByte != -1) {
            String curr = chunkMap.get(currByte);

            for (int i = 0; i < curr.length(); i++) {
                encode.writeBits(1, curr.charAt(i) - '0');
                compressCount++;
            }
            currByte = bits.readBits(BITS_PER_WORD);
        }

        // 5. PSEUDO_EOF constant
        String EOF = chunkMap.get(PSEUDO_EOF);
        for (char c : EOF.toCharArray()) {
            encode.writeBits(1, c - '0');
            compressCount++;
        }

        encode.flush();
        showString("Compressed bits written: " + compressCount);
        return compressCount;
    }

    private int treeSize(TreeNode n) {
        if (n.isLeaf()) {
            return 1 + (BITS_PER_WORD + 1);
        }

        return 1 + treeSize(n.getLeft()) + treeSize(n.getRight());
    }

    private void treeBuilder(TreeNode n, BitOutputStream out) {
        if (n.isLeaf()) {
            out.writeBits(1, 1);
            out.writeBits(BITS_PER_WORD + 1, n.getValue());
        } else {
            out.writeBits(1, 0);
            treeBuilder(n.getLeft(), out);
            treeBuilder(n.getRight(), out);
        }

    }

    /**
     * Uncompress a previously compressed stream in, writing the
     * uncompressed bits/data to out.
     * 
     * @param in  is the previously compressed data (not a BitInputStream)
     * @param out is the uncompressed file/stream
     * @return the number of bits written to the uncompressed file/stream
     * @throws IOException if an error occurs while reading from the input file or
     *                     writing to the output file.
     */
    public int uncompress(InputStream in, OutputStream out) throws IOException {

        BitInputStream bits = new BitInputStream(in);

        int magic = bits.readBits(BITS_PER_INT);
        if (magic != MAGIC_NUMBER) {
            myViewer.showError("Error reading compressed file. \n" +
                    "File did not start with the huff magic number.");
            throw new IOException("Magic number doesnt match");
        }

        int store = bits.readBits(BITS_PER_INT);

        if (store == STORE_COUNTS) {
            HuffPriorityQ pq = new HuffPriorityQ();

            for (int i = 0; i < ALPH_SIZE; i++) {
                int count = bits.readBits(BITS_PER_INT);
                showString(count + " ");
                if (count < 0) {
                    throw new IOException("Missing or invalid frequency count.");
                }

                if (count > 0) {
                    pq.enqueue(new TreeNode(i, count));
                }
            }
            pq.enqueue(new TreeNode(PSEUDO_EOF, 1));

            huffTree(pq);
            root = pq.dequeue();
        } else if (store == STORE_TREE) {
            // STF
            int size = bits.readBits(BITS_PER_INT);
            root = uncompressBuilder(bits);

        }

        BitOutputStream decode = new BitOutputStream(out);
        decode.flush();
        return uncompressHelper(root, bits, decode);

    }

    private int uncompressHelper(TreeNode n, BitInputStream in, BitOutputStream decode) throws IOException {

        int bitsWritten = 0;
        while (n.getValue() != PSEUDO_EOF) {
            if (n.isLeaf()) {
                decode.writeBits(BITS_PER_WORD, n.getValue());
                bitsWritten += BITS_PER_WORD;
                n = root;
            } else {
                int dir = in.readBits(1);
                if (dir == 0) {
                    n = n.getLeft();
                } else {
                    n = n.getRight();
                }
            }
        }
        return bitsWritten;

    }

    private TreeNode uncompressBuilder(BitInputStream in) throws IOException {
        int cur = in.readBits(1);
        if (cur == -1) {
            throw new IOException("error in reading compressed file");
        }
        if (cur == 1) {
            int val = in.readBits(BITS_PER_WORD + 1);
            showString("decoded value written:" + val);
            return new TreeNode(val, 0);
        } else {
            TreeNode left = uncompressBuilder(in);
            TreeNode right = uncompressBuilder(in);
            return new TreeNode(left, 0, right);
        }
    }

    public void setViewer(IHuffViewer viewer) {
        myViewer = viewer;
    }

    private void showString(String s) {
        if (myViewer != null) {
            myViewer.update(s);
        }
    }
}
