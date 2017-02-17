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

public class MergeOperation {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery;

	public static String[] oldTableNames = new String[2];

	/**
	 * Generate Impala/ SPARL SQL queries from E-TriAL-QL UNION ALL (MERGE)
	 * operations.
	 * 
	 * @param tableNames
	 * @param oldTableName
	 * @param newTableName
	 */
	public static void CreateQuery(ArrayList<String> tableNames, String[] oldTableName, String newTableName) {

		baseQuery = "Select subject, predicate, object, provenance FROM " + tableNames.get(0);
		for (int i = 1; i < tableNames.size(); i++) {
			baseQuery = baseQuery + " UNION ALL Select subject, predicate, object, provenance FROM "
					+ tableNames.get(i);
		}

		finalQuery = "INSERT INTO " + tableNames.get(0) + " " + baseQuery;

		createTableQuery = "CREATE TABLE " + tableNames.get(0) + " ( " + "subject String, " + "predicate String, "
				+ "object String " + " ) STORED AS PARQUET;";

		if (Configuration.compositeJoin) {
			finalQuery = "none";
		}

		QueryStruct.fillStructure(oldTableNames, "new", baseQuery, finalQuery, createTableQuery);
	}
}
