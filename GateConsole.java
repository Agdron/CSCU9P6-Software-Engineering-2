
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * An interface to SAAMS:
 * Gate Control Console:
 * Inputs events from gate staff, and displays aircraft and gate information.
 * This class is a controller for the GateInfoDatabase and the AircraftManagementDatabase: sends messages when aircraft dock, have finished disembarking, and are fully emarked and ready to depart.
 * This class also registers as an observer of the GateInfoDatabase and the
 * AircraftManagementDatabase, and is notified whenever any change occurs in those <<model>> elements.
 * See written documentation.
 */
public class GateConsole implements ActionListener, Observer {

	/***  The GateConsole interface has access to the GateInfoDatabase.*/
	private GateInfoDatabase gateInfoDatabase;

	/***  The GateConsole interface has access to the AircraftManagementDatabase.LSH*/
	private AircraftManagementDatabase aircraftDatabase;

	/**
	 * This gate's gateNumber
	 * - for identifying this gate's information in the GateInfoDatabase.
	 */
	private int gateNumber;
	private JFrame frame;
	private JButton addPassengerButton = new JButton("Add Passenger");
	private JButton planeDockedButton = new JButton("Plane Docked");
	private JButton planeUnloadedButton = new JButton("Plane Unloaded");
	private JButton readyToDepartButton = new JButton("Flight Ready to Depart");
	private JLabel passName = new JLabel("Passenger Name");

	private JTextArea planeDetailsTextArea = new JTextArea(7,20);
	private JScrollPane planeDetailsScroll = new JScrollPane(planeDetailsTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	private JTextArea passengerList = new JTextArea(5,20);
	private JScrollPane passengerListScroll = new JScrollPane(passengerList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	private JTextField passengerNameInput = new JTextField(10);

	public GateConsole(GateInfoDatabase gateInfoDatabase, AircraftManagementDatabase aircraftDatabase, int gateNumber){
		frame = new JFrame("Gate " + gateNumber);
		this.aircraftDatabase = aircraftDatabase;
		this.gateInfoDatabase = gateInfoDatabase;
		this.gateNumber = gateNumber;

		aircraftDatabase.addObserver(this);
		gateInfoDatabase.addObserver(this);

		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(435, 350);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());

		planeDetailsTextArea.setEditable(false);
		passengerList.setEditable(false);

		JPanel mainPanel = new JPanel(new GridLayout(2, 2));

		JPanel panel1 = new JPanel(new GridLayout(2, 1));

		panel1.add(new JLabel("Passenger onbroad"));
		panel1.add(passengerListScroll);

		JPanel panel2 = new JPanel(new GridLayout(2, 1));
		panel2.add(new JLabel("Plane Details"));
		panel2.add(planeDetailsScroll);

		JPanel panel3 = new JPanel(new GridLayout(3, 1));
		panel3.add(passName);
		panel3.add(passengerNameInput);
		panel3.add(addPassengerButton);

		JPanel panel4 = new JPanel(new GridLayout(3, 1));
		panel4.add(planeDockedButton);
		panel4.add(planeUnloadedButton);
		panel4.add(readyToDepartButton);

		mainPanel.add(panel1);
		mainPanel.add(panel2);
		mainPanel.add(panel3);
		mainPanel.add(panel4);

		frame.add(mainPanel);
		frame.setVisible(true);

		addPassengerButton.addActionListener(this);
		planeDockedButton.addActionListener(this);
		planeUnloadedButton.addActionListener(this);
		readyToDepartButton.addActionListener(this);

		frame.setLocation(1105, 20);

		intializePlaneDetail();
	}

	private int gatemCode(){
		return gateInfoDatabase.getmCode(gateNumber);
	}

	private void intializePlaneDetail() {
		String output = "";
		output += "Gate Status : FREE\n";
		output += "Flight Code : \n";
		output += "Plane Status : \n";
		output += "#Passenger : \n";
		output += "To : \n";
		output += "From : \n";
		output += "Next :";

		planeDetailsTextArea.setText(output);
	}

	public String getStatusToString() {
		int gateStatus = gateInfoDatabase.getStatus(gateNumber);
		if(gateStatus == Gate.FREE) {
			return "FREE";
		}
		else if(gateStatus == Gate.RESERVED) {
			return "RESERVED";
		}
		else if(gateStatus == Gate.OCCUPIED) {
			return "OCCUPPIED";
		}
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		if(gateInfoDatabase.getStatus(gateNumber) != Gate.FREE) {
			passengerList.setText(aircraftDatabase.getMR(gatemCode()).getPassengerList().toString());
			planeDetailsTextArea.setText("Gate Status : " + getStatusToString() + "\n" + aircraftDatabase.getMR(gatemCode()).getDetails());
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(gatemCode() != -1) {
			if (e.getSource() == planeDockedButton){
				aircraftDatabase.setStatus(gatemCode(), ManagementRecord.UNLOADING);
				gateInfoDatabase.docked(gateNumber);
			}
			else if (e.getSource() == planeUnloadedButton){
				aircraftDatabase.getMR(gatemCode()).getPassengerList().clear();
				aircraftDatabase.setStatus(gatemCode(), ManagementRecord.READY_CLEAN_AND_MAINT);
			}
			else if (e.getSource() == addPassengerButton){
				if(passengerNameInput.getText().equals("")) {
					passName.setForeground(Color.red);
				} else {
					PassengerDetails passengerDetail = new PassengerDetails(passengerNameInput.getText());
					passengerNameInput.setText(null);
					aircraftDatabase.addPassenger(gatemCode(), passengerDetail);
					passName.setForeground(Color.black);
				}
			}
			else if (e.getSource() == readyToDepartButton){
				if(aircraftDatabase.getStatus(gatemCode()) == ManagementRecord.READY_PASSENGERS) {
					aircraftDatabase.setStatus(gatemCode(), ManagementRecord.READY_DEPART);
					passengerList.setText(null);
					intializePlaneDetail();
					gateInfoDatabase.departed(gateNumber);
				}
			}
		}


	}
}

