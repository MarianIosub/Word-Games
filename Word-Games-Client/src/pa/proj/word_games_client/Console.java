package pa.proj.word_games_client;

import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Console {
    public static void runConsole() {
        try {
            Client client = new Client();

            Scanner scanner = new Scanner(System.in);
            String message;
            int serverWaitsForResponse;
            String request;

            while (true) {
                // Afisez mesajul primit de la server
                message = client.readServerMessage();
                while (message == null) {
                    TimeUnit.SECONDS.sleep(1);
                    message = client.readServerMessage();
                }

                serverWaitsForResponse = Integer.parseInt(message.split(":::")[0]);
                message = message.split(":::")[1];
                if (!message.equals("dummy")) {
                    System.out.println(message);

                    if(message.equals("La revedere!"))
                        return;
                }

                if (serverWaitsForResponse == 1) {
                    // Citesc raspunsul de la tastatura si il trimit
                    System.out.print(" >> ");
                    request = scanner.nextLine();
                    client.sendRequest(request);
                } else {
                    client.sendRequest("OK");
                }
            }
        }
        catch(SocketException socketException) {
            System.out.println("Conexiunea cu server-ul s-a intrerupt brusc!");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}