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
    // ArrayList<Pair<IRStoreNode, BasicBlockNode>> def_in_bbs = new ArrayList<>();
    Map<String, ArrayList<Pair<IRStoreNode, BasicBlockNode>>> def_in_bbs = new HashMap<>();
    Map<String, IRAllocaNode> allocas = new HashMap<>();
    ArrayList<BasicBlockNode> entries = new ArrayList<>();
    int rename_serial = 0;

    String renameAlloca(String obj) {
        return obj + ".Replace." + rename_serial++;
    }

    String bbLabel() {
        return "Label.eli_CE." + rename_serial++;
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

                    if (node instanceof IRAllocaNode) {
                        allocas.put(((IRAllocaNode) node).result, (IRAllocaNode) node);
                    }
                }
            }
        }
    }

    public void printIR() {
        ir_beg.printToString();
    }

    public void calcCFG() {
        // generate bbs

        // System.out.println("AAAA");

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

                    } else if (node instanceof IRStoreNode) {

                        if (allocas.containsKey(((IRStoreNode) node).ptr)) {
                            if (!def_in_bbs.containsKey(((IRStoreNode) node).ptr)) {
                                def_in_bbs.put(((IRStoreNode) node).ptr, new ArrayList<>());
                            }
                            def_in_bbs.get(((IRStoreNode) node).ptr).add(new Pair<>((IRStoreNode) node, bb));
                        }
                    }

                    node.bb = bb;
                }
            }
        }

        // System.out.println("BBBB");

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

        // System.out.println("CCCC");

        // set dom set to the whole set of bbs
        Set<BasicBlockNode> all_bbs = new HashSet<>(bbs.values());
        for (BasicBlockNode bb : bbs.values()) {
            bb.dominates = all_bbs;
            if (bb.precursors.isEmpty()) {
                entries.add(bb);
            }
        }

        // System.out.println("DDDD");

        // delete global vars in def_in_bbs
        // for (Map.Entry<String, ArrayList<Pair<IRStoreNode, BasicBlockNode>>> entry :
        // def_in_bbs.entrySet()) {
        // if (entry.getKey().charAt(0) == '@') {
        // def_in_bbs.remove(entry.getKey());
        // }
        // }

        // System.out.println("EEEE");
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

            // if (bb.precursors.isEmpty()) {
            // entries.add(bb);
            // }

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
        System.out.println("\nCFG:");
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            System.out.println("BB " + bb.label + ":");
            // System.out.println(" in: " + bb.in);
            // System.out.println(" out: " + bb.out);
            // System.out.println(" def: " + bb.def);
            // System.out.println(" use: " + bb.use);
            System.out.print("  precursors: ");
            for (BasicBlockNode prec : bb.precursors) {
                System.out.print(prec.label + " ");
            }
            System.out.println();
            System.out.print("  successors: ");
            for (BasicBlockNode succ : bb.successors) {
                System.out.print(succ.label + " ");
            }
            System.out.println();
            System.out.print("  dominates: ");
            for (BasicBlockNode dom : bb.dominates) {
                System.out.print(dom.label + " ");
            }
            System.out.println("\n" + bb.dominates.size() + "\n");
        }
    }

    public void printIDom() {
        System.out.println("\nIDom:");
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            System.out.println("BB " + bb.label + ":");
            System.out.println("  idom: " + (bb.idom == null ? "null" : bb.idom.label));
        }
    }

    public void printFrontier() {
        System.out.println("\nFrontier:");
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            System.out.println("BB " + bb.label + ":");
            System.out.print("  dom_frontier: ");
            for (BasicBlockNode dom : bb.dom_frontier) {
                System.out.print(dom.label + " ");
            }
            System.out.println();
        }
    }

    public void calcDominate() {
        // calc dom set of bbs

        // if (entries.isEmpty()) {
        // System.out.println("No entries");
        // }

        for (BasicBlockNode entry : entries) {
            // System.out.println("Entry: " + entry.label);
            entry.dominates = new HashSet<>();
            entry.dominates.add(entry);
            Queue<BasicBlockNode> queue = new LinkedList<>();
            queue.add(entry);

            while (!queue.isEmpty()) {
                BasicBlockNode bb = queue.poll();

                for (BasicBlockNode succ : bb.successors) {

                    Set<BasicBlockNode> tmp = new HashSet<>(bb.dominates);
                    // tmp.addAll(bb.dominates);
                    for (BasicBlockNode prec : succ.precursors) {
                        if (prec == bb) {
                            continue;
                        }
                        tmp.retainAll(prec.dominates);
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

        // printCFG();

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

                if (queue.isEmpty()) {
                    break;
                }

                BasicBlockNode tmp = queue.poll();
                // System.out.println(tmp.label);

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

        // printIDom();

        // construct dom tree
        for (BasicBlockNode bb : bbs.values()) {
            if (bb.idom != null) {
                bb.idom.dom_tree_son.add(bb);
            }
        }

        // System.out.println("D");

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
                node.dom_frontier.add(bb);
            }
        }

        // printFrontier();

    }

    public void placePhi() {

        // System.out.println("\ndefs:");
        // for (Map.Entry<String, ArrayList<Pair<IRStoreNode, BasicBlockNode>>> entry :
        // def_in_bbs.entrySet()) {
        // ArrayList<Pair<IRStoreNode, BasicBlockNode>> def_in_bb = entry.getValue();
        // System.out.println(entry.getKey() + ":");
        // for (Pair<IRStoreNode, BasicBlockNode> pair : def_in_bb) {
        // System.out.println(" " + pair.first.toString() + "\n in " +
        // pair.second.label);
        // }
        // System.out.println();
        // }

        for (Map.Entry<String, ArrayList<Pair<IRStoreNode, BasicBlockNode>>> entry : def_in_bbs.entrySet()) {

            ArrayList<Pair<IRStoreNode, BasicBlockNode>> def_in_bb = entry.getValue();

            // System.out.println(entry.getKey() + ":");

            for (Pair<IRStoreNode, BasicBlockNode> pair : def_in_bb) {
                IRStoreNode def = pair.first;
                BasicBlockNode beg = pair.second;
                // bb = bb.dom_frontier;
                Queue<BasicBlockNode> queue = new LinkedList<>();
                for (BasicBlockNode frontier : beg.dom_frontier) {
                    queue.add(frontier);
                }

                // reserve phi
                while (!queue.isEmpty()) {
                    BasicBlockNode bb = queue.poll();
                    if (bb.head.next instanceof IRPhiNode && ((IRPhiNode) bb.head.next).result == null) {
                        continue;
                    }

                    IRPhiNode phi_node = new IRPhiNode();
                    phi_node.prev = bb.head;
                    phi_node.next = bb.head.next;
                    bb.head.next.prev = phi_node;
                    bb.head.next = phi_node;

                    phi_node.tp = def.tp;
                    phi_node.vals = new String[bb.precursors.size()];
                    phi_node.labels = new String[bb.precursors.size()];
                    for (int i = 0; i != bb.precursors.size(); ++i) {
                        phi_node.labels[i] = bb.precursors.get(i).label;
                        // if (phi_node.tp.equals("ptr")) {
                        // phi_node.vals[i] = "null";
                        // } else {
                        // phi_node.vals[i] = "0";
                        // }
                        phi_node.vals[i] = "undef";
                    }

                    for (BasicBlockNode frontier : bb.dom_frontier) {
                        queue.add(frontier);
                    }
                }
            }

            // System.out.println("RES");
            // ir_beg.printToString();

            // find the entry block
            BasicBlockNode bb = def_in_bb.get(0).second;
            while (bb.idom != null) {
                bb = bb.idom;
            }

            // rename obj
            // for (BasicBlockNode bb : entries) {

            Stack<String> stack = new Stack<>();
            if (def_in_bb.get(0).first.tp.equals("ptr")) {
                stack.add("null");
            } else {
                stack.add("0");
            }
            Map<String, String> replace = new HashMap<>();// <cur_name, replace_name>
            collectRenames(entry.getKey(), stack, bb, replace);
            renameVars(replace, bb.head);
            // }

            // System.out.println("C");
        }

        // System.out.println("Exit");

        // delete alloca
        for (IRNode node = ir_beg; node != null; node = node.next) {
            if (node instanceof IRDefFuncNode) {
                for (IRNode cur = ((IRDefFuncNode) node).stmt; cur != null; cur = cur.next) {
                    if (cur instanceof IRAllocaNode) {
                        ((IRAllocaNode) cur).eliminated = true;
                    }
                }
            }
        }
    }

    void renameVars(Map<String, String> replace, IRNode beg) {
        for (IRNode node = beg; node != null; node = node.next) {

            // System.out.println(node.toString());

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
                    for (int i = 0; i != call_node.args.length; ++i) {
                        if (replace.containsKey(call_node.args[i])) {
                            call_node.args[i] = replace.get(call_node.args[i]);
                        }
                    }
                }

            } else if (node instanceof IRGetEleNode) {
                IRGetEleNode ele_node = ((IRGetEleNode) node);

                if (replace.containsKey(ele_node.ptr)) {
                    ele_node.ptr = replace.get(ele_node.ptr);
                }

                for (int i = 0; i != ele_node.idxs.length; ++i) {

                    if (replace.containsKey(ele_node.idxs[i])) {

                        ele_node.idxs[i] = replace.get(ele_node.idxs[i]);
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

                if (load_node.eliminated) {
                    continue;
                }

                if (replace.containsKey(load_node.ptr)) {
                    load_node.ptr = replace.get(load_node.ptr);
                }

            } else if (node instanceof IRPhiNode) {
                IRPhiNode phi_node = ((IRPhiNode) node);

                for (int i = 0; i != phi_node.vals.length; ++i) {
                    if (replace.containsKey(phi_node.vals[i])) {
                        phi_node.vals[i] = replace.get(phi_node.vals[i]);
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

                if (store_node.eliminated) {
                    continue;
                }

                if (replace.containsKey(store_node.value)) {
                    store_node.value = replace.get(store_node.value);
                }

                if (replace.containsKey(store_node.ptr)) {
                    store_node.ptr = replace.get(store_node.ptr);
                }
            }

        }
    }

    void collectRenames(String alloca_name, Stack<String> stack,
            BasicBlockNode bb, Map<String, String> replace) {
        int stack_count = 0;
        IRNode beg = bb.head.next;

        // System.out.println("RA");

        // process reserved phi node
        if (bb.head.next instanceof IRPhiNode && ((IRPhiNode) bb.head.next).result == null) {
            // beg = bb.head.next.next;
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

        /// System.out.println("B");

        // rewrite every command
        // Map<String, String> replace = new HashMap<>();// <cur_name, replace_name>

        for (IRNode node = beg;; node = node.next) {

            // System.out.println(node.toString());

            if (node instanceof IRLoadNode) {
                IRLoadNode load_node = ((IRLoadNode) node);

                if (load_node.eliminated) {
                    continue;
                }

                if (load_node.ptr.equals(alloca_name)) {// using the current value of variable

                    // construct reflect relationship
                    replace.put(load_node.result, stack.peek());

                    // delete the load node
                    load_node.eliminated = true;
                }

            } else if (node instanceof IRStoreNode) {
                IRStoreNode store_node = ((IRStoreNode) node);

                if (store_node.eliminated) {
                    continue;
                }

                if (store_node.ptr.equals(alloca_name)) {// changing the value of variable

                    // push the new value into stack
                    stack.add(store_node.value);
                    ++stack_count;

                    // delete the store node
                    store_node.eliminated = true;
                }
            }

            if (node == bb.tail) {
                break;
            }
        }

        // if (replace.containsKey("%IdRetVal.103")) {
        // System.out.println(bb.label + ":");
        // System.out.println(replace.get("%IdRetVal.103"));
        // }

        // System.out.println("C");

        // rewrite phi branches
        for (BasicBlockNode succ : bb.successors) {

            // System.out.println(succ.label);

            // rewrite phi branches
            if (succ.head.next instanceof IRPhiNode) {
                IRPhiNode phi_node = ((IRPhiNode) succ.head.next);

                // System.out.println("F");

                for (int i = 0; i != phi_node.labels.length; ++i) {

                    if (phi_node.labels[i].equals(bb.label) && phi_node.vals[i].equals("undef")) {
                        if (stack.isEmpty()) {
                            if (phi_node.tp.equals("ptr")) {
                                phi_node.vals[i] = "null";
                            } else {
                                phi_node.vals[i] = "0";
                            }
                        } else {
                            phi_node.vals[i] = stack.peek();
                        }
                        break;
                    }
                }
            }

            // rewrite phi nodes at the beginning of succ
            // for (IRNode node = succ.head.next; node instanceof IRPhiNode; node =
            // node.next) {
            // IRPhiNode phi_node = ((IRPhiNode) node);
            // for (int i = 0; i != phi_node.vals.length; ++i) {
            // if (replace.containsKey(phi_node.vals[i])) {
            // phi_node.vals[i] = replace.get(phi_node.vals[i]);
            // }
            // }
            // }
        }

        // System.out.println("D");

        // recursive
        for (

        BasicBlockNode son : bb.dom_tree_son) {
            collectRenames(alloca_name, stack, son, replace);
        }

        // pop stack
        for (int i = 0; i != stack_count; ++i) {
            stack.pop();
        }

    }

    public void eliminatePhi() {
        // eliminate critical edge
        ArrayList<Pair<String, BasicBlockNode>> new_bbs = new ArrayList<>();
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();

            if (bb.successors.size() <= 1) {
                continue;
            }

            for (BasicBlockNode succ : bb.successors) {
                if (succ.precursors.size() <= 1) {
                    continue;
                }
                if (!(succ.head.next instanceof IRPhiNode)) {
                    continue;
                }

                // create empty bb and insert the commands into the linked list
                BasicBlockNode new_bb = new BasicBlockNode();

                IRLabelNode label_node = new IRLabelNode();
                label_node.label = bbLabel();
                new_bb.label = label_node.label;
                new_bbs.add(new Pair<>(new_bb.label, new_bb));

                IRBrNode br_node = new IRBrNode();
                br_node.label_true = succ.label;
                label_node.next = br_node;
                br_node.prev = label_node;

                new_bb.head = label_node;
                new_bb.tail = br_node;

                succ.head.prev.next = new_bb.head;
                new_bb.head.prev = succ.head.prev;
                succ.head.prev = new_bb.tail;
                new_bb.tail.next = succ.head;

                // replace bb with new_bb in phi_node and precs of succ
                succ.precursors.remove(bb);
                succ.precursors.add(new_bb);

                for (IRNode node = succ.head.next;; node = node.next) {
                    if (node instanceof IRPhiNode) {
                        for (int i = 0; i != ((IRPhiNode) node).labels.length; ++i) {
                            if (((IRPhiNode) node).labels[i].equals(bb.label)) {
                                ((IRPhiNode) node).labels[i] = new_bb.label;
                            }
                        }
                    }

                    if (node == succ.tail) {
                        break;
                    }
                }

                // replace succ with new_bb in succs and br_node of bb
                bb.successors.remove(succ);
                bb.successors.add(new_bb);

                if (bb.tail instanceof IRBrNode) {
                    IRBrNode tail_node = ((IRBrNode) bb.tail);
                    if (tail_node.label_true.equals(succ.label)) {
                        tail_node.label_true = new_bb.label;
                    }
                    if (tail_node.label_false != null && tail_node.label_false.equals(succ.label)) {
                        tail_node.label_false = new_bb.label;
                    }
                }
            }
        }

        // insert new bbs into set
        for (Pair<String, BasicBlockNode> pair : new_bbs) {
            bbs.put(pair.first, pair.second);
        }

        System.out.println("; ECE");
        // printIR();

        // replace phi with mv
        for (IRNode cur = ir_beg; cur != null; cur = cur.next) {
            if (cur instanceof IRDefFuncNode) {
                for (IRNode node = ((IRDefFuncNode) cur).stmt; node != null; node = node.next) {
                    if (node instanceof IRPhiNode) {
                        IRPhiNode phi_node = ((IRPhiNode) node);
                        for (int i = 0; i != phi_node.labels.length; ++i) {
                            // find the reserve bb
                            BasicBlockNode reserve_bb = bbs.get(phi_node.labels[i]);

                            // insert mv_node
                            IRMvNode mv_node = new IRMvNode();
                            mv_node.result = phi_node.result;
                            mv_node.op2 = phi_node.vals[i];
                            mv_node.tp = phi_node.tp;
                            mv_node.op1 = "0";
                            mv_node.operator = "add";

                            reserve_bb.tail.prev.next = mv_node;
                            mv_node.prev = reserve_bb.tail.prev;
                            reserve_bb.tail.prev = mv_node;
                            mv_node.next = reserve_bb.tail;
                        }

                        // delete phi_node
                        phi_node.eliminated = true;
                    }
                }
            }
        }

        System.out.println("; EPHI");
        // printIR();
    }

    public void deleteEliminated() {
        for (IRNode cur = ir_beg; cur != null; cur = cur.next) {
            if (cur instanceof IRDefFuncNode) {
                for (IRNode node = ((IRDefFuncNode) cur).stmt; node != null; node = node.next) {
                    if (node instanceof IRAllocaNode && ((IRAllocaNode) node).eliminated) {
                        if (node.prev == cur) {
                            ((IRDefFuncNode) cur).stmt = node.next;
                        } else {
                            node.prev.next = node.next;
                        }
                        if (node.next != null) {
                            node.next.prev = node.prev;
                        }

                    } else if (node instanceof IRLoadNode && ((IRLoadNode) node).eliminated) {
                        if (node.prev == cur) {
                            ((IRDefFuncNode) cur).stmt = node.next;
                        } else {
                            node.prev.next = node.next;
                        }
                        if (node.next != null) {
                            node.next.prev = node.prev;
                        }

                    } else if (node instanceof IRStoreNode && ((IRStoreNode) node).eliminated) {
                        if (node.prev == cur) {
                            ((IRDefFuncNode) cur).stmt = node.next;
                        } else {
                            node.prev.next = node.next;
                        }
                        if (node.next != null) {
                            node.next.prev = node.prev;
                        }

                    } else if (node instanceof IRPhiNode && ((IRPhiNode) node).eliminated) {
                        if (node.prev == cur) {
                            ((IRDefFuncNode) cur).stmt = node.next;
                        } else {
                            node.prev.next = node.next;
                        }
                        if (node.next != null) {
                            node.next.prev = node.prev;
                        }
                    }
                }
            }
        }
    }

    public void dfs(BasicBlockNode node, ArrayList<BasicBlockNode> order) {
        for (BasicBlockNode succ : node.successors) {
            dfs(succ, order);
        }

        order.add(node);
    }

    public void linearScan() {

        for (BasicBlockNode entry : entries) {
            ArrayList<BasicBlockNode> linear_order = new ArrayList<>();
            dfs(entry, linear_order);
            int comm_order = 0;
            for (int i = linear_order.size() - 1; i >= 0; --i) {

                BasicBlockNode node = linear_order.get(i);
                for (IRNode ir_node = node.head;; ir_node = ir_node.next) {
                    ir_node.order = comm_order++;
                    if (ir_node == node.tail) {
                        break;
                    }
                }

            }
        }
    }

    // TODO

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

}
