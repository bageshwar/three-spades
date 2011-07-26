/**
 * 
 */
package org.techno.blackthree.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.techno.blackthree.common.Codes;
import org.techno.blackthree.server.Server;

/**
 * @author bageshwp
 * 
 */
public class Client implements Runnable {

	Socket clientSocket = null;

	/**
	 * This monitor blocks any thread to access this thread's info until the
	 * stream and vital parameters have been successfully initialized.
	 * */
	private boolean monitor = false;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public static void main(String s[]) {

		Client client = null;

		if (s == null) {
			System.out.println("Host/port missing");
			System.exit(-1);
		} else if (s.length == 1) {
			try {
				client = new Client(s[0]);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else

		if (s.length == 2) {
			try {
				client = new Client(s[0], Integer.parseInt(s[1]));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Invalid Number of parameters:  Client host port");
		}

		new Thread(client).start();

	}

	private Client(String host) throws UnknownHostException, IOException {

		this(host, Server.SERVER_PORT);

	}

	private Client(String host, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(host, port);
	}

	@Override
	public void run() {

		try {
			initStreams();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void initStreams() throws IOException, ClassNotFoundException {

		System.out.println("Initing Client streams...");
		input = new ObjectInputStream(clientSocket.getInputStream());
		//output = new ObjectOutputStream(clientSocket.getOutputStream());

		System.out.println(input.available());
		//Object  o =input.readObject(); 
		String s = input.readUTF();
		System.out.println("read "+s);
		if (Codes.OK.equalsIgnoreCase(s))
			System.out.println("Connection Successfull");
		
		//output.writeChars(Codes.OK);

	}
}
