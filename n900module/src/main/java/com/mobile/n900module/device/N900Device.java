package com.mobile.n900module.device;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.newland.me.ConnUtils;
import com.newland.me.DeviceManager;
import com.newland.mtype.ConnectionCloseEvent;
import com.newland.mtype.Device;
import com.newland.mtype.ExModuleType;
import com.newland.mtype.ModuleType;
import com.newland.mtype.event.DeviceEventListener;
import com.newland.mtype.module.common.cardreader.CardReader;
import com.newland.mtype.module.common.emv.EmvModule;
import com.newland.mtype.module.common.iccard.ICCardModule;
import com.newland.mtype.module.common.light.IndicatorLight;
import com.newland.mtype.module.common.pin.K21Pininput;
import com.newland.mtype.module.common.printer.Printer;
import com.newland.mtype.module.common.rfcard.RFCardModule;
import com.newland.mtype.module.common.scanner.BarcodeScanner;
import com.newland.mtype.module.common.scanner.BarcodeScannerManager;
import com.newland.mtype.module.common.security.SecurityModule;
import com.newland.mtype.module.common.serialport.SerialModule;
import com.newland.mtype.module.common.storage.Storage;
import com.newland.mtype.module.common.swiper.K21Swiper;
import com.newland.mtypex.nseries.NSConnV100ConnParams;

public class N900Device extends AbstractDevice {
    private static final String K21_DRIVER_NAME = "com.newland.me.K21Driver";
    private static final String TAG = "N900Device";
    private static N900Device n900Device=null;
    private static DeviceManager deviceManager ;
    private static Context baseActivity;

    private N900Device(Context baseactivity) {
        N900Device.baseActivity = baseactivity;
    }

    public static N900Device getInstance(Context baseactivity) {
        if (n900Device == null) {
            synchronized (N900Device.class) {
                if (n900Device == null) {
                    n900Device = new N900Device(baseactivity);
                }
            }
        }
        N900Device.baseActivity = baseactivity;
        return n900Device;
    }

    @Override
    public void connectDevice() {
        try {
            deviceManager = ConnUtils.getDeviceManager();
            deviceManager.init(baseActivity, K21_DRIVER_NAME, new NSConnV100ConnParams(), new DeviceEventListener<ConnectionCloseEvent>() {
                @Override
                public void onEvent(ConnectionCloseEvent event, Handler handler) {
                    if (event.isSuccess()) {
                        Log.i(TAG, "设备被客户主动断开！");
                    }
                    if (event.isFailed()) {
                        Log.i(TAG, "设备链接异常断开！");
                    }
                }

                @Override
                public Handler getUIHandler() {
                    return null;
                }
            });
            Log.i(TAG, "N900设备控制器已初始化!");
            deviceManager.connect();
            deviceManager.getDevice().setBundle(new NSConnV100ConnParams());
            Log.i(TAG, "设备连接成功.");
        } catch (Exception e1) {
            e1.printStackTrace();
            Log.i(TAG, "链接异常,请检查设备或重新连接...");
        }
    }

    @Override
    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (deviceManager != null) {
                        deviceManager.disconnect();
                        deviceManager = null;
                        //baseActivity.showMessage("设备断开成功...", MessageTag.TIP);
                        Log.i(TAG, "设备断开成功...");
                        //baseActivity.btnStateToWaitingInit();
                    }
                } catch (Exception e) {
                    //baseActivity.showMessage("设备断开异常:" + e, MessageTag.TIP);
                    Log.i(TAG, "设备断开异常");
                }
            }
        }).start();
    }

    @Override
    public boolean isDeviceAlive() {
        return (deviceManager != null && deviceManager.getDevice().isAlive());
    }

    @Override
    public CardReader getCardReaderModuleType() {
        return (CardReader) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_CARDREADER);
    }

    @Override
    public EmvModule getEmvModuleType() {
        return (EmvModule) deviceManager.getDevice().getExModule("EMV_INNERLEVEL2");
    }

    @Override
    public ICCardModule getICCardModule() {
        return (ICCardModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_ICCARDREADER);
    }

    @Override
    public IndicatorLight getIndicatorLight() {
        return (IndicatorLight) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_INDICATOR_LIGHT);
    }

    @Override
    public K21Pininput getK21Pininput() {
        return (K21Pininput) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PININPUT);
    }

    @Override
    public Printer getPrinter() {
        Printer printer=(Printer) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_PRINTER);
        printer.init();
        return printer;
    }

    @Override
    public RFCardModule getRFCardModule() {
        return (RFCardModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_RFCARDREADER);
    }

    @Override
    public BarcodeScanner getBarcodeScanner() {
        BarcodeScannerManager barcodeScannerManager=(BarcodeScannerManager) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_BARCODESCANNER);
        return barcodeScannerManager.getDefault();
    }

    @Override
    public SecurityModule getSecurityModule() {
        return (SecurityModule) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SECURITY);
    }

    @Override
    public Storage getStorage() {
        return (Storage) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_STORAGE);
    }

    @Override
    public K21Swiper getK21Swiper() {
        return (K21Swiper) deviceManager.getDevice().getStandardModule(ModuleType.COMMON_SWIPER);
    }

    @Override
    public Device getDevice() {
        return deviceManager.getDevice();
    }
    @Override
    public SerialModule getUsbSerial() {
        return (SerialModule) deviceManager.getDevice().getExModule(ExModuleType.USBSERIAL);
    }
}
