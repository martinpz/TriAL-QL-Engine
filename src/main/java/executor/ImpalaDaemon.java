/*
* Copyright (C) 2017 University of Freiburg.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* The TriAL-QL Engine is a research project at the department of 
* computer science, University of Freiburg. 
*
* More information on the project:
* http://dbis.informatik.uni-freiburg.de/forschung/projekte/DiPoS/
* zablocki@informatik.uni-freiburg.de
*/

package executor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ImpalaDaemon {

	public static final String impalaConnUrl = "";
	private static Connection objConnection = null;

	public static ResultSet main(String sqlStatement) {

		ResultSet rs = null;

		System.out.println("\n=============================================");
		System.out.println("Using Connection URL: " + impalaConnUrl);
		System.out.println("Running Query: " + sqlStatement);

		try {

			Class.forName("org.apache.hive.jdbc.HiveDriver");

			objConnection = DriverManager.getConnection(impalaConnUrl);

			Statement stmt = objConnection.createStatement();
			ResultSet rs2 = stmt.executeQuery(sqlStatement);

			return rs2;

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static void noReturn(String sqlStatement) {

		System.out.println("\n=============================================");
		System.out.println("Using Connection URL: " + impalaConnUrl);
		System.out.println("Running Query: " + sqlStatement);

		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");

			objConnection = DriverManager.getConnection(impalaConnUrl);

			Statement stmt = objConnection.createStatement();
			stmt.executeUpdate(sqlStatement);
			objConnection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() throws SQLException {
		objConnection.close();
	}
}