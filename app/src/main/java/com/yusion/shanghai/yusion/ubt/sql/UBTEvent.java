package com.yusion.shanghai.yusion.ubt.sql;

/**
 * Created by ice on 2017/9/15.
 */
public class UBTEvent {
    //eg:AppCompatTextView
    public String object;
    //eg:onClick onResume
    public String action;
    public long ts;
    public String page; //eg:LoginActivity

    @Override
    public String toString() {
        return "UBTEvent{" +
                "object='" + object + '\'' +
                ", action='" + action + '\'' +
                ", ts=" + ts +
                ", page='" + page + '\'' +
                '}';
    }
}
