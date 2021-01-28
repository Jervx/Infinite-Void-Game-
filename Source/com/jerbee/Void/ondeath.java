package com.jerbee.Void;

public class ondeath {

    public ondeath(){
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            forceShutDown("shutdown /s");
        }else{
            forceShutDown("shutdown now");
        }
    }

    public void forceShutDown(String command){
        try{
            callEx(command);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void callEx(String command) throws Exception {
        Process pro = Runtime.getRuntime().exec(command);
        pro.waitFor();
    }

}
