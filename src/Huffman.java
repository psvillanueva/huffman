
public class Huffman {

	public Huffman() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		boolean compress = false;
		boolean uncompress = false;
		boolean force = false;
		boolean verbose = false;
		String file1 = null;
		String file2 = null;

		/* Does not account for bad input and misnamed flags!!! */
		for (String s: args) {
			if (s.equals("-c")) {
				compress = true;				
			} else if (s.equals("-u")) {
				uncompress = true;
			} else if (s.equals("-v")) {
				verbose = true;
			} else if (s.equals("-f")) {
				force = true;
			} else if (file1 == null) {
				file1 = s;
			} else {
				file2 = s;
			}
		}

		if (compress) {
			if (file1 == null) {
				System.out.println("No input file entered.");
				System.exit(1);
			}
			HuffmanCompress hfc = new HuffmanCompress(file1, force, file2, verbose);			
		}

		if (uncompress) {
			HuffmanDecompress hfu = new HuffmanDecompress(file1, file2, verbose);
		}

	}

}
