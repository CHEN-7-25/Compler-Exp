package inter;

import symbols.Type;
import lexer.Token;

public class For extends Stmt {

    Expr expr; // 循环的判定条件，如 i < 10
    Stmt stmt1, stmt2, stmt3; // stmt1:初始化(i=0), stmt2:步进更新(i=i+1), stmt3:循环体

    public For() { 
        expr = null; stmt1 = null; stmt2 = null; stmt3 = null; 
    }

    public void init(Stmt s1, Expr x, Stmt s2, Stmt s3) {
        this.stmt1 = s1;
        this.expr = x;
        this.stmt2 = s2;
        this.stmt3 = s3;
        // 校验循环条件是否为布尔类型
        if (expr.type != Type.Bool) {
            expr.error("boolean required in for");
        }
    }

    @Override
    public void gen(int b, int a) {
        after = a;  // 记录跳出循环的目标标号（支持 break 语句）
        
        // 1. 生成初始化部分的中间代码
        if (stmt1 != Stmt.Null) {
            stmt1.gen(b, a); 
        }
        
        int label = newlabel();   // 循环条件测试的标号 L_test
        emitlabel(label);
        
        // 2. 判定条件：如果条件为假，直接跳到标号 a (即循环结束的地方)
        expr.jumping(0, a);       
        
        int label2 = newlabel();  // 循环体的标号 L_body
        emitlabel(label2);
        
        // 3. 生成循环体代码
        stmt3.gen(label2, a);     
        
        // 4. 生成步进更新部分的代码
        if (stmt2 != Stmt.Null) {
            stmt2.gen(label2, a); 
        }
        
        // 5. 无条件跳回到条件测试处
        emit("goto L" + label);   
    }
}