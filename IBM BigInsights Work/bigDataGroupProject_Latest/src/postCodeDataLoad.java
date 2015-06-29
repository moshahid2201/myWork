import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class postCodeDataLoad {

	/**
	 * @param args
	 */
	static final String db = "jdbc:db2://bivm.ibm.com:51000/bigsql";
	static final String user = "bigsql";
	static final String pwd = "bigsql";
	
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		// Register JDBC driver
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		conn = DriverManager.getConnection(db, user, pwd);
		stmt = conn.createStatement();
		System.out.println("Connected to the database.");
		File folder = new File("/home/biadmin/Desktop/data/CodePoints/CSV");
		File[] listOfFiles = folder.listFiles();
		String sql=new String();
		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		          
		    	   	  sql="load hadoop using file url " +
		    	  		"'sftp://biadmin:biadmin@bivm:22"+folder.getAbsolutePath()+"/"+listOfFiles[i].getName()+"'" +
		    	  		" with SOURCE PROPERTIES ('field.delimiter'=',') " +
		    	  		"into table bigdatagroupproject.postCode_Table ;";	
		    	  //System.out.println(sql);
		    	   	System.out.println("Loading file "+ listOfFiles[i].getName()); 
		    	   	boolean flag=  stmt.execute(sql);
		    	   	  System.out.println("Completed..");
		    	   	  System.out.println("-------------------------");
		    	   	  System.out.println();
		      } else if (listOfFiles[i].isDirectory()) {
		        System.out.println("Directory " + listOfFiles[i].getName());
		      }
		    }
		
		stmt.close();
		conn.close();
		System.out.println("Finsihed Successfully");

		
		

	}

}
