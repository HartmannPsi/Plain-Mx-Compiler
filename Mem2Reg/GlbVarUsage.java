package Mem2Reg;

import IR.IRNodes.*;
// import java.util.Map;
// import java.util.ArrayList;
// import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class GlbVarUsage {
    public Set<IRDefFuncNode> load_funcs = new HashSet<>(), store_funcs = new HashSet<>();
    public boolean eliminated = false;
    public IRGlbInitNode init_node = null;

    public void print() {
        System.out.println("load funcs:");
        for (IRDefFuncNode node : load_funcs) {
            System.out.println(node.func_name);
        }
        System.out.println("store funcs:");
        for (IRDefFuncNode node : store_funcs) {
            System.out.println(node.func_name);
        }
        System.out.println("eliminated: " + eliminated);
    }
}
