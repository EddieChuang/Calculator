package calculator;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

public class CalculatorApp {
	
	private enum Sign{
		positive, negative
	}
	private String symbol_add = "+";
	private String symbol_sub = "\u2013";
	private String symbol_mul = "\u00D7";
	private String symbol_div = "\u00F7";

	private JFrame frame;
	private JTextField expText;
	private String operand;
	private String operator;
	private Sign sign; // true: positive, false: negative
	private int pos;
	private Calculator calculator;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalculatorApp window = new CalculatorApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CalculatorApp() {
		calculator = new Calculator();
		initialize();
		initCalculator();
	}
	
	/**
	 *  點擊數字按鈕更新運算元
	 * */
	private ActionListener numberButtonActionListener(String digit) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!operator.equals("")) {
					// 輸入完運算子，接著輸入運算元
					operator = "";
					operand = digit;
					calculator.push(operand);
					pos += 1;
				} else {
					// 繼續輸入運算元
					if(operand.equals("0") || operand.equals("")) {
						operand = digit;
					} else {
						operand += digit;
					}
					calculator.set(operand, pos);
				}
				expText.setText(calculator.getExpression());
			}
		};
	}
	
	/**
	 *  點擊正負號按鈕，切換運算元的正負號
	 * */
	private ActionListener signButtonActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!operand.equals("") && !operand.equals("0")) {
					sign = sign == Sign.positive ? Sign.negative : Sign.positive;
					if(sign == Sign.positive) {
						operand = operand.replace("-", "");
					} else {
						operand = "-" + operand;
					}
					calculator.set(operand, pos);
					expText.setText(calculator.getExpression());
				}
			}
		};
	}
	
	/**
	 *  點擊小數點按鈕，輸入小數
	 * */
	private ActionListener pointButtonActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!operand.equals("") && !operand.contains(".")) {
					operand += ".";
					calculator.set(operand, pos);
					expText.setText(calculator.getExpression());
				}
			}
		};
	}
	
	/**
	 * 點擊等於按鈕，計算答案
	 * */
	private ActionListener equalButtonActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!operand.equals("")) {
					String answer = format(calculator.calculate());
					initCalculator();
					operand = answer;
					calculator.set(answer, 0);
					expText.setText(calculator.getExpression());
				}
			}
		};
	}	
	
	/**
	 * 點擊運算子按鈕
	 */
	private ActionListener operatorButtonActionListener(String op) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(expText.getText().equals("0")) {
					return;
				}
				operator = op;
				if(!operand.equals("")) {
					operand = "";
					calculator.push(operator);
					sign = Sign.positive;
					pos += 1;
				} else {
					calculator.set(operator, pos);
				}
				expText.setText(calculator.getExpression());
			}
			
		};
	}

	/**
	 * 點擊倒退按鈕，刪除一個字元
	 */
	private ActionListener backButtonActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!operator.equals("")){
					calculator.pop();
					getTopOperand();
					
				} else if(!operand.equals("")) {
					operand = operand.substring(0, operand.length()-1);
					if(operand.equals("") || operand.equals("-")) {
						calculator.pop();
						if(calculator.isEmpty()) {
							initCalculator();
						} else {
							getTopOperator();
						}
					} else {
						calculator.set(operand, pos);
					}
				}
				
				expText.setText(calculator.getExpression());
			}
		};
	}
	
	/**
	 * 點擊清除按鈕，全部重置
	 * */
	private ActionListener clearButtonActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculator.clear();
				initCalculator();
				expText.setText(calculator.getExpression());
			}
		};
	}
	
	/**
	 * 點擊ce按鈕，清除目前運算元。若目前是運算子，則不動作
	 * */
	private ActionListener ceButtonActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!operand.equals("")) {
					calculator.pop();
					if(calculator.isEmpty()) {
						initCalculator();
					} else {
						getTopOperator();
					}
					expText.setText(calculator.getExpression());
				}
			}
		};
	}
	
	public static String format(String s) {
		String[] splited = s.split("\\.");
		return Integer.parseInt(splited[1]) == 0 ? splited[0] : s;
	}
	
	/**
	 * 初始化Calculator
	 * */
	private void initCalculator() {
		calculator.clear();
		calculator.push("0");
		operator = "";
		operand = "0";
		sign = Sign.positive;
		pos = 0;
	}
	
	/**
	 * 取得calculator stack中最上面的運算子
	 * */
	private void getTopOperator() {
		operator = calculator.top();
		operand = "";
		sign = Sign.positive;
		pos -= 1;
	}
	
	/**
	 * 取得calculator stack中最上面的運算元
	 * */
	private void getTopOperand() {
		operand = calculator.top();
		operator = "";
		sign = operand.contains("-") ? Sign.negative : Sign.positive;
		pos -= 1;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Tahoma", Font.BOLD, 24));
		frame.setBounds(100, 100, 350, 528);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		expText = new JTextField();
		expText.setText("0");
		expText.setFont(new Font("Tahoma", Font.PLAIN, 30));
		expText.setHorizontalAlignment(SwingConstants.RIGHT);
		expText.setBounds(15, 15, 298, 49);
		frame.getContentPane().add(expText);
		expText.setColumns(10);
		
		/***** number button *****/
		JButton btn0 = new JButton("0");
		btn0.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn0.addActionListener(numberButtonActionListener(btn0.getText()));
		btn0.setBounds(90, 379, 76, 76);
		frame.getContentPane().add(btn0);
		
		JButton btn1 = new JButton("1");
		btn1.addActionListener(numberButtonActionListener(btn1.getText()));
		btn1.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn1.setBounds(15, 304, 76, 76);
		frame.getContentPane().add(btn1);
		
		JButton btn2 = new JButton("2");
		btn2.addActionListener(numberButtonActionListener(btn2.getText()));
		btn2.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn2.setBounds(90, 304, 76, 76);
		frame.getContentPane().add(btn2);
		
		JButton btn3 = new JButton("3");
		btn3.addActionListener(numberButtonActionListener(btn3.getText()));
		btn3.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn3.setBounds(165, 304, 76, 76);
		frame.getContentPane().add(btn3);
		
		JButton btn4 = new JButton("4");
		btn4.addActionListener(numberButtonActionListener(btn4.getText()));
		btn4.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn4.setBounds(15, 229, 76, 76);
		frame.getContentPane().add(btn4);
		
		JButton btn5 = new JButton("5");
		btn5.addActionListener(numberButtonActionListener(btn5.getText()));
		btn5.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn5.setBounds(90, 229, 76, 76);
		frame.getContentPane().add(btn5);
		
		JButton btn6 = new JButton("6");
		btn6.addActionListener(numberButtonActionListener(btn6.getText()));
		btn6.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn6.setBounds(165, 229, 76, 76);
		frame.getContentPane().add(btn6);
		
		JButton btn7 = new JButton("7");
		btn7.addActionListener(numberButtonActionListener(btn7.getText()));
		btn7.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn7.setBounds(15, 154, 76, 76);
		frame.getContentPane().add(btn7);
		
		JButton btn8 = new JButton("8");
		btn8.addActionListener(numberButtonActionListener(btn8.getText()));
		btn8.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn8.setBounds(90, 154, 76, 76);
		frame.getContentPane().add(btn8);
		
		JButton btn9 = new JButton("9");
		btn9.addActionListener(numberButtonActionListener(btn9.getText()));
		btn9.setFont(new Font("Tahoma", Font.BOLD, 30));
		btn9.setBounds(165, 154, 76, 76);
		frame.getContentPane().add(btn9);
		/***** end of number button *****/
		
		/***** sign button *****/
		JButton btn_sign = new JButton("\u00B1");
		btn_sign.addActionListener(signButtonActionListener());
		btn_sign.setBackground(Color.WHITE);
		btn_sign.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_sign.setBounds(15, 379, 76, 76);
		frame.getContentPane().add(btn_sign);
		/***** end of sign button *****/
		
		/***** point button *****/
		JButton btn_point = new JButton(".");
		btn_point.addActionListener(pointButtonActionListener());
		btn_point.setBackground(Color.WHITE);
		btn_point.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_point.setBounds(165, 379, 76, 76);
		frame.getContentPane().add(btn_point);
		/***** end of point button *****/
		
		/***** equal button *****/
		JButton btn_equal = new JButton("=");
		btn_equal.addActionListener(equalButtonActionListener());
		btn_equal.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_equal.setBounds(240, 379, 76, 76);
		frame.getContentPane().add(btn_equal);
		/***** end of equal button *****/
		
		/***** operator button *****/
		JButton btn_add = new JButton(symbol_add);
		btn_add.addActionListener(operatorButtonActionListener(btn_add.getText()));
		btn_add.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_add.setBounds(240, 304, 76, 76);
		frame.getContentPane().add(btn_add);
		
		JButton btn_sub = new JButton(symbol_sub);
		btn_sub.addActionListener(operatorButtonActionListener(btn_sub.getText()));
		btn_sub.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_sub.setBounds(240, 229, 76, 76);
		frame.getContentPane().add(btn_sub);
		
		JButton btn_mul = new JButton(symbol_mul);
		btn_mul.addActionListener(operatorButtonActionListener(btn_mul.getText()));
		btn_mul.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_mul.setBounds(240, 154, 76, 76);
		frame.getContentPane().add(btn_mul);
		
		JButton btn_div = new JButton(symbol_div);
		btn_div.addActionListener(operatorButtonActionListener(btn_div.getText()));
		btn_div.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_div.setBounds(240, 79, 76, 76);
		frame.getContentPane().add(btn_div);
		/***** end of operator button *****/
		
		/***** back button *****/
		JButton btn_back = new JButton("\u2190");
		btn_back.addActionListener(backButtonActionListener());
		btn_back.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_back.setBounds(165, 79, 76, 76);
		frame.getContentPane().add(btn_back);
		/***** end of back button *****/
		
		/***** clear button *****/
		JButton btn_clear = new JButton("C");
		btn_clear.addActionListener(clearButtonActionListener());
		btn_clear.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_clear.setBounds(90, 79, 76, 76);
		frame.getContentPane().add(btn_clear);
		/***** end of clear button *****/
		
		/***** ce button *****/
		JButton btn_ce = new JButton("CE");
		btn_ce.addActionListener(ceButtonActionListener());
		btn_ce.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btn_ce.setBounds(15, 79, 76, 76);
		frame.getContentPane().add(btn_ce);
		/***** end of ce button *****/
	}
}
