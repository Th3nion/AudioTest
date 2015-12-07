import javax.sound.sampled.*;

/**
 * Created by Thenion on 07/12/2015.
 */
public class Launcher {
    public static void main(String args[]){
        Network network = new Network();

        TargetDataLine line;
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

        try {
            Capture capture = new Capture(targetInfo, format);
            Player player = new Player(sourceInfo, format);
            Thread captureThread = new Thread(capture, "Capture");
            Thread networkThread = new Thread(network, "Network");

            captureThread.start();
            networkThread.start();

            network.addSendBufferListener(player);
            capture.addSendBufferListener(network);

        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
