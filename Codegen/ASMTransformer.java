package Codegen;

import Codegen.ASMNodes.*;
import IR.IRNodes.*;

import java.util.Map;
import java.util.HashMap;

public class ASMTransformer {

    IRNode ir_beg;
    ASMNode asm_beg = new ASMNode();
    Map<String, Map<String, Integer>> funcs = new HashMap<>();
    int rename_serial = 0;

    public ASMTransformer(IRNode beg) {
        this.ir_beg = beg;
    }

    public void generateASM() {
        visit(ir_beg, asm_beg, null, 0);
    }

    public void printASM() {
        asm_beg.printToString();
    }

    String RenameLabel(String label) {
        return "Label.ASM.Rename." + label + "." + rename_serial++;
    }

    void visit(IRNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        if (node instanceof IRAllocaNode) {
            visit((IRAllocaNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRBinaryNode) {
            visit((IRBinaryNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRBrNode) {
            visit((IRBrNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRCallNode) {
            visit((IRCallNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRConstStrNode) {
            visit((IRConstStrNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRDclFuncNode) {
            visit((IRDclFuncNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRDebugNode) {
            visit((IRDebugNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRDefClsNode) {
            visit((IRDefClsNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRDefFuncNode) {
            visit((IRDefFuncNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRGetEleNode) {
            visit((IRGetEleNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRGlbInitNode) {
            visit((IRGlbInitNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRIcmpNode) {
            visit((IRIcmpNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRLabelNode) {
            visit((IRLabelNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRLoadNode) {
            visit((IRLoadNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRNLNode) {
            visit((IRNLNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRPhiNode) {
            visit((IRPhiNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRRetNode) {
            visit((IRRetNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRSelectNode) {
            visit((IRSelectNode) node, prev, var_map, total_mem);
        } else if (node instanceof IRStoreNode) {
            visit((IRStoreNode) node, prev, var_map, total_mem);
        } else if (node == null) {
            return;
        } else {
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
        if (isImm(var)) {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = var;
            return new ASMRetType(li_node, li_node);

        } else if (isGlobal(var)) {
            ASMLwNode lw_node = new ASMLwNode();
            lw_node.rd = reg;
            lw_node.imm = var.substring(1, var.length());
            return new ASMRetType(lw_node, lw_node);
            // pseudo op

        } else if (isLocal(var)) {
            int addr = var_map.get(var);
            ASMRetType ret = getStackAddr(addr, reg);
            // reg -> value

            ASMLwNode lw_node = new ASMLwNode();
            lw_node.rd = reg;
            lw_node.imm = "0";
            lw_node.rs1 = reg;
            ret.tail.next = lw_node;
            // reg = [reg]

            return new ASMRetType(ret.head, lw_node);

        } else if (isNull(var)) {
            ASMLiNode li_node = new ASMLiNode();
            li_node.rd = reg;
            li_node.imm = "0";
            return new ASMRetType(li_node, li_node);

        } else {
            throw new RuntimeException("Unknown variable: " + var);
        }
    }

    void visit(IRBinaryNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        int addr = var_map.get(node.result);

        ASMRetType ret_op1 = loadValue(var_map, "t0", node.op1);
        prev.next = ret_op1.head;
        // t0 -> op1

        ASMRetType ret_op2 = loadValue(var_map, "t1", node.op2);
        ret_op1.tail.next = ret_op2.head;
        // t1 -> op2

        ASMArithNode arith_node = new ASMArithNode();
        arith_node.rd = "t1";
        arith_node.rs1 = "t0";
        arith_node.rs2 = "t1";
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

        ASMRetType ret = getStackAddr(addr, "t0");
        arith_node.next = ret.head;
        // t0 -> result

        ASMSwNode sw_node = new ASMSwNode();
        sw_node.rs1 = "t0";
        sw_node.rs2 = "t1";
        sw_node.imm = "0";
        ret.tail.next = sw_node;
        // [t0] = t1

        visit(node.next, sw_node, var_map, total_mem);
    }

    void visit(IRBrNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        if (node.label_false == null) {
            ASMJNode j_node = new ASMJNode();
            j_node.label = node.label_true;
            prev.next = j_node;
            visit(node.next, j_node, var_map, total_mem);

        } else {

            String true_tmp_label = RenameLabel("True_tmp");

            ASMRetType ret = loadValue(var_map, "t0", node.cond);
            prev.next = ret.head;
            // t0 = cond

            ASMBrNode br_node = new ASMBrNode();
            br_node.rs1 = "t0";
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

    void visit(IRCallNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // TODO:
    }

    void visit(IRConstStrNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.result;
        prev.next = label_node;

        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".asciz";
        dot_node.arg1 = node.prac_val;
        label_node.next = dot_node;

        ASMDotInstNode dot_node2 = new ASMDotInstNode();
        dot_node2.inst = ".size";
        dot_node2.arg1 = node.result;
        dot_node2.arg2 = Integer.toString(node.length);
        dot_node.next = dot_node2;

        visit(node.next, dot_node2, var_map, total_mem);
    }

    void visit(IRDclFuncNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        visit(node.next, prev, var_map, total_mem);
    }

    void visit(IRDebugNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMCommNode comm_node = new ASMCommNode();
        comm_node.message = "; " + node.message;
        prev.next = comm_node;
        visit(node.next, comm_node, var_map, total_mem);
    }

    void visit(IRDefClsNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        visit(node.next, prev, var_map, total_mem);
    }

    int collectVars(Map<String, Integer> map, IRDefFuncNode def_node) {

        int addr = 0;

        if (def_node.ids != null) {
            for (int i = 0; i != def_node.ids.length; ++i) {
                map.put(def_node.ids[i], addr);
                addr += 4;
            }
        }

        for (IRNode node = def_node.stmt; node != null; node = node.next) {

            if (node instanceof IRAllocaNode) {
                IRAllocaNode alloca_node = (IRAllocaNode) node;
                map.put(alloca_node.result, addr);
                addr += 8;
                // result: addr
                // value: addr + 4

            } else if (node instanceof IRBinaryNode) {
                IRBinaryNode binary_node = (IRBinaryNode) node;
                map.put(binary_node.result, addr);
                addr += 4;

            } else if (node instanceof IRCallNode) {
                IRCallNode call_node = (IRCallNode) node;
                map.put(call_node.result, addr);
                if (call_node.result != null) {
                    addr += 8;
                } else {
                    addr += 4;
                }
                // ra: addr
                // result: addr + 4

            } else if (node instanceof IRGetEleNode) {
                IRGetEleNode ele_node = (IRGetEleNode) node;
                map.put(ele_node.result, addr);
                addr += 4;

            } else if (node instanceof IRIcmpNode) {
                IRIcmpNode icmp_node = (IRIcmpNode) node;
                map.put(icmp_node.result, addr);
                addr += 4;

            } else if (node instanceof IRLoadNode) {
                IRLoadNode load_node = (IRLoadNode) node;
                map.put(load_node.result, addr);
                addr += 4;

            } else if (node instanceof IRPhiNode) {
                IRPhiNode phi_node = (IRPhiNode) node;
                map.put(phi_node.result, addr);
                addr += 4;

            } else if (node instanceof IRSelectNode) {
                IRSelectNode select_node = (IRSelectNode) node;
                map.put(select_node.result, addr);
                addr += 4;
            }
        }

        while (addr % 16 != 0) {
            addr += 4;
        }

        return addr;
    }

    ASMRetType appStackSpace(int imm) {
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
        funcs.put(func_name, new HashMap<>());
        Map<String, Integer> _var_map = funcs.get(func_name);
        int _total_mem = collectVars(var_map, node);

        ASMRetType ret = appStackSpace(total_mem);
        prev.next = ret.head;

        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".globl";
        dot_node.arg1 = func_name;
        ret.tail.next = dot_node;

        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = func_name;
        dot_node.next = label_node;

        visit(node.stmt, label_node, _var_map, _total_mem);

        ASMNode tail = label_node;
        while (tail.next != null) {
            tail = tail.next;
        }

        visit(node.next, tail, var_map, total_mem);
    }

    void visit(IRGetEleNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {

        ASMRetType ret = loadValue(var_map, "t0", node.ptr);
        prev.next = ret.head;
        ASMNode tail = ret.tail;
        // t0 = ptr

        for (int i = 0; i != node.idxs.length; ++i) {
            ASMRetType ret_idx = loadValue(var_map, "t1", node.idxs[i]);
            tail.next = ret_idx.head;
            tail = ret_idx.tail;
            // t1 = idx

            ASMArithImmNode arith_node = new ASMArithImmNode();
            arith_node.op = "slli";
            arith_node.rd = "t1";
            arith_node.rs1 = "t1";
            arith_node.imm = Integer.toString(2);
            tail.next = arith_node;
            tail = arith_node;
            // t1 = t1 * 4

            ASMArithNode arith_node2 = new ASMArithNode();
            arith_node2.op = "add";
            arith_node2.rd = "t0";
            arith_node2.rs1 = "t0";
            arith_node2.rs2 = "t1";
            tail.next = arith_node2;
            tail = arith_node2;
            // t0 = t0 + t1

            if (i != node.idxs.length - 1) {
                ASMLwNode lw_node = new ASMLwNode();
                lw_node.rd = "t0";
                lw_node.imm = "0";
                lw_node.rs1 = "t0";
                tail.next = lw_node;
                tail = lw_node;
                // t0 = [t0]
            }
        }

        int addr = var_map.get(node.result);
        ASMRetType ret2 = getStackAddr(addr, "t1");
        tail.next = ret2.head;
        // t1 -> result

        ASMSwNode sw_node = new ASMSwNode();
        sw_node.rs1 = "t1";
        sw_node.rs2 = "t0";
        sw_node.imm = "0";
        ret2.tail.next = sw_node;
        // [t1] = t0

        visit(node.next, sw_node, var_map, total_mem);
    }

    void visit(IRGlbInitNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.result.substring(1, node.result.length());
        prev.next = label_node;

        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".zero";
        dot_node.arg1 = "4";
        label_node.next = dot_node;

        visit(node.next, dot_node, var_map, total_mem);
    }

    void visit(IRIcmpNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // TODO:
    }

    void visit(IRLabelNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.label;
        prev.next = label_node;

        visit(node.next, label_node, var_map, total_mem);
    }

    void visit(IRLoadNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // TODO:
    }

    void visit(IRNLNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        ASMCommNode comm_node = new ASMCommNode();
        comm_node.message = "\n";
        prev.next = comm_node;
        visit(node.next, comm_node, var_map, total_mem);
    }

    void visit(IRPhiNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // TODO:
    }

    void visit(IRRetNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // TODO:
    }

    void visit(IRSelectNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // TODO:
    }

    void visit(IRStoreNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        // TODO:
    }
}
