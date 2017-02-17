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

import executor.AppImpala;
import executor.AppSpark;

/**
 * Add Provenance to non-recursive E-TriAL-QL operations.
 */
public class Provenance {

	/**
	 * Add Provenance to E-TriAL-QL Selector operations.
	 * 
	 * @param newTableName
	 * @param provenance
	 */
	static void addProvenance(String newTableName, String provenance) {

		String[] beforeFROM = FilterSelector.baseQuery.split("FROM", 2);
		FilterSelector.baseQuery = beforeFROM[0] + provenance + " FROM " + beforeFROM[1];

		FilterSelector.finalQuery = "INSERT INTO " + newTableName + " " + FilterSelector.baseQuery;
		FilterSelector.createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, "
				+ "predicate String, " + "object String " + ", provenance String " + " ) STORED AS PARQUET;";
	}

	/**
	 * Add provenance to UNION E-TriAL-QL operator.
	 * 
	 * @param newTableName
	 * @param table1ShortForm
	 * @param table2ShortForm
	 * @param oldTableName
	 */
	static void unionProvenance(String newTableName, String table1ShortForm, String table2ShortForm,
			String[] oldTableName) {

		UnionSetOperation.baseQuery = "SELECT subject, predicate, object, MIN(provenance) FROM " + " (SELECT "
				+ table1ShortForm + ".subject, " + table1ShortForm + ".predicate, " + table1ShortForm + ".object, "
				+ table1ShortForm + ".provenance FROM " + oldTableName[0] + " " + table1ShortForm + " UNION ALL "
				+ " SELECT " + table2ShortForm + ".subject, " + table2ShortForm + ".predicate, " + table2ShortForm
				+ ".object, " + table2ShortForm + ".provenance FROM " + oldTableName[1] + " " + table2ShortForm
				+ ") MyTableU GROUP BY subject, predicate, object";

		UnionSetOperation.finalQuery = "INSERT INTO " + newTableName + " " + UnionSetOperation.baseQuery;

		UnionSetOperation.createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, "
				+ "predicate String, " + "object String " + ", provenance String " + " ) STORED AS PARQUET;";
	}

	/**
	 * Add provenance to set E-TriAL-QL (INTERSECT and MINUS) operations.
	 * 
	 * @param newTableName
	 * @param table1ShortForm
	 * @param table2ShortForm
	 * @param oldTableName
	 * @param existance
	 */
	static void setProvenance(String newTableName, String table1ShortForm, String table2ShortForm,
			String[] oldTableName, String existance) {

		SetOperations.baseQuery = "SELECT  " + table1ShortForm + ".subject, " + table1ShortForm + ".predicate, "
				+ table1ShortForm + ".object , " + table1ShortForm + ".provenance FROM " + oldTableName[0] + " "
				+ table1ShortForm + existance + oldTableName[1] + " " + table2ShortForm + " ON " + table1ShortForm
				+ ".subject = " + table2ShortForm + ".subject AND " + table1ShortForm + ".predicate = "
				+ table2ShortForm + ".predicate AND " + table1ShortForm + ".object = " + table2ShortForm + ".object ";

		if (existance.equals(" LEFT JOIN ")) {
			SetOperations.baseQuery = SetOperations.baseQuery + " WHERE " + table2ShortForm + ".predicate IS NULL";
		}

		SetOperations.finalQuery = "INSERT INTO " + newTableName + " " + SetOperations.baseQuery;

		SetOperations.createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, "
				+ "predicate String, " + "object String " + ", provenance String " + " ) STORED AS PARQUET;";

	}

	/**
	 * STORE operator with Provenance.
	 * 
	 * @param newTableName
	 * @param oldTableName
	 */
	static void storeProvenance(String newTableName, String[] oldTableName) {

		StoreOperation.baseQuery = "SELECT subject AS subject, predicate AS predicate, object AS object,"
				+ " provenance AS provenance FROM " + oldTableName[0];

		StoreOperation.finalQuery = "INSERT INTO " + newTableName
				+ " SELECT subject, predicate, object, provenance FROM " + oldTableName[0];

		StoreOperation.createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, "
				+ "predicate String, " + "object String " + ", provenance String " + " ) STORED AS PARQUET;";
	}

	/**
	 * Drop operator with Provenance.
	 * 
	 * @param oldTableName
	 */
	static void dropProvenance(String[] oldTableName) {
		if (AppImpala.runOnImpala) {
			DropOperation.baseQuery = "ALTER TABLE " + oldTableName[0] + " DROP provenance";
		} else if (AppSpark.runOnSPARK) {
			DropOperation.baseQuery = "Delete table from HDFS, then create new table without provenance";
		}

	}

	/**
	 * Add provenance to Triple Join E-TriAL-QL operation
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param selectionArray
	 * @param joinOnExpression
	 * @param table2ShortForm
	 * @param table1ShortForm
	 * @param whereExpression
	 * @param provenanceAppenderList
	 */
	static void appendProvenance(String[] oldTableName, String newTableName, String[] selectionArray,
			ArrayList<String> joinOnExpression, String table2ShortForm, String table1ShortForm, String whereExpression,
			ArrayList<Integer> provenanceAppenderList) {

		String provenance = ", MIN(CONCAT(";

		if (provenanceAppenderList.get(0) == 1) {
			provenance = provenance + table1ShortForm + ".provenance, '/', " + table2ShortForm
					+ ".provenance)) AS provenance";
		} else if (provenanceAppenderList.get(0) == 2) {
			provenance = provenance + table2ShortForm + ".provenance, '/', " + table1ShortForm
					+ ".provenance)) AS provenance";
		}

		JoinSelector.baseQuery = "SELECT " + selectionArray[0] + " AS subject, " + selectionArray[1] + " AS predicate, "
				+ selectionArray[2] + " AS object" + provenance + " FROM " + oldTableName[0] + " " + table1ShortForm
				+ " JOIN " + oldTableName[1] + " " + table2ShortForm + " ON " + joinOnExpression.get(0) + " "
				+ joinOnExpression.get(1) + " " + joinOnExpression.get(2);

		if (joinOnExpression.size() > 3) {
			for (int i = 3; i < joinOnExpression.size(); i = i + 3) {
				JoinSelector.baseQuery = JoinSelector.baseQuery + " AND " + joinOnExpression.get(i) + " "
						+ joinOnExpression.get(i + 1) + " " + joinOnExpression.get(i + 2);
			}
		}

		if (!(whereExpression == null)) {
			JoinSelector.baseQuery = JoinSelector.baseQuery + " WHERE " + whereExpression;
		}

		JoinSelector.baseQuery = JoinSelector.baseQuery + " GROUP BY " + selectionArray[0] + ", " + selectionArray[1]
				+ ", " + selectionArray[2];

		JoinSelector.finalQuery = "INSERT INTO " + newTableName + " " + JoinSelector.baseQuery;

		JoinSelector.createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, "
				+ "predicate String, " + "object String " + ", provenance String " + " ) STORED AS PARQUET;";

	}
}
