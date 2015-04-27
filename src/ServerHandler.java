import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class ServerHandler implements NIOHandler {
    private ServerSocketChannel serverSocket;
    private Selector selector;
    
    public ServerHandler(ServerSocketChannel serverSocket, Selector selector) {
        this.serverSocket = serverSocket;
        this.selector = selector;
    }
    
    public void execute(SelectionKey key) {
        if (key.isAcceptable()) {
            try {
                SocketChannel client = serverSocket.accept();
                if (client != null) {
                    client.configureBlocking(false);
                    SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
                    clientKey.attach(new ClientHandler(client));
                    System.out.println("client: " + client.socket().getRemoteSocketAddress() + " connected.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
