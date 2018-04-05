package com.iot.mqtt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import static com.iot.mqtt.SettingActivity.getEquipment_ID;
import static com.iot.mqtt.SettingActivity.getEquipment_Topic;
import static com.iot.mqtt.SettingActivity.getProduct_APIKey;
import static com.iot.mqtt.SettingActivity.getProduct_ID;
import static com.iot.mqtt.SettingActivity.getServer_IP;
import static com.iot.mqtt.SettingActivity.getServer_Port;
import static com.iot.mqtt.SettingActivity.getServer_Qos;
import static com.iot.mqtt.SettingActivity.getServer_keepAlive;
import static com.iot.mqtt.SettingActivity.setDateTypeStr;
import static com.iot.mqtt.SettingActivity.setEquipment_APIKey;
import static com.iot.mqtt.SettingActivity.setEquipment_ID;
import static com.iot.mqtt.SettingActivity.setEquipment_Topic;
import static com.iot.mqtt.SettingActivity.setProduct_APIKey;
import static com.iot.mqtt.SettingActivity.setProduct_ID;
import static com.iot.mqtt.SettingActivity.setReConnet_bool;
import static com.iot.mqtt.SettingActivity.setServer_IP;
import static com.iot.mqtt.SettingActivity.setServer_Port;
import static com.iot.mqtt.SettingActivity.setServer_Qos;
import static com.iot.mqtt.SettingActivity.setServer_keepAlive;

public class MainActivity extends Activity {//AppCompatActivity
    private static final String TAG = "MainActivity";
    private static MqttManager mqttManger;
    private static Toast toast;
    private Context mContext;
    private ImageButton titleButton_Settting;
    public Button button_yes;
    private TextView mShowTextView;
    private Spinner mspinner1, mspinner2, mspinner3, mspinner4;
    String[] data1, data2, data3, data4;
    String str1_en, str2_en, str3_en, str4_en, str1_cn, str2_cn, str3_cn, str4_cn, printStr;
    private static boolean isRedTextView;//表示是否联网

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initSetting();
        initMqttManager();
    }

    public static boolean isRedTextView() {
        return isRedTextView;
    }

    public static void setIsRedTextView(boolean isRedTextView) {
        MainActivity.isRedTextView = isRedTextView;
    }

    public static MqttManager getMqttManger() {
        return mqttManger;
    }

    /***
     * 初始化MQTT客户端
     */
    public void initMqttManager() {
        if (!isSettingVolueNull()&&isInternetConnected(getApplication())) {
            setEvenEnum(EvenEnum.MQTT_SETTING_LOGIN_TRUE);
        }
    }

    private void initUI() {
        button_yes = (Button) findViewById(R.id.button_yes);
        titleButton_Settting = (ImageButton) findViewById(R.id.imageButton_config);
        toast = new Toast(this).makeText(this, "", Toast.LENGTH_SHORT);
        mShowTextView = (TextView) findViewById(R.id.showText);
        mShowTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        printlnUpTextView("IOT MQTT");
        /***
         * 楼层
         */
        data1 = new String[SpringData.floors.length];
        for (int a = 0; a < SpringData.floors.length; a++) {
            data1[a] = SpringData.floors[a][0];
        }
        mspinner1 = (Spinner) this.findViewById(R.id.spinner1);
        mspinner1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data1));
        mspinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str1_cn = data1[position];
                str1_en = SpringData.floors[position][1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /***
         * 房间
         */
        data2 = new String[SpringData.positions.length];
        for (int a = 0; a < SpringData.positions.length; a++) {
            data2[a] = SpringData.positions[a][0];
        }
        mspinner2 = (Spinner) this.findViewById(R.id.spinner2);
        mspinner2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data2));
        mspinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str2_cn = data2[position];
                str2_en = SpringData.positions[position][1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /***
         * 设备
         */
        data3 = new String[SpringData.domesticAppliance.length];
        for (int a = 0; a < SpringData.domesticAppliance.length; a++) {
            data3[a] = SpringData.domesticAppliance[a][0];
        }
        mspinner3 = (Spinner) this.findViewById(R.id.spinner3);
        mspinner3.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data3));
        mspinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str3_cn = data3[position];
                str3_en = SpringData.domesticAppliance[position][1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /***
         * 操作
         */
        data4 = new String[SpringData.manipulate.length];
        for (int a = 0; a < SpringData.manipulate.length; a++) {
            data4[a] = SpringData.manipulate[a][0];
        }
        mspinner4 = (Spinner) this.findViewById(R.id.spinner4);
        mspinner4.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data4));
        mspinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str4_cn = data4[position];
                str4_en = SpringData.manipulate[position][1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanPushData()) {
                    mqttManger.sendData_Json(str4_en, str3_en);
                    printlnUpTextView("操作：" + str4_cn + str1_cn + str2_cn + str3_cn);
                }
            }
        });

        titleButton_Settting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSetttingActivity();
            }
        });

    }

    /***
     * 一系列的判断语句
     * @return
     */
    public boolean isCanPushData() {
        if (isSettingVolueNull()) {//设置不能为空
            return false;
        } else if (!isInternetConnected(getApplication())) {
            return false;
        } else if (!mqttManger.getMqttAndroidClient().isConnected()) {
            setEvenEnum(EvenEnum.MQTT_CONNECT_FALSE);
            return false;
        } else {
            return true;
        }
    }

    /***
     * 判断网络是否打开与网络是否可用
     * @param context
     * @return
     */
    private boolean isInternetConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                } else {
                    setEvenEnum(EvenEnum.MQTT_INTERNET_FALSE);
                    return false;
                }
            } else {
                setEvenEnum(EvenEnum.MQTT_INTERNET_OPEN_FALSE);
                return false;
            }
        }
        return false;
    }

    /***
     * 打开设置setting activity 的方法
     */
    public void startSetttingActivity() {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
    }

    /***
     * 用户决定是否重连的弹窗
     */
    public void mqttReconnectDialog() {
            final Context context = this;
            runOnUiThread(//更新UI用的方法
                    new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder rconectDialog = new AlertDialog.Builder(context);
                            rconectDialog.setTitle("连接异常提示：");
                            rconectDialog.setMessage("连接MQTT服务器异常，是否重连？");
                            rconectDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setEvenEnum(EvenEnum.MQTT_RECLIENT_TRUE);
                                    mqttManger.reConnectServer();
                                }
                            });
                            rconectDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setEvenEnum(EvenEnum.MQTT_RECLIENT_FALSE);
                                }
                            });

                            rconectDialog.show();
                        }
                    }
            );
    }

    public void internetSettingDialog() {
        final Context context = this;
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder rconectDialog = new AlertDialog.Builder(context);
                        rconectDialog.setTitle("连接异常提示：");
                        rconectDialog.setMessage("网络连接异常，是否前往设置？");
                        rconectDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = null;
                                // 判断手机系统的版本 即API大于10 就是3.0或以上版本
                                if (android.os.Build.VERSION.SDK_INT > 10) {
                                    intent = new Intent(
                                            android.provider.Settings.ACTION_WIFI_SETTINGS);
                                } else {
                                    intent = new Intent();
                                    ComponentName component = new ComponentName(
                                            "com.android.settings",
                                            "com.android.settings.WirelessSettings");
                                    intent.setComponent(component);
                                    intent.setAction("android.intent.action.VIEW");
                                }
                                context.startActivity(intent);
                            }
                        });
                        rconectDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setEvenEnum(EvenEnum.MQTT_INTERNET_SETTING_FALSE);
                            }
                        });

                        rconectDialog.show();
                    }
                }
        );
    }


    public void printlnUpTextView(String str) {
        printStr = str;
        //这个方法可以在子线程中更新UI
        runOnUiThread(new Runnable() {
            public void run() {
                refreshAlarmView(mShowTextView, printStr + "\n");
            }
        });
        Log.e(TAG, str);
    }

    public static void printlnToast(String str) {
        toast.setText(str);
        toast.show();
    }

    /**
     * TestView 滚动刷新
     *
     * @param textView
     * @param msg
     */
    private void refreshAlarmView(TextView textView, String msg) {
        //http://blog.csdn.net/benbenxiongyuan/article/details/53454146
        textView.append(msg);
        if(isRedTextView()){
            textView.setTextColor(Color.RED);
            setIsRedTextView(false);
        }else{
            textView.setTextColor(Color.BLACK);
        }
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > (textView.getHeight() - textView.getLineHeight() - 2)) {
            textView.scrollTo(0, offset - textView.getHeight() + textView.getLineHeight() + 2);
        }
    }

    /***
     * 读取配置文件,//取出数据
     */
    public void initSetting() {
        //打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        SharedPreferences userSettings = getSharedPreferences("setting", 0);
        setServer_IP(userSettings.getString("Server_IP", "183.230.40.39"));//默认值
        setServer_Port(userSettings.getString("Server_Port", String.valueOf(6002)));//default
        setProduct_ID(userSettings.getString("Product_ID", "102504"));
        setProduct_APIKey(userSettings.getString("Product_Key", "gaLr=XxjhVGMf1=t4qazdJA6Nmk="));
        setEquipment_ID(userSettings.getString("Equipment_ID", "20438494"));
        setEquipment_APIKey(userSettings.getString("Equipment_Key", "androidemui"));
        setServer_Qos(userSettings.getString("Server_Qos", String.valueOf(1)));
        setServer_keepAlive(userSettings.getString("Server_keepAlive", String.valueOf(60)));
        setEquipment_Topic(userSettings.getString("Equipment_Topic", "DuerOS_LED"));
        setDateTypeStr(userSettings.getString("Data_Type", "Json"));
        setReConnet_bool(userSettings.getBoolean("ReConnet_bool", true));
    }

    /***
     * 判断设置信息是否非空,空则返回true
     * @return
     */
    public  boolean isSettingVolueNull() {
        if (!getServer_IP().equals("")
                && !getServer_Port().equals("")
                && !getProduct_ID().equals("")
                && !getProduct_APIKey().equals("")
                && !getEquipment_ID().equals("")
                && !getServer_Qos().equals("")
                && !getServer_keepAlive().equals("")
                && !getEquipment_Topic().equals("")) {//&& !getEquipment_APIKey().equals("") 设备key 暂未使用
            //setEvenEnum(EvenEnum.MQTT_SETTING_LOGIN_TRUE);
            return false;
        } else {
            setEvenEnum(EvenEnum.MQTT_SETTING_LOGIN_FLASE);
            return true;
        }
    }

    public void reConnectMQTT(){
        if (!isSettingVolueNull()&&isInternetConnected(getApplication())) {
            getMqttManger().reConnectServer();
        }
    }

    public void setEvenEnum(EvenEnum connectionState) {
        switch (connectionState) {
            case MQTT_SETTING_LOGIN_TRUE:
                printlnUpTextView("正在初始化MQTT客户端。");
                mqttManger = new MqttManager(getApplication(), this);
                break;
            case MQTT_INTERNET_TRUE:
                printlnUpTextView("网络连接成功。");
                break;
            case MQTT_INTERNET_FALSE:
                printlnUpTextView("网络连接失败！");
                setIsRedTextView(true);
                internetSettingDialog();
                break;
            case MQTT_CONNECT_TRUE:
                printlnUpTextView("MQTT服务器连接成功。");
                break;
            case MQTT_CONNECT_FALSE:
                printlnUpTextView("连接MQTT服务器失败或断开，请尝试重连！");
                setIsRedTextView(true);
                if (SettingActivity.isReConnet_bool()) {//判断是否自动重连
                    setEvenEnum(EvenEnum.MQTT_AUTO_CONNECT_TRUE);
                } else {
                    mqttReconnectDialog();
                }
                break;
            case MQTT_PUBLISH_TRUE:
                printlnUpTextView("正在发布消息。");
                break;
            case MQTT_PUBLISH_FALSE:
                printlnUpTextView("发布消息失败！");
                setIsRedTextView(true);
                break;
            case MQTT_CLIENT_RECEIVE_TRUE:
                printlnUpTextView("客户端收到订阅MQTT主题的消息。");
                break;
            case MQTT_EXCEPTION:
                printlnUpTextView("出现异常！！！");
                setIsRedTextView(true);
                break;
            case MQTT_SERVER_RECEIVE_TRUE:
                printlnUpTextView("服务器收到客户端发布的消息。");
                break;
            case MQTT_CLIENT_OPEN_TRUE:
                printlnUpTextView("客户端启动成功。");
                break;
            case MQTT_CLIENT_OPEN_FALSE:
                printlnUpTextView("客户端启动失败！");
                setIsRedTextView(true);
                break;
            case MQTT_SUBSCRIBE_TRUE:
                printlnUpTextView("订阅主题成功。");
                break;
            case MQTT_SUBSCRIBE_FALSE:
                printlnUpTextView("订阅主题异常！");
                break;
            case MQTT_RECLIENT_TRUE:
                printlnUpTextView("正在重新连接服务器。");
                break;
            case MQTT_RECLIENT_FALSE:
                printlnUpTextView("取消重新连接，服务器断开！");
                setIsRedTextView(true);
                break;
            case MQTT_AUTO_CONNECT_TRUE:
                printlnUpTextView("正在自动重连，请稍等。");
                reConnectMQTT();
                break;
            case MQTT_SETTING_LOGIN_FLASE:
                setIsRedTextView(true);
                printlnUpTextView("MQTT登录信息不能为空！现在前往设置。");
                printlnToast("MQTT登录信息不能为空！现在前往设置。");
                startSetttingActivity();
                break;
            case MQTT_INTERNET_SETTING_FALSE:
                printlnUpTextView("取消设置网络！");
                setIsRedTextView(true);
                break;
            case MQTT_INTERNET_OPEN_FALSE:
                printlnUpTextView("网络未开启！");
                setIsRedTextView(true);
                internetSettingDialog();
                break;
            case MQTT_CONNECT_MQTT_NOW:
                printlnUpTextView("正在连接MQTT服务器...");
                break;
            default:
                printlnUpTextView("产生未知事件。");
                setIsRedTextView(true);
                break;
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
