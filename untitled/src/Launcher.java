/**
 * Created by Thenion on 07/12/2015.
 */
public class Launcher {

    public static void main(String args[]) {
        Server server = new Server(40000);
        Thread t = new Thread(server, "Server");
        t.start();
    }
}
