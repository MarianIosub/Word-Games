import pa.proj.word_games.games.FazanGame;
import pa.proj.word_games.games.HangMan;
import pa.proj.word_games.games.Player;
import pa.proj.word_games.managers.EntityFactoryManager;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        //Hang-Man
        /*HangMan hangMangame=new HangMan();
        hangMangame.addPlayer(new Player("Mirel"));
        hangMangame.hangManGame();*/

        //FAZAN
        /*try
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
        }*/
    }
}
