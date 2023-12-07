package com.example.studentoutcomebackend.adapter.image;

import com.example.studentoutcomebackend.utils.FileTypeCheckUtil;
import com.example.studentoutcomebackend.utils.SM3Util;
import kong.unirest.core.ContentType;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import kong.unirest.core.json.JSONException;
import kong.unirest.core.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class AlistImpl implements ImageService{

    private static String alistHost = "http://100.64.151.47:5244";
    private static String userName = "certUploader";
    private static String password = "user1234";
    private static AlistImpl instance;
    private String authToken;

    /**
     * 单例模式~，避免重复申请authToken
     * @return
     */
    public static ImageService get(){
        return Objects.requireNonNullElseGet(instance, AlistImpl::new);
    }

    /**
     * 通过alist的api登录
     */
    private void login(){
        HttpResponse<JsonNode> response = Unirest.post(alistHost + "/api/auth/login")
                .header("Content-Type", "application/json")
                .body("{\"username\": \"" + userName + "\",\"password\": \"" + password + "\"}")
                .asJson();
        try{
            authToken = resolveAlistResponse(response).getString("token");
        } catch(AlistException e){
            throw new ImageServiceException("Image service login failed!");
        }
    }

    private AlistImpl() {
        login();
    }

    /**
     * 解析alist返回的json
     * @param response 返回的response
     * @return data里面的json对象，code不是200就抛异常
     */
    private JSONObject resolveAlistResponse(HttpResponse<JsonNode> response){
        JSONObject resObj = response.getBody().getObject();
        int code = resObj.getInt("code");
        if(code == 200){
            try{
                return resObj.getJSONObject("data");
            } catch (JSONException e) {
                return null;
            }

        } else {
            String msg = resObj.getString("msg");
            throw new AlistException(code, msg);
        }
    }

    /**
     * 检测文件合法(是不是.png/jpg)->(没登陆/登陆过期就登录)->计算文件的SM3摘要作为文件名->上传到alist
     * @param multipartImg 图片
     * @return
     */
    @Override
    public String saveImage(MultipartFile multipartImg) {
        String fileName;
        InputStream imgIs;
        InputStream imgIs2;
        ContentType fileType = null;
        String imgTypeStr;
        byte[] imgBytes;
        try{
            imgBytes = multipartImg.getBytes();
            imgIs = multipartImg.getInputStream();
            imgIs2 = multipartImg.getInputStream();
        } catch (IOException e){
            throw new ImageServiceException("Image Not Readable");
        }
        // 检测文件是否合法
        imgTypeStr = FileTypeCheckUtil.getFileTypeByMagicNumber(imgIs);
        if(Objects.equals(imgTypeStr, "png"))
            fileType = ContentType.IMAGE_PNG;
        else if (imgTypeStr.equals("jpg"))
            fileType = ContentType.IMAGE_JPEG;
        if(fileType == null)
            throw new ImageServiceException("Not a png/jpeg image");
        fileName = SM3Util.getHashString(imgBytes) + "." + imgTypeStr;


        if(authToken == null)
            login();

        // 最多尝试5次，不行就报错
        for(int __= 0; __ < 5; __++){
            HttpResponse<JsonNode> response = Unirest.put(alistHost + "/api/fs/form")
                    .header("Authorization", authToken)
                    .header("File-Path", "/" + fileName)
                    .field("file", imgIs2, fileName)
                    .asJson();
            try{
                JSONObject ans = resolveAlistResponse(response);
                return fileName;
            }catch (AlistException e){

            }

        }
        throw new ImageServiceException("Failed to upload image. Max tries exceeded.");
    }


    /**
     * 以指定的文件名从alist删除图片
     * @param imageName 图片的路径
     */
    @Override
    public void removeImage(String imageName) {
        if(authToken == null)
            login();

        // 最多尝试5次，不行就报错
        for(int __= 0; __ < 5; __++){
            HttpResponse<JsonNode> response = Unirest.post(alistHost + "/api/fs/remove")
                    .header("Authorization", authToken)
                    .header("Content-Type", "application/json")
                    .body("{\"names\": [\""+ imageName +"\"],\"dir\": \"/\"}")
                    .asJson();
            try{
                JSONObject ans = resolveAlistResponse(response);
                return;
            }catch (AlistException e){

            }

        }
        throw new ImageServiceException("Failed to delete image. Max tries exceeded.");
    }
}

class AlistException extends RuntimeException{
    public int code;
    public String msg;
    public AlistException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}