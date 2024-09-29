package Mem2Reg;

import IR.IRNodes.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class BasicBlockNode {
    public String label = null;
    public ArrayList<BasicBlockNode> precursors = new ArrayList<>(), successors = new ArrayList<>();
    public IRNode head = null, tail = null;
    public Set<String> use = new HashSet<>(), def = new HashSet<>(), in = new HashSet<>(), out = new HashSet<>();
    public Set<BasicBlockNode> dominates = new HashSet<>(), dom_frontier = new HashSet<>();
    public BasicBlockNode idom = null;
    public ArrayList<BasicBlockNode> dom_tree_son = new ArrayList<>();
}
