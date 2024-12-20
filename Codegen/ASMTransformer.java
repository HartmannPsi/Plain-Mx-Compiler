package Codegen;

import Codegen.ASMNodes.*;
import IR.IRNodes.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

//TODO: Change the way to visit args
// IRGetEleNode
// IRIcmpNode
//TODO: reduce applying memory

public class ASMTransformer {

    IRNode ir_beg;
    ASMNode asm_beg = new ASMNode();
    int rename_serial = 0;
    Map<String, String> alloca_map = new HashMap<>();

    public ASMTransformer(IRNode beg, Map<String, String> var_map) {
        this.ir_beg = beg;
        this.alloca_map = var_map;
    }

    public void generateASM() {
        visit(ir_beg, asm_beg, new HashMap<>(), 0);
    }

    public void printASM() {
        asm_beg.printToString();
    }

    String renameLabel(String label) {
        return "Label.ASM.Rename." + label + "." + rename_serial++;
    }

    String renameVar(String label) {
        return "Var.ASM.Rename." + label + "." + rename_serial++;
    }

    void visit(IRNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        if (node == null) {
            return;
        }

        ASMCommNode comm_node = new ASMCommNode();
        comm_node.message = "\n# " + node.toString();
        // System.out.println(node.toString());
        prev.next = comm_node;

        if (node instanceof IRAllocaNode) {
            visit((IRAllocaNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRBinaryNode) {
            visit((IRBinaryNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRBrNode) {
            visit((IRBrNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRCallNode) {
            visit((IRCallNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRConstStrNode) {
            visit((IRConstStrNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRDclFuncNode) {
            visit((IRDclFuncNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRDebugNode) {
            visit((IRDebugNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRDefClsNode) {
            visit((IRDefClsNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRDefFuncNode) {
            visit((IRDefFuncNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRGetEleNode) {
            visit((IRGetEleNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRGlbInitNode) {
            visit((IRGlbInitNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRIcmpNode) {
            visit((IRIcmpNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRLabelNode) {
            visit((IRLabelNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRLoadNode) {
            visit((IRLoadNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRNLNode) {
            visit((IRNLNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRPhiNode) {
            visit((IRPhiNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRRetNode) {
            visit((IRRetNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRSelectNode) {
            visit((IRSelectNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRStoreNode) {
            visit((IRStoreNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRSectionNode) {
            visit((IRSectionNode) node, comm_node, var_map, total_mem);
        } else if (node instanceof IRPseudoArgNode) {
            visit((IRPseudoArgNode) node, comm_node, var_map, total_mem);
        } else {
            prev.next = null;
            visit(node.next, prev, var_map, total_mem);
        }
    }

    boolean isImm(String str) {
        if (str.charAt(0) == '-' || str.charAt(0) == '0' || str.charAt(0) == '1' || str.charAt(0) == '2'
                || str.charAt(0) == '3' || str.charAt(0) == '4' || str.charAt(0) == '5' || str.charAt(0) == '6'
                || str.charAt(0) == '7' || str.charAt(0) == '8' || str.charAt(0) == '9') {
            return true;
        }
        return false;
    }

    boolean isGlobal(String str) {
        if (str.charAt(0) == '@') {
            return true;
        }
        return false;
    }

    boolean isLocal(String str) {
        if (str.charAt(0) == '%') {
            return true;
        }
        return false;
    }

    boolean isNull(String str) {
        if (str.equals("null")) {
            return true;
        }
        return false;
    }

    boolean isBool(String str) {
        if (str.equals("true") || str.equals("false")) {
            return true;
        }
        return false;
    }

    boolean isStr(String str) {
        if (str.contains("String")) {
            return true;
        }
        return false;
    }

    void visit(IRAllocaNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        int addr = var_map.get(node.result);
        ASMRetType ret = getStackAddr(addr, "t0");
        prev.next = ret.head;
        // t0 -> result

        ASMArithImmNode arith_node = new ASMArithImmNode();
        arith_node.op = "addi";
        arith_node.rd = "t1";
        arith_node.rs1 = "t0";
        arith_node.imm = Integer.toString(4);
        ret.tail.next = arith_node;
        // t1 -> value

        ASMSwNode sw_node = new ASMSwNode();
        sw_node.rs1 = "t0";
        sw_node.rs2 = "t1";
        sw_node.imm = "0";
        arith_node.next = sw_node;
        // [t0] = t1

        ASMSwNode sw_node2 = new ASMSwNode();
        sw_node2.rs1 = "t1";
        sw_node2.rs2 = "zero";
        sw_node2.imm = "0";
        sw_node.next = sw_node2;
        // [t1] = 0

        visit(node.next, sw_node2, var_map, total_mem);
    }

    ASMRetType loadValue(Map<String, Integer> var_map, String reg, String var) {
        if (var == null) {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = "0";
            return new ASMRetType(li_node, li_node, reg);

        } else if (isImm(var)) {

            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = var;
            return new ASMRetType(li_node, li_node, reg);

        } else if (isGlobal(var)) {

            if (isStr(var)) {
                ASMLaNode la_node = new ASMLaNode();
                la_node.rd = reg;
                la_node.label = var.substring(1, var.length());
                return new ASMRetType(la_node, la_node, reg);

            } else {
                ASMLaNode la_node = new ASMLaNode();
                la_node.rd = reg;
                la_node.label = var.substring(1, var.length());
                return new ASMRetType(la_node, la_node, reg);
            }
            // pseudo op

        } else if (isLocal(var)) {

            // judge whether it is a register or a stack variable
            if (alloca_map.containsKey(var) && !alloca_map.get(var).equals("SPILL")) {
                ASMNode empty_node = new ASMNode();
                return new ASMRetType(empty_node, empty_node, alloca_map.get(var));

            } else {

                int addr = var_map.get(var);

                if (addr < 2048 && addr >= -2048) {

                    ASMLwNode lw_node = new ASMLwNode();
                    lw_node.rd = reg;
                    lw_node.imm = Integer.toString(addr);
                    lw_node.rs1 = "sp";

                    return new ASMRetType(lw_node, lw_node, reg);

                } else {
                    ASMRetType ret = getStackAddr(addr, reg);
                    // reg -> value

                    ASMLwNode lw_node = new ASMLwNode();
                    lw_node.rd = reg;
                    lw_node.imm = "0";
                    lw_node.rs1 = reg;
                    ret.tail.next = lw_node;
                    // reg = [reg]

                    return new ASMRetType(ret.head, lw_node, reg);
                }
            }

        } else if (isNull(var)) {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = "0";
            return new ASMRetType(li_node, li_node, reg);

        } else if (isBool(var)) {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = (var.equals("true") ? "1" : "0");
            return new ASMRetType(li_node, li_node, reg);

        } else {
            throw new RuntimeException("Unknown variable: " + var);
        }
    }

    ASMRetType loadValue(int addr, String reg) {
        if (addr < 2048 && addr >= -2048) {

            ASMLwNode lw_node = new ASMLwNode();
            lw_node.rd = reg;
            lw_node.imm = Integer.toString(addr);
            lw_node.rs1 = "sp";

            return new ASMRetType(lw_node, lw_node, reg);

        } else {
            ASMRetType ret = getStackAddr(addr, reg);
            // reg -> value

            ASMLwNode lw_node = new ASMLwNode();
            lw_node.rd = reg;
            lw_node.imm = "0";
            lw_node.rs1 = reg;
            ret.tail.next = lw_node;
            // reg = [reg]

            return new ASMRetType(ret.head, lw_node, reg);
        }
    }

    ASMRetType storeValue(Map<String, Integer> var_map, String reg, String var) {
        // store reg to var
        if (var == null) {
            ASMNode empty_node = new ASMNode();
            return new ASMRetType(empty_node, empty_node);

        } else if (isGlobal(var)) {

            ASMLaNode la_node = new ASMLaNode();
            la_node.rd = "t0";
            la_node.label = var.substring(1, var.length());
            // t0 = &var

            ASMSwNode sw_node = new ASMSwNode();
            sw_node.rs1 = "t0";
            sw_node.rs2 = reg;
            sw_node.imm = "0";
            la_node.next = sw_node;
            // [t0] = reg

            return new ASMRetType(la_node, sw_node);

        } else if (isLocal(var)) {

            // judge whether it is a register or a stack variable
            if (alloca_map.containsKey(var) && !alloca_map.get(var).equals("SPILL")) {
                ASMNode empty_node = new ASMNode();
                return new ASMRetType(empty_node, empty_node);

            } else {

                int addr = var_map.get(var);

                if (addr < 2048 && addr >= -2048) {
                    ASMSwNode sw_node = new ASMSwNode();
                    sw_node.rs1 = "sp";
                    sw_node.rs2 = reg;
                    sw_node.imm = Integer.toString(addr);
                    return new ASMRetType(sw_node, sw_node);

                } else {

                    ASMRetType ret = getStackAddr(addr, "t0");
                    // t0 -> addr

                    ASMSwNode sw_node = new ASMSwNode();
                    sw_node.rs1 = "t0";
                    sw_node.rs2 = reg;
                    sw_node.imm = "0";
                    ret.tail.next = sw_node;
                    // [t0] = reg

                    return new ASMRetType(ret.head, sw_node);
                }
            }

        } else {
            throw new RuntimeException("Unknown variable: " + var);
        }
    }

    ASMRetType storeValue(int addr, String reg) {
        if (addr < 2048 && addr >= -2048) {
            ASMSwNode sw_node = new ASMSwNode();
            sw_node.rs1 = "sp";
            sw_node.rs2 = reg;
            sw_node.imm = Integer.toString(addr);
            return new ASMRetType(sw_node, sw_node);

        } else {

            String tmp_reg = (reg.equals("t0") ? "t1" : "t0");

            ASMRetType ret = getStackAddr(addr, tmp_reg);
            // t0 -> addr

            ASMSwNode sw_node = new ASMSwNode();
            sw_node.rs1 = tmp_reg;
            sw_node.rs2 = reg;
            sw_node.imm = "0";
            ret.tail.next = sw_node;
            // [t0] = reg

            return new ASMRetType(ret.head, sw_node);
        }
    }

    ASMRetType loadValueExact(Map<String, Integer> var_map, String reg, String var) {
        if (var == null) {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = "0";
            return new ASMRetType(li_node, li_node, reg);

        } else if (isImm(var)) {

            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = var;
            return new ASMRetType(li_node, li_node, reg);

        } else if (isGlobal(var)) {

            if (isStr(var)) {
                ASMLaNode la_node = new ASMLaNode();
                la_node.rd = reg;
                la_node.label = var.substring(1, var.length());
                return new ASMRetType(la_node, la_node, reg);

            } else {
                ASMLaNode la_node = new ASMLaNode();
                la_node.rd = reg;
                la_node.label = var.substring(1, var.length());
                return new ASMRetType(la_node, la_node, reg);
            }
            // pseudo op

        } else if (isLocal(var)) {
            // judge whether it is a register or a stack variable
            if (alloca_map.containsKey(var) && !alloca_map.get(var).equals("SPILL")) {
                ASMMvNode mv_node = new ASMMvNode();
                mv_node.rd = reg;
                mv_node.rs = alloca_map.get(var);

                if (mv_node.rd.equals(mv_node.rs)) {
                    ASMNode empty_node = new ASMNode();
                    return new ASMRetType(empty_node, empty_node, reg);

                } else {
                    return new ASMRetType(mv_node, mv_node, reg);
                }
            } else {

                int addr = var_map.get(var);
                if (addr < 2048 && addr >= -2048) {

                    ASMLwNode lw_node = new ASMLwNode();
                    lw_node.rd = reg;
                    lw_node.imm = Integer.toString(addr);
                    lw_node.rs1 = "sp";

                    return new ASMRetType(lw_node, lw_node, reg);

                } else {
                    ASMRetType ret = getStackAddr(addr, reg);
                    // reg -> value

                    ASMLwNode lw_node = new ASMLwNode();
                    lw_node.rd = reg;
                    lw_node.imm = "0";
                    lw_node.rs1 = reg;
                    ret.tail.next = lw_node;
                    // reg = [reg]

                    return new ASMRetType(ret.head, lw_node, reg);
                }
            }

        } else if (isNull(var)) {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = "0";
            return new ASMRetType(li_node, li_node, reg);

        } else if (isBool(var)) {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = (var.equals("true") ? "1" : "0");
            return new ASMRetType(li_node, li_node, reg);

        } else {
            throw new RuntimeException("Unknown variable: " + var);
        }
    }

    void visit(IRBinaryNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        if (isImm(node.op1) && isImm(node.op2)) {// 2 constants
            int op1 = Integer.parseInt(node.op1), op2 = Integer.parseInt(node.op2);
            // System.out.println("op1: " + op1 + " op2: " + op2 + " operator: " +
            // node.operator);
            switch (node.operator) {
                case "add":// +
                    op1 += op2;
                    break;
                case "sub":// -
                    op1 -= op2;
                    break;
                case "mul":// *
                    op1 *= op2;
                    break;
                case "sdiv":// /
                    if (op2 == 0) {
                        op2 = 1;
                        // Warning: may cause error
                    }
                    op1 /= op2;
                    break;
                case "srem":// %
                    if (op2 == 0) {
                        op2 = 1;
                        // Warning: may cause error
                    }
                    op1 %= op2;
                    break;
                case "and":// &
                    op1 &= op2;
                    break;
                case "or":// |
                    op1 |= op2;
                    break;
                case "xor":// ^
                    op1 ^= op2;
                    break;
                case "shl":// <<
                    op1 <<= op2;
                    break;
                case "ashr":// >>
                    op1 >>= op2;
                    break;
                default:
                    throw new RuntimeException("Unknown operator: " + node.operator);
            }

            // int addr = 0;
            if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
                // on stack

                ASMLiNode li_node = new ASMLiNode();
                li_node.rd = "t1";
                li_node.imm = Integer.toString(op1);
                prev.next = li_node;

                ASMRetType sw_ret = storeValue(var_map, "t1", node.result);
                li_node.next = sw_ret.head;

                // addr = var_map.get(node.result);
                // ASMRetType ret = getStackAddr(addr, "t0");
                // li_node.next = ret.head;
                // // t0 -> result

                // ASMSwNode sw_node = new ASMSwNode();
                // sw_node.rs1 = "t0";
                // sw_node.rs2 = "t1";
                // sw_node.imm = "0";
                // ret.tail.next = sw_node;
                // [t0] = t1

                visit(node.next, sw_ret.tail, var_map, total_mem);
            } else {
                // in register

                ASMLiNode li_node = new ASMLiNode();
                li_node.rd = alloca_map.get(node.result);
                li_node.imm = Integer.toString(op1);
                prev.next = li_node;
                visit(node.next, li_node, var_map, total_mem);
            }
            return;
        }

        boolean comm_imm = false;
        int comm_type = 0;
        String imm = null, var = null;

        switch (node.operator) {
            case "sub":// -
                comm_type = 3;
                break;

            case "mul":// *
            case "sdiv":// /
            case "srem":// %
                comm_type = 2;
                break;

            case "add":// +
            case "and":// &
            case "or":// |
            case "xor":// ^
                comm_type = 0;
                break;

            case "shl":// <<
            case "ashr":// >>
                comm_type = 1;
                break;

            default:
                throw new RuntimeException("Unknown operator: " + node.operator);
        }

        if (comm_type == 1) {// op2: imm[4:0]
            var = node.op1;
            imm = node.op2;
            if (isImm(node.op2) && Integer.parseInt(node.op2) >= 0 && Integer.parseInt(node.op2) <= 31) {
                comm_imm = true;
            } else {
                comm_imm = false;

            }
        } else if (comm_type == 0) {// imm[11:0]

            if (isImm(node.op2)) {
                imm = node.op2;
                var = node.op1;
            } else if (isImm(node.op1)) {
                imm = node.op1;
                var = node.op2;
            }

            if (imm != null) {

                int imm_val = Integer.parseInt(imm);
                if (imm_val >= -2048 && imm_val <= 2047) {
                    comm_imm = true;
                } else {
                    comm_imm = false;
                }

            } else {
                comm_imm = false;
            }

        } else if (comm_type == 2) {
            comm_imm = false;

        } else {// sub

            if (isImm(node.op1)) {
                comm_imm = false;

            } else if (isImm(node.op2)) {

                var = node.op1;
                int imm_val = Integer.parseInt(node.op2);
                imm_val = -imm_val;

                if (imm_val >= -2048 && imm_val <= 2047) {
                    comm_imm = true;
                    imm = Integer.toString(imm_val);
                } else {
                    comm_imm = false;
                }
            }
        }

        if (comm_imm) {
            // 1 variable

            ASMRetType ret_op1 = loadValue(var_map, "t0", var);
            prev.next = ret_op1.head;
            // t0 -> op1

            ASMArithImmNode arith_node = new ASMArithImmNode();
            arith_node.rd = "t1";
            arith_node.rs1 = ret_op1.reg;
            arith_node.imm = imm;
            ret_op1.tail.next = arith_node;

            switch (node.operator) {
                case "add":// +
                case "sub":// -
                    arith_node.op = "addi";
                    break;
                case "and":// &
                    arith_node.op = "andi";
                    break;
                case "or":// |
                    arith_node.op = "ori";
                    break;
                case "xor":// ^
                    arith_node.op = "xori";
                    break;
                case "shl":// <<
                    arith_node.op = "slli";
                    break;
                case "ashr":// >>
                    arith_node.op = "srai";
                    break;
                default:
                    throw new RuntimeException("Unknown operator: " + node.operator);
            }

            // int addr = 0;
            if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
                // on stack

                ASMRetType sw_ret = storeValue(var_map, "t1", node.result);
                arith_node.next = sw_ret.head;

                // addr = var_map.get(node.result);
                // ASMRetType ret = getStackAddr(addr, "t0");
                // arith_node.next = ret.head;
                // // t0 -> result

                // ASMSwNode sw_node = new ASMSwNode();
                // sw_node.rs1 = "t0";
                // sw_node.rs2 = "t1";
                // sw_node.imm = "0";
                // ret.tail.next = sw_node;
                // [t0] = t1

                visit(node.next, sw_ret.tail, var_map, total_mem);

            } else {
                // in register

                arith_node.rd = alloca_map.get(node.result);
                visit(node.next, arith_node, var_map, total_mem);
            }

            return;
        } else {
            // 2 variables

            ASMRetType ret_op1 = loadValue(var_map, "t0", node.op1);
            prev.next = ret_op1.head;
            // t0 -> op1

            ASMRetType ret_op2 = loadValue(var_map, "t1", node.op2);
            ret_op1.tail.next = ret_op2.head;
            // t1 -> op2

            ASMArithNode arith_node = new ASMArithNode();
            arith_node.rd = "t1";
            arith_node.rs1 = ret_op1.reg;
            arith_node.rs2 = ret_op2.reg;
            ret_op2.tail.next = arith_node;
            // t1 = t0 op t1

            switch (node.operator) {
                case "add":// +
                    arith_node.op = "add";
                    break;
                case "sub":// -
                    arith_node.op = "sub";
                    break;
                case "mul":// *
                    arith_node.op = "mul";
                    break;
                case "sdiv":// /
                    arith_node.op = "div";
                    break;
                case "srem":// %
                    arith_node.op = "rem";
                    break;
                case "and":// &
                    arith_node.op = "and";
                    break;
                case "or":// |
                    arith_node.op = "or";
                    break;
                case "xor":// ^
                    arith_node.op = "xor";
                    break;
                case "shl":// <<
                    arith_node.op = "sll";
                    break;
                case "ashr":// >>
                    arith_node.op = "sra";
                    break;
                default:
                    throw new RuntimeException("Unknown operator: " + node.operator);
            }

            // int addr = 0;
            if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
                // on stack

                ASMRetType sw_ret = storeValue(var_map, "t1", node.result);
                arith_node.next = sw_ret.head;

                // addr = var_map.get(node.result);
                // ASMRetType ret = getStackAddr(addr, "t0");
                // arith_node.next = ret.head;
                // // t0 -> result

                // ASMSwNode sw_node = new ASMSwNode();
                // sw_node.rs1 = "t0";
                // sw_node.rs2 = "t1";
                // sw_node.imm = "0";
                // ret.tail.next = sw_node;
                // [t0] = t1

                visit(node.next, sw_ret.tail, var_map, total_mem);

            } else {
                // in register

                arith_node.rd = alloca_map.get(node.result);
                visit(node.next, arith_node, var_map, total_mem);
            }
        }
    }

    void visit(IRBrNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        if (node.label_false == null) {
            ASMJNode j_node = new ASMJNode();
            j_node.label = node.label_true;
            prev.next = j_node;
            visit(node.next, j_node, var_map, total_mem);

        } else {

            String true_tmp_label = renameLabel("True_tmp");

            ASMRetType ret = loadValue(var_map, "t0", node.cond);
            prev.next = ret.head;
            // t0 = cond

            ASMBrNode br_node = new ASMBrNode();
            br_node.rs1 = ret.reg;
            br_node.rs2 = "zero";
            br_node.op = "bne";
            br_node.label = true_tmp_label;
            ret.tail.next = br_node;
            // if t0 == true then j true_tmp

            ASMJNode j_node = new ASMJNode();
            j_node.label = node.label_false;
            br_node.next = j_node;
            // j false

            ASMLabelNode label_node = new ASMLabelNode();
            label_node.label = true_tmp_label;
            j_node.next = label_node;
            // true_tmp:

            ASMJNode j_node2 = new ASMJNode();
            j_node2.label = node.label_true;
            label_node.next = j_node2;
            // j true

            visit(node.next, j_node2, var_map, total_mem);
        }
    }

    boolean isBuiltin(String func_name) {
        if (func_name.equals("@print") || func_name.equals("@println") || func_name.equals("@printInt")
                || func_name.equals("@printlnInt") || func_name.equals("@getString") || func_name.equals("@getInt")
                || func_name.equals("@toString") || func_name.equals("@boolToString")
                || func_name.equals("@string.length")
                || func_name.equals("@string.substring")
                || func_name.equals("@string.parseInt") || func_name.equals("@string.ord")
                || func_name.equals("@string.add") || func_name.equals("@string.eq")
                || func_name.equals("@string.ne") || func_name.equals("@string.lt")
                || func_name.equals("@string.le") || func_name.equals("@string.gt")
                || func_name.equals("@string.ge") || func_name.equals("@array.malloc")
                || func_name.equals("@array.size") || func_name.equals("@malloc")) {
            return true;
        }
        return false;
    }

    void visit(IRCallNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        // System.out.println(node.toString());

        Set<String> saved_regs = new HashSet<>();
        if (node.in() != null) {
            for (String active_var : node.in()) {
                if (alloca_map.containsKey(active_var) && !alloca_map.get(active_var).equals("SPILL")) {
                    saved_regs.add(alloca_map.get(active_var));
                }
            }
        }

        int arg_cnt = (node.args == null ? 0 : node.args.length);
        ASMNode tail = prev;

        // int ra_st_addr = var_map.get(node.result) + 4;
        // ASMRetType ret4 = getStackAddr(ra_st_addr, "t2");
        // tail.next = ret4.head;
        // tail = ret4.tail;
        // t2 = ra_st_addr

        // int caller_st_addr = 12 * 4;// ra

        // ASMRetType ra_addr = getStackAddr(caller_st_addr, "t0");
        // tail.next = ra_addr.head;
        // tail = ra_addr.tail;
        // t0 -> ra

        ASMSwNode sw_ra_node = new ASMSwNode();
        sw_ra_node.rs2 = "ra";
        sw_ra_node.rs1 = "sp";
        sw_ra_node.imm = var_map.get("ra").toString();
        tail.next = sw_ra_node;
        tail = sw_ra_node;
        // [t0] = ra

        for (int i = 0; i < 7; ++i) {

            if (!saved_regs.contains("t" + i)) {
                continue;
            }

            // ASMRetType ti_addr = getStackAddr(caller_st_addr + 4 * (i + 1), "t0");
            // tail.next = ti_addr.head;
            // tail = ti_addr.tail;
            // t0 -> t[i]

            ASMSwNode sw_ti_node = new ASMSwNode();
            sw_ti_node.rs2 = "t" + i;
            sw_ti_node.rs1 = "sp";
            sw_ti_node.imm = var_map.get("t" + i).toString();
            tail.next = sw_ti_node;
            tail = sw_ti_node;
            // [t0] = t[i]
        }

        for (int i = 0; i < 8; ++i) {

            if (!saved_regs.contains("a" + i)) {
                continue;
            }

            // ASMRetType ai_addr = getStackAddr(caller_st_addr + 4 * (i + 8 + 1), "t0");
            // tail.next = ai_addr.head;
            // tail = ai_addr.tail;
            // t0 -> a[i]

            ASMSwNode sw_ai_node = new ASMSwNode();
            sw_ai_node.rs2 = "a" + i;
            sw_ai_node.rs1 = "sp";
            sw_ai_node.imm = var_map.get("a" + i).toString();
            tail.next = sw_ai_node;
            tail = sw_ai_node;
            // [t0] = a[i]
        }

        if (saved_regs.contains("gp")) {
            ASMSwNode sw_gp_node = new ASMSwNode();
            sw_gp_node.rs2 = "gp";
            sw_gp_node.rs1 = "sp";
            sw_gp_node.imm = var_map.get("gp").toString();
            tail.next = sw_gp_node;
            tail = sw_gp_node;
        }

        if (saved_regs.contains("tp")) {
            ASMSwNode sw_tp_node = new ASMSwNode();
            sw_tp_node.rs2 = "tp";
            sw_tp_node.rs1 = "sp";
            sw_tp_node.imm = var_map.get("tp").toString();
            tail.next = sw_tp_node;
            tail = sw_tp_node;
        }
        // save caller saved registers: ra, t0-t6, a0-a7, gp, tp

        for (int i = 0; i < arg_cnt && i < 8; ++i) {
            // TODO: Save Regs & use saved regs

            if (alloca_map.containsKey(node.args[i]) && alloca_map.get(node.args[i]).charAt(0) == 'a'
                    && alloca_map.get(node.args[i]).charAt(1) < ('0' + i)) {
                // need to load from saved memory

                int arg_ser = alloca_map.get(node.args[i]).charAt(1) - '0';
                int arg_addr = var_map.get("a" + arg_ser);
                // ASMRetType ret = getStackAddr(arg_addr, "t0");
                // tail.next = ret.head;
                // tail = ret.tail;
                // t0 -> arg[i]

                ASMLwNode lw_node = new ASMLwNode();
                lw_node.rd = "a" + i;
                lw_node.imm = Integer.toString(arg_addr);
                lw_node.rs1 = "sp";
                tail.next = lw_node;
                tail = lw_node;
                // a[i] = [t0]

            } else {

                ASMRetType ret = loadValueExact(var_map, "a" + i, node.args[i]);

                tail.next = ret.head;
                tail = ret.tail;
            }
        }
        // a0-a7 = args[0-7]
        // load args to a0-a7

        int _total_mem = (arg_cnt - 8) * 4;
        if (_total_mem < 0) {
            _total_mem = 0;
        }
        while (_total_mem % 16 != 0) {
            _total_mem += 4;
        }
        // the size of the stack space for the arguments

        if (!isBuiltin(node.func_name) && arg_cnt > 8) {
            ASMRetType ret = appStackSpace(_total_mem);
            tail.next = ret.head;
            tail = ret.tail;

            Map<String, Integer> _var_map = new HashMap<>();
            int addr = 0;

            for (int i = 8; i < arg_cnt; ++i) {
                _var_map.put(node.args[i], addr);
                addr += 4;
            }
            addr = 0;

            for (int i = 8; i < arg_cnt; ++i) {

                if (isLocal(node.args[i])) {

                    if (alloca_map.containsKey(node.args[i]) && !alloca_map.get(node.args[i]).equals("SPILL")) {

                        String arg_reg = alloca_map.get(node.args[i]);
                        if (arg_reg.charAt(0) == 'a' && (arg_cnt >= 8 || arg_reg.charAt(1) < '0' + arg_cnt)) {
                            // need to load from saved memory

                            int arg_ser = alloca_map.get(node.args[i]).charAt(1) - '0';
                            int arg_addr = var_map.get("a" + arg_ser) + _total_mem;

                            ASMRetType ld_ret = loadValue(arg_addr, "t1");
                            tail.next = ld_ret.head;
                            tail = ld_ret.tail;

                            // ASMRetType ld_arg = getStackAddr(arg_addr, "t1");
                            // tail.next = ld_arg.head;
                            // tail = ld_arg.tail;
                            // // t1 -> arg[i]

                            // ASMLwNode lw_node = new ASMLwNode();
                            // lw_node.rd = "t1";
                            // lw_node.imm = "0";
                            // lw_node.rs1 = "t1";
                            // ld_arg.tail.next = lw_node;
                            // tail = lw_node;
                            // t1 = [t1]

                        } else {
                            // in register

                            ASMMvNode mv_node = new ASMMvNode();
                            mv_node.rd = "t1";
                            mv_node.rs = arg_reg;
                            tail.next = mv_node;
                            tail = mv_node;
                            // t1 = arg[i]
                        }

                    } else {
                        // on stack

                        int arg_addr = var_map.get(node.args[i]);
                        arg_addr += _total_mem;

                        ASMRetType lw_ret = loadValue(arg_addr, "t1");
                        tail.next = lw_ret.head;
                        tail = lw_ret.tail;

                        // ASMRetType _ret = getStackAddr(arg_addr, "t1");
                        // tail.next = _ret.head;
                        // // reg -> value

                        // ASMLwNode lw_node = new ASMLwNode();
                        // lw_node.rd = "t1";
                        // lw_node.imm = "0";
                        // lw_node.rs1 = "t1";
                        // _ret.tail.next = lw_node;
                        // // reg = [reg]

                        // tail = lw_node;
                    }

                } else {
                    ASMRetType ret3 = loadValue(var_map, "t1", node.args[i]);
                    tail.next = ret3.head;
                    tail = ret3.tail;
                }
                // t1 = arg[i]

                ASMRetType sw_ret_ = storeValue(addr, "t1");
                tail.next = sw_ret_.head;
                tail = sw_ret_.tail;

                // ASMRetType ret2 = getStackAddr(addr, "t0");
                // tail.next = ret2.head;
                // tail = ret2.tail;
                // // t0 -> arg[i]

                // ASMSwNode sw_node = new ASMSwNode();
                // sw_node.rs1 = "t0";
                // sw_node.rs2 = "t1";
                // sw_node.imm = "0";
                // tail.next = sw_node;
                // tail = sw_node;
                // [t0] = t1

                addr += 4;
            }
            // [sp + 4 * i] = args[i-8]
            // for non-builtin args: save args to stack
        }

        // ASMSwNode sw_node2 = new ASMSwNode();
        // sw_node2.rs1 = "t2";
        // sw_node2.rs2 = "ra";
        // sw_node2.imm = "0";
        // tail.next = sw_node2;
        // tail = sw_node2;
        // // [t2] = ra

        ASMCallNode call_node = new ASMCallNode();
        call_node.label = node.func_name.substring(1, node.func_name.length());
        tail.next = call_node;
        tail = call_node;
        // call func_name

        if (!isBuiltin(node.func_name) && arg_cnt > 8) {
            ASMRetType ret7 = releaseStackSpace(_total_mem);
            tail.next = ret7.head;
            tail = ret7.tail;
        }
        // release stack space

        String res_reg = "";

        if (!node.res_tp.equals("void")) {
            if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
                // on stack

                int ret_st_addr = var_map.get(node.result);

                ASMRetType sw_ret = storeValue(ret_st_addr, "a0");
                tail.next = sw_ret.head;
                tail = sw_ret.tail;

                // ASMRetType ret5 = getStackAddr(ret_st_addr, "t2");
                // tail.next = ret5.head;
                // tail = ret5.tail;
                // // t2 = ret_st_addr

                // ASMSwNode sw_node3 = new ASMSwNode();
                // sw_node3.rs1 = "t2";
                // sw_node3.rs2 = "a0";
                // sw_node3.imm = "0";
                // tail.next = sw_node3;
                // tail = sw_node3;
                // [t2] = a0
            } else {
                // in register

                ASMMvNode mv_node = new ASMMvNode();
                mv_node.rd = alloca_map.get(node.result);
                if (!mv_node.rd.equals("a0")) {
                    mv_node.rs = "a0";
                    tail.next = mv_node;
                    tail = mv_node;
                    res_reg = mv_node.rd;
                } else {
                    res_reg = "a0";
                }
                // result = a0
            }
        }
        // save result

        // int caller_ld_addr = 12 * 4;

        // ASMRetType lw_ra = getStackAddr(caller_ld_addr, "t0");
        // tail.next = lw_ra.head;
        // tail = lw_ra.tail;
        // t0 -> ra

        ASMLwNode lw_ra_node = new ASMLwNode();
        lw_ra_node.rd = "ra";
        lw_ra_node.rs1 = "sp";
        lw_ra_node.imm = var_map.get("ra").toString();
        tail.next = lw_ra_node;
        tail = lw_ra_node;
        // ra = [t0]

        for (int i = 0; i < 7; ++i) {

            if (res_reg.equals("t" + i)) {
                continue;
            }

            if (!saved_regs.contains("t" + i)) {
                continue;
            }

            // ASMRetType lw_ti = getStackAddr(caller_ld_addr + 4 * (i + 1), "t0");
            // tail.next = lw_ti.head;
            // tail = lw_ti.tail;
            // t0 -> t[i]

            ASMLwNode lw_ti_node = new ASMLwNode();
            lw_ti_node.rd = "t" + i;
            lw_ti_node.rs1 = "sp";
            lw_ti_node.imm = var_map.get("t" + i).toString();
            tail.next = lw_ti_node;
            tail = lw_ti_node;
            // t[i] = [t0]
        }

        for (int i = 0; i < 8; ++i) {

            if (res_reg.equals("a" + i)) {
                continue;
            }

            if (!saved_regs.contains("a" + i)) {
                continue;
            }

            // ASMRetType lw_ai = getStackAddr(caller_ld_addr + 4 * (i + 8 + 1), "t0");
            // tail.next = lw_ai.head;
            // tail = lw_ai.tail;
            // t0 -> a[i]

            ASMLwNode lw_ai_node = new ASMLwNode();
            lw_ai_node.rd = "a" + i;
            lw_ai_node.rs1 = "sp";
            lw_ai_node.imm = var_map.get("a" + i).toString();
            tail.next = lw_ai_node;
            tail = lw_ai_node;
            // a[i] = [t0]
        }

        if (saved_regs.contains("gp") && !res_reg.equals("gp")) {
            ASMLwNode lw_gp_node = new ASMLwNode();
            lw_gp_node.rd = "gp";
            lw_gp_node.rs1 = "sp";
            lw_gp_node.imm = var_map.get("gp").toString();
            tail.next = lw_gp_node;
            tail = lw_gp_node;
        }

        if (saved_regs.contains("tp") && !res_reg.equals("tp")) {
            ASMLwNode lw_tp_node = new ASMLwNode();
            lw_tp_node.rd = "tp";
            lw_tp_node.rs1 = "sp";
            lw_tp_node.imm = var_map.get("tp").toString();
            tail.next = lw_tp_node;
            tail = lw_tp_node;
        }
        // load caller saved registers: ra, t0-t6, a0-a7

        visit(node.next, tail, var_map, total_mem);
    }

    void visit(IRConstStrNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.result.substring(1, node.result.length());
        prev.next = label_node;

        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".asciz";
        dot_node.arg1 = "\"" + node.prac_val + "\"";
        label_node.next = dot_node;

        ASMDotInstNode dot_node2 = new ASMDotInstNode();
        dot_node2.inst = ".size";
        dot_node2.arg1 = node.result.substring(1, node.result.length());
        dot_node2.arg2 = Integer.toString(node.length);
        dot_node.next = dot_node2;

        visit(node.next, dot_node2, var_map, total_mem);
    }

    void visit(IRDclFuncNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        visit(node.next, prev, var_map, total_mem);
    }

    void visit(IRDebugNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMCommNode comm_node = new ASMCommNode();
        comm_node.message = "# " + node.message;
        prev.next = comm_node;
        visit(node.next, comm_node, var_map, total_mem);
    }

    void visit(IRPseudoArgNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMCommNode comm_node = new ASMCommNode();
        comm_node.message = "# " + node.toString();
        prev.next = comm_node;
        visit(node.next, comm_node, var_map, total_mem);
    }

    void visit(IRDefClsNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        visit(node.next, prev, var_map, total_mem);
    }

    int collectVars(Map<String, Integer> map, IRDefFuncNode def_node, Set<String> used_regs_callee) {

        int addr = 0;
        // s0-s11: [0, 12 * 4)
        // ra: [12 * 4, 12 * 4 + 4)
        // t0-t6: [12 * 4 + 4, 12 * 4 + 4 + 7 * 4)
        // a0-a7: [12 * 4 + 4 + 7 * 4, 12 * 4 + 4 + 7 * 4 + 8 * 4)
        // gp: [12 * 4 + 4 + 7 * 4 + 8 * 4, 12 * 4 + 4 + 7 * 4 + 8 * 4 + 4)
        // tp: [12 * 4 + 4 + 7 * 4 + 8 * 4 + 4, 12 * 4 + 4 + 7 * 4 + 8 * 4 + 4 + 4)

        // if (def_node.ids != null) {
        // for (int i = 0; i != def_node.ids.length; ++i) {
        // map.put(def_node.ids[i], addr);
        // addr += 4;
        // }
        // }
        Set<String> used_regs_caller = new HashSet<>();

        for (IRNode node = def_node.stmt; node != null; node = node.next) {

            // if (node instanceof IRAllocaNode) {
            // IRAllocaNode alloca_node = (IRAllocaNode) node;
            // if (!map.containsKey(alloca_node.result)) {
            // map.put(alloca_node.result, addr);
            // addr += 8;
            // }
            // // result: addr
            // // value: addr + 4

            // } else
            if (node instanceof IRBinaryNode) {
                IRBinaryNode binary_node = (IRBinaryNode) node;
                if ((!alloca_map.containsKey(binary_node.result) || alloca_map.get(binary_node.result).equals("SPILL"))
                        && !map.containsKey(binary_node.result)) {
                    map.put(binary_node.result, addr);
                    addr += 4;

                } else if (alloca_map.containsKey(binary_node.result)
                        && !alloca_map.get(binary_node.result).equals("SPILL")) {

                    String reg = alloca_map.get(binary_node.result);
                    if (reg.charAt(0) == 's') {
                        used_regs_callee.add(reg);
                    }
                }

            } else if (node instanceof IRCallNode) {
                IRCallNode call_node = (IRCallNode) node;

                // if (call_node.result == null) {
                // call_node.result = renameVar("Void.Virtual.Ret");
                // // map.put(call_node.result, addr);
                // }

                if (call_node.result != null
                        && (!alloca_map.containsKey(call_node.result)
                                || alloca_map.get(call_node.result).equals("SPILL"))
                        && !map.containsKey(call_node.result)) {
                    map.put(call_node.result, addr);
                    addr += 4;

                } else if (call_node.result != null && alloca_map.containsKey(call_node.result)
                        && !alloca_map.get(call_node.result).equals("SPILL")) {

                    String reg = alloca_map.get(call_node.result);
                    if (reg.charAt(0) == 's') {
                        used_regs_callee.add(reg);
                    }
                }

                if (call_node.in() != null) {
                    for (String active_var : call_node.in()) {
                        if (alloca_map.containsKey(active_var) && !alloca_map.get(active_var).equals("SPILL")) {
                            used_regs_caller.add(alloca_map.get(active_var));
                        }
                    }
                }

                used_regs_callee.add("ra");
                // result: addr

            } else if (node instanceof IRGetEleNode) {
                IRGetEleNode ele_node = (IRGetEleNode) node;
                if ((!alloca_map.containsKey(ele_node.result) || alloca_map.get(ele_node.result).equals("SPILL"))
                        && !map.containsKey(ele_node.result)) {
                    map.put(ele_node.result, addr);
                    addr += 4;

                } else if (alloca_map.containsKey(ele_node.result)
                        && !alloca_map.get(ele_node.result).equals("SPILL")) {

                    String reg = alloca_map.get(ele_node.result);
                    if (reg.charAt(0) == 's') {
                        used_regs_callee.add(reg);
                    }
                }

            } else if (node instanceof IRIcmpNode) {
                IRIcmpNode icmp_node = (IRIcmpNode) node;
                if ((!alloca_map.containsKey(icmp_node.result) || alloca_map.get(icmp_node.result).equals("SPILL"))
                        && !map.containsKey(icmp_node.result)) {
                    map.put(icmp_node.result, addr);
                    addr += 4;

                } else if (alloca_map.containsKey(icmp_node.result)
                        && !alloca_map.get(icmp_node.result).equals("SPILL")) {

                    String reg = alloca_map.get(icmp_node.result);
                    if (reg.charAt(0) == 's') {
                        used_regs_callee.add(reg);
                    }
                }

            } else if (node instanceof IRLoadNode) {
                IRLoadNode load_node = (IRLoadNode) node;
                if ((!alloca_map.containsKey(load_node.result) || alloca_map.get(load_node.result).equals("SPILL"))
                        && !map.containsKey(load_node.result)) {
                    map.put(load_node.result, addr);
                    addr += 4;

                } else if (alloca_map.containsKey(load_node.result)
                        && !alloca_map.get(load_node.result).equals("SPILL")) {

                    String reg = alloca_map.get(load_node.result);
                    if (reg.charAt(0) == 's') {
                        used_regs_callee.add(reg);
                    }
                }

            } else if (node instanceof IRPhiNode) {
                IRPhiNode phi_node = (IRPhiNode) node;
                if ((!alloca_map.containsKey(phi_node.result) || alloca_map.get(phi_node.result).equals("SPILL"))
                        && !map.containsKey(phi_node.result)) {
                    map.put(phi_node.result, addr);
                    addr += 4;
                } else if (alloca_map.containsKey(phi_node.result)
                        && !alloca_map.get(phi_node.result).equals("SPILL")) {

                    String reg = alloca_map.get(phi_node.result);
                    if (reg.charAt(0) == 's') {
                        used_regs_callee.add(reg);
                    }
                }

            } else if (node instanceof IRSelectNode) {
                IRSelectNode select_node = (IRSelectNode) node;
                if ((!alloca_map.containsKey(select_node.result) || alloca_map.get(select_node.result).equals("SPILL"))
                        && !map.containsKey(select_node.result)) {
                    map.put(select_node.result, addr);
                    addr += 4;
                } else if (alloca_map.containsKey(select_node.result)
                        && !alloca_map.get(select_node.result).equals("SPILL")) {

                    String reg = alloca_map.get(select_node.result);
                    if (reg.charAt(0) == 's') {
                        used_regs_callee.add(reg);
                    }
                }
            } else if (node instanceof IRRetNode) {
                IRRetNode ret_node = (IRRetNode) node;
                ret_node.used_regs = used_regs_callee;
            }
        }

        int reg_cnt = used_regs_callee.size() + used_regs_caller.size();
        addr += reg_cnt * 4;

        if (reg_cnt > 0) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                entry.setValue(entry.getValue() + reg_cnt * 4);
            }

            int reg_addr = 0;
            for (String reg : used_regs_callee) {
                map.put(reg, reg_addr * 4);
                ++reg_addr;
            }

            for (String reg : used_regs_caller) {
                map.put(reg, reg_addr * 4);
                ++reg_addr;
            }
        }

        while (addr % 16 != 0) {
            addr += 4;
        }

        return addr;
    }

    ASMRetType appStackSpace(int imm) {

        if (imm == 0) {
            ASMNode empty_node = new ASMNode();
            return new ASMRetType(empty_node, empty_node);
        }

        if (-imm < 1 << 11 && -imm >= -(1 << 11)) {
            ASMArithImmNode arith_node = new ASMArithImmNode();
            arith_node.op = "addi";
            arith_node.rd = "sp";
            arith_node.rs1 = "sp";
            arith_node.imm = Integer.toString(-imm);
            return new ASMRetType(arith_node, arith_node);

        } else {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = "t0";
            li_node.imm = Integer.toString(-imm);

            ASMArithNode arith_node = new ASMArithNode();
            arith_node.op = "add";
            arith_node.rd = "sp";
            arith_node.rs1 = "sp";
            arith_node.rs2 = "t0";
            li_node.next = arith_node;
            return new ASMRetType(li_node, arith_node);
        }
    }

    ASMRetType releaseStackSpace(int imm) {

        if (imm == 0) {
            ASMNode empty_node = new ASMNode();
            return new ASMRetType(empty_node, empty_node);
        }

        if (imm < 1 << 11 && imm >= -(1 << 11)) {
            ASMArithImmNode arith_node = new ASMArithImmNode();
            arith_node.op = "addi";
            arith_node.rd = "sp";
            arith_node.rs1 = "sp";
            arith_node.imm = Integer.toString(imm);
            return new ASMRetType(arith_node, arith_node);

        } else {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = "t0";
            li_node.imm = Integer.toString(imm);

            ASMArithNode arith_node = new ASMArithNode();
            arith_node.op = "add";
            arith_node.rd = "sp";
            arith_node.rs1 = "sp";
            arith_node.rs2 = "t0";
            li_node.next = arith_node;
            return new ASMRetType(li_node, arith_node);
        }
    }

    ASMRetType getStackAddr(int imm, String reg) {
        if (imm < 1 << 11 && imm >= -(1 << 11)) {
            ASMArithImmNode arith_node = new ASMArithImmNode();
            arith_node.op = "addi";
            arith_node.rd = reg;
            arith_node.rs1 = "sp";
            arith_node.imm = Integer.toString(imm);
            return new ASMRetType(arith_node, arith_node);

        } else {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = Integer.toString(imm);

            ASMArithNode arith_node = new ASMArithNode();
            arith_node.op = "add";
            arith_node.rd = reg;
            arith_node.rs1 = "sp";
            arith_node.rs2 = reg;
            li_node.next = arith_node;
            return new ASMRetType(li_node, arith_node);
        }
    }

    void visit(IRDefFuncNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        String func_name = node.func_name.substring(1, node.func_name.length());
        // funcs.put(func_name, new HashMap<>());

        int args_total_mem = (node.ids == null ? 0 : node.ids.length - 8) * 4;
        if (args_total_mem < 0) {
            args_total_mem = 0;
        }
        int args_cnt = (node.ids == null ? 0 : node.ids.length);
        while (args_total_mem % 16 != 0) {
            args_total_mem += 4;
        }
        // the size of the stack space for the arguments

        Map<String, Integer> _var_map = new HashMap<>();
        Set<String> used_regs = new HashSet<>();

        for (int i = 0; i != args_cnt; ++i) {
            if (alloca_map.containsKey(node.ids[i]) && !alloca_map.get(node.ids[i]).equals("SPILL")) {
                if (alloca_map.get(node.ids[i]).charAt(0) == 's') {
                    used_regs.add(alloca_map.get(node.ids[i]));
                }
            }
        }

        int _total_mem = collectVars(_var_map, node, used_regs);
        // the size of the stack space for the variables

        int addr = 0;
        for (int i = 8; i < args_cnt; ++i) {
            String arg = node.ids[i];
            _var_map.put(arg, addr + _total_mem);
            addr += 4;
            // arg[i] -> sp + _total_mem + i
        }

        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".globl";
        dot_node.arg1 = func_name;
        prev.next = dot_node;

        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = func_name;
        dot_node.next = label_node;

        ASMRetType ret = appStackSpace(_total_mem);
        label_node.next = ret.head;
        // allocate stack space

        ASMCommNode var_table_node = new ASMCommNode();
        var_table_node.message = "# Variable Table:\n";
        for (Map.Entry<String, Integer> entry : _var_map.entrySet()) {
            var_table_node.message += "\t\t# " + entry.getKey() + " -> " + entry.getValue() + "\n";
        }
        ret.tail.next = var_table_node;
        // print variable table

        ASMNode tail = var_table_node;

        for (int i = 0; i < 12; ++i) {

            if (!used_regs.contains("s" + i)) {
                continue;
            }

            ASMSwNode sw_node = new ASMSwNode();
            sw_node.rs2 = "s" + i;
            sw_node.rs1 = "sp";
            sw_node.imm = _var_map.get("s" + i).toString();
            tail.next = sw_node;
            tail = sw_node;
            // [sp + 4 * i] = si
        }
        // save callee saved registers: s0-s11

        for (int i = 8; i < args_cnt; ++i) {
            String arg = node.ids[i];
            if (alloca_map.containsKey(arg) && !alloca_map.get(arg).equals("SPILL")) {
                String reg = alloca_map.get(arg);
                int arg_addr = _var_map.get(arg);

                ASMRetType lw_ret = loadValue(arg_addr, reg);
                tail.next = lw_ret.head;
                tail = lw_ret.tail;

                // ASMRetType ret2 = getStackAddr(arg_addr, "t0");
                // tail.next = ret2.head;
                // tail = ret2.tail;
                // // t0 -> arg[i]

                // ASMLwNode lw_node = new ASMLwNode();
                // lw_node.rd = reg;
                // lw_node.imm = "0";
                // lw_node.rs1 = "t0";
                // tail.next = lw_node;
                // tail = lw_node;
                // arg[i] = [t0]

                _var_map.remove(arg);
                // save arg from memory to register
            }
        }
        // manage args

        // _total_mem += args_total_mem;
        // the size of the stack space for the arguments and the variables

        visit(node.stmt, tail, _var_map, _total_mem);

        tail = ret.head;
        tail.retract = true;
        while (tail.next != null) {
            tail = tail.next;
            tail.retract = true;
        }

        visit(node.next, tail, var_map, total_mem);
    }

    void visit(IRGetEleNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        ASMRetType ret = loadValue(var_map, "t0", node.ptr);
        prev.next = ret.head;
        ASMNode tail = ret.tail;
        // t0 = ptr

        ASMNode crux_node = null;

        if (node.idxs.length == 1) { // array access

            if (isImm(node.idxs[0]) && Integer.parseInt(node.idxs[0]) * 4 < 1 << 11) {
                // constant

                int imm = Integer.parseInt(node.idxs[0]);

                ASMArithImmNode arith_node = new ASMArithImmNode();
                arith_node.op = "addi";
                arith_node.rd = "t0";
                arith_node.rs1 = ret.reg;
                arith_node.imm = Integer.toString(imm * 4);
                tail.next = arith_node;
                tail = arith_node;
                // t0 = t0 + idx[0]
                crux_node = arith_node;

            } else {
                // variable

                ASMRetType ret_idx = loadValue(var_map, "t1", node.idxs[0]);
                tail.next = ret_idx.head;
                tail = ret_idx.tail;
                // t1 = idx[0]

                ASMArithImmNode arith_node = new ASMArithImmNode();
                arith_node.op = "slli";
                arith_node.rd = "t1";
                arith_node.rs1 = ret_idx.reg;
                arith_node.imm = Integer.toString(2);
                tail.next = arith_node;
                tail = arith_node;
                // // t1 = t1 * 4

                ASMArithNode arith_node2 = new ASMArithNode();
                arith_node2.op = "add";
                arith_node2.rd = "t0";
                arith_node2.rs1 = ret.reg;
                arith_node2.rs2 = "t1";
                tail.next = arith_node2;
                tail = arith_node2;
                // // t0 = t0 + t1

                crux_node = arith_node2;
            }

        } else if (node.idxs.length == 2) { // class access

            if (isImm(node.idxs[1]) && Integer.parseInt(node.idxs[1]) * 4 < 1 << 11) {
                // constant

                int imm = Integer.parseInt(node.idxs[1]);

                ASMArithImmNode arith_node = new ASMArithImmNode();
                arith_node.op = "addi";
                arith_node.rd = "t0";
                arith_node.rs1 = ret.reg;
                arith_node.imm = Integer.toString(imm * 4);
                tail.next = arith_node;
                tail = arith_node;
                // t0 = t0 + idx[1]
                crux_node = arith_node;

            } else {
                // variable

                ASMRetType ret_idx = loadValue(var_map, "t1", node.idxs[1]);
                tail.next = ret_idx.head;
                tail = ret_idx.tail;
                // t1 = idx[1]

                ASMArithImmNode arith_node = new ASMArithImmNode();
                arith_node.op = "slli";
                arith_node.rd = "t1";
                arith_node.rs1 = ret_idx.reg;
                arith_node.imm = Integer.toString(2);
                tail.next = arith_node;
                tail = arith_node;
                // // t1 = t1 * 4

                ASMArithNode arith_node2 = new ASMArithNode();
                arith_node2.op = "add";
                arith_node2.rd = "t0";
                arith_node2.rs1 = ret.reg;
                arith_node2.rs2 = "t1";
                tail.next = arith_node2;
                tail = arith_node2;
                // // t0 = t0 + t1

                crux_node = arith_node2;
            }
        }
        // for (int i = 0; i != node.idxs.length; ++i) {
        // ASMRetType ret_idx = loadValue(var_map, "t1", node.idxs[i]);
        // tail.next = ret_idx.head;
        // tail = ret_idx.tail;
        // // t1 = idx

        // ASMArithImmNode arith_node = new ASMArithImmNode();
        // arith_node.op = "slli";
        // arith_node.rd = "t1";
        // arith_node.rs1 = "t1";
        // arith_node.imm = Integer.toString(2);
        // tail.next = arith_node;
        // tail = arith_node;
        // // t1 = t1 * 4

        // ASMArithNode arith_node2 = new ASMArithNode();
        // arith_node2.op = "add";
        // arith_node2.rd = "t0";
        // arith_node2.rs1 = "t0";
        // arith_node2.rs2 = "t1";
        // tail.next = arith_node2;
        // tail = arith_node2;
        // // t0 = t0 + t1

        // if (i != node.idxs.length - 1) {
        // ASMLwNode lw_node = new ASMLwNode();
        // lw_node.rd = "t0";
        // lw_node.imm = "0";
        // lw_node.rs1 = "t0";
        // tail.next = lw_node;
        // tail = lw_node;
        // // t0 = [t0]
        // }
        // }

        if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
            // on stack

            int addr = var_map.get(node.result);

            ASMRetType sw_ret = storeValue(addr, "t0");
            tail.next = sw_ret.head;
            tail = sw_ret.tail;

            // ASMRetType ret2 = getStackAddr(addr, "t1");
            // tail.next = ret2.head;
            // // t1 -> result

            // ASMSwNode sw_node = new ASMSwNode();
            // sw_node.rs1 = "t1";
            // sw_node.rs2 = "t0";
            // sw_node.imm = "0";
            // ret2.tail.next = sw_node;
            // [t1] = t0

            visit(node.next, tail, var_map, total_mem);
        } else {
            // in register

            if (crux_node instanceof ASMArithNode) {
                ((ASMArithNode) crux_node).rd = alloca_map.get(node.result);
            } else if (crux_node instanceof ASMArithImmNode) {
                ((ASMArithImmNode) crux_node).rd = alloca_map.get(node.result);
            }
            visit(node.next, crux_node, var_map, total_mem);
        }

    }

    String toNum(String str) {
        if (str == null) {
            return "0";
        } else if (isBool(str)) {
            return (str.equals("true") ? "1" : "0");
        } else if (isImm(str)) {
            return str;
        } else {
            return "0";
        }
    }

    void visit(IRGlbInitNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.result.substring(1, node.result.length());
        prev.next = label_node;

        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".word";
        dot_node.arg1 = toNum(node.val);
        label_node.next = dot_node;

        visit(node.next, dot_node, var_map, total_mem);
    }

    void visit(IRIcmpNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        int num_cnt = 0;

        if (isImm(node.op1)) {
            ++num_cnt;
        }

        if (isImm(node.op2)) {
            ++num_cnt;
        }

        ASMNode tail = prev;

        if (num_cnt == 2) {
            // 2 constants
            int op1 = Integer.parseInt(node.op1), op2 = Integer.parseInt(node.op2);
            int res = 0;

            switch (node.cond) {
                case "eq":// ==
                    res = (op1 == op2 ? 1 : 0);
                    break;

                case "ne":// !=
                    res = (op1 != op2 ? 1 : 0);
                    break;

                case "slt":// <
                    res = (op1 < op2 ? 1 : 0);
                    break;

                case "sgt":// >
                    res = (op1 > op2 ? 1 : 0);
                    break;

                case "sle":// <=
                    res = (op1 <= op2 ? 1 : 0);
                    break;

                case "sge":// >=
                    res = (op1 >= op2 ? 1 : 0);
                    break;

                default:
                    throw new RuntimeException("Unknown condition code: " + node.cond);
            }

            if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
                // on stack

                ASMLiNode li_node = new ASMLiNode();
                li_node.rd = "t0";
                li_node.imm = Integer.toString(res);
                prev.next = li_node;
                tail = li_node;

                int addr = var_map.get(node.result);

                ASMRetType sw_ret = storeValue(addr, "t0");
                tail.next = sw_ret.head;
                tail = sw_ret.tail;

                // ASMRetType ret = getStackAddr(addr, "t1");
                // tail.next = ret.head;
                // // t1 -> result

                // ASMSwNode sw_node = new ASMSwNode();
                // sw_node.rs1 = "t1";
                // sw_node.rs2 = "t0";
                // sw_node.imm = "0";
                // ret.tail.next = sw_node;
                // [t1] = t0

                visit(node.next, tail, var_map, total_mem);
                return;

            } else {
                // in register

                ASMLiNode li_node = new ASMLiNode();
                li_node.rd = alloca_map.get(node.result);
                li_node.imm = Integer.toString(res);
                prev.next = li_node;
                tail = li_node;
                // result = res

                visit(node.next, tail, var_map, total_mem);
                return;
            }

        } else {
            // 0 constant

            ASMRetType ret_op1 = loadValue(var_map, "t0", node.op1);
            prev.next = ret_op1.head;
            // t0 = op1

            ASMRetType ret_op2 = loadValue(var_map, "t1", node.op2);
            ret_op1.tail.next = ret_op2.head;
            // t1 = op2

            // ASMArithNode crux_node = null;

            switch (node.cond) {
                case "eq":// ==
                    ASMArithNode arith_node = new ASMArithNode();
                    arith_node.op = "xor";
                    arith_node.rd = "t0";
                    arith_node.rs1 = ret_op1.reg;
                    arith_node.rs2 = ret_op2.reg;
                    ret_op2.tail.next = arith_node;
                    // t0 = t0 ^ t1

                    ASMArithImmNode arith_node7 = new ASMArithImmNode();
                    arith_node7.op = "sltiu";
                    arith_node7.rd = "t0";
                    arith_node7.rs1 = "t0";
                    arith_node7.imm = "1";
                    arith_node.next = arith_node7;
                    // t0 = t0 == 0
                    tail = arith_node7;
                    break;

                case "ne":// !=
                    ASMArithNode arith_node8 = new ASMArithNode();
                    arith_node8.op = "xor";
                    arith_node8.rd = "t0";
                    arith_node8.rs1 = ret_op1.reg;
                    arith_node8.rs2 = ret_op2.reg;
                    ret_op2.tail.next = arith_node8;
                    // t0 = t0 ^ t1

                    // ASMArithImmNode arith_node9 = new ASMArithImmNode();
                    // arith_node9.op = "snez";
                    // arith_node9.rd = "t0";
                    // arith_node9.rs1 = "t0";
                    // arith_node8.next = arith_node9;
                    // t0 = t0 != 0
                    tail = arith_node8;
                    break;

                case "slt":// <
                    ASMArithNode arith_node1 = new ASMArithNode();
                    arith_node1.op = "slt";
                    arith_node1.rd = "t0";
                    arith_node1.rs1 = ret_op1.reg;
                    arith_node1.rs2 = ret_op2.reg;
                    ret_op2.tail.next = arith_node1;
                    // t0 = t0 < t1
                    tail = arith_node1;
                    break;

                case "sgt":// >
                    ASMArithNode arith_node2 = new ASMArithNode();
                    arith_node2.op = "slt";
                    arith_node2.rd = "t0";
                    arith_node2.rs1 = ret_op2.reg;
                    arith_node2.rs2 = ret_op1.reg;
                    ret_op2.tail.next = arith_node2;
                    // t0 = t1 < t0
                    tail = arith_node2;
                    break;

                case "sle":// <=
                    ASMArithNode arith_node3 = new ASMArithNode();
                    arith_node3.op = "slt";
                    arith_node3.rd = "t0";
                    arith_node3.rs1 = ret_op2.reg;
                    arith_node3.rs2 = ret_op1.reg;
                    ret_op2.tail.next = arith_node3;
                    // t0 = t1 < t0

                    ASMArithImmNode arith_node4 = new ASMArithImmNode();
                    arith_node4.op = "xori";
                    arith_node4.rd = "t0";
                    arith_node4.rs1 = "t0";
                    arith_node4.imm = "1";
                    arith_node3.next = arith_node4;
                    // t0 = t0 ^ 1
                    tail = arith_node4;
                    break;

                case "sge":// >=
                    ASMArithNode arith_node5 = new ASMArithNode();
                    arith_node5.op = "slt";
                    arith_node5.rd = "t0";
                    arith_node5.rs1 = ret_op1.reg;
                    arith_node5.rs2 = ret_op2.reg;
                    ret_op2.tail.next = arith_node5;
                    // t0 = t0 < t1

                    ASMArithImmNode arith_node6 = new ASMArithImmNode();
                    arith_node6.op = "xori";
                    arith_node6.rd = "t0";
                    arith_node6.rs1 = "t0";
                    arith_node6.imm = "1";
                    arith_node5.next = arith_node6;
                    // t0 = t0 ^ 1
                    tail = arith_node6;
                    break;

                default:
                    throw new RuntimeException("Unknown condition code: " + node.cond);
            }
            // t0 = value

            if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
                // on stack

                int addr = var_map.get(node.result);

                ASMRetType sw_ret = storeValue(addr, "t0");
                tail.next = sw_ret.head;
                tail = sw_ret.tail;

                // ASMRetType ret = getStackAddr(addr, "t1");
                // tail.next = ret.head;
                // // t1 -> result

                // ASMSwNode sw_node = new ASMSwNode();
                // sw_node.rs1 = "t1";
                // sw_node.rs2 = "t0";
                // sw_node.imm = "0";
                // ret.tail.next = sw_node;
                // [t1] = t0

                visit(node.next, tail, var_map, total_mem);
            } else {
                // in register

                if (tail instanceof ASMArithNode) {
                    ((ASMArithNode) tail).rd = alloca_map.get(node.result);
                } else if (tail instanceof ASMArithImmNode) {
                    ((ASMArithImmNode) tail).rd = alloca_map.get(node.result);
                }

                visit(node.next, tail, var_map, total_mem);
            }
        }
    }

    void visit(IRLabelNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.label;
        prev.next = label_node;

        visit(node.next, label_node, var_map, total_mem);
    }

    void visit(IRLoadNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMRetType ret = loadValue(var_map, "t0", node.ptr);
        prev.next = ret.head;
        // t0 = ptr

        ASMLwNode lw_node = new ASMLwNode();
        lw_node.rd = "t1";
        lw_node.imm = "0";
        lw_node.rs1 = ret.reg;
        ret.tail.next = lw_node;
        // t1 = [t0]

        if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
            // on stack

            int addr = var_map.get(node.result);

            ASMRetType sw_ret = storeValue(addr, "t1");
            lw_node.next = sw_ret.head;

            // ASMRetType ret2 = getStackAddr(addr, "t0");
            // lw_node.next = ret2.head;
            // // t0 -> result

            // ASMSwNode sw_node = new ASMSwNode();
            // sw_node.rs1 = "t0";
            // sw_node.rs2 = "t1";
            // sw_node.imm = "0";
            // ret2.tail.next = sw_node;
            // [t0] = t1

            visit(node.next, sw_ret.tail, var_map, total_mem);
        } else {
            // in register

            lw_node.rd = alloca_map.get(node.result);
            visit(node.next, lw_node, var_map, total_mem);
        }
    }

    void visit(IRNLNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // ASMCommNode comm_node = new ASMCommNode();
        // comm_node.message = "\n";
        // prev.next = comm_node;
        visit(node.next, prev, var_map, total_mem);
    }

    void visit(IRPhiNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // TODO: sth left for optimization
    }

    void visit(IRRetNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMNode tail = prev;

        if (node.val != null) {
            ASMRetType ret = loadValueExact(var_map, "a0", node.val);
            tail.next = ret.head;
            tail = ret.tail;
            // a0 = ret value
        }

        for (int i = 0; i < 12; ++i) {

            if (!node.used_regs.contains("s" + i)) {
                continue;
            }

            ASMLwNode lw_si_node = new ASMLwNode();
            lw_si_node.rd = "s" + i;
            lw_si_node.imm = var_map.get("s" + i).toString();
            lw_si_node.rs1 = "sp";
            tail.next = lw_si_node;
            tail = lw_si_node;
            // s[i] = [sp + 4 * i]
        }
        // load callee saved registers: s0-s11

        ASMRetType ret2 = releaseStackSpace(total_mem);
        tail.next = ret2.head;
        // release stack space

        ASMRetNode ret_node = new ASMRetNode();
        ret2.tail.next = ret_node;

        visit(node.next, ret_node, var_map, total_mem);
    }

    void visit(IRSelectNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        // ASMRetType ret_v2 = loadValue(var_map, "t1", node.val2);
        // ret_v1.tail.next = ret_v2.head;
        // t1 = val2 false_val

        // TODO: finish selecting machanism

        ASMRetType ret_cond = loadValue(var_map, "t2", node.cond);
        prev.next = ret_cond.head;
        // t2 = cond

        String nez_label = renameLabel("Nez");
        String end_label = renameLabel("End");

        ASMBrNode br_node = new ASMBrNode();
        br_node.rs1 = ret_cond.reg;
        br_node.op = "bnez";
        br_node.label = nez_label;
        ret_cond.tail.next = br_node;
        // if t2 != 0 (i.e., t2 == true) then j nez

        ASMRetType ret_v2 = loadValueExact(var_map, "t0", node.val2);
        br_node.next = ret_v2.head;
        // t0 = val2 false_val (if t2 == false)

        ASMJNode j_node = new ASMJNode();
        j_node.label = end_label;
        ret_v2.tail.next = j_node;
        // j end

        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = nez_label;
        j_node.next = label_node;
        // nez:

        ASMRetType ret_v1 = loadValueExact(var_map, "t0", node.val1);
        label_node.next = ret_v1.head;
        // t0 = val1 true_val

        ASMLabelNode label_node2 = new ASMLabelNode();
        label_node2.label = end_label;
        ret_v1.tail.next = label_node2;
        // end:

        if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
            // on stack

            int addr = var_map.get(node.result);

            ASMRetType sw_ret = storeValue(addr, "t0");
            label_node2.next = sw_ret.head;

            // ASMRetType ret = getStackAddr(addr, "t1");
            // label_node2.next = ret.head;
            // // t1 -> result

            // ASMSwNode sw_node = new ASMSwNode();
            // sw_node.rs1 = "t1";
            // sw_node.rs2 = "t0";
            // sw_node.imm = "0";
            // ret.tail.next = sw_node;
            // [t1] = t0

            visit(node.next, sw_ret.tail, var_map, total_mem);
        } else {
            // in register

            ASMMvNode mv_node = new ASMMvNode();
            mv_node.rd = alloca_map.get(node.result);
            mv_node.rs = "t0";
            label_node2.next = mv_node;
            // result = t0

            visit(node.next, mv_node, var_map, total_mem);
        }

    }

    void visit(IRStoreNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMRetType ret = loadValue(var_map, "t0", node.ptr);
        prev.next = ret.head;
        // t0 = ptr

        ASMRetType ret2 = loadValue(var_map, "t1", node.value);
        ret.tail.next = ret2.head;
        // t1 = val

        ASMSwNode sw_node = new ASMSwNode();
        sw_node.rs1 = ret.reg;
        sw_node.rs2 = ret2.reg;
        sw_node.imm = "0";
        ret2.tail.next = sw_node;
        // [t0] = t1

        visit(node.next, sw_node, var_map, total_mem);
    }

    void visit(IRSectionNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".section";
        dot_node.arg1 = node.section;
        prev.next = dot_node;
        visit(node.next, dot_node, var_map, total_mem);
    }

    public void printAlloca() {
        System.out.println("# Allocated Registers:");
        for (Map.Entry<String, String> entry : alloca_map.entrySet()) {
            if (!entry.getValue().equals("SPILL")) {
                System.out.println("# " + entry.getKey() + " -> " + entry.getValue());
            }

        }
    }
}
