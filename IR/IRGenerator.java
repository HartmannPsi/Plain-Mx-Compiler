package IR;

import defNodes.*;
import defNodes.exprNodes.*;
import defNodes.exprNodes.BinaryOpNode.BinaryOprand;
import defNodes.stmtNodes.*;
import util.error.*;
import util.*;
import java.util.Map;
import java.util.HashMap;

public class IRGenerator {
    ProgNode root;
    int id_serial = 0, label_serial = 0;
    Map<String, Map<String, Integer>> classes = new HashMap<>();

    public IRGenerator(ProgNode root) {
        this.root = root;
    }

    public void generateIR() {
        visit(root);
    }

    int getIdSerial() {
        return id_serial++;
    }

    int getLabelSerial() {
        return label_serial++;
    }

    String renameLabel(String label) {
        return label + ".Label." + getLabelSerial();
    }

    String renameIdLocal(String id) {
        return "%" + id + "." + getIdSerial();
    }

    // String anonIdLocal() {
    // return "%" + ((Integer) getIdSerial()).toString();
    // }

    String ptrLocal() {
        return "%ptr.Pointer." + getIdSerial();
    }

    String ptrGlobal() {
        return "@ptr.Pointer." + getIdSerial();
    }

    String renameIdGlobal(String id) {
        return "@" + id + "." + getIdSerial();
    }

    // String anonIdGlobal() {
    // return "@" + ((Integer) getIdSerial()).toString();
    // }

    void throw_internal(String msg, position pos) {
        throw new internalError(msg, pos);
    }

    String visit(Node node) {
        if (node instanceof ProgNode) {
            return visit((ProgNode) node);
        } else if (node instanceof BraceStmtNode) {
            return visit((BraceStmtNode) node);
        } else if (node instanceof BreakNode) {
            return visit((BreakNode) node);
        } else if (node instanceof ClsConsNode) {
            return visit((ClsConsNode) node);
        } else if (node instanceof ContinueNode) {
            return visit((ContinueNode) node);
        } else if (node instanceof DefClassNode) {
            return visit((DefClassNode) node);
        } else if (node instanceof DefFuncNode) {
            return visit((DefFuncNode) node);
        } else if (node instanceof DefVarNode) {
            return visit((DefVarNode) node);
        } else if (node instanceof EmptyStmtNode) {
            return visit((EmptyStmtNode) node);
        } else if (node instanceof ExprStmtNode) {
            return visit((ExprStmtNode) node);
        } else if (node instanceof ForStmtNode) {
            return visit((ForStmtNode) node);
        } else if (node instanceof IfStmtNode) {
            return visit((IfStmtNode) node);
        } else if (node instanceof ReturnNode) {
            return visit((ReturnNode) node);
        } else if (node instanceof WhileStmtNode) {
            return visit((WhileStmtNode) node);
        } else if (node instanceof ArrayAccessNode) {
            return visit((ArrayAccessNode) node);
        } else if (node instanceof ArrayNode) {
            return visit((ArrayNode) node);
        } else if (node instanceof BinaryOpNode) {
            return visit((BinaryOpNode) node);
        } else if (node instanceof BoolConstNode) {
            return visit((BoolConstNode) node);
        } else if (node instanceof FStringNode) {
            return visit((FStringNode) node);
        } else if (node instanceof FuncCallNode) {
            return visit((FuncCallNode) node);
        } else if (node instanceof IdNode) {
            return visit((IdNode) node);
        } else if (node instanceof MemAccNode) {
            return visit((MemAccNode) node);
        } else if (node instanceof NewExprNode) {
            return visit((NewExprNode) node);
        } else if (node instanceof NullNode) {
            return visit((NullNode) node);
        } else if (node instanceof NumConstNode) {
            return visit((NumConstNode) node);
        } else if (node instanceof StringConstNode) {
            return visit((StringConstNode) node);
        } else if (node instanceof TernaryOpNode) {
            return visit((TernaryOpNode) node);
        } else if (node instanceof ThisNode) {
            return visit((ThisNode) node);
        } else if (node instanceof UnaryOpNode) {
            return visit((UnaryOpNode) node);
        } else if (node instanceof TypeNode) {
            return visit((TypeNode) node);
        } else if (node == null) {
            return null;
        } else {
            throw_internal("Unknown Node Type", node.pos);
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////

    String visit(ProgNode node) {
        for (int i = 0; i != node.global_stmt.length; ++i) {
            visit(node.global_stmt[i]);
        }
        return null;
    }

    String visit(BraceStmtNode node) {
        for (int i = 0; i != node.stmts.length; ++i) {
            visit(node.stmts[i]);
        }
        return null;
    }

    String visit(BreakNode node) {
        Node scope = node;
        while (scope != null && !(scope instanceof ForStmtNode || scope instanceof WhileStmtNode)) {
            scope = scope.father;
        }

        if (scope == null) {
            throw_internal("Break statement not in loop", node.pos);
        } else if (scope instanceof ForStmtNode) {
            System.out.println("br label %" + ((ForStmtNode) scope).end_label);
        } else {
            System.out.println("br label %" + ((WhileStmtNode) scope).end_label);
        }

        return null;
    }

    String visit(ClsConsNode node) {// TODO:

        return null;
    }

    String visit(ContinueNode node) {
        Node scope = node;
        while (scope != null && !(scope instanceof ForStmtNode || scope instanceof WhileStmtNode)) {
            scope = scope.father;
        }

        if (scope == null) {
            throw_internal("Continue statement not in loop", node.pos);
        } else if (scope instanceof ForStmtNode) {
            System.out.println("br label %" + ((ForStmtNode) scope).step_label);
        } else {
            System.out.println("br label %" + ((WhileStmtNode) scope).cond_label);
        }

        return null;
    }

    String visit(DefClassNode node) {// TODO:

        return null;
    }

    String visit(DefFuncNode node) {// TODO:

        return null;
    }

    String visit(DefVarNode node) {// TODO:

        return null;
    }

    String visit(EmptyStmtNode node) {
        return null;
    }

    String visit(ExprStmtNode node) {
        visit(node.expr);
        return null;
    }

    String visit(ForStmtNode node) {
        String init_label = renameLabel("For.Init"), cond_label = renameLabel("For.Cond"),
                body_label = renameLabel("For.Body"), step_label = renameLabel("For.Step"),
                end_label = renameLabel("For.End");
        node.end_label = end_label;
        node.step_label = step_label;

        System.out.println(init_label + ":");
        visit(node.init);
        System.out.println("br label %" + cond_label);

        System.out.println(cond_label + ":");
        if (node.cond != null) {
            String cond_id = visit(node.cond);
            System.out.println("br i1 " + cond_id + ", label %" + body_label + ", label %" + end_label);
        } else {
            System.out.println("br label %" + body_label);
        }

        System.out.println(body_label + ":");
        if (node.stmts != null) {
            for (int i = 0; i != node.stmts.length; ++i) {
                visit(node.stmts[i]);
            }
        }
        System.out.println("br label %" + step_label);

        System.out.println(step_label + ":");
        visit(node.step);
        System.out.println("br label %" + cond_label);

        System.out.println(end_label + ":");
        return null;
    }

    String visit(IfStmtNode node) {

        String cond_label = renameLabel("If.Cond"), then_label = renameLabel("If.Then"),
                else_label = renameLabel("If.Else"),
                end_label = renameLabel("If.End");

        System.out.println(cond_label + ":");
        String cond_id = visit(node.cond);
        System.out.println("br i1 " + cond_id + ", label %" + then_label + ", label %" + else_label);

        System.out.println(then_label + ":");
        visit(node.then_stmt);
        System.out.println("br label %" + end_label);

        System.out.println(else_label + ":");
        visit(node.else_stmt);
        System.out.println("br label %" + end_label);

        System.out.println(end_label + ":");
        return null;
    }

    String visit(ReturnNode node) {
        if (node.expr == null) {
            System.out.println("ret void");
        } else {
            String ret_id = visit(node.expr);
            System.out.println("ret " + ((ExprNode) node.expr).type.getLLVMType() + " " + ret_id);
        }
        return null;
    }

    String visit(WhileStmtNode node) {
        String cond_label = renameLabel("While.Cond"), body_label = renameLabel("While.Body"),
                end_label = renameLabel("While.End");
        node.end_label = end_label;
        node.cond_label = cond_label;

        System.out.println(cond_label + ":");
        String cond_id = visit(node.cond);
        System.out.println("br i1 " + cond_id + ", label %" + body_label + ", label %" + end_label);

        System.out.println(body_label + ":");
        if (node.stmts != null) {
            for (int i = 0; i != node.stmts.length; ++i) {
                visit(node.stmts[i]);
            }
        }
        System.out.println("br label %" + cond_label);

        System.out.println(end_label + ":");
        return null;
    }

    String visit(ArrayAccessNode node) {// TODO:

        return null;
    }

    String visit(ArrayNode node) {// TODO:

        return null;
    }

    String visit(BinaryOpNode node) {
        String ret_id = renameIdLocal("BinaryRetVal");
        String ret_tp = node.type.getLLVMType();

        if (node.oprand == BinaryOpNode.BinaryOprand.Mul) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = mul " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOpNode.BinaryOprand.Div) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = sdiv " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOpNode.BinaryOprand.Mod) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = srem " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.Add) {

            if (node.type.equal("string")) {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                System.out.println(
                        ret_id + " = call i8* @string.add(i8* " + lhs_id + ", i8* " + rhs_id + ")");

            } else {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                System.out.println(ret_id + " = add " + ret_tp + " " + lhs_id + ", " + rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Sub) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = sub " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.ShiftL) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = shl " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.ShiftR) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = ashr " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.Lt) {

            if (node.type.equal("string")) {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                System.out.println(ret_id + " = call i1 @string.lt(i8* " + lhs_id + ", i8* " + rhs_id + ")");

            } else {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                System.out.println(ret_id + " = icmp slt " + cmp_tp + " " + lhs_id + ", " + rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Le) {

            if (node.type.equal("string")) {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                System.out.println(ret_id + " = call i1 @string.le(i8* " + lhs_id + ", i8* " + rhs_id + ")");

            } else {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                System.out.println(ret_id + " = icmp sle " + cmp_tp + " " + lhs_id + ", " + rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Gt) {

            if (node.type.equal("string")) {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                System.out.println(ret_id + " = call i1 @string.gt(i8* " + lhs_id + ", i8* " + rhs_id + ")");

            } else {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                System.out.println(ret_id + " = icmp sgt " + cmp_tp + " " + lhs_id + ", " + rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Ge) {

            if (node.type.equal("string")) {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                System.out.println(ret_id + " = call i1 @string.ge(i8* " + lhs_id + ", i8* " + rhs_id + ")");

            } else {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                System.out.println(ret_id + " = icmp sge " + cmp_tp + " " + lhs_id + ", " + rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Eq) {

            if (node.type.equal("string")) {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                System.out.println(ret_id + " = call i1 @string.eq(i8* " + lhs_id + ", i8* " + rhs_id + ")");

            } else {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                System.out.println(ret_id + " = icmp eq " + cmp_tp + " " + lhs_id + ", " + rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Ne) {

            if (node.type.equal("string")) {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                System.out.println(ret_id + " = call i1 @string.ne(i8* " + lhs_id + ", i8* " + rhs_id + ")");

            } else {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                System.out.println(ret_id + " = icmp ne " + cmp_tp + " " + lhs_id + ", " + rhs_id);
            }

        } else if (node.oprand == BinaryOprand.BAnd) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = and " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.BOr) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = or " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.LAnd) {// short circuit
            String lhs_label = renameLabel("LAnd.Lhs"), rhs_label = renameLabel("LAnd.Rhs"),
                    end_label = renameLabel("LAnd.End");
            // String tmp_ptr = ptrLocal();
            // System.out.println(tmp_ptr + " = alloca " + ret_tp);

            System.out.println(lhs_label + ":");
            String lhs_id = visit(node.lhs);
            // System.out.println("store " + ret_tp + " " + lhs_id + ", ptr " + tmp_ptr);
            System.out.println("br i1 " + lhs_id + ", label %" + rhs_label + ", label %" + end_label);

            System.out.println(rhs_label + ":");
            String rhs_id = visit(node.rhs);
            // System.out.println("store " + ret_tp + " " + rhs_id + ", ptr " + tmp_ptr);
            System.out.println("br label %" + end_label);

            System.out.println(end_label + ":");
            // System.out.println(ret_id + " = load " + ret_tp + ", ptr " + tmp_ptr);
            System.out.println(ret_id + " = phi " + ret_tp + " [ false, %" + lhs_label + " ], [ " + rhs_id + ", %"
                    + rhs_label + " ]");

        } else if (node.oprand == BinaryOprand.LOr) {// short circuit
            String lhs_label = renameLabel("LOr.Lhs"), rhs_label = renameLabel("LOr.Rhs"),
                    end_label = renameLabel("LOr.End");
            // String tmp_ptr = ptrLocal();
            // System.out.println(tmp_ptr + " = alloca " + ret_tp);

            System.out.println(lhs_label + ":");
            String lhs_id = visit(node.lhs);
            // System.out.println("store " + ret_tp + " " + lhs_id + ", ptr " + tmp_ptr);
            System.out.println("br i1 " + lhs_id + ", label %" + end_label + ", label %" + rhs_label);

            System.out.println(rhs_label + ":");
            String rhs_id = visit(node.rhs);
            // System.out.println("store " + ret_tp + " " + rhs_id + ", ptr " + tmp_ptr);
            System.out.println("br label %" + end_label);

            System.out.println(end_label + ":");
            // System.out.println(ret_id + " = load " + ret_tp + ", ptr " + tmp_ptr);
            System.out.println(ret_id + " = phi " + ret_tp + " [ true, %" + lhs_label + " ], [ " + rhs_id + ", %"
                    + rhs_label + " ]");

        } else if (node.oprand == BinaryOprand.BXor) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            System.out.println(ret_id + " = xor " + ret_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.Assign) {

        } else {
            throw_internal("Unknown Binary Operator", node.pos);

        }

        return ret_id;
    }

    String visit(BoolConstNode node) {
        return node.val ? "true" : "false";
    }

    String visit(FStringNode node) {

        if (node.exprs == null) {

            String ret_id = renameIdLocal("FStringRetVal");
            System.out.print(ret_id + " = constant [");
            System.out.print(node.getLength(0) + 1);
            System.out.print(" x i8] c\"");
            System.out.print(node.toString(0));
            System.out.println("\\00\"");
            return ret_id;

        } else {

            String[] expr_ids = new String[node.exprs.length];
            String[] str_ids = new String[node.exprs.length];
            for (int i = 0; i != node.exprs.length; ++i) {
                expr_ids[i] = visit(node.exprs[i]);

                if (((ExprNode) node.exprs[i]).type.equal("string")) {

                    str_ids[i] = expr_ids[i];

                } else if (((ExprNode) node.exprs[i]).type.equal("int")) {

                    str_ids[i] = renameIdLocal("FString.Int");
                    System.out.println(str_ids[i] + " = call i8* @toString(i32 " + expr_ids[i] + ")");
                    // System.out.println(str_ids[i] + " = call i8* @intToStr(i32 " + expr_ids[i] +
                    // ")");

                } else if (((ExprNode) node.exprs[i]).type.equal("bool")) {

                    str_ids[i] = renameIdLocal("FString.Bool");
                    System.out.println(str_ids[i] + " = call i8* @boolToString(i1 " + expr_ids[i] + ")");
                    // System.out.println(str_ids[i] + " = call i8* @boolToStr(i1 " + expr_ids[i] +
                    // ")");

                } else {
                    throw_internal("Invalid Type in FString", node.pos);
                }
            }

            String[] tmp_ids = new String[node.literals.length];
            System.out.print(tmp_ids[0] + " = constant [");
            System.out.print(node.getLength(0) + 1);
            System.out.print(" x i8] c\"");
            System.out.print(node.toString(0));
            System.out.println("\\00\"");

            for (int i = 1; i != node.literals.length; ++i) {
                tmp_ids[i] = renameIdLocal("FStringTmp");
                String inter_id = renameIdLocal("FStringInter");
                String literal_id = renameIdLocal("FStringLiteral");
                System.out.println(
                        inter_id + " = call i8* @string.add(i8* " + tmp_ids[i - 1] + ", i8* " + str_ids[i - 1] + ")");

                System.out.print(literal_id + " = constant [");
                System.out.print(node.getLength(i) + 1);
                System.out.print(" x i8] c\"");
                System.out.print(node.toString(i));
                System.out.println("\\00\"");

                System.out.println(
                        tmp_ids[i] + " = call i8* @string.add(i8* " + inter_id + ", i8* " + literal_id + ")");
            }

            String ret_id = tmp_ids[tmp_ids.length - 1];
            return ret_id;
        }
    }

    String visit(FuncCallNode node) {// TODO:

        return null;
    }

    String visit(IdNode node) {

        if (node.is_var) {

            String ret_id = renameIdLocal("IdRetVal");
            System.out.println(ret_id + " = load " + node.type.getLLVMType() + ", ptr " + node.rename_id);
            return ret_id;

        } else {
            return null;
        }
    }

    String visit(MemAccNode node) {// TODO:

        return null;
    }

    String visit(NewExprNode node) {// TODO:

        return null;
    }

    String visit(NullNode node) {
        return "null";
    }

    String visit(NumConstNode node) {
        return ((Integer) node.val).toString();
    }

    String visit(StringConstNode node) {
        String ret_id = renameIdLocal("StringConst");
        System.out.print(ret_id + " = constant [");
        System.out.print(node.getLength() + 1);
        System.out.print(" x i8] c\"");
        System.out.print(node.toString());
        System.out.println("\\00\"");
        return ret_id;
    }

    String visit(TernaryOpNode node) {
        String cond_label = renameLabel("Ternary.Cond"), true_label = renameLabel("Ternary.True"),
                false_label = renameLabel("Ternary.False"), end_label = renameLabel("Ternary.End");
        String ret_id = renameIdLocal("TernaryRetVal");
        String ret_tp = node.type.getLLVMType();

        System.out.println(cond_label + ":");
        // System.out.println(tmp_ptr + " = alloca " + ret_tp);
        String cond_id = visit(node.cond);
        System.out.println("br i1 " + cond_id + ", label %" + true_label + ", label %" + false_label);

        System.out.println(true_label + ":");
        String true_id = visit(node.lhs);
        // System.out.println("store " + ret_tp + " " + true_id + ", ptr " + tmp_ptr);
        System.out.println("br label %" + end_label);

        System.out.println(false_label + ":");
        String false_id = visit(node.rhs);
        // System.out.println("store " + ret_tp + " " + false_id + ", ptr " + tmp_ptr);
        System.out.println("br label %" + end_label);

        System.out.println(end_label + ":");
        // System.out.println(ret_id + " = load " + ret_tp + ", ptr " + tmp_ptr);
        System.out.println(
                ret_id + " = phi " + ret_tp + " [ " + true_id + ", %" + true_label + " ], [ " + false_id + ", %"
                        + false_label + " ]");

        return ret_id;
    }

    String visit(ThisNode node) {// TODO:

        return null;
    }

    String visit(UnaryOpNode node) {
        String expr_id = visit(node.expr);
        String ret_id = renameIdLocal("UnaryRetVal");
        String ret_tp = node.type.getLLVMType();

        if (node.oprand == UnaryOpNode.UnaryOprand.SAddL) {

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SAddR) {

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SSubL) {

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SSubR) {

        } else if (node.oprand == UnaryOpNode.UnaryOprand.Plus) {
            System.out.println(ret_id + " = add " + ret_tp + " 0, " + expr_id);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.Minus) {
            System.out.println(ret_id + " = sub " + ret_tp + " 0, " + expr_id);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.LNot) {
            System.out.println(ret_id + " = xor " + ret_tp + " true, " + expr_id);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.BNot) {
            System.out.println(ret_id + " = xor " + ret_tp + " " + ((Integer) (~0)).toString() + ", " + expr_id);

        } else {
            throw_internal("Unknown Unary Operator", node.pos);
        }

        return ret_id;
    }

    String visit(TypeNode node) {
        return null;
    }

}
