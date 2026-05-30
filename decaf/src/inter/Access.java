package inter;

import symbols.Type;
import lexer.Tag;
import lexer.Word;

/**
 * 抽象语法树节点：数组元素访问表达式 (如 array[index])。
 */
public class Access extends Op {

	public Id array;
	public Expr index;

	/**
	 * 构造数组访问节点。
	 * 
	 * @param a 访问的数组变量（Id）
	 * @param i 访问的下标表达式
	 * @param p 数组元素的具体类型
	 */
	public Access(Id a, Expr i, Type p) {
		super(new Word("[]", Tag.INDEX), p);
		array = a;
		index = i;
	}

	/**
	 * 归约并生成中间代码节点，保证下标被求值到位。
	 */
	public Expr gen() {
		return new Access(array, index.reduce(), type);
	}

	/**
	 * 处理数组访问在布尔控制流中的情况。
	 * 首先把数组元素的值归约成临时变量或地址，然后发射判定该地址真假的跳转指令。
	 */
	public void jumping(int t, int f) {
		emitjumps(reduce().toString(), t, f);
	}

	public String toString() {
		return array.toString() + " [ " + index.toString() + " ]";
	}
}
