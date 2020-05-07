package pony.xcode.citypicker;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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

    private String toJson() {
        //将json数据变成字符串
        StringBuilder sb = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = mContext.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(FILE_NAME)));
            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    //获取所有的城市
    public List<City> getAllCities() {
        String json = toJson();
        if (TextUtils.isEmpty(json)) return new ArrayList<>();
        List<City> result = new ArrayList<>();
        Type listType = new TypeToken<List<City>>() {
        }.getType();
        List<City> list = new Gson().fromJson(json, listType);
        if (list != null && !list.isEmpty()) {
            result.addAll(list);
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
