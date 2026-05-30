package lexer;

/**
 * 词法记号（Token）：保留字、标识符与复合运算符。
 * 派生自 Token，用于处理含有多个字符的词素 (lexeme)。
 */
public class Word extends Token {
	public String lexeme = "";

	/**
	 * 构造复合词法记号节点。
	 * 
	 * @param s   具体词素的字符串内容
	 * @param tag 对应的标记类别
	 */
	public Word(String s, int tag) {
		super(tag);
		lexeme = s;
	}

	public String toString() {
		return lexeme;
	}

	// 预定义的全局关键字与常见多字符操作符
	public static final Word and = new Word("&&", Tag.AND), or = new Word("||", Tag.OR),
			eq = new Word("==", Tag.EQ), ne = new Word("!=", Tag.NE),
			le = new Word("<=", Tag.LE), ge = new Word(">=", Tag.GE),
			minus = new Word("minus", Tag.MINUS),
			True = new Word("true", Tag.TRUE),
			False = new Word("false", Tag.FALSE),
			temp = new Word("t", Tag.TEMP);
}
