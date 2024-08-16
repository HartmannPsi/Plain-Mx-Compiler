package IR;

import defNodes.*;
import defNodes.exprNodes.*;
import defNodes.exprNodes.BinaryOpNode.BinaryOprand;
import defNodes.stmtNodes.*;
import util.error.*;
import util.*;
import java.util.Map;
import IR.IRNodes.*;
import java.util.HashMap;

public class IRGenerator {
    ProgNode root;
    int id_serial = 0, label_serial = 0;
    Map<String, Map<String, Integer>> classes = new HashMap<>();
    String global_var_init = "@Global.Var.Init";
    public IRNode beg = null;

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
            return null;
        } else {
            throw_internal("Unknown Node Type", node.pos);
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////

    // TODO: change return type
    IRRetType visit(ProgNode node) {

        IRRetType tmp = GlobalVarInit();
        beg = tmp.head;
        IRNode tail = tmp.tail;

        for (int i = 0; i != node.global_stmt.length; ++i) {
            IRRetType now = visit(node.global_stmt[i]);
            tail.next = now.head;
            tail = now.tail;
        }
        return new IRRetType(beg, tail, null);
    }

    IRRetType GlobalVarInit() {
        // "@Global.Var.Init"
        IRDefFuncNode def_node = new IRDefFuncNode();
        def_node.func_name = global_var_init;
        def_node.result_tp = "void";
        // System.out.println("define void " + global_var_init + "() {");
        def_node.stmt = new IRNode();
        IRNode tail = def_node.stmt;
        for (Node _node : root.global_stmt) {
            if (_node instanceof DefVarNode) {

                DefVarNode node = (DefVarNode) _node;
                Type tp = ((TypeNode) node.type).type;
                String rename_tp;
                if (tp.dim > 0) {
                    rename_tp = "ptr";
                } else if (root.rename_cls.containsKey(tp.id)) {
                    rename_tp = root.rename_cls.get(tp.id);
                } else {
                    rename_tp = tp.getLLVMType();
                }

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
                    String rename_tp;
                    if (tp.equal("int") || tp.equal("bool") || tp.equal("string")) {
                        rename_tp = tp.getLLVMType();
                    } else if (tp.dim == 0) {
                        rename_tp = root.rename_cls.get(tp.id);
                    } else {
                        rename_tp = "ptr";
                    }

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
            cons_def_node.tps[0] = cls_rename;
            cons_def_node.stmt = new IRNode();
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
            // System.out.println("}");
            tail = cons_def_node;

            for (Node _node : node.stmt) {// process funcs
                if (_node instanceof DefFuncNode) {
                    // TODO:
                }
            }

            return new IRRetType(def_node, tail, null);
        } else {
            // System.out.println(" }");
            return new IRRetType(def_node, def_node, null);
        }

        // return null;
    }

    IRRetType visit(DefFuncNode node) {

        Type ret_tp = ((TypeNode) node.type).type;
        String rename_ret_tp;
        String rename_id = ((IdNode) node.name).rename_id;

        IRDefFuncNode def_node = new IRDefFuncNode();

        if (ret_tp.dim > 0) {
            rename_ret_tp = "ptr";

        } else if (root.rename_cls.containsKey(ret_tp.id)) {
            rename_ret_tp = root.rename_cls.get(ret_tp.id);

        } else {
            rename_ret_tp = ret_tp.getLLVMType();

        }

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
            String cls_tp = ((IdNode) ((DefClassNode) node.father).name).rename_id;
            def_node.ids[arg_cnt] = "%this";
            def_node.tps[arg_cnt++] = cls_tp;
            // System.out.print(cls_tp + " %this");

            // if (node.ids != null) {
            // System.out.print(", ");
            // }
        }

        if (node.ids != null) {
            for (int i = 0; i != node.ids.length; ++i) {
                Type tp = ((TypeNode) node.tps[i]).type;
                String rename_tp;
                if (tp.dim > 0) {
                    rename_tp = "ptr";

                } else if (root.rename_cls.containsKey(tp.id)) {
                    rename_tp = root.rename_cls.get(tp.id);

                } else {
                    rename_tp = tp.getLLVMType();

                }
                def_node.ids[arg_cnt] = ((IdNode) node.ids[i]).rename_id;
                def_node.tps[arg_cnt++] = rename_tp;
                // System.out.print(rename_tp + " " + ((IdNode) node.ids[i]).rename_id);
                // if (i != node.ids.length - 1) {
                // System.out.print(", ");
                // }
            }
        }

        // System.out.println(") {");

        if (((IdNode) node.name).id.equals("main")) {
            IRCallNode call_node = new IRCallNode();
            call_node.func_name = global_var_init;
            call_node.res_tp = "void";
            def_node.stmt = call_node;
            // System.out.println("call void " + global_var_init + "()");
        } else {
            def_node.stmt = new IRNode();
        }

        IRNode tail = def_node.stmt;

        if (node.stmt != null) {
            for (Node subnode : node.stmt) {
                IRRetType now = visit(subnode);
                tail.next = now.head;
                tail = now.tail;
            }
        }

        // System.out.println("}");
        return new IRRetType(def_node, def_node, null);
    }

    IRRetType visit(DefVarNode node) {

        Type tp = ((TypeNode) node.type).type;
        String rename_tp;

        IRNode head = new IRNode(), tail = head;

        if (tp.dim > 0) {
            rename_tp = "ptr";

        } else if (root.rename_cls.containsKey(tp.id)) {
            rename_tp = root.rename_cls.get(tp.id);

        } else {
            rename_tp = tp.getLLVMType();

        }

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
        String init_label = renameLabel("For.Init"), cond_label = renameLabel("For.Cond"),
                body_label = renameLabel("For.Body"), step_label = renameLabel("For.Step"),
                end_label = renameLabel("For.End");
        node.end_label = end_label;
        node.step_label = step_label;

        IRNode head, tail;
        IRLabelNode init_node = new IRLabelNode(), cond_node = new IRLabelNode(), body_node = new IRLabelNode(),
                step_node = new IRLabelNode(), end_node = new IRLabelNode();
        head = tail = init_node;
        init_node.label = init_label;

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

        String cond_label = renameLabel("If.Cond"), then_label = renameLabel("If.Then"),
                else_label = renameLabel("If.Else"),
                end_label = renameLabel("If.End");
        IRLabelNode cond_node = new IRLabelNode(), then_node = new IRLabelNode(), else_node = new IRLabelNode(),
                end_node = new IRLabelNode();
        IRNode head, tail;
        head = tail = cond_node;

        cond_node.label = cond_label;
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
        } else {
            IRRetType ret_id = visit(node.expr);
            ret_node.tp = ((ExprNode) node.expr).type.getLLVMType();
            ret_node.val = ret_id.ret_id;
            // System.out.println("ret " + ((ExprNode) node.expr).type.getLLVMType() + " " +
            // ret_id);
        }
        return new IRRetType(ret_node, ret_node, null);
    }

    IRRetType visit(WhileStmtNode node) {
        String cond_label = renameLabel("While.Cond"), body_label = renameLabel("While.Body"),
                end_label = renameLabel("While.End");
        node.end_label = end_label;
        node.cond_label = cond_label;

        IRNode head, tail;
        IRLabelNode cond_node = new IRLabelNode(), body_node = new IRLabelNode(), end_node = new IRLabelNode();
        head = tail = cond_node;

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

    IRRetType visit(ArrayAccessNode node) {// TODO:

        return null;
    }

    IRRetType visit(ArrayNode node) {// TODO:

        return null;
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
                call_node.res_tp = "i8*";
                call_node.result = ret_id;
                call_node.args = new String[] { lhs_id.ret_id, rhs_id.ret_id };
                call_node.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(
                // ret_id + " = call i8* @string.add(i8* " + lhs_id + ", i8* " + rhs_id + ")");

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
                call_node.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.lt(i8* " + lhs_id + ", i8* "
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
                call_node.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.le(i8* " + lhs_id + ", i8* "
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
                call_node.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.gt(i8* " + lhs_id + ", i8* "
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
                call_node.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.ge(i8* " + lhs_id + ", i8* "
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
                call_node.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.eq(i8* " + lhs_id + ", i8* "
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
                call_node.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(ret_id + " = call i1 @string.ne(i8* " + lhs_id + ", i8* "
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
            String lhs_label = renameLabel("LAnd.Lhs"), rhs_label = renameLabel("LAnd.Rhs"),
                    end_label = renameLabel("LAnd.End");
            // String tmp_ptr = ptrLocal();
            // System.out.println(tmp_ptr + " = alloca " + ret_tp);
            IRLabelNode lhs_node = new IRLabelNode(), rhs_node = new IRLabelNode(), end_node = new IRLabelNode();

            head = tail = lhs_node;
            lhs_node.label = lhs_label;
            // System.out.println(lhs_label + ":");
            IRRetType lhs_id = visit(node.lhs);
            tail.next = lhs_id.head;
            tail = lhs_id.tail;
            IRBrNode br1_node = new IRBrNode();
            br1_node.label_true = rhs_label;
            br1_node.label_false = end_label;
            br1_node.cond = lhs_id.ret_id;
            tail.next = br1_node;
            tail = br1_node;
            // System.out.println("store " + ret_tp + " " + lhs_id + ", ptr " + tmp_ptr);
            // System.out.println("br i1 " + lhs_id + ", label %" + rhs_label + ", label %"
            // + end_label);

            rhs_node.label = rhs_label;
            tail.next = rhs_node;
            tail = rhs_node;
            // System.out.println(rhs_label + ":");
            IRRetType rhs_id = visit(node.rhs);
            tail.next = rhs_id.head;
            tail = rhs_id.tail;
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
            IRPhiNode phi_node = new IRPhiNode();
            phi_node.tp = ret_tp;
            phi_node.result = ret_id;
            phi_node.vals = new String[] { "false", rhs_id.ret_id };
            phi_node.labels = new String[] { lhs_label, rhs_label };
            tail.next = phi_node;
            tail = phi_node;
            // System.out.println(ret_id + " = phi " + ret_tp + " [ false, %" + lhs_label +
            // " ], [ " + rhs_id + ", %"
            // + rhs_label + " ]");

        } else if (node.oprand == BinaryOprand.LOr) {// short circuit
            String lhs_label = renameLabel("LOr.Lhs"), rhs_label = renameLabel("LOr.Rhs"),
                    end_label = renameLabel("LOr.End");
            // String tmp_ptr = ptrLocal();
            // System.out.println(tmp_ptr + " = alloca " + ret_tp);
            IRLabelNode lhs_node = new IRLabelNode(), rhs_node = new IRLabelNode(), end_node = new IRLabelNode();

            head = tail = lhs_node;
            lhs_node.label = lhs_label;
            // System.out.println(lhs_label + ":");
            IRRetType lhs_id = visit(node.lhs);
            tail.next = lhs_id.head;
            tail = lhs_id.tail;
            IRBrNode br1_node = new IRBrNode();
            br1_node.label_true = end_label;
            br1_node.label_false = rhs_label;
            br1_node.cond = lhs_id.ret_id;
            tail.next = br1_node;
            tail = br1_node;
            // System.out.println("store " + ret_tp + " " + lhs_id + ", ptr " + tmp_ptr);
            // System.out.println("br i1 " + lhs_id + ", label %" + end_label + ", label %"
            // + rhs_label);

            rhs_node.label = rhs_label;
            tail.next = rhs_node;
            tail = rhs_node;
            // System.out.println(rhs_label + ":");
            IRRetType rhs_id = visit(node.rhs);
            tail.next = rhs_id.head;
            tail = rhs_id.tail;
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
            IRPhiNode phi_node = new IRPhiNode();
            phi_node.tp = ret_tp;
            phi_node.result = ret_id;
            phi_node.vals = new String[] { "true", rhs_id.ret_id };
            phi_node.labels = new String[] { lhs_label, rhs_label };
            tail.next = phi_node;
            tail = phi_node;
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

        } else if (node.oprand == BinaryOprand.Assign) {// TODO:

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

            String ret_id = renameIdLocal("FStringRetVal");
            IRConstStrNode const_node = new IRConstStrNode();
            const_node.result = ret_id;
            const_node.literal = node.toString(0);
            const_node.length = node.getLength(0) + 1;
            tail.next = const_node;
            tail = const_node;
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
                    call_node.res_tp = "i8*";
                    call_node.result = str_ids[i];
                    call_node.args = new String[] { expr_ids[i].ret_id };
                    call_node.tps = new String[] { "i32" };
                    tail.next = call_node;
                    tail = call_node;
                    // System.out.println(str_ids[i] + " = call i8* @toString(i32 " + expr_ids[i] +
                    // ")");
                    // System.out.println(str_ids[i] + " = call i8* @intToStr(i32 " + expr_ids[i] +
                    // ")");

                } else if (((ExprNode) node.exprs[i]).type.equal("bool")) {

                    str_ids[i] = renameIdLocal("FString.Bool");
                    IRCallNode call_node = new IRCallNode();
                    call_node.func_name = "@boolToString";
                    call_node.res_tp = "i8*";
                    call_node.result = str_ids[i];
                    call_node.args = new String[] { expr_ids[i].ret_id };
                    call_node.tps = new String[] { "i1" };
                    tail.next = call_node;
                    tail = call_node;
                    // System.out.println(str_ids[i] + " = call i8* @boolToString(i1 " + expr_ids[i]
                    // + ")");
                    // System.out.println(str_ids[i] + " = call i8* @boolToStr(i1 " + expr_ids[i] +
                    // ")");

                } else {
                    throw_internal("Invalid Type in FString", node.pos);
                }
            }

            String[] tmp_ids = new String[node.literals.length];
            tmp_ids[0] = renameIdLocal("FStringTmp");
            IRConstStrNode const_node = new IRConstStrNode();
            const_node.result = tmp_ids[0];
            const_node.literal = node.toString(0);
            const_node.length = node.getLength(0) + 1;
            tail.next = const_node;
            tail = const_node;
            // System.out.print(tmp_ids[0] + " = constant [");
            // System.out.print(node.getLength(0) + 1);
            // System.out.print(" x i8] c\"");
            // System.out.print(node.toString(0));
            // System.out.println("\\00\"");

            for (int i = 1; i != node.literals.length; ++i) {
                tmp_ids[i] = renameIdLocal("FStringTmp");
                String inter_id = renameIdLocal("FStringInter");
                String literal_id = renameIdLocal("FStringLiteral");

                IRCallNode call_node = new IRCallNode();
                call_node.func_name = "@string.add";
                call_node.res_tp = "i8*";
                call_node.result = inter_id;
                call_node.args = new String[] { tmp_ids[i - 1], str_ids[i - 1] };
                call_node.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node;
                tail = call_node;
                // System.out.println(
                // inter_id + " = call i8* @string.add(i8* " + tmp_ids[i - 1] + ", i8* " +
                // str_ids[i - 1] + ")");

                IRConstStrNode const_node2 = new IRConstStrNode();
                const_node2.result = literal_id;
                const_node2.literal = node.toString(i);
                const_node2.length = node.getLength(i) + 1;
                tail.next = const_node2;
                tail = const_node2;
                // System.out.print(literal_id + " = constant [");
                // System.out.print(node.getLength(i) + 1);
                // System.out.print(" x i8] c\"");
                // System.out.print(node.toString(i));
                // System.out.println("\\00\"");

                IRCallNode call_node2 = new IRCallNode();
                call_node2.func_name = "@string.add";
                call_node2.res_tp = "i8*";
                call_node2.result = tmp_ids[i];
                call_node2.args = new String[] { inter_id, literal_id };
                call_node2.tps = new String[] { "i8*", "i8*" };
                tail.next = call_node2;
                tail = call_node2;
                // System.out.println(
                // tmp_ids[i] + " = call i8* @string.add(i8* " + inter_id + ", i8* " +
                // literal_id + ")");
            }

            String ret_id = tmp_ids[tmp_ids.length - 1];
            return new IRRetType(head, tail, ret_id);
        }
    }

    IRRetType visit(FuncCallNode node) {// TODO:

        return null;
    }

    IRRetType visit(IdNode node) {

        IRNode empty_node = new IRNode();

        if (node.is_var) {

            String ret_id = renameIdLocal("IdRetVal");
            IRLoadNode load_node = new IRLoadNode();
            load_node.tp = node.type.getLLVMType();
            load_node.result = ret_id;
            load_node.ptr = node.rename_id;
            System.out.println(ret_id + " = load " + node.type.getLLVMType() + ", ptr " + node.rename_id);
            return new IRRetType(load_node, load_node, ret_id);

        } else {
            return new IRRetType(empty_node, empty_node, node.rename_id);
        }
    }

    IRRetType visit(MemAccNode node) {// TODO:

        return null;
    }

    IRRetType visit(NewExprNode node) {// TODO:

        return null;
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
        String ret_id = renameIdLocal("StringConst");
        IRConstStrNode const_node = new IRConstStrNode();
        const_node.result = ret_id;
        const_node.literal = node.toString();
        const_node.length = node.getLength() + 1;
        // System.out.print(ret_id + " = constant [");
        // System.out.print(node.getLength() + 1);
        // System.out.print(" x i8] c\"");
        // System.out.print(node.toString());
        // System.out.println("\\00\"");
        return new IRRetType(const_node, const_node, ret_id);
    }

    IRRetType visit(TernaryOpNode node) {
        String cond_label = renameLabel("Ternary.Cond"), true_label = renameLabel("Ternary.True"),
                false_label = renameLabel("Ternary.False"), end_label = renameLabel("Ternary.End");
        String ret_id = renameIdLocal("TernaryRetVal");
        String ret_tp = node.type.getLLVMType();

        IRLabelNode cond_node = new IRLabelNode(), true_node = new IRLabelNode(), false_node = new IRLabelNode(),
                end_node = new IRLabelNode();
        IRNode head = cond_node, tail = head;

        cond_node.label = cond_label;
        // System.out.println(cond_label + ":");
        // System.out.println(tmp_ptr + " = alloca " + ret_tp);
        IRRetType cond_id = visit(node.cond);
        tail.next = cond_id.head;
        tail = cond_id.tail;
        IRBrNode br1_node = new IRBrNode();
        br1_node.label_true = true_label;
        br1_node.label_false = false_label;
        br1_node.cond = cond_id.ret_id;
        tail.next = br1_node;
        tail = br1_node;
        // System.out.println("br i1 " + cond_id + ", label %" + true_label + ", label
        // %" + false_label);

        true_node.label = true_label;
        tail.next = true_node;
        tail = true_node;
        // System.out.println(true_label + ":");
        IRRetType true_id = visit(node.lhs);
        tail.next = true_id.head;
        tail = true_id.tail;
        // System.out.println("store " + ret_tp + " " + true_id + ", ptr " + tmp_ptr);
        IRBrNode br2_node = new IRBrNode();
        br2_node.label_true = end_label;
        tail.next = br2_node;
        tail = br2_node;
        // System.out.println("br label %" + end_label);

        false_node.label = false_label;
        tail.next = false_node;
        tail = false_node;
        // System.out.println(false_label + ":");
        IRRetType false_id = visit(node.rhs);
        tail.next = false_id.head;
        tail = false_id.tail;
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
        IRPhiNode phi_node = new IRPhiNode();
        phi_node.tp = ret_tp;
        phi_node.result = ret_id;
        phi_node.vals = new String[] { true_id.ret_id, false_id.ret_id };
        phi_node.labels = new String[] { true_label, false_label };
        tail.next = phi_node;
        tail = phi_node;
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

        if (node.oprand == UnaryOpNode.UnaryOprand.SAddL) {// TODO:

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SAddR) {

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SSubL) {

        } else if (node.oprand == UnaryOpNode.UnaryOprand.SSubR) {

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
        return null;
    }

}
