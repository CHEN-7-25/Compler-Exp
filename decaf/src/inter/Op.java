package inter;

import symbols.Type;
import lexer.Token;

/**
 * 类 Op 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Op extends Expr{
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Op(Token tok, Type p) { super(tok,p); }
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Expr reduce()
	{
		Expr x = gen();
		Temp t = new Temp(type);
		emit(t.toString()+" = "+x.toString());
		return t;
	}
}
