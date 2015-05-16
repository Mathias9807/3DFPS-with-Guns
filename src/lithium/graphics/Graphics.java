package lithium.graphics;

import lithium.Main;
import lithium.graphics.opengl.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;

public class Graphics {
	
	public static DisplayMode dispMode;
	public static final String WINDOW_TITLE = "Lithium";
	
	public static RenderEngine rEngine;
	
	public static VAO[] VAOArray;
	
	public static void init() {
		try {
			createWindow();
		} catch (LWJGLException e) {
			System.err.println("Failed to create window. ");
			System.exit(0);
		}
		
		Model.loadModels();
		VAOArray = VAO.createVAOArray();
		
		rEngine = new BasicRenderer();
		rEngine.init();
	}
	
	public static void tick() {
		if (Display.isCloseRequested()) Main.running = false;
		
		rEngine.tick();
		
		Display.update();
		Display.sync(30);
		
		String error = GLU.gluErrorString(GL11.glGetError());
		if (error != "No error") System.out.println(error);
	}
	
	public static void end() {
		rEngine.end();
		Display.destroy();
	}
	
	public static void createWindow() throws LWJGLException {
		PixelFormat pixelFormat = new PixelFormat(8, 8, 0, 0);
		ContextAttribs context = new ContextAttribs(3, 2)
			.withForwardCompatible(true)
			.withProfileCore(true);
		
		dispMode = Display.getAvailableDisplayModes()[2];
		Display.setDisplayMode(dispMode);
		Display.setTitle(WINDOW_TITLE);
		Display.create(pixelFormat, context);
	}
	
}
