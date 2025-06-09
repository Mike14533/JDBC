import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;
@SuppressWarnings("serial")
public class JDBCMainWindowContentDog extends JInternalFrame implements ActionListener
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
	private JLabel DogNameLabel=new JLabel("Dog's Name:        ");
	private JLabel DogSizeLabel=new JLabel("Dog's Size:                 ");
	private JLabel DogBreedLabel=new JLabel("Dog's Breed:        ");


	private JTextField IDTF= new JTextField(10);
	
	

	private JTextField DogNameTF=new JTextField(10);
	private JTextField DogSizeTF=new JTextField(10);
	private JTextField DogBreedTF=new JTextField(10);
	private JButton ListAllDogs  = new JButton("ListAllDogs");
	private JButton ListAllSmallDogs  = new JButton("ListAllSmallDogs");
	private JButton ListAllMediumDogs  = new JButton("ListAllMediumDogs");
	private JButton ListAllLargeDogs  = new JButton("ListAllLargeDogs");



	private static QueryTableModelDogs TableModel = new QueryTableModelDogs();
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



	public JDBCMainWindowContentDog( String aTitle)
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
//		detailsPanel.add(DateLabel);			
//		detailsPanel.add(DateTF);
//		detailsPanel.add(TimeLabel);			
//		detailsPanel.add(TimeTF);
//		detailsPanel.add(FirstNameLabel);		
//		detailsPanel.add(FirstNameTF);
//		detailsPanel.add(LastNameLabel);		
//		detailsPanel.add(LastNameTF);
//	
		
		//detailsPanel.add(PhoneNumberLabel);
		//detailsPanel.add(PhoneNumberTF);
		detailsPanel.add(DogNameLabel);
		detailsPanel.add(DogNameTF);
		detailsPanel.add(DogSizeLabel);
		detailsPanel.add(DogSizeTF);
		detailsPanel.add(DogBreedLabel);
		detailsPanel.add(DogBreedTF);

		

	

	
	

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
		exportButtonPanel.add(ListAllDogs);
		exportButtonPanel.add(ListAllSmallDogs);
		exportButtonPanel.add(ListAllMediumDogs);
		exportButtonPanel.add(ListAllLargeDogs);
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
		this.ListAllDogs.addActionListener(this);
		this.ListAllSmallDogs.addActionListener(this);
		this.ListAllMediumDogs.addActionListener(this);
		this.ListAllLargeDogs.addActionListener(this);

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
			
			DogNameTF.setText("");
			DogSizeTF.setText("");
			DogBreedTF.setText("");		

		}

		if (target == insertButton)
		{		 
			try
			{String updateTemp ="INSERT INTO dog VALUES("+
					IDTF.getText()+", '"+DogNameTF.getText()+"', '"+DogSizeTF.getText()+"', '"+DogBreedTF.getText()+"');";

			
			 
		        
		       
		        stmt.executeUpdate(updateTemp);
		   
		
			}
			catch (SQLException sqle)
			{
				//Message from the SQL 
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
				

				
	
				String updateTemp = "delete from dog where id = ? AND dogName = ? ";
				PreparedStatement pstmt = con.prepareStatement(updateTemp);
				pstmt.setString(1, IDTF.getText());
				pstmt.setString(2, DogNameTF.getText());
						
				
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
				
				
						
				String updateTemp = "UPDATE Dog SET dogName = '" + DogNameTF.getText() + 
		                   "', Size = '" + DogSizeTF.getText() + 
		                   "', Breed = '" + DogBreedTF.getText() + 
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
		
		



		/////////////////////////////////////////////////////////////////////////////////////
		//I have only added functionality of 2 of the button on the lower right of the template
		///////////////////////////////////////////////////////////////////////////////////

//		if(target == this.ListAllCustomers){
//
//			cmd = "select distinct firstName from Customer;";
//
//			try{					
//				rs= stmt.executeQuery(cmd); 	
//				writeToFile(rs);
//			}
//			catch(Exception e1){e1.printStackTrace();}
//
//		}
		
		if(target == this.exportButton){

			cmd = "select  * from dog;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		if(target == this.ListAllDogs){

			cmd = "select distinct dogname from dog;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		
		if(target == this.ListAllSmallDogs){

			cmd = "select distinct dogname as SmallDogs from dog where size like 'small';";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		
		if(target == this.ListAllMediumDogs){

			cmd = "select distinct dogname as MediumDogs from dog where size like 'medium';";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		
		if(target == this.ListAllLargeDogs){

			cmd = "select distinct dogname as LargeDogs from dog where size like 'large';";

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
