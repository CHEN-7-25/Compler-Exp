package inter;

import lexer.Lexer;

/**
 * 类 Node 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Node {
	int lexline = 0;
	
	Node() {lexline=Lexer.line;}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	void error(String s) { throw new Error("near line "+lexline+": "+s); }
	
	static int labels = 0;

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public int newlabel() { return ++labels;}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public void emitlabel(int i) { System.out.print("L"+i+":"); }
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public void emit(String s) { System.out.println("\t"+s); }
}
