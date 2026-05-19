package lexer;

/**
 * 类 Num 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Num extends Token {
	public final int value;
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Num(int v) { super(Tag.NUM); value = v; }
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public String toString() { return ""+value; }
}
