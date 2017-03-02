package com.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.encoding.EncodingHandler;
import com.qrcodescan.R;
import com.utils.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.service.notification.Condition.SCHEME;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.openQrCodeScan)
//    Button openQrCodeScan;
//    @BindView(R.id.text)
//    EditText text;
//    @BindView(R.id.CreateQrCode)
//    Button CreateQrCode;
//    @BindView(R.id.QrCode)
//    ImageView QrCode;
//    @BindView(R.id.qrCodeText)
//    TextView qrCodeText;
    private static final String SCHEME = "package";
    //调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)

    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    /**
     * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
     */
    private static final String APP_PKG_NAME_22 = "pkg";
    /**
     * InstalledAppDetails所在包名
     */
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    /**
     * InstalledAppDetails类名
     */
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    //打开扫描界面请求码
    private static final int REQUEST_CODE = 1;
    //扫描成功返回码
    private static final int RESULT_OK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //打开二维码扫描界面
        if(CommonUtil.isCameraCanUse()){
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            // 设置跳转之后关闭主页面，再扫描页面点击返回键时候直接退出程序
            MainActivity.this.finish();
        }else{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("您的相关权限拍照和录像未打开，请尝试打开再重试。");
            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    showInstalledAppDetails(MainActivity.this, getPackageName());
                    dialog.dismiss();

                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    }

//    @OnClick({R.id.openQrCodeScan, R.id.CreateQrCode})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.openQrCodeScan:
//                //打开二维码扫描界面
//                if(CommonUtil.isCameraCanUse()){
//                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                    startActivityForResult(intent, REQUEST_CODE);
//                }else{
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("提示");
//                    builder.setMessage("您的相关权限拍照和录像未打开，请尝试打开再重试。");
//                    builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            showInstalledAppDetails(MainActivity.this, getPackageName());
//                            dialog.dismiss();
//
//                        }
//                    });
//                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.create().show();
//                }
//                break;
//            case R.id.CreateQrCode:
//                try {
//                    //获取输入的文本信息
//                    String str = text.getText().toString().trim();
//                    if(str != null && !"".equals(str.trim())){
//                        //根据输入的文本生成对应的二维码并且显示出来
//                        Bitmap mBitmap = EncodingHandler.createQRCode(text.getText().toString(), 500);
//                        if(mBitmap != null){
//                            Toast.makeText(this,"二维码生成成功！",Toast.LENGTH_SHORT).show();
//                            QrCode.setImageBitmap(mBitmap);
//                        }
//                    }else{
//                        Toast.makeText(this,"文本信息不能为空！",Toast.LENGTH_SHORT).show();
//                    }
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }

    private void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // 2.3（ApiLevel 9）以上，使用SDK提供的接口
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE:
            //扫描结果回调
            if (resultCode == RESULT_OK) { //RESULT_OK = -1
                Bundle bundle = data.getExtras();
                String scanResult = bundle.getString("qr_scan_result");
//            Log.d("AAA",scanResult+">>>>>>>>>>>>>>>>>>>" );
                Intent intent  = new Intent(Intent.ACTION_VIEW,Uri.parse(scanResult));
                startActivity(intent);
                //将扫描出的信息显示出来
//            qrCodeText.setText(scanResult);
            }
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scanner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
