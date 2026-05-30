package inter;

import symbols.Type;
import lexer.Token;

/**
 * 抽象语法树节点：逻辑运算符（例如 &&, ||, ! 等）的基类。
 */
public class Logical extends Expr {
	public Expr expr1, expr2;

	/**
	 * 构造逻辑运算节点，并做类型检查，必须均为布尔型。
	 */
	Logical(Token tok, Expr x1, Expr x2) {
		super(tok, null);
		expr1 = x1;
		expr2 = x2;
		type = check(expr1.type, expr2.type);
		if (type == null)
			error("type error");
	}

	/**
	 * 语义分析：检查逻辑运算符左右两侧操作数类型。
	 * 逻辑运算只允许在布尔类型之间进行。
	 */
	public Type check(Type p1, Type p2) {
		if (p1 == Type.Bool && p2 == Type.Bool)
			return Type.Bool;
		else
			return null;
	}

	/**
	 * 逻辑表达式转化为值的中间代码生成。
	 * 通过控制流跳转生成具体的布尔值 (true / false) 并存入临时变量。
	 * 
	 * @return 存放运算结果为 true 或 false 的临时变量
	 */
	public Expr gen() {
		int f = newlabel();
		int a = newlabel();
		Temp temp = new Temp(type); // 申请局部变量装载评估结果
		this.jumping(0, f); // 如果表达式为假，则跳转到 f 标志处
		emit(temp.toString() + " = true"); // 如果没有跳转，说明为真
		emit("goto L" + a); // 结论赋值完，跳向出口
		emitlabel(f);
		emit(temp.toString() + " = false"); // 假分支的情况
		emitlabel(a); // 公共出口
		return temp;
	}
}
