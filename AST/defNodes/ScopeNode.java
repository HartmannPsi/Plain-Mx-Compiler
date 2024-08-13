package defNodes;

import java.util.HashMap;
import java.util.Map;

public class ScopeNode extends Node {
    public Map<String, Type> vars = new HashMap<>();
    // <Name, Type>
}
