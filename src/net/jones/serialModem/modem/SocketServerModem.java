package net.jones.serialModem.modem;

 

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import net.jones.serialModem.zmodem.XModem;
import net.jones.serialModem.zmodem.YModem;

public class SocketServerModem extends SerialModem {

	public static void main(String[] args)  {
			(new SocketServerModem()).go(9090);
	}
	private ServerSocket svrSock = null; 
	private Socket cntSock = null; 
	
	private int port = -1;
	
	
	public void go(int pport) {
		port = pport;		
		lg = Logger.getLogger( SocketModem.class.getName() );
		lg.setLevel(Level.ALL);
		lg.addHandler( new StreamHandler(System.out, new SimpleFormatter()));		

		while (true) {
			try {		
				startSession();
			} catch (Exception e) {
				lg.log(Level.SEVERE,"SocketServer:"+pport, e);					

			} finally {
				try {srIn.close();}     catch (Exception e) {}
				try {srOut.close();}    catch (Exception e) {}
				try {cntSock.close();}  catch (Exception e) {}
				try {svrSock.close();}  catch (Exception e) {}
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {}
		}
	}
	
	void startSession() throws Exception {
		
		try { if(cntSock != null) cntSock.close();}  catch (Exception e) {}		
		try { if(svrSock != null) svrSock.close();}  catch (Exception e) {}
		
		buildMenu();
		svrSock = new ServerSocket(port);
		lg.info("Socket Server Modem Restarted on port: "+port);
		cntSock = svrSock.accept();
		srOut  = cntSock.getOutputStream();		
		srIn   = new TimerInputStream(cntSock.getInputStream());
		yModem = new YModem(srIn, srOut);
		xModem = new XModem(srIn, srOut);
			
		srOut.write(CLEAR);
		srOut.write(header);

		lg.info("Socket Server Modem - connecton");

		while (true) {
			if(disconnected) {								
				srOut.write((PROMPT).getBytes());
				if(! processCommand(getStringFromPort(false).trim())) 
					srOut.write((CONFAIL).getBytes());

			} 
			Thread.sleep(1000);  
		}
	}
	
	protected int userPassword() throws IOException {
		cntSock.getOutputStream().write(bbs.password.getBytes());
		cntSock.getOutputStream().flush();
		return -1;
	}
	protected int userUserID() throws IOException {
		cntSock.getOutputStream().write(bbs.user.getBytes());
		cntSock.getOutputStream().flush();
		return -1;
	}

}

