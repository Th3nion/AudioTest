import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thenion on 07/12/2015.
 */
public class Capture implements Runnable{

    private TargetDataLine targetLine;
    private List<SendBuffer> listeners;

    public Capture(TargetDataLine.Info info, AudioFormat format){
        try {
            targetLine = (TargetDataLine) AudioSystem.getLine(info);
            targetLine.open(format);
            listeners = new ArrayList<>();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        targetLine.start();
    }


    @Override
    public void run() {
        while(true){
            int numBytesRead;
            byte[] targetData = new byte[targetLine.getBufferSize() / 5];
            numBytesRead = targetLine.read(targetData, 0, targetData.length);
            for (SendBuffer sb : listeners){
                sb.sendBuffer(targetData);
            }
        }

    }

    public void addSendBufferListener(SendBuffer sb){
        listeners.add(sb);
    }
}
