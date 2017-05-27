package com.ggtf.xieyingwu.cloudphoto;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.GetTemporaryLinkResult;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.ThumbnailFormat;
import com.dropbox.core.v2.files.ThumbnailSize;
import com.ggtf.xieyingwu.cloudphoto.dropbox.DbConstant;
import com.ggtf.xieyingwu.cloudphoto.instagram.IgRequest;
import com.ggtf.xieyingwu.cloudphoto.utils.FileUtil;
import com.ggtf.xieyingwu.cloudphoto.utils.HeaderUtil;
import com.ggtf.xieyingwu.cloudphoto.utils.SpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    static final String TAG = MainActivity.class.getName();
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    private IgRequest igRequest;
    private FileMetadata downloadFile;
    private ImageView img;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String path = msg.obj.toString();
            Log.w(TAG, "path = " + path);

            Glide.with(MainActivity.this)
                    .load(path)
                    .error(R.mipmap.ic_launcher)
                    .into(img);
        }
    };
    private HeaderUtil headerUtil;

    public static Uri buildDropBoxUri(FileMetadata file) {
        return new Uri.Builder()
                .scheme("dropbox")
                .authority("dropbox")
                .path(file.getPathLower()).build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.imgSrc);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        headerUtil = new HeaderUtil();
    }

    private void instagramTest() {
        SpUtil.initSp(this, "Instagram_SP");
        boolean instagram_cache = FileUtil.initCacheDir("Instagram_CACHE");
        Log.w(TAG, "instagram_cache = " + instagram_cache);
        igRequest = new IgRequest(this);
    }

    public void btnClick(View view) {
        switch (view.getId()) {
            case R.id.auth_instagram:
                instagramTest();
                igRequest.directToAuthorization();
                break;
            case R.id.auth_dropbox:
                SpUtil.initSp(this, "DropBox_SP");
                toAuthDropBox();
                break;
            case R.id.set_header_take:
                headerUtil.openCameraForHeader(this, new File(Environment.getExternalStorageDirectory(), "avatar.jpg"));
                break;
            case R.id.set_header_pick:
                headerUtil.pickPicForHeader(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        headerUtil.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessTokenForDropBox();
    }

    private void getAccessTokenForDropBox() {
        String oAuth2Token = Auth.getOAuth2Token();
        Log.w(TAG, "DropBox oAuth2Token = " + oAuth2Token);
        if (TextUtils.isEmpty(oAuth2Token)) return;
        SpUtil.saveStr(DbConstant.DB_KEY_ACCESS_TOKEN, oAuth2Token);
        startMakingApiCalls(oAuth2Token);
    }

    private void startMakingApiCalls(final String accessToken) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DbxRequestConfig requestConfig = DbxRequestConfig.newBuilder("CloudPhoto-v1.0").build();
                    DbxClientV2 dbxClientV2 = new DbxClientV2(requestConfig, accessToken);
                    DbxUserFilesRequests files = dbxClientV2.files();
                    Log.w(TAG, "列举出文件夹目录;传入path空字符串获取第一级目录；进入子目录时传递子目录文件夹路径");
                    ListFolderResult listFolderResult = files.listFolder("");
                    List<Metadata> entries = listFolderResult.getEntries();
                    Log.w(TAG, "entries.size = " + entries.size());
                    FileMetadata imgOrVideo = null;
                    for (Metadata entry : entries) {
                        if (entry instanceof FileMetadata) {
                            FileMetadata file = (FileMetadata) entry;
                            Log.w(TAG, "file.getPath() " + file.getPathDisplay());
                            downloadFile = file;
                            if (file.getPathDisplay().endsWith(".jpg") || file.getPathDisplay().endsWith(".mp4")) {
                                imgOrVideo = file;
                                break;
                            }
                        } else if (entry instanceof FolderMetadata) {
                            FolderMetadata folder = (FolderMetadata) entry;
                            Log.w(TAG, "folder.getPathLower() = " + folder.getPathLower());
                        }
                    }
                    if (downloadFile != null) {
//                        downloadFileFromDropBox(dbxClientV2, downloadFile);
                    }
                    if (imgOrVideo != null) {
                        downloadThumbnailImg(dbxClientV2, imgOrVideo);
                    }
                } catch (DbxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void downloadFileFromDropBox(DbxClientV2 dbxClientV2, FileMetadata fileMetadata) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), fileMetadata.getName());
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (!newFile) return;
            }
            DbxUserFilesRequests files = dbxClientV2.files();
            DbxDownloader<FileMetadata> download = files.download(fileMetadata.getPathLower(), fileMetadata.getRev());
            download.download(new FileOutputStream(file));
        } catch (DbxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void downloadThumbnailImg(DbxClientV2 dbxClientV2, FileMetadata fileMetadata) {
        try {
            Uri uri = buildDropBoxUri(fileMetadata);
            DbxUserFilesRequests files = dbxClientV2.files();
            long size = fileMetadata.getSize();
            Log.w(TAG, "size = " + size);
            GetTemporaryLinkResult temporaryLink = files.getTemporaryLink(uri.getPath());
            String link = temporaryLink.getLink();
            Log.w(TAG, "临时的下载地址；link = " + link);
            Log.w(TAG, "startTime = " + System.currentTimeMillis());
            File thumbnailImgFile = new File(Environment.getExternalStorageDirectory(), fileMetadata.getName() + ".jpg");
            DbxDownloader<FileMetadata> start = files.getThumbnailBuilder(uri.getPath())
                    .withFormat(ThumbnailFormat.PNG)
                    .withSize(ThumbnailSize.W128H128)
                    .start();
            if (!thumbnailImgFile.exists()) {
                boolean newFile = thumbnailImgFile.createNewFile();
                if (!newFile) return;
            }

            FileMetadata download = start.download(new FileOutputStream(thumbnailImgFile));
            Log.w(TAG, "endTime = " + System.currentTimeMillis());
            Message msg = myHandler.obtainMessage();
            msg.what = 0;
            msg.obj = thumbnailImgFile.getAbsolutePath();
            myHandler.sendMessage(msg);

        } catch (DbxException | IOException e) {
            e.printStackTrace();
        }

    }

    private void toAuthDropBox() {
        String accessToken = SpUtil.getStr(DbConstant.DB_KEY_ACCESS_TOKEN);
        if (TextUtils.isEmpty(accessToken))
            Auth.startOAuth2Authentication(this, DbConstant.APP_KEY);
        else
            startMakingApiCalls(accessToken);
    }
}
