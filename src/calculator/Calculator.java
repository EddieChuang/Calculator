package calculator;

import java.util.ArrayList;
import java.util.Stack;



public class Calculator {
	
	private static String symbol_add = "+";
	private static String symbol_sub = "\u2013";
	private static String symbol_mul = "\u00D7";
	private static String symbol_div = "\u00F7";
	Stack<String> expression;
	
	Calculator(){
		initialize();
	}
	
	private void initialize() {
		expression = new Stack<String>();
	}
	
	public boolean isEmpty() {
		return expression.isEmpty();
	}
	
	public void clear() {
		expression.clear();
	}
	
	public void set(String element, int index) {
		expression.set(index, element);
	}
	
	public void push(String element) {
		expression.add(element);
	}
	
	public String pop() {
		return expression.pop();
	}
	
	public String top() {
		return expression.peek();
	}
	
	public String getExpression() {
		String res = "";
		for(String op : expression) {
			res += op.contains("-") ? "(" + op + ")" : op;
		}
		return res;
	}
	
	private ArrayList<String> getPostfix(){
		System.out.println(expression);
		Stack<String> tmp = new Stack<String>();
		ArrayList<String> postfix = new ArrayList<String>();
		for(int i = 0; i < expression.size(); ++i) {
			if(i % 2 == 0) {
				// operand
				postfix.add(expression.get(i));
			} else {
				// operator
				String operator = expression.get(i);
				while(!tmp.isEmpty() && priority(tmp.peek()) >= priority(operator)) {
					postfix.add(tmp.pop());
				}
				tmp.add(operator);
			}
		}
		while(!tmp.isEmpty()) {
			postfix.add(tmp.pop());
		}
		System.out.println(postfix);
		return postfix;
	}
	
	public String calculate() {
		ArrayList<String> postfix = getPostfix();
		Stack<Float> values = new Stack<Float>();
		for(String element : postfix) {
			if(isOperator(element)) {
				float y = values.pop();
				float x = values.pop();
				values.push(evaluate(x, y, element));
			} else {
				values.push(Float.valueOf(element));
			}
		}
		System.out.println(String.valueOf(values.peek()));
		return String.valueOf(values.peek());
	}
	
	private static float evaluate(float x, float y, String operator) {
		if(operator.equals(symbol_add)) {
			return x + y;
		} else if(operator.equals(symbol_sub)) {
			return x - y;
		} else if(operator.equals(symbol_mul)) {
			return x * y;
		} else if(operator.equals(symbol_div)) {
			return x / y;
		}
		return 0;
	}
	
	private static boolean isOperator(String element) {
		String allSymbol = symbol_add + symbol_sub + symbol_mul + symbol_div;
		return allSymbol.contains(element);
	}
	
	private static int priority(String operator) {
		return operator.equals("+") || operator.equals(symbol_sub) ? 0 : 1;
	}
	
	public static void main(String[] args) {
		Calculator calculator = new Calculator();
		// 1 * 2 + 3 * 4 / 2 = 2 + 6 = 8
		calculator.push("0");
		calculator.push("\u00D7");
		calculator.push("2");
		calculator.push("+");
		calculator.push("3");
		calculator.push("\u00D7");
		calculator.push("4");
		calculator.push("\u00F7");
		calculator.push("3");
//		calculator.getPostfix();
		
		System.out.println(calculator.calculate());
//		System.out.println("0.0".split("\\."));
//		private static String symbol_sub = "\u2013";
//		private static String symbol_mul = "";
//		private static String symbol_div = "\u00F7";
	}

}
