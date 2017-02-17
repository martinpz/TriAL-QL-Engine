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

public class UnionSetOperation {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery;

	/**
	 * Generate Impala/ Spark SQL from E-TriAL-QL UNION operations.
	 * @param oldTableName
	 * @param newTableName
	 * @param setOperator
	 * @param setProvenance
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, String setOperator,
			boolean setProvenance) {

		String table1ShortForm = oldTableName[0].substring(0, 2) + "1";
		String table2ShortForm = oldTableName[0].substring(0, 2) + "2";

		baseQuery = "SELECT " + table1ShortForm + ".subject, " + table1ShortForm + ".predicate, " + table1ShortForm
				+ ".object " + " FROM " + oldTableName[0] + " " + table1ShortForm + " " + setOperator + " SELECT "
				+ table2ShortForm + ".subject, " + table2ShortForm + ".predicate, " + table2ShortForm + ".object "
				+ " FROM " + oldTableName[1] + " " + table2ShortForm;

		finalQuery = "INSERT INTO " + newTableName + " " + baseQuery;

		createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, " + "predicate String, "
				+ "object String " + " ) STORED AS PARQUET;";

		if (Configuration.compositeJoin) {
			finalQuery = "none";
		}

		if (setProvenance) {
			Provenance.unionProvenance(newTableName, table1ShortForm, table2ShortForm, oldTableName);
		}

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, finalQuery, createTableQuery);
	}
}
