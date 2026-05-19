package symbols;

import inter.Id;

import java.util.Hashtable;

import lexer.Token;

/**
 * 类 Env 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Env {
	private Hashtable table;
	protected Env prev;
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Env(Env n) { table = new Hashtable(); prev=n;}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public void put(Token w, Id i) {
		table.put(w, i);
	}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Id get(Token w){
		for(Env e=this; e!=null; e=e.prev){
			Id found = (Id)(e.table.get(w));
			if(found!=null) return found;
		}
		return null;
	}
}
