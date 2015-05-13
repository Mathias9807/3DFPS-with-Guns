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
		CarbonClient.addHandler("DSCN", (header, data) -> { Main.client.disconnect(); });
		
		Main.client = new CarbonClient(ip);
	}
	
	public static void updateServer(Client c) {
	}
	
	public static void updateClient() {
	}
	
	private static MPlayer serverAddClient(HeaderData header, byte[] data) {
		MPlayer mp = new MPlayer(header, data);
//		System.out.println(mp.client.getPort());
		for (int i = 0; i < playerList.size(); i++) 
			serverSendClientInfo("JOIN", mp, playerList.get(i).client);
		for (int i = 0; i < playerList.size(); i++) 
			serverSendClientInfo("JOIN", playerList.get(i), mp.client);
		playerList.add(mp);
		return mp;
	}
	
	private static void serverSendClientInfo(String label, MPlayer mp, Client target) {
		CarbonServer.sendPacket(
				mp.client, target, 
				label, mp.getData());
	}

	private static void serverRemoveClient(HeaderData header, byte[] data) {
		MPlayer toRemove = null;
		for (int i = 0; i < playerList.size(); i++) {
			MPlayer mp = playerList.get(i);
			if (mp.client.getIP().getHostAddress().equals(header.ip.getHostAddress()) 
					&& mp.client.getPort() == header.port) {
				toRemove = mp;
			}else 
				CarbonServer.sendPacket(new Client(header.ip, header.port), 
						mp.client, "LEFT", null);
		}
		playerList.remove(toRemove);
	}
	
	private static void clientPlayerJoined(HeaderData header, byte[] data) {
		Level.addMPlayer(header, data);
//		System.out.println(header.port + " joined");
	}
	
	private static void clientPlayerLeft(HeaderData header, byte[] data) {
		Level.removeMPlayer(header, data);
	}
	
	public static void stopClient() {
	}
	
}
