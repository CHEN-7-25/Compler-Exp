package inter;

import symbols.Type;
import lexer.Token;

/**
 * 抽象语法树节点：一元运算符 (如一元负号 -)。
 * （注意：逻辑非运算符 ! 作为另一个派生类 Not 的逻辑处理）。
 */
public class Unary extends Op {

   public Expr expr;

   /**
    * 构造一元运算符节点，并进行类型检查。
    */
   public Unary(Token tok, Expr x) { // handles minus, for ! see Not
      super(tok, null);
      expr = x;
      type = Type.max(Type.Int, expr.type); // 将类型和 Int 结合提升
      if (type == null)
         error("type error");
   }

   /**
    * 中间代码生成逻辑：将唯一的操作数归约为地址，然后返回运算节点自身。
    */
   public Expr gen() {
      return new Unary(op, expr.reduce());
   }

   public String toString() {
      return op.toString() + " " + expr.toString();
   }
}
