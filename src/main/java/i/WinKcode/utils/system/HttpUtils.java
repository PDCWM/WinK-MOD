package i.WinKcode.utils.system;

import javax.annotation.Nullable;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpUtils {
    /**
     * http get请求
     * @param httpUrl 链接
     * @return 响应数据
     */
    public static HttpRetVal doGet(String httpUrl, List<property> list){
        //链接
        HttpURLConnection connection=null;
        InputStream is=null;
        BufferedReader br = null;
        StringBuilder result = new StringBuilder();
        int retStatus = 250;
        try {
            //创建连接
            URL url=new URL(httpUrl);
            connection= (HttpURLConnection) url.openConnection();
            //设置请求方式
            connection.setRequestMethod("GET");
            //设置连接超时时间
            connection.setConnectTimeout(15000);
            //设置读取超时时间
            connection.setReadTimeout(15000);

            if(!list.isEmpty()){
                for (property pro : list){
                    connection.setRequestProperty(pro.key,pro.val);
                }
            }
            //开始连接
            connection.connect();
            retStatus = connection.getResponseCode();
            //获取响应数据
            //if(connection.getResponseCode()==200){
                //获取返回的数据
                is=connection.getInputStream();
                if(is!=null){
                    br=new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String temp = null;
                    while ((temp=br.readLine())!=null){
                        result.append(temp);
                    }
                }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            assert connection != null;
            connection.disconnect();// 关闭远程连接
        }
        return new HttpRetVal(retStatus,result.toString());
    }

    /**
     * post请求
     * @param httpUrl 链接
     * @param param 参数
     * @return
     */
    public static String doPost(String httpUrl, @Nullable String param) {
        StringBuilder result = new StringBuilder();
        //连接
        HttpURLConnection connection = null;
        OutputStream os = null;
        InputStream is = null;
        BufferedReader br = null;
        try {
            //创建连接对象
            URL url = new URL(httpUrl);
            //创建连接
            connection = (HttpURLConnection) url.openConnection();
            //设置请求方法
            connection.setRequestMethod("POST");
            //设置连接超时时间
            connection.setConnectTimeout(15000);
            //设置读取超时时间
            connection.setReadTimeout(15000);
            //设置是否可读取
            connection.setDoOutput(true);
            //设置响应是否可读取
            connection.setDoInput(true);
            //设置参数类型
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //拼装参数
            if (param != null && !param.equals("")) {
                //设置参数
                os = connection.getOutputStream();
                //拼装参数
                os.write(param.getBytes(StandardCharsets.UTF_8));
            }
            //设置权限
            //设置请求头等
            //开启连接
            //connection.connect();
            //读取响应
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                if (is != null) {
                    br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                    String temp = null;
                    if ((temp = br.readLine()) != null) {
                        result.append(temp);
                    }
                }
            }
            //关闭连接
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //关闭连接
            assert connection != null;
            connection.disconnect();
        }
        return result.toString();
    }
}