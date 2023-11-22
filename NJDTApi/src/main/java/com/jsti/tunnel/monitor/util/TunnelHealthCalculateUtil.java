package com.jsti.tunnel.monitor.util;
 
import static com.jsti.tunnel.monitor.enumeration.TunnelHealthState.A;
import static com.jsti.tunnel.monitor.enumeration.TunnelHealthState.B;
import static com.jsti.tunnel.monitor.enumeration.TunnelHealthState.C;
import static com.jsti.tunnel.monitor.enumeration.TunnelHealthState.D;
import static com.jsti.tunnel.monitor.enumeration.TunnelHealthState.E;

import java.math.BigDecimal;

public class TunnelHealthCalculateUtil {
	
	//private static final Logger logger = LoggerFactory.getLogger(TunnelHealthCalculateUtil.class);
	  
    public static BigDecimal α = new BigDecimal("0.8");
    public static BigDecimal β = new BigDecimal("0.2");
    
    public final static int precision =6;
    
    
    //常量矩阵(5*4)
    private static  BigDecimal[][] constMatrix = {
            {new BigDecimal("0.8"),new BigDecimal(0),new BigDecimal(0),new BigDecimal(0)},
            {new BigDecimal("0.5"),new BigDecimal("0.5"),new BigDecimal(0),new BigDecimal(0)},
            {new BigDecimal(0),new BigDecimal("0.5"),new BigDecimal("0.5"),new BigDecimal(0)},
            {new BigDecimal(0),new BigDecimal(0),new BigDecimal("0.5"),new BigDecimal("0.5")},
            {new BigDecimal(0),new BigDecimal(0),new BigDecimal(0),new BigDecimal("1.2")}
    };

    //均分法计算列矩阵k(4*1)
    private static BigDecimal[][] kMatrix(BigDecimal L){
    	
        BigDecimal[][] k = new BigDecimal[4][1];
        
        BigDecimal avgValue = L.divide(new BigDecimal(5),precision,BigDecimal.ROUND_HALF_UP);
        k[0][0] = avgValue;
        k[1][0] = avgValue.multiply(new BigDecimal(2));
        k[2][0] = avgValue.multiply(new BigDecimal(3));
        k[3][0] = avgValue.multiply(new BigDecimal(4));
        return k;
    }

    //计算列矩阵a(5行*1列)
    static BigDecimal[] get_a(BigDecimal L){ 
        BigDecimal[][] k = kMatrix(L);//4行*1列 矩阵
        BigDecimal[] a = new BigDecimal[5];
        for(int i=0;i<5;i++){
            a[i] = constMatrix[i][0].multiply(k[0][0]).add(constMatrix[i][1].multiply(k[1][0])).add(constMatrix[i][2].multiply(k[2][0])).add(constMatrix[i][3].multiply(k[3][0]));
        }
        return a;
    }

    //等级向量
    private static final BigDecimal[][] G = {
            {new BigDecimal(A.getLevel()), new BigDecimal(B.getLevel()),new BigDecimal(C.getLevel()),new BigDecimal(D.getLevel()),new BigDecimal(E.getLevel())}
    };

    @SuppressWarnings("unused")
	private static void aArraycheck(BigDecimal[] a) throws Exception {
        if(a==null || a.length!=5){
            throw new Exception("传入的检测指标值不正确");
        }
        for (int i = 0; i < a.length; i++) {
            if(i>0){
                if(a[i].compareTo(a[i-1])==-1) {
                    throw new RuntimeException(new Exception("传入的检测指标值不正确"));
                }
            }
        }
    }

    private static BigDecimal μA(String xString,BigDecimal[] a) {
        BigDecimal x = new BigDecimal(xString);
        BigDecimal a1 = a[0],a2 = a[1];
        if(x.compareTo(a1)==-1) return new BigDecimal(1);
        else if(x.compareTo(a2)==1) return new BigDecimal(0);
        else {
            BigDecimal  dividend =  x.subtract(a2);//被除数 被除数/除数
            BigDecimal  divisor = a1.subtract(a2);//除数
            return dividend.divide(divisor,precision,BigDecimal.ROUND_HALF_UP);
        }
    }

    private static BigDecimal μB(String xString,BigDecimal[] a){
        BigDecimal x = new BigDecimal(xString);
        BigDecimal a1 = a[0],a2 = a[1], a3 = a[2];
        if( (x.compareTo(a1)==-1) || (x.compareTo(a3)==1)) return new BigDecimal(0);
        else if(x.compareTo(a2)!=1){
            BigDecimal dividend = x.subtract(a1);
            BigDecimal divisor = a2.subtract(a1);
            return dividend.divide(divisor,precision,BigDecimal.ROUND_HALF_UP);
        }
        else {
            BigDecimal  dividend = x.subtract(a3);
            BigDecimal divisor  = a2.subtract(a3);
            return dividend.divide(divisor,precision,BigDecimal.ROUND_HALF_UP);
        }
    }

    private static BigDecimal μC(String xString,BigDecimal[] a) {
        BigDecimal x = new BigDecimal(xString);
        BigDecimal a2 = a[1], a3 = a[2],a4 = a[3];
        if( (x.compareTo(a2)==-1) || (x.compareTo(a4)==1)) return new BigDecimal(0);
        else if(x.compareTo(a3)!=1) {
            BigDecimal dividend = x.subtract(a2);
            BigDecimal divisor = a3.subtract(a2);
            return dividend.divide(divisor,precision,BigDecimal.ROUND_HALF_UP);
        }
        else {
            BigDecimal  dividend = x.subtract(a4);
            BigDecimal  divisor = a3.subtract(a4);
            return dividend.divide(divisor,precision,BigDecimal.ROUND_HALF_UP);
        }
    }

    private static BigDecimal μD(String xString,BigDecimal[] a) { 
        BigDecimal x = new BigDecimal(xString);
        BigDecimal a3 = a[2],a4 = a[3],a5 = a[4];
        if((x.compareTo(a3)==-1)||(x.compareTo(a5)==1)) return new BigDecimal(0);
        else if(x.compareTo(a4)!=1) {
            BigDecimal dividend = x.subtract(a3);
            BigDecimal divisor = a4.subtract(a3);
            return dividend.divide(divisor,precision,BigDecimal.ROUND_HALF_UP);
        }
        else{
            BigDecimal dividend =  x.subtract(a5); 
            BigDecimal divisor =  a4.subtract(a5); 
            return dividend.divide(divisor,precision,BigDecimal.ROUND_HALF_UP);
        }
    }

    private static BigDecimal μE(String xString,BigDecimal[] a) {
        BigDecimal x = new BigDecimal(xString);
        BigDecimal a4 = a[3],a5 = a[4];
        if(x.compareTo(a4)!=1) return new BigDecimal(0);
        else if(x.compareTo(a5)==1) return new BigDecimal(1);
        else{
            BigDecimal dividend = x.subtract(a4);
            BigDecimal divisor = a5.subtract(a4);
            return dividend.divide(divisor,precision,BigDecimal.ROUND_HALF_UP);
        }
    }


    private static BigDecimal[][] getQ(String xString,BigDecimal[] a) throws Exception {
        //aArraycheck((a));
        BigDecimal[][] Q = {
                {μA(xString,a)},
                {μB(xString,a)},
                {μC(xString,a)},
                {μD(xString,a)},
                {μE(xString,a)}
        };
        return Q;
    }

    public static BigDecimal getR(String xString,BigDecimal L) throws Exception {
    	BigDecimal[] a = get_a(L); 
        BigDecimal[][] Q = getQ(xString,a);//G*Q ->行矩阵*列举证 
        if(Q.length!=G[0].length) throw new Exception("Matrix inner dimensions must agree");
        int length = Q.length;
        BigDecimal R = new BigDecimal(0);
        for (int i = 0; i < length; i++) {
            R = R.add(G[0][i].multiply(Q[i][0]));
        }
        return R;
    }
    


}
