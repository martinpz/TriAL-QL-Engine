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
import java.util.ArrayList;

import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.AppImpala;
import executor.ImpalaDaemon;

public class KleeneSemiNaiveImpala {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";
	static ResultSet results = null;
	public static String temporaryQuery;
	static int numberOfLines;
	static String whereExp = "";

	/**
	 * Semi-naive Impala implementation of Transitive closure.
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param joinOnExpression
	 * @param kleeneType
	 * @param selectionPart
	 * @param kleeneDepth1
	 * @param heuristicTable
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, ArrayList<String> joinOnExpression,
			String kleeneType, String[] selectionPart, int kleeneDepth1, String heuristicTable) {

		String tableShortForm = oldTableName[0].substring(0, 2);
		String currentTableName = oldTableName[0];
		if (!heuristicTable.equals(""))
			currentTableName = "deltaP";

		String sel1, sel2, sel3, sel1l, sel2l, sel3l;
		String join = "";
		String topQueryPart = "";

		int stepCounter = 0;

		tableInitialization(oldTableName, heuristicTable);

		numberOfLines = 1;
		while (numberOfLines != 0) {
			stepCounter++;
			String cTableShort = currentTableName;

			if (selectionPart[0].equals("1")) {
				sel1 = cTableShort + "." + selectionPart[1];
				sel1l = tableShortForm + "1" + "." + selectionPart[1];
			} else {
				sel1 = tableShortForm + "1" + "." + selectionPart[1];
				sel1l = cTableShort + "." + selectionPart[1];
			}

			if (selectionPart[2].equals("1")) {
				sel2 = cTableShort + "." + selectionPart[3];
				sel2l = tableShortForm + "1" + "." + selectionPart[3];
			} else {
				sel2 = tableShortForm + "1" + "." + selectionPart[3];
				sel2l = cTableShort + "." + selectionPart[3];
			}

			if (selectionPart[4].equals("1")) {
				sel3 = cTableShort + "." + selectionPart[5];
				sel3l = tableShortForm + "1" + "." + selectionPart[5];
			} else {
				sel3 = tableShortForm + "1" + "." + selectionPart[5];
				sel3l = cTableShort + "." + selectionPart[5];
			}

			if (kleeneType.equals("right")) {
				topQueryPart = "SELECT DISTINCT " + sel1 + " AS subject, " + sel2 + " AS predicate, " + sel3
						+ " AS object" + " FROM " + currentTableName + " JOIN " + oldTableName[0] + " " + tableShortForm
						+ 1 + " ON ";

				if (currentTableName != oldTableName[0])
					whereExp = " WHERE deltaP.iter='" + Integer.toString(stepCounter - 1) + "' ";

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0)
						join = join + " AND ";

					if (joinOnExpression.get(k).toString().substring(2, 3).equals("1")) {
						join = join + " " + cTableShort + joinOnExpression.get(k).toString().substring(3);
					} else {
						join = join + " " + tableShortForm + 1 + joinOnExpression.get(k).toString().substring(3);
					}

					join = join + " " + joinOnExpression.get(k + 1) + " ";

					if (joinOnExpression.get(k + 2).toString().substring(2, 3).equals("1")) {
						join = join + " " + cTableShort + joinOnExpression.get(k + 2).toString().substring(3);
					} else {
						join = join + " " + tableShortForm + 1 + joinOnExpression.get(k + 2).toString().substring(3);
					}
				}
			} else if (kleeneType.equals("left")) {
				topQueryPart = "SELECT DISTINCT " + sel1l + " AS subject, " + sel2l + " AS predicate, " + sel3l
						+ " AS object" + " FROM " + oldTableName[0] + " " + tableShortForm + 1 + " JOIN "
						+ currentTableName + " ON ";

				if (currentTableName != oldTableName[0]) {
					whereExp = " WHERE deltaP.iter='" + Integer.toString(stepCounter - 1) + "' ";
				}

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0)
						join = join + " AND ";

					if (joinOnExpression.get(k).toString().substring(2, 3).equals("1")) {
						join = join + " " + tableShortForm + 1 + joinOnExpression.get(k).toString().substring(3);
					} else {
						join = join + " " + cTableShort + joinOnExpression.get(k).toString().substring(3);
					}

					join = join + " " + joinOnExpression.get(k + 1) + " ";

					if (joinOnExpression.get(k + 2).toString().substring(2, 3).equals("1")) {
						join = join + " " + tableShortForm + 1 + joinOnExpression.get(k + 2).toString().substring(3);
					} else {
						join = join + " " + cTableShort + joinOnExpression.get(k + 2).toString().substring(3);
					}
				}
			}

			String insertTmp = "INSERT OVERWRITE tmp " + topQueryPart + join + whereExp;
			ImpalaDaemon.noReturn(insertTmp);
			ImpalaDaemon.noReturn("COMPUTE STATS tmp");
			baseQuery = baseQuery + insertTmp + "\n";

			temporaryQuery = "SELECT tmp.subject, tmp.predicate, tmp.object FROM tmp LEFT ANTI JOIN deltaP"
					+ " ON tmp.subject = deltaP.subject AND tmp.predicate = deltaP.predicate"
					+ " AND tmp.object = deltaP.object ";

			String insertDeltaP = "INSERT INTO deltaP partition(iter='" + Integer.toString(stepCounter) + "') "
					+ temporaryQuery;
			ImpalaDaemon.noReturn(insertDeltaP);
			ImpalaDaemon.noReturn("COMPUTE INCREMENTAL STATS deltaP");

			baseQuery = baseQuery + insertDeltaP + "\n";

			String resultsChecking = "SELECT COUNT(*) AS count FROM deltaP " + " WHERE iter='"
					+ Integer.toString(stepCounter) + "' ";
			results = ImpalaDaemon.main(resultsChecking);
			baseQuery = baseQuery + resultsChecking + "\n";

			try {
				results.next();
				numberOfLines = results.getInt(1);
			} catch (Exception e) {
			}

			currentTableName = "deltaP";
			System.out.println("# of new lines " + numberOfLines);
			join = "";
		}

		System.out.println("Loop Finished");

		temporaryQuery = "SELECT COUNT(*) FROM (SELECT subject AS subject, predicate as predicate,"
				+ " object AS object from deltaP) MyTable9";

		// The full table
		// temporaryQuery = "SELECT subject AS subject, predicate as predicate,
		// object AS object from deltaP";

		if (!heuristicTable.equals("")) {
			temporaryQuery = "SELECT COUNT(*) FROM (SELECT subject AS subject, predicate as predicate, object AS object from deltaP"
					+ " UNION SELECT subject, predicate, object FROM " + heuristicTable + ") MyTableL";
		}

		if (kleeneDepth1 == -10) {
			temporaryQuery = temporaryQuery + " WHERE iter <> '0'";

		}
		if (AppImpala.theLastQuery) {
			results = ImpalaDaemon.main(temporaryQuery);
		} else {
			results = ImpalaDaemon.main("CREATE TABLE " + newTableName + " AS ( " + temporaryQuery + " )");
		}

		baseQuery = baseQuery + temporaryQuery + "\n";

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
		ResultStruct.fillStructure(results);

	}

	static void tableInitialization(String[] oldTableName, String heuristicTable) {
		String createDeltaP = "CREATE TABLE deltaP (subject string, predicate string, object string) "
				+ "PARTITIONED BY (iter string) STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createDeltaP);

		baseQuery = createDeltaP + "\n";

		String insertDeltaP0;
		if (heuristicTable.equals("")) {
			insertDeltaP0 = "INSERT INTO deltaP partition(iter='0') SELECT subject, predicate, object " + " FROM "
					+ oldTableName[0];
		} else {
			insertDeltaP0 = "INSERT INTO deltaP partition(iter='0') SELECT subject, predicate, object " + " FROM "
					+ heuristicTable + " WHERE iter='2'";

		}
		ImpalaDaemon.noReturn(insertDeltaP0);

		ImpalaDaemon.noReturn("COMPUTE STATS deltaP");

		baseQuery = baseQuery + insertDeltaP0 + "\n";

		String createTmpTable = "CREATE TABLE tmp (subject string, predicate string, object string) "
				+ " STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createTmpTable);
		baseQuery = baseQuery + createTmpTable + "\n";

	}
}
