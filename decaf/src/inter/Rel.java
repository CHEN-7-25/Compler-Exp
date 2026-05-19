package inter;

import symbols.Array;
import symbols.Type;
import lexer.Token;

/**
 * 类 Rel 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Rel extends Logical {

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public Rel(Token tok, Expr x1, Expr x2) { super(tok, x1, x2); }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public Type check(Type p1, Type p2) {
      if ( p1 instanceof Array || p2 instanceof Array ) return null;
      else if( p1 == p2 ) return Type.Bool;
      else return null;
   }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public void jumping(int t, int f) {
      Expr a = expr1.reduce();
      Expr b = expr2.reduce();
      String test = a.toString() + " " + op.toString() + " " + b.toString();
      emitjumps(test, t, f);
   }
}
