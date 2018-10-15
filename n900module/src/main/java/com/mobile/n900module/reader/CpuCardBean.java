package com.mobile.n900module.reader;

import com.newland.mtype.util.ISOUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CpuCardBean {
    private byte [][] source = new byte[6][];
    public Map statuscode = new HashMap();
    //MDF
    //IC卡发行标识
    public byte ic_issuer[];
    //IC卡应用类型标识
    public  byte ic_use_type;
    //IC卡应用版本
    public  byte ic_use_ver;
    //IC卡应用序列号
    public  byte ic_no[];
    //IC卡应用序列号字符串形式
    public String ic_no_s;
    //IC卡起始日期YYYYMMDD
    public  byte ic_start_date[];
    //IC卡截止日期YYYYMMDD
    public  byte ic_end_date[];
    public  byte ic_ins_ver;
    public  byte pin_status;
    //员工号
    public  byte employee_no;
    //员工号符串形式
    public String employee_no_s;
    //员工密码，BCD，4个字节
    public  byte [] employee_pin = new byte[2];
    //员工卡
    public  byte restrict_status;

    private  RestrictC restrictinfo;

    public class RestrictC{
        public byte [] restrict_oilcode;				//限油品编码
        /**
         * 1 限省， 2限省和市，3限上级省，4限油站
         */
        //是否限省 0xff代表不限制
        public byte  restrict_provincestatus;
        //源数据u
        public byte  orign[];

        RestrictC(byte source[])
        {
            orign = Arrays.copyOf(source, source.length);
        }

        public void parseData() {}

    }

    public byte[][] getSource() {
        return source;
    }

    public void setSource(byte[][] source) {
        this.source = source;
    }
    private boolean parseMdf(byte source [])
    {
        boolean ret = true;
        if(source.length < 50)
        {
            ret = false;
            //读卡异常
            statuscode.put("result", "001");
        }
        else {
            //IC卡应用发行者标识
            ic_issuer = Arrays.copyOfRange(source, 21, 29);
            //IC卡应用类型标识
            ic_use_type = source[29];
            //IC卡应用版本
            ic_use_ver = source[30];
            //IC卡应用序列号
            ic_no = Arrays.copyOfRange(source, 31, 41);
            ic_no_s = byte2bcd(ic_no);
            //IC卡起始日期YYYYMMDD
            ic_start_date = Arrays.copyOfRange(source, 41, 45);
            //IC卡截止日期YYYYMMDD
            ic_end_date = Arrays.copyOfRange(source, 45, 49);
            ic_ins_ver = source[49];
        }
        return  ret;
    }

    /**
     * 解析第三条命令
     * @param source
     * @return
     */
    private boolean parseCmd2(byte source[]) {
        return true;
    }

    /**
     * 解析第四条命令
     * @param source
     * @return
     */
    private boolean parseCmd3(byte[] source)
    {
        boolean ret = false;
        if(source.length >= 4)
        {
            pin_status=source[0];
            employee_no=source[1];
            employee_pin[0]=source[2];
            employee_pin[1]=source[3];
            restrict_status=0;  //员工卡
        }
        else {
            ret = false;
        }

        return ret;
    }

    /**
     * 解析第五条命令
     * @param source
     */
    private void parseCmd4(byte[] source)
    {
        restrictinfo = new RestrictC(source);

    }

    /**
     * 解析第六条命令
     * @param source
     */
    private void parseCmd5(byte[] source) {
        employee_no = source[1];
        employee_no_s = byte2bcd(new byte[]{employee_no});
        employee_pin = Arrays.copyOfRange(source, 2, 4);
    }

    public boolean parseTestSource(){
        boolean ret = false;
        for(int i = 0; i < source.length; i++)
        {
            switch(i)
            {
                case 0:
                    break;
                case 1:
                    ret = parseMdf(source[i]);
                    break;
                case 2:
                    break;
                case 3:
                    ret = parseTestCmd(source[i]);
                    break;
                case 4:
                    parseCmd4(source[i]);
                    break;
                case 5:
                    parseCmd5(source[i]);
                    break;
                default:
                    break;
            }
        }

        return  ret;
    }

    private boolean parseTestCmd(byte[] source){
        boolean ret = false;
        if (source.length>4){
            ic_no_s= ISOUtils.hexString(source).substring(21,40);
            ret=true;
        }

        return ret;
    }

    /**
     *源数据进行解析
     * @return
     */
    public boolean parseSource()
    {
        boolean ret = false;
        for(int i = 0; i < source.length; i++)
        {
            switch(i)
            {
                case 0:
                    break;
                case 1:
                    ret = parseMdf(source[i]);
                    break;
                case 2:
                    break;
                case 3:
                    ret = parseCmd3(source[i]);
                    break;
                case 4:
                    parseCmd4(source[i]);
                    break;
                case 5:
                    parseCmd5(source[i]);
                    break;
                default:
                    break;
            }
        }

        return  ret;
    }

    private String byte2bcd(byte[] bcd) {
        StringBuilder temp = new StringBuilder(bcd.length * 2);
        for (byte aBcd : bcd) {
            temp.append((byte) ((aBcd & 0xf0) >>> 4));
            temp.append((byte) (aBcd & 0x0f));
        }
        return temp.charAt(0) == 0 ? temp.substring(1) : temp.toString();
    }
}
