package inter;

import symbols.Type;

/*
 * 抽象语法树节点：For 循环结构
 * 负责 For 循环的语义类型检查与三地址中间代码(TAC)生成
 */
public class For extends Stmt {

    Expr expr; // 循环的判定条件，如 i < 10
    Stmt stmt1, stmt2, stmt3; // stmt1:初始化(i=0), stmt2:步进更新(i=i+1), stmt3:循环体

    public For() { 
        expr = null; stmt1 = null; stmt2 = null; stmt3 = null; 
    }

    public void init(Stmt s1, Expr x, Stmt s2, Stmt s3) {
        this.stmt1 = s1;
        this.expr = x;
        this.stmt2 = s2;
        this.stmt3 = s3;
        // 校验循环条件是否为布尔类型
        // 如果类型不匹配，主动抛出类型不兼容的语义错误
        if (expr.type != Type.Bool) {
            expr.error("boolean required in for");
        }
    }

    /*
     * 中间代码生成 (Syntax-Directed Translation)
     * 功能描述：为 For 循环生成带有条件跳转和无条件跳转的三地址控制流代码
     * @param b 当前语句块的进入标号 (begin)
     * @param a 当前语句块的跳出标号 (after)
     */
    @Override
    public void gen(int b, int a) {
        after = a; // 保存出口标号，供内部的 break 语句使用

        // 1. 生成初始化语句 (如 a = b)
        if (stmt1 != Stmt.Null) {
            stmt1.gen(b, 0);
        }

        // 2. 生成条件判断标号 L3
        int test_label = newlabel();
        emitlabel(test_label);

        // 3. 生成条件跳转代码 (iffalse ... goto L2)
        expr.jumping(0, a);

        // 4. 生成循环体标号 L4 与代码
        int body_label = newlabel();
        emitlabel(body_label);
        stmt3.gen(body_label, a);

        // 5. 生成迭代步进标号 L5 与代码
        int inc_label = newlabel();
        emitlabel(inc_label);
        if (stmt2 != Stmt.Null) {
            stmt2.gen(inc_label, 0);
        }

        // 6. 无条件跳回条件判断 L3
        emit("goto L" + test_label);

        // 以下注释的代码用于输出语法树结构，便于调试和理解生成过程（实验3）
//        System.out.println("stmt : for begin");
//        if (stmt1 != Stmt.Null) stmt1.gen(b, a); // 1. 打印初始化赋值 (如 i = 0)
//        if (stmt3 != Stmt.Null) stmt3.gen(b, a); // 2. 递归打印循环体
//        if (stmt2 != Stmt.Null) stmt2.gen(b, a); // 3. 打印迭代更新赋值 (如 i = i + 1)
//        System.out.println("stmt : for end");
    }
}