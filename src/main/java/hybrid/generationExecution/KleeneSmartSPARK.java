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
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.AppSpark;

public class KleeneSmartSPARK {
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
	 * Scalable Smart SPARK implementation of Transitive closure.
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

		numberOfLines = 1;
		// while (numberOfLines > 0) {
		for (int z = 1; z <= 4; z++) {
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

			// Right Kleene Closure
			if (kleeneType.equals("right")) {
				topQueryPart = "SELECT DISTINCT " + sel1 + " AS subject, " + sel2 + " AS predicate, " + sel3
						+ " AS object" + " FROM deltaQ" + " JOIN deltaP" + Integer.toString(stepCounter - 1) + " ON ";

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

			// Left Kleene Closure
			} else if (kleeneType.equals("left")) {

				topQueryPart = "SELECT DISTINCT " + sel1l + " AS subject, " + sel2l + " AS predicate, " + sel3l
						+ " AS object" + " FROM deltaQ" + " JOIN deltaP" + Integer.toString(stepCounter - 1) + " ON ";

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

			}
			String insertDeltaP = topQueryPart + join;

			insertDeltaP = insertDeltaP + " UNION SELECT subject, predicate, object FROM deltaP"
					+ Integer.toString(stepCounter - 1);

			insertDeltaP = insertDeltaP + " UNION SELECT subject, predicate, object FROM deltaQ";

			resultFrame = sqlContext.sql(insertDeltaP);
			resultFrame.cache().registerTempTable("deltaP" + stepCounter);

			baseQuery = baseQuery + insertDeltaP + "\n";

			String KhardCode = "";

			if (kleeneType.equals("right")) {

				KhardCode = "SELECT DISTINCT dp.subject AS subject, dp.predicate AS predicate, gq.object AS object "
						+ " FROM  (SELECT DISTINCT p.subject as subject , p.predicate as predicate"
						+ " , p.object as object FROM deltaP" + stepCounter + " p JOIN "
						+ " (SELECT DISTINCT deltaQ.object as object FROM deltaQ) l " + " ON p.object = l.object) dp"
						+ " JOIN (SELECT DISTINCT p.subject as subject, p.predicate as predicate, p.object as object "
						+ " from colleagues_10p p JOIN (SELECT DISTINCT subject as subject FROM deltaQ ) q "
						+ " ON p.subject=q.subject ) gq "
						+ " ON dp.object = gq.subject AND dp.predicate = gq.predicate";

			} else if (kleeneType.equals("left")) {
				KhardCode = "SELECT DISTINCT dp.subject AS subject, dp.predicate AS predicate, gq.object AS object "
						+ " FROM  (SELECT DISTINCT p.subject as subject, p.predicate as predicate "
						+ " , p.object as object FROM deltaP" + stepCounter + " p JOIN "
						+ " (SELECT DISTINCT deltaQ.subject as subject FROM deltaQ) l "
						+ " ON p.subject = l.subject ) gq"
						+ " JOIN (SELECT DISTINCT p.subject as subject, p.predicate as predicate, p.object as object "
						+ " from colleagues_10p p JOIN (SELECT DISTINCT object as object FROM deltaQ ) q "
						+ " ON p.object = q.object ) dp "
						+ " ON dp.object = gq.subject AND dp.predicate = gq.predicate";
			}

			resultFrame = sqlContext.sql(KhardCode);
			resultFrame.cache().registerTempTable("deltaK");

			String PiHardCode = "" + " SELECT subject, predicate, object from deltaP" + stepCounter
					+ " UNION select subject, predicate," + " object from deltaQ"
					+ " UNION select subject, predicate, object FROM deltaP" + (stepCounter - 1);

			resultFrame = sqlContext.sql(PiHardCode);
			resultFrame.cache().registerTempTable("deltaP" + stepCounter);

			String QHardCode = "SELECT k.subject as subject, k.predicate as predicate, k.object as object"
					+ " FROM deltaK k LEFT OUTER JOIN " + " (SELECT subject, predicate, object FROM deltaP"
					+ stepCounter + " ) p" + " ON k.subject = p.subject AND k.object= p.object"
					+ " AND k.predicate = p.predicate WHERE p.object IS NULL";

			resultFrame = sqlContext.sql(QHardCode);
			resultFrame.cache().registerTempTable("deltaQ");

			join = "";
		}
		System.out.println("###Loop Finished");

		temporaryQuery = "SELECT subject AS subject, predicate as predicate, object AS object from deltaP"
				+ Integer.toString(stepCounter);
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
	 * @param oldTableName
	 * @param tableShortForm
	 * @param joinOnExpression
	 * @param selectionPart
	 */
	static void tableInitialization(String[] oldTableName, String tableShortForm, ArrayList<String> joinOnExpression,
			String[] selectionPart) {

		String createDeltaP1 = "SELECT subject, predicate, object FROM " + oldTableName[0];

		resultFrame = sqlContext.sql(createDeltaP1);
		resultFrame.registerTempTable("deltaP1");

		baseQuery = createDeltaP1 + "\n";

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

		firstJoinAndIntersection = firstJoinAndIntersection + " ) joinTable" + "  LEFT OUTER JOIN " + oldTableName[0]
				+ " t1 " + " ON t1.subject = joinTable.subject AND t1.predicate = joinTable.predicate"
				+ " AND t1.object = joinTable.object WHERE t1.predicate IS NULL";

		resultFrame = sqlContext.sql(firstJoinAndIntersection);
		resultFrame.registerTempTable("deltaQ");

		baseQuery = baseQuery + firstJoinAndIntersection + "\n";
	}

}
