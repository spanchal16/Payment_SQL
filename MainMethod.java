import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class MainMethod 
{

	public static void main(String[] args) 
	{
		/*
		 * username and password is stored in a variable as String which is used in getConnection() method
		 */
		String username = "spanchal";
		String password = "B00828070";
		try
		{
			// Class.forName() is used to register the driver class
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// getConnection() will establish connection with database 
			Connection con = DriverManager.getConnection("jdbc:mysql://db.cs.dal.ca:3306?serverTimezone=UTC",
					username, password);
			
			// Create object of PaymentManagement class where the methods are made
			PaymentManagement pm = new PaymentManagement();
			
			// calling reconcilePayments
			pm.reconcilePayments(con);

			float amount = 10000;
			String cheque_number = "LJG12";

			// Creating ArrayList to display the order number and check number
			ArrayList<Integer> myArrList = new ArrayList<Integer>();
			/*
			myArrList.add(10100);
			myArrList.add(10101);
			myArrList.add(10102);
			myArrList.add(10103);
			myArrList.add(10342);
			 */
			
			// storing the true false in boolean variable myArrList1
			Boolean myArrList1 = pm.payOrder(con, amount, cheque_number, myArrList);

			// Checks if the value return is true or false
			if(myArrList1 == true)
			{
				System.out.println(true);
			}
			else
			{
				System.out.println(false);

			}
			/*
			System.out.println(pm.unpaidOrders(con));
			System.out.println(pm.unknownPayments(con));
			 */
		}
		
		// All the thrown exception from try block will be catch over here
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

}
