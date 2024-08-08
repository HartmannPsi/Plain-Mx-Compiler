import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.stringtemplate.v4.compiler.CodeGenerator.exprOptions_return;

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
                        // TODO: throw sth
                        return null;
                    }
                } else {
                    continue;
                }
            }
        }

        throw_semantic(tp_mis, ctx);
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

        throw_semantic(tp_mis, ctx);
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
        while (node != null && !(node instanceof DefClassNode)) {
            node = node.father;
        }
        if (node == null) {
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
        } else if (node == null) {
            return;
        } else {
            throw_syntax("Illegal Node Type", null);
        }
    }

    void check(ProgNode node) {
        if (node.father == null) {
            throw_syntax("Illegal Node Structure", null);
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
    }

    void check(ContinueNode node) {
        checkControlFlow(node);

    }

    void check(DefClassNode node) {

    }

    void check(DefFuncNode node) {

    }

    void check(DefVarNode node) {

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
        check(node.step);
        for (int i = 0; i != node.stmts.length; ++i) {
            check(node.stmts[i]);
        }
    }

    void check(IfStmtNode node) {
        check(node.cond);
        check(node.then_stmt);
        check(node.else_stmt);
    }

    void check(ReturnNode node) {
        checkReturn(node);
        // TODO: special: main / type check
    }

    void check(WhileStmtNode node) {
        check(node.cond);
        for (int i = 0; i != node.stmts.length; ++i) {
            check(node.stmts[i]);
        }
    }

    void check(ArrayAccessNode node) {

    }

    void check(ArrayNode node) {

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
                // TODO: consider null
                if (!lhs.type.equal(rhs.type)) {
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
                // TODO: consider null
                if (!lhs.type.equal(rhs.type)) {
                    throw_semantic(tp_mis, null);
                }
                // TODO: check if lvalue
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

    }

    void check(IdNode node) {
        // TODO: get type
    }

    void check(MemAccNode node) {

    }

    void check(NewExprNode node) {

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
            throw_semantic(tp_mis, null);
        }

        check(node.lhs);
        check(node.rhs);
        if (!((ExprNode) (node.lhs)).type.equal(((ExprNode) (node.rhs)).type)) {
            throw_semantic(tp_mis, null);
        }

        node.type = ((ExprNode) (node.lhs)).type.clone();
    }

    void check(ThisNode node) {
        checkInClsDef(node);
        // TODO: get type
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
