import pa.proj.word_games.controllers.WordController;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            try
            {
                System.out.println(WordController.verifyWordExistence("rdafasfa")?"Cuvantul a fost gasit!":"Cuvantul nu a fost gasit!");
                System.out.println(WordController.extractEasyWord().getText());
                System.out.println(WordController.extractMediumWord().getText());
                System.out.println(WordController.extractHardWord().getText());
                System.out.println(WordController.compareWords("Marioara","Mărioara"));
                System.out.println(WordController.fazanRule("Mirela","lautari"));
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
