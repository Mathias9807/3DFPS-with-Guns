package tremor;

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
	}
	
	private static void loop() {
	}
	
	private static void end() {
	}

}
