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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Parser: Auto generated from ANTLR and the E-TriAL-QL grammar.
 */
@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class TriALQLParser extends Parser {
	static {
		RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION);
	}

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache = new PredictionContextCache();
	public static final int T__0 = 1, T__1 = 2, Select = 3, From = 4, Join = 5, On = 6, Store = 7, Provenance = 8,
			As = 9, Using = 10, Left = 11, Right = 12, And = 13, Or = 14, Drop = 15, Union = 16, Minus = 17,
			Intersect = 18, Filter = 19, With = 20, Load = 21, Cache = 22, Merge = 23, Star = 24, Plus = 25, Pos = 26,
			Equals = 27, NEquals = 28, GTEquals = 29, LTEquals = 30, GT = 31, LT = 32, Apostrophe = 33, Quote = 34,
			Semikolon = 35, OBracket = 36, CBracket = 37, Comma = 38, Bool = 39, Identifier = 40, String = 41,
			PositiveInt = 42, Int = 43, Comment = 44, Whitespace = 45;
	public static final int RULE_parse = 0, RULE_block = 1, RULE_selectBlock = 2, RULE_selectRecursionBlock = 3,
			RULE_selectJoinBlock = 4, RULE_operatorBlock = 5, RULE_mergeBlock = 6, RULE_storeBlock = 7,
			RULE_loadBlock = 8, RULE_dropBlock = 9, RULE_equationList = 10, RULE_equation = 11,
			RULE_equationOperator = 12, RULE_term = 13, RULE_literal = 14, RULE_selection = 15,
			RULE_selectionRecursion = 16, RULE_selectionProvenanceCreator = 17, RULE_selectionProvenanceAppender = 18,
			RULE_recursionType = 19, RULE_kleeneDepth = 20, RULE_filterExpr = 21, RULE_filterExprAnd = 22;
	public static final String[] ruleNames = { "parse", "block", "selectBlock", "selectRecursionBlock",
			"selectJoinBlock", "operatorBlock", "mergeBlock", "storeBlock", "loadBlock", "dropBlock", "equationList",
			"equation", "equationOperator", "term", "literal", "selection", "selectionRecursion",
			"selectionProvenanceCreator", "selectionProvenanceAppender", "recursionType", "kleeneDepth", "filterExpr",
			"filterExprAnd" };

	private static final String[] _LITERAL_NAMES = { null, "'r1'", "'r2'", "'SELECT'", "'FROM'", "'JOIN'", "'ON'",
			"'STORE'", "'PROVENANCE'", "'AS'", "'USING'", "'left'", "'right'", "'AND'", "'OR'", "'DROP'", "'UNION'",
			"'MINUS'", "'INTERSECT'", "'FILTER'", "'WITH'", "'LOAD'", "'CACHE'", "'MERGE'", "'*'", "'+'", null, "'='",
			"'!='", "'>='", "'<='", "'>'", "'<'", "'''", "'\"'", "';'", "'['", "']'", "','" };
	private static final String[] _SYMBOLIC_NAMES = { null, null, null, "Select", "From", "Join", "On", "Store",
			"Provenance", "As", "Using", "Left", "Right", "And", "Or", "Drop", "Union", "Minus", "Intersect", "Filter",
			"With", "Load", "Cache", "Merge", "Star", "Plus", "Pos", "Equals", "NEquals", "GTEquals", "LTEquals", "GT",
			"LT", "Apostrophe", "Quote", "Semikolon", "OBracket", "CBracket", "Comma", "Bool", "Identifier", "String",
			"PositiveInt", "Int", "Comment", "Whitespace" };
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() {
		return "TriALQL.g4";
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return _serializedATN;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public TriALQLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	public static class ParseContext extends ParserRuleContext {
		public TerminalNode EOF() {
			return getToken(TriALQLParser.EOF, 0);
		}

		public List<BlockContext> block() {
			return getRuleContexts(BlockContext.class);
		}

		public BlockContext block(int i) {
			return getRuleContext(BlockContext.class, i);
		}

		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_parse;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterParse(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitParse(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(47);
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
						{
							setState(46);
							block();
						}
					}
					setState(49);
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ((((_la) & ~0x3f) == 0
						&& ((1L << _la) & ((1L << Store) | (1L << Drop) | (1L << Load) | (1L << Identifier))) != 0));
				setState(51);
				match(EOF);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public TerminalNode Semikolon() {
			return getToken(TriALQLParser.Semikolon, 0);
		}

		public SelectBlockContext selectBlock() {
			return getRuleContext(SelectBlockContext.class, 0);
		}

		public SelectRecursionBlockContext selectRecursionBlock() {
			return getRuleContext(SelectRecursionBlockContext.class, 0);
		}

		public SelectJoinBlockContext selectJoinBlock() {
			return getRuleContext(SelectJoinBlockContext.class, 0);
		}

		public OperatorBlockContext operatorBlock() {
			return getRuleContext(OperatorBlockContext.class, 0);
		}

		public StoreBlockContext storeBlock() {
			return getRuleContext(StoreBlockContext.class, 0);
		}

		public LoadBlockContext loadBlock() {
			return getRuleContext(LoadBlockContext.class, 0);
		}

		public DropBlockContext dropBlock() {
			return getRuleContext(DropBlockContext.class, 0);
		}

		public MergeBlockContext mergeBlock() {
			return getRuleContext(MergeBlockContext.class, 0);
		}

		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_block;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitBlock(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(61);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 1, _ctx)) {
				case 1: {
					setState(53);
					selectBlock();
				}
					break;
				case 2: {
					setState(54);
					selectRecursionBlock();
				}
					break;
				case 3: {
					setState(55);
					selectJoinBlock();
				}
					break;
				case 4: {
					setState(56);
					operatorBlock();
				}
					break;
				case 5: {
					setState(57);
					storeBlock();
				}
					break;
				case 6: {
					setState(58);
					loadBlock();
				}
					break;
				case 7: {
					setState(59);
					dropBlock();
				}
					break;
				case 8: {
					setState(60);
					mergeBlock();
				}
					break;
				}
				setState(63);
				match(Semikolon);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectBlockContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() {
			return getTokens(TriALQLParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return getToken(TriALQLParser.Identifier, i);
		}

		public TerminalNode Equals() {
			return getToken(TriALQLParser.Equals, 0);
		}

		public TerminalNode Select() {
			return getToken(TriALQLParser.Select, 0);
		}

		public SelectionContext selection() {
			return getRuleContext(SelectionContext.class, 0);
		}

		public TerminalNode From() {
			return getToken(TriALQLParser.From, 0);
		}

		public TerminalNode With() {
			return getToken(TriALQLParser.With, 0);
		}

		public SelectionProvenanceCreatorContext selectionProvenanceCreator() {
			return getRuleContext(SelectionProvenanceCreatorContext.class, 0);
		}

		public TerminalNode Filter() {
			return getToken(TriALQLParser.Filter, 0);
		}

		public FilterExprContext filterExpr() {
			return getRuleContext(FilterExprContext.class, 0);
		}

		public SelectBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_selectBlock;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterSelectBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitSelectBlock(this);
		}
	}

	public final SelectBlockContext selectBlock() throws RecognitionException {
		SelectBlockContext _localctx = new SelectBlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_selectBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(65);
				match(Identifier);
				setState(66);
				match(Equals);
				setState(67);
				match(Select);
				setState(68);
				selection();
				setState(71);
				_la = _input.LA(1);
				if (_la == With) {
					{
						setState(69);
						match(With);
						setState(70);
						selectionProvenanceCreator();
					}
				}

				setState(73);
				match(From);
				setState(74);
				match(Identifier);
				setState(77);
				_la = _input.LA(1);
				if (_la == Filter) {
					{
						setState(75);
						match(Filter);
						setState(76);
						filterExpr();
					}
				}

			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectRecursionBlockContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() {
			return getTokens(TriALQLParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return getToken(TriALQLParser.Identifier, i);
		}

		public TerminalNode Equals() {
			return getToken(TriALQLParser.Equals, 0);
		}

		public TerminalNode Select() {
			return getToken(TriALQLParser.Select, 0);
		}

		public SelectionRecursionContext selectionRecursion() {
			return getRuleContext(SelectionRecursionContext.class, 0);
		}

		public TerminalNode From() {
			return getToken(TriALQLParser.From, 0);
		}

		public TerminalNode On() {
			return getToken(TriALQLParser.On, 0);
		}

		public EquationListContext equationList() {
			return getRuleContext(EquationListContext.class, 0);
		}

		public TerminalNode Using() {
			return getToken(TriALQLParser.Using, 0);
		}

		public RecursionTypeContext recursionType() {
			return getRuleContext(RecursionTypeContext.class, 0);
		}

		public TerminalNode With() {
			return getToken(TriALQLParser.With, 0);
		}

		public SelectionProvenanceAppenderContext selectionProvenanceAppender() {
			return getRuleContext(SelectionProvenanceAppenderContext.class, 0);
		}

		public TerminalNode Filter() {
			return getToken(TriALQLParser.Filter, 0);
		}

		public FilterExprContext filterExpr() {
			return getRuleContext(FilterExprContext.class, 0);
		}

		public KleeneDepthContext kleeneDepth() {
			return getRuleContext(KleeneDepthContext.class, 0);
		}

		public SelectRecursionBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_selectRecursionBlock;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterSelectRecursionBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				try {
					((TriALQLListener) listener).exitSelectRecursionBlock(this);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public final SelectRecursionBlockContext selectRecursionBlock() throws RecognitionException {
		SelectRecursionBlockContext _localctx = new SelectRecursionBlockContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_selectRecursionBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(79);
				match(Identifier);
				setState(80);
				match(Equals);
				setState(81);
				match(Select);
				setState(82);
				selectionRecursion();
				setState(85);
				_la = _input.LA(1);
				if (_la == With) {
					{
						setState(83);
						match(With);
						setState(84);
						selectionProvenanceAppender();
					}
				}

				setState(87);
				match(From);
				setState(88);
				match(Identifier);
				setState(89);
				match(On);
				setState(90);
				equationList();
				setState(93);
				_la = _input.LA(1);
				if (_la == Filter) {
					{
						setState(91);
						match(Filter);
						setState(92);
						filterExpr();
					}
				}

				setState(95);
				match(Using);
				setState(96);
				recursionType();
				setState(98);
				_la = _input.LA(1);
				if (_la == OBracket) {
					{
						setState(97);
						kleeneDepth();
					}
				}

			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectJoinBlockContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() {
			return getTokens(TriALQLParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return getToken(TriALQLParser.Identifier, i);
		}

		public TerminalNode Equals() {
			return getToken(TriALQLParser.Equals, 0);
		}

		public TerminalNode Select() {
			return getToken(TriALQLParser.Select, 0);
		}

		public SelectionContext selection() {
			return getRuleContext(SelectionContext.class, 0);
		}

		public TerminalNode From() {
			return getToken(TriALQLParser.From, 0);
		}

		public TerminalNode Join() {
			return getToken(TriALQLParser.Join, 0);
		}

		public TerminalNode With() {
			return getToken(TriALQLParser.With, 0);
		}

		public SelectionProvenanceAppenderContext selectionProvenanceAppender() {
			return getRuleContext(SelectionProvenanceAppenderContext.class, 0);
		}

		public TerminalNode On() {
			return getToken(TriALQLParser.On, 0);
		}

		public EquationListContext equationList() {
			return getRuleContext(EquationListContext.class, 0);
		}

		public TerminalNode Filter() {
			return getToken(TriALQLParser.Filter, 0);
		}

		public FilterExprContext filterExpr() {
			return getRuleContext(FilterExprContext.class, 0);
		}

		public SelectJoinBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_selectJoinBlock;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterSelectJoinBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitSelectJoinBlock(this);
		}
	}

	public final SelectJoinBlockContext selectJoinBlock() throws RecognitionException {
		SelectJoinBlockContext _localctx = new SelectJoinBlockContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_selectJoinBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(100);
				match(Identifier);
				setState(101);
				match(Equals);
				setState(102);
				match(Select);
				setState(103);
				selection();
				setState(106);
				_la = _input.LA(1);
				if (_la == With) {
					{
						setState(104);
						match(With);
						setState(105);
						selectionProvenanceAppender();
					}
				}

				setState(108);
				match(From);
				setState(109);
				match(Identifier);
				setState(110);
				match(Join);
				setState(111);
				match(Identifier);
				setState(114);
				_la = _input.LA(1);
				if (_la == On) {
					{
						setState(112);
						match(On);
						setState(113);
						equationList();
					}
				}

				setState(118);
				_la = _input.LA(1);
				if (_la == Filter) {
					{
						setState(116);
						match(Filter);
						setState(117);
						filterExpr();
					}
				}

			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OperatorBlockContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() {
			return getTokens(TriALQLParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return getToken(TriALQLParser.Identifier, i);
		}

		public TerminalNode Equals() {
			return getToken(TriALQLParser.Equals, 0);
		}

		public TerminalNode Union() {
			return getToken(TriALQLParser.Union, 0);
		}

		public TerminalNode Minus() {
			return getToken(TriALQLParser.Minus, 0);
		}

		public TerminalNode Intersect() {
			return getToken(TriALQLParser.Intersect, 0);
		}

		public TerminalNode With() {
			return getToken(TriALQLParser.With, 0);
		}

		public TerminalNode Provenance() {
			return getToken(TriALQLParser.Provenance, 0);
		}

		public OperatorBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_operatorBlock;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterOperatorBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitOperatorBlock(this);
		}
	}

	public final OperatorBlockContext operatorBlock() throws RecognitionException {
		OperatorBlockContext _localctx = new OperatorBlockContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_operatorBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(120);
				match(Identifier);
				setState(121);
				match(Equals);
				setState(122);
				match(Identifier);
				setState(123);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0
						&& ((1L << _la) & ((1L << Union) | (1L << Minus) | (1L << Intersect))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(124);
				match(Identifier);
				setState(127);
				_la = _input.LA(1);
				if (_la == With) {
					{
						setState(125);
						match(With);
						setState(126);
						match(Provenance);
					}
				}

			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MergeBlockContext extends ParserRuleContext {
		public List<TerminalNode> Identifier() {
			return getTokens(TriALQLParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return getToken(TriALQLParser.Identifier, i);
		}

		public List<TerminalNode> Merge() {
			return getTokens(TriALQLParser.Merge);
		}

		public TerminalNode Merge(int i) {
			return getToken(TriALQLParser.Merge, i);
		}

		public MergeBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_mergeBlock;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterMergeBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitMergeBlock(this);
		}
	}

	public final MergeBlockContext mergeBlock() throws RecognitionException {
		MergeBlockContext _localctx = new MergeBlockContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_mergeBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(129);
				match(Identifier);
				setState(130);
				match(Merge);
				setState(131);
				match(Identifier);
				setState(136);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == Merge) {
					{
						{
							setState(132);
							match(Merge);
							setState(133);
							match(Identifier);
						}
					}
					setState(138);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StoreBlockContext extends ParserRuleContext {
		public TerminalNode Store() {
			return getToken(TriALQLParser.Store, 0);
		}

		public List<TerminalNode> Identifier() {
			return getTokens(TriALQLParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return getToken(TriALQLParser.Identifier, i);
		}

		public TerminalNode As() {
			return getToken(TriALQLParser.As, 0);
		}

		public TerminalNode Provenance() {
			return getToken(TriALQLParser.Provenance, 0);
		}

		public StoreBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_storeBlock;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterStoreBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitStoreBlock(this);
		}
	}

	public final StoreBlockContext storeBlock() throws RecognitionException {
		StoreBlockContext _localctx = new StoreBlockContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_storeBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(139);
				match(Store);
				setState(141);
				_la = _input.LA(1);
				if (_la == Provenance) {
					{
						setState(140);
						match(Provenance);
					}
				}

				setState(143);
				match(Identifier);
				setState(144);
				match(As);
				setState(145);
				match(Identifier);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LoadBlockContext extends ParserRuleContext {
		public TerminalNode Load() {
			return getToken(TriALQLParser.Load, 0);
		}

		public TerminalNode Identifier() {
			return getToken(TriALQLParser.Identifier, 0);
		}

		public TerminalNode Cache() {
			return getToken(TriALQLParser.Cache, 0);
		}

		public LoadBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_loadBlock;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterLoadBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitLoadBlock(this);
		}
	}

	public final LoadBlockContext loadBlock() throws RecognitionException {
		LoadBlockContext _localctx = new LoadBlockContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_loadBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(147);
				match(Load);
				setState(149);
				_la = _input.LA(1);
				if (_la == Cache) {
					{
						setState(148);
						match(Cache);
					}
				}

				setState(151);
				match(Identifier);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DropBlockContext extends ParserRuleContext {
		public TerminalNode Drop() {
			return getToken(TriALQLParser.Drop, 0);
		}

		public TerminalNode Identifier() {
			return getToken(TriALQLParser.Identifier, 0);
		}

		public TerminalNode Provenance() {
			return getToken(TriALQLParser.Provenance, 0);
		}

		public DropBlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_dropBlock;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterDropBlock(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitDropBlock(this);
		}
	}

	public final DropBlockContext dropBlock() throws RecognitionException {
		DropBlockContext _localctx = new DropBlockContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_dropBlock);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(153);
				match(Drop);
				setState(155);
				_la = _input.LA(1);
				if (_la == Provenance) {
					{
						setState(154);
						match(Provenance);
					}
				}

				setState(157);
				match(Identifier);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EquationListContext extends ParserRuleContext {
		public List<EquationContext> equation() {
			return getRuleContexts(EquationContext.class);
		}

		public EquationContext equation(int i) {
			return getRuleContext(EquationContext.class, i);
		}

		public List<TerminalNode> Comma() {
			return getTokens(TriALQLParser.Comma);
		}

		public TerminalNode Comma(int i) {
			return getToken(TriALQLParser.Comma, i);
		}

		public EquationListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_equationList;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterEquationList(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitEquationList(this);
		}
	}

	public final EquationListContext equationList() throws RecognitionException {
		EquationListContext _localctx = new EquationListContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_equationList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(159);
				equation();
				setState(164);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == Comma) {
					{
						{
							setState(160);
							match(Comma);
							setState(161);
							equation();
						}
					}
					setState(166);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EquationContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}

		public TermContext term(int i) {
			return getRuleContext(TermContext.class, i);
		}

		public EquationOperatorContext equationOperator() {
			return getRuleContext(EquationOperatorContext.class, 0);
		}

		public EquationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_equation;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterEquation(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitEquation(this);
		}
	}

	public final EquationContext equation() throws RecognitionException {
		EquationContext _localctx = new EquationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_equation);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(167);
				term();
				setState(168);
				equationOperator();
				setState(169);
				term();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EquationOperatorContext extends ParserRuleContext {
		public TerminalNode Equals() {
			return getToken(TriALQLParser.Equals, 0);
		}

		public TerminalNode NEquals() {
			return getToken(TriALQLParser.NEquals, 0);
		}

		public TerminalNode GTEquals() {
			return getToken(TriALQLParser.GTEquals, 0);
		}

		public TerminalNode LTEquals() {
			return getToken(TriALQLParser.LTEquals, 0);
		}

		public TerminalNode GT() {
			return getToken(TriALQLParser.GT, 0);
		}

		public TerminalNode LT() {
			return getToken(TriALQLParser.LT, 0);
		}

		public EquationOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_equationOperator;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterEquationOperator(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitEquationOperator(this);
		}
	}

	public final EquationOperatorContext equationOperator() throws RecognitionException {
		EquationOperatorContext _localctx = new EquationOperatorContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_equationOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(171);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Equals) | (1L << NEquals) | (1L << GTEquals)
						| (1L << LTEquals) | (1L << GT) | (1L << LT))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public TerminalNode Pos() {
			return getToken(TriALQLParser.Pos, 0);
		}

		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class, 0);
		}

		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_term;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterTerm(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitTerm(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_term);
		try {
			setState(175);
			switch (_input.LA(1)) {
			case Pos:
				enterOuterAlt(_localctx, 1); {
				setState(173);
				match(Pos);
			}
				break;
			case Bool:
			case String:
			case Int:
				enterOuterAlt(_localctx, 2); {
				setState(174);
				literal();
			}
				break;
			default:
				throw new NoViableAltException(this);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode Bool() {
			return getToken(TriALQLParser.Bool, 0);
		}

		public TerminalNode Int() {
			return getToken(TriALQLParser.Int, 0);
		}

		public TerminalNode String() {
			return getToken(TriALQLParser.String, 0);
		}

		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_literal;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterLiteral(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(177);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Bool) | (1L << String) | (1L << Int))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionContext extends ParserRuleContext {
		public List<TerminalNode> Comma() {
			return getTokens(TriALQLParser.Comma);
		}

		public TerminalNode Comma(int i) {
			return getToken(TriALQLParser.Comma, i);
		}

		public List<TerminalNode> Pos() {
			return getTokens(TriALQLParser.Pos);
		}

		public TerminalNode Pos(int i) {
			return getToken(TriALQLParser.Pos, i);
		}

		public List<TerminalNode> Identifier() {
			return getTokens(TriALQLParser.Identifier);
		}

		public TerminalNode Identifier(int i) {
			return getToken(TriALQLParser.Identifier, i);
		}

		public SelectionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_selection;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterSelection(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitSelection(this);
		}
	}

	public final SelectionContext selection() throws RecognitionException {
		SelectionContext _localctx = new SelectionContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_selection);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(179);
				_la = _input.LA(1);
				if (!(_la == Pos || _la == Identifier)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(180);
				match(Comma);
				setState(181);
				_la = _input.LA(1);
				if (!(_la == Pos || _la == Identifier)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(182);
				match(Comma);
				setState(183);
				_la = _input.LA(1);
				if (!(_la == Pos || _la == Identifier)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionRecursionContext extends ParserRuleContext {
		public List<TerminalNode> Pos() {
			return getTokens(TriALQLParser.Pos);
		}

		public TerminalNode Pos(int i) {
			return getToken(TriALQLParser.Pos, i);
		}

		public List<TerminalNode> Comma() {
			return getTokens(TriALQLParser.Comma);
		}

		public TerminalNode Comma(int i) {
			return getToken(TriALQLParser.Comma, i);
		}

		public SelectionRecursionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_selectionRecursion;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterSelectionRecursion(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitSelectionRecursion(this);
		}
	}

	public final SelectionRecursionContext selectionRecursion() throws RecognitionException {
		SelectionRecursionContext _localctx = new SelectionRecursionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_selectionRecursion);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(185);
				match(Pos);
				setState(186);
				match(Comma);
				setState(187);
				match(Pos);
				setState(188);
				match(Comma);
				setState(189);
				match(Pos);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionProvenanceCreatorContext extends ParserRuleContext {
		public List<TerminalNode> Pos() {
			return getTokens(TriALQLParser.Pos);
		}

		public TerminalNode Pos(int i) {
			return getToken(TriALQLParser.Pos, i);
		}

		public List<TerminalNode> Comma() {
			return getTokens(TriALQLParser.Comma);
		}

		public TerminalNode Comma(int i) {
			return getToken(TriALQLParser.Comma, i);
		}

		public SelectionProvenanceCreatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_selectionProvenanceCreator;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterSelectionProvenanceCreator(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitSelectionProvenanceCreator(this);
		}
	}

	public final SelectionProvenanceCreatorContext selectionProvenanceCreator() throws RecognitionException {
		SelectionProvenanceCreatorContext _localctx = new SelectionProvenanceCreatorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_selectionProvenanceCreator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(191);
				match(Pos);
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == Comma) {
					{
						{
							setState(192);
							match(Comma);
							setState(193);
							match(Pos);
						}
					}
					setState(198);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SelectionProvenanceAppenderContext extends ParserRuleContext {
		public TerminalNode Comma() {
			return getToken(TriALQLParser.Comma, 0);
		}

		public SelectionProvenanceAppenderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_selectionProvenanceAppender;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterSelectionProvenanceAppender(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitSelectionProvenanceAppender(this);
		}
	}

	public final SelectionProvenanceAppenderContext selectionProvenanceAppender() throws RecognitionException {
		SelectionProvenanceAppenderContext _localctx = new SelectionProvenanceAppenderContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_selectionProvenanceAppender);
		try {
			setState(205);
			switch (_input.LA(1)) {
			case T__0:
				enterOuterAlt(_localctx, 1); {
				{
					setState(199);
					match(T__0);
					setState(200);
					match(Comma);
					setState(201);
					match(T__1);
				}
			}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2); {
				{
					setState(202);
					match(T__1);
					setState(203);
					match(Comma);
					setState(204);
					match(T__0);
				}
			}
				break;
			default:
				throw new NoViableAltException(this);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RecursionTypeContext extends ParserRuleContext {
		public TerminalNode Left() {
			return getToken(TriALQLParser.Left, 0);
		}

		public TerminalNode Right() {
			return getToken(TriALQLParser.Right, 0);
		}

		public RecursionTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_recursionType;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterRecursionType(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitRecursionType(this);
		}
	}

	public final RecursionTypeContext recursionType() throws RecognitionException {
		RecursionTypeContext _localctx = new RecursionTypeContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_recursionType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(207);
				_la = _input.LA(1);
				if (!(_la == Left || _la == Right)) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KleeneDepthContext extends ParserRuleContext {
		public TerminalNode OBracket() {
			return getToken(TriALQLParser.OBracket, 0);
		}

		public TerminalNode CBracket() {
			return getToken(TriALQLParser.CBracket, 0);
		}

		public List<TerminalNode> PositiveInt() {
			return getTokens(TriALQLParser.PositiveInt);
		}

		public TerminalNode PositiveInt(int i) {
			return getToken(TriALQLParser.PositiveInt, i);
		}

		public List<TerminalNode> Star() {
			return getTokens(TriALQLParser.Star);
		}

		public TerminalNode Star(int i) {
			return getToken(TriALQLParser.Star, i);
		}

		public TerminalNode Plus() {
			return getToken(TriALQLParser.Plus, 0);
		}

		public TerminalNode Comma() {
			return getToken(TriALQLParser.Comma, 0);
		}

		public KleeneDepthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_kleeneDepth;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterKleeneDepth(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitKleeneDepth(this);
		}
	}

	public final KleeneDepthContext kleeneDepth() throws RecognitionException {
		KleeneDepthContext _localctx = new KleeneDepthContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_kleeneDepth);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(209);
				match(OBracket);
				setState(210);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0
						&& ((1L << _la) & ((1L << Star) | (1L << Plus) | (1L << PositiveInt))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					consume();
				}
				setState(213);
				_la = _input.LA(1);
				if (_la == Comma) {
					{
						setState(211);
						match(Comma);
						setState(212);
						_la = _input.LA(1);
						if (!(_la == Star || _la == PositiveInt)) {
							_errHandler.recoverInline(this);
						} else {
							consume();
						}
					}
				}

				setState(215);
				match(CBracket);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterExprContext extends ParserRuleContext {
		public List<FilterExprAndContext> filterExprAnd() {
			return getRuleContexts(FilterExprAndContext.class);
		}

		public FilterExprAndContext filterExprAnd(int i) {
			return getRuleContext(FilterExprAndContext.class, i);
		}

		public List<TerminalNode> Or() {
			return getTokens(TriALQLParser.Or);
		}

		public TerminalNode Or(int i) {
			return getToken(TriALQLParser.Or, i);
		}

		public FilterExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_filterExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterFilterExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitFilterExpr(this);
		}
	}

	public final FilterExprContext filterExpr() throws RecognitionException {
		FilterExprContext _localctx = new FilterExprContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_filterExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(217);
				filterExprAnd();
				setState(222);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == Or) {
					{
						{
							setState(218);
							match(Or);
							setState(219);
							filterExprAnd();
						}
					}
					setState(224);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilterExprAndContext extends ParserRuleContext {
		public List<EquationContext> equation() {
			return getRuleContexts(EquationContext.class);
		}

		public EquationContext equation(int i) {
			return getRuleContext(EquationContext.class, i);
		}

		public List<TerminalNode> And() {
			return getTokens(TriALQLParser.And);
		}

		public TerminalNode And(int i) {
			return getToken(TriALQLParser.And, i);
		}

		public FilterExprAndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_filterExprAnd;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).enterFilterExprAnd(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof TriALQLListener)
				((TriALQLListener) listener).exitFilterExprAnd(this);
		}
	}

	public final FilterExprAndContext filterExprAnd() throws RecognitionException {
		FilterExprAndContext _localctx = new FilterExprAndContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_filterExprAnd);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(225);
				equation();
				setState(230);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == And) {
					{
						{
							setState(226);
							match(And);
							setState(227);
							equation();
						}
					}
					setState(232);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN = "\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3/\u00ec\4\2\t\2\4"
			+ "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"
			+ "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
			+ "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2\6\2\62"
			+ "\n\2\r\2\16\2\63\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3@\n\3\3\3"
			+ "\3\3\3\4\3\4\3\4\3\4\3\4\3\4\5\4J\n\4\3\4\3\4\3\4\3\4\5\4P\n\4\3\5\3\5"
			+ "\3\5\3\5\3\5\3\5\5\5X\n\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5`\n\5\3\5\3\5\3\5"
			+ "\5\5e\n\5\3\6\3\6\3\6\3\6\3\6\3\6\5\6m\n\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6"
			+ "u\n\6\3\6\3\6\5\6y\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u0082\n\7\3\b\3"
			+ "\b\3\b\3\b\3\b\7\b\u0089\n\b\f\b\16\b\u008c\13\b\3\t\3\t\5\t\u0090\n\t"
			+ "\3\t\3\t\3\t\3\t\3\n\3\n\5\n\u0098\n\n\3\n\3\n\3\13\3\13\5\13\u009e\n"
			+ "\13\3\13\3\13\3\f\3\f\3\f\7\f\u00a5\n\f\f\f\16\f\u00a8\13\f\3\r\3\r\3"
			+ "\r\3\r\3\16\3\16\3\17\3\17\5\17\u00b2\n\17\3\20\3\20\3\21\3\21\3\21\3"
			+ "\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\7\23\u00c5"
			+ "\n\23\f\23\16\23\u00c8\13\23\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00d0"
			+ "\n\24\3\25\3\25\3\26\3\26\3\26\3\26\5\26\u00d8\n\26\3\26\3\26\3\27\3\27"
			+ "\3\27\7\27\u00df\n\27\f\27\16\27\u00e2\13\27\3\30\3\30\3\30\7\30\u00e7"
			+ "\n\30\f\30\16\30\u00ea\13\30\3\30\2\2\31\2\4\6\b\n\f\16\20\22\24\26\30"
			+ "\32\34\36 \"$&(*,.\2\t\3\2\22\24\3\2\35\"\5\2))++--\4\2\34\34**\3\2\r"
			+ "\16\4\2\32\33,,\4\2\32\32,,\u00f0\2\61\3\2\2\2\4?\3\2\2\2\6C\3\2\2\2\b"
			+ "Q\3\2\2\2\nf\3\2\2\2\fz\3\2\2\2\16\u0083\3\2\2\2\20\u008d\3\2\2\2\22\u0095"
			+ "\3\2\2\2\24\u009b\3\2\2\2\26\u00a1\3\2\2\2\30\u00a9\3\2\2\2\32\u00ad\3"
			+ "\2\2\2\34\u00b1\3\2\2\2\36\u00b3\3\2\2\2 \u00b5\3\2\2\2\"\u00bb\3\2\2"
			+ "\2$\u00c1\3\2\2\2&\u00cf\3\2\2\2(\u00d1\3\2\2\2*\u00d3\3\2\2\2,\u00db"
			+ "\3\2\2\2.\u00e3\3\2\2\2\60\62\5\4\3\2\61\60\3\2\2\2\62\63\3\2\2\2\63\61"
			+ "\3\2\2\2\63\64\3\2\2\2\64\65\3\2\2\2\65\66\7\2\2\3\66\3\3\2\2\2\67@\5"
			+ "\6\4\28@\5\b\5\29@\5\n\6\2:@\5\f\7\2;@\5\20\t\2<@\5\22\n\2=@\5\24\13\2"
			+ ">@\5\16\b\2?\67\3\2\2\2?8\3\2\2\2?9\3\2\2\2?:\3\2\2\2?;\3\2\2\2?<\3\2"
			+ "\2\2?=\3\2\2\2?>\3\2\2\2@A\3\2\2\2AB\7%\2\2B\5\3\2\2\2CD\7*\2\2DE\7\35"
			+ "\2\2EF\7\5\2\2FI\5 \21\2GH\7\26\2\2HJ\5$\23\2IG\3\2\2\2IJ\3\2\2\2JK\3"
			+ "\2\2\2KL\7\6\2\2LO\7*\2\2MN\7\25\2\2NP\5,\27\2OM\3\2\2\2OP\3\2\2\2P\7"
			+ "\3\2\2\2QR\7*\2\2RS\7\35\2\2ST\7\5\2\2TW\5\"\22\2UV\7\26\2\2VX\5&\24\2"
			+ "WU\3\2\2\2WX\3\2\2\2XY\3\2\2\2YZ\7\6\2\2Z[\7*\2\2[\\\7\b\2\2\\_\5\26\f"
			+ "\2]^\7\25\2\2^`\5,\27\2_]\3\2\2\2_`\3\2\2\2`a\3\2\2\2ab\7\f\2\2bd\5(\25"
			+ "\2ce\5*\26\2dc\3\2\2\2de\3\2\2\2e\t\3\2\2\2fg\7*\2\2gh\7\35\2\2hi\7\5"
			+ "\2\2il\5 \21\2jk\7\26\2\2km\5&\24\2lj\3\2\2\2lm\3\2\2\2mn\3\2\2\2no\7"
			+ "\6\2\2op\7*\2\2pq\7\7\2\2qt\7*\2\2rs\7\b\2\2su\5\26\f\2tr\3\2\2\2tu\3"
			+ "\2\2\2ux\3\2\2\2vw\7\25\2\2wy\5,\27\2xv\3\2\2\2xy\3\2\2\2y\13\3\2\2\2"
			+ "z{\7*\2\2{|\7\35\2\2|}\7*\2\2}~\t\2\2\2~\u0081\7*\2\2\177\u0080\7\26\2"
			+ "\2\u0080\u0082\7\n\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\r\3"
			+ "\2\2\2\u0083\u0084\7*\2\2\u0084\u0085\7\31\2\2\u0085\u008a\7*\2\2\u0086"
			+ "\u0087\7\31\2\2\u0087\u0089\7*\2\2\u0088\u0086\3\2\2\2\u0089\u008c\3\2"
			+ "\2\2\u008a\u0088\3\2\2\2\u008a\u008b\3\2\2\2\u008b\17\3\2\2\2\u008c\u008a"
			+ "\3\2\2\2\u008d\u008f\7\t\2\2\u008e\u0090\7\n\2\2\u008f\u008e\3\2\2\2\u008f"
			+ "\u0090\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0092\7*\2\2\u0092\u0093\7\13"
			+ "\2\2\u0093\u0094\7*\2\2\u0094\21\3\2\2\2\u0095\u0097\7\27\2\2\u0096\u0098"
			+ "\7\30\2\2\u0097\u0096\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0099\3\2\2\2"
			+ "\u0099\u009a\7*\2\2\u009a\23\3\2\2\2\u009b\u009d\7\21\2\2\u009c\u009e"
			+ "\7\n\2\2\u009d\u009c\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\3\2\2\2\u009f"
			+ "\u00a0\7*\2\2\u00a0\25\3\2\2\2\u00a1\u00a6\5\30\r\2\u00a2\u00a3\7(\2\2"
			+ "\u00a3\u00a5\5\30\r\2\u00a4\u00a2\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4"
			+ "\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\27\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9"
			+ "\u00aa\5\34\17\2\u00aa\u00ab\5\32\16\2\u00ab\u00ac\5\34\17\2\u00ac\31"
			+ "\3\2\2\2\u00ad\u00ae\t\3\2\2\u00ae\33\3\2\2\2\u00af\u00b2\7\34\2\2\u00b0"
			+ "\u00b2\5\36\20\2\u00b1\u00af\3\2\2\2\u00b1\u00b0\3\2\2\2\u00b2\35\3\2"
			+ "\2\2\u00b3\u00b4\t\4\2\2\u00b4\37\3\2\2\2\u00b5\u00b6\t\5\2\2\u00b6\u00b7"
			+ "\7(\2\2\u00b7\u00b8\t\5\2\2\u00b8\u00b9\7(\2\2\u00b9\u00ba\t\5\2\2\u00ba"
			+ "!\3\2\2\2\u00bb\u00bc\7\34\2\2\u00bc\u00bd\7(\2\2\u00bd\u00be\7\34\2\2"
			+ "\u00be\u00bf\7(\2\2\u00bf\u00c0\7\34\2\2\u00c0#\3\2\2\2\u00c1\u00c6\7"
			+ "\34\2\2\u00c2\u00c3\7(\2\2\u00c3\u00c5\7\34\2\2\u00c4\u00c2\3\2\2\2\u00c5"
			+ "\u00c8\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7%\3\2\2\2"
			+ "\u00c8\u00c6\3\2\2\2\u00c9\u00ca\7\3\2\2\u00ca\u00cb\7(\2\2\u00cb\u00d0"
			+ "\7\4\2\2\u00cc\u00cd\7\4\2\2\u00cd\u00ce\7(\2\2\u00ce\u00d0\7\3\2\2\u00cf"
			+ "\u00c9\3\2\2\2\u00cf\u00cc\3\2\2\2\u00d0\'\3\2\2\2\u00d1\u00d2\t\6\2\2"
			+ "\u00d2)\3\2\2\2\u00d3\u00d4\7&\2\2\u00d4\u00d7\t\7\2\2\u00d5\u00d6\7("
			+ "\2\2\u00d6\u00d8\t\b\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8"
			+ "\u00d9\3\2\2\2\u00d9\u00da\7\'\2\2\u00da+\3\2\2\2\u00db\u00e0\5.\30\2"
			+ "\u00dc\u00dd\7\20\2\2\u00dd\u00df\5.\30\2\u00de\u00dc\3\2\2\2\u00df\u00e2"
			+ "\3\2\2\2\u00e0\u00de\3\2\2\2\u00e0\u00e1\3\2\2\2\u00e1-\3\2\2\2\u00e2"
			+ "\u00e0\3\2\2\2\u00e3\u00e8\5\30\r\2\u00e4\u00e5\7\17\2\2\u00e5\u00e7\5"
			+ "\30\r\2\u00e6\u00e4\3\2\2\2\u00e7\u00ea\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e8"
			+ "\u00e9\3\2\2\2\u00e9/\3\2\2\2\u00ea\u00e8\3\2\2\2\30\63?IOW_dltx\u0081"
			+ "\u008a\u008f\u0097\u009d\u00a6\u00b1\u00c6\u00cf\u00d7\u00e0\u00e8";
	public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}