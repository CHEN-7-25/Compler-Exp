package inter;

import lexer.Token;

/**
 * 类 And 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class And extends Logical{
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public And(Token tok, Expr x1, Expr x2) { super(tok,x1,x2);}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public void jumping(int t, int f){
		int label = f != 0?f:newlabel();
		expr1.jumping(0, label);
		expr2.jumping(t, f);
		if(f==0) emitlabel(label);
	}

}
