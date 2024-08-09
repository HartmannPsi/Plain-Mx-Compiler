import java.util.Map;
import java.util.Set;
import org.antlr.v4.runtime.ParserRuleContext;
import defNodes.*;
import defNodes.exprNodes.*;
import defNodes.stmtNodes.*;
import util.error.*;
import util.position;

public class SemanticChecker {
    String int_type = "int", bool_type = "bool", string_type = "string", void_type = "void", null_type = "null",
            mul_def = "Multiple Definitions", inv_id = "Invalid Identifier", undef_id = "Undefined Identifier",
            tp_mis = "Type Mismatch", inv_ctrl = "Invalid Control Flow", inv_tp = "Invalid Type",
            miss_ret = "Missing Return Statement", out_of_bd = "Dimension Out Of Bound";
    Set<String> global_class = null;
    Map<String, FuncType> global_func = null;
    Map<String, Type> global_var = null;
    public ProgNode root = null;

    public SemanticChecker(ProgNode root) {
        this.root = root;
        global_class = root.class_ids;
        global_func = root.func_ids;
        global_var = root.vars;
    }

    void throw_semantic(String error, ParserRuleContext ctx) {
        throw new semanticError(error, new position(ctx));
    }

    void throw_syntax(String error, ParserRuleContext ctx) {
        throw new syntaxError(error, new position(ctx));
    }

    void addToFuncs(String id, FuncType type, Node node, ParserRuleContext ctx) {

        if (node.father instanceof ProgNode) {// global func
            if (global_func.containsKey(id)) {
                throw_semantic(mul_def, ctx);
            }
            global_func.put(id, type);
        } else if (node.father instanceof DefClassNode) {// method func
            Map<String, FuncType> methods = ((DefClassNode) node.father).methods;
            if (methods.containsKey(id)) {
                throw_semantic(mul_def, ctx);
            }
            global_func.put(id, type);
        }
    }

    void addToCls(String id, Node node, ParserRuleContext ctx) {

        if (!(node.father instanceof ProgNode)) {
            throw_syntax("Illegal Class Definition", ctx);
        }

        if (global_class.contains(id)) {
            throw_semantic(mul_def, ctx);
        }
        global_class.add(id);
    }

    FuncType getFuncTypeFromGlb(String id) {
        if (global_func.containsKey(id)) {
            return global_func.get(id);
        } else {
            return null;
        }
    }

    boolean existsCls(String id) {
        return global_class.contains(id);
    }

    void addToVarScope(String id, Type type, Node node, ParserRuleContext ctx) {
        while (!(node instanceof ScopeNode)) {
            node = node.father;
        }
        Map<String, Type> scope = ((ScopeNode) node).vars;
        if (scope.containsKey(id)) {
            throw_semantic(mul_def, ctx);
        } else {
            scope.put(id, type);
        }
    }

    boolean checkInVarScope(String id, Node node) {
        while (!(node instanceof ScopeNode)) {
            node = node.father;
        }
        Map<String, Type> scope = ((ScopeNode) node).vars;
        return scope.containsKey(id);
    }

    Type getVarTypeFromScope(String id, Node node) {
        while (node != null) {

            while (!(node instanceof ScopeNode)) {
                node = node.father;
            }
            Map<String, Type> scope = ((ScopeNode) node).vars;
            if (scope.containsKey(id)) {
                return scope.get(id);
            }
            node = node.father;
        }

        return null;
    }

    Type getVarTypeFromCls(String cls, String id, ParserRuleContext ctx) {
        for (int i = 0; i != root.global_stmt.length; ++i) {
            if (root.global_stmt[i] instanceof DefClassNode) {

                DefClassNode node = (DefClassNode) root.global_stmt[i];
                if (((IdNode) node.name).id == cls) {

                    Map<String, Type> scope = node.vars;
                    if (scope.containsKey(id)) {
                        return scope.get(id);

                    } else {
                        throw_semantic(undef_id, ctx);
                        return null;
                    }
                } else {
                    continue;
                }
            }
        }

        throw_semantic(undef_id, ctx);
        return null;
    }

    FuncType getFuncTypeFromCls(String cls, String id, ParserRuleContext ctx) {
        for (int i = 0; i != root.global_stmt.length; ++i) {
            if (root.global_stmt[i] instanceof DefClassNode) {

                DefClassNode node = (DefClassNode) root.global_stmt[i];
                if (((IdNode) node.name).id == cls) {

                    Map<String, FuncType> scope = node.methods;
                    if (scope.containsKey(id)) {
                        return scope.get(id);

                    } else {
                        throw_semantic(undef_id, ctx);
                        return null;
                    }
                } else {
                    continue;
                }
            }
        }

        throw_semantic(undef_id, ctx);
        return null;
    }

    void checkControlFlow(Node node) {
        while (node != null && !(node instanceof WhileStmtNode || node instanceof ForStmtNode)) {
            node = node.father;
        }
        if (node == null) {
            throw_semantic(inv_ctrl, null);
        }
    }

    void checkInClsDef(Node node) {
        ClsConsNode tmp = (ClsConsNode) node;
        while (node != null && !(node instanceof DefClassNode)) {
            node = node.father;
        }
        if (node == null) {
            throw_semantic(inv_ctrl, null);
        }

        if (((IdNode) tmp.id).id != ((IdNode) ((DefClassNode) node).name).id) {
            throw_semantic(inv_ctrl, null);
        }
    }

    void checkInFuncDef(Node node) {
        while (node != null && !(node instanceof DefFuncNode)) {
            node = node.father;
        }
        if (node == null) {
            throw_semantic(inv_ctrl, null);
        }
    }

    void checkReturn(Node node) {
        while (node != null && !(node instanceof DefFuncNode || node instanceof ClsConsNode)) {
            node = node.father;
        }
        if (node == null) {
            throw_semantic(inv_ctrl, null);
        }
    }

    public void check() {
        check(root);
    }

    void check(Node node) {
        if (node instanceof ProgNode) {
            check((ProgNode) node);
        } else if (node instanceof BraceStmtNode) {
            check((BraceStmtNode) node);
        } else if (node instanceof BreakNode) {
            check((BreakNode) node);
        } else if (node instanceof ClsConsNode) {
            check((ClsConsNode) node);
        } else if (node instanceof ContinueNode) {
            check((ContinueNode) node);
        } else if (node instanceof DefClassNode) {
            check((DefClassNode) node);
        } else if (node instanceof DefFuncNode) {
            check((DefFuncNode) node);
        } else if (node instanceof DefVarNode) {
            check((DefVarNode) node);
        } else if (node instanceof EmptyStmtNode) {
            check((EmptyStmtNode) node);
        } else if (node instanceof ExprStmtNode) {
            check((ExprStmtNode) node);
        } else if (node instanceof ForStmtNode) {
            check((ForStmtNode) node);
        } else if (node instanceof IfStmtNode) {
            check((IfStmtNode) node);
        } else if (node instanceof ReturnNode) {
            check((ReturnNode) node);
        } else if (node instanceof WhileStmtNode) {
            check((WhileStmtNode) node);
        } else if (node instanceof ArrayAccessNode) {
            check((ArrayAccessNode) node);
        } else if (node instanceof ArrayNode) {
            check((ArrayNode) node);
        } else if (node instanceof BinaryOpNode) {
            check((BinaryOpNode) node);
        } else if (node instanceof BoolConstNode) {
            check((BoolConstNode) node);
        } else if (node instanceof FStringNode) {
            check((FStringNode) node);
        } else if (node instanceof FuncCallNode) {
            check((FuncCallNode) node);
        } else if (node instanceof IdNode) {
            check((IdNode) node);
        } else if (node instanceof MemAccNode) {
            check((MemAccNode) node);
        } else if (node instanceof NewExprNode) {
            check((NewExprNode) node);
        } else if (node instanceof NullNode) {
            check((NullNode) node);
        } else if (node instanceof NumConstNode) {
            check((NumConstNode) node);
        } else if (node instanceof StringConstNode) {
            check((StringConstNode) node);
        } else if (node instanceof TernaryOpNode) {
            check((TernaryOpNode) node);
        } else if (node instanceof ThisNode) {
            check((ThisNode) node);
        } else if (node instanceof UnaryOpNode) {
            check((UnaryOpNode) node);
        } else if (node instanceof TypeNode) {
            check((TypeNode) node);
        } else if (node == null) {
            return;
        } else {
            throw_syntax("Illegal Node Type", null);
        }
    }

    void collectIds() {
        // TODO:
        /*
         * first: cls declarations
         * second: func declartions
         * third: cls member declarations
         */

        int def_cls_cnt = 0, def_func_cnt = 0;
        for (int i = 0; i != root.global_stmt.length; ++i) {
            if (root.global_stmt[i] instanceof DefClassNode) {
                ++def_cls_cnt;
            } else if (root.global_stmt[i] instanceof DefFuncNode) {
                ++def_func_cnt;
            }
        }

        DefClassNode[] def_cls_stmt = new DefClassNode[def_cls_cnt];
        DefFuncNode[] def_func_stmt = new DefFuncNode[def_func_cnt];
        def_cls_cnt = def_func_cnt = 0;

        for (int i = 0; i != root.global_stmt.length; ++i) {
            if (root.global_stmt[i] instanceof DefClassNode) {
                def_cls_stmt[def_cls_cnt] = (DefClassNode) root.global_stmt[i];
                ++def_cls_cnt;
            } else if (root.global_stmt[i] instanceof DefFuncNode) {
                def_func_stmt[def_func_cnt] = (DefFuncNode) root.global_stmt[i];
                ++def_func_cnt;
            }
        }

        // collect cls decl

        // collect func decl

        // collect cls member decl
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    void check(TypeNode node) {
        if (!node.type.id.equals(int_type) && !node.type.id.equals(bool_type) && !node.type.id.equals(string_type)) {
            if (!existsCls(node.type.id)) {
                throw_semantic(inv_id, null);
            }
        }

        if (node.type.dim < 0) {
            throw_semantic(out_of_bd, null);
        }
    }

    void check(ProgNode node) {
        if (node.father == null) {
            throw_syntax("Illegal Node Structure", null);
        }

        collectIds();
        // TODO:

        for (int i = 0; i != node.global_stmt.length; ++i) {
            check(node.global_stmt[i]);
        }
    }

    void check(BraceStmtNode node) {
        for (int i = 0; i != node.stmts.length; ++i) {
            check(node.stmts[i]);
        }
    }

    void check(BreakNode node) {
        checkControlFlow(node);
    }

    void check(ClsConsNode node) {
        checkInClsDef(node);
        for (int i = 0; i != node.stmt.length; ++i) {
            check(node.stmt[i]);
        }
    }

    void check(ContinueNode node) {
        checkControlFlow(node);
    }

    void check(DefClassNode node) {
        // TODO: collect ids
        boolean has_cons = false;
        check(node.name);
        for (int i = 0; i != node.stmt.length; ++i) {

            if (node.stmt[i] instanceof ClsConsNode) {
                if (has_cons) {
                    throw_semantic(mul_def, null);
                } else {
                    has_cons = true;
                }
            }
            check(node.stmt[i]);
        }
    }

    void check(DefFuncNode node) {
        // TODO: collect ids
        check(node.type);
        check(node.name);

        Type ret_tp = ((TypeNode) node.type).type;
        boolean has_ret_stmt = false;
        String id = ((IdNode) node.name).id;

        Node father = node.father;
        Map<String, FuncType> scope;
        if (father instanceof ProgNode) {
            scope = global_func;

            if (scope.containsKey(((IdNode) node.name).id)) {
                throw_semantic(mul_def, null);
            }

        } else {
            scope = ((DefClassNode) father).methods;

            if (scope.containsKey(((IdNode) node.name).id)) {
                throw_semantic(mul_def, null);
            }
        }

        for (int i = 0; i != node.ids.length; ++i) {
            check(node.tps[i]);
            check(node.ids[i]);

            String _id = ((IdNode) node.ids[i]).id;
            Type tp = ((TypeNode) node.tps[i]).type;
            tp.is_lvalue = true;

            if (node.vars.containsKey(_id)) {
                throw_semantic(mul_def, null);
            } else {
                node.vars.put(_id, tp);
            }
        }

        FuncType func_tp = new FuncType();
        func_tp.return_type = ((TypeNode) node.type).type.clone();
        func_tp.paras = new Type[node.ids.length];
        for (int i = 0; i != node.ids.length; ++i) {
            func_tp.paras[i] = ((TypeNode) node.tps[i]).type.clone();
        }

        scope.put(((IdNode) node.name).id, func_tp);
        for (int i = 0; i != node.stmt.length; ++i) {
            check(node.stmt[i]);
            if (node.stmt[i] instanceof ReturnNode) {
                has_ret_stmt = true;
            }
        }

        if (!has_ret_stmt) {
            if (!ret_tp.equal(void_type) && !id.equals("main")) {
                throw_semantic(miss_ret, null);
            }
        }
    }

    void check(DefVarNode node) {

        check(node.type);

        for (int i = 0; i != node.ids.length; ++i) {
            check(node.ids[i]);

            if (node.inits[i] != null) {
                check(node.inits[i]);
                if (!((TypeNode) node.type).type.equal(((TypeNode) node.inits[i]).type)) {
                    throw_semantic(tp_mis, null);
                }
            }

            Type tp = ((TypeNode) node.type).type.clone();
            tp.is_lvalue = true;

            addToVarScope(((IdNode) node.ids[i]).id, tp, node, null);
        }

    }

    void check(EmptyStmtNode node) {
        return;
    }

    void check(ExprStmtNode node) {
        check(node.expr);
    }

    void check(ForStmtNode node) {
        check(node.init);
        check(node.cond);

        if (!((ExprNode) node.cond).type.equal(bool_type)) {
            throw_semantic(inv_tp, null);
        }

        check(node.step);
        for (int i = 0; i != node.stmts.length; ++i) {
            check(node.stmts[i]);
        }
    }

    void check(IfStmtNode node) {
        check(node.cond);

        if (!((ExprNode) node.cond).type.equal(bool_type)) {
            throw_semantic(inv_tp, null);
        }

        check(node.then_stmt);
        check(node.else_stmt);
    }

    void check(ReturnNode node) {

        Node tmp = node;
        while (tmp != null && !(tmp instanceof DefFuncNode || tmp instanceof ClsConsNode)) {
            tmp = tmp.father;
        }

        if (tmp == null) {
            throw_semantic(inv_ctrl, null);
        }

        check(node.expr);

        if (tmp instanceof ClsConsNode) {
            if (node.expr != null) {
                throw_syntax("Class Constructor Has Return Value", null);
            }
        } else {

            if (node.expr == null) {
                if (!((TypeNode) ((DefFuncNode) tmp).type).type.equal(void_type)
                        && !((IdNode) ((DefFuncNode) tmp).name).id.equals("main")) {
                    throw_semantic(tp_mis, null);
                }

            } else if (((TypeNode) ((DefFuncNode) tmp).type).type.equal(void_type)) {
                throw_syntax("Void Function Has Return Value", null);

            } else if (!((ExprNode) node.expr).type.equal(((TypeNode) ((DefFuncNode) tmp).type).type)) {
                throw_semantic(tp_mis, null);
            }
        }

    }

    void check(WhileStmtNode node) {
        check(node.cond);

        if (!((ExprNode) node.cond).type.equal(bool_type)) {
            throw_semantic(inv_tp, null);
        }

        for (int i = 0; i != node.stmts.length; ++i) {
            check(node.stmts[i]);
        }
    }

    void check(ArrayAccessNode node) {
        check(node.array);
        node.type = ((ExprNode) node.array).type.clone();
        --node.type.dim;
        node.type.is_lvalue = true;
        if (node.type.dim < 0) {
            throw_semantic(out_of_bd, null);
        }

        check(node.serial);
        if (!((ExprNode) node.serial).type.equal(int_type)) {
            throw_semantic(tp_mis, null);
        }
    }

    void check(ArrayNode node) {
        check(node.vals[0]);
        Type tps = ((ExprNode) node.vals[0]).type.clone();
        node.type = tps.clone();
        ++node.type.dim;

        for (int i = 1; i != node.vals.length; ++i) {
            check(node.vals[i]);

            if (!((ExprNode) node.vals[i]).type.equal(tps)) {
                throw_semantic(tp_mis, null);
            }
        }
    }

    void check(BinaryOpNode node) {
        check(node.lhs);
        check(node.rhs);
        ExprNode lhs = (ExprNode) node.lhs, rhs = (ExprNode) node.rhs;

        switch (node.oprand) {
            case Mul:
            case Div:
            case Mod:
            case Sub:
            case ShiftL:
            case ShiftR:
            case BAnd:
            case BOr:
            case BXor:
                if (!lhs.type.equal(new Type(int_type)) || !rhs.type.equal(new Type(int_type))) {
                    throw_semantic(tp_mis, null);
                }
                node.type = new Type(int_type);
                break;

            case Lt:
            case Le:
            case Gt:
            case Ge:
                if (!lhs.type.equal(new Type(int_type)) || !rhs.type.equal(new Type(int_type))) {
                    throw_semantic(tp_mis, null);
                }
                node.type = new Type(bool_type);
                break;

            case Eq:
            case Ne:
                if (lhs.type.equal(new Type(null_type))) {
                    if (rhs.type.equal(int_type) || rhs.type.equal(bool_type)
                            || rhs.type.equal(string_type)) {
                        throw_semantic(tp_mis, null);
                    }

                } else if (!lhs.type.equal(rhs.type)) {
                    throw_semantic(tp_mis, null);
                }

                node.type = new Type(bool_type);
                break;

            case LAnd:
            case LOr:
                if (!lhs.type.equal(new Type(bool_type)) || !rhs.type.equal(new Type(bool_type))) {
                    throw_semantic(tp_mis, null);
                }
                node.type = new Type(bool_type);
                break;

            case Assign:
                if (rhs.type.equal(null_type)) {

                } else if (!lhs.type.equal(rhs.type)) {
                    throw_semantic(tp_mis, null);
                }

                if (!lhs.type.is_lvalue) {
                    throw_semantic(tp_mis, null);
                }

                node.type = lhs.type.clone();
                break;

            case Add:
                if (lhs.type.equal(new Type(int_type))) {
                    if (!rhs.type.equal(new Type(int_type))) {
                        throw_semantic(tp_mis, null);
                    }
                    node.type = new Type(int_type);

                } else if (lhs.type.equal(new Type(string_type))) {
                    if (!rhs.type.equal(new Type(string_type))) {
                        throw_semantic(tp_mis, null);
                    }
                    node.type = new Type(string_type);

                } else {
                    throw_semantic(tp_mis, null);
                }
                break;

            case None:
                break;
        }

    }

    void check(BoolConstNode node) {
        node.type = new Type(bool_type, 0, false);
    }

    boolean checkFStringType(ExprNode node) {
        if (node.type.equal(new Type(string_type))) {
            return true;
        } else if (node.type.equal(new Type(int_type))) {
            return true;
        } else if (node.type.equal(new Type(bool_type))) {
            return true;
        } else {
            return false;
        }
    }

    void check(FStringNode node) {
        for (int i = 0; i != node.exprs.length; ++i) {
            check(node.exprs[i]);
            if (!checkFStringType((ExprNode) node.exprs[i])) {
                throw_syntax("Invalid FString Expression", null);
            }
        }
        node.type = new Type(string_type);
    }

    void check(FuncCallNode node) {
        if (node.id instanceof MemAccNode) {// call member methods
            Type cls_tp = ((ExprNode) ((MemAccNode) node.id).object).type;

            if (cls_tp.dim > 0) {// only has size method

                if (!((IdNode) ((MemAccNode) node.id).member).id.equals("size")) {
                    throw_semantic(undef_id, null);
                }

                if (node.paras != null) {
                    throw_semantic(undef_id, null);
                }

                node.type = new Type(int_type);

            } else if (cls_tp.id.equals(string_type)) {// built-in methods

                Type int_tp = new Type(int_type, 0, false);

                switch (((IdNode) ((MemAccNode) node.id).member).type.id) {
                    case "length":
                        node.type = new Type(int_type);
                        if (node.paras != null) {
                            throw_semantic(undef_id, null);
                        }
                        break;

                    case "substring":
                        node.type = new Type(string_type);
                        if (node.paras.length != 2) {
                            throw_semantic(undef_id, null);
                        }
                        ExprNode p0 = (ExprNode) node.paras[0], p1 = (ExprNode) node.paras[1];
                        if (!int_tp.equal(p0.type) || !int_tp.equal(p1.type)) {
                            throw_semantic(undef_id, null);
                        }
                        break;

                    case "parseInt":
                        node.type = new Type(int_type);
                        if (node.paras != null) {
                            throw_semantic(undef_id, null);
                        }
                        break;

                    case "ord":
                        node.type = new Type(int_type);
                        if (node.paras.length != 1) {
                            throw_semantic(undef_id, null);
                        }
                        ExprNode p = (ExprNode) node.paras[0];
                        if (!int_tp.equal(p.type)) {
                            throw_semantic(undef_id, null);
                        }
                        break;

                    default:
                        throw_semantic(undef_id, null);
                        break;
                }

            } else {// member method
                for (int i = 0; i != root.global_stmt.length; ++i) {

                    if (root.global_stmt[i] instanceof DefClassNode) {
                        DefClassNode def_cls = (DefClassNode) root.global_stmt[i];

                        if (((IdNode) def_cls.name).id.equals(cls_tp.id)) {
                            Map<String, FuncType> scope = def_cls.methods;
                            String id = ((IdNode) ((MemAccNode) node.id).member).id;

                            if (scope.containsKey(id)) {
                                node.type = scope.get(id).return_type.clone();
                            } else {
                                throw_semantic(undef_id, null);
                            }
                        }
                    }
                }

            }

        } else if (node.id instanceof IdNode) {// call global functions

            String id = ((IdNode) node.id).id;

            if (global_func.containsKey(id)) {
                node.type = global_func.get(id).return_type.clone();
            } else {
                throw_semantic(undef_id, null);
            }

        } else {
            throw_semantic(tp_mis, null);
        }
    }

    void check(IdNode node) {
        if (node.father instanceof MemAccNode) {// find in class

            Type cls_tp = ((ExprNode) ((MemAccNode) node.father).object).type;

            if (cls_tp.dim > 0) {// only has size method

                if (!node.id.equals("size")) {
                    throw_semantic(undef_id, null);
                }
                node.type = new Type(int_type);

            } else if (cls_tp.id.equals(string_type)) {// built-in methods

                switch (node.id) {
                    case "length":
                        node.type = new Type(int_type);
                        break;

                    case "substring":
                        node.type = new Type(string_type);
                        break;

                    case "parseInt":
                        node.type = new Type(int_type);
                        break;

                    case "ord":
                        node.type = new Type(int_type);
                        break;

                    default:
                        throw_semantic(undef_id, null);
                        break;
                }

            } else if (node.father.father instanceof FuncCallNode) {// member method
                for (int i = 0; i != root.global_stmt.length; ++i) {

                    if (root.global_stmt[i] instanceof DefClassNode) {
                        DefClassNode def_cls = (DefClassNode) root.global_stmt[i];

                        if (((IdNode) def_cls.name).id.equals(cls_tp.id)) {
                            Map<String, FuncType> scope = def_cls.methods;
                            if (scope.containsKey(node.id)) {
                                node.type = scope.get(node.id).return_type.clone();
                            } else {
                                throw_semantic(undef_id, null);
                            }
                        }
                    }
                }

            } else {// member variable
                for (int i = 0; i != root.global_stmt.length; ++i) {

                    if (root.global_stmt[i] instanceof DefClassNode) {
                        DefClassNode def_cls = (DefClassNode) root.global_stmt[i];

                        if (((IdNode) def_cls.name).id == cls_tp.id) {
                            Map<String, Type> scope = def_cls.vars;
                            if (scope.containsKey(node.id)) {
                                node.type = scope.get(node.id).clone();
                            } else {
                                throw_semantic(undef_id, null);
                            }
                        }
                    }
                }
            }

        } else {// find in scope
            if (node.father instanceof FuncCallNode) {// function
                if (global_func.containsKey(node.id)) {
                    node.type = global_func.get(node.id).return_type.clone();
                } else {
                    throw_semantic(undef_id, null);
                }

            } else {// variable
                if (global_var.containsKey(node.id)) {
                    node.type = global_var.get(node.id).clone();
                } else {
                    throw_semantic(undef_id, null);
                }
            }

        }
    }

    void check(MemAccNode node) {
        check(node.object);
        check(node.member);
        node.type = ((ExprNode) node.member).type.clone();
        node.type.is_lvalue = true;
    }

    void check(NewExprNode node) {
        check(node.create_type);
        node.type = ((TypeNode) node.create_type).type.clone();

        if (node.lengths != null) {
            if (node.init_array != null) {
                throw_syntax("Invalid New Operation", null);
            }
            for (int i = 0; i != node.lengths.length; ++i) {
                check(node.lengths[i]);
                if (!((ExprNode) node.lengths[i]).type.equal(int_type)) {
                    throw_semantic(tp_mis, null);
                }
            }

        } else {
            if (node.init_array != null) {
                check(node.init_array);
                if (!((ExprNode) node.init_array).type.equal(node.type)) {
                    throw_semantic(tp_mis, null);
                }
            }
        }
    }

    void check(NullNode node) {
        node.type = new Type(null_type, 0, false);
    }

    void check(NumConstNode node) {
        node.type = new Type(int_type, 0, false);
    }

    void check(StringConstNode node) {
        node.type = new Type(string_type, 0, false);
    }

    void check(TernaryOpNode node) {
        check(node.cond);
        if (!((ExprNode) (node.cond)).type.equal(new Type(bool_type))) {
            throw_semantic(inv_tp, null);
        }

        check(node.lhs);
        check(node.rhs);
        if (!((ExprNode) (node.lhs)).type.equal(((ExprNode) (node.rhs)).type)) {
            throw_semantic(tp_mis, null);
        }

        node.type = ((ExprNode) (node.lhs)).type.clone();
    }

    void check(ThisNode node) {
        Node tmp = node;
        while (tmp != null && !(tmp instanceof DefClassNode)) {
            tmp = tmp.father;
        }
        if (tmp == null) {
            throw_semantic(inv_ctrl, null);
        }

        node.type = new Type(((IdNode) ((DefClassNode) tmp).name).id);
    }

    void check(UnaryOpNode node) {
        check(node.expr);

        switch (node.oprand) {
            case SAddL:
            case SAddR:
            case SSubL:
            case SSubR:
            case BNot:
            case Plus:
            case Minus:
                if (!((ExprNode) node.expr).type.equal(new Type(int_type))) {
                    throw_semantic(tp_mis, null);
                }
                node.type = new Type(int_type);
                break;

            case LNot:
                if (!((ExprNode) node.expr).type.equal(new Type(bool_type))) {
                    throw_semantic(tp_mis, null);
                }
                node.type = new Type(bool_type);
                break;

            case None:
                break;
        }
    }
}
