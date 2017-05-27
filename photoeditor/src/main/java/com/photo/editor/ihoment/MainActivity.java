package com.photo.editor.ihoment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ly.img.android.sdk.decoder.ImageSource;
import ly.img.android.sdk.models.config.ImageStickerConfig;
import ly.img.android.sdk.models.config.StickerCategoryConfig;
import ly.img.android.sdk.models.config.interfaces.StickerListConfigInterface;
import ly.img.android.sdk.models.config.interfaces.ToolConfigInterface;
import ly.img.android.sdk.models.constant.Directory;
import ly.img.android.sdk.models.state.CameraSettings;
import ly.img.android.sdk.models.state.EditorLoadSettings;
import ly.img.android.sdk.models.state.EditorSaveSettings;
import ly.img.android.sdk.models.state.ImgLyConfig;
import ly.img.android.sdk.models.state.manager.SettingsList;
import ly.img.android.sdk.tools.FilterEditorTool;
import ly.img.android.ui.activities.CameraPreviewActivity;
import ly.img.android.ui.activities.CameraPreviewBuilder;
import ly.img.android.ui.activities.PhotoEditorBuilder;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    final static String FOLDER = "photoEditorSdkImgDir";
    final static int REQUEST_CODE_PHOTO_EDITOR = 1;
    //    static final String BASE_DIR = "/storage/emulated/0/baidu/flyflow/downloads";
    static final String BASE_DIR = "/storage/emulated/0";
    final static String TAG = MainActivity.class.getName();
    private ImageView afterImg;
    private ImageView beforeImg;
    private String myPicture;
    private ArrayList<String> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        afterImg = (ImageView) findViewById(R.id.edit_after_img);
        beforeImg = (ImageView) findViewById(R.id.edit_before_img);
        initSpanner();
    }

    private void initSpanner() {
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

    public void btnClick(View view) {
//        fromCameraToEditor();
        editorImgInStorage();
    }

    private void editorImgInStorage() {
        if (TextUtils.isEmpty(myPicture)) {
            Toast.makeText(this, "未选中编辑的图片", Toast.LENGTH_SHORT).show();
            return;
        }
        SettingsList settingsList = new SettingsList();
        /*自定义UI*/
        ImgLyConfig config = settingsList.getConfig();
        ArrayList<ToolConfigInterface> tools = config.getTools();
        for (ToolConfigInterface tool : tools) {
            String title = tool.getTitle();
            Log.w(TAG, "title = " + title);
        }
        /*自定义Sticker贴图*/
        customSticker(config);
        tools.add(0, new FilterEditorTool(R.string.my_filter, R.mipmap.ic_launcher_round));
        config.setTools(tools);

        settingsList
                .getSettingsModel(EditorLoadSettings.class)
                .setImageSourcePath(myPicture, true) // Load with delete protection true!

                .getSettingsModel(EditorSaveSettings.class)
                .setExportDir(Directory.DCIM, FOLDER)
                .setExportPrefix("result_")
                .setSavePolicy(
                        EditorSaveSettings.SavePolicy.KEEP_SOURCE_AND_CREATE_ALWAYS_OUTPUT
                );
        new PhotoEditorBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, REQUEST_CODE_PHOTO_EDITOR);
    }


    private void customSticker(ImgLyConfig config) {
        ArrayList<StickerListConfigInterface> stickerConfig = config.getStickerConfig();
        stickerConfig.add(new StickerCategoryConfig("newSticker",
                ImageSource.create(android.R.drawable.ic_secure),
                new ImageStickerConfig(R.string.my_filter,
                        R.mipmap.ic_launcher,
                        R.mipmap.ic_launcher_round
                )));
        config.setStickerLists(stickerConfig);
    }

    private void fromCameraToEditor() {
        SettingsList settingsList = new SettingsList();
        settingsList
                // Set custom camera export settings
                .getSettingsModel(CameraSettings.class)
                .setExportDir(Directory.DCIM, FOLDER)
                .setExportPrefix("camera_")
                // Set custom editor export settings
                .getSettingsModel(EditorSaveSettings.class)
                .setExportDir(Directory.DCIM, FOLDER)
                .setExportPrefix("result_")
                .setSavePolicy(
                        EditorSaveSettings.SavePolicy.RETURN_ALWAYS_ONLY_OUTPUT
                );
        new CameraPreviewBuilder(this)
                .setSettingsList(settingsList)
                .startActivityForResult(this, REQUEST_CODE_PHOTO_EDITOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PHOTO_EDITOR) {
            String resultPath = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
            String sourcePath = data.getStringExtra(CameraPreviewActivity.SOURCE_IMAGE_PATH);
            Log.w(TAG, "resultPath = " + resultPath);
            Log.w(TAG, "sourcePath = " + sourcePath);
            if (resultPath != null) {
                // Scan result file
                File file = new File(resultPath);
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                scanIntent.setData(contentUri);
                sendBroadcast(scanIntent);
            }

            if (sourcePath != null) {
                // Scan camera file
                File file = new File(sourcePath);
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(file);
                scanIntent.setData(contentUri);
                sendBroadcast(scanIntent);
            }

            Toast.makeText(this, "Image Save on: " + resultPath, Toast.LENGTH_LONG).show();
            afterImg.setImageURI(Uri.parse("file://" + resultPath));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        myPicture = BASE_DIR + "/" + data_list.get(position);
        String file = "file://" + myPicture;
        Log.w(TAG, "ImgFile = " + file);
        Uri uri = Uri.parse(file);
        beforeImg.setImageURI(uri);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (data_list == null || data_list.isEmpty()) return;
        myPicture = BASE_DIR + "/" + data_list.get(0);
        String file = "file://" + myPicture;
        Log.w(TAG, "ImgFile = " + file);
        Uri uri = Uri.parse(file);
        beforeImg.setImageURI(uri);
    }
}
