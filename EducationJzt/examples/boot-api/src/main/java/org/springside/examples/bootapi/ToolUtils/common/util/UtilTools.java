package org.springside.examples.bootapi.ToolUtils.common.util;

import com.google.common.collect.Lists;
import com.websuites.core.response.IResult;
import com.websuites.core.response.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.examples.bootapi.api.AccountActivity;
import org.springside.modules.utils.text.EncodeUtil;
import org.springside.modules.utils.text.HashUtil;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ZhangLei on 2019/1/6 0006
 */
public class UtilTools {
    private static Logger logger = LoggerFactory.getLogger(AccountActivity.class);
    private static DecimalFormat df = new DecimalFormat("000000");

    public static String upLastLetterofCertificateNo(String certificateno) {
        String lastletter = certificateno.substring(certificateno.length() - 1);
        boolean f = lastletter.matches("[a-zA-Z]");
        if (f) {
            String upperlastletter = lastletter.toUpperCase();
            return certificateno.substring(0, certificateno.length() - 1) + upperlastletter;
        } else {
            return certificateno;
        }

    }

    public static HashMap<String, String> object2StringMap(HashMap<String, Object> paramsMap) {
        HashMap rsMap = new HashMap();
        Iterator it = paramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String mapKey = valueOf(entry.getKey());
            String mapValue = valueOf(entry.getValue());
            rsMap.put(mapKey, mapValue.trim());
        }
        return rsMap;
    }

    public static String valueOf(Object obj) {
        if ((obj == null) || (obj.equals("null"))) return "";
        if (obj instanceof String) {
            return obj.toString();
        }
        if (obj instanceof BigDecimal) {
            BigDecimal bigObj = (BigDecimal) obj;
            return bigObj.toString();
        }
        return obj.toString();
    }

    /**
     * 对象转换为int
     * @param object
     * @return
     */
    public static int toInt(Object object) {
        if (object ==null){
            return 0;
        }
        if (object instanceof String) {
            return NumberUtils.toInt(object.toString());
        }
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }
        return 0;
    }

    public static boolean isEmpty(String str) {
        return StringUtils.isBlank(str) || "null".equals(str) ;
    }

    public static boolean isNull(Object obj)
    {
        return (obj == null);
    }

    public static String getResultMapValue(IResult rs, int rsIndex, int listIndex, String mapKey)
    {
        String value = "";
        try {
            List rsList = rs.getResult(rsIndex);
            Map rsMap = (Map)rsList.get(listIndex);
            value = valueOf(rsMap.get(mapKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value.trim();
    }

    public static String getResultMapValue(IResult rs, String mapKey)
    {
        String value = "";
        try {
            List rsList = rs.getResult(0);
            Map rsMap = (Map)rsList.get(0);
            value = valueOf(rsMap.get(mapKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value.trim();
    }

    public static Map getResultMap(IResult rs, int rsIndex, int listIndex)
    {
        Map rsMap = null;
        try {
            List rsList = rs.getResult(rsIndex);
            rsMap = (Map)rsList.get(listIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsMap;
    }

    public static Map getResultMap(IResult rs)
    {
        Map rsMap = null;
        try {
            List rsList = rs.getResult(0);
            if ((rsList != null) && (!(rsList.isEmpty())))
                rsMap = (Map)rsList.get(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rsMap;
    }

    public static IResult makerResults(Map rsMap)
    {
        IResult rs = new Result();
        rs.setResType(4);
        List rsList = new ArrayList();
        rsList.add(rsMap);
        rs.setResult(rsList);
        return rs;
    }

    public static IResult makerResults(IResult rs, Map rsMap)
    {
        List rsList = new ArrayList();
        rs.setResType(4);
        rsList.add(rsMap);
        rs.setResult(rsList);
        return rs;
    }

    public static IResult makerResults(IResult rs, String key, String value)
    {
        List rsList = new ArrayList();
        HashMap rsMap = new HashMap();
        rsMap.put(key, value);
        rsList.add(rsMap);
        rs.setResult(rsList);
        rs.setResType(4);
        return rs;
    }

    public static IResult makerResults(String code, String msg) {
        IResult rs = new Result();
        rs.setErrorCode(code);
        rs.setErrorMessage(msg);
        return rs;
    }

    public static IResult makerSusResults(String msg) {
        IResult rs = new Result();
        rs.setErrorCode("1");
        rs.setErrorMessage(msg);
        return rs;
    }

    public static IResult makerSusResults(String msg, List list) {
        IResult rs = new Result();
        rs.setErrorCode("1");
        rs.setErrorMessage(msg);
        rs.setResType(4);
        rs.setResult(list);
        return rs;
    }

    public static IResult makerSusResults(String msg, Map rsMap) {
        return makerSusResults(msg, (HashMap) rsMap);
    }

    public static IResult makerSusResults(String msg, HashMap rsMap) {
        IResult rs = new Result();
        List list = new ArrayList();
        list.add(rsMap);
        rs.setErrorCode("1");
        rs.setErrorMessage(msg);
        rs.setResType(4);
        rs.setResult(list);
        return rs;
    }

    public static IResult makerSusResults(String msg, String reStrKey, String reStrVal) {
        HashMap rsMap = new HashMap();
        rsMap.put(reStrKey, reStrVal);
        return makerSusResults(msg, rsMap);
    }


    public static <T> IResult makerErrResults(String msg, T data) {
        IResult rs = new Result();
        rs.setErrorCode("0");
        rs.setErrorMessage(msg);
        rs.setResType(4);
        if (data instanceof List) {
            rs.setResult(data);
        }
        if (data instanceof Map) {
            List list = Lists.newArrayList(data);
            rs.setResult(list);
        }
        return rs;
    }

    public static IResult makerErsResults(String msg) {
        IResult rs = new Result();
        rs.setErrorCode("0");
        rs.setErrorMessage(msg);
        return rs;
    }

    public static IResult makerResults(IResult rs, List rsList) {
        rs.setResult(rsList);
        rs.setResType(4);
        return rs;
    }

    public static List getSplitJsonParams(String channel)
    {
        List array = null;
        try {
            array = new ArrayList();
            String params = valueOf(channel);
            params = params.replace("{", "");
            params = params.replace("}", "");
            String[] paramsArray = params.split(",");
            for (String str : paramsArray) {
                String[] temp = str.split(":");
                Map channelMap = new HashMap();
                channelMap.put("channelno", temp[0]);
                channelMap.put("channelpay", temp[1]);
                String bankid = (temp.length == 2) ? "" : temp[2];
                channelMap.put("bankid", bankid);
                String vouchercode = (temp.length == 4) ? temp[3] : "";
                channelMap.put("vouchercode", vouchercode);
                array.add(channelMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("======= 拆分 chennal参数异常 =====");
            return null;
        }
        return array;
    }

    public static List splitVoucher(String voucher)
    {
        List array = null;
        try {
            array = new ArrayList();
            String params = valueOf(voucher);
            params = params.replace("{", "");
            params = params.replace("}", "");
            String[] paramsArray = params.split(",");
            for (String str : paramsArray) {
                String[] temp = str.split(":");
                Map voucherMap = new HashMap();
                voucherMap.put("vouchercode", temp[0]);
                voucherMap.put("voucheramount", temp[1]);
                array.add(voucherMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("======= 拆分 voucher 参数异常 =====");
        }
        return array;
    }

    public static String getChannelType(String channelno)
    {
        int length = channelno.length();
        if (length == 2)
            return channelno;
        if (length > 2) {
            return channelno.substring(length - 2, length);
        }
        return null;
    }

    public static String getListMapValue(List srcList, String mapKey)
    {
        if ((srcList == null) || (srcList.isEmpty())) return "";
        Map m = (Map)srcList.get(0);
        return valueOf(m.get(mapKey));
    }

    public static BigDecimal toBigDecimal(Object obj)
    {
        String strObj = valueOf(obj);
        BigDecimal decStrObj = new BigDecimal(0);
        decStrObj.setScale(2, RoundingMode.HALF_UP);
        try {
            decStrObj = new BigDecimal(strObj);
            decStrObj = decStrObj.setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" ===== 非数值转换Bigdecimal异常  =====");
        }
        return decStrObj;
    }

    public static BigDecimal toBigDecimal(int i)
    {
        BigDecimal bignumber = new BigDecimal(i);
        bignumber = bignumber.setScale(2, RoundingMode.HALF_UP);
        return bignumber;
    }

    public static BigDecimal toBigDecimal(String amount)
    {
        BigDecimal decimalAmount = new BigDecimal(0);
        try {
            decimalAmount = new BigDecimal(amount);
            decimalAmount = decimalAmount.setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            logger.error("=== 非数值字符串转换BigDecimal异常 ===",e);
            return null;
        }
        return decimalAmount;
    }

    public static BigInteger toBigInteger(String amount)
    {
        BigInteger decimalAmount = null;
        try {
            decimalAmount = new BigInteger(amount);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("=== 非数值字符串转换BigInteger异常 ===");
        }
        return decimalAmount;
    }

    public static String calculateBuyerPay(String smrjg, String charge)
    {
        BigDecimal decimalSmrjg = toBigDecimal(smrjg);
        decimalSmrjg = decimalSmrjg.setScale(2, RoundingMode.HALF_UP);
        BigDecimal decimalCharge = toBigDecimal(charge);
        decimalCharge = decimalCharge.setScale(2, RoundingMode.HALF_UP);
        BigDecimal decimalBuyerPay = decimalSmrjg.add(decimalCharge);
        return valueOf(decimalBuyerPay);
    }

    public static boolean isFailOrEmpty(IResult rs)
    {
        if (!(rs.isSuccessful())) return true;
        List rsList = rs.getResult(0);
        return rsList.isEmpty();
    }

    public static String getBankCode(String channelno)
    {
        if (StringUtils.isBlank(channelno) || "null".equals(channelno)) {
            return "";
        } else if (channelno.length() == 4) {
            return channelno;
        } else {
            return channelno.substring(0, 4);
        }
    }

    public static boolean isSfcg(String channelno)
    {
        String channeltype = getChannelType(channelno);
        return "06".equals(channeltype);
    }

    public static boolean isSfzf(String channelno)
    {
        String channeltype = getChannelType(channelno);
        return "01".equals(channeltype);
    }

    public static boolean isCashVourcher(String channelno)
    {
        return "1001".equals(channelno);
    }

    public static boolean isRedVourcher(String channelno)
    {
        return "1002".equals(channelno);
    }

    public static boolean isCornVourcher(String channelno)
    {
        return "1003".equals(channelno);
    }


    public static boolean isTradable(String tradestauts)
    {
        return "1".equals(tradestauts);
    }

    public static boolean isTradable(List tradeList)
    {
        String tradestatus = getListMapValue(tradeList, "tradestatus");
        return "1".equals(tradestatus);
    }

    public static String getCurDatetime()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date();
        return df.format(curDate);
    }

    public static String getCurDatetime(String pattern)
    {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date curDate = new Date();
        return df.format(curDate);
    }

    public static boolean isRecharge(String businesscode)
    {
        return "12001".equals(businesscode);
    }

    public static boolean isWithdraw(String businesscode)
    {
        return "12002".equals(businesscode);
    }

    public static String getCanCelBusiCode(String signstatus)
    {
        boolean assigned = "2".equals(signstatus);
        boolean unassigned = "1".equals(signstatus);
        String assinedCode = "11004";
        String unassinedCode = "11014";
        return (assigned) ? assinedCode : (unassigned) ? unassinedCode : "";
    }

    public static boolean isEmpty(List list)
    {
        return list == null || list.isEmpty();
    }

    public static boolean isSubscription(String dealType) {
        return "10001".equals(dealType) || "10002".equals(dealType);
    }

    public static boolean isTranferction(String dealType)
    {
        return "10003".equals(dealType);
    }

    public static boolean isBuyer(String saleorbuy)
    {
        return "0".equals(saleorbuy);
    }

    public static boolean isSaler(String saleorbuy)
    {
        return "1".equals(saleorbuy);
    }


    public static boolean isAvailable(String isavailable)
    {
        return "1".equals(isavailable);
    }

    public static int unitConvert(String validtimeunit)
    {
        if ("D".equals(validtimeunit))
            return 1;
        if ("W".equals(validtimeunit))
            return 7;
        if ("M".equals(validtimeunit))
            return 30;
        if ("Y".equals(validtimeunit))
            return 365;
        return 0;
    }

    public static String calculateDate(Date curDate, String validtime, String validtimeunit)
    {
        String endDate = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(curDate);
            int ivalidtime = Integer.parseInt(validtime);
            int ivalidtimeunit = unitConvert(validtimeunit);
            int iday = ivalidtime * ivalidtimeunit;
            calendar.add(Calendar.DATE, iday);
            endDate = formatDate(calendar.getTime(), "yyyyMMdd");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" ======= 解析消费券规则表中validtime字段异常，非数值 ======= ");
            return "";
        }
        return endDate;
    }

    public static String formatDate(Date date, String pattern)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatDate(String pattern)
    {
        Date date = new Date();
        return formatDate(date, pattern);
    }

    public static String formatDate()
    {
        String pattern = "yyyyMMdd";
        return formatDate(pattern);
    }

    public static String converVoucherIntegral(String vouchervalue, String convertratio) {
        if (StringUtils.isBlank(vouchervalue) || StringUtils.isBlank(convertratio)) {
            return "";
        }
        BigInteger i_integral;
        try {
            BigDecimal ratio = toBigDecimal(convertratio);
            BigDecimal pvouchervalue = toBigDecimal(vouchervalue);
            BigDecimal d_integral = pvouchervalue.divideToIntegralValue(ratio);
            i_integral = d_integral.toBigInteger();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" =========  积分兑换消费券 比例计算异常  ============");
            return "";
        }
        return i_integral.toString();
    }

    public static int getVocherCount(String vorcheramount, String vouchernominalvalue) {
        int vochercount = 0;
        try {
            BigDecimal bigVocherCount = new BigDecimal(0);
            BigDecimal amount = toBigDecimal(vorcheramount);
            BigDecimal valuetype = toBigDecimal(vouchernominalvalue);
            bigVocherCount = amount.divide(valuetype);
            bigVocherCount = bigVocherCount.setScale(0, 5);
            vochercount = bigVocherCount.intValue();
        } catch (Exception e) {
            logger.error("=========  积分兑换消费券 计算兑换后的消费券张数异常  ============",e);
        }
        return vochercount;
    }

    public static boolean isEnoughIntgeralBal(String integralbal, String converintegralbal) {
        if (StringUtils.isBlank(integralbal) || StringUtils.isBlank(converintegralbal)) {
            return false;
        }
        int rs = -1;
        try {
            BigDecimal bintegralbal = toBigDecimal(integralbal);
            BigDecimal bconverintegralbal = toBigDecimal(converintegralbal);
            rs = bintegralbal.compareTo(bconverintegralbal);
        } catch (Exception e) {
            logger.error(" ===== 积分兑换消费券，比较积分余额出现异常  ===== ",e);
        }
        return (rs != -1);
    }

    public static boolean isError(String errcode) {
        return "0".equals(errcode);
    }

    public static boolean notGreaterZero(BigDecimal number)
    {
        return (number.compareTo(new BigDecimal(0)) < 1);
    }

    public static boolean isGreaterZero(BigDecimal number) {
        return (number.compareTo(new BigDecimal(0)) > 0);
    }

    public static boolean isGreaterZero(String number) {
        BigDecimal bNumber = toBigDecimal(number);
        return !isNull(bNumber) && isGreaterZero(bNumber);
    }

    public static boolean assignBankWay(int fromway) {
        switch (fromway)
        {
            case 1:
                return false;
            case 2:
                return false;
            case 0:
                return true;
        }
        return false;
    }

    public static String getRandomCode(String a, int pwdLength) {
        int randomNum = getRandomCode(0, pwdLength);
        return valueOf(randomNum);
    }

    public static int getRandomCode(int a, int pwdLength) {
        Random random = new Random();
        int b = random.nextInt(10);
        a = a * 10 + b;
        if (a < 100000) {
            return getRandomCode(a, pwdLength);
        }
        return a;
    }

    @Deprecated
    public static String formatCurrency(BigDecimal sum) {
        DecimalFormat df_two = new DecimalFormat("#0.00");
        return df_two.format(sum);
    }

    public static String formatCurrency(double sum) {
        NumberFormat df_two = new DecimalFormat("#0.00");
        return df_two.format(sum);
    }

    public static String formatInfoMap(Map<String, String> infoMap) {
        StringBuffer sb = new StringBuffer();
        for (String key : infoMap.keySet()) {
            sb.append(key + "***" + infoMap.get(key));
        }
        return sb.toString();
    }

    public static int parseInt(String intstr) {
        return ((isEmpty(intstr)) ? 0 : Integer.parseInt(intstr.trim()));
    }

    public static double parseDouble(String doublestr) {
        return ((isEmpty(doublestr)) ? 0.0 : Double.parseDouble(doublestr.trim()));
    }

    public static void tracelog(StringBuilder log, String msg) {
        log.append(msg);
        logger.info("**||************************||**");
        logger.info(msg);
        logger.info("**||************************||**");
    }

    /**
     * 根据individual判断是机构用户、个人用户
     * @param individual，0为个人，1为机构
     * @return boolean 成功或者失败
     */
    public static boolean isIndividual(String individual){
        return individual.equals("0");
    }
    /**
     * 判断两个map中同样key是否一样
     * @param key
     * @return
     */
    public static boolean oldMapEquqlsNewMap(Map oldmap,HashMap newmap,String key){
        return valueOf(oldmap.get(key)).equals(valueOf(newmap.get(key)));
    }


    /**
     * String转map
     * @param map
     * @return
     */
    public static Map stringToMap(String map){
        if (map.startsWith("{")) {
            map = map.substring(1, map.length());
        }
        if (map.endsWith("}")) {
            map = map.substring(0, map.length() - 1);
        }

        Map map1 = new HashMap();
        String[] out = map.split(",");
        for (String anOut : out) {
            String[] inn = anOut.split("=");
            if(inn[0].equals("data")){
                map1.put(inn[0].replaceAll(" ", ""),"");
            }else{
                map1.put(inn[0].replaceAll(" ", ""),inn[1]);
            }
        }
        return map1;
    }
    /**
     * 精确到小数后4位
     * @param num 待处理的数字
     * @param point 精确到point位
     * @return
     */
    public static String numAccurateToPoint(String num,int point){
        String zero = "";
        while(zero.length()<point){
            zero+="0";
        }
        return new DecimalFormat("#,##0."+zero).format(Double.parseDouble(num));
    }

    public static<T> T deepClone(T src) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(src);
        objectOutputStream.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        obj = objectInputStream.readObject();
        objectInputStream.close();
        return (T) obj;
    }

    public static String hashPassword(String password) {
        return EncodeUtil.encodeBase64(HashUtil.sha1(password));
    }

}
