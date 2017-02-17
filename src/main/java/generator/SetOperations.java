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

import data.structures.Configuration;
import data.structures.QueryStruct;

public class SetOperations {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery;

	/**
	 * Generate Impala/ Spark SQL from E-TriAL-QL (INTERSECT and MINUS) operations.
	 * @param oldTableName
	 * @param newTableName
	 * @param setOperator
	 * @param setProvenance
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, String setOperator,
			boolean setProvenance) {

		String table1ShortForm = oldTableName[0].substring(0, 2) + "1";
		String table2ShortForm = oldTableName[1].substring(0, 2) + "2";

		String existance = "";

		if (setOperator.equals("MINUS")) {
			existance = " LEFT OUTER JOIN ";
		} else if (setOperator.equals("INTERSECT")) {
			existance = " INNER JOIN ";
		}

		baseQuery = "SELECT " + table1ShortForm + ".subject, " + table1ShortForm + ".predicate, " + table1ShortForm
				+ ".object " + " FROM " + oldTableName[0] + " " + table1ShortForm + existance + oldTableName[1] + " "
				+ table2ShortForm + " ON " + table1ShortForm + ".subject = " + table2ShortForm + ".subject AND "
				+ table1ShortForm + ".predicate = " + table2ShortForm + ".predicate AND " + table1ShortForm
				+ ".object = " + table2ShortForm + ".object ";

		if (existance.equals(" LEFT OUTER JOIN ")) {
			baseQuery = baseQuery + " WHERE " + table2ShortForm + ".predicate IS NULL";
		}

		finalQuery = "INSERT INTO " + newTableName + " " + baseQuery;
		if (Configuration.compositeJoin) {
			finalQuery = "none";
		}

		createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, " + "predicate String, "
				+ "object String " + " ) STORED AS PARQUET;";

		if (setProvenance) {
			Provenance.setProvenance(newTableName, table1ShortForm, table2ShortForm, oldTableName, existance);
		}

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, finalQuery, createTableQuery);
	}

}
