package inter;

import lexer.Token;

/**
 * 抽象语法树节点：逻辑与运算符 (&&)。
 */
public class And extends Logical {

	public And(Token tok, Expr x1, Expr x2) {
		super(tok, x1, x2);
	}

	/**
	 * 逻辑与的短路求值 (Short-circuit evaluation) 控制流生成。
	 * 如果左操作数为假，则整体一定为假（发生短路，直接跳向假出口）；
	 * 只有当左操作数为真时，才会去计算并测试右侧操作数。
	 */
	public void jumping(int t, int f) {
		int label = f != 0 ? f : newlabel(); // 确定假出口标号
		expr1.jumping(0, label); // 如果左侧为假，直接跳转到假出口 (短路)
		expr2.jumping(t, f); // 左侧如果掉落下来（为真），则运算右侧
		if (f == 0)
			emitlabel(label);
	}

}
