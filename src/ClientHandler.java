import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements NIOHandler {

    private SocketChannel client;
    private String message;

    public ClientHandler(SocketChannel client) {
        this.client = client;
    }

    public void execute(SelectionKey key) {
        if (client != null) {
            try {
                if (key.isReadable()) {
                    int rcode = 0;
                    ByteBuffer bb = ByteBuffer.allocate(1024);
                    rcode = client.read(bb);
                    if (rcode > 0) {
                        bb.flip();
                        CharBuffer cb = StandardCharsets.US_ASCII.decode(bb);
                        message = cb.toString().replaceAll("(\\r|\\n)", "");
                        bb.clear();
                        System.out.println("Server receive message from client " + client.socket().getRemoteSocketAddress() + ": " + message);
                        if (message.equals("quit")) {
                            client.close();
                            return;
                        }
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                }
                else if (key.isWritable()) {
                    message = message + "\n";
                    ByteBuffer bb = ByteBuffer.wrap(message.getBytes());
                    client.write(bb);
                    System.out.println("Server send message to client " + client.socket().getRemoteSocketAddress() + ": " + message);
                    bb.rewind();
                    key.interestOps(SelectionKey.OP_READ);
                }

            } catch (IOException e) {
                try {
                    client.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                key.cancel();
                e.printStackTrace();
            }
        }
    }
}
