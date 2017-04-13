package com.jin.fragment;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jin.slnews.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ZhaoLingFragment extends BaseFragment {
    private HttpUtils httpUtils;
    //private ProgressDialog progressDialog = null;
    private TextView tv_city;
    private TextView tv_weather;
    private TextView tv_windDir;
    private TextView tv_windSc;
    private TextView tv_tmp;// 温度
    private TextView tv_fl;// 体感温度
    private TextView tv_hum;// 相对湿度（%）
    private TextView tv_pcpn;// 降水量（mm）
    private TextView tv_pres;// 气压
    private TextView tv_vis;// 能见度（km）
    private Button btn_getWeather;
    // 声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    // 声明定位回调监听器
    // public AMapLocationListener mLocationListener = new
    // AMapLocationListener();
    // 声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_zhaoling, null);
        ViewUtils.inject(this, view); // 注入view和事件
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        btn_getWeather = (Button) view.findViewById(R.id.btn_getWeather);
        btn_getWeather.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // getIP();
                getLocation();
            }
        });
        tv_windDir = (TextView) view.findViewById(R.id.tv_windDir);
        tv_windSc = (TextView) view.findViewById(R.id.tv_windSc);

        tv_weather = (TextView) view.findViewById(R.id.tv_weather);
        tv_tmp = (TextView) view.findViewById(R.id.tv_tmp);
        tv_fl = (TextView) view.findViewById(R.id.tv_fl);
        tv_hum = (TextView) view.findViewById(R.id.tv_hum);
        tv_pcpn = (TextView) view.findViewById(R.id.tv_pcpn);
        tv_pres = (TextView) view.findViewById(R.id.tv_pres);
        tv_vis = (TextView) view.findViewById(R.id.tv_vis);
        return view;
    }

    @Override
    public void initData() {
        System.out.println("初始化实时天气！");
		httpUtils = new HttpUtils();
////		getWeagher("沈阳");
//		tv_city.setText("沈阳市");
//		// 1获取用户ip
//		// getIP();
//		// 初始化定位
        mLocationClient = new AMapLocationClient(mActivity);
        // 设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation location) {
                mLocationClient.stopLocation();
                String city = location.getCity();
                System.out.println("当前城市：" + city);
                if (TextUtils.isEmpty(city)) {
                    //toast("定位失败！");
                    return;

                }
                tv_city.setText(city);
                getWeagher(city);
            }
        });

        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(false);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 设置定位间隔,单位毫秒,默认为1000ms
        mLocationOption.setInterval(2000);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        getLocation();

    }

    private void getLocation() {
        // 启动定位
        mLocationClient.startLocation();
    }

    private void getWeagher(String city) {

        //progressDialog = ProgressDialog.show(mActivity, "请稍等...", "正在获取" + city + "天气...", true);
        RequestParams requestParams = new RequestParams();
        requestParams.addQueryStringParameter("city", city.endsWith("市") ? city.substring(0, city.length() - 1) : city);
        requestParams.addHeader("apikey", "69acd31de3673047221795cc3a052bd9");
        httpUtils.send(HttpMethod.GET, "http://apis.baidu.com/heweather/weather/free",
                requestParams, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        //if (progressDialog != null) {
                        //	progressDialog.cancel();
                        //}
                        String weather = responseInfo.result;
                        System.out.println("weather=" + weather);
                        // tv_weather.setText(weather);

                        // 把JsonElement对象转换成JsonArray
                        JsonArray citys = null;

                        JsonParser parser = new JsonParser();
                        JsonElement data = parser.parse(weather);

                        try {
                            citys = data.getAsJsonObject().get("HeWeather data service 3.0").getAsJsonArray();
                            JsonElement city0 = citys.get(0);
                            // System.out.println("=" +
                            // city0.getAsJsonObject().get("status") + "=");

                            if (city0.getAsJsonObject().get("status").toString().equals("\"ok\"")) {
                                toast("获取天气成功");
                                // 解析天气
                                parse(city0.getAsJsonObject());
                            } else {

                                toast("获取天气失败");
                            }
                        } catch (Exception e) {
                            toast("解析天气失败" + e.toString());
                            e.printStackTrace();
                        }

                        // System.out.println(citys.toString());

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        //if (progressDialog != null) {
                        //	progressDialog.cancel();
                        //}
                        System.out.println("获取天气失败=" + msg);
                        toast("获取天气失败：网络异常");
                    }
                });
    }

    protected void parse(JsonObject city) {
        JsonObject now = city.get("now").getAsJsonObject();
        String windDir = now.get("wind").getAsJsonObject().get("dir").toString();// 风向
        String windSc = now.get("wind").getAsJsonObject().get("sc").toString();// 风力
        String txt = now.get("cond").getAsJsonObject().get("txt").toString();// 天气状况描述
        String tmp = now.get("tmp").toString();// 温度
        String fl = now.get("fl").toString();// 体感温度
        String hum = now.get("hum").toString();// 相对湿度（%）
        String pcpn = now.get("pcpn").toString();// 降水量（mm）
        String pres = now.get("pres").toString();// 气压
        String vis = now.get("vis").toString();// 能见度（km）

        tv_weather.setText(txt);
        tv_windDir.setText(windDir);
        tv_windSc.setText(windSc);
        tv_tmp.setText(tmp);
        tv_fl.setText(fl);
        tv_hum.setText(hum);
        tv_pcpn.setText(pcpn);
        tv_pres.setText(pres);
        tv_vis.setText(vis);

    }

    protected void toast(String string) {
        Toast.makeText(mActivity, string, Toast.LENGTH_SHORT).show();
    }

}
