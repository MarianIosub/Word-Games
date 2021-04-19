package pa.proj.word_games.csv;

import java.io.*;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;

public class WordController {
    private static String dictionaryLink = System.getProperty("user.dir") + "\\data\\cuvinte_romana.csv";

    /**
     * Extracts a random word from the CSV file.
     * @return The extracted word.
     * @throws IOException
     */
    private static String extractWord() throws IOException {
        String extractedWord = null;
        Random random = new Random();
        int numberOfFounds = random.nextInt(173000);
        BufferedReader reader = new BufferedReader(new FileReader(dictionaryLink));
        while (numberOfFounds != 0) {
            extractedWord = reader.readLine();
            numberOfFounds--;
        }
        return extractedWord;
    }


    /**
     * Verifies if a word appears in the CSV file.
     * @param word The word that will be verified.
     * @return true, if the given word appears in the CSV file; false, if not
     * @throws IOException
     */
    public static boolean verifyWordExistence(String word) throws IOException {
        String readedWord = null;
        boolean found = false;
        BufferedReader reader = new BufferedReader(new FileReader(dictionaryLink));
        while ((readedWord = reader.readLine()) != null) {
            if (word.equals(readedWord)) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Extracts a random word from the CSV file with the length between 2 and 6.
     * @return The extracted word.
     * @throws IOException
     */
    public static String extractEasyWord() throws IOException {
        String extractedWord = null;
        while (extractedWord == null) {
            extractedWord = extractWord();
            if(extractedWord.length() < 3 || extractedWord.length() > 6)
                extractedWord = null;
        }
        return extractedWord;
    }

    /**
     * Extracts a random word from the CSV file with the length between 6 and 10.
     * @return The extracted word.
     * @throws IOException
     */
    public static String extractMediumWord() throws IOException {
        String extractedWord = null;
        while (extractedWord == null) {
            extractedWord = extractWord();
            if(extractedWord.length() < 7 || extractedWord.length() > 9)
                extractedWord = null;
        }
        return extractedWord;
    }

    /**
     * Extracts a random word from the CSV file with the length higher than 9.
     * @return The extracted word.
     * @throws IOException
     */
    public static String extractHardWord() throws IOException {
        String extractedWord = null;
        while (extractedWord == null) {
            extractedWord = extractWord();
            if(extractedWord.length() < 10)
                extractedWord = null;
        }
        return extractedWord;
    }

    /**
     * Checks if two words are equals, even if one or both of them contains diacritics.
     * @param word1 The first word.
     * @param word2 The second word.
     * @return true, if the words are equal; false, if not
     */
    public static boolean verifyTheDiacritics(String word1, String word2) {
        // Getting rid of diacritics
        word1 = Normalizer.normalize(word1, Normalizer.Form.NFD);
        word1 = word1.replaceAll("\\p{M}", "");
        word1 = word1.toLowerCase(Locale.ROOT);

        word2 = Normalizer.normalize(word2, Normalizer.Form.NFD);
        word2 = word2.replaceAll("\\p{M}", "");
        word2 = word2.toLowerCase(Locale.ROOT);

        return word1.equals(word2);
    }

    public static boolean fazanRule(String previousWord, String nextWord) {
        previousWord=previousWord.toLowerCase();
        nextWord=nextWord.toLowerCase();
        if ((previousWord.charAt(previousWord.length() - 2) == nextWord.charAt(0)) && (previousWord.charAt(previousWord.length() - 1) == nextWord.charAt(1))) {
            return true;
        } else {
            return false;
        }
    }
}
