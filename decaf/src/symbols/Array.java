package symbols;

import lexer.Tag;

/**
 * 符号表实体：数组类型类 (Array)。
 * 继承自 Type，用于表示复合数据结构——数组。
 */
public class Array extends Type {
	public Type of;
	public int size = 1;

	/**
	 * 构造数组类型对象。
	 * 
	 * @param sz 数组的大小 (元素个数)
	 * @param p  数组底层的元素类型 (Type)
	 */
	public Array(int sz, Type p) {
		super("[]", Tag.INDEX, sz * p.width);
		size = sz;
		of = p;
	}

	public String toString() {
		return "[" + size + "]" + of.toString();
	}
}
