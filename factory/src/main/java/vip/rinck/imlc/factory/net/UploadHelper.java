package vip.rinck.imlc.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;


import java.io.File;
import java.util.Date;

import vip.rinck.imlc.factory.Factory;
import vip.rinck.imlc.utils.HashUtil;

/**
 * 上传工具类，用于上传所有文件到阿里云OSS
 */
public class UploadHelper {

    private static final  String ENDPOINT = "http://oss.imlc.rinck.vip";
    //上传的仓库名
    private static final  String BUCKET_NAME = "imlc";

    private static OSS getClient(){
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI7bEBAxrPmn1K","Yp3YKykAE6MTttaA5VMfqjHVbHjUms");
        return new OSSClient(Factory.app(),ENDPOINT,credentialProvider);
    }

    /**
     * 上传的最终方法，成功返回一个路径
     * @param objKey 上传成功后，在服务器上生成的独立的KEY
     * @param path 需要上传的文件的路径
     * @return 存储的地址
     */
    private static String upload(String objKey,String path){

        //构建一个上传请求
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME,objKey,path);
        try {
            //初始化上传的client
            OSS client = getClient();
            //开始同步上传
            PutObjectResult result = client.putObject(request);
            //得到一个外网可访问的地址
            String url = client.presignPublicObjectURL(BUCKET_NAME,objKey);
            Log.d("TAG","PublicUrl:"+url);
            return url;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 上传普通图片
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path){
        String key = getImageObjKey(path);
        return upload(key,path);
    }

    /**
     * 上传头像
     * @param path
     * @return
     */
    public static String uploadPortrait(String path){
        String key = getPortraitObjKey(path);
        return upload(key,path);
    }

    /**
     * 上传音频
     * @param path
     * @return
     */
    public static String uploadAudio(String path){
        String key = getAudioObjKey(path);
        return upload(key,path);
    }

    /**
     * 按月存储，避免一个文件夹太多
     * @return yyyy-MM
     */
    private static String getDataString(){

        return DateFormat.format("yyyyMM",new Date()).toString();
    }

    private static String getImageObjKey(String path){
        String fileMD5 = HashUtil.getMD5String(new File(path));
        String dateString = getDataString();
        return String.format("image/%s/%s.jpg",dateString,fileMD5);
    }

    private static String getPortraitObjKey(String path){
        String fileMD5 = HashUtil.getMD5String(new File(path));
        String dateString = getDataString();
        return String.format("portrait/%s/%s.jpg",dateString,fileMD5);
    }

    private static String getAudioObjKey(String path){

        String fileMD5 = HashUtil.getMD5String(new File(path));
        String dateString = getDataString();
        return String.format("audio/%s/%s.mp3",dateString,fileMD5);
    }

}
