package lithium.level;

import static org.lwjgl.input.Keyboard.*;

import carbonserver.HeaderData;

public class Player extends MPlayer {

	public Player(HeaderData header) {
		super(header, null);
	}
	
	public void tick(double delta) {
		if (isKeyDown(KEY_W)) pos.z -= delta;
		if (isKeyDown(KEY_S)) pos.z += delta;
		if (isKeyDown(KEY_D)) pos.x += delta;
		if (isKeyDown(KEY_A)) pos.x -= delta;
	}
	
	public void retrieveData(byte[] data) {
	}

}
