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

import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import data.structures.QueryStruct;
import data.structures.ResultStruct;
import executor.AppSpark;
import generator.KleeneFixed;

public class KleeneHeuristicsSPARK {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";

	/**
	 * Heuristic Naive SPARK implementation of Transitive closure.
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param whereExpression
	 * @param joinOnExpression
	 * @param kleeneDepth1
	 * @param kleeneDepth2
	 * @param kleeneType
	 * @param selectionPart
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, String whereExpression,
			ArrayList<String> joinOnExpression, int kleeneDepth1, int kleeneDepth2, String kleeneType,
			String[] selectionPart) {

		DataFrame resultFrame = null;
		SQLContext sqlContext = AppSpark.sqlContext;

		int numberOfLines = -1;

		KleeneFixed.CreateQuery(oldTableName, newTableName, whereExpression, joinOnExpression, 0, 3, kleeneType,
				selectionPart);

		String insertTmp = KleeneFixed.baseQuery;

		resultFrame = sqlContext.sql(insertTmp);
		resultFrame.registerTempTable("temp1");

		KleeneFixed.CreateQuery(oldTableName, newTableName, whereExpression, joinOnExpression, 4, -1, kleeneType,
				selectionPart);

		String minusOperation = "SELECT DISTINCT t.subject, t.predicate, t.object FROM (" + KleeneFixed.baseQuery
				+ ") t"
				+ " LEFT JOIN temp1 ON t.subject = temp1.subject AND t.predicate = temp1.predicate AND t.object = "
				+ " temp1.object WHERE temp1.predicate IS NULL ";

		baseQuery = baseQuery + minusOperation + "\n";

		resultFrame = sqlContext.sql(minusOperation);
		resultFrame.registerTempTable("temp2");

		String resultsChecking = "SELECT COUNT(*) AS count FROM temp2";

		resultFrame = sqlContext.sql(resultsChecking);

		Row[] results = resultFrame.collect();
		numberOfLines = (int) results[0].getLong(0);

		baseQuery = baseQuery + resultsChecking + "\n";

		System.out.println("# of new lines " + numberOfLines);

		if (numberOfLines != 0) {
			KleeneSemiNaiveSPARK.CreateQuery(oldTableName, newTableName, joinOnExpression, kleeneType, selectionPart,
					kleeneDepth1, "temp2");
		} else {
			resultFrame = sqlContext.sql("SELECT subject, predicate, object FROM temp1");
			QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
			ResultStruct.fillStructureSpark(resultFrame);
		}

	}

}
