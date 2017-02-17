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

public class FilterSelector {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery;

	/**
	 * Generate Impala/ SPARL SQL queries from E-TriAL-QL Selector operations.
	 * @param oldTableName
	 * @param newTableName
	 * @param whereExpression
	 * @param selectionArray
	 * @param projectedColumn
	 * @param provenance
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, String whereExpression,
			String[] selectionArray, ArrayList<String> projectedColumn, String provenance) {
		if (projectedColumn != null) {
			String[] projectedColumnSplitted = new String[2];

			for (int i = 0; i < projectedColumn.size(); i++) {
				projectedColumnSplitted = projectedColumn.get(i).split("#");
				selectionArray[Integer.parseInt(projectedColumnSplitted[0])] = projectedColumnSplitted[1];
			}
		}

		String tableShortForm = oldTableName[0].substring(0, 2) + "1";

		baseQuery = "SELECT " + selectionArray[0] + " AS subject, " + selectionArray[1] + " AS predicate, "
				+ selectionArray[2] + " AS object" + " FROM " + oldTableName[0] + " " + tableShortForm;

		if (!(whereExpression == null)) {
			baseQuery = baseQuery + " WHERE " + whereExpression;
		}

		finalQuery = "INSERT INTO " + newTableName + " " + baseQuery;

		createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, " + "predicate String, "
				+ "object String " + " ) STORED AS PARQUET;";

		if (!provenance.equals("")) {
			Provenance.addProvenance(newTableName, provenance);
		}

		if (Configuration.compositeJoin) {
			finalQuery = "none";
		}

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, finalQuery, createTableQuery);
	}

}
