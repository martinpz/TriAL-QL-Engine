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

package hybrid.generationExecutionProvenance;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.AppSpark;

public class ConnectivityPatternSPARKP {
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
	static String secondSel;

	/**
	 * Generator/ Executor of SPARK queries formed by recursive E-TriAL-QL with
	 * Provenance queries. This implementation includes recalculations and
	 * composite queries of Connectivity Pattern.
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param joinOnExpression
	 * @param kleeneType
	 * @param selectionPart
	 * @param sourceDest
	 * @param provenanceAppenderList
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, ArrayList<String> joinOnExpression,
			String kleeneType, String[] selectionPart, String[] sourceDest, ArrayList<Integer> provenanceAppenderList) {

		String tableShortForm = oldTableName[0].substring(0, 2);
		String currentTableName = oldTableName[0];
		String join;

		int stepCounter = 0;
		int templCounter = 0;
		int temprCounter = 0;
		int hops = 0;

		int res = 0;

		String provenance;
		int finalStepNumber = -1;

		hops++;
		String directCon = "SELECT COUNT(*) from (SELECT '1' FROM " + currentTableName + " WHERE " + " subject="
				+ sourceDest[0] + " AND object=" + sourceDest[1] + " LIMIT 1) c";
		resultFrame = sqlContext.sql(directCon);
		baseQuery = directCon + "\n";

		Row[] results = resultFrame.collect();
		res = (int) results[0].getLong(0);

		System.out.println("#1 res = " + res);
		JOptionPane.showMessageDialog(null, "#1 res = " + res);
		if (res != 0)
			finalStepNumber = 1;

		if (res == 0) {
			tableInitialization(oldTableName, sourceDest);
			hops++;

			String oneHop = "SELECT COUNT(*) FROM (SELECT '1' FROM deltaPl0 " + tableShortForm + 1 + " JOIN deltaPr0 "
					+ tableShortForm + 2 + " ON " + joinOnExpression.get(0) + joinOnExpression.get(1)
					+ joinOnExpression.get(2) + " LIMIT 1) c";

			resultFrame = sqlContext.sql(oneHop);
			baseQuery = baseQuery + oneHop + "\n";

			results = resultFrame.collect();
			res = (int) results[0].getLong(0);

			System.out.println("#2 res = " + res);
			JOptionPane.showMessageDialog(null, "#2 res = " + res);
			if (res != 0)
				finalStepNumber = 2;

		}

		while (newResultsL && newResultsR && res == 0) {
			stepCounter++;
			hops++;

			// If odd
			if (stepCounter % 2 == 1 && newResultsL) {
				if (selectionPart[2].equals("1")) {
					secondSel = "d.";
				} else {
					secondSel = tableShortForm + 2 + ".";
				}
				provenance = ", MIN(CONCAT(d.provenance, '/', " + tableShortForm + 2 + ".provenance)) AS provenance";

				join = "SELECT d." + selectionPart[1] + ", " + secondSel + selectionPart[3] + ", " + tableShortForm + 2
						+ "." + selectionPart[5] + provenance + " FROM deltaPl" + templCounter + " d " + "JOIN ("
						+ " SELECT * FROM " + oldTableName[0] + " WHERE object NOT like '\"%') " + tableShortForm + 2
						+ " ON d" + joinOnExpression.get(0).toString().substring(3) + " " + joinOnExpression.get(1)
						+ " " + joinOnExpression.get(2) + " GROUP BY d.subject, " + secondSel + "predicate, "
						+ tableShortForm + 2 + ".object";

				resultFrame = sqlContext.sql(join);
				resultFrame.registerTempTable("tmpl");

				baseQuery = baseQuery + join + "\n";

				if (unionr.equals("")) {
					unionr = "SELECT * FROM deltaPr0 ";
				}
				unionl = "SELECT * FROM deltaPl0 ";
				if (templCounter != 0) {
					for (int j = 1; j <= templCounter; j++) {
						unionl = unionl + " UNION ALL " + "SELECT * FROM deltaPl" + j;
					}
				}

				temporaryQuery = "SELECT tmpl.subject, tmpl.predicate, tmpl.object, tmpl.provenance"
						+ " FROM tmpl LEFT JOIN ( " + unionl + " ) deltaPl"
						+ " ON tmpl.subject = deltaPl.subject AND tmpl.predicate = deltaPl.predicate"
						+ " AND tmpl.object = deltaPl.object " + " WHERE deltaPl.predicate IS NULL";

				resultFrame = sqlContext.sql(temporaryQuery);
				resultFrame.registerTempTable("deltaPl" + Integer.toString(templCounter + 1));

				baseQuery = baseQuery + temporaryQuery + "\n";

				resultFrame = sqlContext
						.sql("SELECT COUNT(*) AS count FROM deltaPl" + Integer.toString(templCounter + 1));

				results = resultFrame.collect();
				int newItems = (int) results[0].getLong(0);

				if (newItems == 0) {
					newResultsL = false;
				}

				templCounter++;

				// If even
			} else if (newResultsR) {
				if (selectionPart[2].equals("1")) {
					secondSel = tableShortForm + 1 + ".";
				} else {
					secondSel = "d.";
				}
				provenance = ", MIN(CONCAT(" + tableShortForm + 1 + ".provenance, '/', d.provenance)) AS provenance";

				join = "SELECT " + tableShortForm + 1 + "." + selectionPart[1] + ", " + secondSel + selectionPart[3]
						+ ", d." + selectionPart[5] + provenance + " FROM " + oldTableName[0] + " " + tableShortForm + 1
						+ " JOIN deltaPr" + temprCounter + " d ON " + joinOnExpression.get(0) + " "
						+ joinOnExpression.get(1) + " d" + joinOnExpression.get(2).toString().substring(3)
						+ " GROUP BY " + tableShortForm + 1 + ".subject, " + secondSel + "predicate, d.object";

				resultFrame = sqlContext.sql(join);
				resultFrame.registerTempTable("tmpr");

				baseQuery = baseQuery + join + "\n";

				if (unionl.equals("")) {
					unionl = "SELECT * FROM deltaPl0 ";
				}
				unionr = "SELECT * FROM deltaPr0 ";
				if (temprCounter != 0) {
					for (int j = 1; j <= temprCounter; j++) {
						unionr = unionr + " UNION ALL " + "SELECT * FROM deltaPr" + j;
					}
				}

				temporaryQuery = "SELECT tmpr.subject, tmpr.predicate, tmpr.object, tmpr.provenance"
						+ " FROM tmpr LEFT JOIN ( " + unionr + " ) deltaPr"
						+ " ON tmpr.subject = deltaPr.subject AND tmpr.predicate = deltaPr.predicate"
						+ " AND tmpr.object = deltaPr.object " + " WHERE deltaPr.predicate IS NULL";

				resultFrame = sqlContext.sql(temporaryQuery);
				resultFrame.registerTempTable("deltaPr" + Integer.toString(temprCounter + 1));

				baseQuery = baseQuery + temporaryQuery + "\n";

				resultFrame = sqlContext
						.sql("SELECT COUNT(*) AS count FROM deltaPr" + Integer.toString(temprCounter + 1));

				results = resultFrame.collect();
				int newItems = (int) results[0].getLong(0);

				if (newItems == 0) {
					newResultsR = false;
				}

				temprCounter++;

			}

			unionl = unionl + " UNION " + "SELECT * FROM deltaPl" + templCounter;

			unionr = unionr + " UNION " + "SELECT * FROM deltaPr" + temprCounter;

			String resultsChecking = "SELECT COUNT(*) AS COUNT, 'Hops:" + hops + "', " + "MIN(CONCAT(p1, '/', p2))"
					+ " FROM " + "(SELECT " + tableShortForm + 1 + ".provenance AS p1, " + tableShortForm + 2
					+ ".provenance AS p2" + " FROM deltaPl" + templCounter + " " + tableShortForm + 1 + " JOIN deltaPr"
					+ temprCounter + " " + tableShortForm + 2 + " ON " + joinOnExpression.get(0)
					+ joinOnExpression.get(1) + joinOnExpression.get(2) + " LIMIT 1 ) MyTable1";

			resultFrame = sqlContext.sql(resultsChecking);

			results = resultFrame.collect();
			res = (int) results[0].getLong(0);

			baseQuery = baseQuery + resultsChecking + "\n";

			System.out.println("#" + (stepCounter + 2) + " res = " + res);

		}
		System.out.println("Loop finished");

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
		ResultStruct.fillStructureSpark(resultFrame);
	}

	static void tableInitialization(String[] oldTableName, String[] sourceDest) {

		String DeltaPl0 = " SELECT DISTINCT subject, predicate, object, provenance FROM " + oldTableName[0] + " WHERE "
				+ " subject =" + sourceDest[0] + " AND object NOT like '\"%'";

		resultFrame = sqlContext.sql(DeltaPl0);
		resultFrame.registerTempTable("deltaPl0");

		baseQuery = baseQuery + DeltaPl0 + "\n";

		String DeltaPr0 = "SELECT DISTINCT subject, predicate, object, provenance FROM " + oldTableName[0] + " WHERE "
				+ " object =" + sourceDest[1];

		resultFrame = sqlContext.sql(DeltaPr0);
		resultFrame.registerTempTable("deltaPr0");

		baseQuery = baseQuery + DeltaPr0 + "\n";

	}

}
