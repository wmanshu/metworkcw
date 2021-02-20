/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
			// Done Initialisation
			System.out.println("UDPServer ready");
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

	public static void main(String args[]) {
		int recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		server.run();
	}

	private void run() {
		int pacSize = 1000;
		byte[] pacData = new byte[pacSize];
		DatagramPacket pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		// Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		int timeout = 30000;
		try {
			recvSoc.setSoTimeout(timeout);
			while (true) {
				pac = new DatagramPacket(pacData, pacSize);
				recvSoc.receive(pac);
				processMessage(new String(pac.getData(), StandardCharsets.UTF_8));
			}
		} catch (Exception e) {
			System.out.println("Exception ar UDP server: " + e);
		}
	}

	public void processMessage(String data) {
		// TO-DO: Use the data to construct a new MessageInfo object
		try {
			MessageInfo msg = new MessageInfo(data);

			// TO-DO: On receipt of first message, initialise the receive buffer;
			int expectTotal = msg.totalMessages;
			int currNum = msg.messageNum;

			if (currNum == 1) {
				receivedMessages = new int[expectTotal];
				totalMessages = 1;
			} else {
				totalMessages += 1;
			}
			receivedMessages[currNum] = currNum;

			// TO-DO: Log receipt of the message
			System.out.println("message received at server: " + data);

			// TO-DO: If this is the last expected message, then identify
			// any missing messages
			if (currNum == expectTotal) {
				// And checks
				System.out.println("expected total: " + expectTotal);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

}
