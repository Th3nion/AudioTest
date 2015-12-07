import java.io.*;
import java.net.Socket;
import java.rmi.server.UID;

/**
 * Created by Thenion on 07/12/2015.
 */
public class Client implements Runnable{
    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;
    private TransfertMessageInterface server;
    private UID id;

    Client(Socket socket, TransfertMessageInterface server){
        this.socket = socket;
        try {
            this.bufferedOutputStream = new BufferedOutputStream( socket.getOutputStream());
            this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            this.server = server;
            id = new UID();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        byte buf[] = new byte[512];
        try {
            while (bufferedInputStream.read(buf) > 0){
                fireBuffer(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fireBuffer( byte buf[]){
        server.transfertMessage(buf,id);
    }

    public UID getId() {
        return id;
    }

    public void sendMessage(byte[] buffer){
        try {
            bufferedOutputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
