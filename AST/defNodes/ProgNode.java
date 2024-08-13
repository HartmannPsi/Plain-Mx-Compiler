package defNodes;

import java.util.HashSet;
import java.util.Set;

import java.util.HashMap;
import java.util.Map;

public class ProgNode extends ScopeNode {
    public Node[] global_stmt = null;
    public Set<String> class_ids = new HashSet<>();
    public Map<String, FuncType> func_ids = new HashMap<>();

    public void printToString() {
        System.out.println("Prog:");
        for (Node stmt : global_stmt) {
            if (stmt != null)
                stmt.printToString();
        }
    }
}
