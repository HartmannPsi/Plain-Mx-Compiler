package defNodes.stmtNodes;

import java.util.Map;
import java.util.HashMap;
import defNodes.FuncType;
import defNodes.Node;
import defNodes.ScopeNode;

public class DefClassNode extends ScopeNode {
    public Node name = null;
    public Node[] stmt = null;
    public Map<String, FuncType> methods = new HashMap<>();

}
