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

public class KleeneSmartImpala {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";
	static ResultSet results = null;
	public static String temporaryQuery;
	static int numberOfLines = 0;
	static String whereExp = "";

	/**
	 * Scalable Smart Impala implementation of Transitive closure.
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param joinOnExpression
	 * @param kleeneType
	 * @param selectionPart
	 * @param kleeneDepth1
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, ArrayList<String> joinOnExpression,
			String kleeneType, String[] selectionPart, int kleeneDepth1) {

		String tableShortForm = oldTableName[0].substring(0, 2);
		String sel1, sel2, sel3, sel1l, sel2l, sel3l, sel1q, sel2q, sel3q;
		String join = "";
		String topQueryPart = "";

		int stepCounter = 1;

		tableInitialization(oldTableName, tableShortForm, joinOnExpression, selectionPart);

		String resultsChecking = "SELECT COUNT(*) AS count FROM deltaQ";
		results = ImpalaDaemon.main(resultsChecking);
		baseQuery = baseQuery + resultsChecking + "\n";

		try {
			results.next();
			numberOfLines = results.getInt(1);
		} catch (Exception e) {
		}

		System.out.println("# of new lines " + numberOfLines);

		// numberOfLines = 1;
		while (numberOfLines != 0) {
			stepCounter++;

			if (selectionPart[0].equals("1")) {
				sel1 = "deltaQ" + "." + selectionPart[1];
				sel1l = "deltaP." + selectionPart[1];
				sel1q = "de1." + selectionPart[1];
			} else {
				sel1 = "deltaP." + selectionPart[1];
				sel1l = "deltaQ" + "." + selectionPart[1];
				sel1q = "de2." + selectionPart[1];

			}

			if (selectionPart[2].equals("1")) {
				sel2 = "deltaQ" + "." + selectionPart[3];
				sel2l = "deltaP." + selectionPart[3];
				sel2q = "de1." + selectionPart[3];
			} else {
				sel2 = "deltaP." + selectionPart[3];
				sel2l = "deltaQ" + "." + selectionPart[3];
				sel2q = "de2." + selectionPart[3];
			}

			if (selectionPart[4].equals("1")) {
				sel3 = "deltaQ" + "." + selectionPart[5];
				sel3l = "deltaP." + selectionPart[5];
				sel3q = "de1." + selectionPart[5];
			} else {
				sel3 = "deltaP." + selectionPart[5];
				sel3l = "deltaQ" + "." + selectionPart[5];
				sel3q = "de2." + selectionPart[5];
			}

			if (kleeneType.equals("right")) {
				topQueryPart = "SELECT DISTINCT " + sel1 + " AS subject, " + sel2 + " AS predicate, " + sel3
						+ " AS object" + " FROM deltaQ" + " JOIN  deltaP ON ";

				whereExp = " WHERE deltaP.iter='" + Integer.toString(stepCounter - 1) + "' ";

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0)
						join = join + " AND ";

					if (joinOnExpression.get(k).toString().substring(2, 3).equals("1")) {
						join = join + " deltaQ" + joinOnExpression.get(k).toString().substring(3);
					} else {
						join = join + " deltaP" + joinOnExpression.get(k).toString().substring(3);
					}

					join = join + " " + joinOnExpression.get(k + 1) + " ";

					if (joinOnExpression.get(k + 2).toString().substring(2, 3).equals("1")) {
						join = join + " deltaQ" + joinOnExpression.get(k + 2).toString().substring(3);
					} else {
						join = join + " deltaP" + joinOnExpression.get(k + 2).toString().substring(3);
					}
				}

			} else if (kleeneType.equals("left")) {
				topQueryPart = "SELECT DISTINCT " + sel1l + " AS subject, " + sel2l + " AS predicate, " + sel3l
						+ " AS object" + " FROM deltaP" + " JOIN  deltaQ ON ";

				whereExp = " WHERE deltaP.iter='" + Integer.toString(stepCounter - 1) + "' ";
				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0)
						join = join + " AND ";

					if (joinOnExpression.get(k).toString().substring(2, 3).equals("1")) {
						join = join + " deltaP" + joinOnExpression.get(k).toString().substring(3);
					} else {
						join = join + " deltaQ" + joinOnExpression.get(k).toString().substring(3);
					}

					join = join + " " + joinOnExpression.get(k + 1) + " ";

					if (joinOnExpression.get(k + 2).toString().substring(2, 3).equals("1")) {
						join = join + " deltaP" + joinOnExpression.get(k + 2).toString().substring(3);
					} else {
						join = join + " deltaQ" + joinOnExpression.get(k + 2).toString().substring(3);
					}
				}

			}
			String insertDeltaP = "INSERT INTO deltaP " + "partition(iter='" + Integer.toString(stepCounter) + "') "
					+ topQueryPart + join + whereExp;

			ImpalaDaemon.noReturn(insertDeltaP);
			baseQuery = baseQuery + insertDeltaP + "\n";
			ImpalaDaemon.noReturn("COMPUTE Incremental STATS deltaP");

			String KhardCode = "";

			if (kleeneType.equals("right")) {

				KhardCode = "INSERT OVERWRITE deltaK SELECT DISTINCT dp.subject, dp.predicate, gq.object "
						+ "FROM  (SELECT DISTINCT p.subject, p.predicate, p.object FROM deltaP p JOIN   "
						+ " (SELECT DISTINCT deltaQ.object as object FROM deltaQ) l "
						+ " ON p.object = l.object  where p.iter='" + stepCounter + "') dp"
						+ " JOIN  (SELECT DISTINCT p.subject, p.predicate, p.object"
						+ " from snb.colleagues p JOIN  (SELECT DISTINCT subject as subject FROM deltaQ ) q "
						+ " ON p.subject=q.subject ) gq "
						+ " ON dp.object = gq.subject AND dp.predicate = gq.predicate";

			} else if (kleeneType.equals("left")) {
				KhardCode = "INSERT OVERWRITE deltaK SELECT DISTINCT dp.subject, dp.predicate, gq.object "
						+ "FROM  (SELECT DISTINCT p.subject, p.predicate, p.object FROM deltaP p JOIN  "
						+ " (SELECT DISTINCT deltaQ.subject as subject FROM deltaQ) l "
						+ " ON p.subject = l.subject  where p.iter='" + stepCounter + "') gq"
						+ " JOIN  (SELECT DISTINCT p.subject, p.predicate, p.object"
						+ " from snb.colleagues p JOIN  (SELECT DISTINCT object as object FROM deltaQ ) q "
						+ " ON p.object = q.object ) dp "
						+ " ON dp.object = gq.subject AND dp.predicate = gq.predicate";

			}
			ImpalaDaemon.noReturn(KhardCode);
			ImpalaDaemon.noReturn("COMPUTE Incremental STATS deltaK");

			String PiHardCode = "INSERT OVERWRITE deltaP " + "partition(iter='" + Integer.toString(stepCounter) + "') "
					+ " SELECT subject, predicate, object from deltaP where iter='" + stepCounter
					+ "' UNION select * from deltaQ"
					+ " UNION select subject, predicate, object FROM deltaP Where iter='" + (stepCounter - 1) + "'";

			ImpalaDaemon.noReturn(PiHardCode);
			ImpalaDaemon.noReturn("COMPUTE INCREMENTAL STATS deltaP");

			String QHardCode = "INSERT OVERWRITE deltaQ SELECT * FROM deltaK k LEFT ANTI JOIN "
					+ " (SELECT subject, predicate, object FROM deltaP  where iter='" + stepCounter + "') p"
					+ " ON k.subject = p.subject AND k.object= p.object" + " AND k.predicate = p.predicate ";

			ImpalaDaemon.noReturn(QHardCode);
			ImpalaDaemon.noReturn("COMPUTE INCREMENTAL STATS deltaQ");

			resultsChecking = "SELECT COUNT(*) AS count FROM deltaQ";
			results = ImpalaDaemon.main(resultsChecking);
			baseQuery = baseQuery + resultsChecking + "\n";

			try {
				results.next();
				numberOfLines = results.getInt(1);
			} catch (Exception e) {
			}
			System.out.println("# of new lines " + numberOfLines);

			join = "";
			whereExp = "";
			topQueryPart = "";

		}
		System.out.println("###Loop Finished");

		temporaryQuery = "SELECT count(*) from deltaP where iter = '" + stepCounter + "'";

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

	static void tableInitialization(String[] oldTableName, String tableShortForm, ArrayList<String> joinOnExpression,
			String[] selectionPart) {
		String createDeltaP = "CREATE TABLE deltaP (subject string, predicate string, object string) "
				+ "PARTITIONED BY (iter string) STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createDeltaP);

		baseQuery = createDeltaP + "\n";

		String insertDeltaP1 = "INSERT INTO deltaP partition(iter='1') SELECT subject as subject"
				+ ", predicate as predicate, object as object" + " FROM " + oldTableName[0];
		ImpalaDaemon.noReturn(insertDeltaP1);
		ImpalaDaemon.noReturn("COMPUTE STATS deltaP");

		baseQuery = baseQuery + insertDeltaP1 + "\n";

		String createDeltaQ = "CREATE TABLE deltaQ (subject string, predicate string, object string)"
				+ " STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createDeltaQ);

		baseQuery = baseQuery + createDeltaQ + "\n";

		String firstJoinAndIntersection = "SELECT joinTable.subject AS subject, " + " joinTable.predicate AS predicate,"
				+ " joinTable.object AS object " + " FROM (SELECT DISTINCT " + tableShortForm + selectionPart[0] + "."
				+ selectionPart[1] + " AS subject, " + tableShortForm + selectionPart[2] + "." + selectionPart[3]
				+ " AS predicate, " + tableShortForm + selectionPart[4] + "." + selectionPart[5] + " AS object"
				+ " FROM  " + oldTableName[0] + " " + tableShortForm + 1 + " JOIN " + oldTableName[0] + " "
				+ tableShortForm + 2 + " ON " + joinOnExpression.get(0) + " " + joinOnExpression.get(1) + " "
				+ joinOnExpression.get(2);

		if (joinOnExpression.size() > 3) {
			for (int i = 3; i < joinOnExpression.size(); i = i + 3) {
				firstJoinAndIntersection = firstJoinAndIntersection + " AND " + joinOnExpression.get(i) + " "
						+ joinOnExpression.get(i + 1) + " " + joinOnExpression.get(i + 2);
			}
		}

		firstJoinAndIntersection = firstJoinAndIntersection + " ) joinTable" + "  LEFT ANTI JOIN " + oldTableName[0]
				+ " t1 " + " ON t1.subject = joinTable.subject AND t1.predicate = joinTable.predicate"
				+ " AND t1.object = joinTable.object";

		String insertDeltaQ1 = "INSERT INTO deltaQ " + firstJoinAndIntersection;

		ImpalaDaemon.noReturn(insertDeltaQ1);

		ImpalaDaemon.noReturn("COMPUTE STATS deltaQ");

		baseQuery = baseQuery + insertDeltaQ1 + "\n";

		String createDeltaK = "CREATE TABLE deltaK (subject string, predicate string, object string)"
				+ " STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createDeltaK);
	}

}
