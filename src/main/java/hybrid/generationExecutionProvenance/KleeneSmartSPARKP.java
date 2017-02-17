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
import javax.swing.JOptionPane;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.AppSpark;

public class KleeneSmartSPARKP {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";
	static ResultSet results = null;
	public static String temporaryQuery;
	static int numberOfLines;
	static String whereExp = "";
	static DataFrame resultFrame = null;
	static JavaSparkContext ctx = AppSpark.ctx;
	static SQLContext sqlContext = AppSpark.sqlContext;

	/**
	 * Scalable Smart SPARK with Provenance implementation of Transitive closure.
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
		String groupBy = "";
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

		resultFrame = sqlContext.sql(resultsChecking);
		baseQuery = baseQuery + resultsChecking + "\n";

		Row[] results = resultFrame.collect();
		numberOfLines = (int) results[0].getLong(0);

		String union = "SELECT subject AS subject, predicate AS predicate, object AS object, "
				+ " provenance AS provenance FROM deltaP1";

		System.out.println("# of new lines " + numberOfLines);
		JOptionPane.showMessageDialog(null, "# of new lines " + numberOfLines);

		numberOfLines = 1;
		while (numberOfLines != 0) {
			stepCounter++;

			if (selectionPart[0].equals("1")) {
				sel1 = "deltaQ." + selectionPart[1];
				sel1l = "deltaP" + Integer.toString(stepCounter - 1) + "." + selectionPart[1];
				sel1q = "de1." + selectionPart[1];
			} else {
				sel1 = "deltaP" + Integer.toString(stepCounter - 1) + "." + selectionPart[1];
				sel1l = "deltaQ." + selectionPart[1];
				sel1q = "de2." + selectionPart[1];

			}

			if (selectionPart[2].equals("1")) {
				sel2 = "deltaQ." + selectionPart[3];
				sel2l = "deltaP" + Integer.toString(stepCounter - 1) + "." + selectionPart[3];
				sel2q = "de1." + selectionPart[3];
			} else {
				sel2 = "deltaP" + Integer.toString(stepCounter - 1) + "." + selectionPart[3];
				sel2l = "deltaQ." + selectionPart[3];
				sel2q = "de2." + selectionPart[3];
			}

			if (selectionPart[4].equals("1")) {
				sel3 = "deltaQ." + selectionPart[5];
				sel3l = "deltaP" + Integer.toString(stepCounter - 1) + "." + selectionPart[5];
				sel3q = "de1." + selectionPart[5];
			} else {
				sel3 = "deltaP" + Integer.toString(stepCounter - 1) + "." + selectionPart[5];
				sel3l = "deltaQ." + selectionPart[5];
				sel3q = "de2." + selectionPart[5];
			}

			String provenance = ", MIN(CONCAT(";
			if (r1First) {
				sel4 = provenance + "deltaP" + (stepCounter - 1) + ".provenance, '/',"
						+ "deltaQ.provenance)) AS provenance";
				sel4l = provenance + "deltaQ.provenance, '/'," + "deltaP" + (stepCounter - 1)
						+ ".provenance)) AS provenance";
				sel4q = provenance + "de1.provenance, '/'," + "de2.provenance)) AS provenance";
			} else if (provenanceAppenderList.get(0) == 2) {
				sel4 = provenance + "deltaQ.provenance, '/'," + "deltaP" + (stepCounter - 1)
						+ ".provenance)) AS provenance";
				sel4l = provenance + "deltaP" + (stepCounter - 1) + ".provenance, '/',"
						+ "deltaQ.provenance)) AS provenance";
				sel4q = provenance + "de2.provenance, '/'," + "de1.provenance)) AS provenance";
			}

			if (kleeneType.equals("right")) {
				topQueryPart = "SELECT " + sel1 + " AS subject, " + sel2 + " AS predicate, " + sel3 + " AS object"
						+ sel4 + " FROM deltaQ" + " JOIN deltaP" + Integer.toString(stepCounter - 1) + " ON ";

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0)
						join = join + " AND ";

					if (joinOnExpression.get(k).toString().substring(2, 3).equals("1")) {
						join = join + " deltaQ" + joinOnExpression.get(k).toString().substring(3);
					} else {
						join = join + " deltaP" + Integer.toString(stepCounter - 1)
								+ joinOnExpression.get(k).toString().substring(3);
					}

					join = join + " " + joinOnExpression.get(k + 1) + " ";

					if (joinOnExpression.get(k + 2).toString().substring(2, 3).equals("1")) {
						join = join + " deltaQ" + joinOnExpression.get(k + 2).toString().substring(3);
					} else {
						join = join + " deltaP" + Integer.toString(stepCounter - 1)
								+ joinOnExpression.get(k + 2).toString().substring(3);
					}
				}
				groupBy = " GROUP BY " + sel1 + ", " + sel2 + ", " + sel3;

			} else if (kleeneType.equals("left")) {

				topQueryPart = "SELECT " + sel1l + " AS subject, " + sel2l + " AS predicate, " + sel3l + " AS object"
						+ sel4l + " FROM deltaQ" + " JOIN deltaP" + Integer.toString(stepCounter - 1) + " ON ";

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0)
						join = join + " AND ";

					if (joinOnExpression.get(k).toString().substring(2, 3).equals("1")) {
						join = join + " deltaP" + Integer.toString(stepCounter - 1)
								+ joinOnExpression.get(k).toString().substring(3);

					} else {
						join = join + " deltaQ" + joinOnExpression.get(k).toString().substring(3);
					}

					join = join + " " + joinOnExpression.get(k + 1) + " ";

					if (joinOnExpression.get(k + 2).toString().substring(2, 3).equals("1")) {
						join = join + " deltaP" + Integer.toString(stepCounter - 1)
								+ joinOnExpression.get(k + 2).toString().substring(3);
					} else {
						join = join + " deltaQ" + joinOnExpression.get(k + 2).toString().substring(3);
					}
				}

				groupBy = " GROUP BY " + sel1l + ", " + sel2l + ", " + sel3l;

			}
			String insertDeltaP = topQueryPart + join;

			insertDeltaP = insertDeltaP + groupBy
					+ " UNION ALL SELECT subject, predicate, object, provenance FROM deltaP"
					+ Integer.toString(stepCounter - 1);

			insertDeltaP = "SELECT subject, predicate, object, MIN(provenance) AS provenance FROM (" + insertDeltaP
					+ " UNION ALL SELECT subject, predicate, object, provenance FROM deltaQ)"
					+ " MyTableQ GROUP BY subject, predicate, object";

			resultFrame = sqlContext.sql(insertDeltaP);
			resultFrame.registerTempTable("deltaP" + stepCounter);

			baseQuery = baseQuery + insertDeltaP + "\n";

			String QJoinAndIntersection = "SELECT c.subject, c.predicate, c.object, MIN(c.provenance) AS provenance FROM "
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

			union = "SELECT subject, predicate, object, provenance FROM deltaP" + stepCounter;

			String cTable = " ( " + union + " ) deltaP";

			whereExp = " GROUP BY " + sel1q + ", " + sel2q + ", " + sel3q + ") c LEFT JOIN" + cTable + " ON "
					+ " c.subject = deltaP.subject AND c.predicate = deltaP.predicate AND"
					+ " c.object = deltaP.object WHERE deltaP.predicate IS NULL"
					+ " GROUP BY c.subject, c.predicate, c.object";

			QJoinAndIntersection = QJoinAndIntersection + join + whereExp;

			resultFrame = sqlContext.sql(QJoinAndIntersection);
			resultFrame.registerTempTable("deltaQ");

			baseQuery = baseQuery + QJoinAndIntersection + "\n";

			resultsChecking = "SELECT COUNT(*) AS count FROM deltaQ";

			resultFrame = sqlContext.sql(resultsChecking);
			baseQuery = baseQuery + resultsChecking + "\n";

			results = resultFrame.collect();
			numberOfLines = (int) results[0].getLong(0);

			System.out.println("# of new lines " + numberOfLines);

			JOptionPane.showMessageDialog(null, "# of new lines " + numberOfLines);

			join = "";
		}
		System.out.println("###Loop Finished");

		temporaryQuery = "SELECT subject AS subject, predicate as predicate, object AS object, "
				+ "provenance AS provenance FROM deltaP" + Integer.toString(stepCounter);
		if (kleeneDepth1 == -10) {
			temporaryQuery = temporaryQuery.substring(90);
		}

		resultFrame = sqlContext.sql(temporaryQuery);
		baseQuery = baseQuery + temporaryQuery + "\n";

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
		ResultStruct.fillStructureSpark(resultFrame);
	}

	/**
	 * Initialization steps.
	 * 
	 * @param oldTableName
	 * @param tableShortForm
	 * @param joinOnExpression
	 * @param selectionPart
	 */
	static void tableInitialization(String[] oldTableName, String tableShortForm, ArrayList<String> joinOnExpression,
			String[] selectionPart) {

		String createDeltaP1 = "SELECT subject, predicate, object, provenance FROM " + oldTableName[0]
				+ " WHERE object NOT like '\"%'";

		resultFrame = sqlContext.sql(createDeltaP1);
		resultFrame.registerTempTable("deltaP1");

		baseQuery = createDeltaP1 + "\n";

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
				+ joinOnExpression.get(1) + " " + joinOnExpression.get(2) + " GROUP BY " + tableShortForm
				+ selectionPart[0] + "." + selectionPart[1] + ", " + tableShortForm + selectionPart[2] + "."
				+ selectionPart[3] + ", " + tableShortForm + selectionPart[4] + "." + selectionPart[5] + " ) joinTable"
				+ "  LEFT JOIN " + oldTableName[0] + " t1 "
				+ " ON t1.subject = joinTable.subject AND t1.predicate = joinTable.predicate"
				+ " AND t1.object = joinTable.object WHERE t1.predicate IS NULL "
				+ " GROUP BY joinTable.subject, joinTable.predicate, joinTable.object";

		resultFrame = sqlContext.sql(firstJoinAndIntersection);
		resultFrame.registerTempTable("deltaQ");

		baseQuery = baseQuery + firstJoinAndIntersection + "\n";
	}

}
