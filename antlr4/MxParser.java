// Generated from MxParser.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class MxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		NL=1, LineComm=2, BlockComm=3, WS=4, Void=5, Bool=6, Int=7, String=8, 
		New=9, Class=10, Null=11, True=12, False=13, This=14, If=15, Else=16, 
		For=17, While=18, Break=19, Continue=20, Return=21, Add=22, Sub=23, Mul=24, 
		Div=25, Mod=26, Gt=27, Lt=28, Ge=29, Le=30, Ne=31, Eq=32, LogicAnd=33, 
		LogicOr=34, LogicNot=35, ShiftR=36, ShiftL=37, BitAnd=38, BitOr=39, BitXor=40, 
		BitNot=41, Assign=42, SelfAdd=43, SelfSub=44, Component=45, LBracket=46, 
		RBracket=47, LP=48, RP=49, QuesMark=50, Colon=51, Semicolon=52, Comma=53, 
		LBrace=54, RBrace=55, Identifier=56, SpChar=57, StringConst=58, NumberConst=59, 
		LogicConst=60;
	public static final int
		RULE_prog = 0, RULE_basic_type = 1, RULE_typename = 2, RULE_def_func = 3, 
		RULE_para_list = 4, RULE_def_class = 5, RULE_class_constructor = 6, RULE_const_val = 7, 
		RULE_array_const = 8, RULE_expr_list = 9, RULE_expr = 10, RULE_stmt = 11, 
		RULE_def_var_stmt = 12, RULE_if_stmt = 13, RULE_while_stmt = 14, RULE_for_stmt = 15, 
		RULE_return_stmt = 16, RULE_break_stmt = 17, RULE_continue_stmt = 18, 
		RULE_expr_stmt = 19, RULE_empty_stmt = 20;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "basic_type", "typename", "def_func", "para_list", "def_class", 
			"class_constructor", "const_val", "array_const", "expr_list", "expr", 
			"stmt", "def_var_stmt", "if_stmt", "while_stmt", "for_stmt", "return_stmt", 
			"break_stmt", "continue_stmt", "expr_stmt", "empty_stmt"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'void'", "'bool'", "'int'", "'string'", 
			"'new'", "'class'", "'null'", "'true'", "'false'", "'this'", "'if'", 
			"'else'", "'for'", "'while'", "'break'", "'continue'", "'return'", "'+'", 
			"'-'", "'*'", "'/'", "'%'", "'>'", "'<'", "'>='", "'<='", "'!='", "'=='", 
			"'&&'", "'||'", "'!'", "'>>'", "'<<'", "'&'", "'|'", "'^'", "'~'", "'='", 
			"'++'", "'--'", "'.'", "'['", "']'", "'('", "')'", "'?'", "':'", "';'", 
			"','", "'{'", "'}'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "NL", "LineComm", "BlockComm", "WS", "Void", "Bool", "Int", "String", 
			"New", "Class", "Null", "True", "False", "This", "If", "Else", "For", 
			"While", "Break", "Continue", "Return", "Add", "Sub", "Mul", "Div", "Mod", 
			"Gt", "Lt", "Ge", "Le", "Ne", "Eq", "LogicAnd", "LogicOr", "LogicNot", 
			"ShiftR", "ShiftL", "BitAnd", "BitOr", "BitXor", "BitNot", "Assign", 
			"SelfAdd", "SelfSub", "Component", "LBracket", "RBracket", "LP", "RP", 
			"QuesMark", "Colon", "Semicolon", "Comma", "LBrace", "RBrace", "Identifier", 
			"SpChar", "StringConst", "NumberConst", "LogicConst"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
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
	public String getGrammarFileName() { return "MxParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ProgContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MxParser.EOF, 0); }
		public List<Def_funcContext> def_func() {
			return getRuleContexts(Def_funcContext.class);
		}
		public Def_funcContext def_func(int i) {
			return getRuleContext(Def_funcContext.class,i);
		}
		public List<Def_classContext> def_class() {
			return getRuleContexts(Def_classContext.class);
		}
		public Def_classContext def_class(int i) {
			return getRuleContext(Def_classContext.class,i);
		}
		public List<Def_var_stmtContext> def_var_stmt() {
			return getRuleContexts(Def_var_stmtContext.class);
		}
		public Def_var_stmtContext def_var_stmt(int i) {
			return getRuleContext(Def_var_stmtContext.class,i);
		}
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterProg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitProg(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 72057594037929440L) != 0)) {
				{
				setState(45);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(42);
					def_func();
					}
					break;
				case 2:
					{
					setState(43);
					def_class();
					}
					break;
				case 3:
					{
					setState(44);
					def_var_stmt();
					}
					break;
				}
				}
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(50);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Basic_typeContext extends ParserRuleContext {
		public TerminalNode Int() { return getToken(MxParser.Int, 0); }
		public TerminalNode String() { return getToken(MxParser.String, 0); }
		public TerminalNode Bool() { return getToken(MxParser.Bool, 0); }
		public Basic_typeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_basic_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterBasic_type(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitBasic_type(this);
		}
	}

	public final Basic_typeContext basic_type() throws RecognitionException {
		Basic_typeContext _localctx = new Basic_typeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_basic_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 448L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypenameContext extends ParserRuleContext {
		public Basic_typeContext basic_type() {
			return getRuleContext(Basic_typeContext.class,0);
		}
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TypenameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typename; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterTypename(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitTypename(this);
		}
	}

	public final TypenameContext typename() throws RecognitionException {
		TypenameContext _localctx = new TypenameContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_typename);
		try {
			setState(56);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				basic_type();
				}
				break;
			case Identifier:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Def_funcContext extends ParserRuleContext {
		public TypenameContext returnType;
		public Token funcId;
		public Para_listContext paras;
		public StmtContext code;
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode Void() { return getToken(MxParser.Void, 0); }
		public TypenameContext typename() {
			return getRuleContext(TypenameContext.class,0);
		}
		public Para_listContext para_list() {
			return getRuleContext(Para_listContext.class,0);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Def_funcContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_func; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterDef_func(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitDef_func(this);
		}
	}

	public final Def_funcContext def_func() throws RecognitionException {
		Def_funcContext _localctx = new Def_funcContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_def_func);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Bool:
			case Int:
			case String:
			case Identifier:
				{
				setState(58);
				((Def_funcContext)_localctx).returnType = typename();
				}
				break;
			case Void:
				{
				setState(59);
				match(Void);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(62);
			((Def_funcContext)_localctx).funcId = match(Identifier);
			setState(63);
			match(LP);
			setState(65);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 72057594037928384L) != 0)) {
				{
				setState(64);
				((Def_funcContext)_localctx).paras = para_list();
				}
			}

			setState(67);
			match(RP);
			setState(68);
			match(LBrace);
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2112498321892232128L) != 0)) {
				{
				{
				setState(69);
				((Def_funcContext)_localctx).code = stmt();
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(75);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Para_listContext extends ParserRuleContext {
		public List<TypenameContext> typename() {
			return getRuleContexts(TypenameContext.class);
		}
		public TypenameContext typename(int i) {
			return getRuleContext(TypenameContext.class,i);
		}
		public List<TerminalNode> Identifier() { return getTokens(MxParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(MxParser.Identifier, i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public Para_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_para_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterPara_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitPara_list(this);
		}
	}

	public final Para_listContext para_list() throws RecognitionException {
		Para_listContext _localctx = new Para_listContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_para_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(77);
			typename();
			setState(78);
			match(Identifier);
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(79);
				match(Comma);
				setState(80);
				typename();
				setState(81);
				match(Identifier);
				}
				}
				setState(87);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Def_classContext extends ParserRuleContext {
		public Token className;
		public TerminalNode Class() { return getToken(MxParser.Class, 0); }
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<Def_var_stmtContext> def_var_stmt() {
			return getRuleContexts(Def_var_stmtContext.class);
		}
		public Def_var_stmtContext def_var_stmt(int i) {
			return getRuleContext(Def_var_stmtContext.class,i);
		}
		public List<Def_funcContext> def_func() {
			return getRuleContexts(Def_funcContext.class);
		}
		public Def_funcContext def_func(int i) {
			return getRuleContext(Def_funcContext.class,i);
		}
		public List<Class_constructorContext> class_constructor() {
			return getRuleContexts(Class_constructorContext.class);
		}
		public Class_constructorContext class_constructor(int i) {
			return getRuleContext(Class_constructorContext.class,i);
		}
		public Def_classContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_class; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterDef_class(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitDef_class(this);
		}
	}

	public final Def_classContext def_class() throws RecognitionException {
		Def_classContext _localctx = new Def_classContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_def_class);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(Class);
			setState(89);
			((Def_classContext)_localctx).className = match(Identifier);
			setState(90);
			match(LBrace);
			setState(96);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 72057594037928416L) != 0)) {
				{
				setState(94);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
				case 1:
					{
					setState(91);
					def_var_stmt();
					}
					break;
				case 2:
					{
					setState(92);
					def_func();
					}
					break;
				case 3:
					{
					setState(93);
					class_constructor();
					}
					break;
				}
				}
				setState(98);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(99);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Class_constructorContext extends ParserRuleContext {
		public Token className;
		public StmtContext code;
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Class_constructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class_constructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterClass_constructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitClass_constructor(this);
		}
	}

	public final Class_constructorContext class_constructor() throws RecognitionException {
		Class_constructorContext _localctx = new Class_constructorContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_class_constructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			((Class_constructorContext)_localctx).className = match(Identifier);
			setState(102);
			match(LP);
			setState(103);
			match(RP);
			setState(104);
			match(LBrace);
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2112498321892232128L) != 0)) {
				{
				{
				setState(105);
				((Class_constructorContext)_localctx).code = stmt();
				}
				}
				setState(110);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(111);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Const_valContext extends ParserRuleContext {
		public Array_constContext array_const() {
			return getRuleContext(Array_constContext.class,0);
		}
		public TerminalNode StringConst() { return getToken(MxParser.StringConst, 0); }
		public TerminalNode NumberConst() { return getToken(MxParser.NumberConst, 0); }
		public TerminalNode LogicConst() { return getToken(MxParser.LogicConst, 0); }
		public TerminalNode Null() { return getToken(MxParser.Null, 0); }
		public Const_valContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_const_val; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterConst_val(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitConst_val(this);
		}
	}

	public final Const_valContext const_val() throws RecognitionException {
		Const_valContext _localctx = new Const_valContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_const_val);
		try {
			setState(118);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBrace:
				enterOuterAlt(_localctx, 1);
				{
				setState(113);
				array_const();
				}
				break;
			case StringConst:
				enterOuterAlt(_localctx, 2);
				{
				setState(114);
				match(StringConst);
				}
				break;
			case NumberConst:
				enterOuterAlt(_localctx, 3);
				{
				setState(115);
				match(NumberConst);
				}
				break;
			case LogicConst:
				enterOuterAlt(_localctx, 4);
				{
				setState(116);
				match(LogicConst);
				}
				break;
			case Null:
				enterOuterAlt(_localctx, 5);
				{
				setState(117);
				match(Null);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Array_constContext extends ParserRuleContext {
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public List<Const_valContext> const_val() {
			return getRuleContexts(Const_valContext.class);
		}
		public Const_valContext const_val(int i) {
			return getRuleContext(Const_valContext.class,i);
		}
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public Array_constContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_const; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterArray_const(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitArray_const(this);
		}
	}

	public final Array_constContext array_const() throws RecognitionException {
		Array_constContext _localctx = new Array_constContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_array_const);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(LBrace);
			setState(121);
			const_val();
			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(122);
				match(Comma);
				setState(123);
				const_val();
				}
				}
				setState(128);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(129);
			match(RBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Expr_listContext extends ParserRuleContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public Expr_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterExpr_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitExpr_list(this);
		}
	}

	public final Expr_listContext expr_list() throws RecognitionException {
		Expr_listContext _localctx = new Expr_listContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_expr_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			expr(0);
			setState(136);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(132);
				match(Comma);
				setState(133);
				expr(0);
				}
				}
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprContext extends ParserRuleContext {
		public Token op;
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public TerminalNode SelfAdd() { return getToken(MxParser.SelfAdd, 0); }
		public TerminalNode SelfSub() { return getToken(MxParser.SelfSub, 0); }
		public TerminalNode Add() { return getToken(MxParser.Add, 0); }
		public TerminalNode Sub() { return getToken(MxParser.Sub, 0); }
		public TerminalNode LogicNot() { return getToken(MxParser.LogicNot, 0); }
		public TerminalNode BitNot() { return getToken(MxParser.BitNot, 0); }
		public TypenameContext typename() {
			return getRuleContext(TypenameContext.class,0);
		}
		public TerminalNode New() { return getToken(MxParser.New, 0); }
		public TerminalNode NumberConst() { return getToken(MxParser.NumberConst, 0); }
		public TerminalNode StringConst() { return getToken(MxParser.StringConst, 0); }
		public TerminalNode LogicConst() { return getToken(MxParser.LogicConst, 0); }
		public TerminalNode Null() { return getToken(MxParser.Null, 0); }
		public TerminalNode This() { return getToken(MxParser.This, 0); }
		public TerminalNode Identifier() { return getToken(MxParser.Identifier, 0); }
		public TerminalNode Mul() { return getToken(MxParser.Mul, 0); }
		public TerminalNode Div() { return getToken(MxParser.Div, 0); }
		public TerminalNode Mod() { return getToken(MxParser.Mod, 0); }
		public TerminalNode ShiftL() { return getToken(MxParser.ShiftL, 0); }
		public TerminalNode ShiftR() { return getToken(MxParser.ShiftR, 0); }
		public TerminalNode Lt() { return getToken(MxParser.Lt, 0); }
		public TerminalNode Le() { return getToken(MxParser.Le, 0); }
		public TerminalNode Gt() { return getToken(MxParser.Gt, 0); }
		public TerminalNode Ge() { return getToken(MxParser.Ge, 0); }
		public TerminalNode Eq() { return getToken(MxParser.Eq, 0); }
		public TerminalNode Ne() { return getToken(MxParser.Ne, 0); }
		public TerminalNode BitAnd() { return getToken(MxParser.BitAnd, 0); }
		public TerminalNode BitXor() { return getToken(MxParser.BitXor, 0); }
		public TerminalNode BitOr() { return getToken(MxParser.BitOr, 0); }
		public TerminalNode LogicAnd() { return getToken(MxParser.LogicAnd, 0); }
		public TerminalNode LogicOr() { return getToken(MxParser.LogicOr, 0); }
		public TerminalNode Colon() { return getToken(MxParser.Colon, 0); }
		public TerminalNode QuesMark() { return getToken(MxParser.QuesMark, 0); }
		public TerminalNode Assign() { return getToken(MxParser.Assign, 0); }
		public Expr_listContext expr_list() {
			return getRuleContext(Expr_listContext.class,0);
		}
		public TerminalNode LBracket() { return getToken(MxParser.LBracket, 0); }
		public TerminalNode RBracket() { return getToken(MxParser.RBracket, 0); }
		public TerminalNode Component() { return getToken(MxParser.Component, 0); }
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitExpr(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, 20, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LP:
				{
				setState(140);
				match(LP);
				setState(141);
				expr(0);
				setState(142);
				match(RP);
				}
				break;
			case SelfAdd:
			case SelfSub:
				{
				setState(144);
				((ExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==SelfAdd || _la==SelfSub) ) {
					((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(145);
				expr(22);
				}
				break;
			case Add:
			case Sub:
				{
				setState(146);
				((ExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==Add || _la==Sub) ) {
					((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(147);
				expr(21);
				}
				break;
			case LogicNot:
			case BitNot:
				{
				setState(148);
				((ExprContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==LogicNot || _la==BitNot) ) {
					((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(149);
				expr(20);
				}
				break;
			case New:
				{
				setState(150);
				((ExprContext)_localctx).op = match(New);
				setState(151);
				typename();
				setState(154);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(152);
					match(LP);
					setState(153);
					match(RP);
					}
					break;
				}
				}
				break;
			case NumberConst:
				{
				setState(156);
				match(NumberConst);
				}
				break;
			case StringConst:
				{
				setState(157);
				match(StringConst);
				}
				break;
			case LogicConst:
				{
				setState(158);
				match(LogicConst);
				}
				break;
			case Null:
				{
				setState(159);
				match(Null);
				}
				break;
			case This:
				{
				setState(160);
				match(This);
				}
				break;
			case Identifier:
				{
				setState(161);
				match(Identifier);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(221);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(219);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(164);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(165);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 117440512L) != 0)) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(166);
						expr(19);
						}
						break;
					case 2:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(167);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(168);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==Add || _la==Sub) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(169);
						expr(18);
						}
						break;
					case 3:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(170);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(171);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ShiftR || _la==ShiftL) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(172);
						expr(17);
						}
						break;
					case 4:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(173);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(174);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 2013265920L) != 0)) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(175);
						expr(16);
						}
						break;
					case 5:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(176);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(177);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==Ne || _la==Eq) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(178);
						expr(15);
						}
						break;
					case 6:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(179);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(180);
						((ExprContext)_localctx).op = match(BitAnd);
						setState(181);
						expr(14);
						}
						break;
					case 7:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(182);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(183);
						((ExprContext)_localctx).op = match(BitXor);
						setState(184);
						expr(13);
						}
						break;
					case 8:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(185);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(186);
						((ExprContext)_localctx).op = match(BitOr);
						setState(187);
						expr(12);
						}
						break;
					case 9:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(188);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(189);
						((ExprContext)_localctx).op = match(LogicAnd);
						setState(190);
						expr(11);
						}
						break;
					case 10:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(191);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(192);
						((ExprContext)_localctx).op = match(LogicOr);
						setState(193);
						expr(10);
						}
						break;
					case 11:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(194);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(195);
						((ExprContext)_localctx).op = match(QuesMark);
						setState(196);
						expr(0);
						setState(197);
						match(Colon);
						setState(198);
						expr(8);
						}
						break;
					case 12:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(200);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(201);
						((ExprContext)_localctx).op = match(Assign);
						setState(202);
						expr(7);
						}
						break;
					case 13:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(203);
						if (!(precpred(_ctx, 26))) throw new FailedPredicateException(this, "precpred(_ctx, 26)");
						setState(204);
						((ExprContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==SelfAdd || _la==SelfSub) ) {
							((ExprContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					case 14:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(205);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(206);
						match(LP);
						setState(208);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2089980323751283200L) != 0)) {
							{
							setState(207);
							expr_list();
							}
						}

						setState(210);
						match(RP);
						}
						break;
					case 15:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(211);
						if (!(precpred(_ctx, 24))) throw new FailedPredicateException(this, "precpred(_ctx, 24)");
						setState(212);
						match(LBracket);
						setState(213);
						expr(0);
						setState(214);
						match(RBracket);
						}
						break;
					case 16:
						{
						_localctx = new ExprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(216);
						if (!(precpred(_ctx, 23))) throw new FailedPredicateException(this, "precpred(_ctx, 23)");
						setState(217);
						((ExprContext)_localctx).op = match(Component);
						setState(218);
						match(Identifier);
						}
						break;
					}
					} 
				}
				setState(223);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StmtContext extends ParserRuleContext {
		public TerminalNode LBrace() { return getToken(MxParser.LBrace, 0); }
		public TerminalNode RBrace() { return getToken(MxParser.RBrace, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Empty_stmtContext empty_stmt() {
			return getRuleContext(Empty_stmtContext.class,0);
		}
		public Def_var_stmtContext def_var_stmt() {
			return getRuleContext(Def_var_stmtContext.class,0);
		}
		public If_stmtContext if_stmt() {
			return getRuleContext(If_stmtContext.class,0);
		}
		public While_stmtContext while_stmt() {
			return getRuleContext(While_stmtContext.class,0);
		}
		public For_stmtContext for_stmt() {
			return getRuleContext(For_stmtContext.class,0);
		}
		public Return_stmtContext return_stmt() {
			return getRuleContext(Return_stmtContext.class,0);
		}
		public Break_stmtContext break_stmt() {
			return getRuleContext(Break_stmtContext.class,0);
		}
		public Continue_stmtContext continue_stmt() {
			return getRuleContext(Continue_stmtContext.class,0);
		}
		public Expr_stmtContext expr_stmt() {
			return getRuleContext(Expr_stmtContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitStmt(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_stmt);
		int _la;
		try {
			setState(241);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(224);
				match(LBrace);
				setState(228);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2112498321892232128L) != 0)) {
					{
					{
					setState(225);
					stmt();
					}
					}
					setState(230);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(231);
				match(RBrace);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(232);
				empty_stmt();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(233);
				def_var_stmt();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(234);
				if_stmt();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(235);
				while_stmt();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(236);
				for_stmt();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(237);
				return_stmt();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(238);
				break_stmt();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(239);
				continue_stmt();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(240);
				expr_stmt();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Def_var_stmtContext extends ParserRuleContext {
		public TypenameContext typename() {
			return getRuleContext(TypenameContext.class,0);
		}
		public List<TerminalNode> Identifier() { return getTokens(MxParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(MxParser.Identifier, i);
		}
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public List<TerminalNode> Assign() { return getTokens(MxParser.Assign); }
		public TerminalNode Assign(int i) {
			return getToken(MxParser.Assign, i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(MxParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(MxParser.Comma, i);
		}
		public Def_var_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_def_var_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterDef_var_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitDef_var_stmt(this);
		}
	}

	public final Def_var_stmtContext def_var_stmt() throws RecognitionException {
		Def_var_stmtContext _localctx = new Def_var_stmtContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_def_var_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(243);
			typename();
			setState(244);
			match(Identifier);
			setState(247);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Assign) {
				{
				setState(245);
				match(Assign);
				setState(246);
				expr(0);
				}
			}

			setState(257);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(249);
				match(Comma);
				setState(250);
				match(Identifier);
				setState(253);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Assign) {
					{
					setState(251);
					match(Assign);
					setState(252);
					expr(0);
					}
				}

				}
				}
				setState(259);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(260);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class If_stmtContext extends ParserRuleContext {
		public ExprContext cond;
		public StmtContext thenStmt;
		public StmtContext elseStmt;
		public TerminalNode If() { return getToken(MxParser.If, 0); }
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public TerminalNode Else() { return getToken(MxParser.Else, 0); }
		public If_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_if_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterIf_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitIf_stmt(this);
		}
	}

	public final If_stmtContext if_stmt() throws RecognitionException {
		If_stmtContext _localctx = new If_stmtContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_if_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(If);
			setState(263);
			match(LP);
			setState(264);
			((If_stmtContext)_localctx).cond = expr(0);
			setState(265);
			match(RP);
			setState(266);
			((If_stmtContext)_localctx).thenStmt = stmt();
			setState(269);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				{
				setState(267);
				match(Else);
				setState(268);
				((If_stmtContext)_localctx).elseStmt = stmt();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class While_stmtContext extends ParserRuleContext {
		public ExprContext cond;
		public TerminalNode While() { return getToken(MxParser.While, 0); }
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public While_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_while_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterWhile_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitWhile_stmt(this);
		}
	}

	public final While_stmtContext while_stmt() throws RecognitionException {
		While_stmtContext _localctx = new While_stmtContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_while_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(While);
			setState(272);
			match(LP);
			setState(273);
			((While_stmtContext)_localctx).cond = expr(0);
			setState(274);
			match(RP);
			setState(275);
			stmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class For_stmtContext extends ParserRuleContext {
		public StmtContext init;
		public Expr_stmtContext cond;
		public ExprContext step;
		public TerminalNode For() { return getToken(MxParser.For, 0); }
		public TerminalNode LP() { return getToken(MxParser.LP, 0); }
		public TerminalNode RP() { return getToken(MxParser.RP, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Empty_stmtContext empty_stmt() {
			return getRuleContext(Empty_stmtContext.class,0);
		}
		public Expr_stmtContext expr_stmt() {
			return getRuleContext(Expr_stmtContext.class,0);
		}
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public For_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_for_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterFor_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitFor_stmt(this);
		}
	}

	public final For_stmtContext for_stmt() throws RecognitionException {
		For_stmtContext _localctx = new For_stmtContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_for_stmt);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			match(For);
			setState(278);
			match(LP);
			setState(279);
			((For_stmtContext)_localctx).init = stmt();
			setState(282);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case New:
			case Null:
			case This:
			case Add:
			case Sub:
			case LogicNot:
			case BitNot:
			case SelfAdd:
			case SelfSub:
			case LP:
			case Identifier:
			case StringConst:
			case NumberConst:
			case LogicConst:
				{
				setState(280);
				((For_stmtContext)_localctx).cond = expr_stmt();
				}
				break;
			case Semicolon:
				{
				setState(281);
				empty_stmt();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(285);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2089980323751283200L) != 0)) {
				{
				setState(284);
				((For_stmtContext)_localctx).step = expr(0);
				}
			}

			setState(287);
			match(RP);
			setState(288);
			stmt();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Return_stmtContext extends ParserRuleContext {
		public ExprContext val;
		public TerminalNode Return() { return getToken(MxParser.Return, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Return_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_return_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterReturn_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitReturn_stmt(this);
		}
	}

	public final Return_stmtContext return_stmt() throws RecognitionException {
		Return_stmtContext _localctx = new Return_stmtContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_return_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			match(Return);
			setState(291);
			((Return_stmtContext)_localctx).val = expr(0);
			setState(292);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Break_stmtContext extends ParserRuleContext {
		public TerminalNode Break() { return getToken(MxParser.Break, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public Break_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_break_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterBreak_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitBreak_stmt(this);
		}
	}

	public final Break_stmtContext break_stmt() throws RecognitionException {
		Break_stmtContext _localctx = new Break_stmtContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_break_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(294);
			match(Break);
			setState(295);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Continue_stmtContext extends ParserRuleContext {
		public TerminalNode Continue() { return getToken(MxParser.Continue, 0); }
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public Continue_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continue_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterContinue_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitContinue_stmt(this);
		}
	}

	public final Continue_stmtContext continue_stmt() throws RecognitionException {
		Continue_stmtContext _localctx = new Continue_stmtContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_continue_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			match(Continue);
			setState(298);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Expr_stmtContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public Expr_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterExpr_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitExpr_stmt(this);
		}
	}

	public final Expr_stmtContext expr_stmt() throws RecognitionException {
		Expr_stmtContext _localctx = new Expr_stmtContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_expr_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			expr(0);
			setState(301);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Empty_stmtContext extends ParserRuleContext {
		public TerminalNode Semicolon() { return getToken(MxParser.Semicolon, 0); }
		public Empty_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_empty_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).enterEmpty_stmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MxParserListener ) ((MxParserListener)listener).exitEmpty_stmt(this);
		}
	}

	public final Empty_stmtContext empty_stmt() throws RecognitionException {
		Empty_stmtContext _localctx = new Empty_stmtContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_empty_stmt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
			match(Semicolon);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 10:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 18);
		case 1:
			return precpred(_ctx, 17);
		case 2:
			return precpred(_ctx, 16);
		case 3:
			return precpred(_ctx, 15);
		case 4:
			return precpred(_ctx, 14);
		case 5:
			return precpred(_ctx, 13);
		case 6:
			return precpred(_ctx, 12);
		case 7:
			return precpred(_ctx, 11);
		case 8:
			return precpred(_ctx, 10);
		case 9:
			return precpred(_ctx, 9);
		case 10:
			return precpred(_ctx, 8);
		case 11:
			return precpred(_ctx, 7);
		case 12:
			return precpred(_ctx, 26);
		case 13:
			return precpred(_ctx, 25);
		case 14:
			return precpred(_ctx, 24);
		case 15:
			return precpred(_ctx, 23);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001<\u0132\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0005\u0000.\b\u0000\n\u0000\f\u00001\t\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0003\u0002"+
		"9\b\u0002\u0001\u0003\u0001\u0003\u0003\u0003=\b\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0003\u0003B\b\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0005\u0003G\b\u0003\n\u0003\f\u0003J\t\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0005\u0004T\b\u0004\n\u0004\f\u0004W\t\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005_\b"+
		"\u0005\n\u0005\f\u0005b\t\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006k\b\u0006\n\u0006"+
		"\f\u0006n\t\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0003\u0007w\b\u0007\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0005\b}\b\b\n\b\f\b\u0080\t\b\u0001\b\u0001\b\u0001\t\u0001"+
		"\t\u0001\t\u0005\t\u0087\b\t\n\t\f\t\u008a\t\t\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0003\n\u009b\b\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0003\n\u00a3\b\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0003\n\u00d1\b\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0005\n\u00dc\b\n\n\n\f\n\u00df"+
		"\t\n\u0001\u000b\u0001\u000b\u0005\u000b\u00e3\b\u000b\n\u000b\f\u000b"+
		"\u00e6\t\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000b"+
		"\u00f2\b\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0003\f\u00f8\b\f\u0001"+
		"\f\u0001\f\u0001\f\u0001\f\u0003\f\u00fe\b\f\u0005\f\u0100\b\f\n\f\f\f"+
		"\u0103\t\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\r\u0003\r\u010e\b\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0003\u000f\u011b\b\u000f\u0001\u000f\u0003\u000f\u011e"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0014\u0001"+
		"\u0014\u0001\u0014\u0000\u0001\u0014\u0015\u0000\u0002\u0004\u0006\b\n"+
		"\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(\u0000\b"+
		"\u0001\u0000\u0006\b\u0001\u0000+,\u0001\u0000\u0016\u0017\u0002\u0000"+
		"##))\u0001\u0000\u0018\u001a\u0001\u0000$%\u0001\u0000\u001b\u001e\u0001"+
		"\u0000\u001f \u015a\u0000/\u0001\u0000\u0000\u0000\u00024\u0001\u0000"+
		"\u0000\u0000\u00048\u0001\u0000\u0000\u0000\u0006<\u0001\u0000\u0000\u0000"+
		"\bM\u0001\u0000\u0000\u0000\nX\u0001\u0000\u0000\u0000\fe\u0001\u0000"+
		"\u0000\u0000\u000ev\u0001\u0000\u0000\u0000\u0010x\u0001\u0000\u0000\u0000"+
		"\u0012\u0083\u0001\u0000\u0000\u0000\u0014\u00a2\u0001\u0000\u0000\u0000"+
		"\u0016\u00f1\u0001\u0000\u0000\u0000\u0018\u00f3\u0001\u0000\u0000\u0000"+
		"\u001a\u0106\u0001\u0000\u0000\u0000\u001c\u010f\u0001\u0000\u0000\u0000"+
		"\u001e\u0115\u0001\u0000\u0000\u0000 \u0122\u0001\u0000\u0000\u0000\""+
		"\u0126\u0001\u0000\u0000\u0000$\u0129\u0001\u0000\u0000\u0000&\u012c\u0001"+
		"\u0000\u0000\u0000(\u012f\u0001\u0000\u0000\u0000*.\u0003\u0006\u0003"+
		"\u0000+.\u0003\n\u0005\u0000,.\u0003\u0018\f\u0000-*\u0001\u0000\u0000"+
		"\u0000-+\u0001\u0000\u0000\u0000-,\u0001\u0000\u0000\u0000.1\u0001\u0000"+
		"\u0000\u0000/-\u0001\u0000\u0000\u0000/0\u0001\u0000\u0000\u000002\u0001"+
		"\u0000\u0000\u00001/\u0001\u0000\u0000\u000023\u0005\u0000\u0000\u0001"+
		"3\u0001\u0001\u0000\u0000\u000045\u0007\u0000\u0000\u00005\u0003\u0001"+
		"\u0000\u0000\u000069\u0003\u0002\u0001\u000079\u00058\u0000\u000086\u0001"+
		"\u0000\u0000\u000087\u0001\u0000\u0000\u00009\u0005\u0001\u0000\u0000"+
		"\u0000:=\u0003\u0004\u0002\u0000;=\u0005\u0005\u0000\u0000<:\u0001\u0000"+
		"\u0000\u0000<;\u0001\u0000\u0000\u0000=>\u0001\u0000\u0000\u0000>?\u0005"+
		"8\u0000\u0000?A\u00050\u0000\u0000@B\u0003\b\u0004\u0000A@\u0001\u0000"+
		"\u0000\u0000AB\u0001\u0000\u0000\u0000BC\u0001\u0000\u0000\u0000CD\u0005"+
		"1\u0000\u0000DH\u00056\u0000\u0000EG\u0003\u0016\u000b\u0000FE\u0001\u0000"+
		"\u0000\u0000GJ\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000\u0000HI\u0001"+
		"\u0000\u0000\u0000IK\u0001\u0000\u0000\u0000JH\u0001\u0000\u0000\u0000"+
		"KL\u00057\u0000\u0000L\u0007\u0001\u0000\u0000\u0000MN\u0003\u0004\u0002"+
		"\u0000NU\u00058\u0000\u0000OP\u00055\u0000\u0000PQ\u0003\u0004\u0002\u0000"+
		"QR\u00058\u0000\u0000RT\u0001\u0000\u0000\u0000SO\u0001\u0000\u0000\u0000"+
		"TW\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000"+
		"\u0000V\t\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000XY\u0005\n"+
		"\u0000\u0000YZ\u00058\u0000\u0000Z`\u00056\u0000\u0000[_\u0003\u0018\f"+
		"\u0000\\_\u0003\u0006\u0003\u0000]_\u0003\f\u0006\u0000^[\u0001\u0000"+
		"\u0000\u0000^\\\u0001\u0000\u0000\u0000^]\u0001\u0000\u0000\u0000_b\u0001"+
		"\u0000\u0000\u0000`^\u0001\u0000\u0000\u0000`a\u0001\u0000\u0000\u0000"+
		"ac\u0001\u0000\u0000\u0000b`\u0001\u0000\u0000\u0000cd\u00057\u0000\u0000"+
		"d\u000b\u0001\u0000\u0000\u0000ef\u00058\u0000\u0000fg\u00050\u0000\u0000"+
		"gh\u00051\u0000\u0000hl\u00056\u0000\u0000ik\u0003\u0016\u000b\u0000j"+
		"i\u0001\u0000\u0000\u0000kn\u0001\u0000\u0000\u0000lj\u0001\u0000\u0000"+
		"\u0000lm\u0001\u0000\u0000\u0000mo\u0001\u0000\u0000\u0000nl\u0001\u0000"+
		"\u0000\u0000op\u00057\u0000\u0000p\r\u0001\u0000\u0000\u0000qw\u0003\u0010"+
		"\b\u0000rw\u0005:\u0000\u0000sw\u0005;\u0000\u0000tw\u0005<\u0000\u0000"+
		"uw\u0005\u000b\u0000\u0000vq\u0001\u0000\u0000\u0000vr\u0001\u0000\u0000"+
		"\u0000vs\u0001\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000vu\u0001\u0000"+
		"\u0000\u0000w\u000f\u0001\u0000\u0000\u0000xy\u00056\u0000\u0000y~\u0003"+
		"\u000e\u0007\u0000z{\u00055\u0000\u0000{}\u0003\u000e\u0007\u0000|z\u0001"+
		"\u0000\u0000\u0000}\u0080\u0001\u0000\u0000\u0000~|\u0001\u0000\u0000"+
		"\u0000~\u007f\u0001\u0000\u0000\u0000\u007f\u0081\u0001\u0000\u0000\u0000"+
		"\u0080~\u0001\u0000\u0000\u0000\u0081\u0082\u00057\u0000\u0000\u0082\u0011"+
		"\u0001\u0000\u0000\u0000\u0083\u0088\u0003\u0014\n\u0000\u0084\u0085\u0005"+
		"5\u0000\u0000\u0085\u0087\u0003\u0014\n\u0000\u0086\u0084\u0001\u0000"+
		"\u0000\u0000\u0087\u008a\u0001\u0000\u0000\u0000\u0088\u0086\u0001\u0000"+
		"\u0000\u0000\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u0013\u0001\u0000"+
		"\u0000\u0000\u008a\u0088\u0001\u0000\u0000\u0000\u008b\u008c\u0006\n\uffff"+
		"\uffff\u0000\u008c\u008d\u00050\u0000\u0000\u008d\u008e\u0003\u0014\n"+
		"\u0000\u008e\u008f\u00051\u0000\u0000\u008f\u00a3\u0001\u0000\u0000\u0000"+
		"\u0090\u0091\u0007\u0001\u0000\u0000\u0091\u00a3\u0003\u0014\n\u0016\u0092"+
		"\u0093\u0007\u0002\u0000\u0000\u0093\u00a3\u0003\u0014\n\u0015\u0094\u0095"+
		"\u0007\u0003\u0000\u0000\u0095\u00a3\u0003\u0014\n\u0014\u0096\u0097\u0005"+
		"\t\u0000\u0000\u0097\u009a\u0003\u0004\u0002\u0000\u0098\u0099\u00050"+
		"\u0000\u0000\u0099\u009b\u00051\u0000\u0000\u009a\u0098\u0001\u0000\u0000"+
		"\u0000\u009a\u009b\u0001\u0000\u0000\u0000\u009b\u00a3\u0001\u0000\u0000"+
		"\u0000\u009c\u00a3\u0005;\u0000\u0000\u009d\u00a3\u0005:\u0000\u0000\u009e"+
		"\u00a3\u0005<\u0000\u0000\u009f\u00a3\u0005\u000b\u0000\u0000\u00a0\u00a3"+
		"\u0005\u000e\u0000\u0000\u00a1\u00a3\u00058\u0000\u0000\u00a2\u008b\u0001"+
		"\u0000\u0000\u0000\u00a2\u0090\u0001\u0000\u0000\u0000\u00a2\u0092\u0001"+
		"\u0000\u0000\u0000\u00a2\u0094\u0001\u0000\u0000\u0000\u00a2\u0096\u0001"+
		"\u0000\u0000\u0000\u00a2\u009c\u0001\u0000\u0000\u0000\u00a2\u009d\u0001"+
		"\u0000\u0000\u0000\u00a2\u009e\u0001\u0000\u0000\u0000\u00a2\u009f\u0001"+
		"\u0000\u0000\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000\u00a2\u00a1\u0001"+
		"\u0000\u0000\u0000\u00a3\u00dd\u0001\u0000\u0000\u0000\u00a4\u00a5\n\u0012"+
		"\u0000\u0000\u00a5\u00a6\u0007\u0004\u0000\u0000\u00a6\u00dc\u0003\u0014"+
		"\n\u0013\u00a7\u00a8\n\u0011\u0000\u0000\u00a8\u00a9\u0007\u0002\u0000"+
		"\u0000\u00a9\u00dc\u0003\u0014\n\u0012\u00aa\u00ab\n\u0010\u0000\u0000"+
		"\u00ab\u00ac\u0007\u0005\u0000\u0000\u00ac\u00dc\u0003\u0014\n\u0011\u00ad"+
		"\u00ae\n\u000f\u0000\u0000\u00ae\u00af\u0007\u0006\u0000\u0000\u00af\u00dc"+
		"\u0003\u0014\n\u0010\u00b0\u00b1\n\u000e\u0000\u0000\u00b1\u00b2\u0007"+
		"\u0007\u0000\u0000\u00b2\u00dc\u0003\u0014\n\u000f\u00b3\u00b4\n\r\u0000"+
		"\u0000\u00b4\u00b5\u0005&\u0000\u0000\u00b5\u00dc\u0003\u0014\n\u000e"+
		"\u00b6\u00b7\n\f\u0000\u0000\u00b7\u00b8\u0005(\u0000\u0000\u00b8\u00dc"+
		"\u0003\u0014\n\r\u00b9\u00ba\n\u000b\u0000\u0000\u00ba\u00bb\u0005\'\u0000"+
		"\u0000\u00bb\u00dc\u0003\u0014\n\f\u00bc\u00bd\n\n\u0000\u0000\u00bd\u00be"+
		"\u0005!\u0000\u0000\u00be\u00dc\u0003\u0014\n\u000b\u00bf\u00c0\n\t\u0000"+
		"\u0000\u00c0\u00c1\u0005\"\u0000\u0000\u00c1\u00dc\u0003\u0014\n\n\u00c2"+
		"\u00c3\n\b\u0000\u0000\u00c3\u00c4\u00052\u0000\u0000\u00c4\u00c5\u0003"+
		"\u0014\n\u0000\u00c5\u00c6\u00053\u0000\u0000\u00c6\u00c7\u0003\u0014"+
		"\n\b\u00c7\u00dc\u0001\u0000\u0000\u0000\u00c8\u00c9\n\u0007\u0000\u0000"+
		"\u00c9\u00ca\u0005*\u0000\u0000\u00ca\u00dc\u0003\u0014\n\u0007\u00cb"+
		"\u00cc\n\u001a\u0000\u0000\u00cc\u00dc\u0007\u0001\u0000\u0000\u00cd\u00ce"+
		"\n\u0019\u0000\u0000\u00ce\u00d0\u00050\u0000\u0000\u00cf\u00d1\u0003"+
		"\u0012\t\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d0\u00d1\u0001\u0000"+
		"\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000\u0000\u00d2\u00dc\u00051\u0000"+
		"\u0000\u00d3\u00d4\n\u0018\u0000\u0000\u00d4\u00d5\u0005.\u0000\u0000"+
		"\u00d5\u00d6\u0003\u0014\n\u0000\u00d6\u00d7\u0005/\u0000\u0000\u00d7"+
		"\u00dc\u0001\u0000\u0000\u0000\u00d8\u00d9\n\u0017\u0000\u0000\u00d9\u00da"+
		"\u0005-\u0000\u0000\u00da\u00dc\u00058\u0000\u0000\u00db\u00a4\u0001\u0000"+
		"\u0000\u0000\u00db\u00a7\u0001\u0000\u0000\u0000\u00db\u00aa\u0001\u0000"+
		"\u0000\u0000\u00db\u00ad\u0001\u0000\u0000\u0000\u00db\u00b0\u0001\u0000"+
		"\u0000\u0000\u00db\u00b3\u0001\u0000\u0000\u0000\u00db\u00b6\u0001\u0000"+
		"\u0000\u0000\u00db\u00b9\u0001\u0000\u0000\u0000\u00db\u00bc\u0001\u0000"+
		"\u0000\u0000\u00db\u00bf\u0001\u0000\u0000\u0000\u00db\u00c2\u0001\u0000"+
		"\u0000\u0000\u00db\u00c8\u0001\u0000\u0000\u0000\u00db\u00cb\u0001\u0000"+
		"\u0000\u0000\u00db\u00cd\u0001\u0000\u0000\u0000\u00db\u00d3\u0001\u0000"+
		"\u0000\u0000\u00db\u00d8\u0001\u0000\u0000\u0000\u00dc\u00df\u0001\u0000"+
		"\u0000\u0000\u00dd\u00db\u0001\u0000\u0000\u0000\u00dd\u00de\u0001\u0000"+
		"\u0000\u0000\u00de\u0015\u0001\u0000\u0000\u0000\u00df\u00dd\u0001\u0000"+
		"\u0000\u0000\u00e0\u00e4\u00056\u0000\u0000\u00e1\u00e3\u0003\u0016\u000b"+
		"\u0000\u00e2\u00e1\u0001\u0000\u0000\u0000\u00e3\u00e6\u0001\u0000\u0000"+
		"\u0000\u00e4\u00e2\u0001\u0000\u0000\u0000\u00e4\u00e5\u0001\u0000\u0000"+
		"\u0000\u00e5\u00e7\u0001\u0000\u0000\u0000\u00e6\u00e4\u0001\u0000\u0000"+
		"\u0000\u00e7\u00f2\u00057\u0000\u0000\u00e8\u00f2\u0003(\u0014\u0000\u00e9"+
		"\u00f2\u0003\u0018\f\u0000\u00ea\u00f2\u0003\u001a\r\u0000\u00eb\u00f2"+
		"\u0003\u001c\u000e\u0000\u00ec\u00f2\u0003\u001e\u000f\u0000\u00ed\u00f2"+
		"\u0003 \u0010\u0000\u00ee\u00f2\u0003\"\u0011\u0000\u00ef\u00f2\u0003"+
		"$\u0012\u0000\u00f0\u00f2\u0003&\u0013\u0000\u00f1\u00e0\u0001\u0000\u0000"+
		"\u0000\u00f1\u00e8\u0001\u0000\u0000\u0000\u00f1\u00e9\u0001\u0000\u0000"+
		"\u0000\u00f1\u00ea\u0001\u0000\u0000\u0000\u00f1\u00eb\u0001\u0000\u0000"+
		"\u0000\u00f1\u00ec\u0001\u0000\u0000\u0000\u00f1\u00ed\u0001\u0000\u0000"+
		"\u0000\u00f1\u00ee\u0001\u0000\u0000\u0000\u00f1\u00ef\u0001\u0000\u0000"+
		"\u0000\u00f1\u00f0\u0001\u0000\u0000\u0000\u00f2\u0017\u0001\u0000\u0000"+
		"\u0000\u00f3\u00f4\u0003\u0004\u0002\u0000\u00f4\u00f7\u00058\u0000\u0000"+
		"\u00f5\u00f6\u0005*\u0000\u0000\u00f6\u00f8\u0003\u0014\n\u0000\u00f7"+
		"\u00f5\u0001\u0000\u0000\u0000\u00f7\u00f8\u0001\u0000\u0000\u0000\u00f8"+
		"\u0101\u0001\u0000\u0000\u0000\u00f9\u00fa\u00055\u0000\u0000\u00fa\u00fd"+
		"\u00058\u0000\u0000\u00fb\u00fc\u0005*\u0000\u0000\u00fc\u00fe\u0003\u0014"+
		"\n\u0000\u00fd\u00fb\u0001\u0000\u0000\u0000\u00fd\u00fe\u0001\u0000\u0000"+
		"\u0000\u00fe\u0100\u0001\u0000\u0000\u0000\u00ff\u00f9\u0001\u0000\u0000"+
		"\u0000\u0100\u0103\u0001\u0000\u0000\u0000\u0101\u00ff\u0001\u0000\u0000"+
		"\u0000\u0101\u0102\u0001\u0000\u0000\u0000\u0102\u0104\u0001\u0000\u0000"+
		"\u0000\u0103\u0101\u0001\u0000\u0000\u0000\u0104\u0105\u00054\u0000\u0000"+
		"\u0105\u0019\u0001\u0000\u0000\u0000\u0106\u0107\u0005\u000f\u0000\u0000"+
		"\u0107\u0108\u00050\u0000\u0000\u0108\u0109\u0003\u0014\n\u0000\u0109"+
		"\u010a\u00051\u0000\u0000\u010a\u010d\u0003\u0016\u000b\u0000\u010b\u010c"+
		"\u0005\u0010\u0000\u0000\u010c\u010e\u0003\u0016\u000b\u0000\u010d\u010b"+
		"\u0001\u0000\u0000\u0000\u010d\u010e\u0001\u0000\u0000\u0000\u010e\u001b"+
		"\u0001\u0000\u0000\u0000\u010f\u0110\u0005\u0012\u0000\u0000\u0110\u0111"+
		"\u00050\u0000\u0000\u0111\u0112\u0003\u0014\n\u0000\u0112\u0113\u0005"+
		"1\u0000\u0000\u0113\u0114\u0003\u0016\u000b\u0000\u0114\u001d\u0001\u0000"+
		"\u0000\u0000\u0115\u0116\u0005\u0011\u0000\u0000\u0116\u0117\u00050\u0000"+
		"\u0000\u0117\u011a\u0003\u0016\u000b\u0000\u0118\u011b\u0003&\u0013\u0000"+
		"\u0119\u011b\u0003(\u0014\u0000\u011a\u0118\u0001\u0000\u0000\u0000\u011a"+
		"\u0119\u0001\u0000\u0000\u0000\u011b\u011d\u0001\u0000\u0000\u0000\u011c"+
		"\u011e\u0003\u0014\n\u0000\u011d\u011c\u0001\u0000\u0000\u0000\u011d\u011e"+
		"\u0001\u0000\u0000\u0000\u011e\u011f\u0001\u0000\u0000\u0000\u011f\u0120"+
		"\u00051\u0000\u0000\u0120\u0121\u0003\u0016\u000b\u0000\u0121\u001f\u0001"+
		"\u0000\u0000\u0000\u0122\u0123\u0005\u0015\u0000\u0000\u0123\u0124\u0003"+
		"\u0014\n\u0000\u0124\u0125\u00054\u0000\u0000\u0125!\u0001\u0000\u0000"+
		"\u0000\u0126\u0127\u0005\u0013\u0000\u0000\u0127\u0128\u00054\u0000\u0000"+
		"\u0128#\u0001\u0000\u0000\u0000\u0129\u012a\u0005\u0014\u0000\u0000\u012a"+
		"\u012b\u00054\u0000\u0000\u012b%\u0001\u0000\u0000\u0000\u012c\u012d\u0003"+
		"\u0014\n\u0000\u012d\u012e\u00054\u0000\u0000\u012e\'\u0001\u0000\u0000"+
		"\u0000\u012f\u0130\u00054\u0000\u0000\u0130)\u0001\u0000\u0000\u0000\u001a"+
		"-/8<AHU^`lv~\u0088\u009a\u00a2\u00d0\u00db\u00dd\u00e4\u00f1\u00f7\u00fd"+
		"\u0101\u010d\u011a\u011d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}