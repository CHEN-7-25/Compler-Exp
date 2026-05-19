package inter;

import symbols.Type;
import lexer.Token;

/**
 * 类 Logical 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Logical extends Expr{
	public Expr expr1, expr2;
	Logical(Token tok, Expr x1, Expr x2){
		super(tok,null);
		expr1 = x1; expr2 = x2;
		type = check(expr1.type, expr2.type);
		if(type==null) error("type error");
	}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Type check(Type p1, Type p2){
		if(p1==Type.Bool&&p2==Type.Bool) return Type.Bool;
		else return null;
	}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Expr gen(){
		int f = newlabel(); int a = newlabel();
		Temp temp = new Temp(type);
		this.jumping(0,f);
		emit(temp.toString()+" = true");
		emit("goto L"+a);
		emitlabel(f);emit(temp.toString()+" = false");
		emitlabel(a);
		return temp;
	}
}
