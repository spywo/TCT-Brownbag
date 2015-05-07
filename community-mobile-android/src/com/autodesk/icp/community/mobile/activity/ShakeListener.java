package com.autodesk.icp.community.mobile.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * 一个检测手机摇晃的监�?�器
 */
public class ShakeListener implements SensorEventListener {
	// 速度阈值，当摇晃速度达到这值�?�产生作用
	private static final int SPEED_SHRESHOLD = 3000;
	// 两次检测的时间间隔
	private static final int UPTATE_INTERVAL_TIME = 70;
	// 传感器管�?�器
	private SensorManager sensorManager;
	// 传感器
	private Sensor sensor;
	// �?力感应监�?�器
	private OnShakeListener onShakeListener;
	// 上下文
	private Context mContext;
	// 手机上一个�?置时�?力感应�??标
	private float lastX;
	private float lastY;
	private float lastZ;
	// 上次检测时间
	private long lastUpdateTime;

	// 构造器
	public ShakeListener(Context c) {
		// 获得监�?�对象
		mContext = c;
		start();
	}

	// 开始
	public void start() {
		// 获得传感器管�?�器
		sensorManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager != null) {
			// 获得�?力传感器
			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}
		// 注册
		if (sensor != null) {
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_GAME);
		}

	}

	// �?�止检测
	public void stop() {
		sensorManager.unregisterListener(this);
	}

	// 设置�?力感应监�?�器
	public void setOnShakeListener(OnShakeListener listener) {
		onShakeListener = listener;
	}

	// �?力感应器感应获得�?�化数�?�
	public void onSensorChanged(SensorEvent event) {
		// 现在检测时间
		long currentUpdateTime = System.currentTimeMillis();
		// 两次检测的时间间隔
		long timeInterval = currentUpdateTime - lastUpdateTime;
		// 判断是�?�达到了检测时间间隔
		if (timeInterval < UPTATE_INTERVAL_TIME)
			return;
		// 现在的时间�?��?last时间
		lastUpdateTime = currentUpdateTime;

		// 获得x,y,z�??标
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		// 获得x,y,z的�?�化值
		float deltaX = x - lastX;
		float deltaY = y - lastY;
		float deltaZ = z - lastZ;

		// 将现在的�??标�?��?last�??标
		lastX = x;
		lastY = y;
		lastZ = z;

		double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ
				* deltaZ)
				/ timeInterval * 10000;
		Log.v("thelog", "===========log===================");
		// 达到速度阀值，�?�出�??示
		if (speed >= SPEED_SHRESHOLD) {
			onShakeListener.onShake();
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	// 摇晃监�?�接�?�
	public interface OnShakeListener {
		public void onShake();
	}

}