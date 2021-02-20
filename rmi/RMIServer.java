/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.nio.IntBuffer;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;
	// private IntBuffer receivedMessagesBuffer;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		int messageNum = msg.messageNum;

		if (totalMessages == -1) {
			totalMessages = msg.totalMessages;
			receivedMessages = new int[totalMessages];
		}

		receivedMessages[messageNum] = messageNum;

		// TO-DO: Log receipt of the message
		System.out.println("Message received: " + msg.toString());

		// TO-DO: If this is the last expected message, then identify
		// any missing messages
		if (totalMessages == (messageNum + 1)) {
			// if (totalMessages == ) {
			// doesn't have missing messages
			System.out.println("Total number of messages received: " + totalMessages);
			// System.out.println("Number of missing messages: 0");
			// } else {
			// // has missing messages
			// int numMissing = expectedTotalMessages - totalMessages;

			// System.out.println("Total number of messages received: " + totalMessages);
			// System.out.println("Number of missing messages: " + numMissing);

			// }
		}
	}

	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		// TO-DO: Instantiate the server class
		// TO-DO: Bind to RMI registry
		try {
			String serverUrl = "serverUrl";
			RMIServer server = new RMIServer();
			rebindServer(serverUrl, server);
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry
		// in the start script)
		try {
			Registry registry = LocateRegistry.createRegistry(8000);

			// TO-DO:
			// Now rebind the server to the registry (rebind replaces any existing servers
			// bound to the serverURL)
			// Note - Registry.rebind (as returned by createRegistry / getRegistry) does
			// something similar but
			// expects different things from the URL field.
			registry.rebind(serverURL, server);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}

	}
}
