package inter;

import lexer.Token;

/**
 * 抽象语法树节点：逻辑或运算符 (||)。
 */
public class Or extends Logical {

   public Or(Token tok, Expr x1, Expr x2) {
      super(tok, x1, x2);
   }

   /**
    * 逻辑或的短路求值 (Short-circuit evaluation) 控制流生成。
    * 如果左操作数为真，则整体一定为真（发生短路，直接跳向真出口）；
    * 只有当左操作数为假时，才会继续计算并测试右侧操作数。
    */
   public void jumping(int t, int f) {
      int label = t != 0 ? t : newlabel(); // 确定真出口标号
      expr1.jumping(label, 0); // 如果左侧为真，直接越过右侧跳转到真出口 (短路)
      expr2.jumping(t, f); // 左侧如果掉落下来（为假），则运算右侧
      if (t == 0)
         emitlabel(label);
   }
}
