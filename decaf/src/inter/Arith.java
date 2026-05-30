package inter;

import symbols.Type;
import lexer.Token;

/**
 * 抽象语法树节点：双目算术运算符 (如 +, -, *, /)。
 */
public class Arith extends Op {

	public Expr expr1, expr2;

	/**
	 * 构造双目运算符节点，并推导结果的类型。
	 */
	public Arith(Token tok, Expr x1, Expr x2) {
		super(tok, null);
		expr1 = x1;
		expr2 = x2;
		type = Type.max(expr1.type, expr2.type); // 将类型向上转型合并 (比如 int 和 float 运算结果为 float)
		if (type == null)
			error("type error");
	}

	/**
	 * 中间代码生成逻辑。对于算术运算，需先将其左右操作数分别归约到地址中，再返回新的整体算术运算节点。
	 * 被 reduce() 调用时，将拼接为类似 t = t1 + t2。
	 */
	public Expr gen() {
		return new Arith(op, expr1.reduce(), expr2.reduce());
	}

	public String toString() {
		return expr1.toString() + " " + op.toString() + " " + expr2.toString();
	}
}
