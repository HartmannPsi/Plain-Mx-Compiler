package IR;

import defNodes.*;
import defNodes.exprNodes.*;
import defNodes.exprNodes.BinaryOpNode.BinaryOprand;
import defNodes.stmtNodes.*;
import util.error.*;
import util.*;

public class IRGenerator {
    ProgNode root;
    int id_serial = 0, label_serial = 0;

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
        return label + "." + getLabelSerial();
    }

    String renameIdLocal(String id) {
        return "%" + id + "." + getIdSerial();
    }

    // String anonIdLocal() {
    // return "%" + ((Integer) getIdSerial()).toString();
    // }

    String ptrLocal() {
        return "%ptr." + getIdSerial();
    }

    String ptrGlobal() {
        return "@ptr." + getIdSerial();
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

        return null;
    }

    String visit(BraceStmtNode node) {

        return null;
    }

    String visit(BreakNode node) {

        return null;
    }

    String visit(ClsConsNode node) {

        return null;
    }

    String visit(ContinueNode node) {

        return null;
    }

    String visit(DefClassNode node) {

        return null;
    }

    String visit(DefFuncNode node) {

        return null;
    }

    String visit(DefVarNode node) {

        return null;
    }

    String visit(EmptyStmtNode node) {

        return null;
    }

    String visit(ExprStmtNode node) {

        return null;
    }

    String visit(ForStmtNode node) {

        return null;
    }

    String visit(IfStmtNode node) {

        return null;
    }

    String visit(ReturnNode node) {

        return null;
    }

    String visit(WhileStmtNode node) {

        return null;
    }

    String visit(ArrayAccessNode node) {

        return null;
    }

    String visit(ArrayNode node) {

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

        } else if (node.oprand == BinaryOprand.Add) {// TODO: Separate String method

            if (node.type.equal("string")) {

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
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
            System.out.println(ret_id + " = icmp slt " + cmp_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.Le) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
            System.out.println(ret_id + " = icmp sle " + cmp_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.Gt) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
            System.out.println(ret_id + " = icmp sgt " + cmp_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.Ge) {
            String lhs_id = visit(node.lhs);
            String rhs_id = visit(node.rhs);
            String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
            System.out.println(ret_id + " = icmp sge " + cmp_tp + " " + lhs_id + ", " + rhs_id);

        } else if (node.oprand == BinaryOprand.Eq) {// TODO: Separate String method

            if (node.type.equal("string")) {

            } else {
                String lhs_id = visit(node.lhs);
                String rhs_id = visit(node.rhs);
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                System.out.println(ret_id + " = icmp eq " + cmp_tp + " " + lhs_id + ", " + rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Ne) {// TODO: Separate String method

            if (node.type.equal("string")) {

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

        return null;
    }

    String visit(FuncCallNode node) {

        return null;
    }

    String visit(IdNode node) {

        return null;
    }

    String visit(MemAccNode node) {

        return null;
    }

    String visit(NewExprNode node) {

        return null;
    }

    String visit(NullNode node) {
        return "null";
    }

    String visit(NumConstNode node) {
        return ((Integer) node.val).toString();
    }

    String visit(StringConstNode node) {

        return null;
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

    String visit(ThisNode node) {

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

        } else if (node.oprand == UnaryOpNode.UnaryOprand.Minus) {

        } else if (node.oprand == UnaryOpNode.UnaryOprand.LNot) {

        } else if (node.oprand == UnaryOpNode.UnaryOprand.BNot) {

        } else {
            throw_internal("Unknown Unary Operator", node.pos);
        }

        return ret_id;
    }

    String visit(TypeNode node) {
        return null;
    }

}
