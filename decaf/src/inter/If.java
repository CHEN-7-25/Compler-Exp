package inter;

import symbols.Type;

/**
 * 类 If 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class If extends Stmt {

   Expr expr; Stmt stmt;

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public If(Expr x, Stmt s) {
      expr = x;  stmt = s;
      if( expr.type != Type.Bool ) expr.error("boolean required in if");
   }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public void gen(int b, int a) {
      int label = newlabel(); // label for the code for stmt
      expr.jumping(0, a);     // fall through on true, goto a on false
      emitlabel(label); stmt.gen(label, a);
   }
}
