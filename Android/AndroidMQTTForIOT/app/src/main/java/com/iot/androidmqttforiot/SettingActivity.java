package com.iot.androidmqttforiot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;


public class SettingActivity extends Activity {

    private TextView
            editText_Server_IP,
            editText_Server_Port,
            editText_Product_ID,
            editText_Product_Key,
            editText_Equipment_ID,
            editText_Equipment_Key,
            editText_Server_Qos,
            editText_Server_keepAlive,
            editText_Equipment_Topic;
    private Switch editText_ReConnet_bool_switch;
    private Spinner Data_Type_spinner;
    private ImageButton titleButton_Back_Main;
    private Button button_Back, button_Save, button_Yes, button_Def;
    private String[] dateType = {"Json", "Number", "String"};
    // 设置会话心跳时间 单位为秒 服务器会每隔1.5*keepAliveInterval秒的时间向客户端发送个消息判断客户端是否在线，但没有重连的机制
    private static String server_keepAlive; //设置会话心跳时间
    private static String server_Qos;//此标识指示发送消息的交付质量等级
    //QOS =0 最多发送一次 QOS =1 至少发送一次 QOS =2只发送一次
    private static String server_IP;// 服务器ip
    private static String server_Port;// 服务器端口
    private static String product_ID; // 产品ID
    private static String product_APIKey; // 产品APIKey
    private static String equipment_ID; // 设备id
    private static String equipment_APIKey;//设备授权码
    private static String equipment_Topic; //数据流模板：主题
    private static String dateTypeStr; //发送数据的数据类型
    private static boolean reConnet_bool;//是否自动重连

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initUI();
        ShowEditText();
    }

    public void initUI() {
        titleButton_Back_Main = (ImageButton) findViewById(R.id.imageButton_back);
        editText_Server_IP = (EditText) findViewById(R.id.editText_Server_IP);
        editText_Server_Port = (EditText) findViewById(R.id.editText_Server_Port);
        editText_Product_ID = (EditText) findViewById(R.id.editText_Product_ID);
        editText_Product_Key = (EditText) findViewById(R.id.editText_Product_Key);
        editText_Equipment_ID = (EditText) findViewById(R.id.editText_Equipment_ID);
        editText_Equipment_Key = (EditText) findViewById(R.id.editText_Equipment_Key);
        editText_Server_Qos = (EditText) findViewById(R.id.editText_Server_Qos);
        editText_Server_keepAlive = (EditText) findViewById(R.id.editText_Server_keepAlive);
        editText_Equipment_Topic = (EditText) findViewById(R.id.editText_Equipment_Topic);
        button_Back = (Button) findViewById(R.id.button_Back);
        button_Save = (Button) findViewById(R.id.button_Save);
        button_Yes = (Button) findViewById(R.id.button_Yes);
        button_Def = (Button) findViewById(R.id.button_def);
        editText_ReConnet_bool_switch = (Switch) findViewById(R.id.editText_ReConnet_bool_switch);
        Data_Type_spinner = (Spinner) findViewById(R.id.Data_Type_spinner);
        editText_ReConnet_bool_switch.setChecked(true);//默认是
        editText_ReConnet_bool_switch.setText("是");
        //匿名内部类
        editText_ReConnet_bool_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editText_ReConnet_bool_switch.setText("是");
                } else {
                    editText_ReConnet_bool_switch.setText("否");
                }
            }
        });

        Data_Type_spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dateType));
        Data_Type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 获取到TextView
                TextView tv = (TextView) view;
                // 修改样式属性
                tv.setTextColor(Color.BLACK);
                tv.setTextSize(20f);
                tv.setGravity(Gravity.CENTER);

                dateTypeStr = dateType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finshActivity();
            }
        });
        button_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeSetting();
                readSettingSetVolue();
            }
        });
        button_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeSetting();
                readSettingSetVolue();
                if (!isSettingVolueNull()) {
                    MainActivity.getMqttManger().reConnectServer();
                    finshActivity();
                } else {
                    MainActivity.printlnToast("连接服务器的登录信息不能为空！");
                }
            }
        });
        button_Def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //readSettingSetVolue();
                settingMyOneNETVolue();
                readSettingVolueSetTextView();
                //setEditTextNull();
            }
        });

        titleButton_Back_Main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finshActivity();
            }
        });

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

            return false;
        } else {
            return true;
        }
    }

    private void ShowEditText() {
        readSettingSetVolue();
        readSettingVolueSetTextView();
    }

    /***
     * 写入配置文件
     */
    private void writeSetting() {
        //打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        SharedPreferences userSettings = getSharedPreferences("setting", 0);
        //让setting处于编辑状态
        SharedPreferences.Editor editor = userSettings.edit();
        //存放数据
        editor.putString("Server_IP", editText_Server_IP.getText().toString());
        editor.putString("Server_Port", editText_Server_Port.getText().toString());
        editor.putString("Product_ID", editText_Product_ID.getText().toString());
        editor.putString("Product_Key", editText_Product_Key.getText().toString());
        editor.putString("Equipment_ID", editText_Equipment_ID.getText().toString());
        editor.putString("Equipment_Key", editText_Equipment_Key.getText().toString());
        editor.putString("Server_Qos", editText_Server_Qos.getText().toString());
        editor.putString("Server_keepAlive", editText_Server_keepAlive.getText().toString());
        editor.putString("Equipment_Topic", editText_Equipment_Topic.getText().toString());
        editor.putString("Data_Type", dateTypeStr);
        editor.putBoolean("ReConnet_bool", editText_ReConnet_bool_switch.isChecked());
        //完成提交
        editor.commit();
    }

    /***
     * 读取配置文件,取出数据,第一次则取默认值
     */
    public void readSettingSetVolue() {
        //打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        SharedPreferences userSettings = getSharedPreferences("setting", 0);
        setServer_IP(userSettings.getString("Server_IP", "183.230.40.39"));//默认值
        setServer_Port(userSettings.getString("Server_Port", String.valueOf(6002)));//default
        setProduct_ID(userSettings.getString("Product_ID", "102504"));
        setProduct_APIKey(userSettings.getString("Product_Key", "gaLr=XxjhVGMf1=t4qazdJA6Nmk="));
        setEquipment_ID(userSettings.getString("Equipment_ID", "20438494"));
        setEquipment_APIKey(userSettings.getString("Equipment_Key", "androidemui"));
        setServer_Qos(userSettings.getString("Server_Qos", String.valueOf(1)));
        setServer_keepAlive(userSettings.getString("Server_keepAlive", String.valueOf(120)));
        setEquipment_Topic(userSettings.getString("Equipment_Topic", "DuerOS_LED"));
        setDateTypeStr(userSettings.getString("Data_Type", dateType[0]));
        setReConnet_bool(userSettings.getBoolean("ReConnet_bool", true));
    }

    /***
     * 设置我的OneNET设备的信息
     */
    public void settingMyOneNETVolue() {
        setServer_IP("183.230.40.39");//默认值
        setServer_Port(String.valueOf(6002));//default
        setProduct_ID( "102504");
        setProduct_APIKey("gaLr=XxjhVGMf1=t4qazdJA6Nmk=");
        setEquipment_ID( "20438494");
        setEquipment_APIKey("androidemui");
        setServer_Qos(String.valueOf(1));
        setServer_keepAlive( String.valueOf(120));
        setEquipment_Topic("DuerOS_LED");
        setDateTypeStr( dateType[0]);
        setReConnet_bool(true);
    }

    //显示数据
    private void readSettingVolueSetTextView() {
        editText_Server_IP.setText(getServer_IP());
        editText_Server_Port.setText(getServer_Port());
        editText_Product_ID.setText(getProduct_ID());
        editText_Product_Key.setText(getProduct_APIKey());
        editText_Equipment_ID.setText(getEquipment_ID());
        editText_Equipment_Key.setText(getEquipment_APIKey());
        editText_Server_Qos.setText(getServer_Qos());
        editText_Server_keepAlive.setText(getServer_keepAlive());
        editText_Equipment_Topic.setText(getEquipment_Topic());
        setSpinnerItemSelectedByValue(Data_Type_spinner, getDateTypeStr());
        editText_ReConnet_bool_switch.setChecked(isReConnet_bool());
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param value
     */
    private static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    //清除指定数据
    private void removeSetting() {
        //打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        SharedPreferences userSettings = getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.remove("Equipment_Key");
        editor.commit();
    }

    //清空数据
    private void clearSetting() {
        //打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
        SharedPreferences userSettings = getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.clear();
        editor.commit();
    }

    private void setEditTextNull() {
        editText_Server_IP.setText("");
        editText_Server_Port.setText("");
        editText_Product_ID.setText("");
        editText_Product_Key.setText("");
        editText_Equipment_ID.setText("");
        editText_Equipment_Key.setText("");
        editText_Server_Qos.setText("");
        editText_Server_keepAlive.setText("");
        editText_Equipment_Topic.setText("");
        setSpinnerItemSelectedByValue(Data_Type_spinner, dateType[0]);
        editText_ReConnet_bool_switch.setChecked(true);
    }

    private void finshActivity() {
        Intent intent = new Intent();
        //intent.putExtra("result", json);
        //调用setResult方法表示我将Intent对象返回给之前的那个Activity，这样就可以在onActivityResult方法中得到Intent对象，
        setResult(1001, intent);
        //结束当前这个Activity对象的生命
        finish();
    }

    public TextView getEditText_Server_IP() {
        return editText_Server_IP;
    }

    public void setEditText_Server_IP(TextView editText_Server_IP) {
        this.editText_Server_IP = editText_Server_IP;
    }

    public static String getServer_keepAlive() {
        return server_keepAlive;
    }

    public static void setServer_keepAlive(String server_keepAlive) {
        SettingActivity.server_keepAlive = server_keepAlive;
    }

    public static String getServer_Qos() {
        return server_Qos;
    }

    public static void setServer_Qos(String server_Qos) {
        SettingActivity.server_Qos = server_Qos;
    }

    public static String getServer_IP() {
        return server_IP;
    }

    public static void setServer_IP(String server_IP) {
        SettingActivity.server_IP = server_IP;
    }

    public static String getServer_Port() {
        return server_Port;
    }

    public static void setServer_Port(String server_Port) {
        SettingActivity.server_Port = server_Port;
    }

    public static String getProduct_ID() {
        return product_ID;
    }

    public static void setProduct_ID(String product_ID) {
        SettingActivity.product_ID = product_ID;
    }

    public static String getProduct_APIKey() {
        return product_APIKey;
    }

    public static void setProduct_APIKey(String product_APIKey) {
        SettingActivity.product_APIKey = product_APIKey;
    }

    public static String getEquipment_ID() {
        return equipment_ID;
    }

    public static void setEquipment_ID(String equipment_ID) {
        SettingActivity.equipment_ID = equipment_ID;
    }

    public static String getEquipment_APIKey() {
        return equipment_APIKey;
    }

    public static void setEquipment_APIKey(String equipment_APIKey) {
        SettingActivity.equipment_APIKey = equipment_APIKey;
    }

    public static String getEquipment_Topic() {
        return equipment_Topic;
    }

    public static void setEquipment_Topic(String equipment_Topic) {
        SettingActivity.equipment_Topic = equipment_Topic;
    }

    public static String getDateTypeStr() {
        return dateTypeStr;
    }

    public static void setDateTypeStr(String dateTypeStr) {
        SettingActivity.dateTypeStr = dateTypeStr;
    }

    public static boolean isReConnet_bool() {
        return reConnet_bool;
    }

    public static void setReConnet_bool(boolean reConnet_bool) {
        SettingActivity.reConnet_bool = reConnet_bool;
    }
}