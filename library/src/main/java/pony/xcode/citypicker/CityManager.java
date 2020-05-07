package pony.xcode.citypicker;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import androidx.annotation.Nullable;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pony.xcode.citypicker.model.City;

/**
 * 通过读取assets下的json文件获取城市列表
 */
public class CityManager {
    private static final String FILE_NAME = "allcity.json";

    private Context mContext;

    public CityManager(Context context) {
        this.mContext = context;
    }

    private String toJson() throws IOException {
        //将json数据变成字符串
        StringBuilder sb = new StringBuilder();
        //获取assets资源管理器
        AssetManager assetManager = mContext.getAssets();
        //通过管理器打开文件并读取
        BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(FILE_NAME)));
        String line;
        while ((line = bf.readLine()) != null) {
            sb.append(line);
        }
        bf.close();
        return sb.toString();
    }

    //获取所有的城市（耗时操作，建议放到子线程中，避免线程堵塞）
    public List<City> getAllCities() {
        List<City> result = new ArrayList<>();
        try {
            String json = toJson();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String code = obj.optString("code");
                String name = obj.optString("name");
                String pinyin = obj.optString("pinyin");
                String province = obj.optString("province");
                result.add(new City(name, province, pinyin, code));
            }
        } catch (IOException e) {
            //读取assets的json文件异常
        } catch (Exception e) {
            //json 解析异常
        }
        Collections.sort(result, new CityComparator());
        return result;
    }

    //输入中文或拼音模糊搜索城市
    public List<City> searchCity(@Nullable String keyword) {
        return searchCity(keyword, null);
    }

    /**
     * 建议使用此方法进行模糊搜索，因为不传入所有城市集会重新去获取所有城市是一个耗时的操作可能会堵塞线程
     *
     * @param keyword   键入的关键词
     * @param allCities 所有城市集
     */
    public List<City> searchCity(@Nullable String keyword, @Nullable List<City> allCities) {
        if (keyword == null || keyword.length() == 0)
            return new ArrayList<>();
        if (allCities == null || allCities.isEmpty()) {
            allCities = getAllCities();
        }
        List<City> resultList = new ArrayList<>();
        for (City city : allCities) {
            String cityName = city.getName();
            String pinyin = city.getPinyin();
            if ((!TextUtils.isEmpty(cityName) && cityName.contains(keyword.trim()))
                    || (!TextUtils.isEmpty(pinyin) && pinyin.contains(keyword.trim()))) {
                resultList.add(city);
            }
        }
        Collections.sort(resultList, new CityComparator());
        return resultList;
    }
}
