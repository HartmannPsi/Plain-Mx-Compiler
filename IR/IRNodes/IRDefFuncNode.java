package IR.IRNodes;

import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Set;
//import java.util.ArrayList;

public class IRDefFuncNode extends IRNode {
    public String result_tp = null, func_name = null;
    public String[] tps = null, ids = null;
    public IRNode stmt = null;
    public int scale = -1;
    public boolean inline = false;
    // public boolean multiple_call = false;

    public Set<IRDefFuncNode> caller_nodes = new HashSet<>(), callee_nodes = new HashSet<>();

    public boolean recursive() {
        Queue<IRDefFuncNode> queue = new LinkedList<>();
        Set<IRDefFuncNode> visited = new HashSet<>();
        visited.add(this);
        queue.add(this);

        while (!queue.isEmpty()) {
            IRDefFuncNode cur = queue.poll();
            for (IRDefFuncNode callee : cur.callee_nodes) {
                if (callee == this) {
                    return true;
                }
                if (!visited.contains(callee)) {
                    visited.add(callee);
                    queue.add(callee);
                }
            }
        }

        return false;
    }

    public boolean isMember() {
        if (ids == null) {
            return false;
        } else {
            return ids[0].equals("%this");
        }
    }

    public void printToString() {

        // shadow = false;

        // if (shadow) {
        // printNext();
        // return;
        // }

        if (scale != -1) {
            System.out.println("; scale: " + scale);
        }
        System.out.print("define dso_local " + result_tp + " " + func_name + "(");

        if (tps != null) {
            for (int i = 0; i != tps.length; ++i) {
                System.out.print(tps[i] + " " + ids[i]);
                if (i != tps.length - 1) {
                    System.out.print(", ");
                }
            }
        }

        System.out.println(") {");
        if (stmt != null)
            stmt.printToString();
        System.out.println("}");
        System.out.println();

        printNext();
    }

    public String toString() {
        String res = "define dso_local " + result_tp + " " + func_name + "(";

        if (tps != null) {
            for (int i = 0; i != tps.length; ++i) {
                res += tps[i] + " " + ids[i];
                if (i != tps.length - 1) {
                    res += ", ";
                }
            }
        }
        res += ") { ... }";
        return res;
    }
}
