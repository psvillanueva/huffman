
/**
 * Implementation for a perfect binary tree node for use with Huffman Coding
 * @author psv
 *
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
	char data;
	int frequency;
	HuffmanNode left;
	HuffmanNode right;

	public HuffmanNode(char data) {
		this.data = data;
		this.frequency = 1;
	}
	
	public HuffmanNode(int frequency) {
		this.frequency = frequency;
	}
	
	public HuffmanNode() {}
	
	@Override
	public int compareTo(HuffmanNode o) {
		if (frequency > o.frequency) {
			return 1;
		} else if (frequency < o.frequency) {
			return -1;
		}
		return 0;
	}

	
	/**
	 * Increment frequency by 1
	 */
	public void increment() { frequency++; }
	
	
	/**
	 * Determines if current node is a leaf
	 * @return true if node is leaf
	 */
	public boolean isLeaf() {
		if (left == null && right == null) {
			return true;
		}
		return false;
	}
}
