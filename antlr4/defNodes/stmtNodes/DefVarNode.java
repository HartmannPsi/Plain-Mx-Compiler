package defNodes.stmtNodes;

import defNodes.*;

public class DefVarNode extends Node {
    public Node type = null;

    public class VarInit {
        public Node id = null;
        public Node init = null;
    };

    public VarInit var_init = null;
}
