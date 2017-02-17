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
import java.util.ArrayList;

import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.AppImpala;
import executor.ImpalaDaemon;

public class KleeneSmartImpalaP {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";
	static ResultSet results = null;
	public static String temporaryQuery;
	static int numberOfLines = 0;
	static String whereExp = "";

	/**
	 * Scalable Smart Impala with Provenance implementation of Transitive closure.
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param joinOnExpression
	 * @param kleeneType
	 * @param selectionPart
	 * @param kleeneDepth1
	 * @param provenanceAppenderList
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, ArrayList<String> joinOnExpression,
			String kleeneType, String[] selectionPart, int kleeneDepth1, ArrayList<Integer> provenanceAppenderList) {

		String tableShortForm = oldTableName[0].substring(0, 2);
		String sel1, sel2, sel3, sel1l, sel2l, sel3l, sel1q, sel2q, sel3q;
		String sel4 = "";
		String sel4l = "";
		String sel4q = "";
		String join = "";
		String topQueryPart = "";
		boolean r1First = false;
		if (provenanceAppenderList.get(0) == 1)
			r1First = true;

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

			String provenance = ", MIN(CONCAT(";
			if (r1First) {
				sel4 = provenance + "deltaP.provenance, '/'," + "deltaQ.provenance)) AS provenance";
				sel4l = provenance + "deltaQ.provenance, '/'," + "deltaP.provenance)) AS provenance";
				sel4q = provenance + "de1.provenance, '/'," + "de2.provenance)) AS provenance";
			} else if (provenanceAppenderList.get(0) == 2) {
				sel4 = provenance + "deltaQ.provenance, '/'," + "deltaP.provenance)) AS provenance";
				sel4l = provenance + "deltaP.provenance, '/'," + "deltaQ.provenance)) AS provenance";
				sel4q = provenance + "de2.provenance, '/'," + "de1.provenance)) AS provenance";
			}

			if (kleeneType.equals("right")) {
				topQueryPart = "SELECT " + sel1 + " AS subject, " + sel2 + " AS predicate, " + sel3 + " AS object"
						+ sel4 + " FROM deltaQ" + " JOIN deltaP ON ";

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
				topQueryPart = "SELECT " + sel1l + " AS subject, " + sel2l + " AS predicate, " + sel3l + " AS object"
						+ sel4l + " FROM deltaP" + " JOIN deltaQ ON ";

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
					+ "SELECT subject, predicate, object, MIN(provenance) FROM (" + topQueryPart + join + whereExp
					+ " GROUP BY subject, predicate, object";

			insertDeltaP = insertDeltaP + " UNION ALL SELECT subject, predicate, object, provenance FROM deltaP"
					+ " WHERE deltaP.iter='" + Integer.toString(stepCounter - 1) + "' ";

			insertDeltaP = insertDeltaP + " UNION ALL SELECT subject, predicate, object, provenance FROM deltaQ)"
					+ " MyTableQ GROUP BY subject, predicate, object";

			ImpalaDaemon.noReturn(insertDeltaP);
			baseQuery = baseQuery + insertDeltaP + "\n";
			ImpalaDaemon.noReturn("COMPUTE STATS deltaP");

			String QJoinAndIntersection = "SELECT c.subject, c.predicate, c.object, MIN(c.provenance) FROM "
					+ " (SELECT " + sel1q + " AS subject, " + sel2q + " AS predicate, " + sel3q + " AS object" + sel4q
					+ " FROM deltaQ de1" + " JOIN deltaQ de2 ON";

			join = "";
			for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
				if (k > 0) {
					join = join + " AND ";
				}

				join = join + " de1" + joinOnExpression.get(k).toString().substring(3) + " "
						+ joinOnExpression.get(k + 1) + " de2" + joinOnExpression.get(k + 2).toString().substring(3);
			}

			whereExp = " GROUP BY subject, predicate, object) c LEFT JOIN"
					+ " (select DISTINCT subject, predicate, object from deltaP where deltap.iter='" + stepCounter
					+ "') deltapx ON " + " c.subject = deltapx.subject AND c.predicate = deltapx.predicate AND"
					+ " c.object = deltapx.object WHERE deltapx.predicate IS NULL"
					+ " GROUP BY c.subject, c.predicate, c.object";

			QJoinAndIntersection = QJoinAndIntersection + join + whereExp;

			String insertDeltaQ = "INSERT OVERWRITE deltaQ " + QJoinAndIntersection;

			ImpalaDaemon.noReturn(insertDeltaQ);
			baseQuery = baseQuery + insertDeltaQ + "\n";
			ImpalaDaemon.noReturn("COMPUTE STATS deltaQ");

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

		// temporaryQuery = "SELECT COUNT(*) FROM (SELECT subject AS subject,
		// predicate as predicate, object AS object, provenance AS provenance"
		// + " from deltaP WHERE iter='" + stepCounter + "') MyTable9";
		temporaryQuery = "SELECT subject AS subject, predicate as predicate, object AS object, provenance as provenance from deltaP"
				+ " WHERE iter='" + stepCounter + "'";
		//
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
		String createDeltaP = "CREATE TABLE deltaP (subject string, predicate string, object string, provenance string) "
				+ "PARTITIONED BY (iter string) STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createDeltaP);

		baseQuery = createDeltaP + "\n";

		String insertDeltaP1 = "INSERT INTO deltaP partition(iter='1') SELECT DISTINCT subject as subject"
				+ ", predicate as predicate, object as object, provenance AS provenance" + " FROM " + oldTableName[0]
				+ " WHERE object NOT like '\"%'";
		ImpalaDaemon.noReturn(insertDeltaP1);

		ImpalaDaemon.noReturn("COMPUTE STATS deltaP");

		baseQuery = baseQuery + insertDeltaP1 + "\n";

		String createDeltaQ = "CREATE TABLE deltaQ (subject string, predicate string, object string, provenance string)"
				+ " STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createDeltaQ);

		baseQuery = baseQuery + createDeltaQ + "\n";

		String firstProvenance = ", MIN(CONCAT(" + tableShortForm + 1 + ".provenance, '/', " + tableShortForm + 2
				+ ".provenance)) AS provenance";

		String firstJoinAndIntersection = "SELECT joinTable.subject AS subject, "
				+ " joinTable.predicate AS predicate, joinTable.object AS object, MIN(joinTable.provenance) AS provenance "
				+ " FROM (SELECT " + tableShortForm + selectionPart[0] + "." + selectionPart[1] + " AS subject, "
				+ tableShortForm + selectionPart[2] + "." + selectionPart[3] + " AS predicate, " + tableShortForm
				+ selectionPart[4] + "." + selectionPart[5] + " AS object" + firstProvenance
				+ " FROM  (SELECT DISTINCT subject, predicate, object, provenance" + " FROM " + oldTableName[0]
				+ " WHERE object NOT like '\"%') " + tableShortForm + 1 + " JOIN "
				+ " (SELECT DISTINCT subject, predicate, object, provenance FROM " + oldTableName[0]
				+ " WHERE object NOT like '\"%') " + tableShortForm + 2 + " ON " + joinOnExpression.get(0) + " "
				+ joinOnExpression.get(1) + " " + joinOnExpression.get(2)
				+ " GROUP BY subject, predicate, object ) joinTable" + "  LEFT JOIN " + oldTableName[0] + " t1 "
				+ " ON t1.subject = joinTable.subject AND t1.predicate = joinTable.predicate"
				+ " AND t1.object = joinTable.object WHERE t1.predicate IS NULL "
				+ " GROUP BY joinTable.subject, joinTable.predicate, joinTable.object";

		String insertDeltaQ1 = "INSERT INTO deltaQ " + firstJoinAndIntersection;

		ImpalaDaemon.noReturn(insertDeltaQ1);

		ImpalaDaemon.noReturn("COMPUTE STATS deltaQ");

		baseQuery = baseQuery + insertDeltaQ1 + "\n";

	}

}
