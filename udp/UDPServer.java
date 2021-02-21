/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
		this.close = false;
		int pacSize = 1000;
		byte[] pacData = new byte[pacSize];
		DatagramPacket pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		// Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		int timeout = 30000;
		try {
			recvSoc.setSoTimeout(timeout);
			while (!close) {
				pac = new DatagramPacket(pacData, pacSize);
				try {
					recvSoc.receive(pac);
				} catch (SocketTimeoutException e) {
					printMsgs();
					recvSoc.close();
				}
				processMessage(new String(pac.getData()).trim());
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			recvSoc.close();
		}
	}

	public void processMessage(String data) {
		// TO-DO: Use the data to construct a new MessageInfo object

		MessageInfo msg = null;
		try {
			msg = new MessageInfo(data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TO-DO: On receipt of first message, initialise the receive buffer;
			int currNum = msg.messageNum;
			if (currNum == 0) {
				this.totalMessages = msg.totalMessages;
				receivedMessages = new int[totalMessages];
			}
			receivedMessages[currNum] = currNum;

			// TO-DO: Log receipt of the message
			System.out.println("Message received: " + data);

			// TO-DO: If this is the last expected message, then identify
			// any missing messages
			if (currNum == (totalMessages - 1)) {
				this.close = true;
				printMsgs();
			}
	}

	private void printMsgs() {
		List<Integer> missingMsgs = new ArrayList<>();
		for(int i = 0; i < totalMessages; i++) {
			if (receivedMessages[i] != i) {
				missingMsgs.add(i);
			}
		}
		System.out.println("Total number of messages sent: " + totalMessages);
		if (missingMsgs.isEmpty()) {
			System.out.println("All messaged received");
		} else {
			System.out.println("Number of missing messages: " + missingMsgs.size());
			System.out.println("Messages numbers that are missing: " + missingMsgs);
		}
	}

}
