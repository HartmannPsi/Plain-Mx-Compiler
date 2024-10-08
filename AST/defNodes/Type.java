package defNodes;

// import util.error.internalError;
// import util.position;

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

    public boolean equal_weak(Type other) {
        return this.id.equals(other.id);
    }

    public boolean equal_weak(String other) {
        return this.equal_weak(new Type(other));
    }

    public boolean equal(String other) {
        return this.equal(new Type(other));
    }

    public boolean equal_hard(Type other) {
        return this.id.equals(other.id) && this.dim == other.dim && this.is_lvalue == other.is_lvalue;
    }

    public String toString() {
        String ret = id;
        for (int i = 0; i < dim; i++) {
            ret += "[]";
        }
        return ret;
    }

    public String getLLVMType() {
        String res;
        if (this.equal("int")) {

            res = "i32";

        } else if (this.equal("bool")) {

            res = "i1";

        } else if (this.equal("void")) {

            // throw new internalError("TypeNode Error: void type is not allowed in this
            // context", new position(114, 514));
            res = "void";

        } else if (this.equal("null")) {

            res = "ptr";

        } else if (this.equal("string")) {

            // res = "i8*";
            res = "ptr";

        } else {

            // res = "%.class." + this.id + "*";
            res = "ptr";

        }

        // for (int i = 0; i != this.dim; ++i) {
        // res += "*";
        // }

        return res;
    }
}
