import javax.lang.model.element.TypeElement;

import defNodes.*;
import defNodes.exprNodes.*;
import defNodes.exprNodes.BinaryOpNode.BinaryOprand;
import defNodes.stmtNodes.*;

public class ASTConstructor extends MxParserBaseVisitor<Node> {

    String int_type = "int", bool_type = "bool", string_type = "string", void_type = "void", null_type = "null";

    @Override
    public Node visitProg(MxParser.ProgContext ctx) {
        ProgNode root = new ProgNode();

        FuncType pr_tp = new FuncType();

        pr_tp.return_type = new Type(void_type, 0, false);
        pr_tp.paras = new Type[1];
        pr_tp.paras[0] = new Type(string_type, 0, true);
        root.func_ids.put("print", pr_tp); // (string) -> void
        root.func_ids.put("println", pr_tp);// (string) -> void

        pr_tp.paras[0] = new Type(int_type, 0, true);
        root.func_ids.put("printInt", pr_tp);// (int) -> void
        root.func_ids.put("printlnInt", pr_tp);// (int) -> void

        pr_tp.paras = null;
        pr_tp.return_type = new Type(int_type, 0, false);
        root.func_ids.put("getInt", pr_tp);// () -> int

        pr_tp.return_type = new Type(string_type, 0, false);
        root.func_ids.put("getString", pr_tp);// () -> string

        pr_tp.paras = new Type[1];
        pr_tp.paras[0] = new Type(int_type, 0, true);
        root.func_ids.put("toString", pr_tp);// (int) -> string

        root.global_stmt = new Node[ctx.getChildCount()];
        for (int i = 0; i < ctx.getChildCount(); ++i) {
            root.global_stmt[i] = visit(ctx.getChild(i));
            root.global_stmt[i].father = root;
        }

        return root;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitBasic_type(MxParser.Basic_typeContext ctx) {

        TypeNode node = new TypeNode();
        node.type = new Type(ctx.getText(), 0, true);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitTypename(MxParser.TypenameContext ctx) {

        TypeNode node;

        if (ctx.getChild(0) instanceof MxParser.Basic_typeContext) {
            node = (TypeNode) visit(ctx.getChild(0));
        } else {
            node = new TypeNode();
            node.type = new Type(ctx.getChild(0).getText(), 0, true);
        }

        node.type.dim = (ctx.getChildCount() - 1) / 2;

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitArray_init_tp(MxParser.Array_init_tpContext ctx) {

        TypeNode node;

        if (ctx.getChild(0) instanceof MxParser.Basic_typeContext) {
            node = (TypeNode) visit(ctx.getChild(0));
        } else {
            node = new TypeNode();
            node.type = new Type(ctx.getChild(0).getText(), 0, true);
        }

        int ind = 0;
        for (int i = 1; i < ctx.getChildCount(); ++i) {
            if (ctx.getChild(i).getText() == "[") {
                ++node.type.dim;
            } else if (ctx.getChild(i).getText() == "]") {
            } else {
                ++ind;
            }
        }

        node.dim_lens = new Node[ind];
        for (int i = 0, j = 0; i < ctx.getChildCount() && j < ind; ++i) {
            if (ctx.getChild(i).getText() != "[" && ctx.getChild(i).getText() == "]") {
                node.dim_lens[j] = visit(ctx.getChild(i));
                node.dim_lens[j].father = node;
                ++j;
            }
        }

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitDef_func(MxParser.Def_funcContext ctx) {
        DefFuncNode node = new DefFuncNode();

        if (ctx.getChild(0) instanceof MxParser.TypenameContext) {
            node.type = visit(ctx.getChild(0));
        } else {
            TypeNode _tp = new TypeNode();
            _tp.type = new Type(void_type, 0, false);
            node.type = _tp;
        }
        node.type.father = node;

        node.name = visit(ctx.getChild(1));
        node.name.father = node;

        if (ctx.getChild(3) instanceof MxParser.Para_listContext) {

            MxParser.Para_listContext para_ctx = (MxParser.Para_listContext) ctx.getChild(3);

            node.paras = new DefFuncNode.Para[1 + (para_ctx.getChildCount() - 2) / 3];

            for (int i = 0; i != para_ctx.getChildCount(); ++i) {
                if (i % 3 == 0) {// typename
                    (node.paras[i / 3]).tp = visit(para_ctx.getChild(i));
                    (node.paras[i / 3]).tp.father = node;
                } else if (i % 3 == 1) {// id
                    (node.paras[i / 3]).id = visit(para_ctx.getChild(i));
                    (node.paras[i / 3]).id.father = node;
                }
            }

            for (int i = 0; i != node.paras.length; ++i) {
                String id = ((IdNode) (node.paras[i].id)).id;
                Type tp = ((TypeNode) (node.paras[i].tp)).type;
                node.vars.put(id, tp);
            }

            node.stmt = new Node[ctx.getChildCount() - 7];
            for (int i = 6; i != ctx.getChildCount() - 1; ++i) {
                node.stmt[i - 6] = visit(ctx.getChild(i));
                node.stmt[i - 6].father = node;
            }
        } else {
            node.paras = null;

            node.stmt = new Node[ctx.getChildCount() - 6];
            for (int i = 5; i != ctx.getChildCount() - 1; ++i) {
                node.stmt[i - 5] = visit(ctx.getChild(i));
                node.stmt[i - 5].father = node;
            }
        }

        // TODO: add function to global scope

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitPara_list(MxParser.Para_listContext ctx) {
        // unused
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitDef_class(MxParser.Def_classContext ctx) {

        DefClassNode node = new DefClassNode();

        node.name = visit(ctx.getChild(1));
        node.name.father = node;

        node.stmt = new Node[ctx.getChildCount() - 5];
        for (int i = 3; i != ctx.getChildCount() - 2; ++i) {
            node.stmt[i - 3] = visit(ctx.getChild(i));
            node.stmt[i - 3].father = node;
        }

        // TODO: add class to global scope

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitClass_constructor(MxParser.Class_constructorContext ctx) {
        ClsConsNode node = new ClsConsNode();

        node.id = visit(ctx.getChild(0));
        node.id.father = node;

        node.stmt = new Node[ctx.getChildCount() - 5];
        for (int i = 4; i != ctx.getChildCount() - 1; ++i) {
            node.stmt[i - 4] = visit(ctx.getChild(i));
            node.stmt[i - 4].father = node;
        }

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitArray(MxParser.ArrayContext ctx) {
        ArrayNode node = new ArrayNode();

        node.vals = new Node[(ctx.getChildCount() - 2) / 2 + 1];
        for (int i = 1; i != ctx.getChildCount() - 1; ++i) {
            if (i % 2 == 1) {// expr
                node.vals[i / 2] = visit(ctx.getChild(i));
                node.vals[i / 2].father = node;
            }
        }

        node.type = ((ExprNode) (node.vals[0])).type.clone();

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitExpr_list(MxParser.Expr_listContext ctx) {
        // unused
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitArr(MxParser.ArrContext ctx) {
        ArrayNode node = (ArrayNode) visit(ctx.getChild(0));
        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitNull(MxParser.NullContext ctx) {
        NullNode node = new NullNode();
        node.type = new Type(null_type);
        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitBOr(MxParser.BOrContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = BinaryOpNode.BinaryOprand.BOr;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitEqOps(MxParser.EqOpsContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = ctx.op.getType() == MxParser.Eq ? BinaryOpNode.BinaryOprand.Eq : BinaryOpNode.BinaryOprand.Ne;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitLAnd(MxParser.LAndContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = BinaryOpNode.BinaryOprand.LAnd;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitTrue(MxParser.TrueContext ctx) {
        BoolConstNode node = new BoolConstNode();
        node.type = new Type(bool_type);
        node.val = true;
        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitString(MxParser.StringContext ctx) {
        StringConstNode node = new StringConstNode();
        node.type = new Type(string_type);
        node.val = ctx.getText().substring(1, ctx.getText().length() - 1);
        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitFalse(MxParser.FalseContext ctx) {
        BoolConstNode node = new BoolConstNode();
        node.type = new Type(bool_type);
        node.val = false;
        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitLOr(MxParser.LOrContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = BinaryOpNode.BinaryOprand.LOr;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitArrayAccess(MxParser.ArrayAccessContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitNotOps(MxParser.NotOpsContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitRSelfOps(MxParser.RSelfOpsContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitArrayInit(MxParser.ArrayInitContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitBuiltInPtr(MxParser.BuiltInPtrContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitFString(MxParser.FStringContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitCompOps(MxParser.CompOpsContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        switch (ctx.op.getType()) {
            case MxParser.Lt:
                node.oprand = BinaryOprand.Lt;
                break;
            case MxParser.Le:
                node.oprand = BinaryOprand.Le;
                break;
            case MxParser.Gt:
                node.oprand = BinaryOprand.Gt;
                break;
            case MxParser.Ge:
                node.oprand = BinaryOprand.Ge;
                break;
        }
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitLSelfOps(MxParser.LSelfOpsContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitSignOps(MxParser.SignOpsContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitTernary(MxParser.TernaryContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitMulOps(MxParser.MulOpsContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        switch (ctx.op.getType()) {
            case MxParser.Mul:
                node.oprand = BinaryOprand.Mul;
                break;
            case MxParser.Div:
                node.oprand = BinaryOprand.Div;
                break;
            case MxParser.Mod:
                node.oprand = BinaryOprand.Mod;
                break;
        }
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitBXor(MxParser.BXorContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = BinaryOprand.BXor;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitNum(MxParser.NumContext ctx) {
        NumConstNode node = new NumConstNode();
        node.type = new Type(int_type);
        node.val = Integer.valueOf(ctx.getText());
        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitBAnd(MxParser.BAndContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = BinaryOprand.BAnd;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitAddOps(MxParser.AddOpsContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = ctx.op.getType() == MxParser.Add ? BinaryOprand.Add : BinaryOprand.Sub;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitObjectInit(MxParser.ObjectInitContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitFuncCall(MxParser.FuncCallContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitMemberAccess(MxParser.MemberAccessContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitShiftOps(MxParser.ShiftOpsContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = ctx.op.getType() == MxParser.ShiftL ? BinaryOprand.ShiftL : BinaryOprand.ShiftR;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitAssign(MxParser.AssignContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = BinaryOprand.Assign;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;
        node.type = new Type(int_type);

        return node;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitId(MxParser.IdContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitParenExpr(MxParser.ParenExprContext ctx) {
        return visit(ctx.getChild(1));
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitForm_string(MxParser.Form_stringContext ctx) {
        // TODO:
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitStmt(MxParser.StmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitDef_var_stmt(MxParser.Def_var_stmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitIf_stmt(MxParser.If_stmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitWhile_stmt(MxParser.While_stmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitFor_stmt(MxParser.For_stmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitReturn_stmt(MxParser.Return_stmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitBreak_stmt(MxParser.Break_stmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitContinue_stmt(MxParser.Continue_stmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitExpr_stmt(MxParser.Expr_stmtContext ctx) {
        return visitChildren(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitEmpty_stmt(MxParser.Empty_stmtContext ctx) {
        return visitChildren(ctx);
    }
}
