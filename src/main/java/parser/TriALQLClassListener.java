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

import java.sql.SQLException;
import java.util.ArrayList;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import data.structures.Configuration;
import executor.AppImpala;
import executor.AppSpark;
import generator.JoinSelector;
import generator.KleeneFixed;
import generator.KleeneFixedProvenance;
import generator.SetOperations;
import generator.StoreOperation;
import generator.UnionSetOperation;
import generator.DropOperation;
import generator.LoadOperation;
import generator.MergeOperation;
import hybrid.generationExecution.CompositConnectivityImpala;
import hybrid.generationExecution.CompositConnectivitySPARK;
import hybrid.generationExecution.ConnectivityPatternImpala;
import hybrid.generationExecution.ConnectivityPatternSPARK;
import hybrid.generationExecution.KleeneHeuristicsImpala;
import hybrid.generationExecution.KleeneHeuristicsSPARK;
import hybrid.generationExecution.KleeneSemiNaiveImpala;
import hybrid.generationExecution.KleeneSemiNaiveSPARK;
import hybrid.generationExecution.KleeneSmartImpala;
import hybrid.generationExecution.KleeneSmartSPARK;
import hybrid.generationExecutionProvenance.ConnectivityPatternImpalaP;
import hybrid.generationExecutionProvenance.ConnectivityPatternSPARKP;
import hybrid.generationExecutionProvenance.KleeneSemiNaiveImpalaP;
import hybrid.generationExecutionProvenance.KleeneSemiNaiveSPARKP;
import hybrid.generationExecutionProvenance.KleeneSmartImpalaP;
import hybrid.generationExecutionProvenance.KleeneSmartSPARKP;
import generator.FilterSelector;
import parser.TriALQLParser.BlockContext;
import parser.TriALQLParser.DropBlockContext;
import parser.TriALQLParser.EquationContext;
import parser.TriALQLParser.EquationListContext;
import parser.TriALQLParser.EquationOperatorContext;
import parser.TriALQLParser.FilterExprAndContext;
import parser.TriALQLParser.FilterExprContext;
import parser.TriALQLParser.KleeneDepthContext;
import parser.TriALQLParser.LiteralContext;
import parser.TriALQLParser.LoadBlockContext;
import parser.TriALQLParser.MergeBlockContext;
import parser.TriALQLParser.OperatorBlockContext;
import parser.TriALQLParser.ParseContext;
import parser.TriALQLParser.RecursionTypeContext;
import parser.TriALQLParser.SelectBlockContext;
import parser.TriALQLParser.SelectJoinBlockContext;
import parser.TriALQLParser.SelectRecursionBlockContext;
import parser.TriALQLParser.SelectionContext;
import parser.TriALQLParser.SelectionProvenanceAppenderContext;
import parser.TriALQLParser.SelectionProvenanceCreatorContext;
import parser.TriALQLParser.SelectionRecursionContext;
import parser.TriALQLParser.StoreBlockContext;
import parser.TriALQLParser.TermContext;

/**
 * During parse tree walking, methods from this class are called. One method is
 * called for entering of each block and sub-block. Another method is called
 * upon exiting each block and sub-block.
 */
public class TriALQLClassListener implements TriALQLListener {

	// Extracted table names
	public static String newTableName = "";
	public String[] oldTableNames = new String[2];

	// Extracted selection elements
	public String[] selectionArray = new String[3];
	String[] selectionPart = new String[6];
	public ArrayList<String> joinOnExpression = new ArrayList<String>();

	public static String provenance = "";

	// Extracted where clause
	public String whereExpression;
	String[] whereSelectionSPO = new String[2];

	// Extracted set operator
	public String setOperator;

	// Flags showing what part of query path is invoked
	boolean filterExpression;
	boolean equationList;

	// Extracted variables for Kleene (recursion)
	int kleeneDepth1;
	int kleeneDepth2 = -1;
	String kleeneType;

	// Pointer showing from where to extract
	int orAndPointer = 1;

	// Necessary for constants as column names and values
	public ArrayList<String> projectedColumn = new ArrayList<String>();

	boolean connectivityPattern = false;
	int sourceDestCounter = 0;
	public String[] sourceDest = new String[2];

	public static boolean isCached = false;

	public static boolean setProvenance = false;

	public ArrayList<Integer> provenanceAppenderList = new ArrayList<Integer>();

	public ArrayList<String> mergeTables = new ArrayList<String>();

	@Override
	public void enterEveryRule(ParserRuleContext arg0) {
	}

	@Override
	public void exitEveryRule(ParserRuleContext arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitErrorNode(ErrorNode arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visitTerminal(TerminalNode arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterParse(ParseContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitParse(ParseContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterBlock(BlockContext ctx) {
		// TODO Auto-generated method stub
	}

	@Override
	public void exitBlock(BlockContext ctx) {

	}

	@Override
	public void enterSelectBlock(SelectBlockContext ctx) {
		newTableName = ctx.getChild(0).getText();

		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i).getText().equals("FROM")) {
				oldTableNames[0] = ctx.getChild(i + 1).getText();
			}
		}
	}

	@Override
	public void exitSelectBlock(SelectBlockContext ctx) {
		FilterSelector.CreateQuery(oldTableNames, newTableName, whereExpression, selectionArray, projectedColumn,
				provenance);
	}

	@Override
	public void enterSelectRecursionBlock(SelectRecursionBlockContext ctx) {
		newTableName = ctx.getChild(0).getText();

		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i).getText().equals("FROM")) {
				oldTableNames[0] = ctx.getChild(i + 1).getText();
			}
			if (ctx.getChild(i).getText().equals("USING")) {
				kleeneType = ctx.getChild(i + 1).getText();
			}
		}
	}

	@Override
	public void exitSelectRecursionBlock(SelectRecursionBlockContext ctx) throws SQLException {
		if (connectivityPattern && AppImpala.runOnImpala) {
			if (setProvenance && !Configuration.compositionalConnectivity) {
				ConnectivityPatternImpalaP.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
						selectionPart, sourceDest, provenanceAppenderList);
			} else if (!Configuration.compositionalConnectivity) {
				ConnectivityPatternImpala.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
						selectionPart, sourceDest);
			} else if (Configuration.compositionalConnectivity) {
				CompositConnectivityImpala.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
						selectionPart, sourceDest);
			}

		} else if (connectivityPattern && AppSpark.runOnSPARK) {
			if (setProvenance && !Configuration.compositionalConnectivity) {
				ConnectivityPatternSPARKP.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
						selectionPart, sourceDest, provenanceAppenderList);
			} else if (!Configuration.compositionalConnectivity) {
				ConnectivityPatternSPARK.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
						selectionPart, sourceDest);
			} else if (Configuration.compositionalConnectivity) {
				CompositConnectivitySPARK.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
						selectionPart, sourceDest);
			}

		} else if ((kleeneDepth1 != 0 || kleeneDepth2 != -1) && kleeneDepth1 != -10) {
			if (setProvenance) {
				KleeneFixedProvenance.CreateQuery(oldTableNames, newTableName, whereExpression, joinOnExpression,
						kleeneDepth1, kleeneDepth2, kleeneType, selectionPart, provenanceAppenderList, setProvenance);
			} else {
				KleeneFixed.CreateQuery(oldTableNames, newTableName, whereExpression, joinOnExpression, kleeneDepth1,
						kleeneDepth2, kleeneType, selectionPart);
			}
		} else if (AppImpala.runOnImpala) {
			if (setProvenance) {
				if (Configuration.SemNaive) {
					KleeneSemiNaiveImpalaP.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
							selectionPart, kleeneDepth1, provenanceAppenderList);
				} else if (Configuration.Smart) {
					KleeneSmartImpalaP.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
							selectionPart, kleeneDepth1, provenanceAppenderList);
				}

			} else {
				if (Configuration.SemNaive) {
					KleeneSemiNaiveImpala.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
							selectionPart, kleeneDepth1, "");
				} else if (Configuration.Smart) {
					KleeneSmartImpala.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
							selectionPart, kleeneDepth1);
				} else if (Configuration.Heuristics) {
					KleeneHeuristicsImpala.CreateQuery(oldTableNames, newTableName, whereExpression, joinOnExpression,
							kleeneDepth1, kleeneDepth2, kleeneType, selectionPart);
				}

			}

		} else if ((AppSpark.runOnSPARK)) {
			if (setProvenance) {
				if (Configuration.SemNaive)
					KleeneSemiNaiveSPARKP.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
							selectionPart, kleeneDepth1, provenanceAppenderList);
				else if (Configuration.Smart) {
					KleeneSmartSPARKP.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
							selectionPart, kleeneDepth1, provenanceAppenderList);
				}

			} else {

				if (Configuration.SemNaive) {
					KleeneSemiNaiveSPARK.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
							selectionPart, kleeneDepth1, "");
				} else if (Configuration.Smart) {
					KleeneSmartSPARK.CreateQuery(oldTableNames, newTableName, joinOnExpression, kleeneType,
							selectionPart, kleeneDepth1);
				} else if (Configuration.Heuristics) {
					KleeneHeuristicsSPARK.CreateQuery(oldTableNames, newTableName, whereExpression, joinOnExpression,
							kleeneDepth1, kleeneDepth2, kleeneType, selectionPart);
				}

			}
		}
	}

	@Override
	public void enterSelectJoinBlock(SelectJoinBlockContext ctx) {
		newTableName = ctx.getChild(0).getText();

		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i).getText().equals("FROM")) {
				oldTableNames[0] = ctx.getChild(i + 1).getText();
			} else if (ctx.getChild(i).getText().equals("JOIN")) {
				oldTableNames[1] = ctx.getChild(i + 1).getText();
			}
		}
	}

	@Override
	public void exitSelectJoinBlock(SelectJoinBlockContext ctx) {
		JoinSelector.CreateQuery(oldTableNames, newTableName, whereExpression, selectionArray, joinOnExpression,
				projectedColumn, setProvenance, provenanceAppenderList);
	}

	@Override
	public void enterOperatorBlock(OperatorBlockContext ctx) {
		newTableName = ctx.getChild(0).getText();
		oldTableNames[0] = ctx.getChild(2).getText();
		oldTableNames[1] = ctx.getChild(4).getText();
		setOperator = ctx.getChild(3).getText();
		try {
			if (ctx.getChild(5).getText().equals("WITH") && ctx.getChild(6).getText().equals("PROVENANCE")) {
				setProvenance = true;
			}

		} catch (Exception e) {

		}
	}

	@Override
	public void exitOperatorBlock(OperatorBlockContext ctx) {
		if (setOperator.equals("UNION")) {
			UnionSetOperation.CreateQuery(oldTableNames, newTableName, setOperator, setProvenance);
		} else {
			SetOperations.CreateQuery(oldTableNames, newTableName, setOperator, setProvenance);
		}
	}

	@Override
	public void enterStoreBlock(StoreBlockContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i++) {
			if (ctx.getChild(i).getText().equals("AS")) {
				oldTableNames[0] = ctx.getChild(i - 1).getText();
				newTableName = ctx.getChild(i + 1).getText();
			} else if (ctx.getChild(i).getText().equals("PROVENANCE")) {
				setProvenance = true;
			}

		}
	}

	@Override
	public void exitStoreBlock(StoreBlockContext ctx) {
		StoreOperation.CreateQuery(oldTableNames, newTableName, setProvenance);

	}

	@Override
	public void enterDropBlock(DropBlockContext ctx) {
		if (ctx.getChild(1).getText().equals("PROVENANCE")) {
			oldTableNames[0] = ctx.getChild(2).getText();
			setProvenance = true;
		} else {
			oldTableNames[0] = ctx.getChild(1).getText();
		}
	}

	@Override
	public void exitDropBlock(DropBlockContext ctx) {
		DropOperation.CreateQuery(oldTableNames, setProvenance);
	}

	@Override
	public void enterEquationList(EquationListContext ctx) {
		equationList = true;
	}

	@Override
	public void exitEquationList(EquationListContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterEquation(EquationContext ctx) {
		String whereSelectionOperator = ctx.getChild(1).getText();
		String whereSelectionTerm = ctx.getChild(2).getText();
		int whereSelectionSPOCounter = 0;

		for (int i = 0; i < ctx.getChildCount(); i = i + 2) {
			if (ctx.getChild(i).getText().equals("s1")) {
				whereSelectionSPO[whereSelectionSPOCounter] = "1.subject";
				whereSelectionSPOCounter++;
			} else if (ctx.getChild(i).getText().equals("p1")) {
				whereSelectionSPO[whereSelectionSPOCounter] = "1.predicate";
				whereSelectionSPOCounter++;
			} else if (ctx.getChild(i).getText().equals("o1")) {
				whereSelectionSPO[whereSelectionSPOCounter] = "1.object";
				whereSelectionSPOCounter++;
			} else if (ctx.getChild(i).getText().equals("s2")) {
				whereSelectionSPO[whereSelectionSPOCounter] = "2.subject";
				whereSelectionSPOCounter++;
			} else if (ctx.getChild(i).getText().equals("p2")) {
				whereSelectionSPO[whereSelectionSPOCounter] = "2.predicate";
				whereSelectionSPOCounter++;
			} else if (ctx.getChild(i).getText().equals("o2")) {
				whereSelectionSPO[whereSelectionSPOCounter] = "2.object";
				whereSelectionSPOCounter++;
			}
		}

		if (filterExpression) {

			if (ctx.getChild(0).getText().equals("s1")) {
				sourceDestCounter++;
				sourceDest[0] = ctx.getChild(2).getText();
			} else if (ctx.getChild(0).getText().equals("o2")) {
				sourceDestCounter++;
				sourceDest[1] = ctx.getChild(2).getText();
			}

			if (sourceDestCounter == 2) {
				connectivityPattern = true;
			}

			if (whereExpression == null) {
				whereExpression = oldTableNames[0].substring(0, 2) + whereSelectionSPO[0] + " " + whereSelectionOperator
						+ " " + whereSelectionTerm;
			} else if (ctx.getParent().getChildCount() > 1
					&& ctx.getParent().getChild(orAndPointer).toString().equals("AND")) {
				whereExpression = whereExpression + " AND (" + oldTableNames[0].substring(0, 2) + whereSelectionSPO[0]
						+ " " + whereSelectionOperator + " " + whereSelectionTerm + ")";
				orAndPointer += 2;
			} else {
				whereExpression = " " + whereExpression.substring(0, whereExpression.length() - 1) + " OR "
						+ oldTableNames[0].substring(0, 2) + whereSelectionSPO[0] + " " + whereSelectionOperator + " "
						+ whereSelectionTerm + " )";
				orAndPointer += 2;
			}
		} else if (equationList) {
			joinOnExpression.add(oldTableNames[0].substring(0, 2) + whereSelectionSPO[0]);
			joinOnExpression.add(whereSelectionOperator);
			joinOnExpression.add(oldTableNames[0].substring(0, 2) + whereSelectionSPO[1]);
		}
	}

	@Override
	public void exitEquation(EquationContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterEquationOperator(EquationOperatorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitEquationOperator(EquationOperatorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterTerm(TermContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitTerm(TermContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterLiteral(LiteralContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitLiteral(LiteralContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterSelection(SelectionContext ctx) {
		int selectionArrayPosition = 0;

		for (int i = 0; i < ctx.getChildCount(); i = i + 2) {
			if (ctx.getChild(i).getText().equals("s1")) {
				selectionArray[selectionArrayPosition] = oldTableNames[0].substring(0, 2) + "1.subject";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("p1")) {
				selectionArray[selectionArrayPosition] = oldTableNames[0].substring(0, 2) + "1.predicate";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("o1")) {
				selectionArray[selectionArrayPosition] = oldTableNames[0].substring(0, 2) + "1.object";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("s2")) {
				selectionArray[selectionArrayPosition] = oldTableNames[0].substring(0, 2) + "2.subject";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("p2")) {
				selectionArray[selectionArrayPosition] = oldTableNames[0].substring(0, 2) + "2.predicate";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("o2")) {
				selectionArray[selectionArrayPosition] = oldTableNames[0].substring(0, 2) + "2.object";
				selectionArrayPosition++;
			} else {
				projectedColumn.add(selectionArrayPosition + "#" + "\'" + ctx.getChild(i).getText() + "\'");

				selectionArray[selectionArrayPosition] = null;
				selectionArrayPosition++;
			}
		}

	}

	@Override
	public void exitSelection(SelectionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterRecursionType(RecursionTypeContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitRecursionType(RecursionTypeContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterKleeneDepth(KleeneDepthContext ctx) {
		try {
			if (ctx.getChild(1).getText().equals("+")) {
				kleeneDepth1 = -10;
			} else {
				kleeneDepth1 = Integer.parseInt(ctx.getChild(1).getText());
			}
		} catch (Exception e) {
			kleeneDepth1 = 0;
		}

		if (ctx.getChildCount() > 3) {
			try {
				kleeneDepth2 = Integer.parseInt(ctx.getChild(3).getText());
			} catch (Exception e) {
				kleeneDepth2 = 0;
			}
		}
	}

	@Override
	public void exitKleeneDepth(KleeneDepthContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterFilterExpr(FilterExprContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exitFilterExpr(FilterExprContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterFilterExprAnd(FilterExprAndContext ctx) {
		filterExpression = true;

	}

	@Override
	public void exitFilterExprAnd(FilterExprAndContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterSelectionRecursion(SelectionRecursionContext ctx) {

		int selectionArrayPosition = 0;
		for (int i = 0; i < ctx.getChildCount(); i = i + 2) {
			if (ctx.getChild(i).getText().equals("s1")) {
				selectionPart[selectionArrayPosition] = "1";
				selectionArrayPosition++;
				selectionPart[selectionArrayPosition] = "subject";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("p1")) {
				selectionPart[selectionArrayPosition] = "1";
				selectionArrayPosition++;
				selectionPart[selectionArrayPosition] = "predicate";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("o1")) {
				selectionPart[selectionArrayPosition] = "1";
				selectionArrayPosition++;
				selectionPart[selectionArrayPosition] = "object";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("s2")) {
				selectionPart[selectionArrayPosition] = "2";
				selectionArrayPosition++;
				selectionPart[selectionArrayPosition] = "subject";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("p2")) {
				selectionPart[selectionArrayPosition] = "2";
				selectionArrayPosition++;
				selectionPart[selectionArrayPosition] = "predicate";
				selectionArrayPosition++;
			} else if (ctx.getChild(i).getText().equals("o2")) {
				selectionPart[selectionArrayPosition] = "2";
				selectionArrayPosition++;
				selectionPart[selectionArrayPosition] = "object";
				selectionArrayPosition++;
			}
		}
	}

	@Override
	public void exitSelectionRecursion(SelectionRecursionContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterLoadBlock(LoadBlockContext ctx) {

		int childrenNumber = ctx.getChildCount();
		if (childrenNumber == 2) {
			oldTableNames[0] = ctx.getChild(1).getText();
			isCached = false;
		} else if (childrenNumber == 3) {
			oldTableNames[0] = ctx.getChild(2).getText();
			isCached = true;
		}

	}

	@Override
	public void exitLoadBlock(LoadBlockContext ctx) {
		LoadOperation.CreateQuery(oldTableNames);
	}

	@Override
	public void enterSelectionProvenanceCreator(SelectionProvenanceCreatorContext ctx) {
		provenance = ", CONCAT(";
		for (int i = 0; i < ctx.getChildCount(); i = i + 2) {
			if (ctx.getChild(i).getText().equals("s1")) {
				provenance = provenance + "subject, '/',";
			} else if (ctx.getChild(i).getText().equals("p1")) {
				provenance = provenance + "predicate, '/',";
			} else if (ctx.getChild(i).getText().equals("o1")) {
				provenance = provenance + "object, '/',";
			}
		}

		provenance = provenance.substring(0, provenance.length() - 6) + ") AS provenance ";
	}

	@Override
	public void exitSelectionProvenanceCreator(SelectionProvenanceCreatorContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterSelectionProvenanceAppender(SelectionProvenanceAppenderContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i = i + 2) {
			if (ctx.getChild(i).getText().equals("r1")) {
				provenanceAppenderList.add(1);
				setProvenance = true;
			} else if (ctx.getChild(i).getText().equals("r2")) {
				provenanceAppenderList.add(2);
				setProvenance = true;
			}
		}

	}

	@Override
	public void exitSelectionProvenanceAppender(SelectionProvenanceAppenderContext ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enterMergeBlock(MergeBlockContext ctx) {
		for (int i = 0; i < ctx.getChildCount(); i = i + 2) {
			mergeTables.add(ctx.getChild(i).getText());
		}
	}

	@Override
	public void exitMergeBlock(MergeBlockContext ctx) {
		MergeOperation.CreateQuery(mergeTables, oldTableNames, newTableName);
	}

}