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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.rowset.CachedRowSet;
import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.ImpalaDaemon;

public class ConnectivityPatternImpalaP {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";
	static ResultSet results = null;
	public static String temporaryQuery;
	static int numberOfLines;
	static String whereExp = "";
	static CachedRowSet resultsCopy;
	static boolean newResultsL = true;
	static boolean newResultsR = true;
	static String secondSel;

	/**
	 * Generator/ Executor of Impala queries formed by recursive E-TriAL-QL with
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
	 * @throws SQLException
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, ArrayList<String> joinOnExpression,
			String kleeneType, String[] selectionPart, String[] sourceDest, ArrayList<Integer> provenanceAppenderList)
			throws SQLException {

		String tableShortForm = oldTableName[0].substring(0, 2);
		String currentTableName = oldTableName[0];
		String join;

		int stepCounter = 0;
		int templCounter = 0;
		int temprCounter = 0;
		int hops = 0;
		String provenance;
		int finalStepNumber = -1;
		String provenanceGetter = "";

		int res = 0;

		hops++;
		String directCon = "SELECT COUNT(*) from (SELECT '1' FROM " + currentTableName + " WHERE " + " subject="
				+ sourceDest[0] + " AND object=" + sourceDest[1] + " LIMIT 1) c";
		results = ImpalaDaemon.main(directCon);
		baseQuery = directCon + "\n";

		results.next();
		res = results.getInt(1);
		System.out.println("#1 res = " + res);
		if (res != 0)
			finalStepNumber = 1;

		if (res == 0) {
			tableInitialization(oldTableName, sourceDest);
			hops++;

			String oneHop = "SELECT COUNT(*) FROM (SELECT '1' FROM deltaPl " + tableShortForm + 1 + " JOIN deltaPr "
					+ tableShortForm + 2 + " ON " + joinOnExpression.get(0) + joinOnExpression.get(1)
					+ joinOnExpression.get(2) + " LIMIT 1) c";
			results = ImpalaDaemon.main(oneHop);

			results.next();
			res = results.getInt(1);
			System.out.println("#2 res = " + res);
			if (res != 0)
				finalStepNumber = 2;
		}

		while (newResultsL && newResultsR && res == 0) {
			stepCounter++;
			hops++;

			// If odd
			if (stepCounter % 2 == 1) {
				if (selectionPart[2].equals("1")) {
					secondSel = "d.";
				} else {
					secondSel = tableShortForm + 2 + ".";
				}
				provenance = ", MIN(CONCAT(d.provenance, '/', " + tableShortForm + 2 + ".provenance)) AS provenance";

				join = "SELECT d." + selectionPart[1] + ", " + secondSel + selectionPart[3] + ", " + tableShortForm + 2
						+ "." + selectionPart[5] + provenance + " FROM deltaPl d " + "JOIN (" + " SELECT * FROM "
						+ oldTableName[0] + " WHERE object NOT like '\"%') " + tableShortForm + 2 + " ON d"
						+ joinOnExpression.get(0).toString().substring(3) + " " + joinOnExpression.get(1) + " "
						+ joinOnExpression.get(2) + " WHERE d.iter='" + templCounter + "'" + " GROUP BY d.subject, "
						+ secondSel + "predicate, " + tableShortForm + 2 + ".object";

				String insertTmpl = "INSERT OVERWRITE tmpl " + join;
				ImpalaDaemon.noReturn(insertTmpl);
				baseQuery = baseQuery + insertTmpl + "\n";

				temporaryQuery = "SELECT tmpl.subject, tmpl.predicate, tmpl.object, tmpl.provenance"
						+ " FROM tmpl LEFT JOIN deltaPl"
						+ " ON tmpl.subject = deltaPl.subject AND tmpl.predicate = deltaPl.predicate"
						+ " AND tmpl.object = deltaPl.object " + " WHERE deltaPl.predicate IS NULL";

				String insertDeltaPl = "INSERT INTO deltaPl partition(iter='" + Integer.toString(templCounter + 1)
						+ "') " + temporaryQuery;
				ImpalaDaemon.noReturn(insertDeltaPl);

				ImpalaDaemon.noReturn("COMPUTE STATS deltaPl");

				baseQuery = baseQuery + insertDeltaPl + "\n";

				results = ImpalaDaemon.main("SELECT COUNT(*) AS count FROM deltaPl " + " WHERE iter='"
						+ Integer.toString(templCounter + 1) + "'");

				int newItems = -1;
				results.next();
				newItems = results.getInt(1);

				if (newItems == 0) {
					newResultsL = false;
				}

				templCounter++;

				// If even
			} else {
				if (selectionPart[2].equals("1")) {
					secondSel = tableShortForm + 1 + ".";
				} else {
					secondSel = "d.";
				}
				provenance = ", MIN(CONCAT(" + tableShortForm + 1 + ".provenance, '/', d.provenance)) AS provenance";

				join = "SELECT " + tableShortForm + 1 + "." + selectionPart[1] + ", " + secondSel + selectionPart[3]
						+ ", d." + selectionPart[5] + provenance + " FROM " + oldTableName[0] + " " + tableShortForm + 1
						+ " JOIN deltaPr d ON " + " " + joinOnExpression.get(0) + " " + joinOnExpression.get(1) + " d"
						+ joinOnExpression.get(2).toString().substring(3) + " WHERE d.iter='" + temprCounter + "'"
						+ " GROUP BY " + tableShortForm + 1 + ".subject, " + secondSel + "predicate, d.object";

				String insertTmpr = "INSERT OVERWRITE tmpr " + join;
				ImpalaDaemon.noReturn(insertTmpr);
				baseQuery = baseQuery + insertTmpr + "\n";

				temporaryQuery = "SELECT tmpr.subject, tmpr.predicate, tmpr.object, tmpr.provenance"
						+ " FROM tmpr LEFT JOIN deltaPr"
						+ " ON tmpr.subject = deltaPr.subject AND tmpr.predicate = deltaPr.predicate"
						+ " AND tmpr.object = deltaPr.object " + " WHERE deltaPr.predicate IS NULL";
				String insertDeltaPr = "INSERT INTO deltaPr partition(iter='" + Integer.toString(temprCounter + 1)
						+ "') " + temporaryQuery;

				ImpalaDaemon.noReturn(insertDeltaPr);

				ImpalaDaemon.noReturn("COMPUTE STATS deltaPr");

				baseQuery = baseQuery + insertDeltaPr + "\n";

				results = ImpalaDaemon.main("SELECT COUNT(*) AS count FROM deltaPr " + " WHERE iter='"
						+ Integer.toString(temprCounter + 1) + "'");

				int newItems = -1;
				results.next();
				newItems = results.getInt(1);

				if (newItems == 0) {
					newResultsR = false;
				}

				temprCounter++;
			}

			String resultsChecking = "SELECT COUNT(*) AS COUNT FROM " + "(select DISTINCT " + tableShortForm + 1
					+ ".subject, " + tableShortForm + 1 + ".predicate, " + tableShortForm + 1 + ".object FROM deltapl "
					+ tableShortForm + 1 + " JOIN deltapr " + tableShortForm + 2 + " ON " + joinOnExpression.get(0)
					+ joinOnExpression.get(1) + joinOnExpression.get(2) + " WHERE " + tableShortForm + 1 + ".iter = '"
					+ templCounter + "' AND " + tableShortForm + 2 + ".iter = '" + temprCounter
					+ "' LIMIT 1 ) MyTable1";

			results = ImpalaDaemon.main(resultsChecking);

			baseQuery = baseQuery + resultsChecking + "\n";

			results.next();
			res = results.getInt(1);

			System.out.println("#" + (stepCounter + 2) + " res = " + res);

			if (res != 0)
				finalStepNumber = 100;

		}
		System.out.println("Loop finished");

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
		ResultStruct.fillStructure(results);

	}

	static void tableInitialization(String[] oldTableName, String[] sourceDest) {

		String createDeltaPl = "CREATE TABLE deltaPl (subject string, predicate string, object string, provenance string) "
				+ "PARTITIONED BY (iter string) STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createDeltaPl);

		baseQuery = baseQuery + createDeltaPl + "\n";

		String insertDeltaPl = "INSERT INTO deltaPl partition(iter='0') " + " SELECT DISTINCT subject, "
				+ "predicate, object, provenance FROM " + oldTableName[0] + " WHERE " + " subject =" + sourceDest[0]
				+ " AND object NOT like '\"%'";
		ImpalaDaemon.noReturn(insertDeltaPl);

		ImpalaDaemon.noReturn("COMPUTE STATS deltaPl");

		baseQuery = baseQuery + insertDeltaPl + "\n";

		String createDeltaPr = "CREATE TABLE deltaPr (subject string, predicate string, object string, provenance string) "
				+ "PARTITIONED BY (iter string) STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createDeltaPr);

		baseQuery = baseQuery + createDeltaPr + "\n";

		String insertDeltaPr = "INSERT INTO deltaPr partition(iter='0') "
				+ " SELECT DISTINCT subject, predicate, object," + " provenance FROM " + oldTableName[0] + " WHERE "
				+ " object =" + sourceDest[1];
		ImpalaDaemon.noReturn(insertDeltaPr);

		ImpalaDaemon.noReturn("COMPUTE STATS deltaPr");

		baseQuery = baseQuery + insertDeltaPr + "\n";

		String createTmpr = "CREATE TABLE tmpr (subject string, predicate string, object string, provenance string) "
				+ "STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createTmpr);

		baseQuery = baseQuery + createTmpr + "\n";

		String createTmpl = "CREATE TABLE tmpl (subject string, predicate string, object string, provenance string) "
				+ "STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createTmpl);

		baseQuery = baseQuery + createTmpl + "\n";

	}
}
