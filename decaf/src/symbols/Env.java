package symbols;

import inter.Id;

import java.util.Hashtable;

import lexer.Token;

/**
 * 符号表环境上下文 (Environment)。
 * 用于管理变量的作用域，采用链式哈希表结构支持层级嵌套的分程序块作用域。
 */
public class Env {
	private Hashtable table;
	protected Env prev;

	/**
	 * 构造一个新的符号表环境上下文。
	 * 
	 * @param n 外围一层的作用域环境（即声明该程序块时的父级Env）
	 */
	public Env(Env n) {
		table = new Hashtable();
		prev = n;
	}

	/**
	 * 将新声明的变量标识符存入当前作用域的哈希表中。
	 * 
	 * @param w 词法记号（通常是指变量的字符串 Token）
	 * @param i 对应的标识符实体节点 (AST Node)
	 */
	public void put(Token w, Id i) {
		table.put(w, i);
	}

	/**
	 * 根据变量 Token 逐级向上回溯查找变量对应的标识符节点。
	 * 
	 * @param w 需要检索的变量词法记号
	 * @return 如果在当前及所有父级环境中都未找到该变量，则返回 null，否则返回查找到的 Id 节点
	 */
	public Id get(Token w) {
		for (Env e = this; e != null; e = e.prev) {
			Id found = (Id) (e.table.get(w));
			if (found != null)
				return found;
		}
		return null;
	}
}
