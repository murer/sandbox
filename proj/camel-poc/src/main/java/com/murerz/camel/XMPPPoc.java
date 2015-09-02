package com.murerz.camel;

import java.io.IOException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

public class XMPPPoc {

	public static void main(String[] args) throws SmackException, IOException, XMPPException {
		// connect to gtalk server
		ConnectionConfiguration connConfig = new ConnectionConfiguration("talk.google.com", 5222, "fuweweu.com");
		XMPPConnection connection = new XMPPTCPConnection(connConfig);
		connection.connect();

		// login with username and password
		connection.login("pyrata", "5t6y7u8i");

		// set presence status info
		Presence presence = new Presence(Presence.Type.available);
		connection.sendPacket(presence);

		// send a message to somebody
		Message msg = new Message("fuweweu@gmail.com", Message.Type.chat);
		msg.setBody("hello");
		connection.sendPacket(msg);

		// receive msg
		PacketListener pl = new PacketListener() {
		  public void processPacket(Packet p) {
		    System.out.println(p.getFrom() + ": " + p.toString());
		    if (p instanceof Message) {
		      Message msg = (Message) p;
		      System.out.println(msg.getFrom() + ": " + msg.getBody());
		    }
		  }
		};
		connection.addPacketListener(pl, null);

		// wait for user to end program
		System.in.read();

		// set presence status to unavailable
		presence = new Presence(Presence.Type.unavailable);
		connection.sendPacket(presence);
	}
	
}
