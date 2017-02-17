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
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.AppSpark;

public class KleeneSemiNaiveSPARKP {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";
	static ResultSet results = null;
	public static String temporaryQuery;
	static int numberOfLines;
	static String whereExp = "";

	/**
	 * 
	 * Semi-naive SPARK with Provenance implementation of Transitive closure.
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
		String currentTableName = "deltaP0";
		String sel1, sel2, sel3, sel1l, sel2l, sel3l;
		String sel4 = "";
		String sel4l = "";
		String join = "";
		String topQueryPart = "";
		String union = "";
		String unionMyTable1 = "";
		String groupBy = "";

		DataFrame resultFrame = null;
		SQLContext sqlContext = AppSpark.sqlContext;

		String createDeltaP0 = "SELECT subject AS subject, predicate AS predicate, object AS object,"
				+ " provenance AS provenance  FROM " + oldTableName[0];

		resultFrame = sqlContext.sql(createDeltaP0);
		resultFrame.registerTempTable("deltaP0");

		int stepCounter = 0;
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

			String provenance = ", MIN(CONCAT(";
			if (provenanceAppenderList.get(0) == 1) {
				sel4 = provenance + cTableShort + ".provenance, '/'," + tableShortForm + "1.provenance))";
				sel4l = provenance + tableShortForm + "1.provenance, '/'," + cTableShort + ".provenance))";
			} else if (provenanceAppenderList.get(0) == 2) {
				sel4 = provenance + tableShortForm + "1.provenance, '/'," + cTableShort + ".provenance))";
				sel4l = provenance + cTableShort + ".provenance, '/'," + tableShortForm + "1.provenance))";
			}

			if (kleeneType.equals("right")) {
				topQueryPart = "SELECT " + sel1 + " AS subject, " + sel2 + " AS predicate, " + sel3 + " AS object"
						+ sel4 + "AS provenance FROM " + currentTableName + " JOIN ("
						+ " SELECT subject, predicate, object, provenance FROM " + oldTableName[0] + " ) "
						+ tableShortForm + 1 + " ON ";

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0) {
						join = join + " AND ";
					}

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

					groupBy = " GROUP BY " + sel1 + ", " + sel2 + ", " + sel3;
				}
			} else if (kleeneType.equals("left")) {
				topQueryPart = "SELECT " + sel1l + " AS subject, " + sel2l + " AS predicate, " + sel3l + " AS object"
						+ sel4l + "AS provenance FROM " + " (SELECT subject, predicate, object" + ", provenance FROM "
						+ oldTableName[0] + " ) " + tableShortForm + 1 + " JOIN " + currentTableName + " ON ";

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0) {
						join = join + " AND ";
					}

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

				groupBy = " GROUP BY " + sel1l + ", " + sel2l + ", " + sel3l;
			}

			resultFrame = sqlContext.sql(topQueryPart + join + whereExp + groupBy);
			resultFrame.registerTempTable("tmp");
			baseQuery = baseQuery + topQueryPart + join + whereExp + groupBy + "\n";

			union = "SELECT subject AS subject, predicate AS predicate, object AS object,"
					+ " provenance AS provenance FROM deltaP0";
			for (int i = 1; i < stepCounter; i++) {
				union = union + " UNION ALL SELECT * FROM deltaP" + i;
			}

			unionMyTable1 = "( " + union + " ) MyTable1";

			temporaryQuery = "SELECT subject, predicate, object, provenance FROM"
					+ " (SELECT tmp.subject AS subject, tmp.predicate AS predicate,"
					+ " tmp.object AS object, tmp.provenance AS provenance FROM tmp LEFT OUTER JOIN " + unionMyTable1
					+ " ON tmp.subject = MyTable1.subject AND tmp.predicate = MyTable1.predicate"
					+ " AND tmp.object = MyTable1.object " + " WHERE MyTable1.predicate IS NULL" + " ) MyTablex";

			baseQuery = baseQuery + temporaryQuery + "\n";
			resultFrame = sqlContext.sql(temporaryQuery);
			resultFrame.registerTempTable("deltaP" + stepCounter);

			String resultsChecking = "SELECT COUNT(*) AS count FROM deltaP" + stepCounter;
			resultFrame = sqlContext.sql(resultsChecking);
			baseQuery = baseQuery + resultsChecking + "\n";

			Row[] results = resultFrame.collect();
			numberOfLines = (int) results[0].getLong(0);

			currentTableName = "deltaP" + stepCounter;
			join = "";

			System.out.println("# of lines " + numberOfLines);
			sqlContext.dropTempTable("tmp");
		}

		System.out.println("Loop Finished");

		union = union + " UNION ALL SELECT * FROM deltaP" + stepCounter;

		if (kleeneDepth1 == -10) {
			union = union.substring(90);
		}

		resultFrame = sqlContext.sql(union);
		baseQuery = baseQuery + union + "\n";

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
		ResultStruct.fillStructureSpark(resultFrame);
	}

}
