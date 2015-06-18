package lithium.level;

import static carbon.CarbonClient.addArrays;
import lithium.Main;

import org.lwjgl.util.vector.Vector3f;

import carbonserver.*;

public class MPlayer {
	
	public Client		client;
	public int			modelIndex;
	
	public Vector3f 	pos, vel, rot;
	
	public double 		lastUpdate;
	
	public MPlayer(HeaderData header, byte[] data) {
		client			= new Client(header.ip, header.port);
		modelIndex 		= 0;
		pos 			= new Vector3f();
		vel 			= new Vector3f();
		rot 			= new Vector3f();
		lastUpdate		= 0.0;
		retrieveData(data);
	}
	
	public void tick(double delta) {
		pos.x += vel.x * delta;
		pos.y += vel.y * delta;
		pos.z += vel.z * delta;
	}
	
	public byte[] getData() {
		byte[] sum;
		sum = addArrays(getByteArray(Main.time), getByteArray(pos.x));
		sum = addArrays(sum, getByteArray(pos.y));
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
		int index = 0;
		double newUpdate = getDouble(data, index); index += 8;
		if (newUpdate < lastUpdate) {
			System.out.println("Packets arrived in wrong order. ");
			return;
		}
		lastUpdate = newUpdate;
		pos.x = getFloat(data, index); index += 4;
		pos.y = getFloat(data, index); index += 4;
		pos.z = getFloat(data, index); index += 4;
		rot.x = getFloat(data, index); index += 4;
		rot.y = getFloat(data, index); index += 4;
		rot.z = getFloat(data, index); index += 4;
		vel.x = getFloat(data, index); index += 4;
		vel.y = getFloat(data, index); index += 4;
		vel.z = getFloat(data, index); index += 4;
	}
	
	public static byte[] getByteArray(float f) {
		return java.nio.ByteBuffer.allocate(Float.BYTES).putFloat(f).array();
	}
	
	public static float getFloat(byte[] b, int offs) {
		return java.nio.ByteBuffer.allocate(b.length).put(b).getFloat(offs);
	}
	
	public static byte[] getByteArray(double d) {
		return java.nio.ByteBuffer.allocate(Double.BYTES).putDouble(d).array();
	}
	
	public static double getDouble(byte[] b, int offs) {
		return java.nio.ByteBuffer.allocate(b.length).put(b).getDouble(offs);
	}
	
}
