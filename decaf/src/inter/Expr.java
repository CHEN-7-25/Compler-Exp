package inter;

import symbols.Type;
import lexer.Token;

public class Expr extends Node {

	public Token op;
	public Type type;

	Expr(Token tok, Type p) {
		op = tok;
		type = p;
	}

	/**
	 * 生成中间代码（针对右值表达式）。
	 * 默认直接返回自身。
	 */
	public Expr gen() {
		return this;
	}

	/**
	 * 将表达式归约为一个单一地址（临时变量或常量）。
	 * 默认直接返回自身。
	 */
	public Expr reduce() {
		return this;
	}

	/**
	 * 为布尔表达式生成跳转代码。
	 * 
	 * @param t true 出口的标号，如果为 0 则控制流连续执行
	 * @param f false 出口的标号，如果为 0 则控制流连续执行
	 */
	public void jumping(int t, int f) {
		emitjumps(toString(), t, f);
	}

	/**
	 * 发射跳转指令的具体逻辑实现。
	 */
	public void emitjumps(String test, int t, int f) {
		if (t != 0 && f != 0) {
			emit("if " + test + " goto L" + t);
			emit("goto L" + f);
		} else if (t != 0)
			emit("if " + test + " goto L" + t);
		else if (f != 0)
			emit("iffalse " + test + " goto L" + f);
		else
			;
	}

	public String toString() {
		return op.toString();
	}
}
