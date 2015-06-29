package com.bigdata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import javax.sql.rowset.JdbcRowSet;

import org.apache.commons.cli.ParseException;

import archive.DataSetType;
import archive.UniversalLoader;
import archive.YorkData;

import com.bigdata.analysis.Dataset;
import com.bigdata.data.OSData;
import com.bigdata.utility.Config;
import com.bigdata.utility.Parameters;
import com.sun.rowset.JdbcRowSetImpl;

public class Main {
	private static final Logger log = Logger.getLogger(Main.class.getName());
	static final String db = "jdbc:db2://bivm.ibm.com:51000/bigsql";
	static final String user = "bigsql";
	static final String pwd = "bigsql";

	public JdbcRowSet getResultSet(String dataName) throws Exception {
		// Statement stm=cn.createStatement();
		JdbcRowSet jdbcRs = new JdbcRowSetImpl();

		String sql = "select substr(ngrcode,1,2) m_code,easting,northing"
				+ " from bigdatagroupproject.osgb36facts facts "
				+ "join bigdatagroupproject.osgb36maineventdimension dm "
				+ "on eventcode=eventid where eventtype=" + "'" + dataName
				+ "'";

		// System.out.println(sql);
		jdbcRs.setCommand(sql); // set properties, and
		jdbcRs.setUrl("jdbc:db2://bivm.ibm.com:51000/bigsql"); // connect to
																// database
		jdbcRs.setUsername("bigsql");
		jdbcRs.setPassword("bigsql");
		jdbcRs.execute();

		// rs.close();

		// stm.close();
		return jdbcRs;
	}

	public JdbcRowSet fetchYork() throws Exception {
		JdbcRowSet jdbcRs = new JdbcRowSetImpl();

		String sql = "select * from bigdatagroupproject.NGRDetailDimension";

		// System.out.println(sql);
		jdbcRs.setCommand(sql); // set properties, and
		jdbcRs.setUrl("jdbc:db2://bivm.ibm.com:51000/bigsql"); // connect to
																// database
		jdbcRs.setUsername("bigsql");
		jdbcRs.setPassword("bigsql");
		jdbcRs.execute();
		return jdbcRs;

	}

	public static void main(String[] args) throws java.text.ParseException,
			Exception {
		Connection conn = null;
		Statement stmt = null;
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		conn = DriverManager.getConnection(db, user, pwd);
		stmt = conn.createStatement();
		System.out.println("Connected to the database.");
		String[] areas = new String[] {
				// 7
				"HP", "HT", "HU", "HW", "HX",
				"HY",
				"HZ",

				// 21
				"NA", "NB", "NC", "ND", "NF", "NG", "NH", "NJ", "NK", "NL",
				"NM", "NN", "NO", "NR", "NS", "NT", "NU", "NW", "NX", "NY",
				"NZ",

				// 1
				"OV",

				// 19
				"SC", "SD", "SE", "SH", "SJ", "SK", "SM", "SN", "SO", "SP",
				"SR", "SS", "ST", "SU", "SV", "SW", "SX", "SY", "SZ",

				// 8
				"TA", "TF", "TG", "TL", "TM", "TQ", "TR", "TV" };

		Parameters params = new Parameters(args);

		if (params.help()) {

			System.exit(0);
		}

		
		{
			String area = "";

			if (params.generateTrainingData() != null)
				area = params.generateTrainingData();
			else if (params.generateTestData() != null)
				area = params.generateTestData();

			log.info("Loading data in progress...");
			System.out.println("Loading data in progress...");

			OSData os = new OSData();

			Dataset d = new Dataset();
			d.setOSData(os);
			// log.info("Data loaded.");
			System.out.println("Data loaded.");
			area = "ALL";
			Main test = new Main();
			JdbcRowSet parks = (JdbcRowSet) test.getResultSet("ParkAndGardens");
			JdbcRowSet lbpRS = test.getResultSet("LBP");
			JdbcRowSet monRS = test.getResultSet("ScheduledMonuments");
			// JdbcRowSet yorkData=test.fetchYork();
			if (area.equals("ALL")) {
				for (String a : areas) {
					d.setArea(a);
					os.load(a);

					
					System.out.println("Creating training data set for " + a);
					d.createTrainingSet(stmt, conn, parks, monRS, lbpRS);
					d.createTestSet(parks, monRS, lbpRS);
	
				}
				os.load(area);

				if (params.generateTrainingData() != null) {
					log.info("Creating training data set for " + area);
					
				} else if (params.generateTestData() != null) {
					log.info("Creating test data set for: " + area);
					
				}
				// yorkData.close();
				parks.close();
				monRS.close();
				lbpRS.close();
			}
		}

	}
}