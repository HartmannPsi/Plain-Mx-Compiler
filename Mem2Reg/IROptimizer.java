package Mem2Reg;

import IR.IRNodes.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import util.Pair;

public class IROptimizer {
    public IRNode ir_beg = null;
    // BasicBlockNode dom_tree_root = null;
    Map<String, BasicBlockNode> bbs = new HashMap<>();
    ArrayList<Pair<IRAllocaNode, BasicBlockNode>> def_in_bbs = new ArrayList<>();
    ArrayList<BasicBlockNode> entries = new ArrayList<>();
    int rename_serial = 0;

    String renameAlloca(String obj) {
        return obj + ".Replace." + rename_serial++;
    }

    public IROptimizer(IRNode beg) {
        ir_beg = beg;

        // link next / prev
        for (IRNode cur = beg; cur != null; cur = cur.next) {
            if (cur.next != null)
                cur.next.prev = cur;

            if (cur instanceof IRDefFuncNode) {
                IRDefFuncNode func_node = (IRDefFuncNode) cur;
                func_node.stmt.prev = func_node;
                for (IRNode node = func_node.stmt; node != null; node = node.next) {
                    if (node.next != null)
                        node.next.prev = node;
                }
            }
        }
    }

    public void calcCFG() {
        // generate bbs
        for (IRNode cur = ir_beg; cur != null; cur = cur.next) {
            if (cur instanceof IRDefFuncNode) {

                BasicBlockNode bb = null;
                for (IRNode node = ((IRDefFuncNode) cur).stmt; node != null; node = node.next) {
                    if (node instanceof IRLabelNode) {

                        bb = new BasicBlockNode();
                        bb.label = ((IRLabelNode) node).label;
                        bb.head = node;

                    } else if (node instanceof IRBrNode || node instanceof IRRetNode) {

                        bb.tail = node;
                        bbs.put(bb.label, bb);

                    } else if (node instanceof IRAllocaNode) {

                        def_in_bbs.add(new Pair<>((IRAllocaNode) node, bb));
                    }

                    node.bb = bb;
                }
            }
        }

        // link bbs
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();

            if (bb.tail instanceof IRBrNode) {

                BasicBlockNode succ1 = bbs.get(((IRBrNode) bb.tail).label_true);
                bb.successors.add(succ1);
                succ1.precursors.add(bb);

                if (((IRBrNode) bb.tail).label_false != null) {
                    BasicBlockNode succ2 = bbs.get(((IRBrNode) bb.tail).label_false);
                    bb.successors.add(succ2);
                    succ2.precursors.add(bb);
                }

            } else {
                // instanceof IRRetNode
            }
        }
    }

    public void activeAnalysis() {
        // calc use / def of bb
        Queue<BasicBlockNode> queue = new LinkedList<>();

        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            for (IRNode node = bb.head;; node = node.next) {
                if (node.def() != null) {
                    bb.def.add(node.def());
                }
                if (node.use() != null) {
                    bb.use.add(node.use());
                }
                if (node == bb.tail)
                    break;
            }

            bb.use.removeAll(bb.def);
            queue.add(bb);
        }

        // calc in / out of bb

        while (!queue.isEmpty()) {
            BasicBlockNode bb = queue.poll();
            bb.in = new HashSet<>(bb.out);
            bb.in.removeAll(bb.def);
            bb.in.addAll(bb.use);

            for (BasicBlockNode prec : bb.precursors) {
                if (!bb.in.equals(prec.out)) {
                    prec.out = new HashSet<>(bb.in);
                    queue.add(prec);
                }
            }
        }

        // calc in / out of ir
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {

            BasicBlockNode bb = entry.getValue();

            if (bb.precursors.isEmpty()) {
                entries.add(bb);
            }

            // ArrayList<IRNode> list = new ArrayList<>();
            // for (IRNode node = bb.head;; node = node.next) {
            // list.add(node);
            // if (node == bb.tail)
            // break;
            // }

            Set<String> last_out = new HashSet<>(bb.out);
            for (IRNode node = bb.tail;; node = node.prev) {
                // IRNode node = list.get(i);
                node.out = new HashSet<>(last_out);
                if (node.def() != null) {
                    last_out.remove(node.def());
                }
                if (node.use() != null) {
                    last_out.add(node.use());
                }
                node.in = new HashSet<>(last_out);

                if (node == bb.head)
                    break;
            }
        }
    }

    public void printCFG() {
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            System.out.println("BB " + bb.label + ":");
            System.out.println("  in: " + bb.in);
            System.out.println("  out: " + bb.out);
            System.out.println("  def: " + bb.def);
            System.out.println("  use: " + bb.use);
            System.out.println("  precursors: " + bb.precursors);
            System.out.println("  successors: " + bb.successors);
        }
    }

    public void calcDominate() {
        // calc dom set of bbs
        for (BasicBlockNode entry : entries) {
            entry.dominates.add(entry);
            Queue<BasicBlockNode> queue = new LinkedList<>();
            queue.add(entry);

            while (!queue.isEmpty()) {
                BasicBlockNode bb = queue.poll();

                for (BasicBlockNode succ : bb.successors) {

                    Set<BasicBlockNode> tmp = new HashSet<>();
                    tmp.addAll(bb.dominates);
                    for (BasicBlockNode prec : succ.precursors) {
                        if (prec == bb) {
                            continue;
                        }
                        tmp.retainAll(succ.dominates);
                    }
                    tmp.add(succ);

                    if (tmp.equals(succ.dominates)) {
                        continue;
                    }

                    succ.dominates = tmp;
                    queue.add(succ);
                }
            }
        }

        // calc IDom of bbs
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();

            if (bb.precursors.isEmpty()) {
                continue;
            }

            Queue<BasicBlockNode> queue = new LinkedList<>();
            for (BasicBlockNode prec : bb.precursors) {
                queue.add(prec);
            }

            while (true) {

                BasicBlockNode tmp = queue.poll();

                if (tmp.dominates.size() == bb.dominates.size() - 1) {
                    Set<BasicBlockNode> set = new HashSet<>(bb.dominates);
                    set.removeAll(tmp.dominates);
                    if (set.size() == 1 && set.contains(bb)) {
                        bb.idom = tmp;
                        break;
                    }
                }

                for (BasicBlockNode prec : tmp.precursors) {
                    queue.add(prec);
                }
            }
        }

        // construct dom tree
        for (BasicBlockNode bb : bbs.values()) {
            if (bb.idom != null) {
                bb.idom.dom_tree_son.add(bb);
            }
        }

        // calc dom frontier of bbs
        for (BasicBlockNode bb : bbs.values()) {

            Set<BasicBlockNode> tmp = new HashSet<>(bb.dominates), set = new HashSet<>();
            tmp.remove(bb);

            for (BasicBlockNode prec : bb.precursors) {
                Set<BasicBlockNode> tmp2 = new HashSet<>(prec.dominates);
                tmp2.removeAll(tmp);
                set.addAll(tmp2);
            }

            for (BasicBlockNode node : set) {
                node.dom_frontier = bb;
            }
        }
    }

    public void placePhi() {
        for (Pair<IRAllocaNode, BasicBlockNode> pair : def_in_bbs) {
            IRAllocaNode def = pair.first;
            BasicBlockNode bb = pair.second;
            String res = def.result;
            bb = bb.dom_frontier;

            // reserve phi
            while (bb != null) {
                if (bb.head instanceof IRPhiNode && ((IRPhiNode) bb.head).result == null) {
                    break;
                }

                IRPhiNode phi_node = new IRPhiNode();
                phi_node.prev = bb.head;
                phi_node.next = bb.head.next;
                bb.head.next.prev = phi_node;
                bb.head.next = phi_node;
                phi_node.vals = new String[bb.precursors.size()];
                phi_node.labels = new String[bb.precursors.size()];
                for (int i = 0; i != bb.precursors.size(); ++i) {
                    phi_node.labels[i] = bb.precursors.get(i).label;
                }
                bb = bb.dom_frontier;
            }

            // delete alloca
            def.prev.next = def.next;
            def.next.prev = def.prev;

            // rename obj
            Stack<String> stack = new Stack<>();
            replaceAlloca(res, stack, bb);
        }
    }

    void replaceAlloca(String alloca_name, Stack<String> stack,
            BasicBlockNode bb) {
        int stack_count = 0;
        IRNode beg = bb.head.next;

        // process reserved phi node
        if (bb.head.next instanceof IRPhiNode && ((IRPhiNode) bb.head.next).result == null) {
            beg = bb.head.next.next;
            IRPhiNode phi_node = ((IRPhiNode) bb.head.next);
            phi_node.result = renameAlloca(alloca_name);
            stack.add(phi_node.result);
            ++stack_count;

            // for (int i = 0; i != phi_node.labels.length; ++i) {
            // if (phi_node.labels[i].equals(prec.label)) {
            // phi_node.vals[i] = prec_rename;
            // }
            // }
        }

        // rewrite every command
        Map<String, String> replace = new HashMap<>();// <cur_name, replace_name>
        for (IRNode node = beg;; node = node.next) {

            if (node instanceof IRBinaryNode) {
                IRBinaryNode bin_node = ((IRBinaryNode) node);

                if (replace.containsKey(bin_node.op1)) {
                    bin_node.op1 = replace.get(bin_node.op1);
                }

                if (replace.containsKey(bin_node.op2)) {
                    bin_node.op2 = replace.get(bin_node.op2);
                }

            } else if (node instanceof IRBrNode) {
                IRBrNode br_node = ((IRBrNode) node);

                if (br_node.cond != null && replace.containsKey(br_node.cond)) {
                    br_node.cond = replace.get(br_node.cond);
                }

            } else if (node instanceof IRCallNode) {
                IRCallNode call_node = ((IRCallNode) node);

                if (call_node.args != null) {
                    for (String arg : call_node.args) {
                        if (replace.containsKey(arg)) {
                            arg = replace.get(arg);
                        }
                    }
                }

            } else if (node instanceof IRGetEleNode) {
                IRGetEleNode ele_node = ((IRGetEleNode) node);

                for (String idx : ele_node.idxs) {
                    if (replace.containsKey(idx)) {
                        idx = replace.get(idx);
                    }
                }

            } else if (node instanceof IRIcmpNode) {
                IRIcmpNode icmp_node = ((IRIcmpNode) node);

                if (replace.containsKey(icmp_node.cond)) {
                    icmp_node.cond = replace.get(icmp_node.cond);
                }

                if (replace.containsKey(icmp_node.op1)) {
                    icmp_node.op1 = replace.get(icmp_node.op1);
                }

                if (replace.containsKey(icmp_node.op2)) {
                    icmp_node.op2 = replace.get(icmp_node.op2);
                }

            } else if (node instanceof IRLoadNode) {
                IRLoadNode load_node = ((IRLoadNode) node);

                if (load_node.ptr.equals(alloca_name)) {// using the current value of variable

                    // construct reflect relationship
                    replace.put(load_node.result, stack.peek());

                    // delete the load node
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                    node = node.prev;
                }

            } else if (node instanceof IRPhiNode) {
                IRPhiNode phi_node = ((IRPhiNode) node);

                for (String val : phi_node.vals) {
                    if (replace.containsKey(val)) {
                        val = replace.get(val);
                    }
                }

            } else if (node instanceof IRRetNode) {
                IRRetNode ret_node = ((IRRetNode) node);

                if (ret_node.val != null && replace.containsKey(ret_node.val)) {
                    ret_node.val = replace.get(ret_node.val);
                }

            } else if (node instanceof IRSelectNode) {
                IRSelectNode sel_node = ((IRSelectNode) node);

                if (replace.containsKey(sel_node.cond)) {
                    sel_node.cond = replace.get(sel_node.cond);
                }

                if (replace.containsKey(sel_node.val1)) {
                    sel_node.val1 = replace.get(sel_node.val1);
                }

                if (replace.containsKey(sel_node.val2)) {
                    sel_node.val2 = replace.get(sel_node.val2);
                }

            } else if (node instanceof IRStoreNode) {
                IRStoreNode store_node = ((IRStoreNode) node);

                if (replace.containsKey(store_node.value)) {
                    store_node.value = replace.get(store_node.value);
                }

                if (store_node.ptr.equals(alloca_name)) {// changing the value of variable

                    // push the new value into stack
                    stack.add(store_node.value);
                    ++stack_count;

                    // delete the store node
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                    node = node.prev;
                }
            }

            if (node == bb.tail) {
                break;
            }
        }

        // rewrite phi branches
        for (BasicBlockNode succ : bb.successors) {

            if (succ.head.next instanceof IRPhiNode) {
                IRPhiNode phi_node = ((IRPhiNode) succ.head.next);

                for (int i = 0; i != phi_node.labels.length; ++i) {
                    if (phi_node.labels[i].equals(bb.label)) {
                        phi_node.vals[i] = stack.peek();
                    }
                }
            }
        }

        // recursive
        for (BasicBlockNode son : bb.dom_tree_son) {
            replaceAlloca(alloca_name, stack, son);
        }

        // pop stack
        for (int i = 0; i != stack_count; ++i) {
            stack.pop();
        }

    }

    // boolean rewrite(IRNode node, Stack<String> stack, String replace) {
    // if (node == null) {
    // return false;
    // } else if (node instanceof IRBinaryNode) {
    // return rewrite(((IRBinaryNode) node), stack, replace);
    // } else if (node instanceof IRBrNode) {
    // return rewrite(((IRBrNode) node), stack, replace);
    // } else if (node instanceof IRCallNode) {
    // return rewrite(((IRCallNode) node), stack, replace);
    // } else if (node instanceof IRGetEleNode) {
    // return rewrite(((IRGetEleNode) node), stack, replace);
    // } else if (node instanceof IRIcmpNode) {
    // return rewrite(((IRIcmpNode) node), stack, replace);
    // } else if (node instanceof IRLoadNode) {
    // return rewrite(((IRLoadNode) node), stack, replace);
    // } else if (node instanceof IRPhiNode) {
    // return rewrite(((IRPhiNode) node), stack, replace);
    // } else if (node instanceof IRRetNode) {
    // return rewrite(((IRRetNode) node), stack, replace);
    // } else if (node instanceof IRSelectNode) {
    // return rewrite(((IRSelectNode) node), stack, replace);
    // } else if (node instanceof IRStoreNode) {
    // return rewrite(((IRStoreNode) node), stack, replace);
    // } else {
    // return false;
    // }
    // }

    // boolean rewrite(IRBinaryNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRBrNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRCallNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRGetEleNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRIcmpNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRLoadNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRPhiNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRRetNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRSelectNode node, Stack<String> stack, String replace) {

    // }

    // boolean rewrite(IRStoreNode node, Stack<String> stack, String replace) {

    // }

    public void linearScan() {
        // TODO
    }

    // TODO

}
