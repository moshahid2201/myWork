package com.bigdata.analysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

import javax.sql.RowSet;
import javax.sql.rowset.JdbcRowSet;

import archive.DataSetType;
import archive.UniversalItem;
import archive.UniversalLoader;
import archive.YorkData;
import archive.YorkDataItem;

import com.bigdata.data.OSData;
import com.bigdata.data.OSDataSingle;
import com.bigdata.utility.Common;
import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.JdbcRowSetImpl;

public class Dataset {
	private final Logger log = Logger
			.getLogger(UniversalLoader.class.getName());

	private YorkData yd;
	private OSData os;
	private UniversalLoader loader;
	private String area;

	private Random r;

	public Dataset() {
		r = new Random();
	}

	public double getHeight(String area, int easting, int northing) {
		int sq = Common.getSquareSymbolInt(easting, northing);
		// System.out.println("squre value"+sq);
		if (os.getData()[sq] != null) {
			double[][] d = os.getData()[sq].getData();
			int eID = (int) (Math.round(easting % 10000) / 50.0);
			int nID = 199 - (int) (Math.round(northing % 10000) / 50.0);

			double h = d[nID][eID];

			
			return h;
		}

		return -1;
	}

	public ArrayList<UniversalItem> getDataset(DataSetType t) {
		switch (t) {
		case BATTLEFIELDS:
			return loader.getdBattlefields();
		case IMMUNITY:
			return loader.getdImmunity();
		case PARKS:
			return loader.getdParks();
		case MONUMENTS:
			return loader.getdMonuments();
		case BUILDINGS:
			return loader.getdBuildings();
		case HERITAGE_SITES:
			return loader.getdHeritageSites();
		}

		return null;
	}

	public double countObjectsWithinRadius(DataSetType t, String area,
			int easting, int northing, int radius) {
		int count = 0;
		ArrayList<UniversalItem> d = getDataset(t);

		if (d != null) {
			for (UniversalItem i : d) {
				if (area.equals(i.getMcode()))

				{
					double e = i.getEasting();
					double n = i.getNorthing();

					double dist = Common.distance(e, n, easting, northing);

					if (dist < radius)
						count++;
				}
			}
		}

		return count;
	}

	public double[] getMinDistanceFromPlace(JdbcRowSet rs, String area,
			int easting, int northing, int radius) throws Exception {

		double min = 9999999;
		double returnValues[] = new double[2];
		double count = 0;

		while (rs.next()) {
			if (area.equals(rs.getString("m_code"))) {
				double e = rs.getDouble("easting");

				double n = rs.getDouble("northing");

				double dist = Common.distance(e, n, easting, northing);

				if (dist < min)
					min = dist;

				if (dist < radius)
					count++;
			}
		}

		returnValues[0] = min;
		returnValues[1] = count;

		return returnValues;
	}

	public void createTrainingSet(Statement stmt, Connection cn,
			JdbcRowSet parks, JdbcRowSet monuments, JdbcRowSet buildings)
			throws Exception {
		BufferedWriter writer = null;

		File output = new File("train_" + area + ".csv");

		int counter = 1;

		try {
			writer = new BufferedWriter(new FileWriter(output));
			writer.write("arch,height,distM,distP,distB,countM,countP,countB,e,n,lat,lon\n");
			// YORK DATA SET
			String sql = "select * from bigdatagroupproject.NGRDetailDimension";
			ResultSet RS = stmt.executeQuery(sql);

			while (RS.next())
			// for (YorkDataItem s : yd.getData())
			{
				if (RS.getString("NGRCode").equalsIgnoreCase(area)) {
					double height = getHeight(area, RS.getInt("Easting"),
							RS.getInt("Northing"));

					System.out.println(height);

					// two values are returned ,
					// at 0th index is the min distance
					// at 1st index is the counter
					double dist_n_CountM[] = getMinDistanceFromPlace(monuments,
							area, Math.round(RS.getFloat("Easting")),
							Math.round(RS.getFloat("Northing")), 5000);
					double dist_n_CountP[] = getMinDistanceFromPlace(parks,
							area, Math.round(RS.getFloat("Easting")),
							Math.round(RS.getFloat("Northing")), 5000);
					double dist_n_CountB[] = getMinDistanceFromPlace(buildings,
							area, Math.round(RS.getFloat("Easting")),
							Math.round(RS.getFloat("Northing")), 5000);

					writer.write(0 + "," + height + "," + dist_n_CountM[0]
							+ "," + dist_n_CountP[0] + "," + dist_n_CountB[0]
							+ "," + dist_n_CountM[1] + "," + dist_n_CountP[1]
							+ "," + dist_n_CountB[1] + ","
							+ Math.round(RS.getFloat("Easting")) + ","
							+ Math.round(RS.getFloat("Northing")) + ","
							+ RS.getFloat("latitude") + ","
							+ RS.getFloat("longitude") + "\n");

				}
				counter++;
			}
			RS.close();

			log.info("Output saved to " + output.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public ArrayList<EastingNorthingPoint> getAllPoints(int squareID,
			double baseX, double baseY) {
		ArrayList<EastingNorthingPoint> points = new ArrayList<EastingNorthingPoint>();

		// int easting = os.getEastingModifier(s.getIterator()) +

		int jump = 50;

		for (int i = jump / 2; i < 200; i += jump) {
			for (int j = jump / 2; j < 200; j += jump) {
				// System.out.println("SQUAREID: " + squareID);
				int e = (int) Common.shiftFromCorner(baseX, i, 50);
				int n = (int) Common.shiftFromCorner(baseY, j, 50);
				String eastingMod = String.valueOf(os
						.getEastingModifier(squareID));
				String northingMod = String.valueOf(os
						.getNorthingModifier(squareID));
				String easting = eastingMod + String.valueOf(j * 50);
				String northing = northingMod + String.valueOf(i * 50);
				double[] latlon = Common.enToLatLon(String.valueOf(e),
						String.valueOf(n));

				// System.out.println(eastingMod + " " + northingMod + " " +
				// easting + " " + northing);

				EastingNorthingPoint p = new EastingNorthingPoint(
						Integer.valueOf(easting), Integer.valueOf(northing), e,
						n, latlon[0], latlon[1]);
				points.add(p);
			}
		}

		return points;
	}

	public void createTestSet(JdbcRowSet parks, JdbcRowSet monuments,
			JdbcRowSet buildings) {
		BufferedWriter writer = null;

		File output = new File("test_" + area + ".csv");

		int counter = 1;

		try {
			writer = new BufferedWriter(new FileWriter(output));
			writer.write("e,n,lat,lon,height,distM,distP,distB,countP\n");

			// for (YorkDataItem s : yd.getData())
			// int it = 0;
			for (OSDataSingle s : os.getData()) {
				if (s != null) {

					System.out.println("Item: " + counter + " / "
							+ os.getData().length + " ("
							+ Math.round((float) counter / os.getData().length)
							+ "%)");

					ArrayList<EastingNorthingPoint> points = getAllPoints(
							counter - 1, s.getXcorner(), s.getYcorner());

					

					// System.exit(0);

					for (EastingNorthingPoint p : points) {
						
						double height = getHeight(area, p.getEasting(),
								p.getNorthing());
						
						double distM_n_Count[] = getMinDistanceFromPlace(
								monuments, area, p.getEasting(),
								p.getNorthing(), 5000);
						double distP_n_Count[] = getMinDistanceFromPlace(parks,
								area, p.getEasting(), p.getNorthing(), 5000);
						double distB_n_Count[] = getMinDistanceFromPlace(
								buildings, area, p.getEasting(),
								p.getNorthing(), 5000);

						writer.write(p.getE() + "," + p.getN() + ","
								+ p.getLat() + "," + p.getLon() + "," + height
								+ "," + distM_n_Count[0] + ","
								+ distP_n_Count[0] + "," + distB_n_Count[0]
								+ "," + distP_n_Count[1] + "\n");
					}
				}
				counter++;

			}

			log.info("Test output saved to " + output.getCanonicalPath());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}

	public void setArea(String p_area) {
		area = p_area;
	}

	public void setOSData(OSData p_s) {
		os = p_s;
	}

	public void setYorkData(YorkData p_d) {
		yd = p_d;
	}

	public void setLoader(UniversalLoader p_l) {
		loader = p_l;
	}
}
