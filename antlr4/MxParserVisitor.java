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
	 * Visit a parse tree produced by {@link MxParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(MxParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link MxParser#expr_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpr_list(MxParser.Expr_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Arr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArr(MxParser.ArrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Null}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNull(MxParser.NullContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BOr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBOr(MxParser.BOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code EqOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqOps(MxParser.EqOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LAnd}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLAnd(MxParser.LAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code True}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrue(MxParser.TrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(MxParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code False}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalse(MxParser.FalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LOr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLOr(MxParser.LOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayAccess(MxParser.ArrayAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotOps(MxParser.NotOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code RSelfOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRSelfOps(MxParser.RSelfOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ArrayInit}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayInit(MxParser.ArrayInitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BuiltInPtr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBuiltInPtr(MxParser.BuiltInPtrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FString}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFString(MxParser.FStringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code CompOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompOps(MxParser.CompOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LSelfOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLSelfOps(MxParser.LSelfOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SignOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSignOps(MxParser.SignOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Ternary}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTernary(MxParser.TernaryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulOps(MxParser.MulOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BXor}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBXor(MxParser.BXorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Num}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNum(MxParser.NumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code BAnd}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBAnd(MxParser.BAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddOps(MxParser.AddOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ObjectInit}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectInit(MxParser.ObjectInitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FuncCall}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFuncCall(MxParser.FuncCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MemberAccess}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberAccess(MxParser.MemberAccessContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ShiftOps}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShiftOps(MxParser.ShiftOpsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(MxParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Id}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(MxParser.IdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenExpr}
	 * labeled alternative in {@link MxParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenExpr(MxParser.ParenExprContext ctx);
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