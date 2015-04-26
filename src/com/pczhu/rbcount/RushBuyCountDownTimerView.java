package com.pczhu.rbcount;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RushBuyCountDownTimerView extends LinearLayout {
	// ״̬
	private boolean isRunning;
	// ��,ʮλ
	private TextView tv_day;
	// Сʱ��ʮλ
	// private TextView tv_hour_decade;
	// Сʱ����λ
	private TextView tv_hour_unit;
	// ���ӣ���λ
	private TextView tv_min_unit;
	// �룬��λ
	private TextView tv_sec_unit;

	private Context context;
	// �Ƿ��������ʱ true ����ʱ false ����ʱ
	private boolean isPositive = false;

	public boolean isPositive() {
		return isPositive;
	}

	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}

	private int day;
	// private int hour_decade;
	private int hour_unit;
	private int min_unit;
	private int sec_unit;
	// ��ʱ��
	private Timer timer;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			if(timelistener != null){
				int day_time  = Integer.valueOf(tv_day.getText().toString());
				int hour_unit_time = Integer.valueOf(tv_hour_unit.getText().toString());
				int min_unit_time = Integer.valueOf(tv_min_unit.getText().toString());
				int sec_unit_time = Integer.valueOf(tv_sec_unit.getText().toString());
				if(timelistener.onTimeAction(day_time, hour_unit_time, min_unit_time, sec_unit_time, isPositive)){//Ŀ��ʱ���������ִ��
					if (isPositive) {
						countUp();
					} else {
						if (!isTimeEnd()) {
							countDown();
						}
					}
				}else{//Ŀ�����������ִ��
					stop();//ֹͣ
					timelistener.onTargetTimeFinished(isPositive);//ִ��ʱ��ֹͣ
				}
			}else{
				if (isPositive) {
					countUp();
				} else {
					if (!isTimeEnd()) {
						countDown();
					}
				}
			}

		}

	};

	private boolean isTimeEnd() {

		// int hour_decade_time =
		// Integer.valueOf(tv_hour_decade.getText().toString());
		int day_time  = Integer.valueOf(tv_day.getText().toString());
		int hour_unit_time = Integer.valueOf(tv_hour_unit.getText().toString());
		int min_unit_time = Integer.valueOf(tv_min_unit.getText().toString());
		int sec_unit_time = Integer.valueOf(tv_sec_unit.getText().toString());
		if (day_time == 0 && hour_unit_time == 0 && min_unit_time == 0
				&& sec_unit_time == 0) {
			tv_sec_unit.setText("00");
			Toast.makeText(context, "ʱ�䵽��", Toast.LENGTH_SHORT).show();
			stop();
			if(timelistener != null)
				timelistener.onTimeFinished(isPositive);
			return true;
		}
		return false;
	};

	public RushBuyCountDownTimerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_counttimer, this);
		tv_day = (TextView) view.findViewById(R.id.tv_day_unit);
		// tv_hour_decade = (TextView) view.findViewById(R.id.tv_hour_decade);
		tv_hour_unit = (TextView) view.findViewById(R.id.tv_hour_unit);
		tv_min_unit = (TextView) view.findViewById(R.id.tv_min_unit);
		tv_sec_unit = (TextView) view.findViewById(R.id.tv_sec_unit);

	}

	/**
	 * 
	 * @Description: ��ʼ��ʱ
	 * @param
	 * @return void
	 * @throws
	 */
	public void start() {
		if(timelistener != null)
			timelistener.onTimeStart(isPositive);
		this.setRunning(true);
		if (timer == null) {
			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			}, 0, 1000);
		}
	}

	/**
	 * 
	 * @Description: ֹͣ��ʱ
	 * @param
	 * @return void
	 * @throws
	 */
	public void stop() {
		this.setRunning(false);
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * ����ʱ���
	 * 
	 * @param hour
	 * @param min
	 * @param sec
	 * @param isPositive
	 *            ����ʱ ����ʱ
	 */
	public void setTime(int day, int hour, int min, int sec, boolean isPositive) {
		this.isPositive = isPositive;
		if (day < 0 || hour >= 24 || min >= 60 || sec >= 60 || hour < 0
				|| min < 0 || sec < 0) {
			throw new RuntimeException(
					"Time format is error,please check out your code");
		}

		this.day = day;

		hour_unit = hour;

		min_unit = min;

		sec_unit = sec;
		// ��
		tv_day.setText(day + "");
		// ʱ
		if (hour_unit > -1 && hour_unit < 10) {
			tv_hour_unit.setText("0" + hour_unit);
		} else {
			tv_hour_unit.setText("" + hour_unit);
		}
		// ��
		if (min_unit > -1 && min_unit < 10) {
			tv_min_unit.setText("0" + min_unit);
		} else {
			tv_min_unit.setText("" + min_unit);
		}
		// ��
		if (sec_unit > -1 && sec_unit < 10) {
			tv_sec_unit.setText("0" + sec_unit);
		} else {
			tv_sec_unit.setText("" + sec_unit);
		}

	}

	public void setTime(long dtime, boolean isPositive) {
		// d/(3600 *24)+"��"+d%(3600 *24)/3600 +"Сʱ"+d%(3600
		// *24)%3600/60+"����"+d%(3600 *24)%3600%60+"��"
		long d = (dtime)/1000;
		if(d<0){
			d = 0-d;
		}
		int daytime = (int) (d / (3600 * 24));
		int hour = (int) (d % (3600 * 24) / 3600);
		int minite = (int) (d % (3600 * 24) % 3600 / 60);
		int second = (int) (d % (3600 * 24) % 3600 % 60);
		
		//System.out.println(d/(3600 *24)+"��"+d%(3600 *24)/3600 +"Сʱ"+d%(3600 *24)%3600/60+"����"+d%(3600 *24)%3600%60+"��");
		
		
		setTime(daytime, hour, minite, second, isPositive);

	}

	/**
	 * 
	 * @Description: ����ʱ
	 * @param
	 * @return boolean
	 * @throws
	 */
	private void countDown() {

		if (isCarry4Unit(tv_sec_unit)) {

			if (isCarry4Unit(tv_min_unit)) {

				if (isCarryHour(tv_hour_unit)) {

					isCarry4day(tv_day);
					// Toast.makeText(context, "ʱ�䵽��",
					// Toast.LENGTH_SHORT).show();
					// stop();

				}
			}
		}
	}

	/**
	 * 
	 * @Description: ����ʱ
	 * @param
	 * @return boolean
	 * @throws
	 */
	private void countUp() {

		if (isPosi4Unit(tv_sec_unit)) {

			if (isPosi4Unit(tv_min_unit)) {

				if (isPosiHour(tv_hour_unit)) {
					// if (isPosi4Decade(tv_hour_decade)) {
					// Toast.makeText(context, "ʱ�䵽��",
					// Toast.LENGTH_SHORT).show();
					// stop();
					isPosi4Day(tv_day);

					// }
				}
			}
		}
	}

	/**
	 * 
	 * @Description: �ݼ��仯���룬���ж��Ƿ���Ҫ��λ
	 * @param
	 * @return boolean
	 * @throws
	 */
	private boolean isCarry4Unit(TextView tv) {

		int time = Integer.valueOf(tv.getText().toString());
		time = time - 1;
		if (time < 0) {
			time = 59;
			tv.setText(time + "");
			return true;
		} else if (time > -1 && time < 10) {
			tv.setText("0" + time);
			return false;
		} else if (time > 9 && time < 60) {
			tv.setText(time + "");
			return false;
		} else {

			return false;
		}

	}

	/**
	 * @deprecated �ݼ��仯Сʱ�����ж��Ƿ���Ҫ��λ
	 * @param tv
	 * @return
	 * @throws
	 */
	public boolean isCarryHour(TextView tv) {
		int time = Integer.valueOf(tv.getText().toString());
		time = time - 1;
		if (time < 10 && time > -1) {
			tv.setText("0" + time + "");
			return false;
		} else if (time < 24 && time > 9) {
			tv.setText(time + "");
			return false;
		} else if (time < 0) {
			time = 23;
			tv.setText(time + "");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @deprecated �ݼ��仯�죬
	 * @param tv_day2
	 * @return
	 */
	private void isCarry4day(TextView tv) {
		int time = Integer.valueOf(tv.getText().toString());
		time = time - 1;
		if (time < 0) {
			time = 0;
			tv.setText(time + "");
		} else {
			tv.setText(time + "");
		}
	}

	/**
	 * @deprecated: �����仯���룬�ж��Ƿ���Ҫ��λ
	 * @param tv
	 * @return
	 */
	private boolean isPosi4Unit(TextView tv) {
		int time = Integer.valueOf(tv.getText().toString());
		time = time + 1;
		if (time > 59) {
			time = 0;
			tv.setText("0" + time);
			return true;
		} else {
			if (time > -1 && time < 10) {
				tv.setText("0" + time);
			} else if (time > 9 && time < 60) {
				tv.setText(time + "");
			}
			return false;
		}
	}

	/**
	 * @deprecated: �����仯Сʱ���ж��Ƿ���Ҫ��λ
	 * @param tv
	 * @return
	 */
	private boolean isPosiHour(TextView tv) {
		int time = Integer.valueOf(tv.getText().toString());
		time = time + 1;
		if (time > 23) {
			time = 0;
			tv.setText("0" + time);
			return true;
		} else {
			if (time < 10 && time > -1) {
				tv.setText("0" + time + "");
			} else if (time < 24 && time > 9) {
				tv.setText(time + "");
			}
			return false;
		}
	}

	/**
	 * @deprecated �����
	 * @param tv
	 * @return
	 */
	private void isPosi4Day(TextView tv) {
		int time = Integer.valueOf(tv.getText().toString());
		time = time + 1;
		tv.setText(time + "");
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	private OnTimeListener timelistener;

	public void setOnTimeListener(OnTimeListener timelistener) {
		this.timelistener = timelistener;
	}

	public interface OnTimeListener {
		/**
		 * ʱ�俪ʼǰ
		 * @param isPosi
		 */
		public void onTimeStart(boolean isPosi);
		/**
		 * ʱ��������
		 * @param isPosi
		 */

		public void onTimeFinished(boolean isPosi);
		/**
		 * ָ��ʱ������
		 * @param day
		 * @param hour
		 * @param min
		 * @param sec
		 * @param isPosi ����ʱ������ʱ
		 * @return 
		 * 			true �ɼ���ִ��
		 * 			false ���ü���ִ��
		 */
		public boolean onTimeAction(int day,int hour,int min,int sec,boolean isPosi);
		/**
		 * ָ��ʱ��������в���
		 * @param isPosi
		 */
		public void onTargetTimeFinished(boolean isPosi);
	}
}
