import java.util.Map;
import java.util.TreeMap;

/** Include encode and decode methods using Huffman code
 *
 * Compression steps:
 *      Step1: Count frequencies
 *      Step2: Build encoding array and decoding trie
 *      Step3: Write decoding trie to output.huf
 *      Step4: Write codeword for each symbol to output.huf
 *
 * Decompression steps:
 *      Step1: Read in decoding trie
 *      Step2: Use codeword bits to walk down the trie, outputting symbols every time you reach a leaf
 *      
 * @author yhc
 */


public class HuffmanCode {
    /** The frequency of each character */
    Map<Character, Integer> mapFre;

    /** The mapping of each character to the HuffmanCode */
    Map<Character, String> mapCode;


    /** Encode the given English characters into a 0&1 sequence */
    public static String encode(String s) {
        String sequence = "";



        return sequence;
    }

    /** Count the frequency of each character and throw them into a HashMap */
    private void makeFreMap(String s) {
        mapFre = new TreeMap<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (mapFre.containsKey(c)) {
                mapFre.put(c, mapFre.get(c) + 1);
            } else {
                mapFre.put(c, 1);
            }
        }

    }

    /** Create the code mapping based on the frequency */
    private void makeCodeMap() {
        d
    }
}
