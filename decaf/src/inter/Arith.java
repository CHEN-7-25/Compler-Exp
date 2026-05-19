package inter;

import symbols.Type;
import lexer.Token;

/**
 * 类 Arith 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Arith extends Op {

	   public Expr expr1, expr2;

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	   public Arith(Token tok, Expr x1, Expr x2)  {
	      super(tok, null); expr1 = x1; expr2 = x2;
	      type = Type.max(expr1.type, expr2.type);
	      if (type == null ) error("type error");
	   }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	   public Expr gen() {
	      return new Arith(op, expr1.reduce(), expr2.reduce());
	   }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	   public String toString() {
	      return expr1.toString()+" "+op.toString()+" "+expr2.toString();
	   }
	}
