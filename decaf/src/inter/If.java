package inter;

import symbols.Type;

/**
 * 抽象语法树节点：单分支 If 控制流语句。
 */
public class If extends Stmt {

    Expr expr;
    Stmt stmt;

    /**
     * 构造单分支 If 语句节点，并进行语义类型检查。
     */
    public If(Expr x, Stmt s) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool)
            expr.error("boolean required in if");
    }

    /**
     * 中间代码生成：生成单分支 If 结构的控制流。
     * * @param b 当前语句块的进入标号
     * 
     * @param a 当前语句块的跳出标号
     */
    public void gen(int b, int a) {
        int label = newlabel(); // label for the code for stmt
        expr.jumping(0, a); // fall through on true, goto a on false
        emitlabel(label);
        stmt.gen(label, a);
        // System.out.println("stmt : if begin");
        // System.out.print(" "); // 为紧跟其后的子语句输出2个空格缩进
        // if (stmt != Stmt.Null) stmt.gen(b, a);
        // System.out.println("stmt : if end");
    }
}
