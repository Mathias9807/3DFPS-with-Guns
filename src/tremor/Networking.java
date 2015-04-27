package tremor;

import carbon.CarbonClient;
import carbonserver.*;

public class Networking {
	
	public static void startServer() {
		CarbonServer.updatesPerSecond = 1;
		CarbonServer.useSystemInputStream = true;
		CarbonServer.eventOnUpdate = (c) -> { updateServer(c); };
		
		CarbonServer.main(null);
	}

	public static void startClient(String ip) {
		CarbonClient.updatesPerSecond = 0;
		CarbonClient.useSystemInputStream = false;
		CarbonClient.eventOnUpdate = () -> { updateClient(); };
		Main.client = new CarbonClient(ip);
	}
	
	public static void updateServer(Client c) {
	}
	
	public static void updateClient() {
	}
	
	public static void stopClient() {
	}
	
}
