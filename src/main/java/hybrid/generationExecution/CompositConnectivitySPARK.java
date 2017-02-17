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

import java.util.ArrayList;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.AppSpark;

public class CompositConnectivitySPARK {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";
	public static String temporaryQuery;
	static String whereExp = "";
	static DataFrame resultFrame = null;
	static JavaSparkContext ctx = AppSpark.ctx;
	static SQLContext sqlContext = AppSpark.sqlContext;
	static String unionl = "";
	static String unionr = "";
	static boolean newResultsL = true;
	static boolean newResultsR = true;

	public static long difference;

	static String secondSel;

	/**
	 * Generator/ Executor of SPARK queries formed by recursive E-TriAL-QL
	 * queries. This implementation includes partial recalculations and
	 * composite queries of Connectivity Pattern.
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param joinOnExpression
	 * @param kleeneType
	 * @param selectionPart
	 * @param sourceDest
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, ArrayList<String> joinOnExpression,
			String kleeneType, String[] selectionPart, String[] sourceDest) {

		int res = 0;

		long lStartTime = System.nanoTime();
		boolean considerImmidiateConnection = false;
		if (considerImmidiateConnection) {
			String directCon = "SELECT * FROM (SELECT '1' FROM " + oldTableName[0] + " WHERE subject = " + sourceDest[0]
					+ " AND object=" + sourceDest[1] + " LIMIT 1) c";

			resultFrame = sqlContext.sql(directCon);
			baseQuery = directCon + "\n";

			res = (int) resultFrame.count();

			System.out.println("#1 res = " + res);
		}
		while (res == 0) {

			String t1 = "SELECT * FROM " + oldTableName[0] + " t1  WHERE  t1.subject=" + sourceDest[0];

			resultFrame = sqlContext.sql(t1);
			resultFrame.cache().registerTempTable("t1");
			baseQuery = baseQuery + t1 + "\n";

			String secondDegree = "SELECT * FROM (SELECT '1' FROM t1 JOIN " + oldTableName[0]
					+ " t2 ON t1.object = t2.subject WHERE " + " t2.object=" + sourceDest[1] + " LIMIT 1 ) c";

			resultFrame = sqlContext.sql(secondDegree);
			baseQuery = baseQuery + secondDegree + "\n";

			res = (int) resultFrame.count();

			System.out.println("#2 res = " + res);

			if (res != 0)
				break;

			String thirdDegree = "WITH " + " t2 AS (SELECT subject, object FROM " + oldTableName[0] + "),"
					+ " tmpL AS (SELECT t1.subject, t2.object FROM t1 JOIN t2 ON t1.object = t2.subject),"
					+ " t12 AS (SELECT tmpL.subject, tmpL.object FROM tmpL LEFT OUTER JOIN t1 ON tmpL.object = t1.object"
					+ " WHERE t1.object IS NULL)" + " SELECT * FROM t12";

			resultFrame = sqlContext.sql(thirdDegree);
			resultFrame.cache().registerTempTable("t12");
			baseQuery = baseQuery + thirdDegree + "\n";

			thirdDegree = " SELECT subject, object FROM " + oldTableName[0] + " WHERE object=" + sourceDest[1];

			resultFrame = sqlContext.sql(thirdDegree);
			resultFrame.cache().registerTempTable("t3");
			baseQuery = baseQuery + thirdDegree + "\n";

			thirdDegree = " SELECT * FROM (" + " SELECT '1' FROM t12 JOIN t3 ON t12.object = t3.subject LIMIT 1) c";

			baseQuery = baseQuery + thirdDegree + "\n";

			resultFrame = sqlContext.sql(thirdDegree);

			res = (int) resultFrame.count();

			System.out.println("#3 res = " + res);

			if (res != 0)
				break;

			String fourthDegree = "WITH " + " t2 AS (SELECT subject, object FROM " + oldTableName[0] + "),"
					+ " tmpR AS (SELECT t2.subject, t3.object FROM t2 JOIN t3 ON t2.object = t3.subject),"
					+ " t32 AS (SELECT tmpR.subject, tmpR.object FROM tmpR LEFT OUTER JOIN t3 ON tmpR.subject = t3.subject"
					+ " WHERE t3.subject IS NULL)" + " SELECT * FROM ("
					+ " SELECT '1' FROM t12 JOIN t32 ON t12.object = t32.subject LIMIT 1 ) c";

			resultFrame = sqlContext.sql(fourthDegree);
			baseQuery = baseQuery + fourthDegree + "\n";

			res = (int) resultFrame.count();

			System.out.println("#4 res = " + res);

			break;
		}
		long lEndTime = System.nanoTime();
		difference = lEndTime - lStartTime;
		System.out.println("Conn time in ms: " + difference / 1000000);

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
		ResultStruct.fillStructureSpark(resultFrame);
	}
}
