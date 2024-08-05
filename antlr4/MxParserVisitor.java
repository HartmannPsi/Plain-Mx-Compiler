// Generated from MxParser.g4 by ANTLR 4.13.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MxParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MxParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MxParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(MxParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#basic_type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasic_type(MxParser.Basic_typeContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#typename}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypename(MxParser.TypenameContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#def_func}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_func(MxParser.Def_funcContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#para_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPara_list(MxParser.Para_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#def_class}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_class(MxParser.Def_classContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#class_constructor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_constructor(MxParser.Class_constructorContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#const_val}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConst_val(MxParser.Const_valContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#array_const}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_const(MxParser.Array_constContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_list(MxParser.Expr_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr(MxParser.ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#form_string_none}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForm_string_none(MxParser.Form_string_noneContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#form_string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForm_string(MxParser.Form_stringContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(MxParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#def_var_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDef_var_stmt(MxParser.Def_var_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#if_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf_stmt(MxParser.If_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#while_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile_stmt(MxParser.While_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#for_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor_stmt(MxParser.For_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#return_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn_stmt(MxParser.Return_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#break_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBreak_stmt(MxParser.Break_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#continue_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContinue_stmt(MxParser.Continue_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expr_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_stmt(MxParser.Expr_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#empty_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmpty_stmt(MxParser.Empty_stmtContext ctx);
}