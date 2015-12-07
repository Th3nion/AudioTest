import java.rmi.server.UID;

/**
 * Created by Thenion on 07/12/2015.
 */
public interface TransfertMessageInterface {
    public void transfertMessage(byte[] buffer, UID id);
}
