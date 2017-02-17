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
import executor.ImpalaDaemon;
import generator.KleeneFixed;

public class KleeneHeuristicsImpala {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";
	static ResultSet results = null;

	/**
	 * Heuristic Naive Impala implementation of Transitive closure.
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

		int numberOfLines = -1;

		KleeneFixed.CreateQuery(oldTableName, newTableName, whereExpression, joinOnExpression, 0, 3, kleeneType,
				selectionPart);

		String createTmp = "CREATE TABLE temp (subject string, predicate string, object string) "
				+ "PARTITIONED BY (iter string) STORED AS PARQUET;";
		ImpalaDaemon.noReturn(createTmp);

		String insertTmp = "INSERT INTO temp partition(iter='1') " + KleeneFixed.baseQuery;
		ImpalaDaemon.noReturn(insertTmp);

		ImpalaDaemon.noReturn("COMPUTE STATS temp");

		KleeneFixed.CreateQuery(oldTableName, newTableName, whereExpression, joinOnExpression, 4, -1, kleeneType,
				selectionPart);

		String minusOperation = "SELECT t.subject, t.predicate, t.object FROM (" + KleeneFixed.baseQuery + ") t"
				+ " LEFT OUTER JOIN temp ON t.subject = temp.subject AND t.predicate = temp.predicate AND t.object = "
				+ " temp.object WHERE temp.predicate IS NULL ";

		insertTmp = "INSERT INTO temp partition(iter='2') " + minusOperation;

		baseQuery = baseQuery + insertTmp + "\n";

		ImpalaDaemon.noReturn(insertTmp);

		ImpalaDaemon.noReturn("COMPUTE STATS temp");

		String resultsChecking = "SELECT COUNT(*) AS count FROM temp " + " WHERE iter='2'";
		results = ImpalaDaemon.main(resultsChecking);
		baseQuery = baseQuery + resultsChecking + "\n";

		try {
			results.next();
			numberOfLines = results.getInt(1);
		} catch (Exception e) {
		}

		System.out.println("# of new lines " + numberOfLines);

		if (numberOfLines != 0) {
			KleeneSemiNaiveImpala.CreateQuery(oldTableName, newTableName, joinOnExpression, kleeneType, selectionPart,
					kleeneDepth1, "temp");
		} else {
			results = ImpalaDaemon
					.main("SELECT COUNT(*) FROM (SELECT subject, predicate, object FROM temp WHERE iter='1') MyTableV");
			QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, "none", "none");
			ResultStruct.fillStructure(results);
		}

	}

}
