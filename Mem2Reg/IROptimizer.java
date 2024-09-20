package Mem2Reg;

import IR.IRNodes.*;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;

public class IROptimizer {
    public IRNode ir_beg = null;
    BasicBlockNode bb_beg = null;
    Map<String, BasicBlockNode> bbs = new HashMap<>();

    public IROptimizer(IRNode beg) {
        ir_beg = beg;
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
                    }
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
        // TODO
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
            ArrayList<IRNode> list = new ArrayList<>();
            for (IRNode node = bb.head;; node = node.next) {
                list.add(node);
                if (node == bb.tail)
                    break;
            }

            Set<String> last_out = new HashSet<>(bb.out);
            for (int i = list.size() - 1; i >= 0; --i) {
                IRNode node = list.get(i);
                node.out = new HashSet<>(last_out);
                if (node.def() != null) {
                    last_out.remove(node.def());
                }
                if (node.use() != null) {
                    last_out.add(node.use());
                }
                node.in = new HashSet<>(last_out);
            }
        }
    }

    // TODO

}
