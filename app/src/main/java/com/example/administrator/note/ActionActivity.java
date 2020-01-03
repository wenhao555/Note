package com.example.administrator.note;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActionActivity extends AppCompatActivity implements View.OnClickListener
{
    private int action;
    private Button btnAdd, btnBack;
    private EditText editText;
    private NotePadDB notePadDB;
    private SQLiteDatabase database;
    private ImageView image;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        action = getIntent().getIntExtra("action", 1);
        notePadDB = new NotePadDB(this);
        database = notePadDB.getWritableDatabase();
        context = this;
        initView();
        doAction();
    }

    private void initView()
    {
        editText = findViewById(R.id.edit_text);
        btnAdd = findViewById(R.id.btn_add);
        btnBack = findViewById(R.id.btn_back);
        image = findViewById(R.id.image);
        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        image.setOnClickListener(this);
    }

    private void doAction()
    {
        switch (action)
        {
            case 1:

                break;
            default:
                return;
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_add:
                insertDB();
                finish();
                break;
            case R.id.btn_back:
                finish();
                break;
            case R.id.image:
                Intent intent1 = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // 启动intent打开本地图库
                startActivityForResult(intent1, 10);
                break;
        }
    }

    private String imgPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 10:
                if (resultCode == RESULT_OK)
                {
                    Uri uri = data.getData();
                    imgPath = getRealFilePath(uri);
                    Log.e("图片", imgPath);
                    //通过uri的方式返回，部分手机uri可能为空
                    if (uri != null)
                    {
                        try
                        {
                            //通过uri获取到bitmap对象
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            image.setImageBitmap(bitmap);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                    } else
                    {
                        //部分手机可能直接存放在bundle中
                        Bundle bundleExtras = data.getExtras();
                        Log.e("图片", imgPath);
                        if (bundleExtras != null)
                        {
                            Bitmap bitmaps = bundleExtras.getParcelable("data");
                            image.setImageBitmap(bitmaps);
                        }
                    }

                }
                break;
        }
    }

    private String getRealFilePath(final Uri uri)
    {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme))
        {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor)
            {
                if (cursor.moveToFirst())
                {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1)
                    {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    private void insertDB()
    {
        ContentValues value = new ContentValues();
        value.put(NotePadDB.CONTENT, editText.getText().toString());
        value.put(NotePadDB.TIME, formatTime());
        value.put(NotePadDB.IMG, imgPath);
        Log.e("图片保存", imgPath);
        database.insert(NotePadDB.NAME, null, value);
    }

    private String formatTime()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

}


