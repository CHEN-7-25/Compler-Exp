package lexer;

/**
 * 词法记号（Token）：浮点数（实数）面值常量。
 */
public class Real extends Token {
	public final float value;

	/**
	 * 构造浮点数常量的记号节点。
	 * 
	 * @param v 浮点数的字面值
	 */
	public Real(float v) {
		super(Tag.REAL);
		value = v;
	}

	public String toString() {
		return "" + value;
	}

}
