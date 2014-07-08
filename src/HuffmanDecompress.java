
/**
 * Implementation for decompression using Huffman Coding
 * @author psv
 *
 */
public class HuffmanDecompress {

	private BinaryFile compressedFile;
	private String outputFileName;
	private boolean verbose;

	public HuffmanDecompress(String filename, String filename2, boolean verbose) {
		compressedFile = new BinaryFile(filename, 'r');
		outputFileName = filename2;
		this.verbose = verbose;
		decompress();
	}


	/**
	 * Main decompression function
	 */
	private void decompress() {
		if (!checkMagicNumber()) {
			System.out.println("Cannot uncompress this file.");
			System.exit(1);
		}

		HuffmanTree tree = new HuffmanTree(compressedFile);
		tree.buildDecompressionTree();

		if (verbose) {			
			tree.printSerialTree();
		}

		TextFile decompressedFile = new TextFile(outputFileName, 'w');
		while (!compressedFile.EndOfFile()) {
			writeDecompressed(decompressedFile, tree.getRoot());

		}
		decompressedFile.close();
		compressedFile.close();
	}


	/**
	 * Uses the HuffmanTree to find character and writes to file
	 * 
	 * @param file file to write into
	 * @param root root of the tree, used for traversal
	 */
	private void writeDecompressed(TextFile file, HuffmanNode root) {

		if (root.isLeaf()) {
			file.writeChar(root.data);
			return;
		}

		boolean bit = compressedFile.readBit();

		if (!bit) {
			writeDecompressed(file, root.left);
		} else {
			writeDecompressed(file, root.right);
		}
	}


	/**
	 * Checks for HF in the first 16 bits of file
	 * 
	 * @return true if HF is found, false otherwise
	 */
	private boolean checkMagicNumber() {
		char c;
		c = compressedFile.readChar();
		if (c != 'H') { return false; }
		c = compressedFile.readChar();
		if (c != 'F') { return false; }

		/* Magic Numbers passed */
		return true;
	}

}
