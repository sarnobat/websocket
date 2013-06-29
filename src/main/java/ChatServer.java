import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ChatServer extends WebSocketServer {

	private static final int CHAT_SERVER_PORT = 8887; // 843 flash policy port

	public static void main(String[] args) throws InterruptedException, IOException {
		ChatServer server = new ChatServer(CHAT_SERVER_PORT);
		server.start();
		System.out.println("ChatServer started on port: " + server.getPort());
		server.addServerInputHandler();
	}

	private ChatServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("+++++SRIDHAR - onOpen() - Client just joined");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("onClose() - Client just left");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("onMessage() - Client just sent a message: " + message);
		this.sendResponseToClient("Hi, my name is ChatServer.java. Did you just say \"" + message+"\"");
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	public void sendResponseToClient(String text) {
		Collection<WebSocket> webSockets = connections();
		synchronized (webSockets) {
			for (WebSocket webSocket : webSockets) {
				webSocket.send(text);
			}
		}
	}

	private void addServerInputHandler() throws IOException {
		System.out.println("You can broadcast to all clients by typing in this console.");
		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String serverConsoleInput = sysin.readLine();
			if (serverConsoleInput.length() > 0) {
				System.out.println("addServerInputHandler() - " + serverConsoleInput);
				sendResponseToClient(serverConsoleInput);
			}
		}
	}
}
