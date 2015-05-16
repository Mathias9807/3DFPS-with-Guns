package lithium.graphics;

import static lithium.graphics.opengl.FBO.setFrameBuffer;
import static lithium.graphics.opengl.OpenGLUtils.*;
import static lithium.graphics.opengl.Shaders.setShader;
import lithium.graphics.opengl.Shaders;
import lithium.level.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.*;

public class BasicRenderer implements RenderEngine {
	
	public static int fov = 65;
	public static float nearPlane = 0.1f, farPlane = 100;
	
	private int shaderBasic;
	
	private Matrix4f matProj, matView, matModel;
	
	public void init() {
		GL11.glClearColor(0.2f, 0.2f, 0.3f, 0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		matProj = new Matrix4f();
		makePerspective(matProj, nearPlane, farPlane, fov);
		
		matView = new Matrix4f();
		matView.translate(new Vector3f(0, 0, 0));
		
		matModel = new Matrix4f();
		
		shaderBasic = Shaders.load("Basic");
		setShader(shaderBasic);
		useMatrix(matProj, "matProj");
		useMatrix(matView, "matView");
	}

	public void tick() {
		setFrameBuffer(null);
		setShader(shaderBasic);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		matView.setIdentity();
		matView.rotate(Level.mainPlayer.rot.x, new Vector3f(1, 0, 0));
		matView.rotate(Level.mainPlayer.rot.y, new Vector3f(0, 1, 0));
		matView.translate(Level.mainPlayer.pos.negate(null));
		useMatrix(matView, "matView");
		
		for (int i = 0; i < Level.playerList.size(); i++) {
			MPlayer mp = Level.playerList.get(i);
			matModel.setIdentity();
			matModel.translate(mp.pos);
			matModel.rotate(mp.rot.x, new Vector3f(1, 0, 0));
			matModel.rotate(mp.rot.y, new Vector3f(0, 1, 0));
			useMatrix(matModel, "matModel");
			Graphics.VAOArray[mp.modelIndex].render();
		}
	}

	public void end() {
		
	}

}
