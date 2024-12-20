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
import java.util.BitSet;

//TODO：optimize calc cfg with bitset
//TODO: optimize active analysis with bitset
//TODO: build function call graph

public class IROptimizer {
    public IRNode ir_beg = null;
    // BasicBlockNode dom_tree_root = null;
    Map<String, BasicBlockNode> bbs = new HashMap<>();
    Map<BasicBlockNode, Integer> bb_to_num = new HashMap<>();
    Map<Integer, BasicBlockNode> num_to_bb = new HashMap<>();
    int bb_idx = 0;
    // ArrayList<Pair<IRStoreNode, BasicBlockNode>> def_in_bbs = new ArrayList<>();
    Map<String, ArrayList<Pair<IRStoreNode, BasicBlockNode>>> def_in_bbs = new HashMap<>();
    Map<String, IRAllocaNode> allocas = new HashMap<>();
    ArrayList<BasicBlockNode> entries = new ArrayList<>();
    Map<BasicBlockNode, ArrayList<IRNode>> comm_orders = new HashMap<>();
    int rename_serial = 0;

    Map<Integer, String> num_to_var = new HashMap<>();
    Map<String, Integer> var_to_num = new HashMap<>();
    // int var_idx = 0;

    Map<String, GlbVarUsage> glb_var_usage = new HashMap<>();
    IRDefFuncNode glb_var_init = null;

    Map<String, IRDefFuncNode> funcs = new HashMap<>();

    int MAX_INLINE_SCALE = 512;
    int bb_cnt = 0;

    String renameAlloca(String obj) {
        return obj + ".Replace." + rename_serial++;
    }

    String renameLocalize(String obj) {
        return "%" + obj.substring(1, obj.length()) + ".Localize." + rename_serial++;
    }

    String renameInline(String obj) {
        if (isLocal(obj))
            return obj + ".Inline." + rename_serial++;
        else
            return obj;
    }

    String renameInlineLabel(String obj) {
        return obj + ".Inline." + rename_serial++;
    }

    boolean isLocal(String obj) {
        return obj.charAt(0) == '%';
    }

    String bbLabel() {
        return "Label.eli_CE." + rename_serial++;
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
                        bb_to_num.put(bb, bb_idx);
                        num_to_bb.put(bb_idx++, bb);

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

                // } else {
                // instanceof IRRetNode
            }
        }

        // System.out.println("CCCC");

        // set dom set to the whole set of bbs
        // Set<BasicBlockNode> all_bbs = new HashSet<>(bbs.values());
        BitSet all_bbs = new BitSet();
        all_bbs.set(0, bb_idx);
        Queue<BasicBlockNode> useless_bb = new LinkedList<>();
        for (BasicBlockNode bb : bbs.values()) {
            bb.dominates = all_bbs;
            if (bb.precursors.isEmpty()) {
                if (bb.isBegOfFunc()) {
                    // begin of function
                    entries.add(bb);
                } else {
                    // useless block
                    useless_bb.add(bb);
                }
            }
        }

        // delete useless bbs
        while (!useless_bb.isEmpty()) {
            BasicBlockNode bb = useless_bb.poll();

            // delete from bbs
            bbs.remove(bb.label);

            // delete from ir
            bb.head.prev.next = bb.tail.next;
            if (bb.tail.next != null) {
                bb.tail.next.prev = bb.head.prev;
            }

            for (BasicBlockNode succ : bb.successors) {
                succ.precursors.remove(bb);
                if (succ.precursors.isEmpty()) {
                    useless_bb.add(succ);
                }
            }
        }

        BasicBlockNode.num_to_bb = num_to_bb;

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

            // System.out.println("BB: " + bb.label);

            for (IRNode node = bb.head;; node = node.next) {

                // System.out.println(node.toString());

                if (node.def() != null) {
                    // bb.def.add(node.def());

                    // if (!var_to_num.containsKey(node.def())) {
                    // System.out.println("NOT FOUND: " + node.def());
                    // System.exit(1);
                    // }

                    int def_idx = var_to_num.get(node.def());
                    bb.bdef.set(def_idx);
                }
                if (node.use() != null) {
                    for (String use : node.use()) {
                        if (use != null) {
                            // bb.use.add(use);
                            // System.out.println(node.toString() + " Use: " + use);

                            // if (!var_to_num.containsKey(use)) {
                            // System.out.println("NOT FOUND: " + use);
                            // System.exit(1);
                            // }

                            int use_idx = var_to_num.get(use);
                            bb.buse.set(use_idx);
                        }
                    }
                }
                if (node == bb.tail)
                    break;
            }

            // bb.use.removeAll(bb.def);
            bb.buse.andNot(bb.bdef);
            queue.add(bb);
        }

        // calc in / out of bb

        while (!queue.isEmpty()) {
            BasicBlockNode bb = queue.poll();
            // System.out.println(bb.label);
            // bb.in = new HashSet<>(bb.out);
            bb.bin = (BitSet) bb.bout.clone();
            // bb.in.removeAll(bb.def);
            bb.bin.andNot(bb.bdef);
            // bb.in.addAll(bb.use);
            bb.bin.or(bb.buse);

            for (BasicBlockNode prec : bb.precursors) {
                // if (!prec.out.containsAll(bb.in)) {// out[prec] != \\union in[bb]
                // prec.out.addAll(bb.in);
                // queue.add(prec);
                // }
                BitSet tmp = (BitSet) prec.bout.clone();// tmp = prec.bout
                tmp.and(bb.bin);// tmp = prec.bout & bb.bin
                // if bb.bin \subset prec.bout then tmp == bb.bin
                if (!tmp.equals(bb.bin)) {
                    prec.bout.or(bb.bin);
                    queue.add(prec);
                }
            }
        }

        // calc bin / bout of ir
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

            // Set<String> last_out = new HashSet<>(bb.out);
            BitSet last_out = (BitSet) bb.bout.clone();

            for (IRNode node = bb.tail;; node = node.prev) {
                // IRNode node = list.get(i);
                // node.out = new HashSet<>(last_out);
                node.bout = (BitSet) last_out.clone();

                if (node.def() != null) {
                    // last_out.remove(node.def());

                    // if (!var_to_num.containsKey(node.def())) {
                    // System.out.println("NOT FOUND: " + node.def());
                    // System.exit(1);
                    // }

                    int def_idx = var_to_num.get(node.def());
                    last_out.clear(def_idx);
                }
                if (node.use() != null) {
                    for (String use : node.use()) {
                        if (use != null) {
                            // last_out.add(use);

                            // if (!var_to_num.containsKey(use)) {
                            // System.out.println("NOT FOUND: " + use);
                            // System.exit(1);
                            // }

                            int use_idx = var_to_num.get(use);
                            last_out.set(use_idx);
                        }
                    }
                    // last_out.add(node.use());
                }
                // node.in = new HashSet<>(last_out);
                node.bin = (BitSet) last_out.clone();

                if (node == bb.head)
                    break;
            }
        }

        // calc in / out of ir

        IRNode.num_to_var = num_to_var;
        BasicBlockNode.num_to_var = num_to_var;

        // for (ArrayList<IRNode> comm_order : comm_orders.values()) {
        // for (IRNode node : comm_order) {
        // node.calcInOut();
        // }
        // }
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
            for (BasicBlockNode dom : bb.dominates()) {
                System.out.print(dom.label + " ");
            }
            System.out.println("\n" + bb.dominates.cardinality() + "\n");
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
            for (String in : bb.in()) {
                System.out.print(" " + in);
            }
            System.out.print("\n  out: ");
            for (String out : bb.out()) {
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
            entry.dominates = new BitSet();
            int entry_idx = bb_to_num.get(entry);
            entry.dominates.set(entry_idx);
            Queue<BasicBlockNode> queue = new LinkedList<>();
            queue.add(entry);

            while (!queue.isEmpty()) {
                BasicBlockNode bb = queue.poll();

                for (BasicBlockNode succ : bb.successors) {

                    // Set<BasicBlockNode> tmp = new HashSet<>(bb.dominates);
                    // tmp.addAll(bb.dominates);
                    BitSet tmp = (BitSet) bb.dominates.clone();

                    for (BasicBlockNode prec : succ.precursors) {
                        if (prec == bb) {
                            continue;
                        }
                        tmp.and(prec.dominates);
                    }
                    int succ_idx = bb_to_num.get(succ);
                    tmp.set(succ_idx);

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

            if (bb.precursors.isEmpty() || bb.dominates.cardinality() == 1) {
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

                if (tmp.dominates.cardinality() == bb.dominates.cardinality() - 1) {
                    // Set<BasicBlockNode> set = new HashSet<>(bb.dominates);
                    BitSet set = (BitSet) bb.dominates.clone();
                    // set.removeAll(tmp.dominates);
                    set.andNot(tmp.dominates);
                    if (set.cardinality() == 1 && set.get(bb_to_num.get(bb))) {
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

            // Set<BasicBlockNode> tmp = new HashSet<>(bb.dominates), set = new HashSet<>();
            BitSet tmp = (BitSet) bb.dominates.clone(), set = new BitSet();
            // tmp.remove(bb);
            int bb_idx = bb_to_num.get(bb);
            tmp.clear(bb_idx);

            for (BasicBlockNode prec : bb.precursors) {
                // Set<BasicBlockNode> tmp2 = new HashSet<>(prec.dominates);
                BitSet tmp2 = (BitSet) prec.dominates.clone();
                // tmp2.removeAll(tmp);
                tmp2.andNot(tmp);
                // set.addAll(tmp2);
                set.or(tmp2);
            }

            // for (BasicBlockNode node : set) {
            // node.dom_frontier.add(bb);
            // }
            for (int i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i + 1)) {
                BasicBlockNode node = num_to_bb.get(i);
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
                    if (node.eliminated) {
                        if (node.prev == cur) {
                            ((IRDefFuncNode) cur).stmt = node.next;
                        } else {
                            node.prev.next = node.next;
                        }
                        if (node.next != null) {
                            node.next.prev = node.prev;
                        }
                        // } else if (node instanceof IRLoadNode && ((IRLoadNode) node).eliminated) {
                        // if (node.prev == cur) {
                        // ((IRDefFuncNode) cur).stmt = node.next;
                        // } else {
                        // node.prev.next = node.next;
                        // }
                        // if (node.next != null) {
                        // node.next.prev = node.prev;
                        // }

                        // } else if (node instanceof IRStoreNode && ((IRStoreNode) node).eliminated) {
                        // if (node.prev == cur) {
                        // ((IRDefFuncNode) cur).stmt = node.next;
                        // } else {
                        // node.prev.next = node.next;
                        // }
                        // if (node.next != null) {
                        // node.next.prev = node.prev;
                        // }

                        // } else if (node instanceof IRPhiNode && ((IRPhiNode) node).eliminated) {
                        // if (node.prev == cur) {
                        // ((IRDefFuncNode) cur).stmt = node.next;
                        // } else {
                        // node.prev.next = node.next;
                        // }
                        // if (node.next != null) {
                        // node.next.prev = node.prev;
                        // }
                        // } else if (node.getClass() == IRNode.class) {
                        // if (node.prev == cur) {
                        // ((IRDefFuncNode) cur).stmt = node.next;
                        // } else {
                        // node.prev.next = node.next;
                        // }
                        // if (node.next != null) {
                        // node.next.prev = node.prev;
                        // }
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
        if (i < 8) {
            return "a" + i;// a0 - a7
        } else if (i < 12) {
            return "t" + (i - 5);// t3 - t6
        } else if (i < 24) {
            return "s" + (i - 12);// s0 - s11
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

    public void numVars() {
        // for (ArrayList<IRNode> comm_order : comm_orders.values()) {
        // for (IRNode node : comm_order) {
        // String var = node.def();
        // if (var != null && !var_to_num.containsKey(var)) {
        // int var_idx = var_to_num.size();
        // var_to_num.put(var, var_idx);
        // num_to_var.put(var_idx, var);
        // }
        // }
        // }

        for (IRNode cur = ir_beg; cur != null; cur = cur.next) {
            if (cur instanceof IRDefFuncNode) {
                IRDefFuncNode def_func_node = ((IRDefFuncNode) cur);
                for (IRNode node = def_func_node.stmt; node != null; node = node.next) {
                    String var = node.def();
                    if (var != null && !var_to_num.containsKey(var)) {
                        int var_idx = var_to_num.size();
                        var_to_num.put(var, var_idx);
                        num_to_var.put(var_idx, var);
                    }
                }
            }

        }

        // print var to num
        // System.out.println("Var to Num:");
        // for (Map.Entry<String, Integer> entry : var_to_num.entrySet()) {
        // System.out.println(entry.getKey() + ": " + entry.getValue());
        // }
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
            BitSet active = new BitSet();
            // Set<String> active = new HashSet<>();
            PriorityQueue<VarLifeRange> vars = new PriorityQueue<>();
            // ArrayList<VarLifeRange> vars2 = new ArrayList<>();// just for debugging
            Map<String, Integer> func_args = new HashMap<>();

            for (IRNode node : comm_order) {

                if (node instanceof IRPseudoArgNode) {
                    func_args.put(((IRPseudoArgNode) node).pseudo_def, ((IRPseudoArgNode) node).idx);
                }

                if (node.def() != null) {
                    if (!life_beg.containsKey(node.def())) {
                        life_beg.put(node.def(), node.order);
                    } else if (life_beg.get(node.def()) > node.order) {
                        life_beg.put(node.def(), node.order);
                    }
                    // active.add(node.def());
                    int def_idx = var_to_num.get(node.def());
                    active.set(def_idx);
                }
                // active.addAll(node.in());
                active.or(node.bin);

                // Set<String> dead_vars = new HashSet<>(active);
                BitSet dead_vars = (BitSet) active.clone();
                // dead_vars.removeAll(node.out());
                dead_vars.andNot(node.bout);

                // for (String dead_var : dead_vars) {
                for (int dead_idx = dead_vars.nextSetBit(0); dead_idx >= 0; dead_idx = dead_vars
                        .nextSetBit(dead_idx + 1)) {

                    // active.remove(dead_var);
                    active.clear(dead_idx);
                    String dead_var = num_to_var.get(dead_idx);

                    if (!life_end.containsKey(dead_var)) {
                        life_end.put(dead_var, node.order);
                    } else if (life_end.get(dead_var) < node.order) {
                        life_end.put(dead_var, node.order);
                    }
                    // vars.add(new VarLifeRange(dead_var, life_beg.get(dead_var), node.order));
                }
            }

            // for (String var : active) {
            for (int var_idx = active.nextSetBit(0); var_idx >= 0; var_idx = active.nextSetBit(var_idx + 1)) {

                String var = num_to_var.get(var_idx);
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

            int[] reg_state = new int[26];// 26 available: s0 - s11, t3 - t6, a0 - a7, gp, tp
            for (int i = 0; i != 26; ++i) {
                reg_state[i] = 0;
            }

            for (Map.Entry<String, Integer> var_beg : life_beg.entrySet()) {
                String var = var_beg.getKey();
                int t1 = var_beg.getValue(), t2 = life_end.get(var_beg.getKey());

                if (func_args.containsKey(var) && func_args.get(var) < 8) {
                    // prior to allocate args into a0 - a7
                    reg_state[func_args.get(var)] = t2;
                    var_state.put(var, regName(func_args.get(var)));

                } else {
                    vars.add(new VarLifeRange(var, t1, t2));
                }

                // vars2.add(new VarLifeRange(var_beg.getKey(), var_beg.getValue(),
                // life_end.get(var_beg.getKey())));
            }

            // print life range of variables
            // System.out.println("; var life:");
            // for (VarLifeRange var : vars2) {
            // System.out.println("; " + var.name + ": [" + var.beg + ", " + var.end + "]");
            // }

            // allocate registers
            // Map<String, String> var_state = new HashMap<>();// <var, reg / mem>

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

    public void deadCodeEliminate() {

        // build the var_uses graph & collect the critical comms
        Map<String, VarUses> var_uses = new HashMap<>();
        Queue<IRNode> critical_comms = new LinkedList<>();
        for (Map.Entry<String, BasicBlockNode> entry : bbs.entrySet()) {
            BasicBlockNode bb = entry.getValue();
            for (IRNode node = bb.head; node != null; node = node.next) {

                // suppose node can be eliminated
                node.eliminated = true;
                if (node instanceof IRCallNode || node instanceof IRStoreNode || node instanceof IRBrNode
                        || node instanceof IRRetNode) {
                    critical_comms.add(node);
                    node.eliminated = false;
                } else if (node instanceof IRLabelNode || node instanceof IRDebugNode || node instanceof IRNLNode) {
                    node.eliminated = false;
                }

                // build graph
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

        // print critical comms
        // System.out.println("; Critical comms:");
        // for (IRNode node : critical_comms) {
        // System.out.println("; " + node.toString());
        // }

        // recover critical vars
        while (!critical_comms.isEmpty()) {
            IRNode node = critical_comms.poll();
            if (node.use() != null) {
                for (String use : node.use()) {
                    if (use != null) {
                        VarUses var_use = var_uses.get(use);
                        if (var_use.def != null && var_use.def.eliminated) {
                            var_use.def.eliminated = false;
                            critical_comms.add(var_use.def);
                        }
                    }
                }
            }
        }

        // // find the dead variables
        // Queue<String> dead_vars = new LinkedList<>();
        // for (Map.Entry<String, VarUses> entry : var_uses.entrySet()) {
        // if (entry.getValue().uses.isEmpty()) {
        // dead_vars.add(entry.getKey());
        // }
        // }

        // // delete the dead variables
        // while (!dead_vars.isEmpty()) {
        // String var_name = dead_vars.poll();
        // VarUses var_use = var_uses.get(var_name);
        // IRNode def_node = var_use.def;

        // // fail to delete if the def_node is call / store / null
        // if (def_node == null || (def_node instanceof IRCallNode) || (def_node
        // instanceof IRStoreNode)) {
        // continue;
        // }

        // // remove def_node in other vars' uses
        // if (def_node.use() != null) {
        // for (String used_var : def_node.use()) {

        // if (used_var != null && var_uses.containsKey(used_var)) {
        // VarUses used_var_uses = var_uses.get(used_var);
        // used_var_uses.uses.remove(def_node);

        // if (used_var_uses.uses.isEmpty()) {
        // dead_vars.add(used_var);
        // }
        // }
        // }
        // }

        // // delete def_node
        // if (def_node.prev instanceof IRDefFuncNode) {
        // ((IRDefFuncNode) def_node.prev).stmt = def_node.next;
        // } else {
        // def_node.prev.next = def_node.next;
        // }
        // if (def_node.next != null) {
        // def_node.next.prev = def_node.prev;
        // }

        // // delete dead_var
        // var_uses.remove(var_name);

        // // System.out.println("Delete " + var_name);
        // }

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

    public void globalVarAnalysis() {

        // if (bb_cnt > 9000) {
        // System.out.println("# Too many basic blocks, skip global var analysis");
        // return;
        // }

        for (IRNode glb_node = ir_beg; glb_node != null; glb_node = glb_node.next) {
            if (glb_node instanceof IRGlbInitNode) {
                IRGlbInitNode glb_init_node = ((IRGlbInitNode) glb_node);
                if (!glb_var_usage.containsKey(glb_init_node.result)) {
                    glb_var_usage.put(glb_init_node.result, new GlbVarUsage());
                }
                glb_var_usage.get(glb_init_node.result).init_node = glb_init_node;

            } else if (glb_node instanceof IRDefFuncNode) {
                IRDefFuncNode def_func_node = ((IRDefFuncNode) glb_node);

                if (def_func_node.eliminated) {
                    continue;
                }

                if (def_func_node.func_name.equals("@Global.Var.Init")) {
                    glb_var_init = def_func_node;
                }

                for (IRNode lcl_node = def_func_node.stmt; lcl_node != null; lcl_node = lcl_node.next) {

                    if (lcl_node instanceof IRStoreNode) {
                        IRStoreNode store_node = (IRStoreNode) lcl_node;
                        String glb_var = store_node.glb();
                        if (glb_var != null) {
                            if (!glb_var_usage.containsKey(glb_var)) {
                                glb_var_usage.put(glb_var, new GlbVarUsage());
                            }
                            glb_var_usage.get(glb_var).store_funcs.add(def_func_node);
                        }

                    } else if (lcl_node instanceof IRLoadNode) {
                        IRLoadNode load_node = (IRLoadNode) lcl_node;
                        String glb_var = load_node.glb();
                        if (glb_var != null) {
                            if (!glb_var_usage.containsKey(glb_var)) {
                                glb_var_usage.put(glb_var, new GlbVarUsage());
                            }
                            glb_var_usage.get(glb_var).load_funcs.add(def_func_node);
                        }
                    }
                }
            }
        }
    }

    public void optiGlbVarInit() {
        // Map<String, String> glb_var_store = new HashMap<>();

        for (IRNode node = glb_var_init.stmt; node != null; node = node.next) {
        }
    }

    public void globalVarToConstant() {

        // if (bb_cnt > 9000) {
        // System.out.println("# Too many basic blocks, skip global var to constant");
        // return;
        // }

        for (Map.Entry<String, GlbVarUsage> entry : glb_var_usage.entrySet()) {
            String var = entry.getKey();
            GlbVarUsage usage = entry.getValue();
            int type = 0;
            if (usage.store_funcs.size() == 1 && usage.store_funcs.contains(glb_var_init)) {
                type = 1;
            } else if (usage.store_funcs.isEmpty()) {
                if (usage.load_funcs.isEmpty()) {
                    type = 3;
                } else {
                    type = 2;
                }
            } else if (usage.load_funcs.isEmpty()) {
                type = 4;
            }

            if (type == 0) {// cannot be optimized
                continue;

            } else if (type == 1) {// use store value
                usage.eliminated = true;
                String val = null;
                Set<IRStoreNode> store_nodes = new HashSet<>();

                // find last var && store nodes
                for (IRNode node = glb_var_init.stmt; node != null; node = node.next) {
                    if (node instanceof IRStoreNode) {
                        IRStoreNode store_node = (IRStoreNode) node;
                        if (store_node.glb() != null && store_node.glb().equals(var)) {
                            val = store_node.value;
                            store_nodes.add(store_node);
                        }
                    }
                }

                if (val.charAt(0) == '%' || val.charAt(0) == '@') {
                    usage.eliminated = false;
                    continue;
                }

                for (IRDefFuncNode func_node : usage.load_funcs) {

                    if (func_node == glb_var_init) {
                        continue;
                    }

                    Set<String> replace = new HashSet<>();

                    // add to replace
                    for (IRNode node = func_node.stmt; node != null; node = node.next) {
                        if (node instanceof IRLoadNode) {
                            IRLoadNode load_node = (IRLoadNode) node;
                            if (load_node.glb() != null && load_node.glb().equals(var)) {
                                replace.add(load_node.result);
                                load_node.eliminated = true;

                                // delete node
                                IRNode prev = node.prev, next = node.next;
                                prev.next = next;
                                if (next != null) {
                                    next.prev = prev;
                                }
                                node = prev;
                            }
                        }
                    }

                    // replace
                    for (IRNode node = func_node.stmt; node != null; node = node.next) {
                        if (node instanceof IRLoadNode) {
                            IRLoadNode load_node = (IRLoadNode) node;
                            if (replace.contains(load_node.ptr)) {
                                load_node.ptr = val;
                            }

                        } else if (node instanceof IRBinaryNode) {
                            IRBinaryNode binary_node = (IRBinaryNode) node;
                            if (replace.contains(binary_node.op1)) {
                                binary_node.op1 = val;
                            }
                            if (replace.contains(binary_node.op2)) {
                                binary_node.op2 = val;
                            }

                        } else if (node instanceof IRBrNode) {
                            IRBrNode br_node = (IRBrNode) node;
                            if (br_node.cond != null && replace.contains(br_node.cond)) {
                                br_node.cond = val;
                            }

                        } else if (node instanceof IRCallNode) {
                            IRCallNode call_node = (IRCallNode) node;
                            if (call_node.args != null) {
                                for (int i = 0; i != call_node.args.length; ++i) {
                                    if (replace.contains(call_node.args[i])) {
                                        call_node.args[i] = val;
                                    }
                                }
                            }

                        } else if (node instanceof IRGetEleNode) {
                            IRGetEleNode get_ele_node = (IRGetEleNode) node;
                            if (replace.contains(get_ele_node.ptr)) {
                                get_ele_node.ptr = val;
                            }
                            if (get_ele_node.idxs != null) {
                                for (int i = 0; i != get_ele_node.idxs.length; ++i) {
                                    if (replace.contains(get_ele_node.idxs[i])) {
                                        get_ele_node.idxs[i] = val;
                                    }
                                }
                            }

                        } else if (node instanceof IRIcmpNode) {
                            IRIcmpNode icmp_node = (IRIcmpNode) node;
                            if (replace.contains(icmp_node.op1)) {
                                icmp_node.op1 = val;
                            }
                            if (replace.contains(icmp_node.op2)) {
                                icmp_node.op2 = val;
                            }

                        } else if (node instanceof IRPhiNode) {
                            IRPhiNode phi_node = (IRPhiNode) node;
                            for (int i = 0; i != phi_node.vals.length; ++i) {
                                if (replace.contains(phi_node.vals[i])) {
                                    phi_node.vals[i] = val;
                                }
                            }

                        } else if (node instanceof IRRetNode) {
                            IRRetNode ret_node = (IRRetNode) node;
                            if (ret_node.val != null && replace.contains(ret_node.val)) {
                                ret_node.val = val;
                            }

                        } else if (node instanceof IRSelectNode) {
                            IRSelectNode select_node = (IRSelectNode) node;
                            if (replace.contains(select_node.cond)) {
                                select_node.cond = val;
                            }
                            if (replace.contains(select_node.val1)) {
                                select_node.val1 = val;
                            }
                            if (replace.contains(select_node.val2)) {
                                select_node.val2 = val;
                            }

                        } else if (node instanceof IRStoreNode) {
                            IRStoreNode store_node = (IRStoreNode) node;
                            if (replace.contains(store_node.ptr)) {
                                store_node.ptr = val;
                            }
                            if (replace.contains(store_node.value)) {
                                store_node.value = val;
                            }
                        }
                    }
                }

                // TODO:
                // delete store_nodes in glb_var_init
                // delete init node
                // IRGlbInitNode init_node = usage.init_node;
                // init_node.prev.next = init_node.next;
                // if (init_node.next != null) {
                // init_node.next.prev = init_node.prev;
                // }
                // init_node.eliminated = true;

            } else if (type == 2) {// use init value
                usage.eliminated = true;
                String val = usage.init_node.val;
                if (val == null) {
                    switch (usage.init_node.tp) {
                        case "i32":
                            val = "0";
                            break;
                        case "i1":
                            val = "false";
                            break;
                        default:
                            val = "null";
                            break;
                    }
                }

                for (IRDefFuncNode func_node : usage.load_funcs) {
                    Set<String> replace = new HashSet<>();

                    // add to replace
                    for (IRNode node = func_node.stmt; node != null; node = node.next) {
                        if (node instanceof IRLoadNode) {
                            IRLoadNode load_node = (IRLoadNode) node;
                            if (load_node.glb() != null && load_node.glb().equals(var)) {
                                replace.add(load_node.result);
                                load_node.eliminated = true;

                                // delete node
                                IRNode prev = node.prev, next = node.next;
                                prev.next = next;
                                if (next != null) {
                                    next.prev = prev;
                                }
                                node = prev;
                            }
                        }
                    }

                    // replace
                    for (IRNode node = func_node.stmt; node != null; node = node.next) {
                        if (node instanceof IRLoadNode) {
                            IRLoadNode load_node = (IRLoadNode) node;
                            if (replace.contains(load_node.ptr)) {
                                load_node.ptr = val;
                            }

                        } else if (node instanceof IRBinaryNode) {
                            IRBinaryNode binary_node = (IRBinaryNode) node;
                            if (replace.contains(binary_node.op1)) {
                                binary_node.op1 = val;
                            }
                            if (replace.contains(binary_node.op2)) {
                                binary_node.op2 = val;
                            }

                        } else if (node instanceof IRBrNode) {
                            IRBrNode br_node = (IRBrNode) node;
                            if (br_node.cond != null && replace.contains(br_node.cond)) {
                                br_node.cond = val;
                            }

                        } else if (node instanceof IRCallNode) {
                            IRCallNode call_node = (IRCallNode) node;
                            if (call_node.args != null) {
                                for (int i = 0; i != call_node.args.length; ++i) {
                                    if (replace.contains(call_node.args[i])) {
                                        call_node.args[i] = val;
                                    }
                                }
                            }

                        } else if (node instanceof IRGetEleNode) {
                            IRGetEleNode get_ele_node = (IRGetEleNode) node;
                            if (replace.contains(get_ele_node.ptr)) {
                                get_ele_node.ptr = val;
                            }
                            if (get_ele_node.idxs != null) {
                                for (int i = 0; i != get_ele_node.idxs.length; ++i) {
                                    if (replace.contains(get_ele_node.idxs[i])) {
                                        get_ele_node.idxs[i] = val;
                                    }
                                }
                            }

                        } else if (node instanceof IRIcmpNode) {
                            IRIcmpNode icmp_node = (IRIcmpNode) node;
                            if (replace.contains(icmp_node.op1)) {
                                icmp_node.op1 = val;
                            }
                            if (replace.contains(icmp_node.op2)) {
                                icmp_node.op2 = val;
                            }

                        } else if (node instanceof IRPhiNode) {
                            IRPhiNode phi_node = (IRPhiNode) node;
                            for (int i = 0; i != phi_node.vals.length; ++i) {
                                if (replace.contains(phi_node.vals[i])) {
                                    phi_node.vals[i] = val;
                                }
                            }

                        } else if (node instanceof IRRetNode) {
                            IRRetNode ret_node = (IRRetNode) node;
                            if (ret_node.val != null && replace.contains(ret_node.val)) {
                                ret_node.val = val;
                            }

                        } else if (node instanceof IRSelectNode) {
                            IRSelectNode select_node = (IRSelectNode) node;
                            if (replace.contains(select_node.cond)) {
                                select_node.cond = val;
                            }
                            if (replace.contains(select_node.val1)) {
                                select_node.val1 = val;
                            }
                            if (replace.contains(select_node.val2)) {
                                select_node.val2 = val;
                            }

                        } else if (node instanceof IRStoreNode) {
                            IRStoreNode store_node = (IRStoreNode) node;
                            if (replace.contains(store_node.ptr)) {
                                store_node.ptr = val;
                            }
                            if (replace.contains(store_node.value)) {
                                store_node.value = val;
                            }
                        }
                    }
                }

                // delete init node
                // IRGlbInitNode init_node = usage.init_node;
                // init_node.prev.next = init_node.next;
                // if (init_node.next != null) {
                // init_node.next.prev = init_node.prev;
                // }
                // init_node.eliminated = true;

            } else if (type == 3) {// delete
                usage.eliminated = true;
                IRGlbInitNode init_node = usage.init_node;
                init_node.prev.next = init_node.next;
                if (init_node.next != null) {
                    init_node.next.prev = init_node.prev;
                }
                init_node.eliminated = true;

            } else {
                usage.eliminated = true;
                for (IRDefFuncNode func_node : usage.store_funcs) {
                    for (IRNode node = func_node.stmt; node != null; node = node.next) {

                        if (node instanceof IRStoreNode) {
                            IRStoreNode store_node = (IRStoreNode) node;
                            if (store_node.glb() != null && store_node.glb().equals(var)) {
                                // delete
                                node.prev.next = node.next;
                                if (node.next != null) {
                                    node.next.prev = node.prev;
                                }
                                node = node.prev;
                            }
                        }
                    }
                }
            }
        }
    }

    public void globalVarToCache() {

        // if (bb_cnt > 9000) {
        // System.out.println("# Too many basic blocks, skip global var localization");
        // return;
        // }

        for (Map.Entry<String, GlbVarUsage> entry : glb_var_usage.entrySet()) {
            String var = entry.getKey();
            GlbVarUsage usage = entry.getValue();

            // System.out.println("var: " + var);
            // usage.print();

            if (usage.eliminated) {
                continue;
            }

            if (usage.load_funcs.size() > 2 || usage.store_funcs.size() > 2) {
                continue;
            }

            if (usage.load_funcs.size() == 2 && !usage.load_funcs.contains(glb_var_init)) {
                continue;
            }

            if (usage.store_funcs.size() == 2 && !usage.store_funcs.contains(glb_var_init)) {
                continue;
            }

            // the function that owns the global variable
            IRDefFuncNode func_node = null;
            for (IRDefFuncNode node : usage.load_funcs) {
                if (node != glb_var_init) {
                    func_node = node;
                    break;
                }
            }

            if (func_node == null) {
                return;
            }

            // if (func_node.recursive()) {
            // continue;
            // }

            if (!func_node.func_name.equals("@main")) {
                continue;
            }

            if (!usage.store_funcs.contains(glb_var_init)) {
                IRNode beg = func_node.stmt;
                String rename = renameLocalize(var);
                // String val = null;

                // add alloca node & init store node
                IRAllocaNode alloca_node = new IRAllocaNode();
                alloca_node.result = rename;
                alloca_node.tp = usage.init_node.tp;

                IRStoreNode store_node = new IRStoreNode();
                store_node.ptr = rename;
                store_node.value = usage.init_node.val;
                store_node.tp = usage.init_node.tp;
                if (store_node.value == null) {
                    switch (store_node.tp) {
                        case "i32":
                            store_node.value = "0";
                            break;
                        case "i1":
                            store_node.value = "false";
                            break;
                        default:
                            store_node.value = "null";
                            break;
                    }
                }
                // val = store_node.value;

                alloca_node.next = store_node;
                store_node.prev = alloca_node;

                // rename the global variable
                for (IRNode node = func_node.stmt; node != null; node = node.next) {
                    if (node instanceof IRStoreNode) {
                        IRStoreNode st_node = (IRStoreNode) node;
                        if (st_node.ptr.equals(var)) {
                            st_node.ptr = rename;
                        }

                    } else if (node instanceof IRLoadNode) {
                        IRLoadNode ld_node = (IRLoadNode) node;
                        if (ld_node.ptr.equals(var)) {
                            ld_node.ptr = rename;
                        }
                    } else if (node instanceof IRRetNode) {
                        // store the value to global variable
                        IRLoadNode ld_node = new IRLoadNode();
                        ld_node.result = renameLocalize("%CacheStVal");
                        ld_node.tp = usage.init_node.tp;
                        ld_node.ptr = rename;

                        IRStoreNode st_node = new IRStoreNode();
                        st_node.ptr = var;
                        st_node.value = ld_node.result;
                        st_node.tp = usage.init_node.tp;

                        ld_node.next = st_node;
                        st_node.prev = ld_node;

                        // insert before ret
                        node.prev.next = ld_node;
                        ld_node.prev = node.prev;
                        st_node.next = node;
                        node.prev = st_node;
                    }
                }

                // insert init after beg
                if (func_node.func_name.equals("@main")) {
                    beg = func_node.stmt.next;// call @Global.Var.Init
                }
                alloca_node.prev = beg;
                store_node.next = beg.next;
                beg.next = alloca_node;
                if (store_node.next != null) {
                    store_node.next.prev = store_node;
                }
                allocas.put(rename, alloca_node);

                // if (!usage.load_funcs.contains(glb_var_init)) {
                // // delete init node
                // IRGlbInitNode init_node = usage.init_node;
                // init_node.prev.next = init_node.next;
                // if (init_node.next != null) {
                // init_node.next.prev = init_node.prev;
                // }
                // init_node.eliminated = true;
                // }

                usage.eliminated = true;

            } else {
                IRStoreNode last_store_node = null;
                for (IRNode node = glb_var_init.stmt; node != null; node = node.next) {
                    if (node instanceof IRStoreNode) {
                        IRStoreNode st_node = (IRStoreNode) node;
                        if (st_node.ptr.equals(var)) {
                            last_store_node = st_node;
                        }
                    }
                }
                IRNode beg = last_store_node;
                while (true) {

                    if (beg.prev instanceof IRStoreNode) {
                        IRStoreNode store_node = (IRStoreNode) beg.prev;
                        if (store_node.glb() != null && !store_node.glb().equals(var)) {
                            break;
                        }

                    } else if (beg.prev instanceof IRDefFuncNode) {
                        beg = beg.next;
                        break;
                    }

                    beg = beg.prev;
                }
                // the comms that initialize the global variable: [beg, last_store_node]

                // delete from glb_var_init
                beg.prev.next = last_store_node.next;
                if (last_store_node.next != null) {
                    last_store_node.next.prev = beg.prev;
                }

                // add alloca node
                String rename = renameLocalize(var);
                IRAllocaNode alloca_node = new IRAllocaNode();
                alloca_node.result = rename;
                alloca_node.tp = usage.init_node.tp;
                alloca_node.next = beg;
                beg.prev = alloca_node;

                // rename the global variable
                for (IRNode node = func_node.stmt; node != null; node = node.next) {
                    if (node instanceof IRStoreNode) {
                        IRStoreNode st_node = (IRStoreNode) node;
                        if (st_node.ptr.equals(var)) {
                            st_node.ptr = rename;
                        }

                    } else if (node instanceof IRLoadNode) {
                        IRLoadNode ld_node = (IRLoadNode) node;
                        if (ld_node.ptr.equals(var)) {
                            ld_node.ptr = rename;
                        }
                    } else if (node instanceof IRRetNode) {
                        // store the value to global variable
                        IRLoadNode ld_node = new IRLoadNode();
                        ld_node.result = renameLocalize("%CacheStVal");
                        ld_node.tp = usage.init_node.tp;
                        ld_node.ptr = rename;

                        IRStoreNode st_node = new IRStoreNode();
                        st_node.ptr = var;
                        st_node.value = ld_node.result;
                        st_node.tp = usage.init_node.tp;

                        ld_node.next = st_node;
                        st_node.prev = ld_node;

                        // insert before ret
                        node.prev.next = ld_node;
                        ld_node.prev = node.prev;
                        st_node.next = node;
                        node.prev = st_node;
                    }
                }

                // update glb_var_usage & rename the global variable in [beg, last_store_node]
                for (IRNode node = beg; node != last_store_node; node = node.next) {
                    if (node instanceof IRStoreNode) {
                        IRStoreNode st_node = (IRStoreNode) node;
                        if (st_node.glb() != null && !st_node.glb().equals(var)) {
                            glb_var_usage.get(st_node.glb()).store_funcs.add(func_node);
                        } else if (st_node.glb() != null && st_node.glb().equals(var)) {
                            st_node.ptr = rename;
                        }
                    } else if (node instanceof IRLoadNode) {
                        IRLoadNode ld_node = (IRLoadNode) node;
                        if (ld_node.glb() != null && !ld_node.glb().equals(var)) {
                            glb_var_usage.get(ld_node.glb()).load_funcs.add(func_node);
                        } else if (ld_node.glb() != null && ld_node.glb().equals(var)) {
                            ld_node.ptr = rename;
                        }
                    }
                }
                last_store_node.ptr = rename;

                // insert init after beg
                beg = func_node.stmt;
                if (func_node.func_name.equals("@main")) {
                    beg = func_node.stmt.next;// call @Global.Var.Init
                }
                alloca_node.prev = beg;
                last_store_node.next = beg.next;
                beg.next = alloca_node;
                if (last_store_node.next != null) {
                    last_store_node.next.prev = last_store_node;
                }
                allocas.put(rename, alloca_node);

                // delete init node
                // IRGlbInitNode init_node = usage.init_node;
                // init_node.prev.next = init_node.next;
                // if (init_node.next != null) {
                // init_node.next.prev = init_node.prev;
                // }
                // init_node.eliminated = true;
                usage.eliminated = true;
            }
        }
    }

    public void buildFuncCallMap() {
        // collect funcs
        for (IRNode node = ir_beg; node != null; node = node.next) {
            if (node instanceof IRDefFuncNode) {
                IRDefFuncNode def_node = (IRDefFuncNode) node;
                funcs.put(def_node.func_name, def_node);
            }
        }

        // build call map
        for (IRDefFuncNode func : funcs.values()) {
            func.scale = 0;
            for (IRNode node = func.stmt; node != null; node = node.next) {
                ++func.scale;
                if (node instanceof IRCallNode) {
                    IRCallNode call_node = (IRCallNode) node;
                    if (!isBuiltin(call_node.func_name)) {
                        IRDefFuncNode callee = funcs.get(call_node.func_name);
                        func.callee_nodes.add(callee);
                        callee.caller_nodes.add(func);
                    }
                } else if (node instanceof IRLabelNode) {
                    ++bb_cnt;
                }
            }
        }
    }

    public void printRecursive() {
        for (IRDefFuncNode func : funcs.values()) {
            System.out.println(func.func_name + " recursive: " + func.recursive());
        }
    }

    public void printFuncCallMap() {
        for (IRDefFuncNode func : funcs.values()) {
            System.out.println(func.func_name + ":");
            // System.out.println("caller: ");
            // for (IRDefFuncNode caller : func.caller_nodes) {
            // System.out.print(caller.func_name + " ");
            // }
            // System.out.println();
            System.out.println("callee: ");
            for (IRDefFuncNode callee : func.callee_nodes) {
                System.out.println(callee.func_name);
            }
            System.out.println();
        }
    }

    InlineRetType rewriteInlineFunc(IRDefFuncNode func_node, IRCallNode call_node) {

        boolean void_tp = func_node.result_tp.equals("void");
        String ret_val = (void_tp ? null : renameInline("%InlineRetVal"));
        String end_label = renameInlineLabel("Func.End.Label");
        IRNode beg = new IRNode(), end = beg;
        Map<String, String> rename_map = new HashMap<>();
        Map<IRDefFuncNode, ArrayList<IRCallNode>> callee_update = new HashMap<>();

        // System.out.println("Rewrite " + func_node.func_name);

        // add args to rename_map
        if (call_node.args != null) {
            for (int i = 0; i != call_node.args.length; ++i) {
                rename_map.put(func_node.ids[i], call_node.args[i]);
            }
        }

        // rename local variables in function
        for (IRNode node = func_node.stmt.next; node != null; node = node.next) {

            // System.out.println("Type: " + node.getClass().getName());

            if (node instanceof IRAllocaNode) {
                IRAllocaNode old_node = (IRAllocaNode) node, new_node = new IRAllocaNode();
                new_node.result = renameInline(old_node.result);
                new_node.tp = old_node.tp;
                rename_map.put(old_node.result, new_node.result);

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

                allocas.put(new_node.result, new_node);

            } else if (node instanceof IRBinaryNode) {
                IRBinaryNode old_node = (IRBinaryNode) node, new_node = new IRBinaryNode();
                new_node.operator = old_node.operator;
                new_node.tp = old_node.tp;
                new_node.result = renameInline(old_node.result);
                rename_map.put(old_node.result, new_node.result);
                if (rename_map.containsKey(old_node.op1)) {
                    new_node.op1 = rename_map.get(old_node.op1);
                } else {
                    new_node.op1 = renameInline(old_node.op1);
                    rename_map.put(old_node.op1, new_node.op1);
                }
                if (rename_map.containsKey(old_node.op2)) {
                    new_node.op2 = rename_map.get(old_node.op2);
                } else {
                    new_node.op2 = renameInline(old_node.op2);
                    rename_map.put(old_node.op2, new_node.op2);
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRBrNode) {
                IRBrNode old_node = (IRBrNode) node, new_node = new IRBrNode();
                if (old_node.cond != null) {
                    if (rename_map.containsKey(old_node.cond)) {
                        new_node.cond = rename_map.get(old_node.cond);
                    } else {
                        new_node.cond = renameInline(old_node.cond);
                        rename_map.put(old_node.cond, new_node.cond);
                    }
                }
                if (rename_map.containsKey(old_node.label_true)) {
                    new_node.label_true = rename_map.get(old_node.label_true);
                } else {
                    new_node.label_true = renameInlineLabel(old_node.label_true);
                    rename_map.put(old_node.label_true, new_node.label_true);
                }
                if (old_node.label_false != null) {
                    if (rename_map.containsKey(old_node.label_false)) {
                        new_node.label_false = rename_map.get(old_node.label_false);
                    } else {
                        new_node.label_false = renameInlineLabel(old_node.label_false);
                        rename_map.put(old_node.label_false, new_node.label_false);
                    }
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRCallNode) {
                IRCallNode old_node = (IRCallNode) node, new_node = new IRCallNode();
                new_node.func_name = old_node.func_name;
                new_node.res_tp = old_node.res_tp;
                new_node.tps = old_node.tps;
                if (old_node.result != null) {
                    new_node.result = renameInline(old_node.result);
                    rename_map.put(old_node.result, new_node.result);
                }
                if (old_node.args != null) {
                    new_node.args = new String[old_node.args.length];
                    for (int i = 0; i != old_node.args.length; ++i) {
                        if (rename_map.containsKey(old_node.args[i])) {
                            new_node.args[i] = rename_map.get(old_node.args[i]);
                        } else {
                            new_node.args[i] = renameInline(old_node.args[i]);
                            rename_map.put(old_node.args[i], new_node.args[i]);
                        }
                    }
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

                IRDefFuncNode callee = funcs.get(new_node.func_name);
                if (!callee_update.containsKey(callee)) {
                    callee_update.put(callee, new ArrayList<>());
                }
                callee_update.get(callee).add(new_node);

            } else if (node instanceof IRGetEleNode) {
                IRGetEleNode old_node = (IRGetEleNode) node, new_node = new IRGetEleNode();
                new_node.result = renameInline(old_node.result);
                rename_map.put(old_node.result, new_node.result);
                new_node.tp = old_node.tp;
                new_node.tps = old_node.tps;
                if (rename_map.containsKey(old_node.ptr)) {
                    new_node.ptr = rename_map.get(old_node.ptr);
                } else {
                    new_node.ptr = renameInline(old_node.ptr);
                    rename_map.put(old_node.ptr, new_node.ptr);
                }
                new_node.idxs = new String[old_node.idxs.length];
                for (int i = 0; i != old_node.idxs.length; ++i) {
                    if (rename_map.containsKey(old_node.idxs[i])) {
                        new_node.idxs[i] = rename_map.get(old_node.idxs[i]);
                    } else {
                        new_node.idxs[i] = renameInline(old_node.idxs[i]);
                        rename_map.put(old_node.idxs[i], new_node.idxs[i]);
                    }
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRIcmpNode) {
                IRIcmpNode old_node = (IRIcmpNode) node, new_node = new IRIcmpNode();
                new_node.cond = old_node.cond;
                new_node.tp = old_node.tp;
                new_node.result = renameInline(old_node.result);
                rename_map.put(old_node.result, new_node.result);
                if (rename_map.containsKey(old_node.op1)) {
                    new_node.op1 = rename_map.get(old_node.op1);
                } else {
                    new_node.op1 = renameInline(old_node.op1);
                    rename_map.put(old_node.op1, new_node.op1);
                }
                if (rename_map.containsKey(old_node.op2)) {
                    new_node.op2 = rename_map.get(old_node.op2);
                } else {
                    new_node.op2 = renameInline(old_node.op2);
                    rename_map.put(old_node.op2, new_node.op2);
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRLabelNode) {
                IRLabelNode old_node = (IRLabelNode) node, new_node = new IRLabelNode();
                if (rename_map.containsKey(old_node.label)) {
                    new_node.label = rename_map.get(old_node.label);
                } else {
                    new_node.label = renameInlineLabel(old_node.label);
                    rename_map.put(old_node.label, new_node.label);
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRLoadNode) {
                IRLoadNode old_node = (IRLoadNode) node, new_node = new IRLoadNode();
                new_node.result = renameInline(old_node.result);
                rename_map.put(old_node.result, new_node.result);
                new_node.tp = old_node.tp;
                if (rename_map.containsKey(old_node.ptr)) {
                    new_node.ptr = rename_map.get(old_node.ptr);
                } else {
                    new_node.ptr = renameInline(old_node.ptr);
                    rename_map.put(old_node.ptr, new_node.ptr);
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRNLNode) {
                IRNLNode new_node = new IRNLNode();
                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRPhiNode) {
                IRPhiNode old_node = (IRPhiNode) node, new_node = new IRPhiNode();
                new_node.result = renameInline(old_node.result);
                rename_map.put(old_node.result, new_node.result);
                new_node.tp = old_node.tp;
                new_node.vals = new String[old_node.vals.length];
                new_node.labels = new String[old_node.labels.length];
                for (int i = 0; i != old_node.vals.length; ++i) {
                    if (rename_map.containsKey(old_node.vals[i])) {
                        new_node.vals[i] = rename_map.get(old_node.vals[i]);
                    } else {
                        new_node.vals[i] = renameInline(old_node.vals[i]);
                        rename_map.put(old_node.vals[i], new_node.vals[i]);
                    }
                    if (rename_map.containsKey(old_node.labels[i])) {
                        new_node.labels[i] = rename_map.get(old_node.labels[i]);
                    } else {
                        new_node.labels[i] = renameInlineLabel(old_node.labels[i]);
                        rename_map.put(old_node.labels[i], new_node.labels[i]);
                    }
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRRetNode) {
                IRRetNode old_node = (IRRetNode) node;
                if (void_tp) {// ret void
                    IRBrNode br_node = new IRBrNode();
                    br_node.label_true = end_label;

                    end.next = br_node;
                    br_node.prev = end;
                    end = br_node;

                } else {
                    IRStoreNode store_node = new IRStoreNode();
                    store_node.tp = old_node.tp;
                    if (rename_map.containsKey(old_node.val)) {
                        store_node.value = rename_map.get(old_node.val);
                    } else {
                        store_node.value = renameInline(old_node.val);
                        rename_map.put(old_node.val, store_node.value);
                    }
                    store_node.ptr = ret_val;
                    IRBrNode br_node = new IRBrNode();
                    br_node.label_true = end_label;

                    store_node.next = br_node;
                    br_node.prev = store_node;
                    end.next = store_node;
                    store_node.prev = end;
                    end = br_node;
                }

            } else if (node instanceof IRSelectNode) {
                IRSelectNode old_node = (IRSelectNode) node, new_node = new IRSelectNode();
                new_node.result = renameInline(old_node.result);
                rename_map.put(old_node.result, new_node.result);
                new_node.tp = old_node.tp;
                if (rename_map.containsKey(old_node.cond)) {
                    new_node.cond = rename_map.get(old_node.cond);
                } else {
                    new_node.cond = renameInline(old_node.cond);
                    rename_map.put(old_node.cond, new_node.cond);
                }
                if (rename_map.containsKey(old_node.val1)) {
                    new_node.val1 = rename_map.get(old_node.val1);
                } else {
                    new_node.val1 = renameInline(old_node.val1);
                    rename_map.put(old_node.val1, new_node.val1);
                }
                if (rename_map.containsKey(old_node.val2)) {
                    new_node.val2 = rename_map.get(old_node.val2);
                } else {
                    new_node.val2 = renameInline(old_node.val2);
                    rename_map.put(old_node.val2, new_node.val2);
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRStoreNode) {
                IRStoreNode old_node = (IRStoreNode) node, new_node = new IRStoreNode();
                new_node.tp = old_node.tp;
                if (rename_map.containsKey(old_node.ptr)) {
                    new_node.ptr = rename_map.get(old_node.ptr);
                } else {
                    new_node.ptr = renameInline(old_node.ptr);
                    rename_map.put(old_node.ptr, new_node.ptr);
                }
                if (rename_map.containsKey(old_node.value)) {
                    new_node.value = rename_map.get(old_node.value);
                } else {
                    new_node.value = renameInline(old_node.value);
                    rename_map.put(old_node.value, new_node.value);
                }

                end.next = new_node;
                new_node.prev = end;
                end = new_node;

            } else if (node instanceof IRDebugNode) {
                IRDebugNode old_node = (IRDebugNode) node, new_node = new IRDebugNode();
                new_node.message = old_node.message;

                end.next = new_node;
                new_node.prev = end;
                end = new_node;
                // } else {
                // IRNode new_node = new IRNode();
                // end.next = new_node;
                // new_node.prev = end;
                // end = new_node;

            }
        }

        // attach end_label node
        IRLabelNode end_label_node = new IRLabelNode();
        end_label_node.label = end_label;
        end.next = end_label_node;
        end_label_node.prev = end;
        end = end_label_node;

        // insert ret_vall alloca if returns value
        if (!void_tp) {
            IRAllocaNode ret_alloca_node = new IRAllocaNode();
            ret_alloca_node.result = ret_val;
            ret_alloca_node.tp = func_node.result_tp;
            ret_alloca_node.next = beg.next;
            beg.next.prev = ret_alloca_node;
            beg.next = ret_alloca_node;
            ret_alloca_node.prev = beg;

            allocas.put(ret_val, ret_alloca_node);
        }

        return new InlineRetType(beg.next, end, ret_val, callee_update);
    }

    public void inlineFuncs() {

        // System.out.println("bb cnt: " + bb_cnt);
        if (bb_cnt > 9000) {
            // System.out.println("# to many basic blocks, skip inline");
            // return;
            // MAX_INLINE_SCALE = 512;
            // } else if (bb_cnt == 188) {
            // MAX_INLINE_SCALE = 500;
        }
        // inline
        for (IRDefFuncNode callee_func_node : funcs.values()) {

            // check if the function can be inlined
            if (callee_func_node.scale > MAX_INLINE_SCALE ||
                    callee_func_node.caller_nodes.isEmpty()
                    || callee_func_node.func_name.equals("@main")
                    || callee_func_node.func_name.equals("@Global.Var.Init") ||
                    callee_func_node.recursive()) {
                continue;
            }

            for (IRDefFuncNode caller_func_node : callee_func_node.caller_nodes) {
                // IRDefFuncNode caller_func_node = caller_node.getKey();

                // System.out.println(callee_func_node.func_name + " scale: " +
                // callee_func_node.scale + " -> inline -> " +
                // caller_func_node.func_name);

                // find all insert points in caller
                ArrayList<IRCallNode> insert_inlines = new ArrayList<>();
                for (IRNode node = caller_func_node.stmt; node != null; node = node.next) {
                    if (node instanceof IRCallNode) {
                        IRCallNode call_node = (IRCallNode) node;
                        if (call_node.func_name.equals(callee_func_node.func_name)) {
                            insert_inlines.add(call_node);
                        }
                    }
                }

                // inline
                for (IRCallNode call_node : insert_inlines) {
                    // get rewrited inline function
                    InlineRetType rewrite_res = rewriteInlineFunc(callee_func_node, call_node);

                    // attach inline function
                    rewrite_res.beg.prev = call_node.prev;
                    call_node.prev.next = rewrite_res.beg;
                    rewrite_res.end.next = call_node.next;
                    if (call_node.next != null) {
                        call_node.next.prev = rewrite_res.end;
                    }

                    // load the return value from inline function
                    if (call_node.result != null) {
                        IRLoadNode load_node = new IRLoadNode();
                        load_node.tp = call_node.res_tp;
                        load_node.result = call_node.result;
                        load_node.ptr = rewrite_res.ret_val;

                        load_node.next = rewrite_res.end.next;
                        if (rewrite_res.end.next != null) {
                            rewrite_res.end.next.prev = load_node;
                        }
                        rewrite_res.end.next = load_node;
                        load_node.prev = rewrite_res.end;
                        rewrite_res.end = load_node;
                    }

                    // add the callee's scale to caller
                    caller_func_node.scale += callee_func_node.scale;

                    {// update caller function
                     // Map<IRDefFuncNode, ArrayList<IRCallNode>> callee_update =
                     // rewrite_res.callee_update;
                     // for (Map.Entry<IRDefFuncNode, ArrayList<IRCallNode>> entry :
                     // callee_update.entrySet()) {
                     // IRDefFuncNode new_callee = entry.getKey();
                     // ArrayList<IRCallNode> calls = entry.getValue();
                     // ArrayList<IRCallNode> cpy1 = new ArrayList<>(calls), cpy2 = new
                     // ArrayList<>(calls);

                        // if (caller_func_node.callee_nodes.containsKey(new_callee)) {
                        // ArrayList<IRCallNode> old_calls =
                        // caller_func_node.callee_nodes.get(new_callee);
                        // cpy1.removeAll(old_calls);
                        // old_calls.addAll(cpy1);
                        // } else {
                        // caller_func_node.callee_nodes.put(new_callee, cpy1);
                        // }

                        // if (new_callee.caller_nodes.containsKey(caller_func_node)) {
                        // ArrayList<IRCallNode> old_calls =
                        // new_callee.caller_nodes.get(caller_func_node);
                        // cpy2.removeAll(old_calls);
                        // old_calls.addAll(cpy2);
                        // } else {
                        // new_callee.caller_nodes.put(caller_func_node, cpy2);
                        // }
                        // }
                    }
                }

                // delete callee inline function from caller
                caller_func_node.callee_nodes.remove(callee_func_node);
                // add callee's callees to caller
                // callee_func_node.callee_nodes.addAll(callee_func_node.callee_nodes);
                // add caller to callee's callees
                for (IRDefFuncNode callee_of_callee : callee_func_node.callee_nodes) {
                    // System.out.println(
                    // "add " + caller_func_node.func_name + " to the caller of" +
                    // callee_of_callee.func_name);
                    callee_of_callee.caller_nodes.add(caller_func_node);
                    // System.out.println(
                    // "add " + callee_of_callee.func_name + " to the callee of" +
                    // caller_func_node.func_name);
                    caller_func_node.callee_nodes.add(callee_of_callee);
                }
            }

            // delete callee function from callees of callee
            for (IRDefFuncNode callee_of_callee : callee_func_node.callee_nodes) {
                callee_of_callee.caller_nodes.remove(callee_func_node);
            }

            // clear callee's callers
            callee_func_node.caller_nodes.clear();
            callee_func_node.callee_nodes.clear();

            // delete callee inline function
            callee_func_node.eliminated = true;

            // delete callee function
            callee_func_node.prev.next = callee_func_node.next;
            if (callee_func_node.next != null) {
                callee_func_node.next.prev = callee_func_node.prev;
            }
        }
        // end inline
    }

    public void detectMain() {
        for (IRNode node = ir_beg; node != null; node = node.next) {
            if (node instanceof IRDefFuncNode) {
                IRDefFuncNode func_node = (IRDefFuncNode) node;
                if (func_node.func_name.equals("@main")) {
                    return;
                }
            }
        }

        System.out.println("# No main function");
        System.exit(1);
    }
}
