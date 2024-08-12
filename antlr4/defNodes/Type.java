package defNodes;

public class Type {
    public String id = null;
    public int dim = 0;
    public boolean is_lvalue = false;
    // public Node[] dim_lens = null;

    public Type(String id, int dim, boolean is_lvalue) {
        this.id = new String(id);
        this.dim = dim;
        this.is_lvalue = is_lvalue;
    }

    public Type() {
    }

    public Type(String id) {
        this.id = new String(id);
    }

    public Type clone() {
        return new Type(id, dim, is_lvalue);
    }

    public boolean equal(Type other) {
        return this.id.equals(other.id) && this.dim == other.dim;
    }

    public boolean equal(String other) {
        return this.equal(new Type(other));
    }

    public boolean equal_hard(Type other) {
        return this.id.equals(other.id) && this.dim == other.dim && this.is_lvalue == other.is_lvalue;
    }
}
