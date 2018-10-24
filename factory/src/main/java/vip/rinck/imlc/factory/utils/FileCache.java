package vip.rinck.imlc.factory.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import vip.rinck.imlc.common.app.Application;
import vip.rinck.imlc.factory.net.Network;
import vip.rinck.imlc.utils.HashUtil;
import vip.rinck.imlc.utils.StreamUtil;

/**
 * 一个简单的文件缓存，实现文件的下载操作
 */
public class FileCache<Holder> {

    private File baseDir;
    private String ext;
    private CacheListener listener;
    private SoftReference<Holder> holderSoftReference;

    public FileCache(String baseDir,String ext,CacheListener<Holder> listener){
        this.baseDir = new File(Application.getCacheDirFile(),baseDir);
        this.ext = ext;
        this.listener = listener;
    }

    //构建一个缓存文件，同一个网络路径对应一个本地的文件
    private File buildCacheFile(String path){
        String key = HashUtil.getMD5String(path);
        return new File(baseDir,key+"."+ext);
    }

    public void download(Holder holder,String path){
        //如果路径就是本地缓存路径
        if(path.startsWith(Application.getCacheDirFile().getAbsolutePath())){
            listener.onDownloadSucceed(holder,new File(path));
            return;
        }

        //构建缓存文件
        final File cacheFile = buildCacheFile(path);
        if(cacheFile.exists()&&cacheFile.length()>0){
            //如果文件存在，无需重新下载
            listener.onDownloadSucceed(holder,cacheFile);
            return;
        }
        holderSoftReference = new SoftReference<>(holder);
        OkHttpClient client = Network.getClient();
        Request request = new Request.Builder()
                .url(path)
                .get()
                .build();
        //发起请求
        Call call = client.newCall(request);
        call.enqueue(new NetCallback(holder,cacheFile));
    }

    //拿最后的目标，只能使用一次
    private Holder getLastHolderAndClear(){
        if(holderSoftReference==null)
            return null;
        else {
            //拿并清理
            Holder holder = holderSoftReference.get();
            holderSoftReference.clear();
            return holder;
        }
    }

    //下载的回调
    private class NetCallback implements Callback{
        private final SoftReference<Holder> holderSoftReference;
        private final File file;

        public NetCallback(Holder holder, File file) {
            this.holderSoftReference = new SoftReference<Holder>(holder);
            this.file = file;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Holder holder = holderSoftReference.get();
            if(holder!=null&&holder==getLastHolderAndClear()){
                FileCache.this.listener.onDownloadFailed(holder);
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            InputStream inputStream = response.body().byteStream();
            //文件的保存就是下载操作
            if(inputStream!=null&& StreamUtil.copy(inputStream,file)){
                Holder holder = holderSoftReference.get();
                if(holder!=null&&holder==getLastHolderAndClear()){
                    FileCache.this.listener.onDownloadFailed(holder);
                }
            }else {
                onFailure(call,null);
            }
        }
    }

    //相关的监听
    public interface CacheListener<Holder>{
        //成功同时把目标返回
        void onDownloadSucceed(Holder holder,File file);
        void onDownloadFailed(Holder holder);
    }
}
