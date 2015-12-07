import javax.sound.sampled.*;

/**
 * Created by Thenion on 07/12/2015.
 */
public class Player  implements SendBuffer{

    SourceDataLine sourceLine;

    public Player(TargetDataLine.Info info, AudioFormat format){
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(format);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        sourceLine.start();
    }

    @Override
    public void sendBuffer(byte[] buffer) {
        sourceLine.write(buffer, 0, buffer.length);
    }
}
