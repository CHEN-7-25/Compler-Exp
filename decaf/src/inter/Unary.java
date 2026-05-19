package inter;

import symbols.Type;
import lexer.Token;

/**
 * 类 Unary 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Unary extends Op {

   public Expr expr;

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public Unary(Token tok, Expr x) {    // handles minus, for ! see Not
      super(tok, null);  expr = x;
      type = Type.max(Type.Int, expr.type);
      if (type == null ) error("type error");
   }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public Expr gen() { return new Unary(op, expr.reduce()); }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public String toString() { return op.toString()+" "+expr.toString(); }
}

