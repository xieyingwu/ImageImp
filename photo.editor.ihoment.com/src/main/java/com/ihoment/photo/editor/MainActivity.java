package com.ihoment.photo.editor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.adobe.creativesdk.aviary.AdobeImageIntent;
import com.adobe.creativesdk.aviary.utils.AdobeImageEditorIntentConfigurationValidator;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    static final String TAG = MainActivity.class.getName();
    static final int REQUEST_CODE_FOR_EDIT = 1;
    //    static final String BASE_DIR = "/storage/emulated/0/baidu/flyflow/downloads";
    static final String BASE_DIR = "/storage/emulated/0";

    private ImageView editBeforeImg;
    private ImageView editAfterImg;
    private List<String> imgS;
    private List<String> imgFileS;
    private ArrayList<String> data_list;
    private Uri uri;
    private File output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*检测修改是否有效*/
        if (BuildConfig.DEBUG) {
            try {
                AdobeImageEditorIntentConfigurationValidator.validateConfiguration(this);
            } catch (Throwable e) {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(e.getMessage()).show();
            }
        }
        /*启动服务；提高加载图片到编辑页面到效率*/
        Intent cdsIntent = AdobeImageIntent.createCdsInitIntent(getBaseContext(), "CDS");
        startService(cdsIntent);
        editBeforeImg = (ImageView) findViewById(R.id.edit_before_img);
        editAfterImg = (ImageView) findViewById(R.id.edit_after_img);
//        findImgUrl();
        initSpinner();
    }

    private void initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.img_choose);
        data_list = initData();
        //适配器
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    private ArrayList<String> initData() {
        ArrayList<String> data = new ArrayList<>();
        File dir = new File(BASE_DIR);
        if (dir.exists() && dir.isDirectory()) {
            String[] list = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String nameIgnoreCase = name.toLowerCase();
                    return nameIgnoreCase.endsWith(".jpg") || nameIgnoreCase.endsWith(".jpeg") || nameIgnoreCase.endsWith(".png");
                }
            });
            List<String> asList = Arrays.asList(list);
            data.addAll(asList);
        }
        return data;
    }

    private void findImgUrl() {
        imgS = new ArrayList<>();
        imgFileS = new ArrayList<>();
        Cursor query = null;
        try {
            query = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (query == null) {
                Log.w(TAG, "query==null");
                return;
            }
            while (query.moveToNext()) {
                String name = query.getString(query.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                byte[] data = query.getBlob(query.getColumnIndex(MediaStore.Images.Media.DATA));
                String fileName = new String(data, 0, data.length - 1);
//                Log.w(TAG, "imgName = " + name);
                Log.w(TAG, "imgFileName = " + fileName);
                imgS.add(name);
                imgFileS.add(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (query != null)
                query.close();
        }
    }

    public void btnClick(View view) {
//        if (imgS.isEmpty() || imgFileS.isEmpty()) return;
//        String imgName = imgS.get(0);
//        Uri uri = Uri.parse("content://media/external/images/media/" + imgName);
//        String imgFileName = imgFileS.get(0);
//        String imgFileName = "/storage/emulated/0/DCIM/Camera/IMG_20170402_143853.jpg";
//        String imgFileName = "/storage/emulated/0/baidu/flyflow/downloads/1493807499407.jpg";
//        Uri uri = Uri.parse("file://" + imgFileName);
//        Log.w(TAG, "uri = " + uri);
//        editBeforeImg.setImageURI(uri);
        if (uri == null) {
            Toast.makeText(this, "未选中编辑的图片", Toast.LENGTH_SHORT).show();
            return;
        }
        startEditAC(uri);
    }

    private void startEditAC(Uri uri) {
        File outDir = new File(Environment.getExternalStorageDirectory(), "AdobeEditorImgDir");
        if ((outDir.exists() && outDir.isDirectory()) || outDir.mkdirs()) {
            output = new File(outDir, UUID.randomUUID().toString() + ".png");
            Intent build = new AdobeImageIntent.Builder(this)
                    .setData(uri)
                    .withOutputFormat(Bitmap.CompressFormat.PNG)
                    .withOutput(output)
//                    .withToolList(new ToolsFactory.Tools[]{SHARPNESS})
                    .build();
            startActivityForResult(build, REQUEST_CODE_FOR_EDIT);
        } else {
            Toast.makeText(this, "OutDir is not exists!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w(TAG, "requestCode = " + requestCode + " ;resultCode =" + resultCode);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FOR_EDIT) {
            Uri editedImageUri = data.getParcelableExtra(AdobeImageIntent.EXTRA_OUTPUT_URI);
            Log.w(TAG, "editedImageUri = " + editedImageUri);
//            editAfterImg.setImageURI(editedImageUri);
            editAfterImg.setImageURI(Uri.fromFile(output));
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String file = "file://" + BASE_DIR + "/" + data_list.get(position);
        Log.w(TAG, "ImgFile = " + file);
        uri = Uri.parse(file);
        editBeforeImg.setImageURI(uri);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        String file = "file://" + BASE_DIR + "/" + data_list.get(0);
        Log.w(TAG, "ImgFile = " + file);
        uri = Uri.parse(file);
        editBeforeImg.setImageURI(uri);
    }
}
