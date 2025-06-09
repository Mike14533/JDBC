import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
@SuppressWarnings("serial")
public class JDBCMainWindowContentBookings extends JInternalFrame implements ActionListener
{	
	String cmd = null;

	// DB Connectivity Attributes
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	private Container content;

	private JPanel detailsPanel;
	private JPanel exportButtonPanel;
	//private JPanel exportConceptDataPanel;
	private JScrollPane dbContentsPanel;

	private Border lineBorder;

	private JLabel IDLabel=new JLabel("ID:                 ");
	private JLabel FirstNameLabel=new JLabel("FirstName:               ");
	private JLabel DateLabel=new JLabel("Date:                 ");
	private JLabel TimeLabel=new JLabel("Time:               ");
	private JLabel LastNameLabel=new JLabel("LastName:      ");
	private JLabel PhoneNumberLabel=new JLabel("Phone Number:               ");
	private JLabel DogNameLabel=new JLabel("Dog's Name:        ");


	private JTextField IDTF= new JTextField(10);
	private JTextField FirstNameTF=new JTextField(10);
	private JTextField LastNameTF=new JTextField(10);
	private JTextField DateTF=new JTextField(10);
	private JTextField TimeTF=new JTextField(10);
	
	private JTextField PhoneNumberTF=new JTextField(10);
	private JTextField DogNameTF=new JTextField(10);
	private JButton ListAllBookings  = new JButton("ListAllBookings");
	private JButton ListBookingsOnDate  = new JButton("ListBookingsOnDate");
	private JTextField BookingsOnDateTF=new JTextField(10);


	private static QueryTableModelBookings TableModel = new QueryTableModelBookings();
	//Add the models to JTabels
	private JTable TableofDBContents=new JTable(TableModel);
	//Buttons for inserting, and updating members
	//also a clear button to clear details panel
	private JButton updateButton = new JButton("Update");
	private JButton insertButton = new JButton("Insert");
	private JButton exportButton  = new JButton("Export");
	private JButton deleteButton  = new JButton("Delete");
	private JButton clearButton  = new JButton("Clear");



//	private JButton  NumLectures = new JButton("NumLecturesForDepartment:");
//	private JTextField NumLecturesTF  = new JTextField(12);
//	private JButton avgAgeDepartment  = new JButton("AvgAgeForDepartment");
//	private JTextField avgAgeDepartmentTF  = new JTextField(12);
//	private JButton ListAllCustomers  = new JButton("ListAllCustomers");
//	private JButton ListAllDogs  = new JButton("ListAllDogs");
//	private JButton ListBookings  = new JButton("ListBookings");



	public JDBCMainWindowContentBookings( String aTitle)
	{	
		
		//setting up the GUI
		super(aTitle, false,false,false,false);
		setEnabled(true);

		initiate_db_conn();
		//add the 'main' panel to the Internal Frame

		content=getContentPane();
		content.setLayout(null);
		content.setBackground(Color.blue);
		lineBorder = BorderFactory.createEtchedBorder(15, Color.red, Color.black);

		//setup details panel and add the components to it
		detailsPanel=new JPanel();
		detailsPanel.setLayout(new GridLayout(11,2));
		detailsPanel.setBackground(Color.lightGray);
		detailsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "CRUD Actions"));

		detailsPanel.add(IDLabel);			
		detailsPanel.add(IDTF);
		detailsPanel.add(DateLabel);			
		detailsPanel.add(DateTF);
		detailsPanel.add(TimeLabel);			
		detailsPanel.add(TimeTF);
//		detailsPanel.add(FirstNameLabel);		
//		detailsPanel.add(FirstNameTF);
//		detailsPanel.add(LastNameLabel);		
//		detailsPanel.add(LastNameTF);
//	
		
		//detailsPanel.add(PhoneNumberLabel);
		//detailsPanel.add(PhoneNumberTF);
		detailsPanel.add(DogNameLabel);
		detailsPanel.add(DogNameTF);

		

	

	
	

		//setup details panel and add the components to it
		exportButtonPanel=new JPanel();
		exportButtonPanel.setLayout(new GridLayout(3,2));
		exportButtonPanel.setBackground(Color.lightGray);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));
//		exportButtonPanel.add(NumLectures);
//		exportButtonPanel.add(NumLecturesTF);
//		exportButtonPanel.add(ListAllCustomers);
//		exportButtonPanel.add(ListAllDogs);
//		exportButtonPanel.add(ListBookings);
//		exportButtonPanel.add(avgAgeDepartmentTF);
//
		exportButtonPanel.add(ListAllBookings);
		exportButtonPanel.add(ListBookingsOnDate);
		exportButtonPanel.add(BookingsOnDateTF);
		exportButtonPanel.setSize(500, 200);
		exportButtonPanel.setLocation(3, 300);
		content.add(exportButtonPanel);

		insertButton.setSize(100, 30);
		updateButton.setSize(100, 30);
		exportButton.setSize (100, 30);
		deleteButton.setSize (100, 30);
		clearButton.setSize (100, 30);



		insertButton.setLocation(370, 10);
		updateButton.setLocation(370, 110);
		exportButton.setLocation (370, 160);
		deleteButton.setLocation (370, 60);
		clearButton.setLocation (370, 210);


		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		exportButton.addActionListener(this);
		deleteButton.addActionListener(this);
		clearButton.addActionListener(this);
	
//
//		this.ListAllCustomers.addActionListener(this);
//		this.ListAllDogs.addActionListener(this);
//		this.ListBookings.addActionListener(this);
//		this.NumLectures.addActionListener(this);
		this.ListAllBookings.addActionListener(this);
		this.ListBookingsOnDate.addActionListener(this);

		content.add(insertButton);
		content.add(updateButton);
		content.add(exportButton);
		content.add(deleteButton);
		content.add(clearButton);
		
	


		TableofDBContents.setPreferredScrollableViewportSize(new Dimension(900, 300));

		dbContentsPanel=new JScrollPane(TableofDBContents,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dbContentsPanel.setBackground(Color.lightGray);
		dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder,"Database Content"));

		detailsPanel.setSize(360, 300);
		detailsPanel.setLocation(3,0);
		dbContentsPanel.setSize(700, 300);
		dbContentsPanel.setLocation(477, 0);

		content.add(detailsPanel);
		content.add(dbContentsPanel);

		setSize(982,645);
		setVisible(true);

		TableModel.refreshFromDB(stmt);
	}

	public void initiate_db_conn()
	{
		try
		{
			// Load the JConnector Driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Specify the DB Name
            String url="jdbc:mysql://localhost:3306/db4_2024";
			
			// Connect to DB using DB URL, Username and password
			con = DriverManager.getConnection(url, "root", "");
			
			//Create a generic statement which is passed to the TestInternalFrame1
			stmt = con.createStatement();
		}
		catch(Exception e)
		{
			System.out.println("Error: Failed to connect to database\n"+e.getMessage());
		}
	}

	//event handling 
	public void actionPerformed(ActionEvent e)
	{
		Object target=e.getSource();
		if (target == clearButton)
	{
			IDTF.setText("");
			FirstNameTF.setText("");			
			LastNameTF.setText("");
			
			PhoneNumberTF.setText("");
			DogNameTF.setText("");
			
			

		}

		if (target == insertButton)
		{		 
			
			try
			{
				//check if there is a dog with the same owner and name
				String checkDogExists = "SELECT COUNT(*) FROM Dog WHERE id = ? AND dogName = ?";
				PreparedStatement checkStmt = con.prepareStatement(checkDogExists);
				checkStmt.setString(1, IDTF.getText());    
				checkStmt.setString(2, DogNameTF.getText());  
				ResultSet rs = checkStmt.executeQuery();

				//if there is a dog with the name and owner id
				if (rs.next() && rs.getInt(1) > 0) {
				   
				    String insertBooking = "INSERT INTO Booking (id, dog_name, Date, Time) VALUES (?, ?, ?, ?)";
				    PreparedStatement pstmt = con.prepareStatement(insertBooking);
				    pstmt.setString(1, IDTF.getText());
				    pstmt.setString(2, DogNameTF.getText());
				    pstmt.setString(3, DateTF.getText());
				    pstmt.setString(4, TimeTF.getText());
				    pstmt.executeUpdate();
				   
				}
				
				
		        
			       
		   
	   
		
			}
			catch (SQLException sqle)
			{
				String message = sqle.getMessage();
				if (message.contains("Cannot insert a null value")) {
					System.err.println("Cannot insert a null value");
				}else {
					System.err.println("Error with  insert:\n"+sqle.toString());
				}
				
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
			}
		}
		
		if (target == deleteButton)
		{		 
			try
			{
				

				
	
				String updateTemp = "delete from booking where id = ? AND Date = ? AND dog_name = ? ";
				PreparedStatement pstmt = con.prepareStatement(updateTemp);
				pstmt.setString(1, IDTF.getText());
				pstmt.setString(2, DateTF.getText());
				pstmt.setString(3, DogNameTF.getText());
				
				pstmt.executeUpdate();
				

			}
			catch (SQLException sqle)
			{
				System.err.println("Error with  delete:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
			}
		}
		
		if (target == updateButton)
		{		 
			try
			{
				String updateTemp = "UPDATE Booking SET Date = '" + DateTF.getText() + 
		                   "', Time = '" + TimeTF.getText() + 
		                   "', dog_name = '" + DogNameTF.getText() + 
		                   "' WHERE id = " + IDTF.getText();
				
				
		        
			       
		        stmt.executeUpdate(updateTemp);

			
				
						
				
			

			}
			catch (SQLException sqle)
			{
				System.err.println("Error with  Update:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
			}
		}
		
		




		
		if(target == this.exportButton){

			cmd = "select * from booking";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		if(target == this.ListAllBookings){

			cmd = "select  * from booking";
		

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		
		if(target == this.ListBookingsOnDate){

			cmd = "select * from booking where date = '"+ BookingsOnDateTF.getText()+"'";
			

			
					
			
		

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}

		

	}
	///////////////////////////////////////////////////////////////////////////

	private void writeToFile(ResultSet rs){
		try{
			System.out.println("In writeToFile");
			FileWriter outputFile = new FileWriter("MyOutput.csv");
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for(int i=0;i<numColumns;i++){
				printWriter.print(rsmd.getColumnLabel(i+1)+",");
			}
			printWriter.print("\n");
			while(rs.next()){
				for(int i=0;i<numColumns;i++){
					printWriter.print(rs.getString(i+1)+",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}
