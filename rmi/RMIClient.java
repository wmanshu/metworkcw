/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.RMIServer;
import rmi.RMIServerI;

import common.MessageInfo;

public class RMIClient {
	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2) {
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String serverUrl = args[0];
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		// TO-DO: Bind to RMIServer
		// TO-DO: Attempt to send messages the specified number of times
		try {
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());

				Registry registry = LocateRegistry.getRegistry(8000);
				RMIServer server = (RMIServer) registry.lookup(serverUrl);
				// RMIServerI server = (RMIServerI) Naming.lookup(serverUrl);
				int numMsgs = 100;
				for (int i = 0; i < numMsgs; i++) {
					server.receiveMessage(new MessageInfo(numMsgs, i));
				}
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
