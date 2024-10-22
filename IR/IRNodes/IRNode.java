package IR.IRNodes;

import java.util.Set;
import java.util.HashSet;
import Mem2Reg.BasicBlockNode;
import java.util.Map;
import java.util.HashMap;
import java.util.BitSet;

public class IRNode {
    public IRNode next = null, prev = null;
    static public boolean shadow = false;
    public Set<String> in = new HashSet<>(), out = new HashSet<>();
    public BitSet bin = new BitSet(), bout = new BitSet();
    public BasicBlockNode bb = null;
    public int order = -1;
    public static Map<String, Integer> var_def = new HashMap<>();
    public static Map<Integer, String> num_to_var = new HashMap<>();
    public boolean eliminated = false;

    public void calcInOut() {
        // TODO:
        for (int i = bin.nextSetBit(0); i >= 0; i = bin.nextSetBit(i + 1)) {
            in.add(num_to_var.get(i));
        }

        for (int i = bout.nextSetBit(0); i >= 0; i = bout.nextSetBit(i + 1)) {
            out.add(num_to_var.get(i));
        }
    }

    public String[] use() {
        return null;
    }

    public String def() {
        return null;
    }

    public void printToString() {
        printNext();
    }

    public String toString() {
        return "[ Basic Type ]";
    }

    public IRNode dispose() {

        if (this instanceof IRConstStrNode || this instanceof IRDclFuncNode || this instanceof IRDefClsNode
                || this instanceof IRDefFuncNode || this instanceof IRGlbInitNode || this instanceof IRLabelNode) {
            shadow = false;
            // System.out.println("Fuck");

        }

        if (shadow) {
            if (next == null) {
                return null;
            }

            return next.dispose();
        } else {
            if (next == null) {
                return this;
            }

            if (this instanceof IRDefFuncNode) {
                IRDefFuncNode func_node = (IRDefFuncNode) this;
                func_node.stmt = func_node.stmt.dispose();
                shadow = false;
            }

            if (this instanceof IRBrNode || this instanceof IRRetNode) {
                shadow = true;
                // System.out.println("Fuck you");
            }

            next = next.dispose();
            return this;
        }

    }

    public void printNext() {
        if (next != null) {
            next.printToString();
        }
    }

    boolean isActive(String str) {
        return !(str != null && ((str.charAt(0) >= '0' && str.charAt(0) <= '9') || str.charAt(0) == '-'
                || str.charAt(0) == '@'
                || str.equals("true") || str.equals("false") || str.equals("null")));
    }
}
