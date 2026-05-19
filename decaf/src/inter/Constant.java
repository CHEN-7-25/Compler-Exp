package inter;

import symbols.Type;
import lexer.Num;
import lexer.Token;
import lexer.Word;

/**
 * 类 Constant 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Constant extends Expr {

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	   public Constant(Token tok, Type p) { super(tok, p); }
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	   public Constant(int i) { super(new Num(i), Type.Int); }

	   public static final Constant
	      True  = new Constant(Word.True,  Type.Bool),
	      False = new Constant(Word.False, Type.Bool);

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	   public void jumping(int t, int f) {
	      if ( this == True && t != 0 ) emit("goto L" + t);
	      else if ( this == False && f != 0) emit("goto L" + f);
	   }
	}

