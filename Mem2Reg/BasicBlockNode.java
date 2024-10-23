package Mem2Reg;

import IR.IRNodes.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.BitSet;
import java.util.Map;
import java.util.HashMap;

public class BasicBlockNode {
    public String label = null;
    public ArrayList<BasicBlockNode> precursors = new ArrayList<>(), successors = new ArrayList<>();
    public IRNode head = null, tail = null;

    // public Set<String> use = new HashSet<>(), def = new HashSet<>(), in = new
    // HashSet<>(), out = new HashSet<>();
    public BitSet buse = new BitSet(), bdef = new BitSet(), bin = new BitSet(), bout = new BitSet();
    // public Set<BasicBlockNode> dominates = new HashSet<>(), dom_frontier = new
    // HashSet<>();
    public BitSet dominates = new BitSet();
    public Set<BasicBlockNode> dom_frontier = new HashSet<>();
    public BasicBlockNode idom = null;
    public ArrayList<BasicBlockNode> dom_tree_son = new ArrayList<>();
    public static Map<Integer, BasicBlockNode> num_to_bb = new HashMap<>();
    public static Map<Integer, String> num_to_var = new HashMap<>();

    public Set<BasicBlockNode> dominates() {
        Set<BasicBlockNode> ret = new HashSet<>();
        for (int i = dominates.nextSetBit(0); i >= 0; i = dominates.nextSetBit(i + 1)) {
            ret.add(num_to_bb.get(i));
        }
        return ret;
    }

    public Set<String> in() {
        Set<String> ret = new HashSet<>();
        for (int i = bin.nextSetBit(0); i >= 0; i = bin.nextSetBit(i + 1)) {
            ret.add(num_to_var.get(i));
        }
        return ret;
    }

    public Set<String> out() {
        Set<String> ret = new HashSet<>();
        for (int i = bout.nextSetBit(0); i >= 0; i = bout.nextSetBit(i + 1)) {
            ret.add(num_to_var.get(i));
        }
        return ret;
    }

    public boolean isBegOfFunc() {
        return head.prev instanceof IRDefFuncNode;
    }

    // public Set<BasicBlockNode> dom_frontier() {
    // return new HashSet<>();
    // }
}
