package com.simbest.boot.wfengine.util;

import com.mzlion.easyokhttp.HttpClient;
import com.simbest.boot.base.exception.Exceptions;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.util.DateUtil;
import com.simbest.boot.util.json.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class WqqueryHttpService {
    private static final Logger log = LoggerFactory.getLogger(WqqueryHttpService.class);

    /**
     * 三个常量建议直接配置
     * 目前客户端系统也没有检验机制，所以在配置文件中配置或者在这里配置是一样的，暂时没用
     */
    private String SOURCESYSTEMID = "wfengine";
    private String SOURCESYSTEMNAME = "Flowable任务管理";
    private String token = "63D9BDC2C1D14FCD9E67856419255421";

    /**
     * @param appUrl 客户端系统的访问地址，根据数据的租户id，从SysAppConfig或获取appUrl
     * @param url 接口url
     * @param jsonParam
     * @throws
     */
    public Map<String,Object> callInterfaceJsonObject(String appUrl,String url, Map<String,Object> jsonParam)  {

        Date date = DateUtil.getCurrent();
        String SUBMITDATE = DateUtil.getTimestamp(date);
        String TIMESTAMP = String.valueOf(date.getTime()/1000);
        String ACCESSTOKEN = Md5Token.MD5(token+TIMESTAMP);


        jsonParam.put("SUBMITDATE",SUBMITDATE);
        jsonParam.put("SOURCESYSTEMID",SOURCESYSTEMID);
        jsonParam.put("SOURCESYSTEMNAME",SOURCESYSTEMNAME);

        Map<String,Object> map = new HashMap<String, Object>();
        try {
            JsonResponse jsonResponse = (JsonResponse)HttpClient
                    .textBody(appUrl + url)
                    .json(jsonParam)
                    .header("TIMESTAMP",TIMESTAMP)
                    .header("ACCESSTOKEN",ACCESSTOKEN)
                    .charset("utf-8")
                    .asBean(JsonResponse.class);
            if (!jsonResponse.getErrcode().equals(JsonResponse.ERROR_CODE)) {
                log.error(SOURCESYSTEMID+"调用"+url+"失败！");
                map.put("state",ConstantsUtils.FAILE);
            } else if (jsonResponse.getErrcode().equals(JsonResponse.SUCCESS_CODE)) {
                log.info(SOURCESYSTEMID+"调用"+url+"成功！");
                map.put("state",ConstantsUtils.SUCCESS);
            }
            map.put("message",jsonResponse.getMessage());
            map.put("data",jsonResponse.getData());
        }catch(Exception e){
            map.put("state",ConstantsUtils.FAILE);
            map.put("message",ConstantsUtils.ERRORMES);
            log.error(Exceptions.getStackTraceAsString(e));
        }
        return map;
    }

    /**
     * @param appUrl 客户端系统的访问地址，根据数据的租户id，从SysAppConfig或获取appUrl
     * @param url 接口url
     * @param jsonParam
     * @throws
     */
    public Map<String,Object> callInterfaceJson(String appUrl,String url, String jsonParam)  {

        Date date = DateUtil.getCurrent();
        String SUBMITDATE = DateUtil.getTimestamp(date);
        String TIMESTAMP = String.valueOf(date.getTime()/1000);
        String ACCESSTOKEN = Md5Token.MD5(token+TIMESTAMP);


        Map m =  (Map)JacksonUtils.json2obj(jsonParam, Map.class);
        m.put("SUBMITDATE",SUBMITDATE);
        m.put("SOURCESYSTEMID",SOURCESYSTEMID);
        m.put("SOURCESYSTEMNAME",SOURCESYSTEMNAME);
        jsonParam=JacksonUtils.obj2json(m);

        Map<String,Object> map = new HashMap<String, Object>();
        try {
            JsonResponse jsonResponse = (JsonResponse)HttpClient
                    .textBody(appUrl + url)
                    .json(jsonParam)
                    .header("TIMESTAMP",TIMESTAMP)
                    .header("ACCESSTOKEN",ACCESSTOKEN)
                    .charset("utf-8")
                    .asBean(JsonResponse.class);
            if (!jsonResponse.getErrcode().equals(JsonResponse.ERROR_CODE)) {
                log.error(SOURCESYSTEMID+"调用"+url+"失败！");
                map.put("state",ConstantsUtils.FAILE);
            } else if (jsonResponse.getErrcode().equals(JsonResponse.SUCCESS_CODE)) {
                log.info(SOURCESYSTEMID+"调用"+url+"成功！");
                map.put("state",ConstantsUtils.SUCCESS);
            }
            map.put("message",jsonResponse.getMessage());
            map.put("data",jsonResponse.getData());
        }catch(Exception e){
            map.put("state",ConstantsUtils.FAILE);
            map.put("message",ConstantsUtils.ERRORMES);
            log.error(Exceptions.getStackTraceAsString(e));
        }
        return map;
    }

    /**
     * 普通字符传参数调用
     * @param appUrl 客户端系统的访问地址，根据数据的租户id，从SysAppConfig或获取appUrl
     * @param url 接口url
     * @param stringParam 字符传参数
     * @throws
     */
    public Map<String,Object> callInterfaceString(String appUrl, String url, Map<String, String> stringParam) {
            Date date = DateUtil.getCurrent();
            String SUBMITDATE = DateUtil.getTimestamp(date);
            String TIMESTAMP = String.valueOf(date.getTime()/1000);
            String ACCESSTOKEN = Md5Token.MD5(token+TIMESTAMP);

        Map<String,Object> map = new HashMap<String,Object>();
        try {
            JsonResponse jsonResponse = (JsonResponse)HttpClient
                    .post(appUrl + url)
                    .param("SUBMITDATE",SUBMITDATE)
                    .param("SOURCESYSTEMID",SOURCESYSTEMID)
                    .param("SOURCESYSTEMNAME",SOURCESYSTEMNAME)
                    .param(stringParam)
                    .header("TIMESTAMP",TIMESTAMP)
                    .header("ACCESSTOKEN",ACCESSTOKEN)
                    .asBean(JsonResponse.class);
            if (jsonResponse.getErrcode().equals(JsonResponse.ERROR_CODE)) {
                log.error(SOURCESYSTEMID+"调用"+url+"失败！");
                map.put("state",ConstantsUtils.FAILE);
            } else if (jsonResponse.getErrcode().equals(JsonResponse.SUCCESS_CODE)) {
                log.info(SOURCESYSTEMID+"调用"+url+"成功！");
                map.put("state",ConstantsUtils.SUCCESS);
            }
            map.put("message",jsonResponse.getMessage());
            map.put("data",jsonResponse.getData());
        }catch(Exception e){
            map.put("state",ConstantsUtils.FAILE);
            map.put("message",ConstantsUtils.ERRORMES);
            log.error(Exceptions.getStackTraceAsString(e));
        }
        return map;
    }

    /**
     * 文件上传接口
     * @param appUrl 客户端系统的访问地址，根据数据的租户id，从SysAppConfig或获取appUrl
     * @param url 接口url
     * @param stringParam 字符传参数
     * @throws
     */
    public Map<String,Object> callInterfaceFile(String appUrl,String url, File file, String filename, Map<String, String> stringParam) {
        Date date = DateUtil.getCurrent();
        String SUBMITDATE = DateUtil.getTimestamp(date);
        String TIMESTAMP = String.valueOf(date.getTime()/1000);
        String ACCESSTOKEN = Md5Token.MD5(token+TIMESTAMP);

        Map<String,Object> map = new HashMap<String,Object>();
        try {
            JsonResponse jsonResponse = (JsonResponse)HttpClient
                    .post(appUrl + url)
                    .param("SUBMITDATE",SUBMITDATE)
                    .param("SOURCESYSTEMID",SOURCESYSTEMID)
                    .param("SOURCESYSTEMNAME",SOURCESYSTEMNAME)
                    .param(stringParam)
                    .param("filename",filename)
                    .param("file",file)
                    .header("TIMESTAMP",TIMESTAMP)
                    .header("ACCESSTOKEN",ACCESSTOKEN)
                    .asBean(JsonResponse.class);
            if (jsonResponse.getErrcode().equals(JsonResponse.ERROR_CODE)) {
                log.error(SOURCESYSTEMID+"调用"+url+"失败！");
                map.put("state",ConstantsUtils.FAILE);
            } else if (jsonResponse.getErrcode().equals(JsonResponse.SUCCESS_CODE)) {
                log.info(SOURCESYSTEMID+"调用"+url+"成功！");
                map.put("state",ConstantsUtils.SUCCESS);
            }
            map.put("message",jsonResponse.getMessage());
            map.put("data",jsonResponse.getData());
        }catch(Exception e){
            map.put("state",ConstantsUtils.FAILE);
            map.put("message",ConstantsUtils.ERRORMES);
            log.error(Exceptions.getStackTraceAsString(e));
        }
        return map;
    }
}
