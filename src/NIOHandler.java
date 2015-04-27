import java.nio.channels.SelectionKey;


public interface NIOHandler {
    public void execute(SelectionKey key);
}
