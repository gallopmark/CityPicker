package pony.xcode.citypicker.db;

class DBConfig {
    static final String DB_NAME_V1 = "china_cities.db";
    private static final String DB_NAME_V2 = "china_cities_v2.db";
    static final String LATEST_DB_NAME = DB_NAME_V2;

    static final String TABLE_NAME = "cities";

    static final String COLUMN_C_NAME = "c_name";
    static final String COLUMN_C_PROVINCE = "c_province";
    static final String COLUMN_C_PINYIN = "c_pinyin";
    static final String COLUMN_C_CODE = "c_code";
}
