package com.pczhu.rbcount;

import com.pczhu.rbcount.RushBuyCountDownTimerView.OnTimeListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final RushBuyCountDownTimerView rbc = (RushBuyCountDownTimerView) findViewById(R.id.tvb);
		rbc.setTime(1, 1, 1, 13, false);
		rbc.setOnTimeListener(new OnTimeListener() {
			
			@Override
			public void onTimeStart(boolean isPosi) {
				Toast.makeText(MainActivity.this, "start", 0).show();
			}
			
			@Override
			public void onTimeFinished(boolean isPosi) {
				if(!isPosi){
					Toast.makeText(MainActivity.this, "time", 0).show();
					rbc.setPositive(true);
					rbc.start();
				}
			}

			@Override
			public boolean onTimeAction(int day, int hour, int min, int sec,
					boolean isPosi) {
				if(day == 1 && hour == 1 && min == 1 && sec == 0 && isPosi == true){
					Toast.makeText(MainActivity.this, "1Сʱһ��һ��", 0).show();
					return false;
				}
				if(day == 1 && hour == 1 && min == 1 && sec == 0 && isPosi == false){
					Toast.makeText(MainActivity.this, "1Сʱһ��һ��", 0).show();
					return true;
				}
				return true;
			}

			@Override
			public void onTargetTimeFinished(boolean isPosi) {
				Toast.makeText(MainActivity.this, "ִ��ʱ���������", 0).show();
				// TODO Auto-generated method stub
				
			}
		});
		rbc.start();
		
	}
}

