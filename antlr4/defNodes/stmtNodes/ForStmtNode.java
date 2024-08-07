package defNodes.stmtNodes;

import defNodes.*;

public class ForStmtNode extends ScopeNode {
    public Node init = null, cond = null, step = null;
    public Node[] stmts = null;
}
