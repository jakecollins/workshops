import java.io.*;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

public class OpenResources {

	private static final int TCP_START = 50000;
	private static final int UDP_START = 51000;

	public static void main(String[] args) throws Exception {

		if (args.length != 3) {
			System.out.println("Usage: OpenResources <numFiles> <numTcp> <numUdp>");
			System.exit(1);
		}

		//Countdown to start - allow time to attach dtruss
		System.out.println("Starting in 45 seconds");
		Thread.sleep(TimeUnit.SECONDS.toMillis(45));
		System.out.println("GO!");

		openFiles(Integer.parseInt(args[0]));
		openNetworkForUDP(Integer.parseInt(args[1]));
		openNetworkForTCP(Integer.parseInt(args[2]));

		redirectStdOut();

		Thread.sleep(TimeUnit.HOURS.toMillis(1));

	}

	//Redirect stdout to a file
	private static void redirectStdOut() throws Exception {
		File temp = File.createTempFile("sout",".txt");
		temp.deleteOnExit();
		System.setOut(new PrintStream(temp));
	}

	//Create zero or more temp files and write to them
	private static void openFiles(int i) throws Exception {
		for (int x = 0; x < i; x++) {
			File temp = File.createTempFile("temp",".txt");
			temp.deleteOnExit();
			FileWriter writer = new FileWriter(temp);
			writer.write("tempFile" + x);
			System.out.printf("File open on: %s\n", temp.getCanonicalPath());
		}
	}

	//Open zero or more UDP network ports
	private static void openNetworkForUDP(int i) throws Exception {
		for (int x = 0; x < i; x++) {
			int port = UDP_START + x;
			new DatagramSocket(port);
			System.out.printf("UDP listening on: %s\n", port);
		}
	}

	//Open zero or more TCP network ports
	private static void openNetworkForTCP(int i) throws Exception {
		for (int x = 0; x < i; x++) {
			int port = TCP_START + x;
			new ServerSocket(port);
			System.out.printf("TCP listening on: %s\n", port);
		}
	}

}
