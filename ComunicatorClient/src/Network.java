import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thenion on 07/12/2015.
 */
public class Network implements Runnable, SendBuffer {
    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedInputStream bufferedInputStream;
    private List<SendBuffer> listeners;

    public Network() {
        try {
            socket = socket = new Socket("192.168.1.26", 40000);
            listeners = new ArrayList<SendBuffer>();
            this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
            this.bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            System.out.println("NETWORK OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte buf[] = new byte[512];
        try {
            while (bufferedInputStream.read(buf) > 0) {
                fireSendBuffer(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fireSendBuffer(byte[] buffer) {
        for (SendBuffer sb : listeners) {
            sb.sendBuffer(buffer);
        }
    }

    public void addSendBufferListener(SendBuffer sb) {
        listeners.add(sb);
    }

    @Override
    public void sendBuffer(byte[] buffer) {
        try {
            bufferedOutputStream.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
