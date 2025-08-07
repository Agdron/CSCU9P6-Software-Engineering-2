import java.util.ArrayList;

/**
 * Contains an array of PassengerDetails objects - one per passenger on a flight.
 * Incoming flights supply their passenger list in their flight descriptor, and the ManagementRecord for the flight extracts the PassengerList and holds it separately.
 * Outbound flights have PassengerLists built from passenger details supplied by the gate consoles, and the list is uploaded to the aircraft as it departs in a newly built FlightDescriptor.
 */
public class PassengerList {
	private ArrayList<PassengerDetails> details = new ArrayList<PassengerDetails>();
	
	/**
 	* The given passenger is boarding.
 	* Their details are recorded, in the passenger list.
 	* @preconditions Status is READY_PASSENGERS
 	*/
	public void addPassenger(PassengerDetails details){
		this.details.add(details);
	}
	
	public int getSize() {
		return details.size();
	}
	
	/**Clear the whole passenger List.
	 * It's called when all passengers have disembarked from the flight.
	 */
	public void clear() {
		details.clear();
	}
	
	
	/**Return String of all passenger in details class separated by a newline.
	 * This is use for screens to display all passengers in a aircraft
	 */
	public String toString() {
		String output = "";
		for(int i = 0; i < details.size(); i++) {
			output += details.get(i).getName() + "\n";
		}
		return output;
	}
}
