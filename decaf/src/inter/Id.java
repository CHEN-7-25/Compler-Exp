package inter;

import symbols.Type;
import lexer.Word;

/**
 * 抽象语法树节点：标识符（叶子节点）。
 * 代表源程序代码中声明的各类变量。
 */
public class Id extends Expr {

	/**
	 * 变量在当前代码块运行栈中的相对字节偏移量（offset）。
	 */
	public int offset;

	/**
	 * 构造标识符表达式节点。
	 * 
	 * @param id 底层词法分析传来的词法标识符节点
	 * @param p  符号表查询到的语义类型
	 * @param b  变量分配的内存数据段偏移字节量
	 */
	public Id(Word id, Type p, int b) {
		super(id, p);
		offset = b;
	}
}
