package inter;

import symbols.Array;
import symbols.Type;
import lexer.Token;

/**
 * 抽象语法树节点：关系运算符 (如 <, >, <=, >=, ==, !=)。
 */
public class Rel extends Logical {

   public Rel(Token tok, Expr x1, Expr x2) {
      super(tok, x1, x2);
   }

   /**
    * 语义分析：对关系运算符两边的操作数进行类型校验。
    * 不支持数组整体进行比较，标量或者同类型才允许比较。
    */
   public Type check(Type p1, Type p2) {
      if (p1 instanceof Array || p2 instanceof Array)
         return null;
      else if (p1 == p2)
         return Type.Bool; // 相同类型（同名类型的标量）的关系比较结果为布尔型
      else
         return null;
   }

   /**
    * 为关系表达式生成跳转指令。
    * 将两侧子表达式归约求值后，组成形如 "a < b" 的布尔测试片段，调用基类的发射跳转。
    */
   public void jumping(int t, int f) {
      Expr a = expr1.reduce(); // 分别计算左右侧的值
      Expr b = expr2.reduce();
      String test = a.toString() + " " + op.toString() + " " + b.toString(); // 比如 i < 10
      emitjumps(test, t, f); // [TAC生成] 发射条件判断 if (i < 10) goto ...
   }
}
