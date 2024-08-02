// Generated from MxParser.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxParser}.
 */
public interface MxParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(MxParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(MxParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#basic_type}.
	 * @param ctx the parse tree
	 */
	void enterBasic_type(MxParser.Basic_typeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#basic_type}.
	 * @param ctx the parse tree
	 */
	void exitBasic_type(MxParser.Basic_typeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#typename}.
	 * @param ctx the parse tree
	 */
	void enterTypename(MxParser.TypenameContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#typename}.
	 * @param ctx the parse tree
	 */
	void exitTypename(MxParser.TypenameContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#def_func}.
	 * @param ctx the parse tree
	 */
	void enterDef_func(MxParser.Def_funcContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#def_func}.
	 * @param ctx the parse tree
	 */
	void exitDef_func(MxParser.Def_funcContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#para_list}.
	 * @param ctx the parse tree
	 */
	void enterPara_list(MxParser.Para_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#para_list}.
	 * @param ctx the parse tree
	 */
	void exitPara_list(MxParser.Para_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#def_class}.
	 * @param ctx the parse tree
	 */
	void enterDef_class(MxParser.Def_classContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#def_class}.
	 * @param ctx the parse tree
	 */
	void exitDef_class(MxParser.Def_classContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#class_constructor}.
	 * @param ctx the parse tree
	 */
	void enterClass_constructor(MxParser.Class_constructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#class_constructor}.
	 * @param ctx the parse tree
	 */
	void exitClass_constructor(MxParser.Class_constructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#const_val}.
	 * @param ctx the parse tree
	 */
	void enterConst_val(MxParser.Const_valContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#const_val}.
	 * @param ctx the parse tree
	 */
	void exitConst_val(MxParser.Const_valContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#array_const}.
	 * @param ctx the parse tree
	 */
	void enterArray_const(MxParser.Array_constContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#array_const}.
	 * @param ctx the parse tree
	 */
	void exitArray_const(MxParser.Array_constContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void enterExpr_list(MxParser.Expr_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#expr_list}.
	 * @param ctx the parse tree
	 */
	void exitExpr_list(MxParser.Expr_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterExpr(MxParser.ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitExpr(MxParser.ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(MxParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(MxParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#def_var_stmt}.
	 * @param ctx the parse tree
	 */
	void enterDef_var_stmt(MxParser.Def_var_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#def_var_stmt}.
	 * @param ctx the parse tree
	 */
	void exitDef_var_stmt(MxParser.Def_var_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void enterIf_stmt(MxParser.If_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#if_stmt}.
	 * @param ctx the parse tree
	 */
	void exitIf_stmt(MxParser.If_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhile_stmt(MxParser.While_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#while_stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhile_stmt(MxParser.While_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#for_stmt}.
	 * @param ctx the parse tree
	 */
	void enterFor_stmt(MxParser.For_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#for_stmt}.
	 * @param ctx the parse tree
	 */
	void exitFor_stmt(MxParser.For_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn_stmt(MxParser.Return_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#return_stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn_stmt(MxParser.Return_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void enterBreak_stmt(MxParser.Break_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#break_stmt}.
	 * @param ctx the parse tree
	 */
	void exitBreak_stmt(MxParser.Break_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#continue_stmt}.
	 * @param ctx the parse tree
	 */
	void enterContinue_stmt(MxParser.Continue_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#continue_stmt}.
	 * @param ctx the parse tree
	 */
	void exitContinue_stmt(MxParser.Continue_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void enterExpr_stmt(MxParser.Expr_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#expr_stmt}.
	 * @param ctx the parse tree
	 */
	void exitExpr_stmt(MxParser.Expr_stmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#empty_stmt}.
	 * @param ctx the parse tree
	 */
	void enterEmpty_stmt(MxParser.Empty_stmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#empty_stmt}.
	 * @param ctx the parse tree
	 */
	void exitEmpty_stmt(MxParser.Empty_stmtContext ctx);
}