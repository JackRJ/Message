package com.example.sendmessage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{
    private final static int FILE_SELECT_CODE = 0;
    private List<Person> result;
    private Button chooser, clear, send;
    private ToggleButton toggleButton1, toggleButton2;
    private ListView listView;
    private TextView chose;
    private TextView pattern;
    private MessageAdapter adapter;
    private List<SubscriptionInfo> list;
    static List<String> firstRow;
    private int IDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SubscriptionManager sManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        list = sManager.getActiveSubscriptionInfoList();
        init();
    }

    private void init(){
        firstRow = new ArrayList<>();
        listView = (ListView)findViewById(R.id.list_view);
        chose = (TextView)findViewById(R.id.chose);
        pattern = (TextView)findViewById(R.id.pattern);
        chooser = (Button)findViewById(R.id.chooser);
        clear = (Button)findViewById(R.id.clear);
        send = (Button)findViewById(R.id.send);
        chooser.setOnClickListener(new ButtonListener());
        clear.setOnClickListener(new ButtonListener());
        send.setOnClickListener(new ButtonListener());
        toggleButton1 = (ToggleButton)findViewById(R.id.simcard1);
        toggleButton2 = (ToggleButton)findViewById(R.id.simcard2);
        toggleButton1.setOnClickListener(new ButtonListener());
        toggleButton2.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.chooser:
                    showFileChooser();
                    break;
                case R.id.clear:
                    result = null;
                    listView.setAdapter(null);
                    chose.setText("");
                    pattern.setText("");
                    break;
                case R.id.send:
                    if(result == null){
                        Toast.makeText(getBaseContext(), "Please install a File First.",  Toast.LENGTH_SHORT).show();
                    } else if (!toggleButton1.isChecked() && !toggleButton2.isChecked()){
                        Toast.makeText(getBaseContext(), "Please choose a simcard.",  Toast.LENGTH_SHORT).show();
                    }else {
                        for (Person person : result){
                            String context = firstRow.get(3).replace(firstRow.get(1),person.getName());
                            for(int i = 4; i < firstRow.size(); i++){
                                context = context.replace(firstRow.get(i),person.replaceList.get(i-4));
                            }
                            sendMessage(IDs,person.getPhoneNumber(),context);
                        }
                        Toast.makeText(getApplicationContext(), "发送完毕", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.simcard1:
                    if (list.size() == 0){
                        Toast.makeText(getApplicationContext(), "You have no simcard.",  Toast.LENGTH_SHORT).show();
                        toggleButton1.setChecked(false);
                    }
                    else if (toggleButton2.isChecked()){
                        Toast.makeText(getApplicationContext(), "You can choose one simcard only.",  Toast.LENGTH_SHORT).show();
                        toggleButton1.setChecked(false);
                    }
                    break;
                case R.id.simcard2:
                    if(list.size() != 2){
                        Toast.makeText(getApplicationContext(), "You have no simcard2.",  Toast.LENGTH_SHORT).show();
                        toggleButton2.setChecked(false);
                    }
                    else if(toggleButton1.isChecked()){
                        Toast.makeText(getApplicationContext(),"You can choose one simcard only.",Toast.LENGTH_SHORT).show();
                        toggleButton2.setChecked(false);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void sendMessage(final int which,String phone,String context){
            SubscriptionInfo sInfo = null;
            if (list.size() == 2) {
                // 双卡
                sInfo = list.get(which);
            } else {
                // 单卡
                sInfo = list.get(0);
            }
            if (sInfo != null){
                int subId = sInfo.getSubscriptionId();
                SmsManager manager = SmsManager.getSmsManagerForSubscriptionId(subId);
                if (!TextUtils.isEmpty(phone)){
                    ArrayList<String> messageList =manager.divideMessage(context);
                    for(String text:messageList){
                        manager.sendTextMessage(phone, null, text, null, null);
                    }
                    Toast.makeText(this, "sending......", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "无法正确的获取SIM卡信息，请稍候重试", Toast.LENGTH_SHORT).show();
                }
            }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.simcard1:
                IDs = 0;
                break;
            case R.id.simcard2:
                IDs = 1;
                break;
        }
    }

    //导入Excel文件
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",  Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this, uri);
                    result = null;
                    if(!path.endsWith(".xls")){
                        Toast.makeText(this,"Please install a File of excel.",Toast.LENGTH_SHORT).show();
                    }else {
                        chose = (TextView) findViewById(R.id.chose);
                        chose.setText("已选择" + path);
                        result = ImportDataFromExcel.ImportExcelData(path);
                        if(result != null && result.size() > 0){
                            adapter = new MessageAdapter(this, R.layout.list_view_item, result);
                            listView.setAdapter(adapter);
                            pattern.setText(firstRow.get(3));
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Loading datas failed！", Toast.LENGTH_SHORT);
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}