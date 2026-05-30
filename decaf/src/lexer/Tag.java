package lexer;

/**
 * 词法记号类别（Tag）常量集合。
 * 为抽象关键字、运算符、类型和字面量等定义唯一的整数标识。
 * ASCII 字符（如 '+', '-'）直接使用其对应整数值，因此此处定义的常量均大于 255。
 */
public class Tag {
    public final static int AND = 256, BASIC = 257, BREAK = 258, DO = 259, ELSE = 260,
            EQ = 261, FALSE = 262, GE = 263, ID = 264, IF = 265,
            INDEX = 266, LE = 267, MINUS = 268, NE = 269, NUM = 270,
            OR = 271, REAL = 272, TEMP = 273, TRUE = 274, WHILE = 275,
            FOR = 276;
}
