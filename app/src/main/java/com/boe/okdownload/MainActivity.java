package com.boe.okdownload;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.boe.okdownload.constant.Constant;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_startDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startDownload = findViewById(R.id.btn_startDownload);
        btn_startDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DownLoadFileUtils.downloadFile(this,"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572516579259&di=30558903856abb8b5cf5bc1ff348bf3d&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F0338cbe93580d5e6b0e89f25531541d455f66fda4a6a5-eVWQaf_fw658","1");
    }
    //停止所有下载任务的方法
    // OkDownload.getInstance().removeAll();

    public static class DownLoadFileUtils {
        private static DownloadTask task;

        public static void removeAllDownload() {

        }

        public static void downloadFile(Context context, String fileUrl,String type) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("onlyonetag", Context.MODE_PRIVATE);
            int onetag = sharedPreferences.getInt("onetag", 0);
            onetag++;
            //步骤1：创建一个SharedPreferences对象
            SharedPreferences sharedPreferences1 = context.getSharedPreferences("onlyonetag", Context.MODE_PRIVATE);
            //步骤2： 实例化SharedPreferences.Editor对象
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            //步骤3：将获取过来的值放入文件
            editor.putInt("onetag", onetag);
            //步骤4：提交
            editor.commit();
            Log.i("onetag", onetag + "");
            GetRequest<File> request = OkGo.<File>get(fileUrl); //构建下载请求
            //创建下载任务，tag为一个任务的唯一标示
            task = OkDownload.request(String.valueOf(onetag), request);
            task.register(new DownloadListener(String.valueOf(onetag)) {
                @Override
                public void onStart(Progress progress) {
                    //开始下载
                    Log.i("aaaaaaaaa", "开始下载文件");
                }

                @Override
                public void onProgress(Progress progress) {
                    Log.i("aaaaaaaaa", "下载文件进度" + progress);
                }

                @Override
                public void onError(Progress progress) {
                    Log.i("aaaaaaaaa", "下载文件出错" );
                }

                @Override
                public void onFinish(File file, Progress progress) {
                    Log.i("aaaaaaaaa", "下载文件完成" );
                }

                @Override
                public void onRemove(Progress progress) {

                }
            }).save();
            //创建文件夹并开始下载
            if (type.equals("1")) {
                task.fileName("文件名");
            } else if (type.equals("2")) {
                //创建文件夹
                task.folder(Constant.FILEPATH + "video" );
                //下载的文件名字
                task.fileName("文件名1");
            } else if (type.equals("3")) {
                task.folder(Constant.FILEPATH + "photoset");
                task.fileName("文件名2");
            }
            //设置下载的文件名
            task.fileName("哈哈哈"); //设置下载的文件名
            task.start(); //开始或继续下载
//            task.restart(); //重新下载
//            task.pause(); //暂停下载
//            task.remove(); //删除下载，只删除记录，不删除文件
//            task.remove(true); //删除下载，同时删除记录和文件
        }

        //截取一个文件加载显示时传入的一个本地完整路径
        public static String subFileFullName(String fileName, String fileUrl) {
            String cutName = "";
            if (!fileUrl.equals("")) {
                cutName = fileName + fileUrl.substring(fileUrl.lastIndexOf("."), fileUrl.length());  //这里获取的是  恰似寒光遇骄阳.txt
            }
            return cutName;
        }
    }
}
