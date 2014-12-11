
import java.math.BigDecimal;
import java.util.EmptyStackException;
import java.util.Stack;


// 测试匹配否
// 特殊加减乘除 ＋ － × ÷     
// 特殊正负 ﹢﹣
// 普通加减乘除正负 + - * / + -
public class Mixing {

	private static String expression;
	
	
	// 后缀式
	private String suffix;

	// 特殊左括号
	final static char LEFT_NORMAL = '﹙';
	// 特殊右括号
	final static char RIGHT_NORMAL = '﹚';
	// 特殊负号
	final static char MINUS = '﹣';
//	final static char MINUS = '－';
	// 特殊加号
	final static char ADD = '＋';
	// 特殊乘法
	final static char MUL = '×';
	// 特殊除法
	final static char DIV = '÷';
	// 特殊减法
	final static char SUB = '－';
	// 特殊等于号
	final static char equ = '＝';
	
	public static String getExpression() {
		return expression;
	}

	
	
	// 返回后缀
	public String getSuffix() {
		return suffix;
	}

	public void setExpression(String equation) {
		expression = equation;
		createSuffix();
		
	}
	
	public Mixing(String equation)
	{
		expression = equation;
		createSuffix();
	}
	
	public Mixing()
	{
		expression = "";
		suffix = "";
	}
	
	/**
	 *  判断括号有没有匹配
	 *  匹配方法：遇到左括号进栈，遇到右括号出栈并且比对出栈的括号
	 * 
	 */
	public  boolean isBalanced()
	{
						
		Stack<Character> store = new Stack<Character>();
		store.clear();
		char c;
		for(int i=0; i<expression.length(); i++)
		{
			c = expression.charAt(i);
			switch(c)
			{
				
				case LEFT_NORMAL:
				{
					store.push(expression.charAt(i));
					break;
				}
				case RIGHT_NORMAL:
				{	
					if(store.isEmpty() || store.pop()!= LEFT_NORMAL)
					{
						return false;
					}
					break;
					
				}
										
			}
		}
		if(store.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	

	private static boolean isOperator(char ope)
	{
		if(ope == ADD || ope==SUB 
				||ope == MUL || ope == DIV)
		{
			return true;
		}
		return false;
	}
	
	private static boolean isNum(char c)
	{
		if(c>='0' && c<='9')
		{
			return true;
		}
		return false;
	}
	// 中缀式转后缀式
	public  String createSuffix()
	{
		Stack<String> stack = new Stack<String>();
		stack.clear();
		String exp = expression.trim();
		String suf = "";
		int i = 0;
		char c;
		while(i < exp.length())
		{
			//加入第一个字符为减号‘—’,则在表达式前面加上“0-”
//			if(stack.isEmpty() && exp.startsWith(MINUS+"")){
//				exp = "0"+exp;
//				System.out.println(exp);
//			}
			c = exp.charAt(i);
			if(c == LEFT_NORMAL) // 左括号
			{
				stack.push(LEFT_NORMAL+"");
			}
			else if(isFit(c)) // 符合数字的一部分
			{
				String num = "";
				
				while(i<exp.length() && isFit(exp.charAt(i)) )
				{
					
					num+=exp.charAt(i);
					i++;
				}
				suf += (num + " "); //后缀
				i--;
			}else if(c == ADD || c == SUB || c == MUL ||c == DIV)  // 运算符
			{
				while(true)
				{
					if(stack.isEmpty())
					{
						break;
					}
					if(stack.peek().equals(""+LEFT_NORMAL))
					{
						break;
					}
					if(compare(stack.peek().charAt(0),c))
					{
						break;
					}
					suf += (stack.pop()+" "); // 后缀
				}
				stack.push(c+""); 
			}
			else if(c == RIGHT_NORMAL)
			{
				while(!stack.isEmpty())
				{
					if(stack.peek().equals(""+LEFT_NORMAL))
					{
						stack.pop();
						break;
					}
					suf += (stack.pop() + " "); // 后缀
				}
				
			}
			i++;
			
		}
		while(!stack.isEmpty())
		{
			suf += (stack.pop() + " "); // 后缀
		}
		this.suffix = suf;
		System.out.println(suf);
		return suf;
		
	}
	
	/**
	 *  判断是否符合数字的一部分
	 * @param digit
	 * @return 符合返回true 否则返回false
	 */
	private boolean isFit(char digit)
	{
		if(digit>='0' && digit<='9'||digit ==MINUS||digit=='.' )
		{
			return true;
		}
		return false;
	}
	
	// 栈中运算符与将要读取的运算符作比较
	// 返回true指示栈中运算符优先级大于将要读取运算符
	// 其他的低于或等于都返回false
	private boolean compare(char stackOpe, char nextOpe)
	{
		int v1 = value(stackOpe);
		int v2 = value(nextOpe);
		if( v1 < v2)
		{
			return true;
		}
		return false;
	}
	
	// 运算符优先级
	private int value(char ope)
	{
		if(ope==ADD || ope==SUB)
		{
			return 1;
		}	
		else if(ope==MUL || ope==DIV)
		{
			return 2;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * @param suffix 后缀式
	 * @return 利用后缀式算出结果
	 */
	public String getResult()
	{
		suffix = suffix.replace(MINUS, '-');
		String[] str = suffix.split(" ");
		Stack<String> valueStack = new Stack<String>();
		for(int i=0; i<str.length; i++)
		{
			// 遇到运算符出栈
			if(str[i].equals(ADD+"") || str[i].equals(SUB+"")
				|| str[i].equals(MUL+"") || str[i].equals(DIV+""))
			{
				String rightNum;
				String leftNum; 
				try
				{
					rightNum = valueStack.pop();
					leftNum = valueStack.pop();
					String result = calc(leftNum,rightNum,str[i]);
					valueStack.push(result);
				}catch(EmptyStackException empty)
				{
					return "算式出现异常";
				}
				
				
			}
			else
			{
				// 遇到数字进栈
				valueStack.push(str[i]);
			}
		}
		if(!valueStack.isEmpty())
		{
			return valueStack.pop();
		}
		return "栈为空 ，出现错误！";
	}
	
	public static String calc(String leftNum, String rightNum, String ope)
	{
		BigDecimal bigLeftNum = null;
		BigDecimal bigRightnum = null;
		try
		{
			bigLeftNum = new BigDecimal(leftNum);
			bigRightnum = new BigDecimal(rightNum);
		}catch(NumberFormatException e)
		{
			return "算式出现异常";
		}
		switch(ope.charAt(0))
		{
		    // 处理加法
			case ADD:return  bigLeftNum.add(bigRightnum).toString();
			// 处理减法
			case SUB:return  bigLeftNum.subtract(bigRightnum).toString();
			// 处理乘法
			case MUL:return  bigLeftNum.multiply(bigRightnum).toString();
			// 处理乘法
			case DIV:
				{
					if(bigRightnum.doubleValue()==0)
					{
						return "除数为零";
					}
					// 20为小数点后的位数
					String result = bigLeftNum.divide(bigRightnum,20,BigDecimal.ROUND_DOWN).toString();
					int mark = 0;
					if( (mark = result.indexOf('.'))!=-1)
					{
						for(int i=mark; i<result.length(); i++)
						{
							if(result.charAt(i)!='0')
							{
								mark = i;
							}
						}
						if(result.charAt(mark)=='.')
						{
							mark -= 1;
						}
						result = result.substring(0,mark+1);
						return result;
					}
					else
					{
						return result;
					}
					
				}
		}
		return null;
	}
	
	
	
	// 测试括号匹配 - —
	public static void main(String[] s)
	{
		String str1 = "﹙5.3＋3﹚×﹙3＋8﹚";
		String str2 = "[{}]{}";
		String str3 = "({}{})";
		String str4 = "16.2+(6.72-4.25)-3.72";
		String str5 = "(((10+7)*(20/30))-(2*40))";
		String str6 = "12";
		
		Mixing cal = new Mixing(str1);
		System.out.println("匹配："+cal.isBalanced());
		System.out.println("后缀："+cal.getSuffix());
		String reult = cal.getResult();
		System.out.println("结果: "+reult);
		
	}
}