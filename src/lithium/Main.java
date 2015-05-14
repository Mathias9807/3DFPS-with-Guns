package lithium;

import org.lwjgl.Sys;

import lithium.graphics.Graphics;
import lithium.level.Level;
import carbon.CarbonClient;

public class Main {
	
	public static CarbonClient client;
	
	public static boolean running = true;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: [program] [IPv4 address] | [-server]");
			System.exit(0);
		}
		
		if (args[0].equals("-server")) {
			Networking.startServer();
			return;
		}
		
		begin();
		
		Networking.startClient(args[0]);
		
		loop();
		
		end();
		
		Networking.stopClient();
	}
	
	private static void begin() {
		Graphics.init();
		Level.init(null);
	}
	
	private static void loop() {
		double now = (double) Sys.getTime() / Sys.getTimerResolution();
		double past = now;
		double delta;
		while (running) {
			now = (double) Sys.getTime() / Sys.getTimerResolution();
			delta = now - past;
			past = now;
			
			Level.tick(delta);
			Graphics.tick();
		}
	}
	
	private static void end() {
		Graphics.end();
	}

}
