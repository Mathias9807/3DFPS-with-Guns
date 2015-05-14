package lithium.level;

import carbonserver.HeaderData;

public class Player extends MPlayer {

	public Player(HeaderData header) {
		super(header, null);
	}
	
	public void tick(double delta) {
		value++;
	}
	
	public void retrieveData(byte[] data) {
	}

}
