package inter;

import symbols.Type;
import lexer.Tag;
import lexer.Word;

/**
 * 类 Access 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Access extends Op{

	public Id array;
	public Expr index;
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Access(Id a, Expr i, Type p){
		super(new Word("[]",Tag.INDEX),p);
		array=a; index = i;
	}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Expr gen() { return new Access(array, index.reduce(),type); }
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public void jumping(int t, int f) { emitjumps(reduce().toString(),t,f); }
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public String toString() {
		return array.toString()+" [ "+index.toString()+" ]";
	}
}
