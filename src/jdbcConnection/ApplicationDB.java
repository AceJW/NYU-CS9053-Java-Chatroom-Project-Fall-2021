package jdbcConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ApplicationDB {
	
	public ApplicationDB(){
		
	}
	public Connection getConnection() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:server.db");
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);;
		}
		return connection;
	}
	
	public void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean isLogin(Connection connection, String userName, String password) {
		Statement statement = null;
		ResultSet resultSet =null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		String exeStr = "SELECT * FROM ClientInfo";
		try {
			resultSet = statement.executeQuery(exeStr);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   try {
			while (resultSet.next())
				if((resultSet.getString("username")).equals(userName) && (resultSet.getString("password")).equals(password)) {
					 System.out.println(resultSet.getString(1) + "\t" + resultSet.getString(2));
					return true;
				}
			 
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	   
	   try {
		statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   
	   try {
		   resultSet.close();
	   } catch (SQLException e) {
			e.printStackTrace();
	   }
		return false;
	}
	public int addAccount(Connection connection, String userName, String password) {
		if(isUserInTheTable(connection,userName) == false) {
			Statement statement = null;
			try {
				statement = connection.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
				System.exit(0);
			}			
			String exeStr = "INSERT INTO ClientInfo (username, password) VALUES('"+userName+"', '"+password+"')";
			try {
				statement.executeUpdate(exeStr);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(isUserInTheTable(connection,userName)==true) {
				return 1;//1 means account created successfully.
			}else {
				return 0;//create account failed.
			}
			
	   }else {
		   return 2;//2 means username is already existed.
	   }   
	}
	
	public boolean isUserInTheTable(Connection connection, String userName) {
		Statement statement = null;
		ResultSet resultSet =null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		String exeStr = "SELECT * FROM ClientInfo where username ='"+userName+"'";
		try {
			resultSet = statement.executeQuery(exeStr);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   try {
			while (resultSet.next())
				if((resultSet.getString("username")).equals(userName)) {
					return true;
				}
			 
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	   
	   try {
		   statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	   
	   try {
		   resultSet.close();
	   } catch (SQLException e) {
			e.printStackTrace();
	   }
		return false;
	}
	
	public void createMessageRecord(Connection connection, String userName) {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		String exeStr="CREATE TABLE messageRecord"+userName+" "+
					  "(id int PRIMARY KEY   NOT NULL," +
					  "status int,"+
					  "message varchar(1000),"+
					  "datetime datetime)";
		try {
				statement.executeUpdate(exeStr);
		} catch (SQLException e) {
				e.printStackTrace();
		}
		
		try {
			   statement.close();
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	public void insertMessageRecord(Connection connection, String username, String date, String message) {
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		String tableName="messageRecord"+username;
		//INSERT INTO `ts01` (`id`, `sender`, `recevier`, `status`, `message`, `datetime`) VALUES ((SELECT count(*) FROM ts01 as temtable)+1, 'Ace', 'Jack', '9', 'aaaa', '2021-01-02 00:00:03');
		
		String exeStr = "INSERT INTO "+tableName+" (id,status,message,datetime) VALUES((SELECT count(*) FROM "+tableName+" as temtable)+1, '1', '"+message+"', '"+date+"')";
		try {
			statement.executeUpdate(exeStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ArrayList<String> showHistoryRecord(Connection connection, String username) {
		String tableName = "messageRecord"+username;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<String> records = new ArrayList<String>();
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		String exeStr = "select * from  "+ tableName;
		try {
			resultSet = statement.executeQuery(exeStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while (resultSet.next()) {
				records.add(resultSet.getString(3));
				System.out.println(resultSet.getString(3));
			}
				
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		return records;	
	}
	public static void main(String[] args){
		ApplicationDB aDB01 = new ApplicationDB();
		
		Connection connection = aDB01.getConnection();
		
		boolean aa=aDB01.isLogin(connection,"root","123");
		if(aa == true) {
			System.out.println("Login Successfully: "+"root");
		}
	    try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	  }
}
