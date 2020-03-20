package pony.xcode.citypicker.adapter;


import pony.xcode.citypicker.model.City;

public interface InnerListener {
    void dismiss(int position, City data);
    void locate();
}
