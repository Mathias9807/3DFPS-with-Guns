package lithium;

import java.net.UnknownHostException;

import lithium.level.*;
import static lithium.level.Level.playerList;
import carbon.CarbonClient;
import carbonserver.*;

public class Networking {
	
	public static void startServer() {
		CarbonServer.updatesPerSecond = 10;
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
		
		CarbonServer.addHandler("INFO", (header, data) -> {
			MPlayer mp = getPlayer(header);
			mp.retrieveData(data);
		});
		
		CarbonServer.main(null);
	}

	public static void startClient(String ip) {
		CarbonClient.updatesPerSecond = 15;
		CarbonClient.useSystemInputStream = false;
		CarbonClient.eventOnUpdate = () -> { updateClient(); };
		
		CarbonClient.addHandler("JOIN", (header, data) -> { clientPlayerJoined(header, data); });
		CarbonClient.addHandler("LEFT", (header, data) -> { clientPlayerLeft(header, data); });
		CarbonClient.addHandler("DSCN", (header, data) -> { Main.client.disconnect(); });
		CarbonClient.addHandler("INFO", (header, data) -> { getPlayer(header).retrieveData(data); });
		
		Main.client = CarbonClient.client = new CarbonClient(ip);
		
		Level.mainPlayer = new Player(new HeaderData("JOIN", 
				Main.client.connectedIP, Main.client.socket.getLocalPort()));
	}
	
	public static void updateServer(Client c) {
		for (int i = 0; i < playerList.size(); i++) {
			for (int j = 0; j < playerList.size(); j++) {
				if (i == j) continue;
				serverSendClientInfo("INFO", playerList.get(j), playerList.get(i).client);
			}
		}
	}
	
	public static void updateClient() {
		try {
			if (Level.mainPlayer != null) 
				CarbonClient
				.client
				.sendPacket("INFO", 
						Level.mainPlayer.getData());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	private static MPlayer serverAddClient(HeaderData header, byte[] data) {
		MPlayer mp = new MPlayer(header, data);
		for (int i = 0; i < playerList.size(); i++) 
			serverSendClientInfo("JOIN", mp, playerList.get(i).client);
		for (int i = 0; i < playerList.size(); i++) 
			serverSendClientInfo("JOIN", playerList.get(i), mp.client);
		Level.addMPlayer(mp);
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
		Level.removeMPlayer(toRemove);
	}
	
	private static void clientPlayerJoined(HeaderData header, byte[] data) {
		MPlayer join = new MPlayer(header, data);
		
		if (header.ip.getHostAddress()
				.equals(CarbonClient.client.connectedIP.getHostAddress()) 
				&& header.port == CarbonClient.client.socket.getLocalPort()) 
			return;
		
		for (int i = 0; i < playerList.size(); i++) {
			if (join.client.getIP().getHostAddress()
					.equals(playerList.get(i).client.getIP().getHostAddress()) 
					&& join.client.getPort() == playerList.get(i).client.getPort()) 
				return;
		}
		Level.addMPlayer(new MPlayer(header, data));
		System.out.println("CLIENT: Player " + header.ip + "::" + header.port + " joined");
	}
	
	private static void clientPlayerLeft(HeaderData header, byte[] data) {
		Level.removeMPlayer(getPlayer(header));
	}
	
	public static void stopClient() {
		Main.client.disconnect();
	}
	
	public static MPlayer getPlayer(HeaderData header) {
		MPlayer mp;
		for (int i = 0; i < playerList.size(); i++) {
			mp = playerList.get(i);
			if (mp.client.getIP().getHostAddress().equals(header.ip.getHostAddress()) 
					&& mp.client.getPort() == header.port) {
				return mp;
			}
		}
		return null;
	}
	
}
