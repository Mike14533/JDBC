import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class JDBCMainWindowDog extends JFrame implements ActionListener
	{
		private JMenuItem exitItem;
		private JMenuItem bookingsItem;
		private JMenuItem customerItem;
		private JMenuItem DogItem;
		public JDBCMainWindowDog()
		{
			// Sets the Window Title
						super( "JDBC 2024 Assignment" ); 
						
						//Setup fileMenu and its elements
						JMenuBar menuBar=new JMenuBar();
						JMenu fileMenu=new JMenu("File");
						exitItem =new JMenuItem("Exit");
						bookingsItem = new JMenuItem("Bookings");
						customerItem = new JMenuItem("Customers");
						DogItem = new JMenuItem("Dogs");
						fileMenu.add(exitItem);
					
						menuBar.add(fileMenu );
						menuBar.add(bookingsItem );
						menuBar.add(customerItem );
						menuBar.add(DogItem );
						setJMenuBar(menuBar);
						
						// Add a listener to the Exit Menu Item
						exitItem.addActionListener(this);
						bookingsItem.addActionListener(this);
						customerItem.addActionListener(this);
						DogItem.addActionListener(this);

						// Create an instance of our class JDBCMainWindowContent 
						JDBCMainWindowContentDog aWindowContent = new JDBCMainWindowContentDog( "Dogs");
						// Add the instance to the main section of the window
						getContentPane().add( aWindowContent );
						
						setSize( 1200, 600 );
						setVisible( true );
		}
		
		
		
		
		
		
		
		// The event handling for the main frame
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource().equals(exitItem)){
				this.dispose();
			}
			else if(e.getSource().equals(bookingsItem)){
				this.dispose();
				new JDBCMainWindowBookings();
			}
			else if(e.getSource().equals(customerItem)){
				this.dispose();
				new JDBCMainWindow();
			}
			else if(e.getSource().equals(DogItem)){
				this.dispose();
				new JDBCMainWindowDog();
			}
				
			}
		}
		
		
		
	