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
    public Set<String> use = new HashSet<>(), def = new HashSet<>(), in = new HashSet<>(), out = new HashSet<>();
    // public Set<BasicBlockNode> dominates = new HashSet<>(), dom_frontier = new
    // HashSet<>();
    public BitSet dominates = new BitSet();
    public Set<BasicBlockNode> dom_frontier = new HashSet<>();
    public BasicBlockNode idom = null;
    public ArrayList<BasicBlockNode> dom_tree_son = new ArrayList<>();
    public static Map<Integer, BasicBlockNode> num_to_bb = new HashMap<>();

    public Set<BasicBlockNode> dominates() {
        Set<BasicBlockNode> ret = new HashSet<>();
        for (int i = dominates.nextSetBit(0); i >= 0; i = dominates.nextSetBit(i + 1)) {
            ret.add(num_to_bb.get(i));
        }
        return ret;
    }

    // public Set<BasicBlockNode> dom_frontier() {
    // return new HashSet<>();
    // }
}
