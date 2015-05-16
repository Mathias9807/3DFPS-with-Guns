package lithium.level;

import java.util.Arrays;

import org.lwjgl.util.vector.Vector3f;

import static carbon.CarbonClient.addArrays;
import carbonserver.*;

public class MPlayer {
	
	public Client		client;
	public int			modelIndex;
	
	public Vector3f 	pos, vel, rot;
	
	public MPlayer(HeaderData header, byte[] data) {
		client			= new Client(header.ip, header.port);
		modelIndex 		= 0;
		pos 			= new Vector3f();
		vel 			= new Vector3f();
		rot 			= new Vector3f();
		retrieveData(data);
	}
	
	public void tick(double delta) {
		pos.x += vel.x * delta;
		pos.y += vel.y * delta;
		pos.z += vel.z * delta;
	}
	
	public byte[] getData() {
		byte[] sum;
		sum = addArrays(getByteArray(pos.x), getByteArray(pos.y));
		sum = addArrays(sum, getByteArray(pos.z));
		sum = addArrays(sum, getByteArray(rot.x));
		sum = addArrays(sum, getByteArray(rot.y));
		sum = addArrays(sum, getByteArray(rot.z));
		sum = addArrays(sum, getByteArray(vel.x));
		sum = addArrays(sum, getByteArray(vel.y));
		sum = addArrays(sum, getByteArray(vel.z));
		return sum;
	}
	
	public void retrieveData(byte[] data) {
		if (data == null) return;
		pos.x = getFloat(Arrays.copyOfRange(data, 0, 4));
		pos.y = getFloat(Arrays.copyOfRange(data, 4, 8));
		pos.z = getFloat(Arrays.copyOfRange(data, 8, 12));
		rot.x = getFloat(Arrays.copyOfRange(data, 12, 16));
		rot.y = getFloat(Arrays.copyOfRange(data, 16, 20));
		rot.z = getFloat(Arrays.copyOfRange(data, 20, 24));
		vel.x = getFloat(Arrays.copyOfRange(data, 24, 28));
		vel.y = getFloat(Arrays.copyOfRange(data, 28, 32));
		vel.z = getFloat(Arrays.copyOfRange(data, 32, 36));
	}
	
	public static byte[] getByteArray(float f) {
		return java.nio.ByteBuffer.allocate(Float.BYTES).putFloat(f).array();
	}
	
	public static float getFloat(byte[] b) {
		return java.nio.ByteBuffer.allocate(Float.BYTES).put(b).getFloat(0);
	}
	
}
