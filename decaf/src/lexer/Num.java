package lexer;

/**
 * 词法记号（Token）：整数面值常量。
 */
public class Num extends Token {
	public final int value;

	/**
	 * 构造整数常量的记号节点。
	 * 
	 * @param v 整数的字面值
	 */
	public Num(int v) {
		super(Tag.NUM);
		value = v;
	}

	public String toString() {
		return "" + value;
	}
}
