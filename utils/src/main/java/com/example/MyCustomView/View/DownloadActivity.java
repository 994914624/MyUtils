package com.example.MyCustomView.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.utils.R;

public class DownloadActivity extends AppCompatActivity {



    private DownloadView downloadView = null;
    private int progress = 0;
    private boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



      //  downloadView = (DownloadView) findViewById(R.id.downloadView);
        downloadView.setProgress(0);
        downloadView.setDownloadState(DownloadView.BEGIN);
        downloadView.setOnCircleButtonClickListener(new DownloadView.OnCircleButtonClickListener() {
            @Override
            public void onCircleButtonClick(View view) {
                if (downloadView.getDownloadState() == DownloadView.BEGIN||
                        downloadView.getDownloadState() == DownloadView.PAUSE) {
                    downloadView.setDownloadState(DownloadView.DOWNLOADING);
                    flag=true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0;flag; i++) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //更新进度
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        downloadView.setProgress(progress++);
                                        if(progress==100) {
                                            downloadView.setDownloadState(DownloadView.STOP);

                                        }
                                    }
                                });
                                if(progress==100) {
                                    break;

                                }
                            }
                        }
                    }).start();
                }else{
                    if(downloadView.getDownloadState() == DownloadView.DOWNLOADING){
                        flag=false;
                        downloadView.setDownloadState(DownloadView.PAUSE);
                        progress=downloadView.getProgress();
                    }else{
                        //DownloadView.STOP
                        Toast.makeText(DownloadActivity.this,"正在安装……",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });



    }
}
