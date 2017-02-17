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

package executor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.util.List;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import data.structures.Configuration;
import data.structures.QueryStruct;
import data.structures.ResultStruct;
import generator.ComposeQueries;
import parser.TriALQLClassListener;
import parser.TriALQLParseQuery;

/**
 * Impala main execution class.
 */
public class AppImpala {
	public static boolean theLastQuery = false;
	public static boolean runOnImpala;
	static ResultSet results = null;
	static String notToDrop = "";
	static boolean parseTree = true;

	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			System.exit(0);
		}

		// Check if E-TriAL-QL query is found as inline argument or as a
		// filename which contains the query.
		boolean file = false;
		if (args[0].equals("-f")) {
			file = true;
		}

		runOnImpala = true;

		String theQuery = "";

		theQuery = args[0];

		if (file) {
			theQuery = readFromFile(args[1].toString());
		}

		// Split multiple E-TriAL-QL queries to single ones.
		String[] queryArray = theQuery.split(";");

		// Go through each E-TriAL-QL query one by one.
		for (int i = 0; i < queryArray.length; i++) {
			if (Configuration.compositeJoin) {
				results = queryExecutor(queryArray[i] + ";", file, theLastQuery, parseTree);
			} else {

				if (queryArray[i].contains("DROP")) {
					dropOperator(queryArray, i, file);
				} else if (queryArray[i].contains("LOAD")) {
					// Do nothing
				}

				else if (i == (queryArray.length - 1)) {

					if (queryArray[i].contains("STORE")) {
						results = storeOperator(queryArray, i, file);
						ImpalaDaemon.noReturn("COMPUTE STATS " + QueryStruct.newTableName.get(i));
					} else {
						theLastQuery = true;
						results = queryExecutor(queryArray[i] + ";", file, theLastQuery, parseTree);
					}

				} else {
					queryExecutor(queryArray[i] + ";", file, theLastQuery, parseTree);
					ImpalaDaemon.noReturn("COMPUTE STATS " + QueryStruct.newTableName.get(i));
				}
			}
		}

		// Compose the queries, if necessary.
		if (Configuration.noFixedPoint && Configuration.compositeJoin) {
			ComposeQueries.composeMultipleJoinQueries();
			results = ResultStruct.results;
		} else if (Configuration.compositeJoin) {
			ComposeQueries.composeMultipleJoinQueriesFixed();
			results = ResultStruct.results;
		}

		try {

			while (results.next()) {

			}

			saveResult(results);

			System.out.println("Results are written");

			dropTables(notToDrop);
			System.out.println("Tables are dropped");

			ImpalaDaemon.closeConnection();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	/**
	 * Execute single Impala query.
	 * 
	 * @param singleQuery
	 * @param file
	 * @param theLastQuery
	 * @param parseTree
	 * @return
	 * @throws Exception
	 */
	private static ResultSet queryExecutor(String singleQuery, boolean file, boolean theLastQuery, boolean parseTree)
			throws Exception {
		if (parseTree) {
			ParseTree tree = TriALQLParseQuery.parse(singleQuery, file);
			TriALQLClassListener extractor = new TriALQLClassListener();
			ParseTreeWalker.DEFAULT.walk(extractor, tree);
		}

		ResultSet results = null;
		int currentQueryCounter = QueryStruct.queryCounter - 1;

		if (!QueryStruct.finalQuery.equals("none")) {

			if (theLastQuery && parseTree) {
				results = ImpalaDaemon.main(QueryStruct.baseQuery.get(currentQueryCounter));
			} else {
				ImpalaDaemon.noReturn(QueryStruct.createTableQuery);
				ImpalaDaemon.noReturn(QueryStruct.finalQuery);
			}
			return results;
		} else if (!Configuration.compositeJoin) {
			return ResultStruct.results;
		} else {
			return results;
		}
	}

	/**
	 * Save the results and Impala queries to the local system.
	 * 
	 * @param results
	 * @throws Exception
	 */
	public static void saveResult(ResultSet results) throws Exception {

		if (Configuration.saveToFiles) {
			if (results != null) {
				PrintWriter writer = new PrintWriter("Results/result.txt", "UTF-8");

				int columnNumber = 3;
				if (!TriALQLClassListener.provenance.equals("") || TriALQLClassListener.setProvenance) {
					columnNumber = 4;
				}

				while (results.next()) {
					results.next();
					String result = "";
					for (int i = 1; i <= columnNumber; i++) {
						result += results.getString(i) + "\t";
						System.out.println(result);
					}
					writer.println(result);
				}
				writer.close();
			}
		}

		PrintWriter writer = new PrintWriter("Results/queries.txt", "UTF-8");

		for (String query : QueryStruct.baseQuery) {
			writer.println(query);
		}
		writer.close();
	}

	/**
	 * DROP IMPALA tables.
	 * 
	 * @param notToDrop
	 */
	public static void dropTables(String notToDrop) {

		String dropTable = "DROP TABLE IF EXISTS ";
		for (int i = 0; i < (QueryStruct.queryCounter - 1); i++) {

			if (!QueryStruct.newTableName.get(i).equals(notToDrop) && !Configuration.compositeJoin) {
				ImpalaDaemon.noReturn(dropTable + QueryStruct.newTableName.get(i) + ";");
			}
		}
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS tmp");
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS temp");
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS deltaP");
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS deltaQ");
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS tmpl");
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS tmpr");
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS deltapl");
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS deltapr");
		ImpalaDaemon.noReturn("DROP TABLE IF EXISTS deltaK");

	}

	/**
	 * Special case: STORE operator.
	 * 
	 * @param queryArray
	 * @param i
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static ResultSet storeOperator(String[] queryArray, int i, boolean file) throws Exception {
		ParseTree tree = TriALQLParseQuery.parse(queryArray[i] + ";", file);
		TriALQLClassListener extractor = new TriALQLClassListener();
		ParseTreeWalker.DEFAULT.walk(extractor, tree);

		theLastQuery = true;
		if (QueryStruct.oldTableName.get(i).equals(QueryStruct.newTableName.get(i))) {
			// results = ImpalaDaemon.main(QueryStruct.baseQuery.get(i - 1));
			notToDrop = QueryStruct.newTableName.get(i);
		} else {
			parseTree = false;
			queryExecutor(queryArray[i] + ";", file, theLastQuery, parseTree);

			results = ImpalaDaemon.main(QueryStruct.baseQuery.get(i));
		}
		return results;
	}

	/**
	 * Special case: DROP operator.
	 * 
	 * @param queryArray
	 * @param i
	 * @param file
	 * @throws Exception
	 */
	public static void dropOperator(String[] queryArray, int i, boolean file) throws Exception {
		ParseTree tree = TriALQLParseQuery.parse(queryArray[i] + ";", file);
		TriALQLClassListener extractor = new TriALQLClassListener();
		ParseTreeWalker.DEFAULT.walk(extractor, tree);
		ImpalaDaemon.noReturn(QueryStruct.baseQuery.get(i));
	}

	/**
	 * Read query from file.
	 * 
	 * @param thePath
	 * @return
	 * @throws IOException
	 */
	public static String readFromFile(String thePath) throws IOException {
		List<String> allLines = Files.readAllLines(Paths.get(thePath));

		String query = "";
		for (String line : allLines) {
			query += line;
		}
		return query;
	}

	/**
	 * Append result number to file.
	 * 
	 * @param Result
	 * @throws IOException
	 */
	public static void writeResult(String Result) throws IOException {
		Files.write(Paths.get("Results/time.txt"), Result.getBytes(), StandardOpenOption.APPEND);
	}

}