package ch.hslu.swde.wda.business;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerClass {

	
	public static void setup() {
	
	// IP Adresse des RMI Servers (von Aussen erreichbar) und die RMI-Port-Nummer
	String hostIp = "10.177.6.157";
	int rmiPort = 1099;
	// Host-IP als System-Property setzen (wird zu Clients im Stub zugestellt)
	System.setProperty("java.rmi.server.hostname", hostIp);
	// Entferntes Objekt erzeugen
	BusinessHandler businessHandlerRO = null;
	Registry reg = null;
	try {
		businessHandlerRO = new BusinessHandlerImpl();
		reg = LocateRegistry.createRegistry(rmiPort);
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	// Namensdienst erstellen und starten
	
	
	if (reg != null) {
	// Entferntes Objekt beim Namensdienst registrieren (binding)
	try {
		reg.rebind(BusinessHandler.RO_NAME, businessHandlerRO);
	} catch (AccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	// IP Adresse und die Port-Nummer anzeigen
	System.out.println("\nServer gestartet: [" + hostIp + ":" + rmiPort + "]");
	// Ausführung anhalten (eine Variante)
	System.out.print("\nBeenden mit ENTER-Taste!\n");
	new java.util.Scanner(System.in).nextLine();
	// Unbind entferntes Objekt
	try {
		reg.unbind(BusinessHandler.RO_NAME);
	} catch (AccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NotBoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	System.exit(0);
	}
	}

	
	public static void main (String args[]) {
		setup();
	}
}


