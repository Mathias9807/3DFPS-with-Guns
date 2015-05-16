package lithium.level;

import static org.lwjgl.input.Keyboard.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import carbonserver.HeaderData;

public class Player extends MPlayer {
	
	private boolean mouse1Held = false;

	public Player(HeaderData header) {
		super(header, null);
	}
	
	public void tick(double delta) {
		Vector3f dir = new Vector3f();
		if (isKeyDown(KEY_W)) dir.z--;
		if (isKeyDown(KEY_S)) dir.z++;
		if (isKeyDown(KEY_D)) dir.x++;
		if (isKeyDown(KEY_A)) dir.x--;
		float cosine = (float) Math.cos(rot.y);
		float sine = (float) Math.sin(rot.y);
		vel.z = dir.z * cosine + dir.x * sine;
		vel.x = dir.x * cosine - dir.z * sine;
		
		if (Mouse.isButtonDown(0) && !mouse1Held) {
			Mouse.setGrabbed(!Mouse.isGrabbed());
			mouse1Held = true;
		}
		if (!Mouse.isButtonDown(0)) mouse1Held = false;
		
		if (Mouse.isGrabbed()) rot.y += Mouse.getDX() / 120.0;
		if (Mouse.isGrabbed()) rot.x -= Mouse.getDY() / 120.0;
		if (rot.x > Math.PI / 2) rot.x = (float) Math.PI / 2;
		if (rot.x < -Math.PI / 2) rot.x = (float) -Math.PI / 2;
		
		super.tick(delta);
	}
	
	public void retrieveData(byte[] data) {
	}

}
