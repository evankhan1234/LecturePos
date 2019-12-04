package xact.idea.lecturepos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;
import xact.idea.lecturepos.Utils.CorrectSizeUtil;

public class QRCodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    QREader qrEader;
    TextView code_info;
    ToggleButton toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        surfaceView=findViewById(R.id.surface_view);
        code_info=findViewById(R.id.code_info);
        toggle=findViewById(R.id.toggle);
        CorrectSizeUtil.getInstance(this).correctSize();
        CorrectSizeUtil.getInstance(this).correctSize(findViewById(R.id.rlt_root));
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                setUpCamera();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

                Toast.makeText(QRCodeActivity.this, "You must permission the camera", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }

    private void setUpCamera() {

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (qrEader.isCameraRunning()){
//                    qrEader.stop();
//                }
//                else {
//                    qrEader.start();
//                }
            }
        });


        qrEader=new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(final String data) {
                code_info.post(new Runnable() {
                    @Override
                    public void run() {
//                        Toast.makeText(QRCodeActivity.this, data, Toast.LENGTH_SHORT).show();
                        if (data!=null){
                            Intent intent = new Intent(QRCodeActivity.this, ItemActivity.class);
                            intent.putExtra("EXTRA_SESSION_ID", data);
                            startActivity(intent);
                            finish();

                        }
                    }
                });

            }
        }).facing(QREader.BACK_CAM).enableAutofocus(true).height(surfaceView.getHeight()).width(surfaceView.getWidth()).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                if (qrEader!=null)
                    qrEader.initAndStart(surfaceView);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

                Toast.makeText(QRCodeActivity.this, "You must permission the camera", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {

                if (qrEader!=null)
                    qrEader.releaseAndCleanup();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

                Toast.makeText(QRCodeActivity.this, "You must permission the camera", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

            }
        }).check();
    }
}
