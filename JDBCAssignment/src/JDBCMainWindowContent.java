import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
@SuppressWarnings("serial")
public class JDBCMainWindowContent extends JInternalFrame implements ActionListener
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
	private JLabel LastNameLabel=new JLabel("LastName:      ");
	private JLabel EmailAddressLabel=new JLabel("Email Address:        ");
	private JLabel AddressLabel=new JLabel("Address:                 ");
	private JLabel PhoneNumberLabel=new JLabel("Phone Number:               ");
	private JLabel DogNameLabel=new JLabel("Dog's Name:        ");
	private JLabel SizeLabel=new JLabel("Dog's Size:                 ");
	private JLabel BreedLabel=new JLabel("Dog's Breed:               ");

	private JTextField IDTF= new JTextField(10);
	private JTextField FirstNameTF=new JTextField(10);
	private JTextField LastNameTF=new JTextField(10);
	private JTextField EmailAddressTF=new JTextField(10);
	private JTextField AddressTF=new JTextField(10);
	private JTextField PhoneNumberTF=new JTextField(10);
//	private JTextField DogNameTF=new JTextField(10);
//	private JTextField SizeTF=new JTextField(10);
//	private JTextField BreedTF=new JTextField(10);



	private static QueryTableModel TableModel = new QueryTableModel();
	//Add the models to JTabels
	private JTable TableofDBContents=new JTable(TableModel);
	//Buttons for inserting, and updating members
	//also a clear button to clear details panel
	private JButton updateButton = new JButton("Update");
	private JButton insertButton = new JButton("Insert");
	private JButton exportButton  = new JButton("Export");
	private JButton deleteButton  = new JButton("Delete");
	private JButton clearButton  = new JButton("Clear");
	



	private JButton ListAllCustomers  = new JButton("ListAllCustomers");
	private JButton ListNumberOfCustomers  = new JButton("ListNumberOfCustomers");
//	private JButton ListAllDogs  = new JButton("ListAllDogs");




	public JDBCMainWindowContent( String aTitle)
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
		detailsPanel.add(FirstNameLabel);		
		detailsPanel.add(FirstNameTF);
		detailsPanel.add(LastNameLabel);		
		detailsPanel.add(LastNameTF);
		detailsPanel.add(EmailAddressLabel);	
		detailsPanel.add(EmailAddressTF);
		detailsPanel.add(AddressLabel);		
		detailsPanel.add(AddressTF);
		detailsPanel.add(PhoneNumberLabel);
		detailsPanel.add(PhoneNumberTF);
//		detailsPanel.add(DogNameLabel);
//		detailsPanel.add(DogNameTF);
//		detailsPanel.add(SizeLabel);
//		detailsPanel.add(SizeTF);
//		detailsPanel.add(BreedLabel);
//		detailsPanel.add(BreedTF);
	

		//setup details panel and add the components to it
		exportButtonPanel=new JPanel();
		exportButtonPanel.setLayout(new GridLayout(3,2));
		exportButtonPanel.setBackground(Color.lightGray);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));
		
		exportButtonPanel.add(ListAllCustomers);
		exportButtonPanel.add(ListNumberOfCustomers);
		//exportButtonPanel.add(ListAllDogs);
	

		//exportButtonPanel.add(ListAllDogs);
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

	

		this.ListAllCustomers.addActionListener(this);
		this.ListNumberOfCustomers.addActionListener(this);
		//this.ListAllDogs.addActionListener(this);



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
			EmailAddressTF.setText("");
			AddressTF.setText("");
			PhoneNumberTF.setText("");

			

		}

		if (target == insertButton)
		{		 
			try
			{
				String updateTemp ="INSERT INTO Customer(firstName, lastName, emailAddress, Address, phoneNumber)"+ " VALUES('"+FirstNameTF.getText()+"', '" + LastNameTF.getText()+"', '"+EmailAddressTF.getText()+"', '"+AddressTF.getText()+"', '"+PhoneNumberTF.getText()+"');";
			
	
				
		        
		       
		        stmt.executeUpdate(updateTemp);
		  
		   
		
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
				
				
	
				String updateTemp = "delete from customer where id ="+IDTF.getText() ;

						
				
				stmt.executeUpdate(updateTemp);
				updateTemp = "delete from dog where id ="+IDTF.getText() ;
				stmt.executeUpdate(updateTemp);

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
			try			{
				String updateTemp = "UPDATE Customer SET firstName = '" + FirstNameTF.getText() + 
		                   "', lastName = '" + LastNameTF.getText() + 
		                   "', emailAddress = '" + EmailAddressTF.getText() +
		                   "', Address = '" + AddressTF.getText() +
		                   "', phoneNumber = '" + PhoneNumberTF.getText() +
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

			cmd = "SELECT * from customer";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}


		/////////////////////////////////////////////////////////////////////////////////////
		//I have only added functionality of 2 of the button on the lower right of the template
		///////////////////////////////////////////////////////////////////////////////////

		if(target == this.ListAllCustomers){

			cmd = "select distinct id, firstName, lastName from Customer;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		
		if(target == this.ListNumberOfCustomers){

			cmd = "select COUNT(*) as NoOfCustomers From Customer;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		
//		if(target == this.ListAllDogs){
//
//			cmd = "select distinct dogname from dog;";
//
//			try{					
//				rs= stmt.executeQuery(cmd); 	
//				writeToFile(rs);
//			}
//			catch(Exception e1){e1.printStackTrace();}
//
//		}
		
	

		

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
