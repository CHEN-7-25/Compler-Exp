package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import parser.Parser;
import lexer.Lexer;

/**
 * 类 Main 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Main {

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File file = new File("D:\\02_Study\\Experiments\\编译技术\\01_Codes\\decaf\\test\\test1.txt");
		Reader reader = null;
		reader = new InputStreamReader(new FileInputStream(file));
		Lexer lex = new Lexer(reader);
		Parser parser = new Parser(lex);
		parser.program();
		System.out.print("\n");
	}

}
