import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A Vigenère cipher decoder tool that uses frequency analysis to display the most likely letters
 * used at each index of the Vigenère encryption key. The correct key length must be provided. 
 * Code Style: Google Style
 * 
 * <p>Future implementations will allow the user to enter the ciphertext and key length, and try 
 * keys for decryption, through the console.
 * 
 * @author Raphael Namay
 */
public class DecoderTool {

  private static String ciphertext =
      "WIEVHSMYRSMCVBPROJEFWPCQPQUVHSSEDNEHUPMLRINDUVNPHSSUFJEPFFFKFUIQQDLCVTIEWIEUKPCMZBVGUJDGUUHG"
      + "QPVGOEEUFSIDHEHQZBRGEFLERNPWWFRRUPGTDNMGUDRGDUEFDQRQJSAOFBLNHETCSFWQUNWJLDHYDTRGOFAUHEIPWP"
      + "APRNNKSPTGQUCQPQUVHSNGWXOTNVSGGCYCQBUVRDRCWJCIRWETQNEPWUOEROTTRMIVVQEQSMEVKFGQYFRPPFNVKBDV"
      + "RUUTQPFHWIEERNPWWFRPHUWQULTJXTDGVURQBJNILUSEROTTRMIPRSDGUUOGUBDKFBTGWIEYRSMDUVNPHSSDRPKKVB"
      + "BQXUAUFMOUHBSORTTXPTCQPQUVHSNGWXOTNNAPDHETVXOWOEEXHSHCYFCQPFTQDSECOSOIXFWQUNUPWJLVKFLCWFSY"
      + "RSMUZFRGRCSEXSEVKJNIVNOTHBSURDICWFDYLUHTHTECUDHKQBCQPQUVHSLCEPRCWPRAIPRGABMROFAHHXBGQFVQOF"
      + "NVZPROVXETHEEXHMORHEBAAFRQASEUHBREKFRUZIOYDOTGGUOODLEORSEGIGIELFNVXTEQIDOOSVTGUGAELMIVLFSV"
      + "KFYFHWENRQEFDUOYQDRKHSWQUNWJLDHORWEFWIRQXHHCQFTYRSKUHODKQHOWWJMRRSTCQUAPQPUPFFMGQUSVKFITGJ"
      + "AIQPSVLDWQUNANVPCQQTTCQULAZFAXHETJUPUIKUHGQFTYRSKDXUTJLT";

  /** The key length. */
  private static int keyLength = 4;

  /** The Constant ALPHABET. */
  private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * The most likely key, determined by the most frequent letter of each segment.
   */
  private static String mostLikelyKey = "";

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {

    for (int i = 0; i < keyLength; i++) {
      String segment = splitCiphertext(i);
      int letterCount = 1;

      System.out.println("Segment " + i + ": " + segment);
      System.out.println("Top 3 frequent letters:");

      // iterates through three most frequent letters of the specified segment
      for (Map.Entry<Character, Integer> pair : getMostFrequentLetters(segment)) {

        // gets key letter corresponding to most frequent letter, e.g. if most frequent
        // letter is G, offset is C, indicating a shift of 2
        char offset =
            ALPHABET.charAt(Math.abs(ALPHABET.indexOf('E') - ALPHABET.indexOf(pair.getKey())));

        System.out.println(letterCount + ". Letter: " + pair.getKey() + " | Frequency: "
            + pair.getValue() + " | Offset (Suggested letter for key index " + i + "): " + offset);

        if (letterCount == 1) {
          mostLikelyKey += offset;
        }

        letterCount++;
      }

      System.out.print("\n");
    }

    System.out.println("Most likely key: " + mostLikelyKey);
    System.out
    .println("Decryption attempt using most likely key: " + decodeCiphertext(mostLikelyKey));
  }

  /**
   * Splits the ciphertext in preparation for frequency analysis of each segment.
   *
   * @param segmentNumber the segment number
   * @return the string
   */
  public static String splitCiphertext(int segmentNumber) {
    String segment = "";

    for (int i = segmentNumber; i < ciphertext.length(); i += keyLength) {
      // adds every nth letter of the ciphertext to the current segment
      segment += ciphertext.charAt(i);
    }

    return segment;
  }

  /**
   * Gets the three most frequent letters of the specified ciphertext segment.
   *
   * @param segment the ciphertext segment
   * @return the three most frequent letters
   */
  public static List<Map.Entry<Character, Integer>> getMostFrequentLetters(String segment) {

    Map<Character, Integer> letterFrequency = new HashMap<Character, Integer>();

    for (int i = 0; i < segment.length(); i++) {

      char currentLetter = segment.charAt(i);

      // current letter has already been added to the map
      if (letterFrequency.containsKey(currentLetter)) {
        
        // increase letter frequency by 1
        letterFrequency.put(currentLetter, letterFrequency.get(currentLetter) + 1);
      } else {
        
        //add the current letter to the map for the first time
        letterFrequency.put(currentLetter, 0); 
      }
    }

    List<Map.Entry<Character, Integer>> topThree = letterFrequency.entrySet().stream()
        // sort frequencies by decreasing value
        .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder())) 
        .limit(3) // include only the three most frequent letters
        .collect(Collectors.toList());

    return topThree;
  }

  /**
   * Decodes the ciphertext using the specified key.
   *
   * @param key the decryption key
   * @return the result of the decryption attempt
   */
  public static String decodeCiphertext(String key) {
    String result = "";
    int keyIndex = 0;

    for (char c : ciphertext.toCharArray()) {
      int totalShifts = ALPHABET.indexOf(key.charAt(keyIndex));

      for (int shiftCount = 0; shiftCount < totalShifts; shiftCount++) {
        if (c == 'A') {
          c = 'Z';
        } else {
          c--; // shift to the preceding letter
        }
      }

      if (keyIndex < keyLength - 1) {
        keyIndex++;
      } else { // last letter of key has been reached
        keyIndex = 0; // go back to the first letter of the key
      }

      result += c;
    }

    return result;
  }

}
