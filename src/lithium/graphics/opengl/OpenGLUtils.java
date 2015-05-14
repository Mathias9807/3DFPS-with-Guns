package lithium.graphics.opengl;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.*;

import static lithium.graphics.opengl.Shaders.*;

public class OpenGLUtils {
	
	public static void useMatrix(Matrix4f m, String var) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		m.store(buffer);
		buffer.flip();
		setParam4m(var, buffer);
	}
	
	public static void makePerspective(Matrix4f m, float near, float far, float fov) {
		float tweenP = far - near;
		float sumP   = far + near;
		float aspect = (float) Display.getWidth() / Display.getHeight();
		
		m.setIdentity();
		m.m00 = (float) (1 / Math.tan(fov / 360d * Math.PI)) / aspect; // fov / 2 / 180 = fov / 360
		m.m11 = (float) (1 / Math.tan(fov / 360d * Math.PI));
		m.m22 = -sumP / tweenP;
		m.m23 = -1;
		m.m32 = -2 * near * far / tweenP;
		m.m33 = 0;
	}
	
	public static Matrix4f lookAt(Vector3f dir) {
		Matrix4f r = new Matrix4f();
		r.setIdentity();
		
		Vector3f up = new Vector3f(0, 1, 0);
		
		Vector3f x = Vector3f.cross(dir, up, null).normalise(null);
		Vector3f y = Vector3f.cross(x, dir, null).normalise(null);
		Vector3f z = dir.normalise(null);
		r.m00 = x.x;
		r.m01 = x.z;
		r.m02 = x.y;
		r.m10 = y.x;
		r.m11 = y.z;
		r.m12 = y.y;
		r.m20 = z.x;
		r.m21 = z.z;
		r.m22 = z.y;
		
		r.invert();
		
		return r;
	}
	
}
