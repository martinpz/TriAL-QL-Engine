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

package loader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import executor.ImpalaDaemon;

public class VerticalPartitionerImpala {

	/**
	 * Impala Vertical Partitioner (preferred VP).
	 * @param inputPath
	 */
	public static void partition(String inputPath) {
		long lStartTime = System.nanoTime();
		try {
			String rawtableQuery = "CREATE TABLE rawgraph(subject string, predicate string, object string) row format delimited fields terminated by '\t'"
					+ "location '" + inputPath + "';";
			ImpalaDaemon.noReturn(rawtableQuery);
			
			String tableQuery = "CREATE TABLE graph (subject string, object string) PARTITIONED BY (predicate string) STORED AS PARQUET;";
			ImpalaDaemon.noReturn(tableQuery);
			
			String predicatesQuery = "SELECT predicate FROM rawgraph WHERE subject != '@prefix' GROUP BY predicate";
			ResultSet getPredicates = ImpalaDaemon.main(predicatesQuery);
			ArrayList<String> predicates = new ArrayList<String>();
			
			while (getPredicates.next()) {
				predicates.add(getPredicates.getString(1));
			}
			
			for (String predicate : predicates) {
				String Query = "INSERT INTO graph partition(predicate='" + predicate
						+ "') SELECT subject,object FROM rawgraph WHERE predicate ='" + predicate + "'";
				ImpalaDaemon.noReturn(Query);
			}
		
			ImpalaDaemon.noReturn("DROP TABLE rawgraph");

			String tableComputeStats = "COMPUTE STATS graph";
			ImpalaDaemon.noReturn(tableQuery);

			ImpalaDaemon.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		long lEndTime = System.nanoTime();
		long difference = lEndTime - lStartTime;

		System.out.println("Partitioning complete.\nElapsed milliseconds: " + difference / 1000000);

	}

}
