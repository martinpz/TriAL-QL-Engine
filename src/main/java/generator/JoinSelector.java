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

package generator;

import java.util.ArrayList;

import data.structures.Configuration;
import data.structures.QueryStruct;

/**
 */
public class JoinSelector {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery;

	/**
	 * Generate Impala/ SPARL SQL queries from E-TriAL-QL Triple Join operations.
	 * @param oldTableName
	 * @param newTableName
	 * @param whereExpression
	 * @param selectionArray
	 * @param joinOnExpression
	 * @param projectedColumn
	 * @param setProvenance
	 * @param provenanceAppenderList
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, String whereExpression,
			String[] selectionArray, ArrayList<String> joinOnExpression, ArrayList<String> projectedColumn,
			Boolean setProvenance, ArrayList<Integer> provenanceAppenderList) {
		String table1ShortForm = oldTableName[0].substring(0, 2) + "1";
		String table2ShortForm = oldTableName[0].substring(0, 2) + "2";

		if (projectedColumn != null) {
			String[] projectedColumnSplitted = new String[2];

			for (int i = 0; i < projectedColumn.size(); i++) {
				projectedColumnSplitted = projectedColumn.get(i).split("#");
				selectionArray[Integer.parseInt(projectedColumnSplitted[0])] = projectedColumnSplitted[1];
			}
		}

		baseQuery = "SELECT DISTINCT " + selectionArray[0] + " AS subject, " + selectionArray[1] + " AS predicate, "
				+ selectionArray[2] + " AS object" + " FROM " + oldTableName[0] + " " + table1ShortForm + " JOIN "
				+ oldTableName[1] + " " + table2ShortForm + " ON " + joinOnExpression.get(0) + " "
				+ joinOnExpression.get(1) + " " + joinOnExpression.get(2);

		if (joinOnExpression.size() > 3) {
			for (int i = 3; i < joinOnExpression.size(); i = i + 3) {
				baseQuery = baseQuery + " AND " + joinOnExpression.get(i) + " " + joinOnExpression.get(i + 1) + " "
						+ joinOnExpression.get(i + 2);
			}
		}

		if (!(whereExpression == null)) {
			baseQuery = baseQuery + " WHERE " + whereExpression;
		}

		if (Configuration.noFixedPoint) {
			String tableShortForm = oldTableName[0].substring(0, 2) + "2";

			baseQuery = "SELECT " + tableShortForm + ".subject AS subject, " + tableShortForm
					+ ".predicate AS predicate, " + tableShortForm + ".object AS object" + " FROM " + oldTableName[1]
					+ " " + tableShortForm;
			baseQuery = baseQuery + " WHERE " + whereExpression;

		}

		createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, " + "predicate String, "
				+ "object String " + " ) STORED AS PARQUET;";
		finalQuery = "INSERT INTO " + newTableName + " " + baseQuery;

		if (setProvenance) {
			Provenance.appendProvenance(oldTableName, newTableName, selectionArray, joinOnExpression, table2ShortForm,
					table1ShortForm, whereExpression, provenanceAppenderList);
			finalQuery = "INSERT INTO " + newTableName + " " + baseQuery;
		}

		if (Configuration.compositeJoin) {
			finalQuery = "none";
		}
		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, finalQuery, createTableQuery);
	}

}
