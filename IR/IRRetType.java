package IR;

import IR.IRNodes.*;

public class IRRetType {
    public IRNode head = null, tail = null;
    public String ret_id = null;

    public IRRetType(IRNode head, IRNode tail, String ret_id) {
        this.head = head;
        this.tail = tail;
        this.ret_id = ret_id;
    }
}
