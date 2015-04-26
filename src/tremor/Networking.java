package tremor;

import carbon.CarbonClient;
import carbonserver.CarbonServer;

public class Networking {
	
	public static void startServer() {
		CarbonServer.updatesPerSecond = 20;
		CarbonServer.main(null);
	}

	public static void startClient(String ip) {
		CarbonClient.updatesPerSecond = 10;
		Main.client = new CarbonClient(ip);
	}
	
	public static void stopClient() {
		Main.client.disconnect();
	}
	
}
