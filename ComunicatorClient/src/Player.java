import com.thinktube.audio.SpeexAEC;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Thenion on 07/12/2015.
 */
public class Player  implements SendBuffer{

    SourceDataLine sourceLine;
    private SpeexAEC aec;


    public Player(TargetDataLine.Info info, AudioFormat format){
        try {
            aec = SpeexAEC.getInstance(Capture.audioConfig);
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        sourceLine.start();
    }

    @Override
    public void sendBuffer(byte[] buffer) {
        short[] shortsEcho = new short[buffer.length / 2];
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortsEcho);
        aec.echoCapture(shortsEcho);
        sourceLine.write(buffer, 0, buffer.length);
    }
}
