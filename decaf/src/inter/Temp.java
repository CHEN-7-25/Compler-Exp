package inter;

import symbols.Type;
import lexer.Word;

/**
 * 抽象语法树节点：临时变量（内部标识临时生成的寄存器/内存对象）。
 */
public class Temp extends Expr {
	static int count = 0; // 全局临时变量计数器
	int number = 0; // 当前节点分配的临时变量编号

	/**
	 * 申请生成一个新的临时变量节点。自动累加编号（如 t1, t2）。
	 */
	public Temp(Type p) {
		super(Word.temp, p);
		number = ++count;
	}

	public String toString() {
		return "t" + number;
	}
}
