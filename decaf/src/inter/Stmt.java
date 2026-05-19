package inter;

/**
 * 类 Stmt 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Stmt extends Node {

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public Stmt() { }

   public static Stmt Null = new Stmt();

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public void gen(int b, int a) {} // called with labels begin and after

   int after = 0;                   // saves label after
   public static Stmt Enclosing = Stmt.Null;  // used for break stmts
}
