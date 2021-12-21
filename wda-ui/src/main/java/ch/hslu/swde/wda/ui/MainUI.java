package ch.hslu.swde.wda.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.rmi.RemoteException;

public class MainUI {

	private static final Logger Log = LogManager.getLogger(MainUI.class);

	
	public static void main(String[] args) throws RemoteException {

		try {
			UI ui = new UI();
			ui.startFromBeginning();
		} catch (RemoteException e) {
			Log.error("A Method in UI threw a java.rmi.RemoteException");
			e.printStackTrace();
		}

	}
}