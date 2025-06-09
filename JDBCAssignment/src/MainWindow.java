import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainWindow extends JFrame implements ActionListener
	{
		private JMenuItem exitItem;
		private JMenuItem bookingsItem;
		private JMenuItem customerItem;
		private JMenuItem dogItem;

		public MainWindow()
		{
			// Sets the Window Title
			super( "JDBC 2024 Assignment" ); 
			
			//Setup fileMenu and its elements
			JMenuBar menuBar=new JMenuBar();
			JMenu fileMenu=new JMenu("File");
			exitItem =new JMenuItem("Exit");
			bookingsItem = new JMenuItem("Bookings");
			customerItem = new JMenuItem("Customers");
			dogItem = new JMenuItem("Dogs");
			fileMenu.add(exitItem);
		
			menuBar.add(fileMenu );
			menuBar.add(bookingsItem );
			menuBar.add(customerItem );
			menuBar.add(dogItem );
			setJMenuBar(menuBar);
			
			// Add a listener to the Exit Menu Item
			exitItem.addActionListener(this);
			bookingsItem.addActionListener(this);
			customerItem.addActionListener(this);
			dogItem.addActionListener(this);

			// Create an instance of our class JDBCMainWindowContent 
			JDBCMainWindowContent aWindowContent = new JDBCMainWindowContent( "Customers");
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
				new MainWindow();
			}
			else if(e.getSource().equals(dogItem)){
				this.dispose();
				new JDBCMainWindowDog();
			}
				
			}
		}
		
		
		
	