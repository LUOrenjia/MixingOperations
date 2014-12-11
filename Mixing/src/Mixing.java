
import java.math.BigDecimal;
import java.util.EmptyStackException;
import java.util.Stack;


// ����ƥ���
// ����Ӽ��˳� �� �� �� ��     
// �������� ����
// ��ͨ�Ӽ��˳����� + - * / + -
public class Mixing {

	private static String expression;
	
	
	// ��׺ʽ
	private String suffix;

	// ����������
	final static char LEFT_NORMAL = '�v';
	// ����������
	final static char RIGHT_NORMAL = '�w';
	// ���⸺��
	final static char MINUS = '��';
//	final static char MINUS = '��';
	// ����Ӻ�
	final static char ADD = '��';
	// ����˷�
	final static char MUL = '��';
	// �������
	final static char DIV = '��';
	// �������
	final static char SUB = '��';
	// ������ں�
	final static char equ = '��';
	
	public static String getExpression() {
		return expression;
	}

	
	
	// ���غ�׺
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
	 *  �ж�������û��ƥ��
	 *  ƥ�䷽�������������Ž�ջ�����������ų�ջ���ұȶԳ�ջ������
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
	// ��׺ʽת��׺ʽ
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
			//�����һ���ַ�Ϊ���š�����,���ڱ��ʽǰ����ϡ�0-��
//			if(stack.isEmpty() && exp.startsWith(MINUS+"")){
//				exp = "0"+exp;
//				System.out.println(exp);
//			}
			c = exp.charAt(i);
			if(c == LEFT_NORMAL) // ������
			{
				stack.push(LEFT_NORMAL+"");
			}
			else if(isFit(c)) // �������ֵ�һ����
			{
				String num = "";
				
				while(i<exp.length() && isFit(exp.charAt(i)) )
				{
					
					num+=exp.charAt(i);
					i++;
				}
				suf += (num + " "); //��׺
				i--;
			}else if(c == ADD || c == SUB || c == MUL ||c == DIV)  // �����
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
					suf += (stack.pop()+" "); // ��׺
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
					suf += (stack.pop() + " "); // ��׺
				}
				
			}
			i++;
			
		}
		while(!stack.isEmpty())
		{
			suf += (stack.pop() + " "); // ��׺
		}
		this.suffix = suf;
		System.out.println(suf);
		return suf;
		
	}
	
	/**
	 *  �ж��Ƿ�������ֵ�һ����
	 * @param digit
	 * @return ���Ϸ���true ���򷵻�false
	 */
	private boolean isFit(char digit)
	{
		if(digit>='0' && digit<='9'||digit ==MINUS||digit=='.' )
		{
			return true;
		}
		return false;
	}
	
	// ջ��������뽫Ҫ��ȡ����������Ƚ�
	// ����trueָʾջ����������ȼ����ڽ�Ҫ��ȡ�����
	// �����ĵ��ڻ���ڶ�����false
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
	
	// ��������ȼ�
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
	 * @param suffix ��׺ʽ
	 * @return ���ú�׺ʽ������
	 */
	public String getResult()
	{
		suffix = suffix.replace(MINUS, '-');
		String[] str = suffix.split(" ");
		Stack<String> valueStack = new Stack<String>();
		for(int i=0; i<str.length; i++)
		{
			// �����������ջ
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
					return "��ʽ�����쳣";
				}
				
				
			}
			else
			{
				// �������ֽ�ջ
				valueStack.push(str[i]);
			}
		}
		if(!valueStack.isEmpty())
		{
			return valueStack.pop();
		}
		return "ջΪ�� �����ִ���";
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
			return "��ʽ�����쳣";
		}
		switch(ope.charAt(0))
		{
		    // ����ӷ�
			case ADD:return  bigLeftNum.add(bigRightnum).toString();
			// �������
			case SUB:return  bigLeftNum.subtract(bigRightnum).toString();
			// ����˷�
			case MUL:return  bigLeftNum.multiply(bigRightnum).toString();
			// ����˷�
			case DIV:
				{
					if(bigRightnum.doubleValue()==0)
					{
						return "����Ϊ��";
					}
					// 20ΪС������λ��
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
	
	
	
	// ��������ƥ�� - ��
	public static void main(String[] s)
	{
		String str1 = "�v5.3��3�w���v3��8�w";
		String str2 = "[{}]{}";
		String str3 = "({}{})";
		String str4 = "16.2+(6.72-4.25)-3.72";
		String str5 = "(((10+7)*(20/30))-(2*40))";
		String str6 = "12";
		
		Mixing cal = new Mixing(str1);
		System.out.println("ƥ�䣺"+cal.isBalanced());
		System.out.println("��׺��"+cal.getSuffix());
		String reult = cal.getResult();
		System.out.println("���: "+reult);
		
	}
}