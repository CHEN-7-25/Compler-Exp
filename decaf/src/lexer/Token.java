package lexer;

/**
 * 类 Token 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Token {
	public final int tag;
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Token(int t) { tag = t; }
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public String toString() { return ""+(char)tag; }
}
