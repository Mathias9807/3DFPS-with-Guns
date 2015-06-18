package lithium;

import org.lwjgl.*;
import org.lwjgl.input.*;

import lithium.graphics.Graphics;
import lithium.level.Level;
import carbon.CarbonClient;

public class Main {
	
	public static CarbonClient client;
	
	public static boolean running = true;
	
	public static double time = 0;
	
	public static String[] args;

	public static void main(String[] args) {
		Main.args = args;
		
		if (args.length == 0) {
			System.out.println("Usage: [program] [options] [IPv4 address] | [-server]");
			System.exit(0);
		}
		
		if (hasParameter("-server")) {
			Networking.startServer();
			return;
		}
		
		begin();
		
		Networking.startClient(args[0]);
		
		loop();
		
		end();
		
		Networking.stopClient();
		
		System.exit(0);
	}
	
	private static void begin() {
		Graphics.init();
		Level.init(null);
		
		try {
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			System.err.println("Failed to initialize keyboard and mouse. ");
		}
	}
	
	private static void loop() {
		double now = (double) Sys.getTime() / Sys.getTimerResolution();
		double past = now;
		double delta;
		while (running) {
			now = (double) Sys.getTime() / Sys.getTimerResolution();
			delta = now - past;
			past = now;
			
			time += delta;
			Level.tick(delta);
			Graphics.tick();
		}
	}
	
	private static void end() {
		Graphics.end();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static boolean hasParameter(String param) {
		for (String a : args) 
			if (a.equals(param))
				return true;
		return false;
	}

}
