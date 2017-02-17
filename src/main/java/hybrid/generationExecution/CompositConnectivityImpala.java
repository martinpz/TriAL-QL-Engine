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

package hybrid.generationExecution;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.ImpalaDaemon;

public class CompositConnectivityImpala {
	public static String baseQuery = "";
	static ResultSet results = null;

	static String secondSel;

	/**
	 * Generator/ Executor of Impala queries formed by recursive E-TriAL-QL
	 * queries. This implementation includes recalculations and composite
	 * queries of Connectivity Pattern.
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param joinOnExpression
	 * @param kleeneType
	 * @param selectionPart
	 * @param sourceDest
	 * @throws SQLException
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, ArrayList<String> joinOnExpression,
			String kleeneType, String[] selectionPart, String[] sourceDest) throws SQLException {

		int res = 0;
		long lStartTime = System.nanoTime();

		boolean considerImmidiateConnection = false;
		if (considerImmidiateConnection) {
			String directCon = "SELECT COUNT(*) FROM (SELECT '1' FROM " + oldTableName[0] + " WHERE subject = "
					+ sourceDest[0] + " AND object=" + sourceDest[1] + " LIMIT 1) c";

			results = ImpalaDaemon.main(directCon);
			baseQuery = directCon + "\n";

			results.next();
			res = results.getInt(1);
			System.out.println("#1 res = " + res);
		}

		while (res == 0) {

			String secondDegree = "SELECT COUNT(*) FROM (SELECT '1' FROM " + oldTableName[0] + " t1 JOIN "
					+ oldTableName[0] + " t2 ON t1.object = t2.subject WHERE  t1.subject=" + sourceDest[0]
					+ " AND t2.object=" + sourceDest[1] + " LIMIT 1 ) c";

			results = ImpalaDaemon.main(secondDegree);
			baseQuery = baseQuery + secondDegree + "\n";

			results.next();
			res = results.getInt(1);
			System.out.println("#2 res = " + res);
			if (res != 0)
				break;

			String thirdDegree = "WITH t1 AS (SELECT subject, object FROM " + oldTableName[0] + " WHERE subject="
					+ sourceDest[0] + ")," + " t2 AS (SELECT subject, object FROM " + oldTableName[0] + "),"
					+ " t3 AS (SELECT subject, object FROM " + oldTableName[0] + " WHERE object=" + sourceDest[1] + "),"
					+ " tmpL AS (SELECT t1.subject, t2.object FROM t1 JOIN t2 ON t1.object = t2.subject),"
					+ " t12 AS (SELECT tmpL.subject, tmpL.object FROM tmpL LEFT ANTI JOIN t1 ON tmpL.object = t1.object)"
					+ " SELECT COUNT(*) FROM (" + " SELECT '1' FROM t12 JOIN t3 ON t12.object = t3.subject LIMIT 1) c";

			results = ImpalaDaemon.main(thirdDegree);
			baseQuery = baseQuery + thirdDegree + "\n";

			results.next();
			res = results.getInt(1);
			System.out.println("#3 res = " + res);
			if (res != 0)
				break;

			String fourthDegree = "WITH t1 AS (SELECT subject, object FROM " + oldTableName[0] + " WHERE subject="
					+ sourceDest[0] + ")," + " t2 AS (SELECT subject, object FROM " + oldTableName[0] + "),"
					+ " t3 AS (SELECT subject, object FROM " + oldTableName[0] + " WHERE object=" + sourceDest[1] + "),"
					+ " tmpL AS (SELECT t1.subject, t2.object FROM t1 JOIN t2 ON t1.object = t2.subject),"
					+ " t12 AS (SELECT tmpL.subject, tmpL.object FROM tmpL LEFT ANTI JOIN t1 ON tmpL.object = t1.object),"
					+ " tmpR AS (SELECT t2.subject, t3.object FROM t2 JOIN t3 ON t2.object = t3.subject),"
					+ " t32 AS (SELECT tmpR.subject, tmpR.object FROM tmpR LEFT ANTI JOIN t3 ON tmpR.subject = t3.subject)"
					+ " SELECT COUNT(*) FROM ("
					+ " SELECT '1' FROM t12 JOIN t32 ON t12.object = t32.subject LIMIT 1 ) c";

			results = ImpalaDaemon.main(fourthDegree);
			baseQuery = baseQuery + fourthDegree + "\n";

			results.next();
			res = results.getInt(1);
			System.out.println("#4 res = " + res);
			if (res != 0)
				break;
		}

		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;
		System.out.println("Conn time in ms: " + difference / 1000000);

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
		ResultStruct.fillStructure(results);
	}
}
