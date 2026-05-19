package inter;

import symbols.Type;
import lexer.Word;

/**
 * 类 Temp 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Temp extends Expr{
	static int count = 0;
	int number = 0;
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Temp(Type p) { super(Word.temp,p); number = ++count; }
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public String toString() {return "t"+number;}
}
