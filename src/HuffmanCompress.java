
/**
 * Implementation for text file compression using Huffman Coding
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class HuffmanCompress {

	TextFile originalFile;
	String 	 outputFileName;
	boolean  force;
	boolean  verbose;


	/**
	 * Creates new TextFile object set to read in characters.
	 * 
	 * @param filename file path to read in
	 * @param filename2 
	 * @param verbose 
	 */
	public HuffmanCompress(String filename, boolean force, String filename2, boolean verbose) {
		originalFile = new TextFile(filename, 'r');
		outputFileName = filename2;
		this.verbose = verbose;
		this.force = force;
		
		compress();
	}


	/**
	 * Main compression function
	 */
	private void compress() {
		ArrayList<HuffmanNode> nodeArray = buildFrequencyArray();
		int originalFileSize = calculateFileSize(nodeArray);
		
		if (verbose) {
			for (HuffmanNode n: nodeArray) {
				System.out.println("Character: " + n.data + 
						", Frequency: " + n.frequency);
			}			
		}

		HuffmanTree tree = new HuffmanTree(nodeArray);
		tree.buildTree();
		tree.buildCodes();

		int compressedFileSize = tree.compressionSize();

		if (!force) {
			if (compressedFileSize > originalFileSize) {					
				System.out.println("Cannot be compressed.");
				System.exit(0);
			}
		}
		writeCompressedFile(tree);
	}


	/**
	 * Calculates the file size of the original file
	 * 
	 * @param  nodeArray array of HuffmanNodes
	 * @return bit size of file
	 */
	private int calculateFileSize(ArrayList<HuffmanNode> nodeArray) {
		int size = 0;
		for (HuffmanNode n: nodeArray) {
			size += (n.frequency * 8);
		}
		return size;
	}


	/**
	 * Builds an array list of tuples that stores char data and frequency
	 */
	private ArrayList<HuffmanNode> buildFrequencyArray() {
		ArrayList<HuffmanNode> nodeArray = new ArrayList<HuffmanNode>();
	
		while (!originalFile.EndOfFile()) {
			char c = originalFile.readChar();
			boolean added = false;
	
			if (nodeArray.isEmpty()) {
				nodeArray.add(new HuffmanNode(c));
				continue;
			} 
	
			for (HuffmanNode t: nodeArray) {
				if (t.data == c) {
					t.increment();
					added = true;
					break;
				}
			}
	
			if (!added) {
				nodeArray.add(new HuffmanNode(c));			
			}
		}
	
		Collections.sort(nodeArray);
	
		return nodeArray;
	}


	/**
	 * Writes binary file using tree codes
	 * @param tree tree to use to write file
	 */
	private void writeCompressedFile(HuffmanTree tree) {
		BinaryFile compressedFile = new BinaryFile(outputFileName, 'w');
		compressedFile.writeChar('H');
		compressedFile.writeChar('F');

		/* Write serialized Huffman Tree*/
		writeSerialTree(compressedFile, tree.getRoot());
		
		if (verbose) {
			tree.printSerialTree();
			System.out.println();
			
			System.out.println("\nCharacter codes:");
			tree.printCodes();			
		}

		/* Write file */
		HashMap<Character, ArrayList<Integer>> codes = tree.getCodes();

		originalFile.rewind();

		while (!originalFile.EndOfFile()) {
			char c = originalFile.readChar();
			ArrayList<Integer> code = codes.get(c);
			System.out.println(code);

			for (Integer i: code) {
				if (i.intValue() == 0) {
					compressedFile.writeBit(false);
				} else {
					compressedFile.writeBit(true);
				}
			}
		}
		compressedFile.close();
		originalFile.close();
	}


	/**
	 * Serializes the tree and writes it into a file
	 * 
	 * @param file file to write the tree into
	 * @param root root of the tree
	 */
	private void writeSerialTree(BinaryFile file, HuffmanNode root) {
		if (root == null) { return; }

		if (root.isLeaf()) {
			file.writeBit(false);
			file.writeChar(root.data);
			return;
		}
		
		file.writeBit(true);

		if (root.left != null) {
			writeSerialTree(file, root.left);
		}

		if (root.right != null) {
			writeSerialTree(file, root.right);
		}



	}
}
