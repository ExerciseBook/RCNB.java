package cn.Skyfalls.RCNB;

import cn.Skyfalls.RCNB.Exceptions.NotEnoughNBException;
import cn.Skyfalls.RCNB.Exceptions.RCNBOverflowException;

import java.util.ArrayList;
import java.util.List;

public class RCNBEncoder {
    private final static String cr = "rRŔŕŖŗŘřƦȐȑȒȓɌɍ";
    private final static String cc = "cCĆćĈĉĊċČčƇƈÇȻȼ";
    private final static String cn = "nNŃńŅņŇňƝƞÑǸǹȠȵ";
    private final static String cb = "bBƀƁƃƄƅßÞþ";

    private final static short sr = (short) cr.length();
    private final static short sc = (short) cc.length();
    private final static short sn = (short) cn.length();
    private final static short sb = (short) cb.length();
    private final static short src = (short) (sr * sc);
    private final static short snb = (short) (sn * sb);
    private final static short scnb = (short) (sc * snb);

    private static short _div(int a, int b){
        return (short) Math.floor(a / b);
    }
    public static byte encodeByte(short i) throws RCNBOverflowException{
        if(i > 0xFF) throw new RCNBOverflowException();
        if(i > 0x7F){
            short value = (short) (i & 0x7F);
            return (byte) (cn.charAt(_div(value, sb)) + cb.charAt(value % sb));
        }
        return (byte) (cr.charAt(_div(i, sc)) + cc.charAt(i % sc));
    }
    public static char[] encodeShort(int value) throws RCNBOverflowException{
        if(value > 0xFFFF) throw new RCNBOverflowException();
        boolean reverse = false;
        int i = value;
        if(i > 0x7FFF){
            reverse = true;
            i = i & 0x7FFF;
        }
        char chars[] = {cr.charAt(_div(i, scnb)), cc.charAt(_div(i % scnb, snb)), cn.charAt(_div(i % snb, sb)), cb.charAt((i % sb))};
        if (reverse) {
            char results[]={chars[2],chars[3],chars[0],chars[1]};
            return results;
        }
        return chars;
    }

    public static String encode(String s) throws RCNBOverflowException{
        StringBuilder sb = new StringBuilder();
        byte _bytes[] = s.getBytes();
        List<Short> bytes=new ArrayList<Short>(_bytes.length);
        for(int i=0;i<_bytes.length;i++){
            bytes.add((short) (_bytes[i]& 0xFF));
        }
        //encode every 2 bytes
        for (int i = 0; i < (bytes.size() >> 1); i++) {
            System.out.println(bytes.get(i));
            sb.append(new String(encodeShort(((bytes.get(i * 2) << 8)) | bytes.get(i * 2 + 1))));
        }
        if ((bytes.size() & 1)== 1) sb.append(encodeByte(bytes.get(bytes.size() - 1)));
        return sb.toString();
    }
}
