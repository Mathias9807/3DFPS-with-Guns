package lithium.level;

import java.util.*;

import carbonserver.HeaderData;

public class Level {
	
	public static Prop[] props;
	public static Player mainPlayer;
	public static List<MPlayer> playerList = new ArrayList<MPlayer>();
	
	public static void init(String levelFile) {
	}
	
	public static void tick(double delta) {
		mainPlayer.tick(delta);

		for (int i = 0; i < playerList.size(); i++) 
			playerList.get(i).tick(delta);
	}
	
	public static void tickMPlayer(HeaderData header, byte[] data) {
		for (int i = 0; i < playerList.size(); i++) {
			if (playerList.get(i).client.getIP().getAddress().equals(header.ip.getAddress()) 
					&& playerList.get(i).client.getPort() == header.port) {
				playerList.get(i).retrieveData(data);
			}
		}
	}
	
	public static void addMPlayer(MPlayer mp) {
		playerList.add(mp);
	}

	public static void removeMPlayer(MPlayer mp) {
		playerList.remove(mp);
	}
	
}
