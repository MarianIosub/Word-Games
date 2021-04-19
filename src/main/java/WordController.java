import java.io.*;
import java.util.Random;

public class WordController {
    private static String dictionaryLink = "C:\\Users\\Marian\\Documents\\GitHub\\Word-Games\\cuvinte_romana.csv";

    public static boolean verifyWordExistence(String wordToVerify) throws IOException {
        String wordReaded = null;
        boolean wordFound = false;
        BufferedReader reader = new BufferedReader(new FileReader(dictionaryLink));
        while ((wordReaded = reader.readLine()) != null) {
            if (wordToVerify.equals(wordReaded)) {
                wordFound = true;
                break;
            }
        }
        return wordFound;
    }

    public static String extractEasyWord() throws IOException {
        String wordExtracted = null;
        Random random = new Random();
        while (wordExtracted == null) {
            Integer noOfFounds = random.nextInt(173000);
            BufferedReader reader = new BufferedReader(new FileReader(dictionaryLink));
            while (noOfFounds != 0) {
                String wordReaded = reader.readLine();
                if (wordReaded.length() > 2 && wordReaded.length() < 7) {
                    wordExtracted = wordReaded;
                }
                noOfFounds--;
            }
        }
        return wordExtracted;
    }

    public static String extractMediumWord() throws IOException {
        String wordExtracted = null;
        Random random = new Random();
        while (wordExtracted == null) {
            Integer noOfFounds = random.nextInt(173000);
            BufferedReader reader = new BufferedReader(new FileReader(dictionaryLink));
            while (noOfFounds != 0) {
                String wordReaded = reader.readLine();
                if (wordReaded.length() > 6 && wordReaded.length() < 10) {
                    wordExtracted = wordReaded;
                }
                noOfFounds--;
            }
        }
        return wordExtracted;
    }

    public static String extractHardWord() throws IOException {
        String wordExtracted = null;
        Random random = new Random();
        while (wordExtracted == null) {
            Integer noOfFounds = random.nextInt(173000);
            BufferedReader reader = new BufferedReader(new FileReader(dictionaryLink));
            while (noOfFounds != 0) {
                String wordReaded = reader.readLine();
                if (wordReaded.length() > 9) {
                    wordExtracted = wordReaded;
                }
                noOfFounds--;
            }
        }
        return wordExtracted;
    }

    public static boolean verifyTheDiacritics(String testedWord1, String testedWord2) {
        testedWord1 = testedWord1.toLowerCase();
        testedWord1 = testedWord1.replace('ă', 'a');
        testedWord1 = testedWord1.replace('â', 'a');
        testedWord1 = testedWord1.replace('î', 'i');
        testedWord1 = testedWord1.replace('ț', 't');
        testedWord1 = testedWord1.replace('ș', 's');

        testedWord2 = testedWord2.toLowerCase();
        testedWord2 = testedWord2.replace('ă', 'a');
        testedWord2 = testedWord2.replace('â', 'a');
        testedWord2 = testedWord2.replace('î', 'i');
        testedWord2 = testedWord2.replace('ț', 't');
        testedWord2 = testedWord2.replace('ș', 's');

        return testedWord1.equals(testedWord2);
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
