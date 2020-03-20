package pony.xcode.citypicker;


import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.lang.ref.WeakReference;
import java.util.List;

import pony.xcode.citypicker.adapter.OnPickListener;
import pony.xcode.citypicker.model.HotCity;
import pony.xcode.citypicker.model.LocateState;
import pony.xcode.citypicker.model.LocatedCity;

public class CityPicker {
    private static final String TAG = "CityPicker";

    private WeakReference<FragmentManager> mFragmentManager;

    private boolean enableAnim;
    private int mAnimStyle;
    private LocatedCity mLocation;
    private List<HotCity> mHotCities;
    private int mWindowWidth;
    private int mWindowHeight;
    private int mGridSpanCount = 3;
    private OnPickListener mOnPickListener;

    private CityPicker() {
    }

    private CityPicker(Fragment fragment) {
        mFragmentManager = new WeakReference<>(fragment.getChildFragmentManager());
    }

    private CityPicker(FragmentActivity activity) {
        mFragmentManager = new WeakReference<>(activity.getSupportFragmentManager());
    }

    public static CityPicker from(Fragment fragment) {
        return new CityPicker(fragment);
    }

    public static CityPicker from(FragmentActivity activity) {
        return new CityPicker(activity);
    }

    /**
     * 设置动画效果
     */
    public CityPicker setAnimationStyle(@StyleRes int animStyle) {
        this.mAnimStyle = animStyle;
        return this;
    }

    /**
     * 设置当前已经定位的城市
     */
    public CityPicker setLocatedCity(LocatedCity location) {
        this.mLocation = location;
        return this;
    }

    public CityPicker setHotCities(List<HotCity> data) {
        this.mHotCities = data;
        return this;
    }

    /**
     * 启用动画效果，默认为false
     */
    public CityPicker enableAnimation(boolean enable) {
        this.enableAnim = enable;
        return this;
    }

    /**
     * 窗口宽度
     */
    public CityPicker windowWidth(int width) {
        this.mWindowWidth = width;
        return this;
    }

    public CityPicker windowHeight(int height) {
        this.mWindowHeight = height;
        return this;
    }

    public CityPicker setGridSpanCount(int spanCount){
        this.mGridSpanCount = spanCount;
        return this;
    }

    /**
     * 设置选择结果的监听器
     */
    public CityPicker setOnPickListener(OnPickListener listener) {
        this.mOnPickListener = listener;
        return this;
    }

    public void show() {
        FragmentTransaction ft = mFragmentManager.get().beginTransaction();
        final Fragment prev = mFragmentManager.get().findFragmentByTag(TAG);
        if (prev != null) {
            ft.remove(prev).commit();
            ft = mFragmentManager.get().beginTransaction();
        }
        ft.addToBackStack(null);
        final CityPickerDialogFragment cityPickerFragment = CityPickerDialogFragment.newInstance(enableAnim);
        cityPickerFragment.setWindowWidth(mWindowWidth);
        cityPickerFragment.setWindowHeight(mWindowHeight);
        cityPickerFragment.setLocatedCity(mLocation);
        cityPickerFragment.setHotCities(mHotCities);
        cityPickerFragment.setAnimationStyle(mAnimStyle);
        cityPickerFragment.setSpanCount(mGridSpanCount);
        cityPickerFragment.setOnPickListener(mOnPickListener);
        cityPickerFragment.show(ft, TAG);
    }

    /**
     * 定位完成
     */
    public void locateComplete(LocatedCity location, @LocateState.State int state) {
        CityPickerDialogFragment fragment = (CityPickerDialogFragment) mFragmentManager.get().findFragmentByTag(TAG);
        if (fragment != null) {
            fragment.locationChanged(location, state);
        }
    }
}
