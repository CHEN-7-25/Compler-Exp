package inter;

import symbols.Type;
import lexer.Word;

/**
 * 类 Id 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Id extends Expr {
	
	public int offset;
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Id(Word id, Type p, int b) { super(id,p); offset=b; }
}
