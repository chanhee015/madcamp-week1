package com.example.tabswithanimatedswipe;

import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tabswithanimatedswipe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TabFragment2 extends Fragment implements View.OnClickListener{

    View view;
    Bitmap bitmap;
    Button upload_button;
    Button download_button;
    private static final int MY_PERMISSION_CAMERA = 5555;

    GridView gridView;
    ImageAdapter imageAdapter;
    String path;
    ImageView imageView;

    public static final String API_BASE_URL = "http://192.249.19.243:8680/";


    private void downloadFile(){
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        DownloadService apiInterface = ServiceGenerator.createService(DownloadService.class);

        Call<ResponseBody> call = apiInterface.downloadFileWithDynamicUrl(API_BASE_URL);
        path = Environment.getExternalStorageDirectory().getPath() + "/Pictures/" + "Saved.png";

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), path);
                    if (writtenToDisk){
                        bitmap = BitmapFactory.decodeFile(path);
                    }
                    Log.d("File Download", String.valueOf(writtenToDisk));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String path) {
        try {
            // todo change the file location/name according to your needs
            File file = new File(path);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    //fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_object2, container, false);
        upload_button = (Button) view.findViewById(R.id.upload_btn);
        upload_button.setOnClickListener(this);
        download_button = (Button) view.findViewById(R.id.download_btn);
        download_button.setOnClickListener(this);
        imageView = view.findViewById(R.id.imageView);
//        gridView = view.findViewById(R.id.grid_view); // .xml의 GridView와 연결
//        imageAdapter = new ImageAdapter(getActivity()); //Adapter와 연결
//        gridView.setAdapter(imageAdapter); // GridView가 ImageAdapter에서 받은 값 뿌릴 수 있게 연결

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_btn:
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
                break;
            case R.id.download_btn:
                System.out.println("Hello????????????????????????????????????");
                downloadFile();
                imageView.setImageBitmap(bitmap);
                break;

        }

    }
}