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
import data.structures.ResultStruct;
import executor.AppImpala;
import executor.AppSpark;
import executor.ImpalaDaemon;

/**
 * Compose 1 compositional SQL query from multiple E-TriAL-QL queries
 */
public class ComposeQueries {

	/**
	 * Compose queries for a Parallel join execution.
	 */
	public static void composeMultipleJoinQueries() {
		String compositeQuery = "";
		for (int i = 0; i < QueryStruct.baseQuery.size(); i++) {
			if (i == QueryStruct.baseQuery.size() - 1) {

				compositeQuery = compositeQuery + " ( " + QueryStruct.baseQuery.get(i) + ") tab" + i + " ON(tab"
						+ (i - 1) + ".object = tab" + i + ".subject)";
				if (AppImpala.runOnImpala) {
					compositeQuery = "SELECT COUNT(*) FROM" + compositeQuery;
				} else {
					compositeQuery = "SELECT * FROM" + compositeQuery;
				}

			} else if (i == 0) {
				compositeQuery = compositeQuery + " ( " + QueryStruct.baseQuery.get(i) + ") tab" + i + " JOIN ";
			} else {
				compositeQuery = compositeQuery + " ( " + QueryStruct.baseQuery.get(i) + ") tab" + i + " ON(tab"
						+ (i - 1) + ".object = tab" + i + ".subject)" + " JOIN ";
			}
		}
		QueryStruct.baseQuery.clear();
		QueryStruct.baseQuery.add(compositeQuery);

		if (AppImpala.runOnImpala) {
			ResultStruct.fillStructure(ImpalaDaemon.main(compositeQuery));
		} else {
			ResultStruct.fillStructureSpark(AppSpark.sqlContext.sql(compositeQuery));
		}

	}

	/**
	 * Compose queries for an ordered join execution.
	 */
	public static void composeMultipleJoinQueriesFixed() {
		String compositeQuery = "WITH " + QueryStruct.newTableName.get(0) + " AS ( ";
		for (int i = 0; i < QueryStruct.baseQuery.size(); i++) {

			if (i == 0) {
				compositeQuery = compositeQuery + QueryStruct.baseQuery.get(i) + "), ";
			} else if (i == QueryStruct.baseQuery.size() - 1) {
				compositeQuery = compositeQuery + QueryStruct.newTableName.get(i) + " AS ( "
						+ QueryStruct.baseQuery.get(i) + ") ";
				if (AppImpala.runOnImpala) {
					compositeQuery = compositeQuery + " SELECT COUNT(*) FROM " + QueryStruct.newTableName.get(i);

				} else {
					compositeQuery = compositeQuery + " SELECT * FROM " + QueryStruct.newTableName.get(i);
				}

			} else {
				compositeQuery = compositeQuery + QueryStruct.newTableName.get(i) + " AS ( "
						+ QueryStruct.baseQuery.get(i) + "), ";
			}

		}

		QueryStruct.baseQuery.clear();
		QueryStruct.baseQuery.add(compositeQuery);

		if (AppImpala.runOnImpala) {
			ResultStruct.fillStructure(ImpalaDaemon.main(compositeQuery));
		} else {
			ResultStruct.fillStructureSpark(AppSpark.sqlContext.sql(compositeQuery));
		}
	}
}
