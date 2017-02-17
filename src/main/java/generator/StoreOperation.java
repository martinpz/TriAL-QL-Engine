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

import data.structures.QueryStruct;

public class StoreOperation {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery;

	/**
	 * STORE data manipulation operator.
	 * @param oldTableName
	 * @param newTableName
	 * @param setProvenance
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, boolean setProvenance) {
		baseQuery = "SELECT subject AS subject, predicate AS predicate, object AS object FROM " + oldTableName[0];

		finalQuery = "INSERT INTO " + newTableName + " SELECT subject, predicate, object FROM " + oldTableName[0];

		createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, " + "predicate String, "
				+ "object String " + " ) STORED AS PARQUET;";

		if (setProvenance) {
			Provenance.storeProvenance(newTableName, oldTableName);
		}

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, finalQuery, createTableQuery);
	}
}
