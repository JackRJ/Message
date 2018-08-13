package com.example.sendmessage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int FILE_SELECT_CODE = 0;
    private List<Person> result;
    private Button chooser, clear, send;
    private ToggleButton toggleButton1, toggleButton2;
    private ListView listView;
    private TextView chose;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        listView = (ListView)findViewById(R.id.list_view);
        chose = (TextView)findViewById(R.id.chose);
        chooser = (Button)findViewById(R.id.chooser);
        clear = (Button)findViewById(R.id.clear);
        send = (Button)findViewById(R.id.send);
        chooser.setOnClickListener(new ButtonListener());
        clear.setOnClickListener(new ButtonListener());
        send.setOnClickListener(new ButtonListener());
        toggleButton1 = (ToggleButton)findViewById(R.id.simcard1);
        toggleButton2 = (ToggleButton)findViewById(R.id.simcard2);
        toggleButton1.setText(new PhoneInfoUtils(this).getProvidersName());
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.chooser:
                    showFileChooser();
                    break;
                case R.id.clear:
                    result = null;
                    //listView.removeAllViews();
                    chose.setText("");
                    break;
                case R.id.send:
                    sendMessage();
                    break;
                default:
                    break;
            }
        }
    }

    private void sendMessage(){
        if(result == null){
            Toast.makeText(this, "Please install a File First.",  Toast.LENGTH_SHORT).show();
        } else if (!toggleButton1.isChecked() && !toggleButton2.isChecked()){
            Toast.makeText(this, "Please choose a simcard.",  Toast.LENGTH_SHORT).show();
        }else if (toggleButton1.isChecked() && toggleButton2.isChecked()){
            Toast.makeText(this, "You can choose one simcard only.",  Toast.LENGTH_SHORT).show();
        }
    }

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
                        adapter = new MessageAdapter(this, R.layout.list_view_item, result);
                        listView.setAdapter(adapter);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}