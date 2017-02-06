package com.mynewmain777777;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.videolan.adasvlv.R;

public class MyValueView extends LinearLayout {
	Context context;
	private TextView titleText;
	private TextView valueText;
	private Button plusBtn;
	private Button minusBtn;
	private Switch switchBtn;
	
	private int floatTimes = 1;
	private int step = 1;
	private int valueCur = 0;
	private int valueMax = 0;
	private int valueMin = 0;
	
	public MyValueView(Context context,int model) {
			super(context);
			
		View view = View.inflate(context, R.layout.my_value_view, this);
		titleText = (TextView) view.findViewById(R.id.valueViewTitleText);
		valueText = (TextView) view.findViewById(R.id.valueViewValueText);
		plusBtn = (Button) view.findViewById(R.id.valueViewPlusBtn);
		minusBtn = (Button) view.findViewById(R.id.valueViewMinusBtn);
		switchBtn = (Switch) view.findViewById(R.id.valueViewSwitchBtn);
		
		changeModel(model);
		
		//处理button点击事件
		Button.OnClickListener plusOnClickListener = new Button.OnClickListener()
		{
			public void onClick(View v) {
				changeValue(valueCur + step);
			}
		};
		plusBtn.setOnClickListener(plusOnClickListener);
		
		Button.OnClickListener minusOnClickListener = new Button.OnClickListener()
		{
			public void onClick(View v) {
				changeValue(valueCur - step);
			}
		};
		minusBtn.setOnClickListener(minusOnClickListener);
		
		changeValue(valueCur);
		 // TODO Auto-generated constructor stub
	}
	
	public void changeModel(int model)
	{
		if(model == 0)
		{
			switchBtn.setVisibility(View.GONE);
			valueText.setVisibility(View.VISIBLE);
			plusBtn.setVisibility(View.VISIBLE);
			minusBtn.setVisibility(View.VISIBLE);
		}
		else
		{
			switchBtn.setVisibility(View.VISIBLE);
			valueText.setVisibility(View.GONE);
			plusBtn.setVisibility(View.GONE);
			minusBtn.setVisibility(View.GONE);
		}
	}
	
	public void changeValue(int tmp)
	{
		if(tmp <= valueMax && tmp >= valueMin)
		{
			valueCur = tmp;
			checkButtonStyle();
			if(floatTimes == 1)
				valueText.setText(valueCur+"");
			else
				valueText.setText(valueCur*1.0/floatTimes+"");
		}	
	}
	
	public void setRange(int rangelimit1,int rangelimit2)
	{
		valueMax = rangelimit1 > rangelimit2 ? rangelimit1 : rangelimit2;
		valueMin = rangelimit1 < rangelimit2 ? rangelimit1 : rangelimit2;
		if(valueCur > valueMax)
			valueCur = valueMax;
		else if(valueCur < valueMin)
			valueCur = valueMin;
		changeValue(valueCur);	
	}
	
	public void setFloatShowTimes(int t)
	{
		if(t != 0)
			floatTimes = t;
	}
	
	public void setStep(int v)
	{
		if(v > 0)
		{
			step = v;
			checkButtonStyle();
		}
	}
	
	public void checkButtonStyle()
	{
		plusBtn.setEnabled(valueCur + step <= valueMax);			
		minusBtn.setEnabled(valueCur - step >= valueMin);
	}
	
	public void setTitle(String title)
	{
		titleText.setText(title);
	}
	
	public int getValue()
	{
		return valueCur;
	}
	
	public void setBtnChecked(boolean b)
	{
		switchBtn.setChecked(b);
	}
	
	public boolean isBtnChecked()
	{
		return switchBtn.isChecked();
	}

}
