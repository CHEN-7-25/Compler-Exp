package inter;

import symbols.Type;
import lexer.Token;

/**
 * 类 Expr 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Expr extends Node{

	public Token op;
	public Type type;
	
	Expr(Token tok, Type p) { op = tok; type = p; }
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Expr gen() { return this;}
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Expr reduce() { return this;}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public void jumping(int t, int f) { emitjumps(toString(),t,f); }
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public void emitjumps(String test, int t, int f){
		if(t!=0&&f!=0){
			emit("if "+test+" goto L"+t);
			emit("goto L"+f);
		}
		else if(t!=0) emit("if "+test+" goto L"+t);
		else if(f!=0) emit("iffalse "+test+" goto L"+f);
		else ;
	}
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public String toString() { return op.toString(); }
}
