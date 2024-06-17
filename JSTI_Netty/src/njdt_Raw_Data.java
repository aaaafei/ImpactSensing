public class njdt_Raw_Data {
    private  String clientIP;
    private  String contents;
    public njdt_Raw_Data(String pCLIENTIP,String pCONTENTS){
         clientIP=pCLIENTIP;
         contents=pCONTENTS;
    }
    public boolean isValid(){
        boolean pValid=true;
        try{
            if (clientIP==null||contents==null){
                throw new Exception("string is NULL");
            }
        }catch(Exception e){
            pValid=false;
        }
        return pValid;
    }
    public String getClientIP(){
        String tmp="0.0.0.0";   // Example is  /221.178.126.85:42515
        tmp=clientIP.substring(1,clientIP.indexOf(":"));
        return tmp;
    }
    public String getClientPort(){
        String tmp="99999";   // Example is  /221.178.126.85:42515
        tmp=clientIP.substring(clientIP.indexOf(":")+1);
        return tmp;
    }
    public String getClientIMEI(){
        String tmp="999999999999999999";   // Example is  *SCOR,OM,862205058920614,S6,0,1,0,0,0,0,1,28,0,1#
        try {
            String[] tmps = contents.split(",");
            tmp = tmps[2];
            if(tmp.length()>=18)throw new Exception("too long");
        }catch (Exception e){
               tmp="--invalid IMEI--";
        }
        return tmp;
    }
    public String getContents(){
        String tmp=contents;   // Example is  *SCOR,OM,862205058920614,S6,0,1,0,0,0,0,1,28,0,1#
        try{
           if(tmp.length()>=80)throw new Exception("too long");
        }catch (Exception e){
            tmp="---invalid contents---";
        }
        return tmp;
    }
    public String getCatalog(){
        String tmp=contents;   // Example is  *SCOR,OM,862205058920614,S6,0,1,0,0,0,0,1,28,0,1#
        try {
            String[] tmps = contents.split(",");
            tmp = tmps[3];
            if(tmp.length()>=4)throw new Exception("too long");
        }catch (Exception e){
            tmp="--";
        }
        return tmp;
    }
    public String getCatalogVal(){
        String tmp=contents;   // Example is  *SCOR,OM,862205058920614,S6,0,1,0,0,0,0,1,28,0,1#
        try {
            String[] tmps=contents.split(",");
            tmp=tmps[tmps.length-1];
            if(tmp.length()>1 && tmp.indexOf("#")>0){
                tmp=tmp.substring(0,1);
            }
            if(tmp.length()>=2)throw new Exception("too long");
        }catch (Exception e){
            tmp="-";
        }
        return tmp;
    }

    public String getVoltage(){
        String tmp="";   // Example is  *SCOR,OM,862205058920614,S6,0,1,0,0,0,0,1,28,0,1#
        try{
            if(contents.indexOf("H0")>0){
                String[] tmps=contents.split(",");
                tmp=tmps[5];
            }
            if(contents.indexOf("Q0")>0){
                String[] tmps=contents.split(",");
                tmp=tmps[4];
            }
        }catch (Exception e){
            tmp="---";
        }
        return tmp;
    }
    public String getSignal(){
        String tmp="";   // Example is  *SCOR,OM,862205058920614,S6,0,1,0,0,0,0,1,28,0,1#
        try{
            if(contents.indexOf("H0")>0){
                String[] tmps=contents.split(",");
                tmp=tmps[6];
            }
            if(contents.indexOf("S6")>0){
                String[] tmps=contents.split(",");
                tmp=tmps[11];
            }
            if(contents.indexOf("Q0")>0){
                String[] tmps=contents.split(",");
                tmp=tmps[tmps.length-1];
                if(tmp.length()>1 && tmp.indexOf("#")>0){
                    tmp=tmp.substring(0,2);
                }
            }
        }catch (Exception e){
            tmp="--";
        }

        return tmp;
    }

    public static void main(String[] args){
        njdt_Raw_Data  n=new njdt_Raw_Data("/221.178.126.159:8902","*SCOR,OM,862205058920614,Q0,369,0,29#");
        if(n.isValid()){
            System.out.println(n.getClientIP());
            System.out.println(n.getClientPort());
            System.out.println(n.getClientIMEI());
            System.out.println(n.getCatalog());
            System.out.println(n.getCatalogVal());
            System.out.println(n.getContents());
            System.out.println(n.getSignal());
        }
    }

}
