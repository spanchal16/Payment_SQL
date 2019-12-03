import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PaymentManagement 
{

	void reconcilePayments(Connection database)
	{
		try
		{


			// createStatement() will create the statement
			Statement st = database.createStatement();

			// execute() will execute the SQL statement	
			st.execute("use spanchal;");

			// creating SQL query to map the orderNumber with payments and find the sum of it
			String sql = "select temp.customerNumber, temp.orderNumber, temp.Sum as Total_Sum, p.checkNumber, p.amount from\r\n" + 
					"(select od.orderNumber, o.customerNumber, SUM(od.quantityOrdered * od.priceEach) as Sum from orderdetails od\r\n" + 
					"natural join orders o group by orderNumber) as temp inner join payments p \r\n" + 
					"on temp.customerNumber = p.customerNumber where Sum = p.amount or p.amount is null";

			// ResultSet will hold all the data retrieved
			ResultSet rs1 = st.executeQuery(sql);

			rs1.close();
			st.close();

		}

		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	public Boolean payOrder(Connection database,float amount, String cheque_number, ArrayList<Integer>orders) 
	{
		Boolean result = true;
		try
		{
			// createStatement() will create the statement
			Statement st = database.createStatement();

			// execute() will execute the SQL statement	
			st.execute("use spanchal;");


			String sql ="select temp.customerNumber, temp.orderNumber, temp.Sum, p.checkNumber, p.amount from\r\n" + 
							"(select od.orderNumber, o.customerNumber, SUM(od.quantityOrdered * od.priceEach) as Sum from orderdetails od\r\n" + 
							"natural join orders o group by orderNumber) as temp inner join payments p \r\n" + 
							"on temp.customerNumber = p.customerNumber where Sum = p.amount or p.amount is null";

			// ResultSet will hold all the data retrieved
			ResultSet rs1 = st.executeQuery(sql);

			ResultSet rs2 = rs1;


			// for loop is used to iterate through all the orders in arraylist
			for(int i=0; i<orders.size(); i++)
			{
				// while loop is used to get orderNumber one after another
				while(rs1.next())
				{

					int temp = rs1.getInt("orderNumber");
					
					// Checks if the orderNumber in arraylist matches the orderNumber in resultset
					// if it matches then return false or willl be true
					if(orders.get(i) == temp)
					{

						result = false;
						rs1.first();
						break;
					}

					else
					{
						result = true;
					}


				}
				rs1.first();

				if(!result) {
					break;
				}

			}




		}


		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return result;


	}


	public ArrayList<Integer> unpaidOrders(Connection database) 
	{
		try
		{


			// createStatement() will create the statement
			Statement st = database.createStatement();

			// execute() will execute the SQL statement	
			st.execute("use spanchal;");

			
			String sql1 = "create table MyPaysOrdersMy as\r\n" + 
					"select temp.customerNumber, temp.orderNumber, temp.Sum, p.checkNumber, p.amount from\r\n" + 
					"(select od.orderNumber, o.customerNumber, SUM(od.quantityOrdered * od.priceEach) as Sum from orderdetails od\r\n" + 
					"natural join orders o group by orderNumber) as temp inner join payments p \r\n" + 
					"on temp.customerNumber = p.customerNumber where Sum = p.amount or p.amount is null";
			st.executeUpdate(sql1);

			// creating sql query which will select those orderNumber who have not paid the amount
			String sql = "select orderNumber from orders where orderNumber not in (select orderNumber from MyPaysOrdersMy )";

			// ResultSet will hold all the data retrieved
			ResultSet rs1 = st.executeQuery(sql);
			ArrayList<Integer> myArrList12 = new ArrayList<Integer>();

			while(rs1.next())
			{

				myArrList12.add(rs1.getInt("orderNumber"));
			}
			String sql33 = "drop table MyPaysOrdersMy";
			st.executeUpdate(sql33);
			return myArrList12;

		}

		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}




		return null;
	}


	public ArrayList<String> unknownPayments(Connection database) 
	{
		ArrayList<String> myArrList123 = new ArrayList<String>();

		try
		{
			// createStatement() will create the statement
			Statement st = database.createStatement();

			// execute() will execute the SQL statement	
			st.execute("use spanchal;");

			String sql1 = "create table MyPaysOrdersMy as\r\n" + 
					"select temp.customerNumber, temp.orderNumber, temp.Sum, p.checkNumber, p.amount from\r\n" + 
					"(select od.orderNumber, o.customerNumber, SUM(od.quantityOrdered * od.priceEach) as Sum from orderdetails od\r\n" + 
					"natural join orders o group by orderNumber) as temp inner join payments p \r\n" + 
					"on temp.customerNumber = p.customerNumber where Sum = p.amount or p.amount is null";
			st.executeUpdate(sql1);

			// creating sql query which will display checkNumber which are unknown payment 
			String sql = "select checkNumber from payments where checkNumber not in (select checkNumber from MyPaysOrdersMy ) ";

			// ResultSet will hold all the data retrieved
			ResultSet rs1 = st.executeQuery(sql);

			while(rs1.next())
			{

				myArrList123.add(rs1.getString(1));
			}

			return myArrList123;


		}

		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return myArrList123;


	}

}
