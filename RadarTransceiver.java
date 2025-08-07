import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.util.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RadarTransceiver implements ActionListener, Observer, ListSelectionListener
{

	private JFrame frame;
	private	JLabel code, to, from, next, name, addPassName;
	private JTextField codeIn, toIn, fromIn, nextIn, nameIn;
	private JButton clearDetails, addPass, leaveAirspace, addPlane;
	private String flightCode, flightTo, flightFrom, nextStop, passengerName;

	private JTextArea planePassengerList = new JTextArea();
	private JScrollPane planePassengerScroll = new JScrollPane(planePassengerList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	private JTextArea addPassengerList = new JTextArea();
	private JScrollPane addPassengerScroll = new JScrollPane(addPassengerList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	//The list of aircrafts in radar
	private JList aircraftList = new JList(new DefaultListModel());
	private JScrollPane scrollList = new JScrollPane(aircraftList);

	private AircraftManagementDatabase aircraftDatabase;

	private PassengerList passengerList = new PassengerList();

	public RadarTransceiver(AircraftManagementDatabase aircraftDatabase){
		flightCode = "";
		flightTo = "";
		flightFrom = "";
		nextStop = "";
		passengerName = "";

		clearDetails = new JButton("Clear Details");
		addPass = new JButton("Add Passenger");
		addPlane = new JButton("Add Plane");
		leaveAirspace = new JButton("Leave Airspace");

		planePassengerList.setEditable(false);
		addPassengerList.setEditable(false);

		this.aircraftDatabase = aircraftDatabase;
		aircraftDatabase.addObserver(this);
		createGUI();   
	}

	//Creating all the buttons, list, and textbox then arrange them
	private void createGUI()
	{
		frame = new JFrame("Radar Transceiver Controller");
		frame.setSize(840, 190);

		codeIn = new JTextField(10);
		toIn = new JTextField(10);
		fromIn = new JTextField(10);
		nextIn = new JTextField(10);
		nameIn = new JTextField(10);

		code = new JLabel("Flight Code ");
		to = new JLabel("Flight To ");
		from = new JLabel("Flight From ");
		next = new JLabel("Next Stop ");
		name = new JLabel("Passenger Name ");
		addPassName = new JLabel("Passenger in Plane");

		clearDetails = new JButton("Clear Details");
		addPass = new JButton("Add Passenger");
		addPlane = new JButton("Add Plane");
		leaveAirspace = new JButton("Leave Airspace");

		aircraftList.setVisibleRowCount(5);


		GridBagConstraints c = new GridBagConstraints();

		Border blackline = BorderFactory.createLineBorder(Color.black, 4);

		JPanel Panel1 = new JPanel(new GridLayout(1,2));

		JPanel inputPanel = new JPanel(new GridLayout(1, 2));

		JPanel inputInfo = new JPanel(new GridLayout(5, 2));
		inputInfo.add(code);
		inputInfo.add(codeIn);
		inputInfo.add(to);
		inputInfo.add(toIn);
		inputInfo.add(from);
		inputInfo.add(fromIn);
		inputInfo.add(next);
		inputInfo.add(nextIn);
		inputInfo.add(name);
		inputInfo.add(nameIn);

		JPanel addPassengerListPanel = new JPanel(new GridLayout(2, 1));
		addPassengerListPanel.add(addPassName);
		addPassengerListPanel.add(addPassengerScroll);

		inputPanel.add(inputInfo);
		inputPanel.add(addPassengerListPanel);

		inputPanel.setBorder(blackline);

		JPanel radarPanel = new JPanel(new GridLayout(1,2));

		JPanel planePassengerListPanel = new JPanel(new GridLayout(2,1));
		planePassengerListPanel.add(new JLabel("Passenger in Plane"),c);
		planePassengerListPanel.add(planePassengerScroll,c);

		JPanel planeListPanel = new JPanel(new GridLayout(2,1));
		planeListPanel.add(new JLabel("Planes in Radar List"),c);
		planeListPanel.add(scrollList,c);

		radarPanel.add(planeListPanel);
		radarPanel.add(planePassengerListPanel);

		JPanel controlPanel = new JPanel(new GridLayout(1, 4)); // 3 rows, 1 column
		controlPanel.add(clearDetails);
		controlPanel.add(addPass);
		controlPanel.add(addPlane);
		controlPanel.add(leaveAirspace);

		Panel1.add(inputPanel);
		Panel1.add(radarPanel);

		JPanel mainPanel = new JPanel(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 3;
		mainPanel.add(Panel1, c);
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 1;
		mainPanel.add(controlPanel, c);

		frame.add(mainPanel);
		frame.setVisible(true);  
		frame.setLocation(0, 20);

		clearDetails.addActionListener(this);
		addPass.addActionListener(this);
		addPlane.addActionListener(this);
		leaveAirspace.addActionListener(this);
		aircraftList.addListSelectionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		flightCode = codeIn.getText();
		flightTo = toIn.getText();
		flightFrom = fromIn.getText();
		nextStop = nextIn.getText();
		passengerName = nameIn.getText();

		if (e.getSource() == addPass){
			if(passengerName.equals("") || passengerName.equals(null)) {
				name.setForeground(Color.red);
			} else {
				PassengerDetails passenger_Name = new PassengerDetails(passengerName);
				passengerList.addPassenger(passenger_Name);
				addPassengerList.setText(passengerList.toString());
				name.setForeground(Color.black);
				nameIn.setText(null);
			}

		}
		else if (e.getSource() == clearDetails) {
			codeIn.setText(null);
			toIn.setText(null);
			fromIn.setText(null);
			nextIn.setText(null);
			nameIn.setText(null);
			addPassengerList.setText(null);
			passengerList = new PassengerList();
		}
		else if (e.getSource() == addPlane){
			//Set JLabel back to black
			code.setForeground(Color.black);
			to.setForeground(Color.black);
			next.setForeground(Color.black);
			from.setForeground(Color.black);
			addPassName.setForeground(Color.black);
			name.setForeground(Color.black);
			
			//if any text box is empty and you try to submit then the jlabel for that empty textbox will go red 
			if(flightCode.equals("")||flightTo.equals("")||flightFrom.equals("")||nextStop.equals("")||passengerList.getSize()==0) {
				if(flightCode.equals("")) {
					code.setForeground(Color.red);
				}
				if(flightTo.equals("")){
					to.setForeground(Color.red);
				}
				if(flightFrom.equals("")){
					from.setForeground(Color.red);
				}
				if(nextStop.equals("")){
					next.setForeground(Color.red);
				}
				if(passengerList.getSize()==0){
					addPassName.setForeground(Color.red);
					name.setForeground(Color.red);
				}
			} else {
				Itinerary itinerary = new Itinerary(flightFrom, flightTo, nextStop);
				FlightDescriptor fD = new FlightDescriptor(flightCode, itinerary, passengerList);
				aircraftDatabase.radarDetect(fD);
	
				codeIn.setText(null);
				toIn.setText(null);
				fromIn.setText(null);
				nextIn.setText(null);
				nameIn.setText(null);
				addPassengerList.setText(null);
				passengerList = new PassengerList();
				
				//set the jlabels back to black
				code.setForeground(Color.black);
				to.setForeground(Color.black);
				next.setForeground(Color.black);
				from.setForeground(Color.black);
				addPassName.setForeground(Color.black);
				name.setForeground(Color.black);
			}
		}
		else if (e.getSource() == leaveAirspace){
			ManagementRecord selectedMR = (ManagementRecord) aircraftList.getSelectedValue();
			if(selectedMR != null) {
				aircraftDatabase.radarLostContact(selectedMR.getmCode());
			}
		}
	}


	//Update the aircraft list content
	@Override
	public void update(Observable o, Object arg) {
		DefaultListModel theList = (DefaultListModel) aircraftList.getModel();
		theList.clear();
		for (int i = 0; i < AircraftManagementDatabase.maxMRs; i++) {
			if(aircraftDatabase.getStatus(i) != ManagementRecord.FREE) {
				theList.addElement(aircraftDatabase.getMR(i));
			}
		}

	}

	//If a item in the Aircraft list is select then it will update the passenger GUI list
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource() == aircraftList) {
			ManagementRecord selectedMR = (ManagementRecord) aircraftList.getSelectedValue();
			if(selectedMR != null) {
				planePassengerList.setText(selectedMR.getPassengerList().toString());
				System.out.println(selectedMR.toString());
			}
		}

	}
}