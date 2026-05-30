package inter;

/**
 * 抽象语法树节点：Break 控制流跳出语句。
 */
public class Break extends Stmt {
    Stmt stmt;

    /**
     * 构造 Break 节点，并检查当前上下文是否存在包裹的循环结构。
     */
    public Break() {
        if (Stmt.Enclosing == Stmt.Null)
            error("unenclosed break"); // 如果不在循环体内，则报错
        stmt = Stmt.Enclosing; // 记录外层循环，用于后续寻找跳转的出口标号
    }

    /**
     * 中间代码生成：生成无条件跳转指令，跳出最近的循环体。
     * 
     * @param b 所在语句的进入标号（本结构不使用）
     * @param a 所在语句的退出标号（不使用，使用外环的 after 标号替代）
     */
    public void gen(int b, int a) {
        emit("goto L" + stmt.after); // [TAC生成] 直接无条件跳转到外围循环记录的退出标号
        // 以下注释的代码用于输出语法树结构，便于调试和理解生成过程（实验3）
        // System.out.println("break");
    }
}
