package symbols;

import lexer.Tag;

/**
 * 类 Array 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Array extends Type{
	public Type of;
	public int size = 1;
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Array(int sz, Type p){
		super("[]", Tag.INDEX, sz*p.width); size = sz; of = p;
	}
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public String toString() { return "["+size+"]"+of.toString(); }
}
