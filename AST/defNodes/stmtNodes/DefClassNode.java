package defNodes.stmtNodes;

import java.util.Map;
import java.util.HashMap;
import defNodes.FuncType;
import defNodes.Node;
import defNodes.ScopeNode;
import defNodes.exprNodes.IdNode;

public class DefClassNode extends ScopeNode {
    public Node name = null;
    public Node[] stmt = null;
    public Map<String, FuncType> methods = new HashMap<>();
    public Map<String, String> rename_methods = new HashMap<>();

    public void printToString() {
        System.out.print("class ");
        name.printToString();
        System.out.println(" {");
        for (Node s : stmt) {
            System.out.print('\t');
            s.printToString();
        }
        System.out.println("};");
    }

    public String getConsRename() {
        String cls_rename = ((IdNode) name).rename_id;
        return "@Cons." + cls_rename.substring(1, cls_rename.length());
    }
}
