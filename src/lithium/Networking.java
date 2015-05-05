package lithium;

import java.util.ArrayList;

import lithium.level.*;
import carbon.CarbonClient;
import carbonserver.*;

public class Networking {
	
	private static ArrayList<MPlayer> playerList;
	
	public static void startServer() {
		CarbonServer.updatesPerSecond = 1;
		CarbonServer.useSystemInputStream = true;
		CarbonServer.eventOnUpdate = (c) -> { updateServer(c); };
		
		CarbonServer.addHandler("CONN", (header, data) -> {
			CarbonServer.handleConnectionRequest(header);
			serverAddClient(header, data);
		});
		
		CarbonServer.addHandler("DSCN", (header, data) -> {
			serverRemoveClient(header, data);
			CarbonServer.handleDisconnect(header);
		});
		
		playerList = new ArrayList<>();
		
		CarbonServer.main(null);
	}

	public static void startClient(String ip) {
		CarbonClient.updatesPerSecond = 1;
		CarbonClient.useSystemInputStream = false;
		CarbonClient.eventOnUpdate = () -> { updateClient(); };
		
		CarbonClient.addHandler("JOIN", (header, data) -> { clientPlayerJoined(header, data); });
		CarbonClient.addHandler("LEFT", (header, data) -> { clientPlayerLeft(header, data); });
		
		Main.client = new CarbonClient(ip);
	}
	
	public static void updateServer(Client c) {
	}
	
	public static void updateClient() {
	}
	
	private static void serverAddClient(HeaderData header, byte[] data) {
		MPlayer mp = new MPlayer(header, data);
		playerList.add(mp);
		serverSendClientInfo("JOIN", mp);
	}
	
	private static void serverSendClientInfo(String label, MPlayer mp) {
		CarbonServer.sendPacket(
				new Client(mp.clientAddress, mp.clientPort), 
				label, mp.getData());
	}

	private static void serverRemoveClient(HeaderData header, byte[] data) {
		for (int i = 0; i < playerList.size(); i++) {
			MPlayer mp = playerList.get(i);
			if (mp.clientAddress.getHostAddress().equals(header.ip.getHostAddress()) 
					&& mp.clientPort == header.port) {
				serverSendClientInfo("LEFT", mp);
				playerList.remove(i);
				break;
			}
		}
	}
	
	private static void clientPlayerJoined(HeaderData header, byte[] data) {
		Level.addMPlayer(header, data);
	}
	
	private static void clientPlayerLeft(HeaderData header, byte[] data) {
		Level.removeMPlayer(header, data);
	}
	
	public static void stopClient() {
	}
	
}
