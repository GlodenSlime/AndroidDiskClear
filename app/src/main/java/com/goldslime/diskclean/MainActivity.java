package com.goldslime.diskclean;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.goldslime.diskclean.utils.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    TextView tv_dev_info, tv_progress_info;
    AppCompatButton btn_startClear, btn_stopClear;

    boolean isRunning = false;
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    String deviceInfo;

    long remainSize = 0;
    long totalRemainSize = 0;
    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_dev_info = findViewById(R.id.tv_dev_info);
        tv_progress_info = findViewById(R.id.tv_progress_info);
        btn_startClear = findViewById(R.id.btn_startClear);
        btn_stopClear = findViewById(R.id.btn_stopClear);

        btn_startClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!canReadWrite()) {
                    showTip();
                    return;
                }

                if (isRunning) {
                    Toast.makeText(MainActivity.this, "请不要重复执行", Toast.LENGTH_SHORT).show();
                    return;
                }

                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    File externalDir = Environment.getExternalStorageDirectory();
                    StatFs stat = new StatFs(externalDir.getPath());
                    long blockSize = stat.getBlockSizeLong();
                    long totalBlocks = stat.getBlockCountLong();
                    long availableBlocks = stat.getAvailableBlocksLong();

                    long totalSize = blockSize * totalBlocks;
                    long availableSize = blockSize * availableBlocks;

                    remainSize = availableSize;
                    totalRemainSize = remainSize;

                    isRunning = true;
                    new Thread(() -> {

                        File file = new File(getFilesDir(), "random.bin");
                        while (isRunning) {
                            filePath = file.getAbsolutePath();
                            if (file.exists()) {
                                file.delete();
                            }
                            byte[] bytes = new byte[4096];
                            FileOutputStream outputStream = null;
                            SecureRandom secureRandom = new SecureRandom();
                            try {
                                outputStream = new FileOutputStream(file);

                                while (isRunning && remainSize > 0) {
                                    secureRandom.nextBytes(bytes);
                                    outputStream.write(bytes);
                                    remainSize -= 4096;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (outputStream != null) {
                                    try {
                                        outputStream.close();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                runOnUiThread(() -> {
                                    DialogUtils.ShowSuccess(MainActivity.this, "清理完成!");
                                });

                                isRunning = false;
                            }
                        }

                        file = new File(getFilesDir(), "random.bin");
                        if (file.exists()) {
                            file.delete();
                        }

                    }).start();

                    //实时更新进度信息
                    new Thread(() -> {
                        while (isRunning) {
                            SystemClock.sleep(100);
                            //能够读写了
                            runOnUiThread(() -> {
                                if (totalRemainSize > 0) {
                                    float progress = 100 * (1 - 1f * remainSize / totalRemainSize);
                                    tv_progress_info.setText("清理进度: " + decimalFormat.format(progress) + "%\n临时文件位置:\n " + filePath);
                                }
                            });
                        }

                        runOnUiThread(() -> {
                            String info = "已完成清理 ";
                            DateTimeFormatter formatter = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                                LocalDateTime currentDateTime = LocalDateTime.now();
                                info += "于" + formatter.format(LocalDateTime.now());
                            }
                            tv_progress_info.setText(info);
                        });
                    }).start();

                    Toast.makeText(MainActivity.this, "开始清理", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "未挂载", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_stopClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!canReadWrite()) {
                    showTip();
                    return;
                }

                isRunning = false;

                Toast.makeText(MainActivity.this, "停止清理", Toast.LENGTH_SHORT).show();
            }
        });
        //实时更新存储空间
        new Thread(() -> {
            while (true) {
                SystemClock.sleep(1000);
                //能够读写了
                if (canReadWrite()) {
                    String state = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        File externalDir = Environment.getExternalStorageDirectory();
                        StatFs stat = new StatFs(externalDir.getPath());
                        long blockSize = stat.getBlockSizeLong();
                        long totalBlocks = stat.getBlockCountLong();
                        long availableBlocks = stat.getAvailableBlocksLong();

                        long totalSize = blockSize * totalBlocks;
                        long availableSize = blockSize * availableBlocks;

                        float availableGBExternal = availableSize / 1024f / 1024f / 1024f;
                        deviceInfo = "剩余空间: " + decimalFormat.format(availableGBExternal) + " GB";
                    }

                    runOnUiThread(() -> {
                        tv_dev_info.setText(deviceInfo);
                    });
                }
            }

        }).start();

        if (!canReadWrite()) {
            showTip();
        }
    }

    void showTip() {
        DialogUtils.ShowTipDialog(MainActivity.this, "请授予我存储权限", "没有权限无法开展工作呢~", new DialogUtils.IOnClick() {
            @Override
            public void onOkClick() {
                requestSDPermission();
            }

            @Override
            public void onCancelClick() {
                finish();
            }
        });
    }

    public boolean canReadWrite() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
        }

        return isGranted;
    }

    void requestSDPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                            .ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    102);
        }
    }

}