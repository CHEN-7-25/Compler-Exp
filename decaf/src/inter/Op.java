package inter;

import symbols.Type;
import lexer.Token;

/**
 * 抽象语法树节点：运算符（Operator）的基类。
 * 派生出算术运算符、一元运算符和数组访问等表达式形式。
 */
public class Op extends Expr {

	public Op(Token tok, Type p) {
		super(tok, p);
	}

	/**
	 * 运算表达式归约逻辑。
	 * 将当前的运算表达式真正“计算”一次（生成具体代码），并将结果保存到一个新的临时变量中。
	 * 
	 * @return 存放运算结果的临时变量节点 (Temp)
	 */
	public Expr reduce() {
		Expr x = gen(); // 构建自身，或向下递归归约子表达式
		Temp t = new Temp(type); // 申请一个新的临时变量存放结果
		emit(t.toString() + " = " + x.toString()); // [TAC生成] 发射三地址代码：t = x
		return t;
	}
}
