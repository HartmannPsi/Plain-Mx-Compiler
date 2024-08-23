package IR;

import defNodes.*;
import defNodes.exprNodes.*;
import defNodes.exprNodes.BinaryOpNode.BinaryOprand;
import defNodes.stmtNodes.*;
import util.error.*;
import util.*;
import java.util.Map;

import org.antlr.v4.parse.ANTLRParser.setElement_return;

import IR.IRNodes.*;
import java.util.HashMap;

// refactor "%this"
// refactor "ptr"

public class IRGenerator {
    ProgNode root;
    int id_serial = 0, label_serial = 0;
    Map<String, Map<String, Integer>> classes = new HashMap<>();
    String global_var_init = "@Global.Var.Init";
    public IRNode beg = null;
    IRNode const_str_head = new IRNode(), const_str_tail = const_str_head;
    // String str = "ptr";

    public IRGenerator(ProgNode root) {
        this.root = root;
    }

    public void generateIR() {
        visit(root);
    }

    public void printIR() {
        beg.printToString();
    }

    int getIdSerial() {
        return id_serial++;
    }

    int getLabelSerial() {
        return label_serial++;
    }

    String renameLabel(String label) {
        return label + ".Label." + getLabelSerial();
    }

    String renameIdLocal(String id) {
        return "%" + id + "." + getIdSerial();
    }

    // String anonIdLocal() {
    // return "%" + ((Integer) getIdSerial()).toString();
    // }

    String ptrLocal() {
        return "%ptr.Pointer." + getIdSerial();
    }

    String ptrGlobal() {
        return "@ptr.Pointer." + getIdSerial();
    }

    String renameIdGlobal(String id) {
        return "@" + id + "." + getIdSerial();
    }

    // String anonIdGlobal() {
    // return "@" + ((Integer) getIdSerial()).toString();
    // }

    void throw_internal(String msg, position pos) {
        throw new internalError(msg, pos);
    }

    IRRetType visit(Node node) {
        if (node instanceof ProgNode) {
            return visit((ProgNode) node);
        } else if (node instanceof BraceStmtNode) {
            return visit((BraceStmtNode) node);
        } else if (node instanceof BreakNode) {
            return visit((BreakNode) node);
        } else if (node instanceof ClsConsNode) {
            return visit((ClsConsNode) node);
        } else if (node instanceof ContinueNode) {
            return visit((ContinueNode) node);
        } else if (node instanceof DefClassNode) {
            return visit((DefClassNode) node);
        } else if (node instanceof DefFuncNode) {
            return visit((DefFuncNode) node);
        } else if (node instanceof DefVarNode) {
            return visit((DefVarNode) node);
        } else if (node instanceof EmptyStmtNode) {
            return visit((EmptyStmtNode) node);
        } else if (node instanceof ExprStmtNode) {
            return visit((ExprStmtNode) node);
        } else if (node instanceof ForStmtNode) {
            return visit((ForStmtNode) node);
        } else if (node instanceof IfStmtNode) {
            return visit((IfStmtNode) node);
        } else if (node instanceof ReturnNode) {
            return visit((ReturnNode) node);
        } else if (node instanceof WhileStmtNode) {
            return visit((WhileStmtNode) node);
        } else if (node instanceof ArrayAccessNode) {
            return visit((ArrayAccessNode) node);
        } else if (node instanceof ArrayNode) {
            return visit((ArrayNode) node);
        } else if (node instanceof BinaryOpNode) {
            return visit((BinaryOpNode) node);
        } else if (node instanceof BoolConstNode) {
            return visit((BoolConstNode) node);
        } else if (node instanceof FStringNode) {
            return visit((FStringNode) node);
        } else if (node instanceof FuncCallNode) {
            return visit((FuncCallNode) node);
        } else if (node instanceof IdNode) {
            return visit((IdNode) node);
        } else if (node instanceof MemAccNode) {
            return visit((MemAccNode) node);
        } else if (node instanceof NewExprNode) {
            return visit((NewExprNode) node);
        } else if (node instanceof NullNode) {
            return visit((NullNode) node);
        } else if (node instanceof NumConstNode) {
            return visit((NumConstNode) node);
        } else if (node instanceof StringConstNode) {
            return visit((StringConstNode) node);
        } else if (node instanceof TernaryOpNode) {
            return visit((TernaryOpNode) node);
        } else if (node instanceof ThisNode) {
            return visit((ThisNode) node);
        } else if (node instanceof UnaryOpNode) {
            return visit((UnaryOpNode) node);
        } else if (node instanceof TypeNode) {
            return visit((TypeNode) node);
        } else if (node == null) {
            IRNode empty_node = new IRNode();
            return new IRRetType(empty_node, empty_node);
        } else {
            throw_internal("Unknown Node Type", node.pos);
        }

        IRNode empty_node = new IRNode();
        return new IRRetType(empty_node, empty_node);
    }

    ////////////////////////////////////////////////////////////////////////

    IRRetType visit(ProgNode node) {

        IRRetType func_decl = BuiltinDecl();
        IRRetType glb_init = GlobalVarInit();
        beg = func_decl.head;
        func_decl.tail.next = glb_init.head;
        IRNode tail = glb_init.tail;

        for (int i = 0; i != node.global_stmt.length; ++i) {
            IRRetType now = visit(node.global_stmt[i]);
            tail.next = now.head;
            tail = now.tail;
        }

        tail.next = const_str_head;
        tail = const_str_tail;

        return new IRRetType(beg, tail, null);
    }

    IRRetType BuiltinDecl() {
        IRDclFuncNode print_node = new IRDclFuncNode(), println_node = new IRDclFuncNode(),
                printInt_node = new IRDclFuncNode(), printlnInt_node = new IRDclFuncNode(),
                getString_node = new IRDclFuncNode(), getInt_node = new IRDclFuncNode(),
                toString_node = new IRDclFuncNode(), boolToString_node = new IRDclFuncNode(),
                string_length_node = new IRDclFuncNode(), string_substring_node = new IRDclFuncNode(),
                string_parseInt_node = new IRDclFuncNode(), string_ord_node = new IRDclFuncNode(),
                string_add_node = new IRDclFuncNode(), string_eq_node = new IRDclFuncNode(),
                string_ne_node = new IRDclFuncNode(), string_lt_node = new IRDclFuncNode(),
                string_le_node = new IRDclFuncNode(), string_gt_node = new IRDclFuncNode(),
                string_ge_node = new IRDclFuncNode(), array_malloc_node = new IRDclFuncNode(),
                array_size_node = new IRDclFuncNode(), malloc_node = new IRDclFuncNode();
        IRNLNode nl_node = new IRNLNode();

        print_node.result_tp = "void";
        print_node.func_name = "@print";
        print_node.tps = new String[] { "ptr" };
        print_node.ids = new String[] { "%0" };
        print_node.next = println_node;

        println_node.result_tp = "void";
        println_node.func_name = "@println";
        println_node.tps = new String[] { "ptr" };
        println_node.ids = new String[] { "%0" };
        println_node.next = printInt_node;

        printInt_node.result_tp = "void";
        printInt_node.func_name = "@printInt";
        printInt_node.tps = new String[] { "i32" };
        printInt_node.ids = new String[] { "%0" };
        printInt_node.next = printlnInt_node;

        printlnInt_node.result_tp = "void";
        printlnInt_node.func_name = "@printlnInt";
        printlnInt_node.tps = new String[] { "i32" };
        printlnInt_node.ids = new String[] { "%0" };
        printlnInt_node.next = getString_node;

        getString_node.result_tp = "ptr";
        getString_node.func_name = "@getString";
        getString_node.next = getInt_node;

        getInt_node.result_tp = "i32";
        getInt_node.func_name = "@getInt";
        getInt_node.next = toString_node;

        toString_node.result_tp = "ptr";
        toString_node.func_name = "@toString";
        toString_node.tps = new String[] { "i32" };
        toString_node.ids = new String[] { "%0" };
        toString_node.next = boolToString_node;

        boolToString_node.result_tp = "ptr";
        boolToString_node.func_name = "@boolToString";
        boolToString_node.tps = new String[] { "i1" };
        boolToString_node.ids = new String[] { "%0" };
        boolToString_node.next = string_length_node;

        string_length_node.result_tp = "i32";
        string_length_node.func_name = "@string.length";
        string_length_node.tps = new String[] { "ptr" };
        string_length_node.ids = new String[] { "%0" };
        string_length_node.next = string_substring_node;

        string_substring_node.result_tp = "ptr";
        string_substring_node.func_name = "@string.substring";
        string_substring_node.tps = new String[] { "ptr", "i32", "i32" };
        string_substring_node.ids = new String[] { "%0", "%1", "%2" };
        string_substring_node.next = string_parseInt_node;

        string_parseInt_node.result_tp = "i32";
        string_parseInt_node.func_name = "@string.parseInt";
        string_parseInt_node.tps = new String[] { "ptr" };
        string_parseInt_node.ids = new String[] { "%0" };
        string_parseInt_node.next = string_ord_node;

        string_ord_node.result_tp = "i32";
        string_ord_node.func_name = "@string.ord";
        string_ord_node.tps = new String[] { "ptr", "i32" };
        string_ord_node.ids = new String[] { "%0", "%1" };
        string_ord_node.next = string_add_node;

        string_add_node.result_tp = "ptr";
        string_add_node.func_name = "@string.add";
        string_add_node.tps = new String[] { "ptr", "ptr" };
        string_add_node.ids = new String[] { "%0", "%1" };
        string_add_node.next = string_eq_node;

        string_eq_node.result_tp = "i1";
        string_eq_node.func_name = "@string.eq";
        string_eq_node.tps = new String[] { "ptr", "ptr" };
        string_eq_node.ids = new String[] { "%0", "%1" };
        string_eq_node.next = string_ne_node;

        string_ne_node.result_tp = "i1";
        string_ne_node.func_name = "@string.ne";
        string_ne_node.tps = new String[] { "ptr", "ptr" };
        string_ne_node.ids = new String[] { "%0", "%1" };
        string_ne_node.next = string_lt_node;

        string_lt_node.result_tp = "i1";
        string_lt_node.func_name = "@string.lt";
        string_lt_node.tps = new String[] { "ptr", "ptr" };
        string_lt_node.ids = new String[] { "%0", "%1" };
        string_lt_node.next = string_le_node;

        string_le_node.result_tp = "i1";
        string_le_node.func_name = "@string.le";
        string_le_node.tps = new String[] { "ptr", "ptr" };
        string_le_node.ids = new String[] { "%0", "%1" };
        string_le_node.next = string_gt_node;

        string_gt_node.result_tp = "i1";
        string_gt_node.func_name = "@string.gt";
        string_gt_node.tps = new String[] { "ptr", "ptr" };
        string_gt_node.ids = new String[] { "%0", "%1" };
        string_gt_node.next = string_ge_node;

        string_ge_node.result_tp = "i1";
        string_ge_node.func_name = "@string.ge";
        string_ge_node.tps = new String[] { "ptr", "ptr" };
        string_ge_node.ids = new String[] { "%0", "%1" };
        string_ge_node.next = array_malloc_node;

        array_malloc_node.result_tp = "ptr";
        array_malloc_node.func_name = "@array.malloc";
        array_malloc_node.tps = new String[] { "i32" };
        array_malloc_node.ids = new String[] { "%0" };
        array_malloc_node.next = array_size_node;

        array_size_node.result_tp = "i32";
        array_size_node.func_name = "@array.size";
        array_size_node.tps = new String[] { "ptr" };
        array_size_node.ids = new String[] { "%0" };
        array_size_node.next = malloc_node;

        malloc_node.result_tp = "ptr";
        malloc_node.func_name = "@malloc";
        malloc_node.tps = new String[] { "i32" };
        malloc_node.ids = new String[] { "%0" };
        malloc_node.next = nl_node;

        return new IRRetType(print_node, nl_node, null);
    }

    IRRetType GlobalVarInit() {
        // "@Global.Var.Init"
        IRDefFuncNode def_node = new IRDefFuncNode();
        def_node.func_name = global_var_init;
        def_node.result_tp = "void";
        // System.out.println("define void " + global_var_init + "() {");
        IRLabelNode label_node = new IRLabelNode();
        label_node.label = renameLabel("Init.Entry");
        def_node.stmt = label_node;
        IRNode tail = def_node.stmt;
        for (Node _node : root.global_stmt) {
            if (_node instanceof DefVarNode) {

                DefVarNode node = (DefVarNode) _node;
                Type tp = ((TypeNode) node.type).type;
                String rename_tp;
                // if (tp.dim > 0) {
                // rename_tp = "ptr";
                // } else if (root.rename_cls.containsKey(tp.id)) {
                // rename_tp = root.rename_cls.get(tp.id);
                // } else {
                rename_tp = tp.getLLVMType();
                // }

                for (int i = 0; i != node.ids.length; ++i) {
                    if (node.inits[i] != null) {
                        IRRetType init_id = visit(node.inits[i]);
                        tail.next = init_id.head;
                        String dest_id = ((IdNode) node.ids[i]).rename_id;
                        IRStoreNode st_node = new IRStoreNode();
                        st_node.tp = rename_tp;
                        st_node.value = init_id.ret_id;
                        st_node.ptr = dest_id;
                        init_id.tail.next = st_node;
                        tail = st_node;
                        // System.out.println("store " + rename_tp + " " + init_id + ", ptr " +
                        // dest_id);
                    }
                }
            }
        }

        IRRetNode ret_node = new IRRetNode();
        ret_node.tp = "void";
        tail.next = ret_node;
        tail = ret_node;
        // System.out.println("}");

        return new IRRetType(def_node, def_node, null);
    }

    IRRetType visit(BraceStmtNode node) {
        IRNode head = new IRNode(), tail = head;
        for (int i = 0; i != node.stmts.length; ++i) {
            IRRetType now = visit(node.stmts[i]);
            tail.next = now.head;
            tail = now.tail;
        }
        return new IRRetType(head, tail, null);
    }

    IRRetType visit(BreakNode node) {
        Node scope = node;
        while (scope != null && !(scope instanceof ForStmtNode || scope instanceof WhileStmtNode)) {
            scope = scope.father;
        }

        IRBrNode br_node = new IRBrNode();

        if (scope == null) {
            throw_internal("Break statement not in loop", node.pos);
        } else if (scope instanceof ForStmtNode) {
            // System.out.println("br label %" + ((ForStmtNode) scope).end_label);
            br_node.label_true = ((ForStmtNode) scope).end_label;
        } else {
            // System.out.println("br label %" + ((WhileStmtNode) scope).end_label);
            br_node.label_true = ((WhileStmtNode) scope).end_label;
        }

        return new IRRetType(br_node, br_node, null);
    }

    IRRetType visit(ClsConsNode node) {

        IRNode head = new IRNode(), tail = head;
        for (Node _node : node.stmt) {
            IRRetType now = visit(_node);
            tail.next = now.head;
            tail = now.tail;
        }
        return new IRRetType(head, tail, null);
    }

    IRRetType visit(ContinueNode node) {
        Node scope = node;
        while (scope != null && !(scope instanceof ForStmtNode || scope instanceof WhileStmtNode)) {
            scope = scope.father;
        }

        IRBrNode br_node = new IRBrNode();

        if (scope == null) {
            throw_internal("Continue statement not in loop", node.pos);
        } else if (scope instanceof ForStmtNode) {
            // System.out.println("br label %" + ((ForStmtNode) scope).step_label);
            br_node.label_true = ((ForStmtNode) scope).step_label;
        } else {
            // System.out.println("br label %" + ((WhileStmtNode) scope).cond_label);
            br_node.label_true = ((WhileStmtNode) scope).cond_label;
        }

        return new IRRetType(br_node, br_node, null);
    }

    IRRetType visit(DefClassNode node) {

        String cls_rename = ((IdNode) node.name).rename_id;
        classes.put(cls_rename, new HashMap<>());
        int location = 0;
        Map<String, Integer> scope = classes.get(cls_rename);
        // boolean first = true;
        ClsConsNode cons_node = null;

        IRDefClsNode def_node = new IRDefClsNode();
        def_node.cls_name = cls_rename;
        int member_cnt = 0;
        // System.out.print(cls_rename + " = type { ");

        if (node.stmt != null) {

            for (Node _node : node.stmt) {
                if (_node instanceof DefVarNode) {
                    DefVarNode var_node = (DefVarNode) _node;
                    member_cnt += var_node.ids.length;
                }
            }

            def_node.tps = new String[member_cnt];
            member_cnt = 0;

            for (int i = 0; i != node.stmt.length; ++i) {// def vars
                if (node.stmt[i] instanceof DefVarNode) {

                    DefVarNode var_node = (DefVarNode) node.stmt[i];
                    Type tp = ((TypeNode) var_node.type).type;
                    String rename_tp = tp.getLLVMType();
                    // if (tp.equal("int") || tp.equal("bool") || tp.equal("string")) {
                    // rename_tp = tp.getLLVMType();
                    // } else if (tp.dim == 0) {
                    // rename_tp = root.rename_cls.get(tp.id);
                    // } else {
                    // rename_tp = "ptr";
                    // }

                    // if (first) {
                    // first = false;
                    // } else {
                    // System.out.print(", ");
                    // }

                    for (int j = 0; j != var_node.ids.length; ++j) {
                        // System.out.print(rename_tp);
                        // if (j != var_node.ids.length - 1) {
                        // System.out.print(", ");
                        // }
                        def_node.tps[member_cnt++] = rename_tp;
                        scope.put(((IdNode) var_node.ids[j]).rename_id, location++);

                    }

                } else if (node.stmt[i] instanceof ClsConsNode) {
                    cons_node = (ClsConsNode) node.stmt[i];
                }
            }
            // System.out.println(" }");

            // constructor
            IRDefFuncNode cons_def_node = new IRDefFuncNode();
            def_node.next = cons_def_node;
            cons_def_node.func_name = node.getConsRename();
            cons_def_node.result_tp = "void";
            cons_def_node.ids = new String[1];
            cons_def_node.tps = new String[1];
            cons_def_node.ids[0] = "%this";
            cons_def_node.tps[0] = "ptr";
            IRLabelNode cons_label_node = new IRLabelNode();
            cons_label_node.label = renameLabel("Cons.Entry");
            cons_def_node.stmt = cons_label_node;
            IRNode tail = cons_def_node.stmt;
            // System.out.println("define void " + node.getConsRename() + "(" + cls_rename +
            // " %this) {");

            for (Node _node : node.stmt) {// init member vars
                if (_node instanceof DefVarNode) {
                    DefVarNode var_node = (DefVarNode) _node;
                    Type tp = ((TypeNode) var_node.type).type;
                    String rename_tp;
                    if (tp.equal("int") || tp.equal("bool") || tp.equal("string")) {
                        rename_tp = tp.getLLVMType();
                    } else if (tp.dim == 0) {
                        rename_tp = root.rename_cls.get(tp.id);
                    } else {
                        rename_tp = "ptr";
                    }

                    for (int i = 0; i != var_node.inits.length; ++i) {
                        if (var_node.inits[i] != null) {
                            IRRetType init_id = visit(var_node.inits[i]);
                            tail.next = init_id.head;
                            int dest_serial = scope.get(((IdNode) var_node.ids[i]).rename_id);
                            String tmp_ptr = ptrLocal();

                            IRGetEleNode ele_node = new IRGetEleNode();
                            IRStoreNode st_node = new IRStoreNode();

                            init_id.tail.next = ele_node;
                            ele_node.next = st_node;
                            tail = st_node;

                            ele_node.result = tmp_ptr;
                            ele_node.tp = cls_rename;
                            ele_node.ptr = "%this";
                            ele_node.tps = new String[] { "i32", "i32" };
                            ele_node.idxs = new String[] { "0", Integer.toString(dest_serial) };

                            st_node.tp = rename_tp;
                            st_node.value = init_id.ret_id;
                            st_node.ptr = tmp_ptr;

                            // System.out.println(tmp_ptr + " = getelementptr " + cls_rename + ", ptr %this,
                            // i32 0, i32 "
                            // + dest_serial);
                            // System.out.println("store " + rename_tp + " " + init_id + ", ptr " +
                            // tmp_ptr);
                        }
                    }
                }
            }

            if (cons_node != null) {// visit constructor
                IRRetType cons = visit(cons_node);
                tail.next = cons.head;
                tail = cons.tail;
            }
            IRRetNode ret_node = new IRRetNode();
            ret_node.tp = "void";
            tail.next = ret_node;
            tail = ret_node;

            // System.out.println("}");
            tail = cons_def_node;

            for (Node _node : node.stmt) {// process funcs
                if (_node instanceof DefFuncNode) {
                    IRRetType now = visit(_node);
                    tail.next = now.head;
                    tail = now.tail;
                }
            }

            IRGlbInitNode init_node = new IRGlbInitNode();
            String cls_size = "@size." + cls_rename.substring(1, cls_rename.length());
            init_node.result = cls_size;
            init_node.tp = "i32";
            init_node.val = Integer.toString(location * 4);
            tail.next = init_node;
            tail = init_node;

            return new IRRetType(def_node, tail, null);
        } else {
            // System.out.println(" }");

            IRGlbInitNode init_node = new IRGlbInitNode();
            String cls_size = "@size." + cls_rename.substring(1, cls_rename.length());
            init_node.result = cls_size;
            init_node.tp = "i32";
            init_node.val = Integer.toString(0);
            def_node.next = init_node;

            return new IRRetType(def_node, init_node, null);
        }

        // return null;
    }

    IRRetType visit(DefFuncNode node) {

        Type ret_tp = ((TypeNode) node.type).type;
        String rename_ret_tp;
        String rename_id = ((IdNode) node.name).rename_id;

        IRDefFuncNode def_node = new IRDefFuncNode();

        // if (ret_tp.dim > 0) {
        // rename_ret_tp = "ptr";

        // } else if (root.rename_cls.containsKey(ret_tp.id)) {
        // rename_ret_tp = root.rename_cls.get(ret_tp.id);

        // } else {
        rename_ret_tp = ret_tp.getLLVMType();

        // }

        // System.out.print("define " + rename_ret_tp + " " + rename_id + "(");
        def_node.func_name = rename_id;
        def_node.result_tp = rename_ret_tp;
        int arg_cnt = node.ids == null ? 0 : node.ids.length;
        if (node.father instanceof DefClassNode) {
            ++arg_cnt;
        }
        def_node.ids = new String[arg_cnt];
        def_node.tps = new String[arg_cnt];
        arg_cnt = 0;

        if (node.father instanceof DefClassNode) {// add "this" pointer
            String cls_tp = "ptr";
            def_node.ids[arg_cnt] = "%this";
            def_node.tps[arg_cnt++] = cls_tp;
            // System.out.print(cls_tp + " %this");

            // if (node.ids != null) {
            // System.out.print(", ");
            // }
        }

        if (node.ids != null) {
            // String[] args = new String[node.ids.length + 1];
            // for (String arg : args) {
            // arg = renameIdLocal("Func.Arg");
            // }

            for (int i = 0; i != node.ids.length; ++i) {
                Type tp = ((TypeNode) node.tps[i]).type;
                String rename_tp;
                if (tp.dim > 0) {
                    rename_tp = "ptr";

                    // } else if (root.rename_cls.containsKey(tp.id)) {
                    // rename_tp = root.rename_cls.get(tp.id);

                } else {
                    rename_tp = tp.getLLVMType();

                }
                def_node.ids[arg_cnt] = renameIdLocal("Func.Arg");
                def_node.tps[arg_cnt++] = rename_tp;
                // System.out.print(rename_tp + " " + ((IdNode) node.ids[i]).rename_id);
                // if (i != node.ids.length - 1) {
                // System.out.print(", ");
                // }
            }
        }

        // System.out.println(") {");
        IRNode tail;

        if (((IdNode) node.name).id.equals("main")) {
            IRCallNode call_node = new IRCallNode();
            call_node.func_name = global_var_init;
            call_node.res_tp = "void";
            IRLabelNode label_node = new IRLabelNode();
            label_node.label = renameLabel("Func.Entry");
            def_node.stmt = label_node;
            label_node.next = call_node;
            tail = call_node;
            // System.out.println("call void " + global_var_init + "()");
        } else {
            IRLabelNode label_node = new IRLabelNode();
            label_node.label = renameLabel("Func.Entry");
            def_node.stmt = label_node;
            tail = label_node;
        }

        if (node.ids != null) {
            int offset;
            if (node.father instanceof DefClassNode) {
                offset = 1;
            } else {
                offset = 0;
            }
            for (int i = 0; i != node.ids.length; ++i) {
                IRAllocaNode alloca_node = new IRAllocaNode();
                alloca_node.result = ((IdNode) node.ids[i]).rename_id;
                alloca_node.tp = def_node.tps[i + offset];
                tail.next = alloca_node;
                tail = alloca_node;

                IRStoreNode st_node = new IRStoreNode();
                st_node.tp = def_node.tps[i + offset];
                st_node.value = def_node.ids[i + offset];
                st_node.ptr = ((IdNode) node.ids[i]).rename_id;
                tail.next = st_node;
                tail = st_node;
                // System.out.println(def_node.ids[i + offset] + " = alloca " + def_node.tps[i +
                // offset]);
            }
        }

        if (node.stmt != null) {
            for (Node subnode : node.stmt) {
                IRRetType now = visit(subnode);
                tail.next = now.head;
                tail = now.tail;
            }
        }

        if (((TypeNode) node.type).type.equal("int")) {
            IRRetNode ret_node = new IRRetNode();
            ret_node.tp = "i32";
            ret_node.val = "0";
            tail.next = ret_node;
            tail = ret_node;
            // System.out.println("call void " + global_var_init + "()");
        } else if (((TypeNode) node.type).type.equal("void")) {
            IRRetNode ret_node = new IRRetNode();
            ret_node.tp = "void";
            tail.next = ret_node;
            tail = ret_node;
            // System.out.println("ret void");

        } else if (((TypeNode) node.type).type.equal("bool")) {
            IRRetNode ret_node = new IRRetNode();
            ret_node.tp = "i1";
            ret_node.val = "false";
            tail.next = ret_node;
            tail = ret_node;
            // System.out.println("call void " + global_var_init + "()");
        } else {
            IRRetNode ret_node = new IRRetNode();
            ret_node.tp = "ptr";
            ret_node.val = "null";
            tail.next = ret_node;
            tail = ret_node;
        }

        // System.out.println("}");
        return new IRRetType(def_node, def_node, null);
    }

    IRRetType visit(DefVarNode node) {

        Type tp = ((TypeNode) node.type).type;
        String rename_tp;

        IRNode head = new IRNode(), tail = head;
        rename_tp = tp.getLLVMType();

        if (node.father instanceof ProgNode) {// global vars

            for (int i = 0; i != node.ids.length; ++i) {
                IdNode _id = (IdNode) node.ids[i];
                IRGlbInitNode init_node = new IRGlbInitNode();
                init_node.result = _id.rename_id;
                init_node.tp = rename_tp;
                tail.next = init_node;
                tail = init_node;
                // System.out.println(_id.rename_id + " = global " + rename_tp + " 0");
            }

        } else {// local vars

            for (int i = 0; i != node.ids.length; ++i) {
                IdNode _id = (IdNode) node.ids[i];
                IRAllocaNode alloca_node = new IRAllocaNode();
                alloca_node.result = _id.rename_id;
                alloca_node.tp = rename_tp;
                tail.next = alloca_node;
                tail = alloca_node;
                // System.out.println(_id.rename_id + " = alloca " + rename_tp);

                if (node.inits[i] != null) {
                    IRRetType init_id = visit(node.inits[i]);
                    tail.next = init_id.head;
                    IRStoreNode st_node = new IRStoreNode();
                    st_node.tp = rename_tp;
                    st_node.value = init_id.ret_id;
                    st_node.ptr = _id.rename_id;
                    init_id.tail.next = st_node;
                    tail = st_node;
                    // System.out.println("store " + rename_tp + " " + init_id + ", ptr " + _id);
                }
            }

        }

        return new IRRetType(head, tail, null);
    }

    IRRetType visit(EmptyStmtNode node) {
        IRNode empty_node = new IRNode();
        return new IRRetType(empty_node, empty_node, null);
    }

    IRRetType visit(ExprStmtNode node) {
        IRRetType _node = visit(node.expr);
        return new IRRetType(_node.head, _node.tail, null);
    }

    IRRetType visit(ForStmtNode node) {
        String cond_label = renameLabel("For.Cond"),
                body_label = renameLabel("For.Body"), step_label = renameLabel("For.Step"),
                end_label = renameLabel("For.End");
        node.end_label = end_label;
        node.step_label = step_label;

        IRNode head, tail;
        IRLabelNode cond_node = new IRLabelNode(), body_node = new IRLabelNode(),
                step_node = new IRLabelNode(), end_node = new IRLabelNode();
        head = tail = new IRNode();
        // init_node.label = init_label;

        // System.out.println(init_label + ":");
        IRRetType inits = visit(node.init);
        tail.next = inits.head;
        tail = inits.tail;
        IRBrNode br1_node = new IRBrNode();
        br1_node.label_true = cond_label;
        tail.next = br1_node;
        tail = br1_node;
        // System.out.println("br label %" + cond_label);

        cond_node.label = cond_label;
        tail.next = cond_node;
        tail = cond_node;
        // System.out.println(cond_label + ":");
        if (node.cond != null) {
            IRRetType cond_id = visit(node.cond);
            tail.next = cond_id.head;
            tail = cond_id.tail;
            IRBrNode br2_node = new IRBrNode();
            br2_node.label_true = body_label;
            br2_node.label_false = end_label;
            br2_node.cond = cond_id.ret_id;
            tail.next = br2_node;
            tail = br2_node;
            // System.out.println("br i1 " + cond_id + ", label %" + body_label + ", label
            // %" + end_label);
        } else {
            IRBrNode br2_node = new IRBrNode();
            br2_node.label_true = body_label;
            tail.next = br2_node;
            tail = br2_node;
            // System.out.println("br label %" + body_label);
        }

        body_node.label = body_label;
        tail.next = body_node;
        tail = body_node;
        // System.out.println(body_label + ":");

        if (node.stmts != null) {
            for (int i = 0; i != node.stmts.length; ++i) {
                IRRetType now = visit(node.stmts[i]);
                tail.next = now.head;
                tail = now.tail;
            }
        }
        IRBrNode br3_node = new IRBrNode();
        br3_node.label_true = step_label;
        tail.next = br3_node;
        tail = br3_node;
        // System.out.println("br label %" + step_label);

        step_node.label = step_label;
        tail.next = step_node;
        tail = step_node;
        // System.out.println(step_label + ":");
        IRRetType steps = visit(node.step);
        tail.next = steps.head;
        tail = steps.tail;
        IRBrNode br4_node = new IRBrNode();
        br4_node.label_true = cond_label;
        tail.next = br4_node;
        tail = br4_node;
        // System.out.println("br label %" + cond_label);

        end_node.label = end_label;
        tail.next = end_node;
        tail = end_node;
        // System.out.println(end_label + ":");
        return new IRRetType(head, tail, null);
    }

    IRRetType visit(IfStmtNode node) {

        String then_label = renameLabel("If.Then"),
                else_label = renameLabel("If.Else"),
                end_label = renameLabel("If.End");
        IRLabelNode then_node = new IRLabelNode(), else_node = new IRLabelNode(),
                end_node = new IRLabelNode();
        IRNode head, tail;
        head = tail = new IRNode();

        // cond_node.label = cond_label;
        // System.out.println(cond_label + ":");
        IRRetType cond_id = visit(node.cond);
        tail.next = cond_id.head;
        tail = cond_id.tail;
        IRBrNode br1_node = new IRBrNode();
        br1_node.label_true = then_label;
        br1_node.label_false = else_label;
        br1_node.cond = cond_id.ret_id;
        tail.next = br1_node;
        tail = br1_node;
        // System.out.println("br i1 " + cond_id + ", label %" + then_label + ", label
        // %" + else_label);

        then_node.label = then_label;
        tail.next = then_node;
        tail = then_node;
        // System.out.println(then_label + ":");
        IRRetType thens = visit(node.then_stmt);
        tail.next = thens.head;
        tail = thens.tail;
        IRBrNode br2_node = new IRBrNode();
        br2_node.label_true = end_label;
        tail.next = br2_node;
        tail = br2_node;
        // System.out.println("br label %" + end_label);

        else_node.label = else_label;
        tail.next = else_node;
        tail = else_node;
        // System.out.println(else_label + ":");
        IRRetType elses = visit(node.else_stmt);
        tail.next = elses.head;
        tail = elses.tail;
        IRBrNode br3_node = new IRBrNode();
        br3_node.label_true = end_label;
        tail.next = br3_node;
        tail = br3_node;
        // System.out.println("br label %" + end_label);

        end_node.label = end_label;
        tail.next = end_node;
        tail = end_node;
        // System.out.println(end_label + ":");
        return new IRRetType(head, tail, null);
    }

    IRRetType visit(ReturnNode node) {
        IRRetNode ret_node = new IRRetNode();
        if (node.expr == null) {
            // System.out.println("ret void");
            ret_node.tp = "void";
            return new IRRetType(ret_node, ret_node, null);
        } else {
            IRRetType ret_id = visit(node.expr);
            ret_node.tp = ((ExprNode) node.expr).type.getLLVMType();
            ret_node.val = ret_id.ret_id;
            ret_id.tail.next = ret_node;
            // System.out.println("ret " + ((ExprNode) node.expr).type.getLLVMType() + " " +
            // ret_id);
            return new IRRetType(ret_id.head, ret_node, null);
        }

    }

    IRRetType visit(WhileStmtNode node) {
        String cond_label = renameLabel("While.Cond"), body_label = renameLabel("While.Body"),
                end_label = renameLabel("While.End");
        node.end_label = end_label;
        node.cond_label = cond_label;

        IRNode head, tail;
        IRLabelNode cond_node = new IRLabelNode(), body_node = new IRLabelNode(), end_node = new IRLabelNode();
        IRBrNode br_node = new IRBrNode();
        br_node.label_true = cond_label;
        head = br_node;
        head.next = cond_node;
        tail = cond_node;

        cond_node.label = cond_label;
        // System.out.println(cond_label + ":");
        IRRetType cond_id = visit(node.cond);
        tail.next = cond_id.head;
        tail = cond_id.tail;
        IRBrNode br1_node = new IRBrNode();
        br1_node.label_true = body_label;
        br1_node.label_false = end_label;
        br1_node.cond = cond_id.ret_id;
        tail.next = br1_node;
        tail = br1_node;
        // System.out.println("br i1 " + cond_id + ", label %" + body_label + ", label
        // %" + end_label);

        body_node.label = body_label;
        tail.next = body_node;
        tail = body_node;
        // System.out.println(body_label + ":");
        if (node.stmts != null) {
            for (int i = 0; i != node.stmts.length; ++i) {
                IRRetType now = visit(node.stmts[i]);
                tail.next = now.head;
                tail = now.tail;
            }
        }
        IRBrNode br2_node = new IRBrNode();
        br2_node.label_true = cond_label;
        tail.next = br2_node;
        tail = br2_node;
        // System.out.println("br label %" + cond_label);

        end_node.label = end_label;
        tail.next = end_node;
        tail = end_node;
        // System.out.println(end_label + ":");
        return new IRRetType(head, tail, null);
    }

    IRRetType visit(ArrayAccessNode node) {
        IRNode head, tail;
        IRRetType arr_id = visit(node.array), ser_id = visit(node.serial);
        head = arr_id.head;
        arr_id.tail.next = ser_id.head;
        tail = ser_id.tail;
        String ret_id = renameIdLocal("ArrayAccessRetVal");
        String lvalue_ptr = ptrLocal();
        String ret_tp = node.type.getLLVMType();

        IRGetEleNode ele_node = new IRGetEleNode();
        ele_node.result = lvalue_ptr;
        ele_node.tp = node.type.getLLVMType();
        ele_node.ptr = arr_id.ret_id;
        ele_node.tps = new String[] { "i32" };
        ele_node.idxs = new String[] { ser_id.ret_id };
        tail.next = ele_node;
        tail = ele_node;

        IRLoadNode load_node = new IRLoadNode();
        load_node.result = ret_id;
        load_node.tp = ret_tp;
        load_node.ptr = lvalue_ptr;
        tail.next = load_node;
        tail = load_node;

        return new IRRetType(head, tail, ret_id, lvalue_ptr);
    }

    IRRetType visit(ArrayNode node) {
        String ret_id = ptrLocal();
        String ret_tp = node.type.getLLVMType();
        Type _tp = node.type.clone();
        --_tp.dim;
        String ele_tp = _tp.getLLVMType();
        int size = node.vals.length;
        IRNode head, tail;
        IRCallNode call_node = new IRCallNode();
        head = tail = call_node;
        call_node.func_name = "@array.malloc";
        call_node.res_tp = ret_tp;
        call_node.result = ret_id;
        call_node.args = new String[] { Integer.toString(size) };
        call_node.tps = new String[] { "i32" };

        for (int i = 0; i != node.vals.length; ++i) {
            ExprNode _node = (ExprNode) node.vals[i];
            IRRetType now = visit(_node);
            tail.next = now.head;
            tail = now.tail;

            String tmp_ptr = ptrLocal();
            IRGetEleNode ele_node = new IRGetEleNode();
            ele_node.result = tmp_ptr;
            ele_node.tp = ret_tp;
            ele_node.ptr = ret_id;
            ele_node.tps = new String[] { "i32" };
            ele_node.idxs = new String[] { Integer.toString(i) };
            tail.next = ele_node;
            tail = ele_node;

            IRStoreNode st_node = new IRStoreNode();
            st_node.tp = ele_tp;
            st_node.value = now.ret_id;
            st_node.ptr = tmp_ptr;
            tail.next = st_node;
            tail = st_node;
        }

        return new IRRetType(head, tail, ret_id);
    }

    IRRetType visit(BinaryOpNode node) {
        String ret_id = renameIdLocal("BinaryRetVal");
        String ret_tp = node.type.getLLVMType();
        IRNode head = null, tail = null;

        // String operator = null;

        if (node.oprand == BinaryOpNode.BinaryOprand.Mul) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "mul";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = mul " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOpNode.BinaryOprand.Div) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "sdiv";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = sdiv " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOpNode.BinaryOprand.Mod) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "srem";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = srem " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOprand.Add) {

            if (node.type.equal("string")) {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.add";
                call_node.res_tp = "ptr";
                call_node.result = ret_id;
                call_node.args = new String[] { lhs_id.ret_id, rhs_id.ret_id };
                call_node.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(
                // ret_id + " = call ptr @string.add(ptr " + lhs_id + ", ptr " + rhs_id + ")");

            } else {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRBinaryNode bin_node = new IRBinaryNode();
                bin_node.operator = "add";
                bin_node.tp = ret_tp;
                bin_node.op1 = lhs_id.ret_id;
                bin_node.op2 = rhs_id.ret_id;
                bin_node.result = ret_id;
                tail.next = bin_node;
                tail = bin_node;
                // System.out.println(ret_id + " = add " + ret_tp + " " + lhs_id + ", " +
                // rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Sub) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "sub";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = sub " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOprand.ShiftL) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "shl";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = shl " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOprand.ShiftR) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "ashr";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = ashr " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOprand.Lt) {

            if (node.type.equal("string")) {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.lt";
                call_node.res_tp = "i1";
                call_node.result = ret_id;
                call_node.args = new String[] { lhs_id.ret_id, rhs_id.ret_id };
                call_node.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.lt(ptr " + lhs_id + ", ptr "
                // + rhs_id + ")");

            } else {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRIcmpNode cmp_node = new IRIcmpNode();
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();

                cmp_node.cond = "slt";
                cmp_node.tp = cmp_tp;
                cmp_node.op1 = lhs_id.ret_id;
                cmp_node.op2 = rhs_id.ret_id;
                cmp_node.result = ret_id;
                tail.next = cmp_node;
                tail = cmp_node;

                // System.out.println(ret_id + " = icmp slt " + cmp_tp + " " + lhs_id + ", " +
                // rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Le) {

            if (node.type.equal("string")) {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.le";
                call_node.res_tp = "i1";
                call_node.result = ret_id;
                call_node.args = new String[] { lhs_id.ret_id, rhs_id.ret_id };
                call_node.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.le(ptr " + lhs_id + ", ptr "
                // + rhs_id + ")");

            } else {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                IRIcmpNode cmp_node = new IRIcmpNode();
                cmp_node.cond = "sle";
                cmp_node.tp = cmp_tp;
                cmp_node.op1 = lhs_id.ret_id;
                cmp_node.op2 = rhs_id.ret_id;
                cmp_node.result = ret_id;
                tail.next = cmp_node;
                tail = cmp_node;
                // System.out.println(ret_id + " = icmp sle " + cmp_tp + " " + lhs_id + ", " +
                // rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Gt) {

            if (node.type.equal("string")) {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.gt";
                call_node.res_tp = "i1";
                call_node.result = ret_id;
                call_node.args = new String[] { lhs_id.ret_id, rhs_id.ret_id };
                call_node.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.gt(ptr " + lhs_id + ", ptr "
                // + rhs_id + ")");

            } else {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                IRIcmpNode cmp_node = new IRIcmpNode();
                cmp_node.cond = "sgt";
                cmp_node.tp = cmp_tp;
                cmp_node.op1 = lhs_id.ret_id;
                cmp_node.op2 = rhs_id.ret_id;
                cmp_node.result = ret_id;
                tail.next = cmp_node;
                tail = cmp_node;
                // System.out.println(ret_id + " = icmp sgt " + cmp_tp + " " + lhs_id + ", " +
                // rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Ge) {

            if (node.type.equal("string")) {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.ge";
                call_node.res_tp = "i1";
                call_node.result = ret_id;
                call_node.args = new String[] { lhs_id.ret_id, rhs_id.ret_id };
                call_node.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.ge(ptr " + lhs_id + ", ptr "
                // + rhs_id + ")");

            } else {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                IRIcmpNode cmp_node = new IRIcmpNode();
                cmp_node.cond = "sge";
                cmp_node.tp = cmp_tp;
                cmp_node.op1 = lhs_id.ret_id;
                cmp_node.op2 = rhs_id.ret_id;
                cmp_node.result = ret_id;
                tail.next = cmp_node;
                tail = cmp_node;
                // System.out.println(ret_id + " = icmp sge " + cmp_tp + " " + lhs_id + ", " +
                // rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Eq) {

            if (node.type.equal("string")) {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.eq";
                call_node.res_tp = "i1";
                call_node.result = ret_id;
                call_node.args = new String[] { lhs_id.ret_id, rhs_id.ret_id };
                call_node.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.eq(ptr " + lhs_id + ", ptr "
                // + rhs_id + ")");

            } else {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                IRIcmpNode cmp_node = new IRIcmpNode();
                cmp_node.cond = "eq";
                cmp_node.tp = cmp_tp;
                cmp_node.op1 = lhs_id.ret_id;
                cmp_node.op2 = rhs_id.ret_id;
                cmp_node.result = ret_id;
                tail.next = cmp_node;
                tail = cmp_node;
                // System.out.println(ret_id + " = icmp eq " + cmp_tp + " " + lhs_id + ", " +
                // rhs_id);
            }

        } else if (node.oprand == BinaryOprand.Ne) {

            if (node.type.equal("string")) {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.ne";
                call_node.res_tp = "i1";
                call_node.result = ret_id;
                call_node.args = new String[] { lhs_id.ret_id, rhs_id.ret_id };
                call_node.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.ne(ptr " + lhs_id + ", ptr "
                // + rhs_id + ")");

            } else {
                IRRetType lhs_id = visit(node.lhs);
                IRRetType rhs_id = visit(node.rhs);
                head = lhs_id.head;
                lhs_id.tail.next = rhs_id.head;
                tail = rhs_id.tail;
                String cmp_tp = ((ExprNode) node.lhs).type.getLLVMType();
                IRIcmpNode cmp_node = new IRIcmpNode();
                cmp_node.cond = "ne";
                cmp_node.tp = cmp_tp;
                cmp_node.op1 = lhs_id.ret_id;
                cmp_node.op2 = rhs_id.ret_id;
                cmp_node.result = ret_id;
                tail.next = cmp_node;
                tail = cmp_node;
                // System.out.println(ret_id + " = icmp ne " + cmp_tp + " " + lhs_id + ", " +
                // rhs_id);
            }

        } else if (node.oprand == BinaryOprand.BAnd) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "and";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = and " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOprand.BOr) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "or";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = or " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOprand.LAnd) {// short circuit
            String entry_rhs_label = renameLabel("LAnd.Entry.Rhs"),
                    end_label = renameLabel("LAnd.End"), exit_lhs_label = renameLabel("LAnd.Exit.Lhs"),
                    exit_rhs_label = renameLabel("LAnd.Exit.Rhs");
            // String tmp_ptr = ptrLocal();
            // System.out.println(tmp_ptr + " = alloca " + ret_tp);
            IRLabelNode entry_rhs_node = new IRLabelNode(),
                    end_node = new IRLabelNode(), exit_lhs_node = new IRLabelNode(), exit_rhs_node = new IRLabelNode();

            // IRBrNode br_node = new IRBrNode();
            // br_node.label_true = entry_lhs_label;
            // head = tail = br_node;

            // tail.next = entry_lhs_node;
            // tail = entry_lhs_node;
            // entry_lhs_node.label = entry_lhs_label;
            // System.out.println(lhs_label + ":");
            IRRetType lhs_id = visit(node.lhs);
            head = lhs_id.head;
            tail = lhs_id.tail;

            IRBrNode br_exit_lhs_node = new IRBrNode();
            br_exit_lhs_node.label_true = exit_lhs_label;
            tail.next = br_exit_lhs_node;
            tail = br_exit_lhs_node;

            exit_lhs_node.label = exit_lhs_label;
            tail.next = exit_lhs_node;
            tail = exit_lhs_node;

            IRBrNode br1_node = new IRBrNode();
            br1_node.label_true = entry_rhs_label;
            br1_node.label_false = end_label;
            br1_node.cond = lhs_id.ret_id;
            tail.next = br1_node;
            tail = br1_node;
            // System.out.println("store " + ret_tp + " " + lhs_id + ", ptr " + tmp_ptr);
            // System.out.println("br i1 " + lhs_id + ", label %" + rhs_label + ", label %"
            // + end_label);

            entry_rhs_node.label = entry_rhs_label;
            tail.next = entry_rhs_node;
            tail = entry_rhs_node;
            // System.out.println(rhs_label + ":");
            IRRetType rhs_id = visit(node.rhs);
            tail.next = rhs_id.head;
            tail = rhs_id.tail;

            IRBrNode br_exit_rhs_node = new IRBrNode();
            br_exit_rhs_node.label_true = exit_rhs_label;
            tail.next = br_exit_rhs_node;
            tail = br_exit_rhs_node;

            exit_rhs_node.label = exit_rhs_label;
            tail.next = exit_rhs_node;
            tail = exit_rhs_node;

            IRBrNode br2_node = new IRBrNode();
            br2_node.label_true = end_label;
            tail.next = br2_node;
            tail = br2_node;
            // System.out.println("store " + ret_tp + " " + rhs_id + ", ptr " + tmp_ptr);
            // System.out.println("br label %" + end_label);

            end_node.label = end_label;
            tail.next = end_node;
            tail = end_node;
            // System.out.println(end_label + ":");
            // System.out.println(ret_id + " = load " + ret_tp + ", ptr " + tmp_ptr);

            // IRPhiNode phi_node = new IRPhiNode();
            // phi_node.tp = ret_tp;
            // phi_node.result = ret_id;
            // phi_node.vals = new String[] { "false", rhs_id.ret_id };
            // phi_node.labels = new String[] { exit_lhs_label, exit_rhs_label };
            // tail.next = phi_node;
            // tail = phi_node;
            // TODO: replace phi with select

            IRSelectNode select_node = new IRSelectNode();
            select_node.tp = ret_tp;
            select_node.result = ret_id;
            select_node.cond = lhs_id.ret_id;
            select_node.val1 = rhs_id.ret_id;
            select_node.val2 = "false";
            tail.next = select_node;
            tail = select_node;

            // System.out.println(ret_id + " = phi " + ret_tp + " [ false, %" + lhs_label +
            // " ], [ " + rhs_id + ", %"
            // + rhs_label + " ]");

        } else if (node.oprand == BinaryOprand.LOr) {// short circuit
            String entry_rhs_label = renameLabel("LOr.Entry.Rhs"),
                    end_label = renameLabel("LOr.End"), exit_lhs_label = renameLabel("LOr.Exit.Lhs"),
                    exit_rhs_label = renameLabel("LOr.Exit.Rhs");
            // String tmp_ptr = ptrLocal();
            // System.out.println(tmp_ptr + " = alloca " + ret_tp);
            IRLabelNode entry_rhs_node = new IRLabelNode(),
                    end_node = new IRLabelNode(), exit_lhs_node = new IRLabelNode(), exit_rhs_node = new IRLabelNode();

            // IRBrNode br_node = new IRBrNode();
            // br_node.label_true = entry_lhs_label;
            // head = tail = br_node;

            // tail.next = entry_lhs_node;
            // tail = entry_lhs_node;
            // entry_lhs_node.label = entry_lhs_label;
            // System.out.println(lhs_label + ":");
            IRRetType lhs_id = visit(node.lhs);
            head = lhs_id.head;
            tail = lhs_id.tail;

            IRBrNode br_exit_lhs_node = new IRBrNode();
            br_exit_lhs_node.label_true = exit_lhs_label;
            tail.next = br_exit_lhs_node;
            tail = br_exit_lhs_node;

            exit_lhs_node.label = exit_lhs_label;
            tail.next = exit_lhs_node;
            tail = exit_lhs_node;

            IRBrNode br1_node = new IRBrNode();
            br1_node.label_true = end_label;
            br1_node.label_false = entry_rhs_label;
            br1_node.cond = lhs_id.ret_id;
            tail.next = br1_node;
            tail = br1_node;
            // System.out.println("store " + ret_tp + " " + lhs_id + ", ptr " + tmp_ptr);
            // System.out.println("br i1 " + lhs_id + ", label %" + end_label + ", label %"
            // + rhs_label);

            entry_rhs_node.label = entry_rhs_label;
            tail.next = entry_rhs_node;
            tail = entry_rhs_node;
            // System.out.println(rhs_label + ":");
            IRRetType rhs_id = visit(node.rhs);
            tail.next = rhs_id.head;
            tail = rhs_id.tail;

            IRBrNode br_exit_rhs_node = new IRBrNode();
            br_exit_rhs_node.label_true = exit_rhs_label;
            tail.next = br_exit_rhs_node;
            tail = br_exit_rhs_node;

            exit_rhs_node.label = exit_rhs_label;
            tail.next = exit_rhs_node;
            tail = exit_rhs_node;

            IRBrNode br2_node = new IRBrNode();
            br2_node.label_true = end_label;
            tail.next = br2_node;
            tail = br2_node;
            // System.out.println("store " + ret_tp + " " + rhs_id + ", ptr " + tmp_ptr);
            // System.out.println("br label %" + end_label);

            end_node.label = end_label;
            tail.next = end_node;
            tail = end_node;
            // System.out.println(end_label + ":");
            // System.out.println(ret_id + " = load " + ret_tp + ", ptr " + tmp_ptr);

            // IRPhiNode phi_node = new IRPhiNode();
            // phi_node.tp = ret_tp;
            // phi_node.result = ret_id;
            // phi_node.vals = new String[] { "true", rhs_id.ret_id };
            // phi_node.labels = new String[] { exit_lhs_label, exit_rhs_label };
            // tail.next = phi_node;
            // tail = phi_node;

            // replace phi with select
            IRSelectNode select_node = new IRSelectNode();
            select_node.tp = ret_tp;
            select_node.result = ret_id;
            select_node.cond = lhs_id.ret_id;
            select_node.val1 = "true";
            select_node.val2 = rhs_id.ret_id;
            tail.next = select_node;
            tail = select_node;

            // System.out.println(ret_id + " = phi " + ret_tp + " [ true, %" + lhs_label + "
            // ], [ " + rhs_id + ", %"
            // + rhs_label + " ]");

        } else if (node.oprand == BinaryOprand.BXor) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "xor";
            bin_node.tp = ret_tp;
            bin_node.op1 = lhs_id.ret_id;
            bin_node.op2 = rhs_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = xor " + ret_tp + " " + lhs_id + ", " +
            // rhs_id);

        } else if (node.oprand == BinaryOprand.Assign) {
            IRRetType lhs_id = visit(node.lhs);
            IRRetType rhs_id = visit(node.rhs);
            head = lhs_id.head;
            lhs_id.tail.next = rhs_id.head;
            tail = rhs_id.tail;
            IRStoreNode store_node = new IRStoreNode();
            store_node.tp = ret_tp;
            store_node.value = rhs_id.ret_id;
            store_node.ptr = lhs_id.lvalue_ptr;
            tail.next = store_node;
            tail = store_node;

            return new IRRetType(head, tail, rhs_id.ret_id);

        } else {
            throw_internal("Unknown Binary Operator", node.pos);

        }

        return new IRRetType(head, tail, ret_id);
    }

    IRRetType visit(BoolConstNode node) {
        IRNode empty_node = new IRNode();
        return new IRRetType(empty_node, empty_node, node.val ? "true" : "false");
    }

    IRRetType visit(FStringNode node) {

        IRNode head = new IRNode(), tail = head;

        if (node.exprs == null) {

            String ret_id = renameIdGlobal("FStringRetVal");
            IRConstStrNode const_node = new IRConstStrNode();
            const_node.result = ret_id;
            const_node.literal = node.toString(0);
            const_node.prac_val = node.literals[0];
            const_node.length = node.getLength(0) + 1;

            const_str_tail.next = const_node;
            const_str_tail = const_node;
            // System.out.print(ret_id + " = constant [");
            // System.out.print(node.getLength(0) + 1);
            // System.out.print(" x i8] c\"");
            // System.out.print(node.toString(0));
            // System.out.println("\\00\"");
            return new IRRetType(head, tail, ret_id);

        } else {

            IRRetType[] expr_ids = new IRRetType[node.exprs.length];
            String[] str_ids = new String[node.exprs.length];
            for (int i = 0; i != node.exprs.length; ++i) {
                expr_ids[i] = visit(node.exprs[i]);
                tail.next = expr_ids[i].head;
                tail = expr_ids[i].tail;

                if (((ExprNode) node.exprs[i]).type.equal("string")) {

                    str_ids[i] = expr_ids[i].ret_id;

                } else if (((ExprNode) node.exprs[i]).type.equal("int")) {

                    str_ids[i] = renameIdLocal("FString.Int");
                    IRCallNode call_node = new IRCallNode();
                    call_node.func_name = "@toString";
                    call_node.res_tp = "ptr";
                    call_node.result = str_ids[i];
                    call_node.args = new String[] { expr_ids[i].ret_id };
                    call_node.tps = new String[] { "i32" };
                    tail.next = call_node;
                    tail = call_node;
                    // System.out.println(str_ids[i] + " = call ptr @toString(i32 " + expr_ids[i] +
                    // ")");
                    // System.out.println(str_ids[i] + " = call ptr @intToStr(i32 " + expr_ids[i] +
                    // ")");

                } else if (((ExprNode) node.exprs[i]).type.equal("bool")) {

                    str_ids[i] = renameIdLocal("FString.Bool");
                    IRCallNode call_node = new IRCallNode();
                    call_node.func_name = "@boolToString";
                    call_node.res_tp = "ptr";
                    call_node.result = str_ids[i];
                    call_node.args = new String[] { expr_ids[i].ret_id };
                    call_node.tps = new String[] { "i1" };
                    tail.next = call_node;
                    tail = call_node;
                    // System.out.println(str_ids[i] + " = call ptr @boolToString(i1 " + expr_ids[i]
                    // + ")");
                    // System.out.println(str_ids[i] + " = call ptr @boolToStr(i1 " + expr_ids[i] +
                    // ")");

                } else {
                    throw_internal("Invalid Type in FString", node.pos);
                }
            }

            String[] tmp_ids = new String[node.literals.length];
            tmp_ids[0] = renameIdGlobal("FStringTmp");
            IRConstStrNode const_node = new IRConstStrNode();
            const_node.result = tmp_ids[0];
            const_node.literal = node.toString(0);
            const_node.prac_val = node.literals[0];
            const_node.length = node.getLength(0) + 1;
            const_str_tail.next = const_node;
            const_str_tail = const_node;

            // System.out.print(tmp_ids[0] + " = constant [");
            // System.out.print(node.getLength(0) + 1);
            // System.out.print(" x i8] c\"");
            // System.out.print(node.toString(0));
            // System.out.println("\\00\"");

            for (int i = 1; i != node.literals.length; ++i) {
                tmp_ids[i] = renameIdLocal("FStringTmp");
                String inter_id = renameIdLocal("FStringInter");
                String literal_id = renameIdGlobal("FStringLiteral");

                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.add";
                call_node.res_tp = "ptr";
                call_node.result = inter_id;
                call_node.args = new String[] { tmp_ids[i - 1], str_ids[i - 1] };
                call_node.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(
                // inter_id + " = call ptr @string.add(ptr " + tmp_ids[i - 1] + ", ptr " +
                // str_ids[i - 1] + ")");

                IRConstStrNode const_node2 = new IRConstStrNode();
                const_node2.result = literal_id;
                const_node2.literal = node.toString(i);
                const_node2.prac_val = node.literals[i];
                const_node2.length = node.getLength(i) + 1;

                const_str_tail.next = const_node2;
                const_str_tail = const_node2;
                // System.out.print(literal_id + " = constant [");
                // System.out.print(node.getLength(i) + 1);
                // System.out.print(" x i8] c\"");
                // System.out.print(node.toString(i));
                // System.out.println("\\00\"");

                IRCallNode call_node2 = new IRCallNode();
                call_node2.func_name = "@string.add";
                call_node2.res_tp = "ptr";
                call_node2.result = tmp_ids[i];
                call_node2.args = new String[] { inter_id, literal_id };
                call_node2.tps = new String[] { "ptr", "ptr" };
                tail.next = call_node2;
                tail = call_node2;
                // System.out.println(
                // tmp_ids[i] + " = call ptr @string.add(ptr " + inter_id + ", ptr " +
                // literal_id + ")");
            }

            String ret_id = tmp_ids[tmp_ids.length - 1];
            return new IRRetType(head, tail, ret_id);
        }
    }

    IRRetType visit(FuncCallNode node) {

        IRNode head = new IRNode(), tail = head;

        if (node.id instanceof MemAccNode) {// member
            String rename_id = ((IdNode) ((MemAccNode) node.id).member).rename_id;
            String rename_tp = ((IdNode) ((MemAccNode) node.id).member).type.getLLVMType();
            String ret_id = renameIdLocal("FuncCallRetVal");

            IRCallNode call_node = new IRCallNode();

            IRRetType obj_id = visit(((MemAccNode) node.id).object);
            head = obj_id.head;
            tail = obj_id.tail;

            // IRLoadNode load_node = new IRLoadNode();
            // load_node.tp = root.rename_cls.get(((ExprNode) ((MemAccNode)
            // node.id).object).type.id);
            // load_node.ptr = obj_id.ret_id;
            // load_node.result = renameIdLocal("FuncCallObj");
            // tail.next = load_node;
            // tail = load_node;

            if (node.paras != null) {
                call_node.args = new String[node.paras.length + 1];
                call_node.tps = new String[node.paras.length + 1];
            } else {
                call_node.args = new String[1];
                call_node.tps = new String[1];
            }

            call_node.args[0] = obj_id.ret_id;
            call_node.tps[0] = "ptr";

            if (node.paras != null) {
                for (int i = 0; i != node.paras.length; ++i) {
                    IRRetType para_id = visit(node.paras[i]);
                    tail.next = para_id.head;
                    tail = para_id.tail;
                    call_node.args[i + 1] = para_id.ret_id;
                    call_node.tps[i + 1] = ((ExprNode) node.paras[i]).type.getLLVMType();
                }
            }

            call_node.func_name = rename_id;
            call_node.res_tp = rename_tp;
            if (!rename_tp.equals("void")) {
                call_node.result = ret_id;
            } else {
                ret_id = null;
            }
            tail.next = call_node;
            tail = call_node;

            return new IRRetType(head, tail, ret_id);

        } else {

            Node tmp = node;
            while (tmp != null && !(tmp instanceof DefClassNode)) {
                tmp = tmp.father;
            }

            if (tmp == null) {// global
                String rename_id = ((IdNode) node.id).rename_id;
                String rename_tp = ((IdNode) node.id).type.getLLVMType();
                String ret_id = renameIdLocal("FuncCallRetVal");

                IRCallNode call_node = new IRCallNode();

                if (node.paras != null) {
                    call_node.args = new String[node.paras.length];
                    call_node.tps = new String[node.paras.length];

                    for (int i = 0; i != node.paras.length; ++i) {
                        IRRetType para_id = visit(node.paras[i]);
                        tail.next = para_id.head;
                        tail = para_id.tail;
                        call_node.args[i] = para_id.ret_id;
                        call_node.tps[i] = ((ExprNode) node.paras[i]).type.getLLVMType();
                        // IRDebugNode debug_node = new IRDebugNode();
                        // debug_node.message = para_id.ret_id;
                        // tail.next = debug_node;
                        // tail = debug_node;
                        // System.out.println(para_id.ret_id + " FUCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
                    }
                }

                call_node.func_name = rename_id;
                call_node.res_tp = rename_tp;
                if (!rename_tp.equals("void")) {
                    call_node.result = ret_id;
                } else {
                    ret_id = null;
                }
                tail.next = call_node;
                tail = call_node;

                return new IRRetType(head, tail, ret_id);

            } else {// classified discussion
                DefClassNode def_node = (DefClassNode) tmp;
                Map<String, String> methods = def_node.rename_methods;

                if (methods.containsKey(((IdNode) node.id).id)) {// member

                    String rename_id = ((IdNode) (node.id)).rename_id;
                    String rename_tp = ((IdNode) (node.id)).type.getLLVMType();
                    String ret_id = renameIdLocal("FuncCallRetVal");

                    IRCallNode call_node = new IRCallNode();

                    String obj_id = "%this";
                    // head = obj_id.head;
                    // tail = obj_id.tail;

                    if (node.paras != null) {
                        call_node.args = new String[node.paras.length + 1];
                        call_node.tps = new String[node.paras.length + 1];
                    } else {
                        call_node.args = new String[1];
                        call_node.tps = new String[1];
                    }

                    call_node.args[0] = obj_id;
                    call_node.tps[0] = "ptr";

                    if (node.paras != null) {
                        for (int i = 0; i != node.paras.length; ++i) {
                            IRRetType para_id = visit(node.paras[i]);
                            tail.next = para_id.head;
                            tail = para_id.tail;
                            call_node.args[i + 1] = para_id.ret_id;
                            call_node.tps[i + 1] = ((ExprNode) node.paras[i]).type.getLLVMType();
                        }
                    }

                    call_node.func_name = rename_id;
                    call_node.res_tp = rename_tp;
                    if (!rename_tp.equals("void")) {
                        call_node.result = ret_id;
                    } else {
                        ret_id = null;
                    }
                    tail.next = call_node;
                    tail = call_node;

                    return new IRRetType(head, tail, ret_id);

                } else {// global
                    String rename_id = ((IdNode) node.id).rename_id;
                    String rename_tp = ((IdNode) node.id).type.getLLVMType();
                    String ret_id = renameIdLocal("FuncCallRetVal");

                    IRCallNode call_node = new IRCallNode();

                    if (node.paras != null) {
                        call_node.args = new String[node.paras.length];
                        call_node.tps = new String[node.paras.length];

                        for (int i = 0; i != node.paras.length; ++i) {
                            IRRetType para_id = visit(node.paras[i]);
                            tail.next = para_id.head;
                            tail = para_id.tail;
                            call_node.args[i] = para_id.ret_id;
                            call_node.tps[i] = ((ExprNode) node.paras[i]).type.getLLVMType();
                        }
                    }

                    call_node.func_name = rename_id;
                    call_node.res_tp = rename_tp;
                    if (!rename_tp.equals("void")) {
                        call_node.result = ret_id;
                    } else {
                        ret_id = null;
                    }
                    tail.next = call_node;
                    tail = call_node;

                    return new IRRetType(head, tail, ret_id);
                }
            }
        }
    }

    IRRetType visit(IdNode node) {

        IRNode empty_node = new IRNode();

        if (node.is_var) {

            Node tmp = node;
            while (tmp != null && !(tmp instanceof DefClassNode)) {
                tmp = tmp.father;
            }

            if (tmp == null) {
                String ret_id = renameIdLocal("IdRetVal");
                IRLoadNode load_node = new IRLoadNode();
                load_node.tp = node.type.getLLVMType();
                load_node.result = ret_id;
                load_node.ptr = node.rename_id;
                // System.out.println(ret_id + " = load " + node.type.getLLVMType() + ", ptr " +
                // node.rename_id);
                return new IRRetType(load_node, load_node, ret_id, node.rename_id);

            } else {// classified discussion
                DefClassNode def_node = (DefClassNode) tmp;
                String cls_rename = ((IdNode) def_node.name).rename_id;
                Map<String, Integer> scope = classes.get(cls_rename);

                if (scope.containsKey(node.rename_id)) {// member
                    String ret_id = renameIdLocal("IdRetVal"), lvalue_ptr = ptrLocal();
                    IRGetEleNode ele_node = new IRGetEleNode();
                    IRNode head, tail;
                    head = tail = ele_node;
                    int offset = scope.get(node.rename_id);

                    ele_node.result = lvalue_ptr;
                    ele_node.tp = cls_rename;
                    ele_node.ptr = "%this";
                    ele_node.tps = new String[] { "i32", "i32" };
                    ele_node.idxs = new String[] { "0", Integer.toString(offset) };

                    IRLoadNode load_node = new IRLoadNode();
                    load_node.result = ret_id;
                    load_node.tp = node.type.getLLVMType();
                    load_node.ptr = lvalue_ptr;
                    tail.next = load_node;
                    tail = load_node;
                    return new IRRetType(head, tail, ret_id, lvalue_ptr);

                } else {// global

                    String ret_id = renameIdLocal("IdRetVal");
                    IRLoadNode load_node = new IRLoadNode();
                    load_node.tp = node.type.getLLVMType();
                    load_node.result = ret_id;
                    load_node.ptr = node.rename_id;
                    // System.out.println(ret_id + " = load " + node.type.getLLVMType() + ", ptr " +
                    // node.rename_id);
                    return new IRRetType(load_node, load_node, ret_id, node.rename_id);
                }
            }

        } else {
            return new IRRetType(empty_node, empty_node);
        }
    }

    IRRetType visit(MemAccNode node) {
        if (node.father instanceof FuncCallNode && ((FuncCallNode) node.father).id == node) {// call function
            IRNode empty_node = new IRNode();
            return new IRRetType(empty_node, empty_node);

        } else {// member variable

            IRNode head, tail;
            IRRetType obj_id = visit(node.object);
            head = obj_id.head;
            tail = obj_id.tail;

            Type obj_tp = ((ExprNode) node.object).type;
            String obj_rename_tp = root.rename_cls.get(obj_tp.id);
            Map<String, Integer> scope = classes.get(obj_rename_tp);
            String member_id = ((IdNode) node.member).rename_id;
            String ret_tp = node.type.getLLVMType();
            String ret_id = renameIdLocal("MemAccRetVal"), lvalue_ptr = ptrLocal();
            int offset = scope.get(member_id);

            IRGetEleNode ele_node = new IRGetEleNode();
            ele_node.result = lvalue_ptr;
            ele_node.tp = obj_rename_tp;
            ele_node.ptr = obj_id.ret_id;
            ele_node.tps = new String[] { "i32", "i32" };
            ele_node.idxs = new String[] { "0", Integer.toString(offset) };
            tail.next = ele_node;
            tail = ele_node;

            IRLoadNode load_node = new IRLoadNode();
            load_node.result = ret_id;
            load_node.tp = ret_tp;
            load_node.ptr = lvalue_ptr;
            tail.next = load_node;
            tail = load_node;

            return new IRRetType(head, tail, ret_id, lvalue_ptr);
        }
    }

    IRRetType newExprRecursive(int layer, String[] ids) {
        if (layer == ids.length) {
            IRNode empty_node = new IRNode();
            return new IRRetType(empty_node, empty_node, "null");
        }
        IRNode head = new IRNode(), tail = head;
        String size = ids[layer];
        String step_var = renameIdLocal("NewExprStep");
        String ret_id = renameIdLocal("NewExprRetVal");

        // ret_id = call array.malloc(size)
        IRCallNode call_node = new IRCallNode();
        call_node.func_name = "@array.malloc";
        call_node.res_tp = "ptr";
        call_node.result = ret_id;
        call_node.args = new String[] { ids[layer] };
        call_node.tps = new String[] { "i32" };
        tail.next = call_node;
        tail = call_node;

        // i = alloca i32 0
        IRAllocaNode alloca_node = new IRAllocaNode();
        alloca_node.result = step_var;
        alloca_node.tp = "i32";
        tail.next = alloca_node;
        tail = alloca_node;

        IRStoreNode st_node = new IRStoreNode();
        st_node.tp = "i32";
        st_node.value = "0";
        st_node.ptr = step_var;
        tail.next = st_node;
        tail = st_node;

        String cond_label = renameLabel("NewExpr.Cond"), body_label = renameLabel("NewExpr.Body"),
                end_label = renameLabel("NewExpr.End");

        IRLabelNode cond_node = new IRLabelNode(), body_node = new IRLabelNode(), end_node = new IRLabelNode();

        IRBrNode br_node3 = new IRBrNode();
        br_node3.label_true = cond_label;
        tail.next = br_node3;
        tail = br_node3;

        cond_node.label = cond_label;
        tail.next = cond_node;
        tail = cond_node;

        // tmp = load i32 i
        IRLoadNode load_node = new IRLoadNode();
        String tmp_var = renameIdLocal("NewExprTmp");
        load_node.result = tmp_var;
        load_node.tp = "i32";
        load_node.ptr = step_var;
        tail.next = load_node;
        tail = load_node;

        // cmp = icmp slt i32 tmp, size
        IRIcmpNode icmp_node = new IRIcmpNode();
        icmp_node.cond = "slt";
        icmp_node.tp = "i32";
        icmp_node.op1 = tmp_var;
        icmp_node.op2 = size;
        String cmp = renameIdLocal("NewExprCmp");
        icmp_node.result = cmp;
        tail.next = icmp_node;
        tail = icmp_node;

        // br i1 cmp, label %body, label %end
        IRBrNode br_node = new IRBrNode();
        br_node.label_true = body_label;
        br_node.label_false = end_label;
        br_node.cond = cmp;
        tail.next = br_node;
        tail = br_node;

        body_node.label = body_label;
        tail.next = body_node;
        tail = body_node;
        // nxt = call NewExprRecursive(layer + 1, ids)
        IRRetType next_id = newExprRecursive(layer + 1, ids);
        tail.next = next_id.head;
        tail = next_id.tail;

        // tmp2 = load i32 i
        IRLoadNode load_node2 = new IRLoadNode();
        String tmp_var2 = renameIdLocal("NewExprTmp");
        load_node2.result = tmp_var2;
        load_node2.tp = "i32";
        load_node2.ptr = step_var;
        tail.next = load_node2;
        tail = load_node2;

        // ele_ptr = getelementptr ptr ret_id, i32 tmp
        IRGetEleNode ele_node = new IRGetEleNode();
        String ele_ptr = ptrLocal();
        ele_node.result = ele_ptr;
        ele_node.tp = "ptr";
        ele_node.ptr = ret_id;
        ele_node.tps = new String[] { "i32" };
        ele_node.idxs = new String[] { tmp_var2 };
        tail.next = ele_node;
        tail = ele_node;

        // store ptr nxt, ele_ptr
        IRStoreNode store_node = new IRStoreNode();
        store_node.tp = "ptr";
        store_node.value = next_id.ret_id;
        store_node.ptr = ele_ptr;
        tail.next = store_node;
        tail = store_node;

        // tmp3 = add i32 tmp2, 1
        IRBinaryNode add_node = new IRBinaryNode();
        String tmp_var3 = renameIdLocal("NewExprTmp");
        add_node.operator = "add";
        add_node.tp = "i32";
        add_node.op1 = tmp_var2;
        add_node.op2 = "1";
        add_node.result = tmp_var3;
        tail.next = add_node;
        tail = add_node;

        // store i32 tmp3, i
        IRStoreNode store_node2 = new IRStoreNode();
        store_node2.tp = "i32";
        store_node2.value = tmp_var3;
        store_node2.ptr = step_var;
        tail.next = store_node2;
        tail = store_node2;

        IRBrNode br_node2 = new IRBrNode();
        br_node2.label_true = cond_label;
        tail.next = br_node2;
        tail = br_node2;

        end_node.label = end_label;
        tail.next = end_node;
        tail = end_node;

        return new IRRetType(head, tail, ret_id);
    }

    IRRetType visit(NewExprNode node) {
        if (node.init_array != null) {
            IRRetType arr_id = visit(node.init_array);
            return arr_id;
        }

        IRNode head = new IRNode(), tail = head;

        if (node.type.dim > 0) {
            // String ret_id = renameIdLocal("NewExprRetVal");
            // String ret_tp = node.type.getLLVMType();
            // Type _tp = node.type.clone();

            String[] len_ids = new String[node.lengths.length];
            for (int i = 0; i != node.lengths.length; ++i) {
                IRRetType len_id = visit(node.lengths[i]);
                tail.next = len_id.head;
                tail = len_id.tail;
                len_ids[i] = len_id.ret_id;
            }

            IRRetType arr_id = newExprRecursive(0, len_ids);
            tail.next = arr_id.head;
            tail = arr_id.tail;

            return new IRRetType(head, tail, arr_id.ret_id);

        } else {
            String ret_id = renameIdLocal("NewExprRetVal");
            String size_id = renameIdLocal("NewExprSize");
            String ret_tp = node.type.getLLVMType();
            String rename_tp = root.rename_cls.get(node.type.id);
            String size_ptr = "@size." + rename_tp.substring(1, rename_tp.length());

            IRLoadNode load_node = new IRLoadNode();
            head = tail = load_node;
            load_node.result = size_id;
            load_node.tp = "i32";
            load_node.ptr = size_ptr;

            IRCallNode call_node = new IRCallNode();
            call_node.func_name = "@malloc";
            call_node.res_tp = ret_tp;
            call_node.result = ret_id;
            call_node.args = new String[] { size_id };
            call_node.tps = new String[] { "i32" };
            tail.next = call_node;
            tail = call_node;

            IRCallNode call_node2 = new IRCallNode();
            call_node2.func_name = "@Cons." + rename_tp.substring(1, rename_tp.length());
            call_node2.res_tp = "void";
            call_node2.args = new String[] { ret_id };
            call_node2.tps = new String[] { ret_tp };
            tail.next = call_node2;
            tail = call_node2;

            return new IRRetType(head, tail, ret_id);
        }
    }

    IRRetType visit(NullNode node) {
        IRNode empty_node = new IRNode();
        return new IRRetType(empty_node, empty_node, "null");
    }

    IRRetType visit(NumConstNode node) {
        IRNode empty_node = new IRNode();
        return new IRRetType(empty_node, empty_node, ((Integer) node.val).toString());
    }

    IRRetType visit(StringConstNode node) {
        String ret_id = renameIdGlobal("StringConst");
        IRConstStrNode const_node = new IRConstStrNode();
        const_node.result = ret_id;
        const_node.literal = node.toString();
        const_node.prac_val = node.val;
        const_node.length = node.getLength() + 1;

        const_str_tail.next = const_node;
        const_str_tail = const_node;

        IRNode empty_node = new IRNode();
        // System.out.print(ret_id + " = constant [");
        // System.out.print(node.getLength() + 1);
        // System.out.print(" x i8] c\"");
        // System.out.print(node.toString());
        // System.out.println("\\00\"");
        return new IRRetType(empty_node, empty_node, ret_id);
    }

    IRRetType visit(TernaryOpNode node) {
        String entry_true_label = renameLabel("Ternary.Entry.True"),
                entry_false_label = renameLabel("Ternary.Entry.False"), end_label = renameLabel("Ternary.End"),
                exit_true_label = renameLabel("Ternary.Exit.True"),
                exit_false_label = renameLabel("Ternary.Exit.False");
        String ret_id = renameIdLocal("TernaryRetVal");
        String ret_tp = node.type.getLLVMType();

        IRLabelNode entry_true_node = new IRLabelNode(), entry_false_node = new IRLabelNode(),
                end_node = new IRLabelNode(), exit_true_node = new IRLabelNode(), exit_false_node = new IRLabelNode();
        IRNode head = new IRNode(), tail = head;

        // cond_node.label = cond_label;
        // System.out.println(cond_label + ":");
        // System.out.println(tmp_ptr + " = alloca " + ret_tp);
        IRRetType cond_id = visit(node.cond);
        tail.next = cond_id.head;
        tail = cond_id.tail;
        IRBrNode br1_node = new IRBrNode();
        br1_node.label_true = entry_true_label;
        br1_node.label_false = entry_false_label;
        br1_node.cond = cond_id.ret_id;
        tail.next = br1_node;
        tail = br1_node;
        // System.out.println("br i1 " + cond_id + ", label %" + true_label + ", label
        // %" + false_label);

        entry_true_node.label = entry_true_label;
        tail.next = entry_true_node;
        tail = entry_true_node;
        // System.out.println(true_label + ":");
        IRRetType true_id = visit(node.lhs);
        tail.next = true_id.head;
        tail = true_id.tail;

        IRBrNode br_exit_true_node = new IRBrNode();
        br_exit_true_node.label_true = exit_true_label;
        tail.next = br_exit_true_node;
        tail = br_exit_true_node;

        exit_true_node.label = exit_true_label;
        tail.next = exit_true_node;
        tail = exit_true_node;
        // System.out.println("store " + ret_tp + " " + true_id + ", ptr " + tmp_ptr);
        IRBrNode br2_node = new IRBrNode();
        br2_node.label_true = end_label;
        tail.next = br2_node;
        tail = br2_node;
        // System.out.println("br label %" + end_label);

        entry_false_node.label = entry_false_label;
        tail.next = entry_false_node;
        tail = entry_false_node;
        // System.out.println(false_label + ":");
        IRRetType false_id = visit(node.rhs);
        tail.next = false_id.head;
        tail = false_id.tail;

        IRBrNode br_exit_false_node = new IRBrNode();
        br_exit_false_node.label_true = exit_false_label;
        tail.next = br_exit_false_node;
        tail = br_exit_false_node;

        exit_false_node.label = exit_false_label;
        tail.next = exit_false_node;
        tail = exit_false_node;
        // System.out.println("store " + ret_tp + " " + false_id + ", ptr " + tmp_ptr);
        IRBrNode br3_node = new IRBrNode();
        br3_node.label_true = end_label;
        tail.next = br3_node;
        tail = br3_node;
        // System.out.println("br label %" + end_label);

        end_node.label = end_label;
        tail.next = end_node;
        tail = end_node;
        // System.out.println(end_label + ":");
        // System.out.println(ret_id + " = load " + ret_tp + ", ptr " + tmp_ptr);

        // IRPhiNode phi_node = new IRPhiNode();
        // phi_node.tp = ret_tp;
        // phi_node.result = ret_id;
        // phi_node.vals = new String[] { true_id.ret_id, false_id.ret_id };
        // phi_node.labels = new String[] { exit_true_label, exit_false_label };
        // tail.next = phi_node;
        // tail = phi_node;

        // replace phi with select
        if (ret_tp.equals("void")) {
            ret_tp = "ptr";
            // avoid void type
        }
        IRSelectNode select_node = new IRSelectNode();
        select_node.tp = ret_tp;
        select_node.result = ret_id;
        select_node.cond = cond_id.ret_id;
        select_node.val1 = true_id.ret_id;
        select_node.val2 = false_id.ret_id;
        tail.next = select_node;
        tail = select_node;

        // System.out.println(
        // ret_id + " = phi " + ret_tp + " [ " + true_id + ", %" + true_label + " ], [ "
        // + false_id + ", %"
        // + false_label + " ]");

        return new IRRetType(head, tail, ret_id);
    }

    IRRetType visit(ThisNode node) {
        IRNode empty_node = new IRNode();
        return new IRRetType(empty_node, empty_node, "%this");
    }

    IRRetType visit(UnaryOpNode node) {
        IRRetType expr_id = visit(node.expr);
        IRNode head = expr_id.head, tail = expr_id.tail;
        String ret_id = renameIdLocal("UnaryRetVal");
        String ret_tp = node.type.getLLVMType();

        if (node.oprand == UnaryOpNode.UnaryOprand.SAddL) {
            // String tmp_id = renameIdLocal("UnaryTmp");
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "add";
            bin_node.tp = ret_tp;
            bin_node.op1 = expr_id.ret_id;
            bin_node.op2 = "1";
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;

            IRStoreNode store_node = new IRStoreNode();
            store_node.tp = ret_tp;
            store_node.ptr = expr_id.lvalue_ptr;
            store_node.value = ret_id;
            tail.next = store_node;
            tail = store_node;

            return new IRRetType(head, tail, ret_id, expr_id.lvalue_ptr);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SAddR) {

            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "add";
            bin_node.tp = ret_tp;
            bin_node.op1 = expr_id.ret_id;
            bin_node.op2 = "1";
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;

            IRStoreNode store_node = new IRStoreNode();
            store_node.tp = ret_tp;
            store_node.ptr = expr_id.lvalue_ptr;
            store_node.value = ret_id;
            tail.next = store_node;
            tail = store_node;

            return new IRRetType(head, tail, expr_id.ret_id);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SSubL) {

            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "sub";
            bin_node.tp = ret_tp;
            bin_node.op1 = expr_id.ret_id;
            bin_node.op2 = "1";
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;

            IRStoreNode store_node = new IRStoreNode();
            store_node.tp = ret_tp;
            store_node.ptr = expr_id.lvalue_ptr;
            store_node.value = ret_id;
            tail.next = store_node;
            tail = store_node;

            return new IRRetType(head, tail, ret_id, expr_id.lvalue_ptr);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SSubR) {

            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "sub";
            bin_node.tp = ret_tp;
            bin_node.op1 = expr_id.ret_id;
            bin_node.op2 = "1";
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;

            IRStoreNode store_node = new IRStoreNode();
            store_node.tp = ret_tp;
            store_node.ptr = expr_id.lvalue_ptr;
            store_node.value = ret_id;
            tail.next = store_node;
            tail = store_node;

            return new IRRetType(head, tail, expr_id.ret_id);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.Plus) {
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "add";
            bin_node.tp = ret_tp;
            bin_node.op1 = "0";
            bin_node.op2 = expr_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = add " + ret_tp + " 0, " + expr_id);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.Minus) {
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "sub";
            bin_node.tp = ret_tp;
            bin_node.op1 = "0";
            bin_node.op2 = expr_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = sub " + ret_tp + " 0, " + expr_id);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.LNot) {
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "xor";
            bin_node.tp = ret_tp;
            bin_node.op1 = "true";
            bin_node.op2 = expr_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = xor " + ret_tp + " true, " + expr_id);

        } else if (node.oprand == UnaryOpNode.UnaryOprand.BNot) {
            IRBinaryNode bin_node = new IRBinaryNode();
            bin_node.operator = "xor";
            bin_node.tp = ret_tp;
            bin_node.op1 = ((Integer) (~0)).toString();
            bin_node.op2 = expr_id.ret_id;
            bin_node.result = ret_id;
            tail.next = bin_node;
            tail = bin_node;
            // System.out.println(ret_id + " = xor " + ret_tp + " " + ((Integer)
            // (~0)).toString() + ", " + expr_id);

        } else {
            throw_internal("Unknown Unary Operator", node.pos);
        }

        return new IRRetType(head, tail, ret_id);
    }

    IRRetType visit(TypeNode node) {
        IRNode empty_node = new IRNode();
        return new IRRetType(empty_node, empty_node);
    }

}
