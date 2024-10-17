package Codegen;

import Codegen.ASMNodes.*;
import IR.IRNodes.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class ASMTransformer {

    IRNode ir_beg;
    ASMNode asm_beg = new ASMNode();
    int rename_serial = 0;
    Map<String, String> alloca_map = new HashMap<>();
    // TODO: allocate vars to registers
    // TODO: Save regs before call function

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
        int addr = 0;
        if (!alloca_map.containsKey(node.result) || alloca_map.get(node.result).equals("SPILL")) {
            // on stack
            addr = var_map.get(node.result);
        } else {
            // in register
            addr = -1;
        }

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

        if (addr != -1) {
            // on stack

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

        } else {
            // in register

            arith_node.rd = alloca_map.get(node.result);
            visit(node.next, arith_node, var_map, total_mem);
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
        if (node.in != null) {
            for (String active_var : node.in) {
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

        int caller_st_addr = 12 * 4;// ra

        // ASMRetType ra_addr = getStackAddr(caller_st_addr, "t0");
        // tail.next = ra_addr.head;
        // tail = ra_addr.tail;
        // t0 -> ra

        ASMSwNode sw_ra_node = new ASMSwNode();
        sw_ra_node.rs2 = "ra";
        sw_ra_node.rs1 = "sp";
        sw_ra_node.imm = Integer.toString(caller_st_addr);
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
            sw_ti_node.imm = Integer.toString(caller_st_addr + 4 * (i + 1));
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
            sw_ai_node.imm = Integer.toString(caller_st_addr + 4 * (i + 7 + 1));
            tail.next = sw_ai_node;
            tail = sw_ai_node;
            // [t0] = a[i]
        }
        // save caller saved registers: ra, t0-t6, a0-a7

        for (int i = 0; i < arg_cnt && i < 8; ++i) {
            // TODO: Save Regs & use saved regs

            if (alloca_map.containsKey(node.args[i]) && alloca_map.get(node.args[i]).charAt(0) == 'a'
                    && alloca_map.get(node.args[i]).charAt(1) < ('0' + i)) {
                // need to load from saved memory

                int arg_ser = alloca_map.get(node.args[i]).charAt(1) - '0';
                int arg_addr = caller_st_addr + 4 * (arg_ser + 8);
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

        int _total_mem = arg_cnt * 4;
        while (_total_mem % 16 != 0) {
            _total_mem += 4;
        }
        // the size of the stack space for the arguments

        if (!isBuiltin(node.func_name)) {
            ASMRetType ret = appStackSpace(_total_mem);
            tail.next = ret.head;
            tail = ret.tail;

            Map<String, Integer> _var_map = new HashMap<>();
            int addr = 0;

            for (int i = 0; i < arg_cnt; ++i) {
                _var_map.put(node.args[i], addr);
                addr += 4;
            }
            addr = 0;

            for (int i = 0; i < arg_cnt; ++i) {
                ASMRetType ret2 = getStackAddr(addr, "t0");
                tail.next = ret2.head;
                tail = ret2.tail;
                // t0 -> arg[i]

                if (isLocal(node.args[i])) {

                    if (alloca_map.containsKey(node.args[i]) && !alloca_map.get(node.args[i]).equals("SPILL")) {

                        String arg_reg = alloca_map.get(node.args[i]);
                        if (arg_reg.charAt(0) == 'a' && (arg_cnt >= 8 || arg_reg.charAt(1) < '0' + arg_cnt)) {
                            // need to load from saved memory

                            int arg_ser = alloca_map.get(node.args[i]).charAt(1) - '0';
                            int arg_addr = caller_st_addr + 4 * (arg_ser + 8) + _total_mem;
                            ASMRetType ld_arg = getStackAddr(arg_addr, "t1");
                            tail.next = ld_arg.head;
                            tail = ld_arg.tail;
                            // t1 -> arg[i]

                            ASMLwNode lw_node = new ASMLwNode();
                            lw_node.rd = "t1";
                            lw_node.imm = "0";
                            lw_node.rs1 = "t1";
                            ld_arg.tail.next = lw_node;
                            tail = lw_node;
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
                        ASMRetType _ret = getStackAddr(arg_addr, "t1");
                        tail.next = _ret.head;
                        // reg -> value

                        ASMLwNode lw_node = new ASMLwNode();
                        lw_node.rd = "t1";
                        lw_node.imm = "0";
                        lw_node.rs1 = "t1";
                        _ret.tail.next = lw_node;
                        // reg = [reg]

                        tail = lw_node;
                    }

                } else {
                    ASMRetType ret3 = loadValue(var_map, "t1", node.args[i]);
                    tail.next = ret3.head;
                    tail = ret3.tail;
                }
                // t1 = arg[i]

                ASMSwNode sw_node = new ASMSwNode();
                sw_node.rs1 = "t0";
                sw_node.rs2 = "t1";
                sw_node.imm = "0";
                tail.next = sw_node;
                tail = sw_node;
                // [t0] = t1

                addr += 4;
            }
            // [sp + 4 * i] = args[i]
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

        if (!isBuiltin(node.func_name)) {
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
                ASMRetType ret5 = getStackAddr(ret_st_addr, "t2");
                tail.next = ret5.head;
                tail = ret5.tail;
                // t2 = ret_st_addr

                ASMSwNode sw_node3 = new ASMSwNode();
                sw_node3.rs1 = "t2";
                sw_node3.rs2 = "a0";
                sw_node3.imm = "0";
                tail.next = sw_node3;
                tail = sw_node3;
                // [t2] = a0
            } else {
                // in register

                ASMMvNode mv_node = new ASMMvNode();
                mv_node.rd = alloca_map.get(node.result);
                mv_node.rs = "a0";
                tail.next = mv_node;
                tail = mv_node;
                res_reg = mv_node.rd;
                // result = a0
            }
        }
        // save result

        int caller_ld_addr = 12 * 4;

        // ASMRetType lw_ra = getStackAddr(caller_ld_addr, "t0");
        // tail.next = lw_ra.head;
        // tail = lw_ra.tail;
        // t0 -> ra

        ASMLwNode lw_ra_node = new ASMLwNode();
        lw_ra_node.rd = "ra";
        lw_ra_node.rs1 = "sp";
        lw_ra_node.imm = Integer.toString(caller_ld_addr);
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
            lw_ti_node.imm = Integer.toString(caller_ld_addr + 4 * (i + 1));
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
            lw_ai_node.imm = Integer.toString(caller_ld_addr + 4 * (i + 7 + 1));
            tail.next = lw_ai_node;
            tail = lw_ai_node;
            // a[i] = [t0]
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

    void visit(IRDefClsNode node, ASMNode prev, Map<String, Integer> var_map, int total_mem) {
        visit(node.next, prev, var_map, total_mem);
    }

    int collectVars(Map<String, Integer> map, IRDefFuncNode def_node) {

        int addr = 12 * 4 + 4 + 7 * 4 + 8 * 4;
        // s0-s11: [0, 12 * 4)
        // ra: [12 * 4, 12 * 4 + 4)
        // t0-t6: [12 * 4 + 4, 12 * 4 + 4 + 7 * 4)
        // a0-a7: [12 * 4 + 4 + 7 * 4, 12 * 4 + 4 + 7 * 4 + 8 * 4)

        // if (def_node.ids != null) {
        // for (int i = 0; i != def_node.ids.length; ++i) {
        // map.put(def_node.ids[i], addr);
        // addr += 4;
        // }
        // }

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
                }
                // result: addr

            } else if (node instanceof IRGetEleNode) {
                IRGetEleNode ele_node = (IRGetEleNode) node;
                if ((!alloca_map.containsKey(ele_node.result) || alloca_map.get(ele_node.result).equals("SPILL"))
                        && !map.containsKey(ele_node.result)) {
                    map.put(ele_node.result, addr);
                    addr += 4;
                }

            } else if (node instanceof IRIcmpNode) {
                IRIcmpNode icmp_node = (IRIcmpNode) node;
                if ((!alloca_map.containsKey(icmp_node.result) || alloca_map.get(icmp_node.result).equals("SPILL"))
                        && !map.containsKey(icmp_node.result)) {
                    map.put(icmp_node.result, addr);
                    addr += 4;
                }

            } else if (node instanceof IRLoadNode) {
                IRLoadNode load_node = (IRLoadNode) node;
                if ((!alloca_map.containsKey(load_node.result) || alloca_map.get(load_node.result).equals("SPILL"))
                        && !map.containsKey(load_node.result)) {
                    map.put(load_node.result, addr);
                    addr += 4;
                }

            } else if (node instanceof IRPhiNode) {
                IRPhiNode phi_node = (IRPhiNode) node;
                if ((!alloca_map.containsKey(phi_node.result) || alloca_map.get(phi_node.result).equals("SPILL"))
                        && !map.containsKey(phi_node.result)) {
                    map.put(phi_node.result, addr);
                    addr += 4;
                }

            } else if (node instanceof IRSelectNode) {
                IRSelectNode select_node = (IRSelectNode) node;
                if ((!alloca_map.containsKey(select_node.result) || alloca_map.get(select_node.result).equals("SPILL"))
                        && !map.containsKey(select_node.result)) {
                    map.put(select_node.result, addr);
                    addr += 4;
                }
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
        // funcs.put(func_name, new HashMap<>());

        int args_total_mem = (node.ids == null ? 0 : node.ids.length) * 4;
        int args_cnt = args_total_mem / 4;
        while (args_total_mem % 16 != 0) {
            args_total_mem += 4;
        }
        // the size of the stack space for the arguments

        Map<String, Integer> _var_map = new HashMap<>();
        int _total_mem = collectVars(_var_map, node);
        // the size of the stack space for the variables

        int addr = 0;
        for (int i = 0; i != args_cnt; ++i) {
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
            ASMSwNode sw_node = new ASMSwNode();
            sw_node.rs2 = "s" + i;
            sw_node.rs1 = "sp";
            sw_node.imm = Integer.toString(i * 4);
            tail.next = sw_node;
            tail = sw_node;
            // [sp + 4 * i] = si
        }
        // save callee saved registers: s0-s11

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

        ASMArithNode crux_node = null;

        if (node.idxs.length == 1) { // array access

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

        } else if (node.idxs.length == 2) { // class access

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
        } else {
            // in register

            crux_node.rd = alloca_map.get(node.result);
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
        ASMRetType ret_op1 = loadValue(var_map, "t0", node.op1);
        prev.next = ret_op1.head;
        // t0 = op1

        ASMRetType ret_op2 = loadValue(var_map, "t1", node.op2);
        ret_op1.tail.next = ret_op2.head;
        // t1 = op2
        ASMNode tail;
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
            ASMRetType ret = getStackAddr(addr, "t1");
            tail.next = ret.head;
            // t1 -> result

            ASMSwNode sw_node = new ASMSwNode();
            sw_node.rs1 = "t1";
            sw_node.rs2 = "t0";
            sw_node.imm = "0";
            ret.tail.next = sw_node;
            // [t1] = t0

            visit(node.next, sw_node, var_map, total_mem);
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
            ASMRetType ret2 = getStackAddr(addr, "t0");
            lw_node.next = ret2.head;
            // t0 -> result

            ASMSwNode sw_node = new ASMSwNode();
            sw_node.rs1 = "t0";
            sw_node.rs2 = "t1";
            sw_node.imm = "0";
            ret2.tail.next = sw_node;
            // [t0] = t1

            visit(node.next, sw_node, var_map, total_mem);
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
            ASMLwNode lw_si_node = new ASMLwNode();
            lw_si_node.rd = "s" + i;
            lw_si_node.imm = Integer.toString(i * 4);
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
            ASMRetType ret = getStackAddr(addr, "t1");
            label_node2.next = ret.head;
            // t1 -> result

            ASMSwNode sw_node = new ASMSwNode();
            sw_node.rs1 = "t1";
            sw_node.rs2 = "t0";
            sw_node.imm = "0";
            ret.tail.next = sw_node;
            // [t1] = t0

            visit(node.next, sw_node, var_map, total_mem);
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
