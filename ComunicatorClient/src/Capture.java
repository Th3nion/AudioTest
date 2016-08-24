import com.thinktube.audio.AudioConfig;
import com.thinktube.audio.SpeexAEC;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by Thenion on 07/12/2015.
 */
public class Capture implements Runnable {

    private TargetDataLine targetLine;
    private List<SendBuffer> listeners;
    private Queue<byte[]> echo;
    public static AudioConfig audioConfig = new AudioConfig(16000, 320);
    private SpeexAEC aec;

    public Capture(TargetDataLine.Info info, AudioFormat format) {
        try {
            aec = SpeexAEC.getInstance(audioConfig);
            echo = new LinkedTransferQueue<>();
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
        while (true) {
            int numBytesRead;
            byte[] targetData = new byte[targetLine.getBufferSize() / 5];
            numBytesRead = targetLine.read(targetData, 0, targetData.length);




            short[] shortsInput = new short[targetData.length / 2];
            ByteBuffer.wrap(targetData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortsInput);

            aec.echoCapture(shortsInput);
            short[] clearedShortBuffer = aec.echoCancel(shortsInput);
            byte[] clearedBuffer = new byte[clearedShortBuffer.length * 2];
            ByteBuffer.wrap(clearedBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(clearedShortBuffer);

            for (SendBuffer sb : listeners) {

                sb.sendBuffer(clearedBuffer);
            }
        }

    }

    public void addSendBufferListener(SendBuffer sb) {
        listeners.add(sb);
    }
}
