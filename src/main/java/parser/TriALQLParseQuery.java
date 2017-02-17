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

package parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class TriALQLParseQuery {

	/**
	 * Class instantiating and calling ANTLR Lexer and Parser.
	 * 
	 * @param Query
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static ParseTree parse(String Query, boolean file) throws IOException {
		TriALQLLexer lexer;

		lexer = new TriALQLLexer(new ANTLRInputStream(Query));

		CommonTokenStream tokens = new CommonTokenStream(lexer);

		TriALQLParser parser = new TriALQLParser(tokens);
		ParseTree tree = parser.parse();

		final List<String> ruleNames = Arrays.asList(TriALQLParser.ruleNames);
		final TreeViewer view = new TreeViewer(ruleNames, tree);

		// If tree is to be drawn, remove comment below.
		// view.open();

		return tree;

	}
}
