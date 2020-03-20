package pony.xcode.citypicker.adapter;


import pony.xcode.citypicker.model.City;

public interface OnPickListener {
    void onPick(int position, City data);
    void onLocate();
    void onCancel();
}
