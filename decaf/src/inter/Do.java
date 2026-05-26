package inter;

import symbols.Type;

/**
 * 类 Do 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Do extends Stmt {

   Expr expr; Stmt stmt;

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public Do() { expr = null; stmt = null; }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public void init(Stmt s, Expr x) {
      expr = x; stmt = s;
      if( expr.type != Type.Bool ) expr.error("boolean required in do");
   }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
    public void gen(int b, int a) {
       System.out.println("stmt : do begin");
       System.out.print("  "); // 为紧跟其后的子语句输出2个空格缩进
       if (stmt != Stmt.Null) stmt.gen(b, a);
       System.out.println("stmt : do end");
    }
}