package com.synway.mypicture;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

import adapter.MyListAdapter;

public class MainActivity extends Activity implements View.OnClickListener {
    private EditText editText;
    private long time = 0;
    private ListView listView;
    private MyListAdapter listAdapter;
    private View emptyView;
    //起始目录“/”
    private String mRootPath = Environment.getExternalStorageDirectory().toString();
    private TextView textView;
    private ArrayList<MyBean> list = new ArrayList<>();
    public static final int UP_DATE = 100;
    public static final int EMPTY = 101;
    public String currentPath = mRootPath;
    public SearchFiles searchFiles;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            switch (msg.what) {
                case UP_DATE:
                    ArrayList<MyBean> arrayList = (ArrayList<MyBean>) msg.obj;
                    list.clear();
                    list.addAll(arrayList);
                    listAdapter.setList(list);
                    break;
                case EMPTY:
                    list.clear();
                    listView.setEmptyView(emptyView);
                    listAdapter.setList(null);
                    break;
            }
            listView.setSelection(0);

            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断应用程序权限够不够
        boolean result = PermissionCheck.checkPermission(this);
        if (!result) {
            Intent intent = new Intent();
            intent.setClass(this, ActPermessionRequest.class);
            startActivity(intent);
            finish();
            return;
        }


        setContentView(R.layout.activity_main);
        editText = bind(R.id.selct);
        listView = bind(R.id.list);
        TextView button = bind(R.id.btn);
        emptyView = bind(R.id.empty_view);
        ImageView upView = bind(R.id.path_pane_up_level);
        textView = bind(R.id.current_path_view);
        searchFiles = new SearchFiles(handler);
        searchFiles.start(mRootPath);
        listAdapter = new MyListAdapter(this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setOnItemLongClickListener(onItemLongClickListener);
        button.setOnClickListener(this);
        upView.setOnClickListener(this);
    }

    private <T extends View> T bind(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onBackPressed() {
        if (currentPath.equals(mRootPath)) {

            currentPath = mRootPath;
            if ((System.currentTimeMillis() - time) > 1500) {
                Toast.makeText(this, "再按一次退出...", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
        } else {
            searchFiles.start(getParentFilePath());

        }

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MyBean myBean = list.get(position);
            if (myBean.type == 0) {
                currentPath = myBean.absloutePath;
                searchFiles.start(myBean.absloutePath);
                setEditText(currentPath);
            }
        }
    };
    private AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final String path = list.get(position).absloutePath;
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("注意").setMessage("删除文件或文件夹").
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAll(new File(path));
                            searchFiles.start(currentPath);
                        }
                    }).show();
            return true;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.path_pane_up_level:
                if (!currentPath.equals(mRootPath)) {
                    setEditText(currentPath);
                    searchFiles.start(getParentFilePath());
                }
                break;
            case R.id.btn:
                String st = editText.getText().toString();
                if (TextUtils.isEmpty(st)) {
                    st = "";
                } else {
                    st.replace(mRootPath, "");
                }
                st = st.replaceAll(" +", "");
                Intent intent = new Intent();
                intent.putExtra(PhotoPickeAct.PHOTO_DIC, st);
                intent.setClass(MainActivity.this, PhotoPickeAct.class);
                startActivity(intent);
                break;

        }
    }

    private void setEditText(String str) {
        if (str.equals(mRootPath)) {
            editText.setText("");
            textView.setText("");
        } else {
            editText.setText(str.replace(mRootPath + "/", ""));
            textView.setText(str.replace(mRootPath + "/", ""));
        }
        editText.setSelection(editText.getText().length());
    }

    private String getParentFilePath() {
        String parentPath = new File(currentPath).getParentFile().getAbsolutePath();
        setEditText(parentPath);
        currentPath = parentPath;
        return parentPath;
    }

    //递归删除指定路径下的所有文件
    public static void deleteAll(File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteAll(f);//递归删除每一个文件
            }
            file.delete();
        }

    }
}
