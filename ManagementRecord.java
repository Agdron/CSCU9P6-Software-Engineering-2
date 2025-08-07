/**
 * An individual aircraft management record:
 * Either FREE or models an aircraft currently known to SAAMS.
 * See MRState diagram for operational details, and written documentation.
 * This class has public static int identifiers for the individual status codes.
 * An MR may be "FREE", or may contain a record of the status of an individual aircraft under the management of SAAMS.
 * An instance of AircraftManagementDatabase holds a collection of ManagementRecords, and sends the ManagementRecords messages to control/fetch their status.
 */
public class ManagementRecord {

	//All the Status code of a Aircraft
	public static int FREE = 0;
	public static int IN_TRANSIT = 1;
	public static int WANTING_TO_LAND = 2;
	public static int GROUND_CLEARANCE_GRANTED = 3;
	public static int LANDING = 4;
	public static int LANDED = 5;
	public static int TAXIING = 6;
	public static int UNLOADING = 7;
	public static int READY_CLEAN_AND_MAINT = 8;
	public static int FAULTY_AWAIT_CLEAN = 9;
	public static int OK_AWAIT_CLEAN = 11;
	public static int CLEAN_AWAIT_MAINT = 10;
	public static int AWAIT_REPAIR = 12;
	public static int READY_REFUEL = 13;
	public static int READY_PASSENGERS = 14;
	public static int READY_DEPART = 15;
	public static int AWAITING_TAXI = 16;
	public static int AWAITING_TAKEOFF = 17;
	public static int DEPARTING_THROUGH_LOCAL_AIRSPACE = 18;

	/** Unique number in the AircraftDatabase to manage each ManagementRecord 
	 * when ManagementRecord is operating mCode will be 0 and above
	 * Initially set to -1 so can indicate any bugs throughout the system */
	private int mCode = -1;

	/** All ManagementRecord initially set to status FREE*/
	private int status = FREE;

	/** When a Aircraft is using a Gate the gateNumber is the which Gate it is at*/
	private int gateNumber;

	/** A short string identifying the flight:
	 *
	 * Usually airline abbreviation plus number, e.g. BA127.
	 * Obtained from the flight descriptor when the aircraft is first detected.
	 *
	 * This is the code used in timetables, and is useful to show on public information screens.
	 */
	private String flightCode;

	/**
	 * Holds the aircraft's itinerary.
	 * Downloaded from the flight descriptor when the aircraft is first detected.
	 */
	private Itinerary itinerary;

	/**
	 * The list of passengers on the aircraft.
	 * Incoming flights supply their passenger list in their flight decsriptor.
	 * Outbound flights have passenger lists built from passenger details supplied by the gate consoles.
	 */
	private PassengerList passengerList;

	/**Contains a description of what is wrong with the aircraft if it is found to be faulty during maintenance inspection.*/
	private String faultDescription;

	/**
	 * Request to set the MR into a new status.
	 *
	 * Only succeeds if the state change conforms to the MRState diagram.
	 *
	 * This is a general purpose state change request where no special details accompany the state change.
	 * [Special status changers are, for example, "taxiTo", where a gate number is supplied.]
	 * @preconditions Valid transition requested
	 */
	public void setStatus(int newStatus){
		if(newStatus == GROUND_CLEARANCE_GRANTED){
			if(status == WANTING_TO_LAND){
				status = GROUND_CLEARANCE_GRANTED;
			}
		} else if(newStatus == LANDING){
			if(status == GROUND_CLEARANCE_GRANTED){
				status = LANDING;
			}
		} else if(newStatus == LANDED){
			if(status == LANDING){
				status = LANDED;
			}
		} else if(newStatus == UNLOADING){
			if(status == TAXIING){
				status = UNLOADING;
			}
		} else if(newStatus == READY_CLEAN_AND_MAINT){
			if(status == UNLOADING || status == AWAIT_REPAIR){
				status = READY_CLEAN_AND_MAINT;
			}
		} else if(newStatus == OK_AWAIT_CLEAN){
			if(status == READY_CLEAN_AND_MAINT){
				status = OK_AWAIT_CLEAN;
			}
		} else if(newStatus == CLEAN_AWAIT_MAINT){
			if(status == READY_CLEAN_AND_MAINT){
				status = CLEAN_AWAIT_MAINT;
			}
		} else if(newStatus == AWAIT_REPAIR){
			if(status == FAULTY_AWAIT_CLEAN || status == CLEAN_AWAIT_MAINT){
				status = AWAIT_REPAIR;
			}
		} else if(newStatus == READY_REFUEL){
			if(status == OK_AWAIT_CLEAN || status == CLEAN_AWAIT_MAINT){
				status = READY_REFUEL;
			}
		} else if(newStatus == READY_PASSENGERS){
			if(status == READY_REFUEL){
				status = READY_PASSENGERS;
			}
		} else if(newStatus == READY_DEPART){
			if(status == READY_PASSENGERS){
				status = READY_DEPART;
			}
		} else if(newStatus == AWAITING_TAXI){
			if(status == READY_DEPART){
				status = AWAITING_TAXI;
			}
		} else if(newStatus == AWAITING_TAKEOFF){
			if(status == AWAITING_TAXI){
				status = AWAITING_TAKEOFF;
			}
		} else if(newStatus == DEPARTING_THROUGH_LOCAL_AIRSPACE){
			if(status == AWAITING_TAKEOFF){
				status = DEPARTING_THROUGH_LOCAL_AIRSPACE;
			}
		}
	}

	public int getStatus(){
		return status;
	}

	public int getmCode() {
		return mCode;
	}

	public String getFlightCode(){
		return flightCode;
	}

	public int getGateNumber() {
		return gateNumber;
	}

	/** Return the a String version of the Status code
	 * Used for displaying the status of a Aircraft
	 * @return
	 */
	public String getStatusToString() {
		if(status == FREE) {
			return "FREE";
		}
		else if (status == IN_TRANSIT) {
			return "IN_TRANSIT";
		}
		else if (status == AWAIT_REPAIR) {
			return "AWAIT_REPAIR";
		}
		else if (status == AWAITING_TAKEOFF) {
			return "AWAITING_TAKEOFF";
		}
		else if (status == AWAITING_TAXI) {
			return "AWAITING_TAXI";
		}
		else if (status == CLEAN_AWAIT_MAINT) {
			return "CLEAN_AWAIT_MAINT";
		}
		else if (status == DEPARTING_THROUGH_LOCAL_AIRSPACE) {
			return "DEPARTING_THROUGH_LOCAL_AIRSPACE";
		}
		else if (status == FAULTY_AWAIT_CLEAN) {
			return "FAULTY_AWAIT_CLEAN";
		}
		else if (status == GROUND_CLEARANCE_GRANTED) {
			return "GROUND_CLEARANCE_GRANTED";
		}
		else if (status == LANDED) {
			return "LANDED";
		}
		else if (status == LANDING) {
			return "LANDING";
		}
		else if (status == OK_AWAIT_CLEAN) {
			return "OK_AWAIT_CLEAN";
		}
		else if (status == READY_CLEAN_AND_MAINT) {
			return "READY_CLEAN_AND_MAINT";
		}
		else if (status == READY_DEPART) {
			return "READY_DEPART";
		}
		else if (status == READY_PASSENGERS) {
			return "READY_PASSENGERS";
		}
		else if (status == READY_REFUEL) {
			return "READY_REFUEL";
		}
		else if (status == TAXIING) {
			return "TAXIING";
		}
		else if (status == UNLOADING) {
			return "UNLOADING";
		}
		else if (status == WANTING_TO_LAND) {
			return "WANTING_TO_LAND";
		}
		return null;
	}


	/**Return String of all info of the Aircraft
	 * Used for in GUI display
	 * @return
	 */
	public String getDetails() {
		String output = "";
		output += "Flight Code : " + flightCode + "\n";
		output += "Status : " + getStatusToString() + "\n";
		if(gateNumber == -1) {
			output += "Gate# : " + "\n";
		} else {
			output += "Gate# : " + gateNumber + "\n";
		}
		output += "#Passenger : " + passengerList.getSize() + "\n";
		output += "To : " + itinerary.getTo() + "\n";
		output += "From : " + itinerary.getFrom() + "\n";
		output += "Next : " + itinerary.getNext() + "\n";
		return output;
	}

	/** Override toString so when you put ManagementRecord in JList in will show the flightcode
	 * very useful used in nearly all GUI display.
	 */
	@Override
	public String toString() {
		if(status == FAULTY_AWAIT_CLEAN || status == AWAIT_REPAIR) {
			return flightCode + " (Faulty)";
		} else {
			return flightCode;
		}
	}

	/** Sets up the MR with details of newly detected flight
	 *
	 * Status must be FREE now, and becomes either IN_TRANSIT or WANTING_TO_LAND depending on the details in the LSH flight descriptor.
	 * @preconditions Status is FREE
	 */
	public void radarDetect(FlightDescriptor fd, int mCode){
		if(status == FREE){
			flightCode = fd.getFlightCode();
			itinerary = fd.getItinerary();
			passengerList = fd.getPassengerList();
			//associate mCode to this MR
			this.mCode = mCode;
			//If aircraft TO is Stirling then it is landing in Stirling otherwise they in transit
			if(itinerary.getTo().equals("Stirling")){
				status = WANTING_TO_LAND;
			} else {
				status = IN_TRANSIT;
			}
		}
	}

	/** This aircraft has departed from local airspace.
	 *
	 * Status must have been either IN_TRANSIT or DEPARTING_THROUGH_LOCAL_AIRSPACE, and becomes FREE (and the flight details are cleared).
	 * 
	 * Set all the attribute back to original
	 * @preconditions Status is IN_TRANSIT or DEPARTING_THROUGH_LOCAL_AIRSPACE
	 */
	public void radarLostContact(){
		if(status == IN_TRANSIT || status == DEPARTING_THROUGH_LOCAL_AIRSPACE){
			status = FREE;
			gateNumber = 0;
			flightCode = null;
			itinerary = null;
			passengerList = null;
			faultDescription = null;
			mCode = -1;
		}
	}

	/** GOC has allocated the given gate for unloading passengers.
	 *
	 * The gate number is recorded.The status must have been LANDED and becomes TAXIING.
	 * @preconditions Status is LANDED
	 */
	public void taxiTo(int gateNumber){
		if(status == LANDED){
			this.gateNumber = gateNumber;
			status = TAXIING;
		}
	}

	/** The Maintenance Supervisor has reported faults.
	 *
	 * The problem description is recorded.
	 *
	 * The status must have been READY_FOR_CLEAN_MAINT or CLEAN_AWAIT_MAINT and becomes FAULTY_AWAIT_CLEAN or AWAIT_REPAIR respectively.
	 * @preconditions Status is READY_FOR_CLEAN_MAINT or CLEAN_AWAIT_MAINT
	 */
	public void faultsFound(String description){
		if(status == READY_CLEAN_AND_MAINT){
			faultDescription = description;
			status = FAULTY_AWAIT_CLEAN;
		}
		if(status == CLEAN_AWAIT_MAINT){
			faultDescription = description;
			status = AWAIT_REPAIR;
		}
	}

	/** The given passenger is boarding this aircraft.
	 *
	 * Their details are recorded in the passengerList.
	 *
	 * For this operation to be applicable, the status must be READY_PASSENGERS, and it doesn't change.
	 * @preconditions Status is READY_PASSENGERS
	 */
	public void addPassenger(PassengerDetails details){
		if(status == READY_PASSENGERS){
			passengerList.addPassenger(details);
		}
	}

	//Return the entire current PassengerList
	public PassengerList getPassengerList(){
		return passengerList;
	}

	//Return the aircraft's Itinerary.
	public Itinerary getItinerary(){
		return itinerary;
	}
}
