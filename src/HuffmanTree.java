
/**
 * Binary Tree implementation for compression/decompression
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class HuffmanTree {

	private ArrayList<HuffmanNode> nodes;
	private HuffmanNode root;
	private HashMap<Character, ArrayList<Integer>> codes;
	private BinaryFile file;

	public HuffmanTree(ArrayList<HuffmanNode> nodes) {
		this.nodes = nodes;
	}

	public HuffmanTree(BinaryFile file) {
		this.file = file;
	}

	public void buildTree() {
		if (nodes.size() == 0) {
			return;
		} else if (nodes.size() == 1) {
			root = nodes.get(0);
			return;
		}

		int frequency = nodes.get(0).frequency + nodes.get(1).frequency;
		HuffmanNode innerNode = new HuffmanNode(frequency);
		innerNode.left =  nodes.get(0);
		innerNode.right = nodes.get(1);

		ArrayList<HuffmanNode> temp = new ArrayList<HuffmanNode>();
		temp.add(innerNode);

		/* Add the remaining of the nodes in */
		for (int i = 2; i < nodes.size(); i++) {
			temp.add(nodes.get(i));
		}

		Collections.sort(temp);

		nodes = temp;

		buildTree();
	}


	public void buildCodes() {
		codes = new HashMap<Character, ArrayList<Integer>>();

		if (root == null) {
			return;
		}

		if (root.left != null) {
			ArrayList<Integer> prefix = new ArrayList<Integer>();
			prefix.add(0);
			buildCodes(prefix, root.left);
		}

		if (root.right != null) {
			ArrayList<Integer> prefix = new ArrayList<Integer>();
			prefix.add(1);
			buildCodes(prefix, root.right);
		}
	}

	private void buildCodes(ArrayList<Integer> prefix, HuffmanNode root) {
		if (root.isLeaf()) {
			codes.put(root.data, prefix);
			return;
		}

		if (root.left != null) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.addAll(prefix);
			temp.add(0);
			buildCodes(temp, root.left);
		}

		if (root.right != null) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			temp.addAll(prefix);
			temp.add(1);
			buildCodes(temp, root.right);
		}
	}
	
	
	public HuffmanNode getRoot() {
		return root;
	}

	public HashMap<Character, ArrayList<Integer>> getCodes() {
		return codes;
	}
	public void printCodes() {
		for (Character c: codes.keySet()) {
			ArrayList<Integer> code = codes.get(c);
			System.out.println("Character: " + c + ", Code: " + code);
		}
	}

	public void printLeaves(HuffmanNode root) {

		if (root.isLeaf()) {
			System.out.println("Char: " + root.data + ", Frequency: " 
					+ root.frequency);
		}

		if (root.left != null) {
			printLeaves(root.left);
		}
		if (root.right != null) {
			printLeaves(root.right);
		}
	}

	public int compressionSize() {
		int size = compressionSize(root) + 48;
		int remainder = size % 8;
		size += 8 - remainder;
		return size;
	}

	private int compressionSize(HuffmanNode root) {
		int size = 1;

		if (root.isLeaf()) {
			char data = root.data;
			int codeLength = codes.get(data).size();
			size += (8 + (root.frequency * codeLength));
			return size;
		}

		if (root.left != null) {
			size += compressionSize(root.left);
		}
		if (root.right != null) {
			size += compressionSize(root.right);
		}

		return size;
	}

	/**
	 * Builds binary tree for decompression
	 */
	 public void buildDecompressionTree() {
		boolean bit = file.readBit();
		
		if (bit == true) {
			root = new HuffmanNode();
		}
	
		buildDecompressionTree(root);
	}

	private void buildDecompressionTree(HuffmanNode root) {
		boolean bit = file.readBit();
		
		if (bit == false) {
			char c = file.readChar();
			root.left = new HuffmanNode(c);
		} else {
			root.left = new HuffmanNode();
			buildDecompressionTree(root.left);
		}
		
		bit = file.readBit();
		if (bit == false) {
			char c = file.readChar();
			root.right = new HuffmanNode(c);
		} else {
			root.right = new HuffmanNode();
			buildDecompressionTree(root.right);
		}
	}

	public void printSerialTree() {
		System.out.println("Serialized Tree:");
		printSerialTree(root);
	}
	
	
	private void printSerialTree(HuffmanNode root) {
		if (root == null) { return; }

		if (root.isLeaf()) {
			System.out.print(root.data + "//");
			return;
		}

		if (root.left != null) {
			System.out.print(0);
			printSerialTree(root.left);
		}

		if (root.right != null) {
			System.out.print(1);
			printSerialTree(root.right);
		}



	}
}
