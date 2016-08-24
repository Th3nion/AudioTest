import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thenion on 07/12/2015.
 */
public class Server  implements Runnable, TransfertMessageInterface{

    private ServerSocket serverSocket;
    private List<Client> clients;

    Server(int portNumber) {
        try {
            serverSocket = new ServerSocket(portNumber);
            clients = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void run() {
        System.out.println("Server runing on : " + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());
        while(true){
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Client Connected : " + clientSocket.getInetAddress());
                synchronized (clients){
                    Client c = new Client(clientSocket,this);
                    clients.add(c);
                    Thread t = new Thread(c, "Client " + c.getId());
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void transfertMessage(byte[] buffer, UID id) {
        for(Client c : clients){
            if(!c.getId().equals(id)){
                c.sendMessage(buffer);
            }
        }
    }
}
