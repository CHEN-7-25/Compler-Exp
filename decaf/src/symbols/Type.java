package symbols;

import lexer.Tag;
import lexer.Word;

/**
 * 类 Type 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Type extends Word {
	public int width = 0;
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Type(String s, int tag, int w) { super(s,tag); width=w; }
	public static final Type
		Int   = new Type( "int",   Tag.BASIC, 4 ),
	    Float = new Type( "float", Tag.BASIC, 8 ),
	    Char  = new Type( "char",  Tag.BASIC, 1 ),
	    Bool  = new Type( "bool",  Tag.BASIC, 1 );
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public static boolean numeric(Type p)	{
		if(p==Type.Char||p==Type.Int||p==Type.Float) return true;
		else return false;
	}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public static Type max(Type p1, Type p2){
		if(!numeric(p1)||!numeric(p2)) return null;
		else if(p1==Type.Float||p2==Type.Float) return Type.Float;
		else if(p1==Type.Int||p2==Type.Int) return Type.Int;
		else return Type.Char;
	}
	
}
