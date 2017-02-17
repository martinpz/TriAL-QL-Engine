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

import data.structures.QueryStruct;

public class KleeneFixedProvenance {
	public static String finalQuery;
	public static String createTableQuery;
	public static String baseQuery = "";

	/**
	 * Generate Impala/ SPARL SQL queries from E-TriAL-QL bounded Kleene with
	 * Provenance operations.
	 * 
	 * @param oldTableName
	 * @param newTableName
	 * @param whereExpression
	 * @param joinOnExpression
	 * @param kleeneDepth1
	 * @param kleeneDepth2
	 * @param kleeneType
	 * @param selectionPart
	 * @param provenanceAppenderList
	 * @param setProvenance
	 */
	public static void CreateQuery(String[] oldTableName, String newTableName, String whereExpression,
			ArrayList<String> joinOnExpression, int kleeneDepth1, int kleeneDepth2, String kleeneType,
			String[] selectionPart, ArrayList<Integer> provenanceAppenderList, boolean setProvenance) {

		ArrayList<String> joins = new ArrayList<String>();
		String tableShortForm = oldTableName[0].substring(0, 2);

		int kleeneDepth = 0;
		int m = 0;
		int n = 0;
		boolean noLiterals = false;
		boolean r1First = false;
		String provenance = "";

		if (kleeneDepth2 < 0) {
			kleeneDepth = kleeneDepth1;
			m = kleeneDepth - 1;
			n = kleeneDepth - 1;
		} else if (kleeneDepth2 != 0) {
			kleeneDepth = kleeneDepth2;
			m = kleeneDepth1 - 1;
			n = kleeneDepth2 - 1;
			if (kleeneDepth1 == 0) {
				m = 0;
			}
		}

		if (provenanceAppenderList.get(0) == 1)
			r1First = true;

		// Right Kleene
		if (kleeneType.equals("right")) {
			if (r1First) {
				provenance = ", MIN(CONCAT(" + tableShortForm + 1 + ".provenance, '/', " + tableShortForm + 2
						+ ".provenance)) AS provenance";
			} else {
				provenance = ", MIN(CONCAT(" + tableShortForm + 2 + ".provenance, '/', " + tableShortForm + 1
						+ ".provenance)) AS provenance";
			}
			String firtstJoin = "SELECT " + tableShortForm + selectionPart[0] + "." + selectionPart[1] + " AS subject, "
					+ tableShortForm + selectionPart[2] + "." + selectionPart[3] + " AS predicate, " + tableShortForm
					+ selectionPart[4] + "." + selectionPart[5] + " AS object" + provenance + " FROM " + oldTableName[0]
					+ " " + tableShortForm + 1 + " JOIN " + oldTableName[0] + " " + tableShortForm + 2 + " ON ";

			for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
				if (k > 0)
					firtstJoin = firtstJoin + " AND ";

				firtstJoin = firtstJoin + tableShortForm + Integer.toString(1) + "."
						+ joinOnExpression.get(k).toString().substring(4) + " " + joinOnExpression.get(k + 1) + " "
						+ tableShortForm + 2 + "." + joinOnExpression.get(k + 2).toString().substring(4);
			}

			String where = "";
			if (joinOnExpression.get(0).toString().substring(4).equals("object")
					|| joinOnExpression.get(2).toString().substring(4).equals("object")) {
				where = " WHERE " + tableShortForm + 1 + ".object NOT like '\"%'";
				noLiterals = true;
			}

			if (whereExpression != null) {
				if (noLiterals) {
					where = where + " AND " + tableShortForm + 1 + whereExpression.substring(3);
				} else {
					where = " WHERE " + tableShortForm + 1 + whereExpression.substring(3);
				}
			}

			firtstJoin = firtstJoin + where + " GROUP BY " + tableShortForm + 1 + "." + selectionPart[1] + ", "
					+ tableShortForm + selectionPart[2] + "." + selectionPart[3] + ", " + tableShortForm
					+ selectionPart[4] + "." + selectionPart[5];
			joins.add(firtstJoin);
			String secondSelection = "";

			for (int i = 2; i <= kleeneDepth; i++) {

				if (r1First) {
					provenance = ", MIN(CONCAT( MyTable" + (i - 1) + ".provenance, '/', " + tableShortForm + (i + 1)
							+ ".provenance)) AS provenance";
				} else {
					provenance = ", MIN(CONCAT(" + tableShortForm + (i + 1) + ".provenance, '/', MyTable" + (i - 1)
							+ ".provenance)) AS provenance";
				}

				if (i == 2) {

					if (selectionPart[2].equals("2"))
						secondSelection = ", " + tableShortForm + 3 + ".";
					else
						secondSelection = ", MyTable1" + ".";

					baseQuery = "SELECT MyTable1." + selectionPart[1] + secondSelection + selectionPart[3] + ", "
							+ tableShortForm + 3 + "." + selectionPart[5] + provenance + " FROM " + " ( " + firtstJoin
							+ " ) MyTable1" + " JOIN " + oldTableName[0] + " " + oldTableName[0].substring(0, 2) + 3;
				} else {
					if (selectionPart[2].equals("2"))
						secondSelection = ", " + tableShortForm + (i + 1) + ".";
					else
						secondSelection = ", " + "MyTable" + (i - 1) + ".";

					baseQuery = "SELECT MyTable" + (i - 1) + "." + selectionPart[1] + secondSelection + selectionPart[3]
							+ ", " + tableShortForm + (i + 1) + "." + selectionPart[5] + provenance + " FROM " + " ( "
							+ baseQuery + " ) MyTable" + (i - 1) + " JOIN " + oldTableName[0] + " " + tableShortForm
							+ (i + 1);

				}

				baseQuery = baseQuery + " ON ";

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0)
						baseQuery = baseQuery + " AND ";

					baseQuery = baseQuery + "MyTable" + (i - 1) + "." + joinOnExpression.get(k).toString().substring(4)
							+ " " + joinOnExpression.get(k + 1) + " " + tableShortForm + (i + 1) + "."
							+ joinOnExpression.get(k + 2).toString().substring(4);
				}

				if (joinOnExpression.get(0).toString().substring(4).equals("object")
						|| joinOnExpression.get(2).toString().substring(4).equals("object")) {
					where = " WHERE MyTable" + Integer.toString(i - 1) + ".object NOT like '\"%'";
				}

				if (whereExpression != null) {
					if (noLiterals) {
						where = where + " AND MyTable" + Integer.toString(i - 1) + whereExpression.substring(3);
					} else {
						where = " WHERE MyTable" + Integer.toString(i - 1) + whereExpression.substring(3);
					}
				}
				where = where + " GROUP BY " + "MyTable" + (i - 1) + "." + selectionPart[1] + ", "
						+ secondSelection.substring(2) + selectionPart[3] + ", " + tableShortForm + (i + 1) + ".object";

				baseQuery = baseQuery + where;
				joins.add(baseQuery);
			}

			baseQuery = "";
			if (kleeneDepth1 == 0) {

				baseQuery = "SELECT subject AS subject, predicate AS predicate, object AS object, provenance"
						+ " AS provenance FROM " + oldTableName[0];
			}

			for (int i = m; i <= n; i++) {
				if (baseQuery == "")
					baseQuery = joins.get(i);
				else
					baseQuery = baseQuery + " UNION ALL " + " ( " + joins.get(i) + " ) ";
			}

			baseQuery = "SELECT subject, predicate, object, MIN(provenance) FROM (" + baseQuery + ")"
					+ " MyTableU GROUP BY subject, predicate, object";

			// Left Kleene
		} else if (kleeneType.equals("left")) {
			if (r1First) {
				provenance = ", MIN(CONCAT(" + tableShortForm + 1 + ".provenance, '/', " + tableShortForm + 2
						+ ".provenance)) AS provenance";
			} else {
				provenance = ", MIN(CONCAT(" + tableShortForm + 2 + ".provenance, '/', " + tableShortForm + 1
						+ ".provenance)) AS provenance";
			}
			String firtstJoin = "SELECT " + tableShortForm + selectionPart[0] + "." + selectionPart[1] + " AS subject, "
					+ tableShortForm + selectionPart[2] + "." + selectionPart[3] + " AS predicate, " + tableShortForm
					+ selectionPart[4] + "." + selectionPart[5] + " AS object" + provenance + " FROM " + oldTableName[0]
					+ " " + tableShortForm + 1 + " JOIN " + oldTableName[0] + " " + tableShortForm + 2 + " ON ";

			for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
				if (k > 0)
					firtstJoin = firtstJoin + " AND ";

				firtstJoin = firtstJoin + tableShortForm + Integer.toString(1) + "."
						+ joinOnExpression.get(k).toString().substring(4) + " " + joinOnExpression.get(k + 1) + " "
						+ tableShortForm + 2 + "." + joinOnExpression.get(k + 2).toString().substring(4);
			}

			String where = "";
			if (joinOnExpression.get(0).toString().substring(4).equals("object")
					|| joinOnExpression.get(2).toString().substring(4).equals("object")) {
				where = " WHERE " + tableShortForm + 1 + ".object NOT like '\"%'";
				noLiterals = true;
			}

			if (whereExpression != null) {
				if (noLiterals) {
					where = where + " AND " + tableShortForm + 1 + whereExpression.substring(3);
				} else {
					where = " WHERE " + tableShortForm + 1 + whereExpression.substring(3);
				}
			}

			firtstJoin = firtstJoin + where + " GROUP BY " + tableShortForm + 1 + "." + selectionPart[1] + ", "
					+ tableShortForm + selectionPart[2] + "." + selectionPart[3] + ", " + tableShortForm
					+ selectionPart[4] + "." + selectionPart[5];
			joins.add(firtstJoin);

			String secondSelection = "";

			for (int i = 2; i <= kleeneDepth; i++) {
				if (r1First) {
					provenance = ", MIN(CONCAT(" + tableShortForm + (i + 1) + ".provenance, '/', MyTable" + (i - 1)
							+ ".provenance)) AS provenance";
				} else {
					provenance = ", MIN(CONCAT( MyTable" + (i - 1) + ".provenance, '/', " + tableShortForm + (i + 1)
							+ ".provenance)) AS provenance";
				}

				if (i == 2) {
					if (!selectionPart[2].equals("2"))
						secondSelection = ", " + tableShortForm + 3 + ".";
					else
						secondSelection = ", MyTable1.";

					baseQuery = "SELECT " + tableShortForm + 3 + "." + selectionPart[1] + secondSelection
							+ selectionPart[3] + ", " + "MyTable1." + selectionPart[5] + provenance + " FROM "
							+ oldTableName[0] + " " + oldTableName[0].substring(0, 2) + 3 + " JOIN " + " ( "
							+ firtstJoin + " ) MyTable1";
				} else {
					if (!selectionPart[2].equals("2"))
						secondSelection = ", " + tableShortForm + (i + 1) + ".";
					else
						secondSelection = ", " + "MyTable" + (i - 1) + ".";

					baseQuery = "SELECT " + tableShortForm + (i + 1) + "." + selectionPart[1] + secondSelection
							+ selectionPart[3] + ", MyTable" + (i - 1) + "." + selectionPart[5] + provenance + " FROM "
							+ oldTableName[0] + " " + tableShortForm + (i + 1) + " JOIN " + " ( " + baseQuery
							+ " ) MyTable" + (i - 1);
				}

				baseQuery = baseQuery + " ON ";

				for (int k = 0; k < joinOnExpression.size(); k = k + 3) {
					if (k > 0)
						baseQuery = baseQuery + " AND ";

					baseQuery = baseQuery + tableShortForm + (i + 1) + "."
							+ joinOnExpression.get(k).toString().substring(4) + " " + joinOnExpression.get(k + 1)
							+ " MyTable" + (i - 1) + "." + joinOnExpression.get(k + 2).toString().substring(4);
				}

				if (joinOnExpression.get(0).toString().substring(4).equals("object")
						|| joinOnExpression.get(2).toString().substring(4).equals("object")) {
					where = " WHERE " + tableShortForm + Integer.toString(i + 1) + ".object NOT like '\"%'";
				}

				if (whereExpression != null) {
					if (noLiterals) {
						where = where + " AND " + tableShortForm + Integer.toString(i + 1)
								+ whereExpression.substring(3);
					} else {
						where = " WHERE " + tableShortForm + Integer.toString(i + 1) + whereExpression.substring(3);
					}
				}

				where = where + " GROUP BY " + tableShortForm + (i + 1) + "." + selectionPart[1] + ", "
						+ secondSelection.substring(2) + selectionPart[3] + ", " + "MyTable" + (i - 1) + ".object";

				baseQuery = baseQuery + where;
				joins.add(baseQuery);
			}

			baseQuery = "";
			if (kleeneDepth1 == 0) {

				baseQuery = "SELECT subject AS subject, predicate AS predicate, object AS object, provenance"
						+ " AS provenance FROM " + oldTableName[0];

				if (whereExpression != null) {
					baseQuery = baseQuery + " WHERE " + whereExpression.substring(4);
				}
			}

			for (int i = m; i <= n; i++) {
				if (baseQuery == "")
					baseQuery = joins.get(i);
				else
					baseQuery = baseQuery + " UNION ALL " + " ( " + joins.get(i) + " ) ";
			}

			baseQuery = "SELECT subject, predicate, object, MIN(provenance) FROM (" + baseQuery + ")"
					+ " MyTableU GROUP BY subject, predicate, object";
		}

		// baseQuery = "SELECT COUNT(*) FROM ( " + baseQuery + " ) MyTablek";
		finalQuery = "INSERT INTO " + newTableName + " " + baseQuery;

		createTableQuery = "CREATE TABLE " + newTableName + " ( " + "subject String, " + "predicate String, "
				+ "object String " + " ) STORED AS PARQUET;";

		QueryStruct.fillStructure(oldTableName, newTableName, baseQuery, finalQuery, createTableQuery);
	}

}
