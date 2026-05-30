package inter;

import symbols.Array;
import symbols.Type;

/**
 * 抽象语法树节点：数组元素赋值语句 (array[index] = expr)。
 */
public class SetElem extends Stmt {

   public Id array;
   public Expr index;
   public Expr expr;

   /**
    * 构造形如 array[index] = expr 的数组赋值节点，并做类型检查。
    * 
    * @param x 数组访问表达式
    * @param y 右侧被赋值的表达式
    */
   public SetElem(Access x, Expr y) {
      array = x.array;
      index = x.index;
      expr = y;
      if (check(x.type, expr.type) == null)
         error("type error");
   }

   /**
    * 语义分析：校验数组元素类型和被赋的值的类型是否匹配。
    */
   public Type check(Type p1, Type p2) {
      if (p1 instanceof Array || p2 instanceof Array)
         return null;
      else if (p1 == p2)
         return p2;
      else if (Type.numeric(p1) && Type.numeric(p2))
         return p2;
      else
         return null;
   }

   /**
    * 中间代码生成：生成计算下表和表达式的指令，然后进行数组赋值。
    */
   public void gen(int b, int a) {
      String s1 = index.reduce().toString(); // 获取归约后的有效索引地址（临时变量或常量）
      String s2 = expr.reduce().toString(); // 获取归约后的右值地址
      emit(array.toString() + " [ " + s1 + " ] = " + s2); // [TAC生成] 发射数组赋值指令
      // System.out.println("assignment");
   }
}
