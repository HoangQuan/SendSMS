# SendSMS
1. Gửi sms từ ứng dụng của bạn
View:
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:orientation="vertical" >
    <EditText 
        android:id="@+id/txtMessage"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content" />
    <Button
        android:id="@+id/btnSend"
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:text="Gửi" />

</LinearLayout>

Add permission để sử dụng SEND_SMS
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.sendsms"
    android:versionCode="1"
    android:versionName="1.0" >

    <uses-sdk
        android:minSdkVersion="8"
        android:targetSdkVersion="21" />

    <application
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme" >
        <activity
            android:name=".MainActivity"
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
<uses-permission android:name="android.permission.SEND_SMS"></uses-permission>
</manifest>

Logic:

protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnSend = (Button) findViewById(R.id.btnSend);
		final EditText txtMes = (EditText) findViewById(R.id.txtMessage);
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendSMS("01656087075", txtMes.getText().toString());
				
			}
		});
	}
	private void sendSMS(String phoneNumber, String mes){
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, mes, null, null);
	}


Kết qủa:

  Trong Android chỉ cần sử dụng SmsManager để tạo và phương thức sendTextMessage để  gửi một tin nhắn văn bản.
  		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, mes, null, null);
trong đó có các tham số sau:


1. destinationAddress: Số điện thoại sẽ gửi đến.
2. scAddress: địa chỉ cả nhà cung cấp dịch vụ. mặc định sẽ là SMSC
3. text : nội dung tin nhắn
4. sentIntent : Intent sẽ được gọi khi message được gửi. 
5. deliveryIntent:Intent sẽ được gọi khi message đã gửi xong.

Ở trên tôi đã dùng các đối số mặc định để định nghĩa các PendingInten. Trên thực tế việc gửi Tin nhắn có thể gặp một số lỗi trong qúa trình gửi. để bắt đc các ngoại lệ sảy ra trong qúa trình gửi thì ta sử dụng class  có sẵn trong Android như sau:

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

Ứng với từng trường hợp chương trình sẽ show ra log tương ứng từ các PenddingIntent.
