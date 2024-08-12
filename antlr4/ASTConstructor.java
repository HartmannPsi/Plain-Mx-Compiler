
import java.util.Map;
import java.util.Set;
import org.antlr.v4.runtime.ParserRuleContext;
import defNodes.*;
import defNodes.exprNodes.*;
import defNodes.exprNodes.BinaryOpNode.BinaryOprand;
import defNodes.exprNodes.UnaryOpNode.UnaryOprand;
import defNodes.stmtNodes.*;
import util.error.*;
import util.position;

public class ASTConstructor extends MxParserBaseVisitor<Node> {

    String int_type = "int", bool_type = "bool", string_type = "string", void_type = "void", null_type = "null",
            mul_def = "Multiple Definitions", inv_id = "Invalid Identifier", undef_id = "Undefined Identifier",
            tp_mis = "Type Mismatch", inv_ctrl = "Invalid Control Flow", inv_tp = "Invalid Type",
            miss_ret = "Missing Return Statement", out_of_bd = "Dimension Out Of Bound";
    Set<String> global_class = null;
    Map<String, FuncType> global_func = null;
    Map<String, Type> global_var = null;
    public ProgNode root = null;

    void throw_semantic(String error, ParserRuleContext ctx) {
        throw new semanticError(error, new position(ctx));
    }

    @Override
    public Node visitProg(MxParser.ProgContext ctx) {
        root = new ProgNode();

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

        // System.out.println(ctx.getChildCount());

        for (int i = 0; i < ctx.getChildCount() - 1; ++i) {
            root.global_stmt[i] = visit(ctx.getChild(i));
            root.global_stmt[i].father = root;
        }

        global_class = root.class_ids;
        global_func = root.func_ids;
        global_var = root.vars;

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
        for (int i = 1; i != ctx.getChildCount(); ++i) {
            if (ctx.getChild(i).getText().equals("[")) {
                ++node.type.dim;
            } else if (ctx.getChild(i).getText().equals("]")) {
            } else {
                ++ind;
            }
        }

        node.dim_lens = new Node[ind];
        for (int j = 0; j != ind; ++j) {
            node.dim_lens[j] = visit(ctx.expr(j));
            node.dim_lens[j].father = node;
            ++j;

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

        node.name = new IdNode();
        ((IdNode) node.name).id = ctx.Identifier().getText();
        node.name.father = node;

        if (ctx.getChild(3) instanceof MxParser.Para_listContext) {

            MxParser.Para_listContext para_ctx = (MxParser.Para_listContext) ctx.getChild(3);

            // node.paras = new DefFuncNode.Para[1 + (para_ctx.getChildCount() - 2) / 3];
            node.tps = new Node[1 + (para_ctx.getChildCount() - 2) / 3];
            node.ids = new Node[1 + (para_ctx.getChildCount() - 2) / 3];

            for (int i = 0; i != para_ctx.getChildCount(); ++i) {
                if (i % 3 == 0) {// typename
                    node.tps[i / 3] = visit(para_ctx.getChild(i));
                    node.tps[i / 3].father = node;
                } else if (i % 3 == 1) {// id
                    node.ids[i / 3] = new IdNode();
                    ((IdNode) node.ids[i / 3]).id = para_ctx.getChild(i).getText();
                    node.ids[i / 3].father = node;
                }
            }

            for (int i = 0; i != node.tps.length; ++i) {
                String id = ((IdNode) (node.ids[i])).id;
                Type tp = ((TypeNode) (node.tps[i])).type;
                node.vars.put(id, tp);
            }

            node.stmt = new Node[ctx.getChildCount() - 7];
            for (int i = 6; i != ctx.getChildCount() - 1; ++i) {
                node.stmt[i - 6] = visit(ctx.getChild(i));
                node.stmt[i - 6].father = node;
            }
        } else {
            // node.paras = null;

            node.stmt = new Node[ctx.getChildCount() - 6];
            for (int i = 5; i != ctx.getChildCount() - 1; ++i) {
                node.stmt[i - 5] = visit(ctx.getChild(i));
                node.stmt[i - 5].father = node;
            }
        }

        // String id = ((IdNode) node.name).id;
        // FuncType type = new FuncType();
        // type.return_type = ((TypeNode) node.type).type.clone();

        // if (node.paras != null) {
        // type.paras = new Type[node.paras.length];
        // for (int i = 0; i != type.paras.length; ++i) {
        // type.paras[i] = ((TypeNode) node.paras[i].tp).type.clone();
        // }
        // }
        /// addToFuncs(id, type, node, ctx);

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

        node.name = new IdNode();
        ((IdNode) node.name).id = ctx.Identifier().getText();
        node.name.father = node;

        node.stmt = new Node[ctx.getChildCount() - 5];
        for (int i = 3; i != ctx.getChildCount() - 2; ++i) {
            node.stmt[i - 3] = visit(ctx.getChild(i));
            node.stmt[i - 3].father = node;
        }

        // String id = ((IdNode) node.name).id;
        // addToCls(id, node, ctx);

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

        node.id = new IdNode();
        ((IdNode) node.id).id = ctx.Identifier().getText();
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
        ArrayAccessNode node = new ArrayAccessNode();

        node.array = visit(ctx.arrayName);
        node.serial = visit(ctx.serial);
        node.array.father = node.serial.father = node;

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
    public Node visitNotOps(MxParser.NotOpsContext ctx) {
        UnaryOpNode node = new UnaryOpNode();
        node.oprand = ctx.op.getType() == MxParser.LogicNot ? UnaryOprand.LNot : UnaryOprand.BNot;
        node.expr = visit(ctx.expr());
        node.expr.father = node;

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
    public Node visitRSelfOps(MxParser.RSelfOpsContext ctx) {
        UnaryOpNode node = new UnaryOpNode();
        node.oprand = ctx.op.getType() == MxParser.SelfAdd ? UnaryOprand.SAddR : UnaryOprand.SSubR;
        node.expr = visit(ctx.expr());
        node.expr.father = node;

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
    public Node visitArrayInit(MxParser.ArrayInitContext ctx) {
        NewExprNode node = new NewExprNode();
        TypeNode _tp = (TypeNode) visit(ctx.array_init_tp());
        node.create_type = _tp;
        node.lengths = _tp.dim_lens;
        _tp.father = node;
        for (int i = 0; i != node.lengths.length; ++i) {
            if (node.lengths[i] != null) {
                node.lengths[i].father = node;
            }
        }
        _tp.dim_lens = null;
        if (ctx.array() != null) {
            node.init_array = visit(ctx.array());
            node.init_array.father = node;
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
    public Node visitBuiltInPtr(MxParser.BuiltInPtrContext ctx) {

        ThisNode node = new ThisNode();

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
    public Node visitFString(MxParser.FStringContext ctx) {
        // unused
        return visit(ctx.form_string());
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
        UnaryOpNode node = new UnaryOpNode();
        node.oprand = ctx.op.getType() == MxParser.SelfAdd ? UnaryOprand.SAddL : UnaryOprand.SSubL;
        node.expr = visit(ctx.expr());
        node.expr.father = node;

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
    public Node visitSignOps(MxParser.SignOpsContext ctx) {
        UnaryOpNode node = new UnaryOpNode();
        node.oprand = ctx.op.getType() == MxParser.Add ? UnaryOprand.Plus : UnaryOprand.Minus;
        node.expr = visit(ctx.expr());
        node.expr.father = node;

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
    public Node visitTernary(MxParser.TernaryContext ctx) {
        TernaryOpNode node = new TernaryOpNode();
        node.cond = visit(ctx.expr(0));
        node.lhs = visit(ctx.expr(1));
        node.rhs = visit(ctx.expr(2));
        node.cond.father = node.lhs.father = node.rhs.father = node;

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
        NewExprNode node = new NewExprNode();
        node.create_type = visit(ctx.typename());
        node.create_type.father = node;

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
    public Node visitFuncCall(MxParser.FuncCallContext ctx) {
        FuncCallNode node = new FuncCallNode();
        node.id = visit(ctx.funcName);
        node.id.father = node;

        if (ctx.expr_list() != null) {
            MxParser.Expr_listContext expr_ctx = ctx.expr_list();
            node.paras = new Node[1 + expr_ctx.getChildCount() / 2];
            for (int i = 0; i != 1 + expr_ctx.getChildCount() / 2; ++i) {
                node.paras[i] = visit(expr_ctx.expr(i));
                node.paras[i].father = node;
            }
        }
        /*
         * if (node.id instanceof IdNode) {// global func
         * // String id = ((IdNode) node.id).id;
         * // FuncType func_type = getFuncTypeFromGlb(id);
         * // if (node.paras.length != func_type.paras.length) {
         * // throw_semantic(undef_id, ctx);
         * // }
         * // for (int i = 0; i != node.paras.length; ++i) {
         * // ExprNode expr = (ExprNode) node.paras[i];
         * // Type para_type = func_type.paras[i];
         * // if (!expr.type.equal(para_type)) {
         * // throw_semantic(undef_id, ctx);
         * // }
         * // }
         * // node.type = func_type.return_type.clone();
         * 
         * 
         * } else if (node.id instanceof MemAccNode) {// member method
         * MemAccNode expr = (MemAccNode) node.id;
         * ExprNode object = (ExprNode) expr.object;
         * IdNode member = (IdNode) expr.member;
         * 
         * if (object.type.dim > 0) {// array only has method size()
         * if (node.paras != null || member.id != "size") {
         * throw_semantic(undef_id, ctx);
         * }
         * node.type = new Type(int_type);
         * 
         * } else if (object.type.id == string_type) {// string has built-in methods
         * switch (member.id) {
         * case "length":
         * if (node.paras != null) {
         * throw_semantic(undef_id, ctx);
         * }
         * node.type = new Type(int_type);
         * break;
         * 
         * case "substring":
         * if (node.paras.length != 2) {
         * throw_semantic(undef_id, ctx);
         * }
         * ExprNode p0 = (ExprNode) node.paras[0], p1 = (ExprNode) node.paras[1];
         * Type int_tp = new Type(int_type, 0, false);
         * if (!int_tp.equal(p0.type) || !int_tp.equal(p1.type)) {
         * throw_semantic(undef_id, ctx);
         * }
         * node.type = new Type(string_type);
         * break;
         * 
         * case "parseInt":
         * if (node.paras != null) {
         * throw_semantic(undef_id, ctx);
         * }
         * node.type = new Type(int_type);
         * break;
         * 
         * case "ord":
         * if (node.paras.length != 1) {
         * throw_semantic(undef_id, ctx);
         * }
         * ExprNode p = (ExprNode) node.paras[0];
         * Type int_tp1 = new Type(int_type, 0, false);
         * if (!int_tp1.equal(p.type)) {
         * throw_semantic(undef_id, ctx);
         * }
         * node.type = new Type(int_type);
         * break;
         * 
         * default:
         * throw_semantic(undef_id, ctx);
         * break;
         * }
         * 
         * } else {// only has declared methods
         * // String id = object.id;
         * // String cls = ((ExprNode) member).type.id;
         * // FuncType func_type = getFuncTypeFromCls(cls, id, ctx);
         * 
         * // if (node.paras.length != func_type.paras.length) {
         * // throw_semantic(undef_id, ctx);
         * // }
         * // for (int i = 0; i != node.paras.length; ++i) {
         * // ExprNode para = (ExprNode) node.paras[i];
         * // Type para_type = func_type.paras[i];
         * // if (!para.type.equal(para_type)) {
         * // throw_semantic(undef_id, ctx);
         * // }
         * // }
         * // node.type = func_type.return_type.clone();
         * //
         * 
         * }
         * 
         * } else {
         * throw_semantic(inv_tp, ctx);
         * }
         */

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
    public Node visitMemberAccess(MxParser.MemberAccessContext ctx) {
        MemAccNode node = new MemAccNode();
        node.object = visit(ctx.expr());
        node.member = new IdNode();
        ((IdNode) node.member).id = ctx.Identifier().getText();
        node.object.father = node.member.father = node;

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
    public Node visitShiftOps(MxParser.ShiftOpsContext ctx) {
        BinaryOpNode node = new BinaryOpNode();
        node.oprand = ctx.op.getType() == MxParser.ShiftL ? BinaryOprand.ShiftL : BinaryOprand.ShiftR;
        node.lhs = visit(ctx.getChild(0));
        node.rhs = visit(ctx.getChild(2));
        node.lhs.father = node.rhs.father = node;

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
        IdNode node = new IdNode();
        node.id = ctx.Identifier().getText();

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
    public Node visitParenExpr(MxParser.ParenExprContext ctx) {
        // simple
        return visit(ctx.expr());
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
        FStringNode node = new FStringNode();
        node.literals = new String[ctx.getChildCount() / 2 + 1];
        node.exprs = new Node[ctx.getChildCount() / 2];

        node.literals[0] = ctx.getChild(0).getText().substring(2, ctx.getChild(0).getText().length() - 1);
        for (int i = 1; i != ctx.getChildCount(); ++i) {
            if (i % 2 == 0) {// literals
                node.literals[i / 2] = ctx.getChild(i).getText().substring(1, ctx.getChild(i).getText().length() - 1);
            } else {// exprs
                node.exprs[i / 2] = visit(ctx.getChild(i));
                node.exprs[i / 2].father = node;
            }
        }

        node.type = new Type(string_type);
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
    // @Override
    // public Node visitStmt(MxParser.StmtContext ctx) {
    // return visitChildren(ctx);
    // }
    /**
     * {@inheritDoc}
     *
     * <p>
     * The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.
     * </p>
     */
    @Override
    public Node visitVar_def(MxParser.Var_defContext ctx) {
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
    public Node visitDef_var_stmt(MxParser.Def_var_stmtContext ctx) {
        DefVarNode node = new DefVarNode();
        node.type = visit(ctx.typename());
        node.type.father = node;
        // node.var_init = new DefVarNode.VarInit[(ctx.getChildCount() - 2) / 2 + 1];
        node.ids = new Node[(ctx.getChildCount() - 2) / 2 + 1];
        node.inits = new Node[(ctx.getChildCount() - 2) / 2 + 1];
        // node.var_init = new DefVarNode.VarInit[1];
        // node.var_init[0].id = new IdNode();
        // System.out.println((ctx.getChildCount() - 2) / 2 + 1);

        for (int i = 0; i != (ctx.getChildCount() - 2) / 2 + 1; ++i) {
            // System.out.println(i);
            MxParser.Var_defContext def_ctx = ctx.var_def(i);
            node.ids[i] = new IdNode();
            ((IdNode) node.ids[i]).id = def_ctx.Identifier().getText();
            node.ids[i].father = node;
            IdNode id = (IdNode) node.ids[i];
            id.type = ((TypeNode) node.type).type.clone();
            // ddToVarScope(id.id, id.type, node, ctx);

            if (def_ctx.expr() != null) {
                node.inits[i] = visit(def_ctx.expr());
                node.inits[i].father = node;
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
    public Node visitIf_stmt(MxParser.If_stmtContext ctx) {
        IfStmtNode node = new IfStmtNode();
        node.cond = visit(ctx.cond);
        node.cond.father = node;

        if (ctx.stmt(0) instanceof MxParser.NewStmtScopeContext) {
            node.then_stmt = visit(ctx.stmt(0));
            node.then_stmt.father = node;
        } else {
            BraceStmtNode son_node = new BraceStmtNode();
            node.then_stmt = son_node;
            son_node.father = node;
            son_node.stmts = new Node[1];
            son_node.stmts[0] = visit(ctx.stmt(0));
            son_node.stmts[0].father = son_node;
        }

        if (ctx.Else() != null) {// have else stmt
            if (ctx.stmt(1) instanceof MxParser.NewStmtScopeContext) {
                node.else_stmt = visit(ctx.stmt(1));
                node.else_stmt.father = node;
            } else {
                BraceStmtNode son_node = new BraceStmtNode();
                node.else_stmt = son_node;
                son_node.father = node;
                son_node.stmts = new Node[1];
                son_node.stmts[0] = visit(ctx.stmt(1));
                son_node.stmts[0].father = son_node;
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
    public Node visitWhile_stmt(MxParser.While_stmtContext ctx) {
        WhileStmtNode node = new WhileStmtNode();
        node.cond = visit(ctx.cond);
        node.cond.father = node;

        if (ctx.getChildCount() == 5) {
            node.stmts = new Node[1];
            node.stmts[0] = visit(ctx.stmt(0));
            node.stmts[0].father = node;
        } else {
            node.stmts = new Node[ctx.getChildCount() - 6];
            for (int i = 0; i != ctx.getChildCount() - 6; ++i) {
                node.stmts[i] = visit(ctx.stmt(i));
                node.stmts[i].father = node;
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
    public Node visitFor_stmt(MxParser.For_stmtContext ctx) {
        ForStmtNode node = new ForStmtNode();

        int other_child_count = 4;

        if (ctx.init != null) {
            node.init = visit(ctx.init);
            node.init.father = node;
            ++other_child_count;
        }
        if (ctx.cond != null) {
            node.cond = visit(ctx.cond);
            node.cond.father = node;
            ++other_child_count;
        }
        if (ctx.step != null) {
            node.step = visit(ctx.step);
            node.step.father = node;
            ++other_child_count;
        }

        if (ctx.getChildCount() == other_child_count + 1) {
            node.stmts = new Node[1];
            node.stmts[0] = visit(ctx.stmt(0));
            node.stmts[0].father = node;
        } else {
            node.stmts = new Node[ctx.getChildCount() - other_child_count - 2];
            for (int i = 0; i != ctx.getChildCount() - other_child_count - 2; ++i) {
                node.stmts[i] = visit(ctx.stmt(i));
                node.stmts[i].father = node;
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
    public Node visitReturn_stmt(MxParser.Return_stmtContext ctx) {
        ReturnNode node = new ReturnNode();
        if (ctx.val != null) {
            node.expr = visit(ctx.val);
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
    public Node visitBreak_stmt(MxParser.Break_stmtContext ctx) {
        // simple
        return new BreakNode();
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
        // simple
        return new ContinueNode();
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
        ExprStmtNode node = new ExprStmtNode();
        node.expr = visit(ctx.expr());
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
    public Node visitEmpty_stmt(MxParser.Empty_stmtContext ctx) {
        // simple
        return new EmptyStmtNode();
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
    public Node visitNewStmtScope(MxParser.NewStmtScopeContext ctx) {
        BraceStmtNode node = new BraceStmtNode();
        node.stmts = new Node[ctx.getChildCount() - 2];
        for (int i = 0; i != ctx.getChildCount() - 2; ++i) {
            node.stmts[i] = visit(ctx.stmt(i));
            node.stmts[i].father = node;
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
    public Node visitOtherStmt(MxParser.OtherStmtContext ctx) {
        // unused
        return visit(ctx.getChild(0));
    }
}
