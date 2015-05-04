package lithium.level;

import java.util.*;

import carbonserver.HeaderData;

public class Level {
	
	public static Prop[] props;
	public static Player mainPlayer;
	public static List<MPlayer> otherPlayers = new ArrayList<MPlayer>();
	
	public static void init(String levelFile) {
	}
	
	public static void tick(double delta) {
	}
	
	public static void addMPlayer(HeaderData header, byte[] data) {
		MPlayer mp = new MPlayer(header, data);
		otherPlayers.add(mp);
	}

	public static void removeMPlayer(HeaderData header, byte[] data) {
		for (int i = 0; i < otherPlayers.size(); i++) {
			if (otherPlayers.get(i).clientAddress.getAddress().equals(header.ip.getAddress()) 
					&& otherPlayers.get(i).clientPort == header.port) {
				otherPlayers.remove(i);
				
				break;
			}
		}
	}
	
}
