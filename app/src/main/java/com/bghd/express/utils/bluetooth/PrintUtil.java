package com.bghd.express.utils.bluetooth;

import android.content.Context;
import android.widget.Toast;

import com.bghd.express.entiy.OrderListEntity;
import com.bghd.express.utils.tools.StringUtils;
import com.bghd.express.utils.tools.ToastUtil;

import java.util.Calendar;

import HPRTAndroidSDKA300.HPRTPrinterHelper;

/**
 * Created by lixu on 2018/2/12.
 */

public class PrintUtil {
    public static void printTest3(Context paramContext, OrderListEntity.DataBean paramDataBean)
    {

        String str3 = "123456789779";
        String str2 = "";
        String str4 = "";
        String localObject3 = "";
        String localObject2 = "";
        String str1 = str2;
        Object localObject1 = str4;
        int  i = 1;
//        for (;;)
//        {
            try
            {
                if (!StringUtils.isEmpty(paramDataBean.getGetuser_province()))
                {
                    str1 = str2;
                    localObject1 = str4;
                    localObject3 = "" + paramDataBean.getGetuser_province();
                }
                localObject2 = localObject3;
                str1 = str2;
                localObject1 = str4;
                if (!StringUtils.isEmpty(paramDataBean.getGetuser_city()))
                {
                    str1 = str2;
                    localObject1 = str4;
                    localObject2 = (String)localObject3 + paramDataBean.getGetuser_city();
                }
                localObject3 = localObject2;
                str1 = str2;
                localObject1 = str4;
                if (!StringUtils.isEmpty(paramDataBean.getGetuser_district()))
                {
                    str1 = str2;
                    localObject1 = str4;
                    localObject3 = (String)localObject2 + paramDataBean.getGetuser_district();
                }
                str1 = str2;
                localObject1 = str4;
                str2 = (String)localObject3 + paramDataBean.getGetuser_address();
                localObject3 = "";
                str1 = str2;
                localObject1 = str4;
                if (!StringUtils.isEmpty(paramDataBean.getShipuser_province()))
                {
                    str1 = str2;
                    localObject1 = str4;
                    localObject3 = "" + paramDataBean.getShipuser_province();
                }
                localObject2 = localObject3;
                str1 = str2;
                localObject1 = str4;
                if (!StringUtils.isEmpty(paramDataBean.getShipuser_city()))
                {
                    str1 = str2;
                    localObject1 = str4;
                    localObject2 = (String)localObject3 + paramDataBean.getShipuser_city();
                }
                localObject3 = localObject2;
                str1 = str2;
                localObject1 = str4;
                if (!StringUtils.isEmpty(paramDataBean.getShipuser_district()))
                {
                    str1 = str2;
                    localObject1 = str4;
                    localObject3 = (String)localObject2 + paramDataBean.getShipuser_district();
                }
                str1 = str2;
                localObject1 = str4;
                localObject2 = (String)localObject3 + paramDataBean.getShipuser_address();
                if (paramDataBean != null)
                {
                    str1 = str2;
                    localObject1 = localObject2;
                    if (paramDataBean.getExpress_no() != null)
                    {
                        str1 = str2;
                        localObject1 = localObject2;
                        localObject3 = String.valueOf(paramDataBean.getExpress_no()).toString();
                        localObject1 = localObject2;
                        str1 = str2;
                    }
                }
            }
            catch (Exception localException)
            {
                //Object localObject2;
                localObject3 = str3;
                //continue;
            }
            try
            {
                HPRTPrinterHelper.printAreaSize("0", "200", "200", "1428", "1");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "10", "8", "400-8558-037");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "10", "40", "www.baigehuidi.com");
                HPRTPrinterHelper.SetMag("2", "2");
                HPRTPrinterHelper.SetBold("2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "300", "21", paramDataBean.getDatou());
                HPRTPrinterHelper.SetMag("1", "1");
                HPRTPrinterHelper.SetBold("1");
                HPRTPrinterHelper.Line("0", String.valueOf(90), "600", String.valueOf(90), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(109), "收");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(160), "件");
                HPRTPrinterHelper.Line("48", String.valueOf(90), "48", String.valueOf(332), "2");
                HPRTPrinterHelper.SetBold("2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(100), "姓名:" + paramDataBean.getGetuser_truename() + " 电话:" + paramDataBean.getGetuser_mobile());
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(129), "地址:");



                HPRTPrinterHelper.AutLine("137", String.valueOf(129), 425, 8, true,false,str1);
                HPRTPrinterHelper.SetBold("0");
                HPRTPrinterHelper.Line("0", String.valueOf(211), "600", String.valueOf(211), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(230), "寄");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(281), "件");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(221), "姓名:" + paramDataBean.getShipuser_truename() + "  电话:" + paramDataBean.getShipuser_mobile());
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(250), "地址:");
                HPRTPrinterHelper.AutLine("137", String.valueOf(250), 425, 8, true,false,(String)localObject1);
                HPRTPrinterHelper.Line("0", String.valueOf(332), "600", String.valueOf(332), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "3", "0", String.valueOf(430), String.valueOf(355), "客户济南转运");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "3", "0", String.valueOf(415), String.valueOf(390), "若退回请发53168");
                HPRTPrinterHelper.SetMag("4", "4");
                HPRTPrinterHelper.SetBold("5");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "3", "0", String.valueOf(415), String.valueOf(405), paramDataBean.getNumber());
                HPRTPrinterHelper.SetBold("0");
                HPRTPrinterHelper.SetMag("1", "1");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", String.valueOf(400), String.valueOf(490), paramDataBean.getOne());
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", String.valueOf(400), String.valueOf(520), paramDataBean.getTwo());
                HPRTPrinterHelper.Barcode(HPRTPrinterHelper.BARCODE, HPRTPrinterHelper.code128, "1", "2", "60", "0", String.valueOf(342), false, "7", "0", "5", (String)localObject3);
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "210", String.valueOf(360), (String)localObject3);
                HPRTPrinterHelper.Line("0", String.valueOf(406), "0", String.valueOf(550), "2");
                HPRTPrinterHelper.Line("400", String.valueOf(406), "400", String.valueOf(550), "2");
                HPRTPrinterHelper.Line("0", String.valueOf(406), "400", String.valueOf(406), "2");
                HPRTPrinterHelper.Line("0", String.valueOf(550), "400", String.valueOf(550), "2");
                HPRTPrinterHelper.AutCenter(HPRTPrinterHelper.TEXT, "0", String.valueOf(418), 400, 8, "打印日期:" + time());
                HPRTPrinterHelper.Line("0", String.valueOf(454), "400", String.valueOf(454), "2");
                HPRTPrinterHelper.Line("200", String.valueOf(454), "200", String.valueOf(502), "2");
                HPRTPrinterHelper.AutCenter(HPRTPrinterHelper.TEXT, "200", String.valueOf(458), 200, 4, "(已验视)");
                HPRTPrinterHelper.Line("0", String.valueOf(502), "400", String.valueOf(502), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "0", String.valueOf(514), "签收人:");
                HPRTPrinterHelper.Line("84", String.valueOf(502), "84", String.valueOf(550), "2");
                HPRTPrinterHelper.Line("200", String.valueOf(502), "200", String.valueOf(550), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "200", String.valueOf(514), "时间:");
                HPRTPrinterHelper.Line("284", String.valueOf(502), "284", String.valueOf(550), "2");
                HPRTPrinterHelper.Line("0", String.valueOf(550), "400", String.valueOf(550), "2");
              //  i = 90 + 464;
                HPRTPrinterHelper.Line("0", String.valueOf(i), "600", String.valueOf(i), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(573), "收");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(624), "件");
                HPRTPrinterHelper.Line("48", String.valueOf(i), "48", String.valueOf(796), "2");
                HPRTPrinterHelper.SetBold("2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(564), "姓名:" + paramDataBean.getGetuser_truename() + "  电话:" + paramDataBean.getGetuser_mobile());
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(593), "地址:");
                HPRTPrinterHelper.AutLine("137", String.valueOf(593), 425, 8, true,false,str1);
                HPRTPrinterHelper.SetBold("0");
                HPRTPrinterHelper.Line("0", String.valueOf(675), "600", String.valueOf(675), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(694), "寄");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(745), "件");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(685), "姓名:" + paramDataBean.getShipuser_truename() + "  电话:" + paramDataBean.getShipuser_mobile());
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(714), "地址:");
                HPRTPrinterHelper.AutLine("137", String.valueOf(714), 425, 8, true,false,(String)localObject1);
                HPRTPrinterHelper.Line("0", String.valueOf(796), "600", String.valueOf(796), "2");
                HPRTPrinterHelper.Barcode(HPRTPrinterHelper.BARCODE, HPRTPrinterHelper.code128, "1", "2", "60", "0", String.valueOf(806), false, "7", "0", "5", (String)localObject3);
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "0", String.valueOf(873), "代收转运运单号码:");
                HPRTPrinterHelper.SetBold("3");
                HPRTPrinterHelper.SetMag("2", "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "3", "0", "0", String.valueOf(900), (String)localObject3);
                HPRTPrinterHelper.SetMag("1", "1");
                HPRTPrinterHelper.SetBold("0");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "0", String.valueOf(951), "(此单发件站留存)");
                HPRTPrinterHelper.SetMag("4", "4");
                HPRTPrinterHelper.SetBold("5");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "1", String.valueOf(400), String.valueOf(804), paramDataBean.getNumber());
                HPRTPrinterHelper.SetBold("0");
                HPRTPrinterHelper.SetMag("1", "1");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "3", "0", "300", String.valueOf(804), "打印日期:" + time());
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", String.valueOf(400), String.valueOf(951), "换单序列号");
                i += 432;
                HPRTPrinterHelper.Line("0", String.valueOf(i), "600", String.valueOf(i), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(1005), "收");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(1056), "件");
                HPRTPrinterHelper.Line("48", String.valueOf(i), "48", String.valueOf(1228), "2");
                HPRTPrinterHelper.SetBold("2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(996), "姓名:" + paramDataBean.getGetuser_truename() + "  电话:" + paramDataBean.getGetuser_mobile());
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(1025), "地址:");
                HPRTPrinterHelper.AutLine("137", String.valueOf(1025), 425, 8,true,false, str1);
                HPRTPrinterHelper.SetBold("0");
                HPRTPrinterHelper.Line("0", String.valueOf(1107), "600", String.valueOf(1107), "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(1126), "寄");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "4", "0", "0", String.valueOf(1177), "件");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(1117), "姓名:" + paramDataBean.getShipuser_truename() + "  电话:" + paramDataBean.getShipuser_mobile());
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "65", String.valueOf(1146), "地址:");
                HPRTPrinterHelper.AutLine("137", String.valueOf(1146), 425, 8, true,false,(String)localObject1);
                HPRTPrinterHelper.Line("0", String.valueOf(1228), "600", String.valueOf(1228), "2");
                HPRTPrinterHelper.Barcode(HPRTPrinterHelper.BARCODE, HPRTPrinterHelper.code128, "1", "2", "60", "0", String.valueOf(1238), false, "7", "0", "5", (String)localObject3);
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "0", String.valueOf(1305), "代收转运运单号码:");
                HPRTPrinterHelper.SetBold("3");
                HPRTPrinterHelper.SetMag("2", "2");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "3", "0", "0", String.valueOf(1332), (String)localObject3);
                HPRTPrinterHelper.SetMag("1", "1");
                HPRTPrinterHelper.SetBold("0");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "8", "0", "0", String.valueOf(1383), "(此单发件人留存)");
                HPRTPrinterHelper.SetMag("2", "2");
                HPRTPrinterHelper.SetBold("2");
                HPRTPrinterHelper.AutLine("260", String.valueOf(1306), 240, 3,true,false, paramDataBean.getOne());
                HPRTPrinterHelper.AutLine("260", String.valueOf(1346), 240, 3,true,false, paramDataBean.getTwo());
                HPRTPrinterHelper.SetBold("0");
                HPRTPrinterHelper.SetMag("1", "1");
                HPRTPrinterHelper.Text(HPRTPrinterHelper.TEXT, "3", "0", "260", String.valueOf(1236), "打印日期:" + time());
                HPRTPrinterHelper.Form();
                HPRTPrinterHelper.Print();
                return;
            }
            catch (Exception e)
            {
                ToastUtil.showToast(paramContext,"打印失败",ToastUtil.TOAST_TYPE_ERRO);
                //paramDataBean.printStackTrace();
                return;
            }
         //   str1 = str2;
          //  localObject1 = localObject2;
         //  str4 = paramDataBean.getId();
         //   localObject3 = str3;
         //   str1 = str2;
         //   localObject1 = localObject2;
//            if (str4 != null)
//            {
//                localObject3 = str3;
//                str1 = str2;
//                localObject1 = localObject2;
//            }
        }
 //   }
    public static String time()
    {
        Calendar localCalendar = Calendar.getInstance();
        String str1 = localCalendar.get(Calendar.YEAR) + "年";
        String str2 = localCalendar.get(Calendar.MONTH) + 1 + "月";
        String str3 = localCalendar.get(Calendar.DATE) + "日";
        new StringBuilder().append(localCalendar.get(Calendar.HOUR)).append("时").toString();
        new StringBuilder().append(localCalendar.get(Calendar.MINUTE)).append("分").toString();
        new StringBuilder().append(localCalendar.get(Calendar.SECOND)).append("秒").toString();
        return str1 + str2 + str3;
    }
}
