package defNodes;

import java.util.HashMap;
import java.util.Map;

public class ScopeNode extends Node {
    public Map<String, Type> vars = new HashMap<>();
    public Map<String, String> rename_vars = new HashMap<>();
    // <Name, Type>
}
