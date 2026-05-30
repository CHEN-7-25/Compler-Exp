package inter;

import symbols.Type;
import lexer.Num;
import lexer.Token;
import lexer.Word;

/**
 * 抽象语法树节点：常数面值（叶子节点）。
 * 代表数字面值常量以及布尔极限值（True, False）。
 */
public class Constant extends Expr {

	public Constant(Token tok, Type p) {
		super(tok, p);
	}

	public Constant(int i) {
		super(new Num(i), Type.Int);
	}

	// 内置全局的真理布尔常量实例
	public static final Constant True = new Constant(Word.True, Type.Bool),
			False = new Constant(Word.False, Type.Bool);

	/**
	 * 布尔常数的短路跳转代码生成。
	 * 如果是 True 常量，需要强制跳转到真出口；如果是 False 需要强制跳转到假出口。
	 */
	public void jumping(int t, int f) {
		if (this == True && t != 0)
			emit("goto L" + t); // [TAC生成] 肯定为真时硬编码无条件跳转
		else if (this == False && f != 0)
			emit("goto L" + f);
	}
}
