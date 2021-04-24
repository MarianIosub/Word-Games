import pa.proj.word_games.games.FazanGame;
import pa.proj.word_games.games.Player;
import pa.proj.word_games.managers.EntityFactoryManager;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            FazanGame fazanGame = new FazanGame();
            fazanGame.addPlayer(new Player("Player1"));
            fazanGame.addPlayer(new Player("Player2"));
            fazanGame.startGame();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        finally
        {
            if(EntityFactoryManager.getInstanceWithoutInitialization() != null)
                EntityFactoryManager.getInstance().close();
        }
    }
}
