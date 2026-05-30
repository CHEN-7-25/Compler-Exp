package inter;

import symbols.Type;

/**
 * 抽象语法树节点：While 循环语句。
 */
public class While extends Stmt {

    Expr expr;
    Stmt stmt;

    public While() {
        expr = null;
        stmt = null;
    }

    /**
     * 初始化 While 节点，并进行布尔条件类型检查。
     */
    public void init(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool)
            expr.error("boolean required in while");
    }

    /**
     * 中间代码生成：生成 While 循环的控制流。
     * 
     * @param b 循环开始的标号，用于下一轮迭代跳回
     * @param a 循环退出的标号，用于假分支或内部 break 跳出
     */
    public void gen(int b, int a) {
        after = a; // 保存跳出外层的标号 a，供内部 break 使用
        expr.jumping(0, a); // 条件判断：true 则进入循环体，false 则跳出循环 (goto a)
        int label = newlabel(); // [TAC生成] 循环体语句的执行标号
        emitlabel(label);
        stmt.gen(label, b);
        emit("goto L" + b); // 循环体执行完毕后，跳回判断条件 (goto b)
        // System.out.println("stmt : while begin");
        // if (stmt != Stmt.Null) stmt.gen(b, a);
        // System.out.println("stmt : while end");
    }
}
