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

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TriALQLParser}.
 */
public interface TriALQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TriALQLParser#parse}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterParse(TriALQLParser.ParseContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#parse}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitParse(TriALQLParser.ParseContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#block}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterBlock(TriALQLParser.BlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#block}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitBlock(TriALQLParser.BlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#selectBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterSelectBlock(TriALQLParser.SelectBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#selectBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitSelectBlock(TriALQLParser.SelectBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#selectRecursionBlock}
	 * .
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterSelectRecursionBlock(TriALQLParser.SelectRecursionBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#selectRecursionBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 * @throws SQLException
	 */
	void exitSelectRecursionBlock(TriALQLParser.SelectRecursionBlockContext ctx) throws SQLException;

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#selectJoinBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterSelectJoinBlock(TriALQLParser.SelectJoinBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#selectJoinBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitSelectJoinBlock(TriALQLParser.SelectJoinBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#operatorBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterOperatorBlock(TriALQLParser.OperatorBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#operatorBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitOperatorBlock(TriALQLParser.OperatorBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#mergeBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterMergeBlock(TriALQLParser.MergeBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#mergeBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitMergeBlock(TriALQLParser.MergeBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#storeBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterStoreBlock(TriALQLParser.StoreBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#storeBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitStoreBlock(TriALQLParser.StoreBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#loadBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterLoadBlock(TriALQLParser.LoadBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#loadBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitLoadBlock(TriALQLParser.LoadBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#dropBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterDropBlock(TriALQLParser.DropBlockContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#dropBlock}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitDropBlock(TriALQLParser.DropBlockContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#equationList}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterEquationList(TriALQLParser.EquationListContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#equationList}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitEquationList(TriALQLParser.EquationListContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#equation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterEquation(TriALQLParser.EquationContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#equation}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitEquation(TriALQLParser.EquationContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#equationOperator}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterEquationOperator(TriALQLParser.EquationOperatorContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#equationOperator}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitEquationOperator(TriALQLParser.EquationOperatorContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#term}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterTerm(TriALQLParser.TermContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#term}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitTerm(TriALQLParser.TermContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#literal}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterLiteral(TriALQLParser.LiteralContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#literal}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitLiteral(TriALQLParser.LiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#selection}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterSelection(TriALQLParser.SelectionContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#selection}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitSelection(TriALQLParser.SelectionContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#selectionRecursion}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterSelectionRecursion(TriALQLParser.SelectionRecursionContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#selectionRecursion}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitSelectionRecursion(TriALQLParser.SelectionRecursionContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link TriALQLParser#selectionProvenanceCreator}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterSelectionProvenanceCreator(TriALQLParser.SelectionProvenanceCreatorContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link TriALQLParser#selectionProvenanceCreator}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitSelectionProvenanceCreator(TriALQLParser.SelectionProvenanceCreatorContext ctx);

	/**
	 * Enter a parse tree produced by
	 * {@link TriALQLParser#selectionProvenanceAppender}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterSelectionProvenanceAppender(TriALQLParser.SelectionProvenanceAppenderContext ctx);

	/**
	 * Exit a parse tree produced by
	 * {@link TriALQLParser#selectionProvenanceAppender}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitSelectionProvenanceAppender(TriALQLParser.SelectionProvenanceAppenderContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#recursionType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterRecursionType(TriALQLParser.RecursionTypeContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#recursionType}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitRecursionType(TriALQLParser.RecursionTypeContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#kleeneDepth}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterKleeneDepth(TriALQLParser.KleeneDepthContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#kleeneDepth}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitKleeneDepth(TriALQLParser.KleeneDepthContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#filterExpr}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterFilterExpr(TriALQLParser.FilterExprContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#filterExpr}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitFilterExpr(TriALQLParser.FilterExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link TriALQLParser#filterExprAnd}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void enterFilterExprAnd(TriALQLParser.FilterExprAndContext ctx);

	/**
	 * Exit a parse tree produced by {@link TriALQLParser#filterExprAnd}.
	 * 
	 * @param ctx
	 *            the parse tree
	 */
	void exitFilterExprAnd(TriALQLParser.FilterExprAndContext ctx);
}