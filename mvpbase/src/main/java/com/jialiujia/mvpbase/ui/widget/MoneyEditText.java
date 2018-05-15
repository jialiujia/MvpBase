package com.jialiujia.mvpbase.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * MvpBase
 * Created by Administrator on 2018/4/15.
 */

public class MoneyEditText extends AppCompatEditText {
	public MoneyEditText(Context context) {
		super(context);
		init();
	}

	public MoneyEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MoneyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	private void init() {
		requestFocus();
		addTextChangedListener(new TextWatcher() {
			private String numStr;
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (TextUtils.isEmpty(getAmount())) { //如果金额为空，重新设置金额，光标移到最后
					numStr = "0.00";
					setAmount(numStr);
					moveCursorEnd(numStr.length());
				}
				if (isChanged()){//如果金额发生改变，格式化金额
					numStr = getMoneyStr(s.toString());
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				String _etStr = getAmount();
				//UtilLogs.e(_etStr + "====" + numberStr);
				//判断输入框的值是否等于金额的值，如果不相同则赋值，如果不断判断TextWatch将会死循环
				if (isChanged()) {//重要：如果文字沒有改变，不再为EditText重新赋值
					setAmount(numStr);//为EditText赋值
					moveCursorEnd(numStr.length());//将光标定位到结尾
				}
			}

			private boolean isChanged(){
				String _etStr = getAmount();
				return !TextUtils.isEmpty(getAmount()) && !_etStr.equals(numStr);
			}

			/*获得金额，此方法做了格式化金额操作，移动小数点，删0，补0*/
			private String getMoneyStr(String pMoney){
				//定义一个StringBuilder，便于操作字符串
				StringBuilder _moneyBuf = new StringBuilder(pMoney);

				//①删除小数点（如果有）
				int _dotIndex = _moneyBuf.indexOf(".");
				if (!(_dotIndex < 0)) {
					_moneyBuf.deleteCharAt(_dotIndex);
				}

				//②判断字符串长度，>3,第一个字符为"0"，删除掉；<3,前面补0，保持至少三位数
				int _len = _moneyBuf.length();
				if (_len > 3 && "0".equals(_moneyBuf.substring(0, 1))) {
					_moneyBuf.deleteCharAt(0);
				} else {
					for (int i = 0; i < 3 - _len; i++) {
						_moneyBuf.insert(0, "0");
					}
				}
				//③将小数点插入倒数第二位和第三位之间，保证两位小数
				_moneyBuf.insert(_moneyBuf.length() - 2, ".");

				return _moneyBuf.toString();
			}
		});
	}

	@Override
	protected void onSelectionChanged(int selStart, int selEnd) {
		moveCursorEnd(getText().length());
		super.onSelectionChanged(selStart, selEnd);
	}

	public void setAmount(String numStr) {
		this.setText(numStr);
	}

	public String getAmount() {
		return getText().toString();
	}

	/**
	 * 将光标移到最后
	 * @param pEnd
	 */
	private void moveCursorEnd(int pEnd) {
		this.setSelection(pEnd);
	}
}
