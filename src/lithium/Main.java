package lithium;

import lithium.graphics.Graphics;
import lithium.level.Level;
import carbon.CarbonClient;

public class Main {
	
	public static CarbonClient client;

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: [program] [IPv4 address] | [-server]");
			System.exit(0);
		}
		
		if (args[0].equals("-server")) {
			Networking.startServer();
			return;
		}
		
		Networking.startClient(args[0]);
		
		begin();
		
		loop();
		
		end();
		
		Networking.stopClient();
	}
	
	private static void begin() {
		Graphics.init();
		Level.init(null);
	}
	
	private static void loop() {
		while (true) {
			Level.tick(1);
			Graphics.tick();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}
	
	private static void end() {
	}

}
