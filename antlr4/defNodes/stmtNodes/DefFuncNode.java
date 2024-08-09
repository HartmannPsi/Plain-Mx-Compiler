package defNodes.stmtNodes;

import defNodes.*;

public class DefFuncNode extends ScopeNode {
    public Node type = null;
    public Node name = null;

    // public class Para {
    // public Node tp;
    // public Node id;
    // };

    // public Para[] paras;
    public Node[] stmt = null;
    public Node[] tps = null;
    public Node[] ids = null;
}
