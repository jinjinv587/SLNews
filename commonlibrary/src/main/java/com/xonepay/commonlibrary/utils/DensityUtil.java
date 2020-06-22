package com.xonepay.commonlibrary.utils;

import android.content.Context;

/**
 * 单位转换
* @ClassName: DensityUtil 
* @Description:
* @author sfq 
* @date 2016年10月31日 下午6:11:10 
*
 */
public class DensityUtil{    

    public static int dip2px(Context context, float dipValue) {  

        final float scale = context.getResources().getDisplayMetrics().density;  

        return (int) (dipValue * scale + 0.5f);  

    }  

    public static int px2dip(Context context, float pxValue) {  

        final float scale = context.getResources().getDisplayMetrics().density;  

        return (int) (pxValue / scale + 0.5f);  

    }  

} 