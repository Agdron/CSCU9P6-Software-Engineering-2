
/**
 * The Main class.
 *
 * The principal component is the usual main method required by Java application to launch the application.
 *
 * Instantiates the databases.
 * Instantiates and shows all the system interfaces as Frames.
 * @stereotype control
 */
// Main.java
import java.util.ArrayList;

/**
 * The Main class is the entry point of the SAAMS (Stirling Airport Aircraft Management System) application.
 * It is responsible for initializing the system components and launching the user interfaces.
 */
public class Main {

	private static AircraftManagementDatabase aircraftDatabase;
	private static GateInfoDatabase gateDatabase;
	private static LATC latcScreen;
	private static GOC gocScreen;
	private static CleaningSupervisor cleaningScreen;
	private static MaintenanceInspector maintenanceScreen;
	private static RefuellingSupervisor refuellingScreen;
	private static ArrayList<GateConsole> gateConsoles;
	private static ArrayList<PublicInfo> publicInfoScreens;
	private static RadarTransceiver radarSimulator;

	public static void main(String[] args) {
		// Instantiate core system databases
		aircraftDatabase = new AircraftManagementDatabase();
		gateDatabase = new GateInfoDatabase();
		gocScreen = new GOC(gateDatabase, aircraftDatabase);
		latcScreen = new LATC(aircraftDatabase);
		radarSimulator = new RadarTransceiver(aircraftDatabase);
		GateConsole gateConsole0 = new GateConsole(gateDatabase, aircraftDatabase, 0);
		GateConsole gateConsole1 = new GateConsole(gateDatabase, aircraftDatabase, 1);
		GateConsole gateConsole2 = new GateConsole(gateDatabase, aircraftDatabase, 2);
		cleaningScreen = new CleaningSupervisor(aircraftDatabase);
		maintenanceScreen = new MaintenanceInspector(aircraftDatabase);
		refuellingScreen = new RefuellingSupervisor(aircraftDatabase);
		PublicInfo publicInfo1 = new PublicInfo(aircraftDatabase);
		PublicInfo publicInfo2 = new PublicInfo(aircraftDatabase);

	}
}
