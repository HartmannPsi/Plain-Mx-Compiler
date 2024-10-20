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
import java.util.PriorityQueue;
import util.Pair;

public class IROptimizer {
    public IRNode ir_beg = null;
    // BasicBlockNode dom_tree_root = null;
    Map<String, BasicBlockNode> bbs = new HashMap<>();
    // ArrayList<Pair<IRStoreNode, BasicBlockNode>> def_in_bbs = new ArrayList<>();
    Map<String, ArrayList<Pair<IRStoreNode, BasicBlockNode>>> def_in_bbs = new HashMap<>();
    Map<String, IRAllocaNode> allocas = new HashMap<>();
    ArrayList<BasicBlockNode> entries = new ArrayList<>();
    Map<BasicBlockNode, ArrayList<IRNode>> comm_orders = new HashMap<>();
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
                // System.out.println(node.toString());
                if (node.def() != null) {
                    bb.def.add(node.def());
                }
                if (node.use() != null) {
                    for (String use : node.use()) {
                        if (use != null) {
                            bb.use.add(use);
                        }
                    }
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
            // System.out.println(bb.label);
            bb.in = new HashSet<>(bb.out);
            bb.in.removeAll(bb.def);
            bb.in.addAll(bb.use);

            for (BasicBlockNode prec : bb.precursors) {
                if (!prec.out.containsAll(bb.in)) {// out[prec] != \\union in[bb]
                    prec.out.addAll(bb.in);
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
                    for (String use : node.use()) {
                        if (use != null) {
                            last_out.add(use);
                        }
                    }
                    // last_out.add(node.use());
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

    public void printInOut() {
        System.out.println("\nInOut:");
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            System.out.println("BB " + bb.label + ":");
            System.out.print("  in: ");
            for (String in : bb.in) {
                System.out.print(" " + in);
            }
            System.out.print("\n  out: ");
            for (String out : bb.out) {
                System.out.print(" " + out);
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

            // System.out.println(bb.label);

            if (bb.precursors.isEmpty() || bb.dominates.size() == 1) {
                continue;
            }

            Queue<BasicBlockNode> queue = new LinkedList<>();
            Set<BasicBlockNode> visited = new HashSet<>();
            for (BasicBlockNode prec : bb.precursors) {
                queue.add(prec);
                visited.add(prec);
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
                    if (!visited.contains(prec)) {
                        visited.add(prec);
                        queue.add(prec);
                    }
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

    public void sortPhi() {
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            if (bb.dom_frontier.isEmpty()) {
                continue;
            }

            Map<String, Integer> order = new HashMap<>();
            int ser = 0;

            // get the order of phis
            for (IRNode node = bb.head;; node = node.next) {
                if (node instanceof IRStoreNode) {
                    IRStoreNode store_node = ((IRStoreNode) node);
                    if (!order.containsKey(store_node.ptr)) {
                        order.put(store_node.ptr, ser++);
                    }
                }
                if (node == bb.tail)
                    break;
            }

            // if (!order.isEmpty()) {
            // for (Map.Entry<String, Integer> entry2 : order.entrySet()) {
            // System.out.println(entry2.getKey() + " " + entry2.getValue());
            // }
            // }

            // init
            Queue<BasicBlockNode> queue = new LinkedList<>();
            Set<BasicBlockNode> visited = new HashSet<>();
            for (BasicBlockNode frontier : bb.dom_frontier) {
                queue.add(frontier);
                visited.add(frontier);
            }

            while (!queue.isEmpty()) {
                BasicBlockNode frontier = queue.poll();
                // System.out.println(frontier.label);
                int phi_cnt = 0;

                // coutn phis & get the order
                for (IRNode node = frontier.head.next; node instanceof IRPhiNode; node = node.next) {
                    ++phi_cnt;
                    // if (!order.containsKey(((IRPhiNode) node).result)) {
                    // order.put(((IRPhiNode) node).result, ser);
                    // }
                }

                // sort
                for (int i = 0; i != phi_cnt; ++i) {
                    IRNode node = (IRPhiNode) frontier.head.next;
                    for (int j = 0; j != phi_cnt - 1; ++j) {
                        String sub = ".Replace.";
                        String key1 = new String(((IRPhiNode) node).result),
                                key2 = new String(((IRPhiNode) node.next).result);
                        int tmp1 = key1.indexOf(sub), tmp2 = key2.indexOf(sub);
                        key1 = key1.substring(0, tmp1);
                        key2 = key2.substring(0, tmp2);
                        int idx1 = ser;
                        if (order.containsKey(key1)) {
                            idx1 = order.get(key1);
                        }
                        int idx2 = ser;
                        if (order.containsKey(key2)) {
                            idx2 = order.get(key2);
                        }
                        // System.out.println(key1 + "=" + idx1 + " " + key2 + "=" + idx2);

                        if (idx1 > idx2) {
                            // System.out.print("SWAP");
                            // swap
                            IRPhiNode phi1 = (IRPhiNode) node, phi2 = (IRPhiNode) node.next, phi3 = new IRPhiNode();
                            phi3.result = phi1.result;
                            phi1.result = phi2.result;
                            phi2.result = phi3.result;

                            phi3.tp = phi1.tp;
                            phi1.tp = phi2.tp;
                            phi2.tp = phi3.tp;

                            phi3.vals = phi1.vals;
                            phi1.vals = phi2.vals;
                            phi2.vals = phi3.vals;

                            phi3.labels = phi1.labels;
                            phi1.labels = phi2.labels;
                            phi2.labels = phi3.labels;

                            phi3.eliminated = phi1.eliminated;
                            phi1.eliminated = phi2.eliminated;
                            phi2.eliminated = phi3.eliminated;
                        }
                        node = node.next;
                    }
                }

                // add the frontiers
                for (BasicBlockNode tmp : frontier.dom_frontier) {
                    if (!visited.contains(tmp)) {
                        queue.add(tmp);
                        visited.add(tmp);
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

    public void eliminateCE() {
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

        // System.out.println("; ECE");
        // printIR();

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

        // System.out.println("; ECE");
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

        // System.out.println("; EPHI");
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
                    } else if (node.getClass() == IRNode.class) {
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

    ArrayList<BasicBlockNode> sortSucc(ArrayList<BasicBlockNode> order) {
        if (order.size() <= 1) {
            return order;

        } else if (order.size() == 2) {

            ArrayList<BasicBlockNode> sorted_order = new ArrayList<>(order);
            BasicBlockNode succ0 = order.get(0), succ1 = order.get(1);

            if ((succ0.label.contains("Body") && succ1.label.contains("End")) || succ1.label.contains("Entry.Rhs")) {
                sorted_order.set(0, succ1);
                sorted_order.set(1, succ0);
                return sorted_order;

            } else {
                return order;
            }

        } else {
            throw new RuntimeException("Error: illegal size of successors");
        }
    }

    public void getOrder(BasicBlockNode node, ArrayList<BasicBlockNode> order, Set<BasicBlockNode> visited) {
        // System.out.println(node.label);
        visited.add(node);
        for (BasicBlockNode succ : sortSucc(node.successors)) {
            if (!visited.contains(succ)) {
                getOrder(succ, order, visited);
            }
            // getOrder(succ, order);
        }

        order.add(node);
    }

    String regName(int i) {
        if (i < 12) {
            return "s" + i;
        } else if (i < 16) {
            return "t" + (i - 9);
        } else if (i < 24) {
            return "a" + (i - 16);
        } else if (i == 24) {
            return "gp";
        } else if (i == 25) {
            return "tp";
        } else {
            throw new RuntimeException("Error: illegal register index");
        }
    }

    public void getLinearOrder() {
        for (BasicBlockNode entry : entries) {
            // get linear order of commands
            ArrayList<BasicBlockNode> bb_order_rev = new ArrayList<>();

            comm_orders.put(entry, new ArrayList<>());
            ArrayList<IRNode> comm_order = comm_orders.get(entry);

            Set<BasicBlockNode> visited_bb = new HashSet<>();
            getOrder(entry, bb_order_rev, visited_bb);
            int comm_ser = 0;
            for (int i = bb_order_rev.size() - 1; i >= 0; --i) {

                BasicBlockNode node = bb_order_rev.get(i);
                for (IRNode ir_node = node.head;; ir_node = ir_node.next) {
                    ir_node.order = comm_ser++;
                    comm_order.add(ir_node);
                    if (ir_node == node.tail) {
                        break;
                    }
                }

            }
        }

        // get linear order of mv nodes
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            for (IRNode node = bb.head; node != null; node = node.next) {

                if (node instanceof IRMvNode) {
                    IRMvNode mv_node = ((IRMvNode) node);

                    if (!IRNode.var_def.containsKey(mv_node.result)) {
                        IRNode.var_def.put(mv_node.result, mv_node.order);

                    } else if (IRNode.var_def.get(mv_node.result) > mv_node.order) {
                        IRNode.var_def.put(mv_node.result, mv_node.order);
                    }
                }

                if (node == bb.tail) {
                    break;
                }
            }
        }
    }

    public Map<String, String> linearScan() {

        // ret value
        Map<String, String> var_state = new HashMap<>();// <var, reg / mem>

        // consider every functions independently
        for (BasicBlockNode entry : entries) {

            ArrayList<IRNode> comm_order = comm_orders.get(entry);

            // get linear order of commands
            // ArrayList<BasicBlockNode> bb_order_rev = new ArrayList<>();
            // ArrayList<IRNode> comm_order = new ArrayList<>();
            // Set<BasicBlockNode> visited_bb = new HashSet<>();
            // getOrder(entry, bb_order_rev, visited_bb);
            // int comm_ser = 0;
            // for (int i = bb_order_rev.size() - 1; i >= 0; --i) {

            // BasicBlockNode node = bb_order_rev.get(i);
            // for (IRNode ir_node = node.head;; ir_node = ir_node.next) {
            // ir_node.order = comm_ser++;
            // comm_order.add(ir_node);
            // if (ir_node == node.tail) {
            // break;
            // }
            // }

            // }

            // print order of bbs
            // System.out.println("; Func " + entry.label + ":\n; BB order:");
            // for (int i = bb_order_rev.size() - 1; i >= 0; --i) {
            // BasicBlockNode node = bb_order_rev.get(i);
            // System.out.println(
            // "; " + (bb_order_rev.size() - 1 - i) + ": " + node.label + " [" +
            // node.head.order + ", "
            // + node.tail.order + "]");
            // }

            // get the sorted life range of variables
            Map<String, Integer> life_beg = new HashMap<>(), life_end = new HashMap<>();
            Set<String> active = new HashSet<>();
            PriorityQueue<VarLifeRange> vars = new PriorityQueue<>();
            ArrayList<VarLifeRange> vars2 = new ArrayList<>();// just for debugging

            for (IRNode node : comm_order) {
                if (node.def() != null) {
                    if (!life_beg.containsKey(node.def())) {
                        life_beg.put(node.def(), node.order);
                    } else if (life_beg.get(node.def()) > node.order) {
                        life_beg.put(node.def(), node.order);
                    }
                    active.add(node.def());
                }
                active.addAll(node.in);

                Set<String> dead_vars = new HashSet<>(active);
                dead_vars.removeAll(node.out);

                for (String dead_var : dead_vars) {
                    active.remove(dead_var);
                    if (!life_end.containsKey(dead_var)) {
                        life_end.put(dead_var, node.order);
                    } else if (life_end.get(dead_var) < node.order) {
                        life_end.put(dead_var, node.order);
                    }
                    // vars.add(new VarLifeRange(dead_var, life_beg.get(dead_var), node.order));
                }
            }

            for (String var : active) {
                if (!life_end.containsKey(var)) {
                    life_end.put(var, comm_order.getLast().order);
                } else if (life_end.get(var) < comm_order.getLast().order) {
                    life_end.put(var, comm_order.getLast().order);
                }
            }

            // for (int i = 0; i != comm_order.size(); ++i) {
            // IRNode node = comm_order.get(i);
            // if (node.def() != null) {
            // if (!life_beg.containsKey(node.def())) {
            // life_beg.put(node.def(), node.order);
            // life_end.put(node.def(), node.order);
            // } else if (life_beg.get(node.def()) > node.order) {
            // life_beg.put(node.def(), node.order);
            // } else if (life_end.get(node.def()) < node.order) {
            // life_end.put(node.def(), node.order);
            // }
            // }

            // if (node.use() != null) {
            // for (String use : node.use()) {
            // life_end.put(use, node.order);
            // }
            // }
            // }

            for (Map.Entry<String, Integer> var_beg : life_beg.entrySet()) {
                vars.add(new VarLifeRange(var_beg.getKey(), var_beg.getValue(), life_end.get(var_beg.getKey())));
                vars2.add(new VarLifeRange(var_beg.getKey(), var_beg.getValue(),
                        life_end.get(var_beg.getKey())));
            }

            // print life range of variables
            // System.out.println("; var life:");
            // for (VarLifeRange var : vars2) {
            // System.out.println("; " + var.name + ": [" + var.beg + ", " + var.end + "]");
            // }

            // allocate registers
            // Map<String, String> var_state = new HashMap<>();// <var, reg / mem>
            int[] reg_state = new int[26];// 26 available: s0 - s11, t3 - t6, a0 - a7, gp, tp
            for (int i = 0; i != 26; ++i) {
                reg_state[i] = 0;
            }

            while (!vars.isEmpty()) {
                VarLifeRange var = vars.poll();
                int i = 0;
                for (; i != 26; ++i) {
                    if (reg_state[i] <= var.beg) {
                        break;
                    }
                }

                if (i == 26) {// spill to stack
                    var_state.put(var.name, "SPILL");
                } else {// allocate register
                    reg_state[i] = var.end;
                    var_state.put(var.name, regName(i));
                }
            }

        }

        // print allocation
        // System.out.println("; Allocation:");
        // for (Map.Entry<String, String> entry2 : var_state.entrySet()) {
        // System.out.println("; " + entry2.getKey() + ": " + entry2.getValue());
        // }

        return var_state;
    }

    public void deleteDeadVar() {

        // build the var_uses graph
        Map<String, VarUses> var_uses = new HashMap<>();
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            for (IRNode node = bb.head; node != null; node = node.next) {

                if (node.def() != null) {
                    if (!var_uses.containsKey(node.def())) {
                        var_uses.put(node.def(), new VarUses());
                    }
                    var_uses.get(node.def()).def = node;
                }

                if (node.use() != null) {
                    for (String use : node.use()) {
                        if (use != null) {
                            if (!var_uses.containsKey(use)) {
                                var_uses.put(use, new VarUses());
                            }
                            var_uses.get(use).uses.add(node);
                        }
                    }
                }

                if (node == bb.tail) {
                    break;
                }
            }
        }

        // print var_uses
        // System.out.println("; Var uses:");
        // for (Map.Entry<String, VarUses> entry : var_uses.entrySet()) {
        // System.out.print("; " + entry.getKey() + " def: ");
        // if (entry.getValue().def != null) {
        // System.out.print(entry.getValue().def.toString());
        // }
        // System.out.print("\nuses: ");
        // for (IRNode node : entry.getValue().uses) {
        // System.out.print(node.toString() + " ");
        // }
        // System.out.println();
        // }

        // find the dead variables
        Queue<String> dead_vars = new LinkedList<>();
        for (Map.Entry<String, VarUses> entry : var_uses.entrySet()) {
            if (entry.getValue().uses.isEmpty()) {
                dead_vars.add(entry.getKey());
            }
        }

        // delete the dead variables
        while (!dead_vars.isEmpty()) {
            String var_name = dead_vars.poll();
            VarUses var_use = var_uses.get(var_name);
            IRNode def_node = var_use.def;

            // fail to delete if the def_node is call / store / null
            if (def_node == null || (def_node instanceof IRCallNode) || (def_node instanceof IRStoreNode)) {
                continue;
            }

            // remove def_node in other vars' uses
            if (def_node.use() != null) {
                for (String used_var : def_node.use()) {

                    if (used_var != null && var_uses.containsKey(used_var)) {
                        VarUses used_var_uses = var_uses.get(used_var);
                        used_var_uses.uses.remove(def_node);

                        if (used_var_uses.uses.isEmpty()) {
                            dead_vars.add(used_var);
                        }
                    }
                }
            }

            // delete def_node
            if (def_node.prev instanceof IRDefFuncNode) {
                ((IRDefFuncNode) def_node.prev).stmt = def_node.next;
            } else {
                def_node.prev.next = def_node.next;
            }
            if (def_node.next != null) {
                def_node.next.prev = def_node.prev;
            }

            // delete dead_var
            var_uses.remove(var_name);

            // System.out.println("Delete " + var_name);
        }
    }

    public void insertPseudoArgs() {
        for (IRNode cur = ir_beg; cur != null; cur = cur.next) {
            if (cur instanceof IRDefFuncNode) {
                IRDefFuncNode def_node = ((IRDefFuncNode) cur);
                if (def_node.ids == null || def_node.ids.length == 0) {
                    continue;
                }

                // generate pseudo arg comms
                IRNode head = new IRNode(), tail = head;
                for (int i = 0; i != def_node.ids.length; ++i) {
                    IRPseudoArgNode pseudo_node = new IRPseudoArgNode();
                    pseudo_node.pseudo_def = def_node.ids[i];
                    pseudo_node.def_args = def_node;
                    pseudo_node.idx = i;
                    tail.next = pseudo_node;
                    pseudo_node.prev = tail;
                    tail = pseudo_node;
                }
                head = head.next;

                // insert comms into Func Entry Label
                IRNode entry_label_node = def_node.stmt;
                entry_label_node.next.prev = tail;
                tail.next = entry_label_node.next;
                entry_label_node.next = head;
                head.prev = entry_label_node;
            }
        }
    }

}
