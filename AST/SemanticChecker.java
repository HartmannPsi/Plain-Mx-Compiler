import java.util.Map;
import java.util.Set;
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

    void throw_semantic(String error, position pos) {
        if (pos != null)
            throw new semanticError(error, pos);
        else
            throw new semanticError(error, new position(114, 514));
    }

    void throw_syntax(String error, position pos) {
        if (pos != null)
            throw new syntaxError(error, pos);
        else
            throw new syntaxError(error, new position(114, 514));
    }

    void addToFuncs(String id, FuncType type, Node node) {

        if (node.father instanceof ProgNode) {// global func
            if (global_func.containsKey(id)) {
                throw_semantic(mul_def, node.pos);
            }
            if (global_var.containsKey(id)) {
                throw_semantic(mul_def, node.pos);
            }
            if (global_class.contains(id)) {
                throw_semantic(mul_def, node.pos);
            }
            global_func.put(id, type);
        } else if (node.father instanceof DefClassNode) {// method func
            Map<String, FuncType> methods = ((DefClassNode) node.father).methods;
            if (methods.containsKey(id)) {
                throw_semantic(mul_def, node.pos);
            }
            if (((DefClassNode) node.father).vars.containsKey(id)) {
                throw_semantic(mul_def, node.pos);
            }
            methods.put(id, type);
        }
    }

    void addToCls(String id, Node node) {

        if (!(node.father instanceof ProgNode)) {
            throw_syntax("Illegal Class Definition", node.pos);
        }

        if (global_class.contains(id)) {
            throw_semantic(mul_def, node.pos);
        }
        if (global_func.containsKey(id)) {
            throw_semantic(mul_def, node.pos);
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

    void addToVarScope(String id, Type type, Node node) {
        position pos = node.pos;
        while (!(node instanceof ScopeNode)) {
            node = node.father;
        }
        Map<String, Type> scope = ((ScopeNode) node).vars;

        if (node instanceof ProgNode) {
            if (global_func.containsKey(id)) {
                throw_semantic(mul_def, pos);
            }
        } else if (node instanceof DefClassNode) {
            if (((DefClassNode) node).methods.containsKey(id)) {
                throw_semantic(mul_def, pos);
            }
        }

        if (scope.containsKey(id)) {
            throw_semantic(mul_def, pos);
        } else {
            scope.put(id, type);
        }
    }

    boolean checkInVarScopeRecursive(String id, Node node) {
        while (node != null) {
            while (node != null && !(node instanceof ScopeNode)) {
                node = node.father;
            }

            if (node == null) {
                throw_syntax("WTF?", null);
                return false;
            }

            Map<String, Type> scope = ((ScopeNode) node).vars;
            if (scope.containsKey(id)) {
                return true;
            }
            node = node.father;
        }

        return false;
    }

    Type getVarTypeFromScope(String id, Node node) {
        while (node != null) {

            while (node != null && !(node instanceof ScopeNode)) {
                node = node.father;
            }

            if (node == null) {
                throw_syntax("WTF?", null);
                return null;
            }

            Map<String, Type> scope = ((ScopeNode) node).vars;
            if (scope.containsKey(id)) {
                return scope.get(id);
            }
            node = node.father;
        }

        return null;
    }

    Type getVarTypeFromCls(String cls, String id, Node _node) {
        for (int i = 0; i != root.global_stmt.length; ++i) {
            if (root.global_stmt[i] instanceof DefClassNode) {

                DefClassNode node = (DefClassNode) root.global_stmt[i];
                if (((IdNode) node.name).id.equals(cls)) {

                    Map<String, Type> scope = node.vars;
                    if (scope.containsKey(id)) {
                        return scope.get(id);

                    } else {
                        // System.out.println(0);
                        throw_semantic(undef_id, _node.pos);
                        return null;
                    }
                } else {
                    continue;
                }
            }
        }

        // System.out.println(1);
        throw_semantic(undef_id, _node.pos);
        return null;
    }

    FuncType getFuncTypeFromCls(String cls, String id, Node _node) {
        for (int i = 0; i != root.global_stmt.length; ++i) {
            if (root.global_stmt[i] instanceof DefClassNode) {

                DefClassNode node = (DefClassNode) root.global_stmt[i];
                if (((IdNode) node.name).id.equals(cls)) {

                    Map<String, FuncType> scope = node.methods;
                    if (scope.containsKey(id)) {
                        return scope.get(id);

                    } else {
                        // System.out.println(2);
                        throw_semantic(undef_id, _node.pos);
                        return null;
                    }
                } else {
                    continue;
                }
            }
        }

        // System.out.println(3);
        throw_semantic(undef_id, _node.pos);
        return null;
    }

    void checkControlFlow(Node node) {
        position pos = node.pos;
        while (node != null && !(node instanceof WhileStmtNode || node instanceof ForStmtNode)) {
            node = node.father;
        }
        if (node == null) {
            // System.out.print(1);
            throw_semantic(inv_ctrl, pos);
        }
    }

    void checkInClsDef(Node node) {
        ClsConsNode tmp = (ClsConsNode) node;
        while (node != null && !(node instanceof DefClassNode)) {
            node = node.father;
        }
        if (node == null) {
            // System.out.print(2);
            throw_semantic(inv_ctrl, tmp.pos);
        }

        if (!((IdNode) tmp.id).id.equals(((IdNode) ((DefClassNode) node).name).id)) {
            // System.out.print(3);
            throw_semantic(inv_ctrl, tmp.pos);
        }
    }

    void checkInFuncDef(Node node) {
        position pos = node.pos;
        while (node != null && !(node instanceof DefFuncNode)) {
            node = node.father;
        }
        if (node == null) {
            // System.out.print(4);
            throw_semantic(inv_ctrl, pos);
        }
    }

    // void checkReturn(Node node) {
    // position pos = node.pos;
    // while (node != null && !(node instanceof DefFuncNode || node instanceof
    // ClsConsNode)) {
    // node = node.father;
    // }
    // if (node == null) {
    // throw_semantic(inv_ctrl, pos);
    // }
    // }

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
            throw_syntax("Illegal Node Type", node.pos);
        }
    }

    void collectIds() {
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

        for (int i = 0; i != def_cls_cnt; ++i) {
            DefClassNode node = def_cls_stmt[i];
            // check(node.name);
            addToCls(((IdNode) node.name).id, node);
        }

        // collect func decl

        for (int i = 0; i != def_func_cnt; ++i) {
            DefFuncNode node = def_func_stmt[i];
            FuncType func_tp = new FuncType();
            check(node.type);
            // check(node.name);
            func_tp.return_type = ((TypeNode) node.type).type.clone();

            if (node.tps != null) {
                func_tp.paras = new Type[node.tps.length];
                for (int j = 0; j != node.tps.length; ++j) {
                    check(node.tps[j]);
                    func_tp.paras[j] = ((TypeNode) node.tps[j]).type.clone();

                }
            }
            addToFuncs(((IdNode) node.name).id, func_tp, node);
        }

        // collect cls member decl

        for (int i = 0; i != def_cls_cnt; ++i) {
            DefClassNode node = def_cls_stmt[i];

            for (int j = 0; j != node.stmt.length; ++j) {
                if (node.stmt[j] instanceof DefVarNode) {

                    DefVarNode var_node = (DefVarNode) node.stmt[j];
                    check(var_node);

                    // for (int k = 0; k != var_node.ids.length; ++k) {
                    // Type tp = ((TypeNode) var_node.type).type.clone();
                    // String id = ((IdNode) var_node.ids[k]).id;
                    // addToVarScope(id, tp, var_node);
                    // }

                } else if (node.stmt[j] instanceof DefFuncNode) {

                    DefFuncNode func_node = (DefFuncNode) node.stmt[j];
                    FuncType func_tp = new FuncType();
                    check(func_node.type);
                    // check(func_node.name);
                    func_tp.return_type = ((TypeNode) func_node.type).type.clone();

                    if (func_node.tps != null) {
                        func_tp.paras = new Type[func_node.tps.length];
                        for (int k = 0; k != func_node.tps.length; ++k) {
                            check(func_node.tps[k]);
                            func_tp.paras[k] = ((TypeNode) func_node.tps[k]).type.clone();
                        }
                    }
                    addToFuncs(((IdNode) func_node.name).id, func_tp, func_node);
                }
            }
        }
    }

    void checkMain() {
        if (!global_func.containsKey("main")) {
            throw_syntax("Missing Function Main", root.pos);
        }

        FuncType main_tp = global_func.get("main");
        if (!main_tp.return_type.equal(int_type) || main_tp.paras != null) {
            throw_syntax("Invalid Function Main Definition", root.pos);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////

    void check(TypeNode node) {
        if (!node.type.id.equals(int_type) && !node.type.id.equals(bool_type) && !node.type.id.equals(string_type)
                && !node.type.id.equals(void_type) && !node.type.id.equals(null_type)) {
            if (!existsCls(node.type.id)) {
                throw_semantic(inv_id, node.pos);
            }
        }

        if (node.type.dim < 0) {
            throw_semantic(out_of_bd, node.pos);
        }
    }

    void check(ProgNode node) {
        if (node.father != null) {
            throw_syntax("Illegal Node Structure", node.pos);
        }

        collectIds();

        for (int i = 0; i != node.global_stmt.length; ++i) {
            check(node.global_stmt[i]);
        }

        checkMain();
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
        boolean has_cons = false;
        // check(node.name);
        for (int i = 0; i != node.stmt.length; ++i) {

            if (node.stmt[i] instanceof ClsConsNode) {
                if (has_cons) {
                    throw_semantic(mul_def, node.stmt[i].pos);
                } else {
                    has_cons = true;
                }
                check(node.stmt[i]);

            } else if (node.stmt[i] instanceof DefFuncNode) {
                check(node.stmt[i]);
            }
        }
    }

    void check(DefFuncNode node) {
        // check(node.type);
        // check(node.name);

        Type ret_tp = ((TypeNode) node.type).type;
        // boolean has_ret_stmt = false;
        String id = ((IdNode) node.name).id;

        // Node father = node.father;
        // Map<String, FuncType> scope;

        // if (father instanceof ProgNode) {
        // scope = global_func;

        // // if (scope.containsKey(((IdNode) node.name).id)) {
        // // throw_semantic(mul_def, null);
        // // }

        // } else {
        // scope = ((DefClassNode) father).methods;

        // // if (scope.containsKey(((IdNode) node.name).id)) {
        // // throw_semantic(mul_def, null);
        // // }
        // }

        if (node.ids != null) {
            for (int i = 0; i != node.ids.length; ++i) {
                // check(node.tps[i]);
                // check(node.ids[i]);

                String _id = ((IdNode) node.ids[i]).id;
                Type tp = ((TypeNode) node.tps[i]).type;
                tp.is_lvalue = true;

                if (node.vars.containsKey(_id)) {
                    System.out.println(2);
                    throw_semantic(mul_def, node.pos);
                } else {
                    node.vars.put(_id, tp);
                }
            }
        }

        // FuncType func_tp = new FuncType();
        // func_tp.return_type = ((TypeNode) node.type).type.clone();
        // func_tp.paras = new Type[node.ids.length];
        // for (int i = 0; i != node.ids.length; ++i) {
        // func_tp.paras[i] = ((TypeNode) node.tps[i]).type.clone();
        // }
        // scope.put(((IdNode) node.name).id, func_tp);

        for (int i = 0; i != node.stmt.length; ++i) {
            check(node.stmt[i]);
            // if (node.stmt[i] instanceof ReturnNode) {
            // has_ret_stmt = true;
            // }
        }

        if (!node.has_ret_stmt) {
            if (!ret_tp.equal(void_type) && !id.equals("main")) {
                throw_semantic(miss_ret, node.pos);
            }
        }
    }

    void check(DefVarNode node) {

        check(node.type);

        for (int i = 0; i != node.ids.length; ++i) {
            // check(node.ids[i]);

            if (node.inits[i] != null) {
                check(node.inits[i]);
                if (!((TypeNode) node.type).type.equal(bool_type) && !((TypeNode) node.type).type.equal(int_type)
                        && !((TypeNode) node.type).type.equal(string_type)) {

                    if (!((ExprNode) node.inits[i]).type.equal(null_type)
                            && !((TypeNode) node.type).type.equal(((ExprNode) node.inits[i]).type)) {
                        throw_semantic(tp_mis, node.inits[i].pos);
                    }

                } else if (!((TypeNode) node.type).type.equal(((ExprNode) node.inits[i]).type)) {

                    throw_semantic(tp_mis, node.inits[i].pos);
                }
            }

            Type tp = ((TypeNode) node.type).type.clone();
            tp.is_lvalue = true;

            addToVarScope(((IdNode) node.ids[i]).id, tp, node);

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

        if (node.cond != null) {
            if (!((ExprNode) node.cond).type.equal(bool_type)) {
                throw_semantic(inv_tp, node.pos);
            }
        }

        check(node.step);
        for (int i = 0; i != node.stmts.length; ++i) {
            check(node.stmts[i]);
        }
    }

    void check(IfStmtNode node) {
        check(node.cond);

        if (!((ExprNode) node.cond).type.equal(bool_type)) {
            throw_semantic(inv_tp, node.pos);
        }

        check(node.then_stmt);
        check(node.else_stmt);
    }

    void check(ReturnNode node) {

        // node.printToString();

        Node tmp = node;
        while (tmp != null && !(tmp instanceof DefFuncNode || tmp instanceof ClsConsNode)) {
            tmp = tmp.father;
        }

        if (tmp == null) {
            // System.out.println("???");
            // System.out.print(5);
            throw_semantic(inv_ctrl, node.pos);
        }

        check(node.expr);

        if (tmp instanceof DefFuncNode) {
            ((DefFuncNode) tmp).has_ret_stmt = true;
        }

        if (tmp instanceof ClsConsNode) {
            if (node.expr != null) {
                throw_syntax("Class Constructor Has Return Value", node.pos);
            }
        } else {

            if (node.expr == null) {
                if (!((TypeNode) ((DefFuncNode) tmp).type).type.equal(void_type)
                /* && !((IdNode) ((DefFuncNode) tmp).name).id.equals("main") */) {
                    throw_semantic(tp_mis, node.pos);
                }

            } else if (((TypeNode) ((DefFuncNode) tmp).type).type.equal(void_type)) {
                throw_semantic(tp_mis, node.pos);

            } else if (!((ExprNode) node.expr).type.equal(((TypeNode) ((DefFuncNode) tmp).type).type)) {

                if (((ExprNode) node.expr).type.equal(null_type)) {
                    Type tp = ((TypeNode) ((DefFuncNode) tmp).type).type;

                    if (tp.equal(bool_type) || tp.equal(int_type) || tp.equal(string_type)) {
                        throw_semantic(tp_mis, node.pos);
                    } else {
                        return;
                    }
                }

                throw_semantic(tp_mis, node.pos);
            }
        }

    }

    void check(WhileStmtNode node) {
        check(node.cond);

        if (!((ExprNode) node.cond).type.equal(bool_type)) {
            throw_semantic(inv_tp, node.pos);
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
            throw_semantic(out_of_bd, node.pos);
        }

        check(node.serial);
        if (!((ExprNode) node.serial).type.equal(int_type)) {
            throw_semantic(tp_mis, node.pos);
        }
    }

    void check(ArrayNode node) {

        if (node.vals != null) {
            Type tps;
            int k = 0;
            do {
                check(node.vals[k]);
                tps = ((ExprNode) node.vals[k]).type;
                if (!tps.equal_weak(null_type) || k == node.vals.length - 1) {
                    break;
                }
                ++k;
            } while (!tps.equal_weak(null_type) && k != node.vals.length);

            node.type = tps.clone();
            ++node.type.dim;
            node.type.is_lvalue = false;

            for (int i = k + 1; i != node.vals.length; ++i) {
                check(node.vals[i]);

                if (!((ExprNode) node.vals[i]).type.equal(tps)
                        && !((ExprNode) node.vals[i]).type.equal_weak(null_type)) {
                    // System.out.println(((ExprNode) node.vals[i]).type.toString());
                    // System.out.println(tps.toString());
                    throw_semantic(tp_mis, node.pos);
                }
            }
        } else {
            node.type = new Type(null_type);
            node.type.is_lvalue = false;
            node.type.dim = 1;
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
                    throw_semantic(tp_mis, node.pos);
                }
                node.type = new Type(int_type);
                break;

            case Lt:
            case Le:
            case Gt:
            case Ge:
                if (lhs.type.equal(int_type)) {
                    if (!rhs.type.equal(int_type)) {
                        throw_semantic(tp_mis, node.pos);
                    }

                } else if (lhs.type.equal(string_type)) {
                    if (!rhs.type.equal(string_type)) {
                        throw_semantic(tp_mis, node.pos);
                    }

                } else {
                    throw_semantic(tp_mis, node.pos);
                }

                node.type = new Type(bool_type);
                break;

            case Eq:
            case Ne:
                if (lhs.type.equal(new Type(null_type))) {
                    if (rhs.type.equal(int_type) || rhs.type.equal(bool_type)
                            || rhs.type.equal(string_type)) {
                        throw_semantic(tp_mis, node.pos);
                    }

                } else if (rhs.type.equal(null_type)) {
                    if (lhs.type.equal(int_type) || lhs.type.equal(bool_type)
                            || lhs.type.equal(string_type)) {
                        throw_semantic(tp_mis, node.pos);
                    }

                } else if (!lhs.type.equal(rhs.type)) {
                    throw_semantic(tp_mis, node.pos);
                }

                node.type = new Type(bool_type);
                break;

            case LAnd:
            case LOr:
                if (!lhs.type.equal(new Type(bool_type)) || !rhs.type.equal(new Type(bool_type))) {
                    throw_semantic(tp_mis, node.pos);
                }
                node.type = new Type(bool_type);
                break;

            case Assign:
                if (rhs.type.equal(null_type)) {

                    if (lhs.type.equal(bool_type) || lhs.type.equal(int_type) || lhs.type.equal(string_type)) {
                        throw_semantic(tp_mis, node.pos);
                    }

                } else if (!lhs.type.equal(rhs.type)) {
                    throw_semantic(tp_mis, node.pos);
                }

                if (!lhs.type.is_lvalue) {
                    throw_semantic(tp_mis, node.pos);
                }

                node.type = lhs.type.clone();
                break;

            case Add:
                if (lhs.type.equal(new Type(int_type))) {
                    if (!rhs.type.equal(new Type(int_type))) {
                        throw_semantic(tp_mis, node.pos);
                    }
                    node.type = new Type(int_type);

                } else if (lhs.type.equal(new Type(string_type))) {
                    if (!rhs.type.equal(new Type(string_type))) {
                        throw_semantic(tp_mis, node.pos);
                    }
                    node.type = new Type(string_type);

                } else {
                    throw_semantic(inv_tp, node.pos);
                }
                break;

            case None:
                break;
        }
        node.type.is_lvalue = false;

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
                throw_semantic(inv_tp, node.pos);
            }
        }
        node.type = new Type(string_type);
        node.type.is_lvalue = false;

    }

    void check(FuncCallNode node) {

        check(node.id);
        if (node.paras != null) {
            for (int i = 0; i != node.paras.length; ++i) {
                check(node.paras[i]);
            }
        }

        if (node.id instanceof MemAccNode) {// call member methods
            Type cls_tp = ((ExprNode) ((MemAccNode) node.id).object).type;

            if (cls_tp.dim > 0) {// only has size method

                if (!((IdNode) ((MemAccNode) node.id).member).id.equals("size")) {
                    // System.out.println(4);
                    throw_semantic(undef_id, node.pos);
                }

                if (node.paras != null) {
                    // System.out.println(5);
                    throw_semantic(undef_id, node.pos);
                }

                node.type = new Type(int_type);

            } else if (cls_tp.id.equals(string_type)) {// built-in methods

                Type int_tp = new Type(int_type, 0, false);

                switch (((IdNode) ((MemAccNode) node.id).member).id) {
                    case "length":
                        node.type = new Type(int_type);
                        if (node.paras != null) {
                            // System.out.println(6);
                            throw_semantic(undef_id, node.pos);
                        }
                        break;

                    case "substring":
                        node.type = new Type(string_type);
                        if (node.paras.length != 2) {
                            // System.out.println(7);
                            throw_semantic(undef_id, node.pos);
                        }
                        ExprNode p0 = (ExprNode) node.paras[0], p1 = (ExprNode) node.paras[1];
                        if (!int_tp.equal(p0.type) || !int_tp.equal(p1.type)) {
                            // System.out.println(8);
                            throw_semantic(undef_id, node.pos);
                        }
                        break;

                    case "parseInt":
                        node.type = new Type(int_type);
                        if (node.paras != null) {
                            // System.out.println(9);
                            throw_semantic(undef_id, node.pos);
                        }
                        break;

                    case "ord":
                        node.type = new Type(int_type);
                        if (node.paras.length != 1) {
                            // System.out.println(10);
                            throw_semantic(undef_id, node.pos);
                        }
                        ExprNode p = (ExprNode) node.paras[0];
                        if (!int_tp.equal(p.type)) {
                            // System.out.println(11);
                            throw_semantic(undef_id, node.pos);
                        }
                        break;

                    default:
                        System.out.println(((IdNode) ((MemAccNode) node.id).member).id);
                        throw_semantic(undef_id, node.pos);
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
                                FuncType func_tp = scope.get(id);
                                node.type = func_tp.return_type.clone();

                                if (node.paras == null) {
                                    if (func_tp.paras != null) {
                                        throw_semantic(undef_id, node.pos);
                                    }

                                } else if (func_tp.paras == null) {
                                    throw_semantic(undef_id, node.pos);

                                } else if (node.paras.length != func_tp.paras.length) {
                                    throw_semantic(undef_id, node.pos);

                                } else {
                                    for (int j = 0; j != node.paras.length; ++j) {
                                        if (!((ExprNode) node.paras[j]).type.equal(func_tp.paras[j])) {

                                            if (((ExprNode) node.paras[j]).type.equal(null_type)) {

                                                if (func_tp.paras[j].equal(int_type)
                                                        || func_tp.paras[j].equal(bool_type)
                                                        || func_tp.paras[j].equal(string_type)) {
                                                    throw_semantic(undef_id, node.pos);
                                                }

                                            } else {
                                                throw_semantic(undef_id, node.pos);
                                            }
                                        }
                                    }
                                }

                            } else {
                                // System.out.println(13);
                                throw_semantic(undef_id, node.pos);
                            }
                        }
                    }
                }

            }

        } else if (node.id instanceof IdNode) {// call scope functions

            Node tmp = node;
            while (tmp != null && !(tmp instanceof ProgNode) && !(tmp instanceof DefClassNode)) {
                tmp = tmp.father;
            }
            String id = ((IdNode) node.id).id;

            if (tmp == null) {

                throw_syntax("???", node.pos);

            } else if (tmp instanceof ProgNode) {

                if (global_func.containsKey(id)) {
                    FuncType func_tp = global_func.get(id);
                    node.type = func_tp.return_type.clone();

                    if (node.paras == null) {
                        if (func_tp.paras != null) {
                            throw_semantic(undef_id, node.pos);
                        }

                    } else if (func_tp.paras == null) {
                        throw_semantic(undef_id, node.pos);

                    } else if (node.paras.length != func_tp.paras.length) {
                        throw_semantic(undef_id, node.pos);

                    } else {
                        for (int j = 0; j != node.paras.length; ++j) {
                            if (!((ExprNode) node.paras[j]).type.equal(func_tp.paras[j])) {

                                if (((ExprNode) node.paras[j]).type.equal(null_type)) {

                                    if (func_tp.paras[j].equal(int_type) || func_tp.paras[j].equal(bool_type)
                                            || func_tp.paras[j].equal(string_type)) {
                                        throw_semantic(undef_id, node.pos);
                                    }
                                } else {
                                    throw_semantic(undef_id, node.pos);
                                }

                            }
                        }
                    }

                } else {
                    // System.out.println(14);
                    throw_semantic(undef_id, node.pos);
                }

            } else {// defcls

                Map<String, FuncType> scope = ((DefClassNode) tmp).methods;

                if (scope.containsKey(id)) {

                    FuncType func_tp = scope.get(id);
                    node.type = func_tp.return_type.clone();

                    if (node.paras == null) {
                        if (func_tp.paras != null) {
                            throw_semantic(undef_id, node.pos);
                        }

                    } else if (func_tp.paras == null) {
                        throw_semantic(undef_id, node.pos);

                    } else if (node.paras.length != func_tp.paras.length) {
                        throw_semantic(undef_id, node.pos);

                    } else {
                        for (int j = 0; j != node.paras.length; ++j) {
                            if (!((ExprNode) node.paras[j]).type.equal(func_tp.paras[j])) {

                                if (((ExprNode) node.paras[j]).type.equal(null_type)) {

                                    if (func_tp.paras[j].equal(int_type) || func_tp.paras[j].equal(bool_type)
                                            || func_tp.paras[j].equal(string_type)) {
                                        throw_semantic(undef_id, node.pos);
                                    }

                                } else {

                                    throw_semantic(undef_id, node.pos);
                                }
                            }
                        }
                    }

                } else {

                    if (global_func.containsKey(id)) {
                        FuncType func_tp = global_func.get(id);
                        node.type = func_tp.return_type.clone();

                        if (node.paras == null) {
                            if (func_tp.paras != null) {
                                throw_semantic(undef_id, node.pos);
                            }

                        } else if (func_tp.paras == null) {
                            throw_semantic(undef_id, node.pos);

                        } else if (node.paras.length != func_tp.paras.length) {
                            throw_semantic(undef_id, node.pos);

                        } else {
                            for (int j = 0; j != node.paras.length; ++j) {
                                if (!((ExprNode) node.paras[j]).type.equal(func_tp.paras[j])) {

                                    if (((ExprNode) node.paras[j]).type.equal(null_type)) {

                                        if (func_tp.paras[j].equal(int_type) || func_tp.paras[j].equal(bool_type)
                                                || func_tp.paras[j].equal(string_type)) {
                                            throw_semantic(undef_id, node.pos);
                                        }

                                    } else {

                                        throw_semantic(undef_id, node.pos);
                                    }
                                }
                            }
                        }

                    } else {
                        // System.out.println(14);
                        throw_semantic(undef_id, node.pos);
                    }
                }

            }

        } else {
            throw_semantic(tp_mis, node.pos);
        }
        node.type.is_lvalue = false;

    }

    void check(IdNode node) {
        if (node.father instanceof MemAccNode && ((MemAccNode) node.father).member == node) {// find in class

            Type cls_tp = ((ExprNode) ((MemAccNode) node.father).object).type;

            if (cls_tp.dim > 0) {// only has size method

                if (!node.id.equals("size")) {
                    // System.out.println(15);
                    throw_semantic(undef_id, node.pos);
                }
                node.type = new Type(int_type);

                if (!(node.father.father instanceof FuncCallNode
                        && ((FuncCallNode) node.father.father).id == node.father)) {
                    throw_semantic(undef_id, node.pos);
                }

                node.type.is_lvalue = false;

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
                        // System.out.println(16);
                        throw_semantic(undef_id, node.pos);
                        break;
                }
                node.type.is_lvalue = false;
                if (!(node.father.father instanceof FuncCallNode
                        && ((FuncCallNode) node.father.father).id == node.father)) {
                    throw_semantic(undef_id, node.pos);
                }

            } else if (node.father.father instanceof FuncCallNode
                    && ((FuncCallNode) node.father.father).id == node.father) {// member method
                for (int i = 0; i != root.global_stmt.length; ++i) {

                    if (root.global_stmt[i] instanceof DefClassNode) {
                        DefClassNode def_cls = (DefClassNode) root.global_stmt[i];

                        if (((IdNode) def_cls.name).id.equals(cls_tp.id)) {

                            Map<String, FuncType> scope = def_cls.methods;
                            if (scope.containsKey(node.id)) {
                                node.type = scope.get(node.id).return_type.clone();
                            } else {
                                // System.out.println(17);
                                throw_semantic(undef_id, node.pos);
                            }
                        }
                    }
                }
                node.type.is_lvalue = false;

            } else {// member variable
                for (int i = 0; i != root.global_stmt.length; ++i) {

                    if (root.global_stmt[i] instanceof DefClassNode) {
                        DefClassNode def_cls = (DefClassNode) root.global_stmt[i];

                        if (((IdNode) def_cls.name).id.equals(cls_tp.id)) {
                            Map<String, Type> scope = def_cls.vars;
                            if (scope.containsKey(node.id)) {
                                node.type = scope.get(node.id).clone();
                            } else {
                                // System.out.println(18);
                                throw_semantic(undef_id, node.pos);
                            }
                        }
                    }
                }
                node.type.is_lvalue = true;
            }

        } else {// find in scope
            if (node.father instanceof FuncCallNode && ((FuncCallNode) node.father).id == node) {// function

                Node tmp = node;
                while (tmp != null && !(tmp instanceof ProgNode) && !(tmp instanceof DefClassNode)) {
                    tmp = tmp.father;
                }

                if (tmp == null) {

                    throw_syntax("???", node.pos);

                } else if (tmp instanceof ProgNode) {

                    if (global_func.containsKey(node.id)) {
                        node.type = global_func.get(node.id).return_type.clone();
                    } else {
                        throw_semantic(undef_id, node.pos);
                    }

                } else {// defcls

                    DefClassNode def_cls = (DefClassNode) tmp;
                    if (def_cls.methods.containsKey(node.id)) {
                        node.type = def_cls.methods.get(node.id).return_type.clone();
                    } else {
                        if (global_func.containsKey(node.id)) {
                            node.type = global_func.get(node.id).return_type.clone();
                        } else {
                            throw_semantic(undef_id, node.pos);
                        }
                    }
                }

                node.type.is_lvalue = false;

            } else {// variable
                if (checkInVarScopeRecursive(node.id, node)) {
                    node.type = getVarTypeFromScope(node.id, node).clone();
                } else {
                    // System.out.println(20);
                    throw_semantic(undef_id, node.pos);
                }
                node.type.is_lvalue = true;

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

        if (node.type.equal(bool_type) || node.type.equal(int_type) || node.type.equal(string_type)) {
            throw_semantic(inv_tp, node.pos);
        }

        if (node.lengths != null && node.lengths.length != 0) {
            if (node.init_array != null) {
                throw_syntax("Invalid New Operation", node.pos);
            }
            for (int i = 0; i != node.lengths.length; ++i) {
                check(node.lengths[i]);
                if (!((ExprNode) node.lengths[i]).type.equal(int_type)) {
                    throw_semantic(tp_mis, node.pos);
                }
            }

        } else {
            if (node.init_array != null) {
                check(node.init_array);
                if (!((ExprNode) node.init_array).type.equal(node.type)) {
                    throw_semantic(tp_mis, node.pos);
                }
            }
        }

        node.type.is_lvalue = false;
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
            throw_semantic(inv_tp, node.pos);
        }

        check(node.lhs);
        check(node.rhs);

        if (!((ExprNode) (node.lhs)).type.equal(((ExprNode) (node.rhs)).type)) {

            if (((ExprNode) node.lhs).type.equal(null_type)) {

                if (((ExprNode) node.rhs).type.equal(int_type) || ((ExprNode) node.rhs).type.equal(bool_type)
                        || ((ExprNode) node.rhs).type.equal(string_type)) {
                    throw_semantic(tp_mis, node.pos);
                }

            } else if (((ExprNode) node.rhs).type.equal(null_type)) {

                if (((ExprNode) node.lhs).type.equal(int_type) || ((ExprNode) node.lhs).type.equal(bool_type)
                        || ((ExprNode) node.lhs).type.equal(string_type)) {
                    throw_semantic(tp_mis, node.pos);
                }

            } else {
                throw_semantic(tp_mis, node.pos);
            }
        }

        node.type = ((ExprNode) (node.lhs)).type.clone();
        if (node.type.equal(null_type)) {
            node.type = ((ExprNode) (node.rhs)).type.clone();
        }

        node.type.is_lvalue = false;
    }

    void check(ThisNode node) {
        Node tmp = node;
        while (tmp != null && !(tmp instanceof DefClassNode)) {
            tmp = tmp.father;
        }
        if (tmp == null) {
            throw_syntax("This Pointer Out Of Bound", node.pos);
        }

        node.type = new Type(((IdNode) ((DefClassNode) tmp).name).id);
        node.type.is_lvalue = false;
    }

    void check(UnaryOpNode node) {
        check(node.expr);

        switch (node.oprand) {
            case SAddL:
            case SAddR:
            case SSubL:
            case SSubR:
                if (!((ExprNode) node.expr).type.is_lvalue) {
                    throw_semantic(inv_tp, node.pos);
                }

            case BNot:
            case Plus:
            case Minus:
                if (!((ExprNode) node.expr).type.equal(new Type(int_type))) {
                    throw_semantic(tp_mis, node.pos);
                }
                node.type = new Type(int_type);
                break;

            case LNot:
                if (!((ExprNode) node.expr).type.equal(new Type(bool_type))) {
                    throw_semantic(tp_mis, node.pos);
                }
                node.type = new Type(bool_type);
                break;

            case None:
                break;
        }
        node.type.is_lvalue = false;
        if (node.oprand == UnaryOpNode.UnaryOprand.SAddL || node.oprand == UnaryOpNode.UnaryOprand.SSubL) {
            node.type.is_lvalue = true;
        }
    }
}
