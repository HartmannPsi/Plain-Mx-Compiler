package IR;

import IR.IRNodes.*;

public class IRRetType {
    public IRNode head = null, tail = null;
    public String ret_id = null, lvalue_ptr = null;

    public IRRetType(IRNode head, IRNode tail) {
        this.head = head;
        this.tail = tail;
    }

    public IRRetType(IRNode head, IRNode tail, String ret_id) {
        this.head = head;
        this.tail = tail;
        this.ret_id = ret_id;
    }

    public IRRetType(IRNode head, IRNode tail, String ret_id, String lvalue_ptr) {
        this.head = head;
        this.tail = tail;
        this.ret_id = ret_id;
        this.lvalue_ptr = lvalue_ptr;
    }
}
