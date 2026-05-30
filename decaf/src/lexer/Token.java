package lexer;

/**
 * 词法记号（Token）基类。
 * 代表词法分析器输出的基本单元，包含一个整型的类别标签（tag）。
 */
public class Token {
	public final int tag;

	/**
	 * 构造词法记号节点。
	 * 
	 * @param t 类别标签，对应 Tag 类中的常数或单个字符的 ASCII 值
	 */
	public Token(int t) {
		tag = t;
	}

	public String toString() {
		return "" + (char) tag;
	}
}
