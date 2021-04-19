import pa.proj.word_games.csv.WordController;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            System.out.println(WordController.verifyWordExistence("rdafasfa")?"Cuvantul a fost gasit!":"Cuvantul nu a fost gasit!");
            System.out.println(WordController.extractEasyWord());
            System.out.println(WordController.extractMediumWord());
            System.out.println(WordController.extractHardWord());
            System.out.println(WordController.verifyTheDiacritics("Marioara","Mărioara"));
            System.out.println(WordController.fazanRule("Mirela","lautari"));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
