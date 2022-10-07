package stream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class atm {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception{
		Scanner sc=new Scanner(System.in);
		Class.forName("com.mysql.jdbc.Driver");
		Connection c=DriverManager.getConnection("jdbc:mysql://localhost:3306/Mohan","root","root");
		Statement st= c.createStatement();
		ResultSet rs;
		int t = 0;
		
		System.out.println("1. Load cash to ATM");
		System.out.println("2. Show customer details");
		System.out.println("3. Show ATM operations");
		System.out.println("4. Check ATM Balance");
		int choice=sc.nextInt();
		switch(choice) {
		case 1:System.out.println("Enter Denominations:");
			   System.out.println("2000 = ");
			   int n=sc.nextInt();
			   System.out.println("500 = ");
			   int n1=sc.nextInt();
			   System.out.println("100 = ");
			   int n2=sc.nextInt();
			   rs=st.executeQuery("Select count(*) from atm");
			   int x=0;
			   while(rs.next()) {
				   x=rs.getInt(1);
			   }
				   if(x==0) {
					   st.executeUpdate("Insert into atm values(2000,"+n+","+(2000*n)+")");
					   st.executeUpdate("Insert into atm values(500,"+n1+","+(500*n1)+")");
					   st.executeUpdate("Insert into atm values(100,"+n2+","+(100*n2)+")");
				   }
				   else {
					   st.executeUpdate("update atm set number=number+"+n+" where denomination=2000");
					   st.executeUpdate("update atm set number=number+"+n1+" where denomination=500");
					   st.executeUpdate("update atm set number=number+"+n2+" where denomination=100");
					   st.executeUpdate("update atm set value=value+"+(n*2000)+" where denomination=2000");
					   st.executeUpdate("update atm set value=value+"+(n1*500)+" where denomination=500");
					   st.executeUpdate("update atm set value=value+"+(n2*100)+" where denomination=100");

				   }
				   System.out.println("Amount Entered Successfully");
		break;
		case 2:
	          rs=st.executeQuery("Select * from customers");
		      System.out.println("AccNo AccHolder PinNo AccBalance");
		      System.out.println();
		      while(rs.next()) {
			    System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"   "+rs.getInt(3)+"   "+rs.getInt(4));
		     }
		break;
		case 3:
			int total=0;
			rs=st.executeQuery("Select value from atm");
			while(rs.next()) 
			total+=rs.getInt(1);
			System.out.println("1. Check Balance");
			System.out.println("2. Withdraw Money");
			System.out.println("3. Transfer Money");
			int choice1=sc.nextInt();
			@SuppressWarnings("unused")
			int accno=0,pinno=0,accbal=0,amount=0,acno=0,amount1=0,no1=0;
			String name="";
			boolean a=true;
			switch(choice1) {
			case 1:System.out.println("Enter your account number:");
				   acno=sc.nextInt();
				   rs=st.executeQuery("Select count(*) from customers where acc_no="+acno);
				   while(rs.next()) {
					if(rs.getInt(1)==0)
						a=false;
				   }
				   if(!a) System.out.println("Account number not available , try again !");
				   else {
				   rs=st.executeQuery("select * from customers where acc_no="+acno);
				   while(rs.next()) {
					accno=rs.getInt(1);
					name=rs.getString(2);
					pinno=rs.getInt(3);
					accbal=rs.getInt(4);
				}
				System.out.println("Enter your pin number : ");
				int pin=sc.nextInt();
				if(pin==pinno) {
					System.out.println("Fetching Account details");
					System.out.println("Account Number : "+accno);
					System.out.println("Account Holder Name : "+name);
					System.out.println("Account Balance : Rs."+accbal);
				}
				else {
					System.out.println("Account number and Pin Number does not match , try again !");
				}
				}
				break;
			case 2:
				
					System.out.println("Enter your account number : ");
					acno=sc.nextInt();
					rs=st.executeQuery("Select count(*) from customers where acc_no="+acno);
					while(rs.next()) {
						if(rs.getInt(1)==0)
							a=false;
					}
					if(!a) System.out.println("Account number not available,try again.");
					else {
					rs=st.executeQuery("select * from customers where acc_no="+acno);
					while(rs.next()) {
						accno=rs.getInt(1);
						name=rs.getString(2);
						pinno=rs.getInt(3);
						accbal=rs.getInt(4);
					}
					System.out.println("Enter your pin number:");
					int pin=sc.nextInt();
					if(pin==pinno) {
						System.out.println("Enter amount to be withdrawn:");
						amount=sc.nextInt();
						int temp=0,temp1=amount;
						if((amount>10000 || amount<100) && amount%100==0 )
							System.out.println("withdrawl range of amount should be between 100 to 100000");
						else if(amount>accbal)
							System.out.println("Account balance is lower than the entered withdrawal amount");
						else if(amount>total)
							System.out.println("ATM does not have enough money to vend");
						else {
							while(amount>3000) {
									amount1+=2000;
									st.executeUpdate("update customers set acc_bal=acc_bal-2000 where acc_no="+accno);
									st.executeUpdate("update atm set number=number-1 where denomination=2000");
									st.executeUpdate("update atm set value=value-2000 where denomination=2000");
									amount-=2000;
									temp+=2000;
							}
							while(amount>1000) {
									amount1+=500;
									st.executeUpdate("update customers set acc_bal=acc_bal-500 where acc_no="+accno);
									st.executeUpdate("update atm set number=number-1 where denomination=500");
									st.executeUpdate("update atm set value=value-500 where denomination=500");
									amount-=500;
									temp+=500;
							}
							while(amount>0) {
									no1+=100;
									amount-=100;
									temp+=100;
							}
								amount1+=no1;
								st.executeUpdate("update customers set acc_bal=acc_bal-"+no1+" where acc_no="+accno);
								st.executeUpdate("update atm set number=number-"+(no1/100) +" where denomination=100");
								st.executeUpdate("update atm set value=value-"+no1 +" where denomination=100");
								
						}
						System.out.println("Amount "+temp+" is withdrawn !");
						if(temp!=temp1) {
							System.out.println("Sorry!"+(temp1-temp)+" is not available in the ATM");
						}
					}
					else {
						System.out.println("AccNo and PinNo not matched,please try again.");
					}
				}
				break;
			case 3:
				System.out.println("Enter your account number:");
				acno=sc.nextInt();
				rs=st.executeQuery("Select count(*) from customers where acc_no="+acno);
				while(rs.next()) {
					if(rs.getInt(1)==0)
						a=false;
				}
				if(!a) System.out.println("Account number not available,enter the valid AccNo.");
				else {
				rs=st.executeQuery("select * from customers where acc_no="+acno);
				while(rs.next()) {
					accno=rs.getInt(1);
					name=rs.getString(2);
					pinno=rs.getInt(3);
					accbal=rs.getInt(4);
				}
				System.out.println("Enter your pin number : ");
				int pin=sc.nextInt();
				if(pin==pinno) {
					System.out.println("Enter the account number you want to transfer your money");
					int aacno=sc.nextInt();
					boolean b=true;
					rs=st.executeQuery("Select count(*) from customers where acc_no="+aacno);
					while(rs.next()) {
						if(rs.getInt(1)==0)
							b=false;
					}
					if(!a) System.out.println("Account number not available , try again !");
					else {
						System.out.println("Enter the amount to transfer:");
						int temp3=sc.nextInt();
						if(temp3>accbal) {
							System.out.println("Your Account Balance is too low for this transaction.");
						}
						else {
							st.executeUpdate("update customers set acc_bal=acc_bal-"+temp3+" where acc_no="+accno);
							st.executeUpdate("update customers set acc_bal=acc_bal+"+temp3+" where acc_no="+aacno);
							System.out.println("Amount transfered successfully.");
						}
					}
				}
				else {
					System.out.println("AccNo and PinNo not matched.");
				}
			}
				break;
			    default:System.out.println("Entered option is not valid.");	
		   }
			
		case 4:
			int total1=0;
			rs=st.executeQuery("Select * from atm");
			System.out.println("Denomination values:");
			while(rs.next()) {
				System.out.printf("%04d %03d %d\n",rs.getInt(1),rs.getInt(2),rs.getInt(3));
				total1+=rs.getInt(3);
			}
			System.out.println();
			System.out.println("Total amount in ATM = "+total1);
			break;
			default:System.out.println("Entered option is not valid.");
		}
		sc.close();
		
	
	}
}
