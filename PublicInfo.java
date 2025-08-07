
/**
 * An interface LSH to SAAMS:
 * Public Information Screen:
 * Display of useful information about aircraft.
 * This class registers as an observer of the AircraftManagementDatabase, and is notified whenever any change occurs in that <<model>> element.
 * See written documentation.
 */
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Public Information Screen: Display of useful information about aircraft.
 * Registers as an observer of the AircraftManagementDatabase.
 */
public class PublicInfo extends JFrame implements Observer {
	private JFrame frame;
	private AircraftManagementDatabase aircraftDatabase;

	private JTextArea planeDetailsTextArea = new JTextArea(7,20);
	private JScrollPane planeDetailsScroll = new JScrollPane(planeDetailsTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	public PublicInfo(AircraftManagementDatabase aircraftDatabase) {
		frame = new JFrame("Public Info");

		this.aircraftDatabase = aircraftDatabase;
		aircraftDatabase.addObserver(this);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(680, 200);
		frame.setLocationRelativeTo(null);

		frame.add(planeDetailsScroll);

		planeDetailsTextArea.setEditable(false);
		frame.setVisible(true);
		frame.setLocation(0, 610);

		planeDetailsTextArea.setFont(new Font("Tahoma", Font. BOLD, 14));
		planeDetailsTextArea.setText("Flights          From          To          Next          Gate         Status");

	}

	/** Update the content of the public info table*/
	@Override
	public void update(Observable o, Object arg) {
		planeDetailsTextArea.setFont(new Font("Tahoma", Font. BOLD, 14));
		planeDetailsTextArea.setText("Flights          From          To          Next          Gate          Status");
		planeDetailsTextArea.append("\n\n");
		for (int i = 0; i < AircraftManagementDatabase.maxMRs; i++) {
			if(aircraftDatabase.getStatus(i) != ManagementRecord.FREE && aircraftDatabase.getStatus(i) != ManagementRecord.IN_TRANSIT) {
				ManagementRecord mr = aircraftDatabase.getMR(i);
				String oneLine = "";
				oneLine += mr.getFlightCode() + "      ";
				oneLine += mr.getItinerary().getFrom() + "      ";
				oneLine += mr.getItinerary().getTo() + "      ";
				oneLine += mr.getItinerary().getNext() + "      ";
				oneLine += mr.getGateNumber() + "      ";
				oneLine += mr.getStatusToString() + "      ";
				planeDetailsTextArea.append(oneLine + "\n\n");
			}

		}
	}

}
