package pony.xcode.citypicker;

import android.text.TextUtils;

import java.util.Comparator;

import pony.xcode.citypicker.model.City;

/**
 * sort by a-z
 */
public class CityComparator implements Comparator<City> {
    @Override
    public int compare(City lhs, City rhs) {
        String a = TextUtils.isEmpty(lhs.getPinyin()) ? "" : lhs.getPinyin().substring(0, 1);
        String b = TextUtils.isEmpty(rhs.getPinyin()) ? "" : rhs.getPinyin().substring(0, 1);
        return a.compareTo(b);
    }
}
