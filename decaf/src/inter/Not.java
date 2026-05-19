package inter;

import lexer.Token;

/**
 * 类 Not 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Not extends Logical {

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public Not(Token tok, Expr x2) { super(tok, x2, x2); }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public void jumping(int t, int f) { expr2.jumping(f, t); }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public String toString() { return op.toString()+" "+expr2.toString(); }
}
