package com.example.sendmessage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.ToggleButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by 帅郑 on 2018/8/12.
 */

public class PhoneInfoUtils {
    private static String TAG = "PhoneInfoUtils";
    static String  ISDOUBLE;
    static String  SIMCARD;
    static String  SIMCARD_1;
    static String  SIMCARD_2;
    static boolean  isDouble;
    private ToggleButton toggleButton1;
    private ToggleButton toggleButton2;

    private TelephonyManager telephonyManager;
    //移动运营商编号
    private String NetworkOperator;
    private Context context;

    public PhoneInfoUtils(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    //获取sim卡iccid
    /*public String getIccid() {
        String iccid = "N/A";
        iccid = telephonyManager.getSimSerialNumber();
        return iccid;
    }

    //获取电话号码
    public String getNativePhoneNumber() {
        String nativePhoneNumber = "N/A";
        nativePhoneNumber = telephonyManager.getLine1Number();
        return nativePhoneNumber;
    }*/

    //获取手机服务商信息
    public String getProvidersName() {
        String providersName = "N/A";
        NetworkOperator = telephonyManager.getNetworkOperator();
        //IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
//        Flog.d(TAG,"NetworkOperator=" + NetworkOperator);
        if (NetworkOperator.equals("46000") || NetworkOperator.equals("46002")) {
            providersName = "移动";//中国移动
        } else if(NetworkOperator.equals("46001")) {
            providersName = "联通";//中国联通
        } else if (NetworkOperator.equals("46003")) {
            providersName = "电信";//中国电信
        }
        return providersName;
    }

    /*public String getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuffer sb = new StringBuffer();

        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());//移动运营商编号
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());//移动运营商名称
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        return  sb.toString();
    }*/

    /*public void initIsDoubleTelephone(Context context)
    {
        isDouble = true;
        Method method = null;
        Object result_0 = null;
        Object result_1 = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try
        {
            // 只要在反射getSimStateGemini 这个函数时报了错就是单卡手机（这是我自己的经验，不一定全正确）
            method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[]{ int.class });
            // 获取SIM卡1
            result_0 = method.invoke(tm, new Object[]{ new Integer(0) });
            // 获取SIM卡2
            result_1 = method.invoke(tm, new Object[]{ new Integer(1) });
        } catch (SecurityException e)
        {
            isDouble = false;
            e.printStackTrace();
            // System.out.println("1_ISSINGLETELEPHONE:"+e.toString());
        } catch (NoSuchMethodException e)
        {
            isDouble = false;
            e.printStackTrace();
            // System.out.println("2_ISSINGLETELEPHONE:"+e.toString());
        } catch (IllegalArgumentException e)
        {
            isDouble = false;
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            isDouble = false;
            e.printStackTrace();
        } catch (InvocationTargetException e)
        {
            isDouble = false;
            e.printStackTrace();
        } catch (Exception e)
        {
            isDouble = false;
            e.printStackTrace();
            // System.out.println("3_ISSINGLETELEPHONE:"+e.toString());
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        if (isDouble)
        {
            // 保存为双卡手机
            editor.putBoolean(ISDOUBLE, true);
            // 保存双卡是否可用
            // 如下判断哪个卡可用.双卡都可以用
            if (result_0.toString().equals("5") && result_1.toString().equals("5"))
            {
                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1"))
                {
                    editor.putString(SIMCARD, "0");
                }
                editor.putBoolean(SIMCARD_1, true);
                editor.putBoolean(SIMCARD_2, true);

                //tv2.setText("双卡可用");
                toggleButton1.setText();

            } else if (!result_0.toString().equals("5") && result_1.toString().equals("5"))
            {// 卡二可用
                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1"))
                {
                    editor.putString(SIMCARD, "1");
                }
                editor.putBoolean(SIMCARD_1, false);
                editor.putBoolean(SIMCARD_2, true);

                tv2.setText("卡二可用");

            } else if (result_0.toString().equals("5") && !result_1.toString().equals("5"))
            {// 卡一可用
                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1"))
                {
                    editor.putString(SIMCARD, "0");
                }
                editor.putBoolean(SIMCARD_1, true);
                editor.putBoolean(SIMCARD_2, false);

                tv2.setText("卡一可用");

            } else
            {// 两个卡都不可用(飞行模式会出现这种种情况)
                editor.putBoolean(SIMCARD_1, false);
                editor.putBoolean(SIMCARD_2, false);

                tv2.setText("飞行模式");
            }
        } else
        {
            // 保存为单卡手机
            editor.putString(SIMCARD, "0");
            editor.putBoolean(ISDOUBLE, false);
        }
        editor.commit();
    }*/
}
