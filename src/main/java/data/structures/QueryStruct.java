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

package data.structures;

import java.util.ArrayList;

/**
 * Data structure which holds the Impala/SPARK SQL queries and more information
 * which will be required during execution.
 */
public class QueryStruct {

	public static ArrayList<String> oldTableName = new ArrayList<String>();
	public static ArrayList<String> newTableName = new ArrayList<String>();
	public static ArrayList<String> baseQuery = new ArrayList<String>();
	public static String finalQuery;
	public static String createTableQuery;
	public static int queryCounter = 0;

	public static void fillStructure(String[] old, String newTable, String base, String finalQ, String createTable) {
		oldTableName.add(old[0]);
		newTableName.add(newTable);
		baseQuery.add(base);
		finalQuery = finalQ;
		createTableQuery = createTable;
		queryCounter++;
	}
}
