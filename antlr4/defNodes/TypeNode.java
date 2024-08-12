package defNodes;

public class TypeNode extends Node {
    public Type type = null;
    public Node[] dim_lens = null;

    public void printToString() {
        System.out.print(type.id);
        for (int i = 0; i != type.dim; ++i) {
            System.out.print("[]");

        }
    }
}
