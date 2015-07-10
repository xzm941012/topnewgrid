package com.example.topnewgrid.util;

import android.widget.TextView;

import com.example.topnewgrid.R;

/**
 * Created by ’Ê∞Ædeœ… on 2015/5/15.
 */
public class LevalUtil {

    public static String getLevel(int i){
        int level=i/10;
        level=level+1;
        return level+"";
    }
}
