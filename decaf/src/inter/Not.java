package inter;

import lexer.Token;

/**
 * 抽象语法树节点：逻辑非运算符 (!)。
 */
public class Not extends Logical {

   public Not(Token tok, Expr x2) {
      super(tok, x2, x2);
   }

   /**
    * 逻辑非运算的跳转代码生成：
    * 直接把子表达式的 true 出口和 false 出口颠倒对调即可完成逻辑取反。
    */
   public void jumping(int t, int f) {
      expr2.jumping(f, t);
   }

   public String toString() {
      return op.toString() + " " + expr2.toString();
   }
}
