package inter;

import lexer.Lexer;

public class Node {
	/**
     * 记录该节点在源代码中的物理行号。
     * 用于在语义分析阶段发生错误时，能够精准定位报错的源码位置。
     */
	int lexline = 0;
	
	/**
     * 静态全局标号计数器。
     * 用于为中间代码生成唯一的控制流跳转锚点（如 L1, L2, L3...）。
     */
	Node() {lexline=Lexer.line;}
	
	/**
     * 生成一个新的中间代码标号。
     * * @return 新的标号整数值
     */
	void error(String s) { throw new Error("near line "+lexline+": "+s); }
	
	static int labels = 0;

	public int newlabel() { return ++labels;}
	
	/**
     * 在控制台输出（发射）一个带有冒号的标号（如 L1: ）。
     * * @param i 需要输出的标号整数值
     */
	public void emitlabel(int i) { System.out.print("L"+i+":"); }
	
	public void emit(String s) { System.out.println("\t"+s); }
}
