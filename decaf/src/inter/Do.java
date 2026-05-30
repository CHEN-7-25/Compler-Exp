package inter;

import symbols.Type;

/**
 * 抽象语法树节点：Do-While 循环语句。
 */
public class Do extends Stmt {

    Expr expr;
    Stmt stmt;

    public Do() {
        expr = null;
        stmt = null;
    }

    /**
     * 初始化 Do-While 节点，并进行布尔条件类型检查。
     */
    public void init(Stmt s, Expr x) {
        expr = x;
        stmt = s;
        if (expr.type != Type.Bool)
            expr.error("boolean required in do");
    }

    /**
     * 中间代码生成：生成 Do-While 循环的控制流。
     * 
     * @param b 循环体的进入标号，即下次迭代跳回的目标
     * @param a 循环退出的标号
     */
    public void gen(int b, int a) {
        after = a; // 保存跳出外层的标号 a，供内部 break 使用
        int label = newlabel(); // [TAC生成] 表达式条件判断的标号
        stmt.gen(b, label); // 执行循环体代码
        emitlabel(label); // 发射条件判断的标号
        expr.jumping(b, 0); // 条件判断：true 跳回 b，false 则继续执行后续指令（自然跳出）
        // 以下注释的代码用于输出语法树结构，便于调试和理解生成过程（实验3）
        // System.out.println("stmt : do begin");
        // System.out.print(" "); // 为紧跟其后的子语句输出2个空格缩进
        // if (stmt != Stmt.Null) stmt.gen(b, a);
        // System.out.println("stmt : do end");
    }
}