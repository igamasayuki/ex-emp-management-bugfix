package com.example.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 性別のenum.
 *
 * @author rui.inoue
 */
public enum Gender {
    MAN(1, "男"),
    WOMAN(2, "女"),
    ;

    /** キー */
    private final Integer key;
    /** 値 */
    private final String value;

    Gender(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    /**
     * キーからenumの取得.
     *
     * @param key キー
     * @return キーに基づくenum
     */
    public static Gender of(Integer key){
        for(Gender gender: Gender.values()){
            if(gender.key == key){
                return gender;
            }
        }
        throw new IndexOutOfBoundsException("The value of enum does not exist.");
    }

    public static Map<Integer, String> getMap(){
        Map<Integer, String> map = new LinkedHashMap<>();
        for(Gender gender: Gender.values()){
            map.put(gender.key, gender.value);
        }
        return map;
    }
}
