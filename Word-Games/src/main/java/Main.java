import pa.proj.word_games.managers.EntityFactoryManager;
import pa.proj.word_games.server.Server;

import java.io.IOException;

public class Main
{
    public static void main(String[] args)  {

        EntityFactoryManager.getInstance();
        Server server = Server.getInstance();
        try {
            server.initialize();
            server.start();
        }
        catch(Exception exception) {
            System.out.println("Exceptie la Main:");
            exception.printStackTrace();
        }
        finally {
            try {
                if(server != null)
                    server.closeServerSocket();
            }
            catch(Exception ignored) { }
        }
    }
}
