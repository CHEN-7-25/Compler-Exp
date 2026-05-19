package lexer;

/**
 * 类 Word 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Word extends Token {
	public String lexeme = "";
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Word(String s, int tag) { super(tag); lexeme = s; }
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public String toString() {return lexeme; }
	
	public static final Word
		and = new Word( "&&", Tag.AND ),  or = new Word( "||", Tag.OR ),
	    eq  = new Word( "==", Tag.EQ  ),  ne = new Word( "!=", Tag.NE ),
	    le  = new Word( "<=", Tag.LE  ),  ge = new Word( ">=", Tag.GE ),
	
	    minus  = new Word( "minus", Tag.MINUS ),
	    True   = new Word( "true",  Tag.TRUE  ),
	    False  = new Word( "false", Tag.FALSE ),
	    temp   = new Word( "t",     Tag.TEMP  );
}
