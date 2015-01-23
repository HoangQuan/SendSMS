package com.example.sendsms;

import android.support.v7.app.ActionBarActivity;
import android.telephony.SmsManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	IntentFilter intentFilter;
	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			TextView txtMessageContent = (TextView) findViewById(R.id.txtMessageContent);
			txtMessageContent.setText(intent.getExtras().getString("sms"));
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		intentFilter = new IntentFilter();
		intentFilter.addAction("SMS_RECEIVED_ACTION");
		Button btnSend = (Button) findViewById(R.id.btnSend);
		final EditText txtMes = (EditText) findViewById(R.id.txtMessage);
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendSMS("01656087075", txtMes.getText().toString());
				txtMes.setText("");
			}
		});
	}
	protected void onResum(){
		registerReceiver(intentReceiver, intentFilter);
		super.onResume();
	}
	
	protected void onPause(){
		unregisterReceiver(intentReceiver);
		super.onPause();
	}
	
	private void sendSMS(String phoneNumber, String mes){
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
		PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
		
//		SmsManager sms = SmsManager.getDefault();
//		sms.sendTextMessage(phoneNumber, null, mes, null, null); // default
		
		// When the SMS has been sent
		registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()){
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "SMS sented", Toast.LENGTH_LONG).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Toast.makeText(getBaseContext(), "Generic failue!", Toast.LENGTH_LONG).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Toast.makeText(getBaseContext(), "No Service!", Toast.LENGTH_LONG).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Toast.makeText(getBaseContext(), "NULL PDU!", Toast.LENGTH_LONG).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Toast.makeText(getBaseContext(), "Radio OFF!", Toast.LENGTH_LONG).show();
						break;
				}
				
			}
		}, new Intent(SENT));
		
		// when delivered
		registerReceiver(new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_LONG).show();
					case Activity.RESULT_CANCELED:
						Toast.makeText(getBaseContext(), "Canceled!", Toast.LENGTH_LONG).show();
				}
			}
		}, new Intent(DELIVERED));
		
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, mes, sentPI, deliverPI);
	}
	
	private void registerReceiver(BroadcastReceiver broadcastReceiver,
			Intent intent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
