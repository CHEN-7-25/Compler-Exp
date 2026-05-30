package symbols;

import lexer.Tag;
import lexer.Word;

/**
 * 符号表实体：数据类型类 (Type)。
 * 继承自关键字 Word，用于表示语言中的基本数据类型及其占用的内存宽度。
 */
public class Type extends Word {
	public int width = 0;

	/**
	 * 构造数据类型对象。
	 * 
	 * @param s   类型的字符串名称 (如 "int")
	 * @param tag 标签类别 (通常为 Tag.BASIC)
	 * @param w   该类型在内存中所占的字节宽度
	 */
	public Type(String s, int tag, int w) {
		super(s, tag);
		width = w;
	}

	// 预定义语言中支持的基本类型标量常数
	public static final Type Int = new Type("int", Tag.BASIC, 4),
			Float = new Type("float", Tag.BASIC, 8),
			Char = new Type("char", Tag.BASIC, 1),
			Bool = new Type("bool", Tag.BASIC, 1);

	/**
	 * 检查指定的类型是否为数值类型。
	 * 
	 * @param p 待检查的类型对象
	 * @return 如果是 char, int, 或 float 类型则返回 true，否则返回 false
	 */
	public static boolean numeric(Type p) {
		if (p == Type.Char || p == Type.Int || p == Type.Float)
			return true;
		else
			return false;
	}

	/**
	 * 类型强制向上转换 (Type Promotion / Coercion) 规则运算。
	 * 在含有两种不同数值类型操作数的运算中，决定结果的返回类型。
	 * 
	 * @param p1 操作数 1 的类型
	 * @param p2 操作数 2 的类型
	 * @return 提升后的结果类型。如果其中一项非数值类型，则返回 null。
	 */
	public static Type max(Type p1, Type p2) {
		if (!numeric(p1) || !numeric(p2))
			return null;
		else if (p1 == Type.Float || p2 == Type.Float)
			return Type.Float;
		else if (p1 == Type.Int || p2 == Type.Int)
			return Type.Int;
		else
			return Type.Char;
	}

}
