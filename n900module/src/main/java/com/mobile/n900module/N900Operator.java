package com.mobile.n900module;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.mobile.n900module.device.N900Device;
import com.mobile.n900module.imp.N900Impl;
import com.mobile.n900module.reader.CpuCardBean;
import com.newland.mtype.module.common.iccard.ICCardModule;
import com.newland.mtype.module.common.iccard.ICCardSlot;
import com.newland.mtype.module.common.iccard.ICCardSlotState;
import com.newland.mtype.module.common.iccard.ICCardType;
import com.newland.mtype.module.common.printer.FontSettingScope;
import com.newland.mtype.module.common.printer.FontType;
import com.newland.mtype.module.common.printer.LiteralType;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.printer.PrinterResult;
import com.newland.mtype.module.common.printer.PrinterStatus;
import com.newland.mtype.module.common.scanner.BarcodeScanner;
import com.newland.mtype.module.common.scanner.ScannerListener;
import com.newland.mtype.util.Dump;
import com.newland.mtype.util.ISOUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class N900Operator {
    private static final String TAG = "N900Operator";
    private static volatile N900Operator instance;
    private ICCardModule module;
    private Printer printer;
    private BarcodeScanner barcodeScanner;
    private N900Impl impl;

    private static final String MDF = "00a404000e315041592e5359532e444446303100";
    private static final String CMD2 = "00a404000ca00000000353494e4f50454300";
    //打开df01文件
    private static final String CMD3 = "00a4000002df01";
    private static final String CMD4 = "00b095000000";
    private static final String CMD5 = "00b09c000060";
    //读取员工号
    private static final String CMD6 = "00b09b0004";
    private static final String[] CMD = {MDF, CMD2, CMD3, CMD4, CMD5, CMD6};

    public static void init(Context context){
        if (instance == null){
            synchronized (N900Operator.class){
                if (instance == null){
                    instance = new N900Operator(context);
                }
            }
        }
    }

    private N900Operator(Context context){
        impl = new N900Impl(context);
        impl.connectDevice();
        printer = N900Device.getInstance(context).getPrinter();
        module = N900Device.getInstance(context).getICCardModule();
        barcodeScanner = N900Device.getInstance(context).getBarcodeScanner();
        printer.init();
        printer.setLineSpace(1);
        slodPowerOn(ICCardSlot.IC1, ICCardType.CPUCARD);
    }

    public static N900Operator getInstance() {

        return instance;
    }

    /**
     * IC模块上电
     *
     * @param slot
     * @param type
     * @return
     */
    public byte[] slodPowerOn(ICCardSlot slot, ICCardType type) {
        try {
            Log.d(TAG, "上电卡槽====" + slot);
            Log.d(TAG, "卡类型====" + type);
            byte[] result = module.powerOn(slot, type);
            Log.d(TAG, "上电结果 :" + Dump.getHexDump(result));
            Log.d(TAG, "卡槽：" + slot.toString() + "上电完成");
            Log.d(TAG, "卡类型：" + type.toString() + "上电完成");
            return result;
        } catch (Exception e) {
            Log.d(TAG, "卡槽上电异常:" + e.getMessage());
            Log.d(TAG, "请检查该卡槽是否已插入IC卡");
        }
        return null;
    }

    /**
     * IC模块下电
     *
     * @param slot
     * @param type
     * @return
     */
    public boolean slodPowerOff(ICCardSlot slot, ICCardType type) {
        try {
            module.powerOff(slot, type);
            Log.d(TAG, "下电完成");
            return true;
        } catch (Exception e) {
            Log.d(TAG, "卡槽下电异常:" + e.getMessage());
            Log.d(TAG, "请检查该卡槽是否已插入IC卡");
        }
        return false;
    }

    public void setICCardType(ICCardSlot slot, ICCardType type){
        module.setICCardType(slot,type);
    }

    /**
     * IC卡槽通信
     *
     * @param msg
     * @param slot
     * @param type
     * @return
     */
    public byte[] slodCommunication(String msg, ICCardSlot slot, ICCardType type) {
        try {
            Log.d(TAG, "发送数据：" + msg);
            byte[] req = hexStr2Byte(msg);
            byte[] result = module.call(slot, type, req, 30, TimeUnit.SECONDS);
            Log.d(TAG, "接收数据：" + ISOUtils.hexString(result));
            Log.d(TAG, "IC卡通信请求成功");

            return result;
        } catch (Exception e) {
            Log.d(TAG, "发送失败：" + e.getMessage());
        }
        return null;
    }

    /**
     * IC卡槽检测
     *
     * @return
     */
    public Map<String, String> slodCheck() {
        try {
            Map<ICCardSlot, ICCardSlotState> map = module.checkSlotsState();
            Map<String, String> result = new HashMap<>();
            for (Map.Entry<ICCardSlot, ICCardSlotState> entry : map.entrySet()) {
                if (entry.getKey() != null) {
                    result.put(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            Log.d(TAG, "IC卡槽检测结束");
            return result;
        } catch (Exception e) {
            Log.d(TAG, "IC卡槽检测异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * 检查是否卡槽是否插入卡片
     * @return
     */
    public boolean checkCardExist(){
        slodPowerOff(ICCardSlot.IC1, ICCardType.CPUCARD);
        slodPowerOn(ICCardSlot.IC1, ICCardType.CPUCARD);
        return slodCommunication("0084000004", ICCardSlot.IC1, ICCardType.CPUCARD) != null;
    }

    /**
     * 获取CPU卡信息
     * @return
     */
    public CpuCardBean getCpuCardInfo() {
        try {
            if (checkCardExist()) {
                CpuCardBean bean = new CpuCardBean();
                byte[][] source = new byte[6][];
                setICCardType(ICCardSlot.IC1, ICCardType.CPUCARD);
                for (int i = 0; i < CMD.length; i++) {
                    byte[] result = slodCommunication(CMD[i], ICCardSlot.IC1, ICCardType.CPUCARD);
                    source[i] = result;
                }
                bean.setSource(source);
                bean.parseTestSource();

                return bean;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取卡片余额 ,type:0x01
     * @return
     */
    public String getCardBalance() {
        return getCardBalance((byte) 0x01);
    }

    /**
     * 获取卡片余额
     * @param type
     * @return
     */
    public String getCardBalance(byte type) {
        try {
            if (checkCardExist()) {
                byte[] cardBuf = new byte[5];
                cardBuf[0] = (byte) 0x80;
                //命令头
                cardBuf[1] = 0x5c;
                cardBuf[2] = 0x00;
                cardBuf[3] = type;
                cardBuf[4] = 0x04;
                byte[] result = slodCommunication(byte2HexStr(cardBuf),
                        ICCardSlot.IC1, ICCardType.CPUCARD);
                Log.d(TAG, byte2HexStr(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化条形码扫描
     * @param context
     */
    public void initBarScan(Context context) {
        barcodeScanner.initScanner(context);
    }

    /**
     * 开始条形码扫描
     * @param listener
     */
    public void startBarScan(ScannerListener listener) {
        barcodeScanner.startScan(1, TimeUnit.SECONDS, listener);
    }

    /**
     * 停止条形码扫描
     */
    public void stopBarScan() {
        barcodeScanner.stopScan();
    }

    /**
     * 打印文字
     * @param text
     * @return
     */
    public PrinterResult printText(String text){
        return printer.print(text,5000, TimeUnit.MILLISECONDS);
    }

    /**
     * 设置打印文字一倍高度
     */
    public void setPrintTextNormalHeight() {
        printer.setFontType(LiteralType.CHINESE, FontSettingScope.HEIGHT,
                FontType.NORMAL);
    }

    /**
     * 设置打印文字两倍高度
     */
    public void setPrintTextDoubleHeight() {
        printer.setFontType(LiteralType.CHINESE, FontSettingScope.HEIGHT,
                FontType.DOUBLE);
    }

    /**
     * 设置打印文字一倍宽度
     */
    public void setPrintTextNormalWidth() {
        printer.setFontType(LiteralType.CHINESE, FontSettingScope.WIDTH,
                FontType.NORMAL);
    }

    /**
     * 设置打印文字两倍宽度
     */
    public void setPrintTextDoubleWidth() {
        printer.setFontType(LiteralType.CHINESE, FontSettingScope.WIDTH,
                FontType.DOUBLE);
    }

    /**
     * 打印图片
     * @param bitmap
     * @return
     */
    public PrinterResult printBitmap(Bitmap bitmap){
        return printBitmap(0, bitmap);
    }

    /**
     * 打印图片
     * @param offset
     * @param bitmap
     * @return
     */
    public PrinterResult printBitmap(int offset, Bitmap bitmap) {
        return printer.print(offset, bitmap, 5000, TimeUnit.MILLISECONDS);
    }

    /**
     * 返回打印机当前状态
     * @return
     */
    public PrinterStatus getPrinterStatus(){
        return printer.getStatus();
    }

    /**
     * 关闭N900设备
     */
    public  void releaseInstance(){
        slodPowerOff(ICCardSlot.IC1, ICCardType.CPUCARD);
        printer = null;
        module = null;
        impl.deltDevice();
        impl = null;
        instance = null;
    }

    private byte[] hexStr2Byte(String hex) {
        if (hex == null || "".equals(hex)) {
            return null;
        }
        hex = hex.toUpperCase();
        int length = hex.length() / 2;
        char[] hexChars = hex.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (toByte(hexChars[pos]) << 4 | toByte(hexChars[pos + 1]));

        }
        return d;
    }

    private String byte2HexStr(byte[] key) {
        StringBuilder d = new StringBuilder(key.length * 2);
        for (byte aKey : key) {
            char hi = Character.forDigit((aKey >> 4) & 0x0F, 16);
            char lo = Character.forDigit(aKey & 0x0F, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    private static byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
