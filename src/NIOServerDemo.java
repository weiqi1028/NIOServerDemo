import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;


public class NIOServerDemo implements Runnable {
    
    public void run() {
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.configureBlocking(false);
            
            String ipAddress = "localhost";
            int port = 1987;
            InetSocketAddress addr = new InetSocketAddress(ipAddress, port);
            serverSocket.bind(addr);
            
            SelectionKey key = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            key.attach(new ServerHandler(serverSocket, selector));
            
            while (!Thread.interrupted()) {
                int n = selector.select();
                if (n == 0)
                    continue;
                Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                while (iter.hasNext()) {
                    SelectionKey k = iter.next();
                    iter.remove();
                    NIOHandler handler = (NIOHandler)k.attachment();
                    handler.execute(k);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        new Thread(new NIOServerDemo()).start();
    }
}
