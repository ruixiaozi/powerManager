import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Title: Main 类 <br/>
 * Description:  <br/>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public class Main {

    private static final long SLEEP_TIME = 1000*60*2;

    public static void main(String[] args) throws PortInUseException {

        while (true) {
            ArrayList<String> list = SerialPortManager.findPorts();
            System.out.println("start");
            System.out.println("com list is:");
            System.out.println(list);
            String port = null;
            for(int i = 0;i<list.size();i++){

                if(list.get(i).indexOf("ttyUSB")!=-1){
                    port = list.get(i);
                    break;
                }

            }
            System.out.println("choose :"+port);
            SerialPort serialPort = SerialPortManager.openPort(port,115200);
            SerialPortManager.addListener(serialPort, new SerialPortManager.DataAvailableListener() {
                @Override
                public void dataAvailable() {
                    byte[] data = null;
                    try {
                        if (serialPort == null) {
                            System.out.println("串口对象为空，监听失败！");
                        } else {
                            // 读取串口数据
                            int re = SerialPortManager.readFromPort(serialPort);
                            //System.out.print(re);
                            if(re == '1'){

                                //关机
                                System.out.println(CommandUtil.run("shutdown -h now"));
                                System.exit(0);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 发生读取错误时显示错误信息后退出系统
                        System.exit(0);
                    }
                }
            });


            //间隔2分钟
            System.out.println("Infom:sleep 2 minutes...");
            try {
                Thread.sleep(SLEEP_TIME);
                SerialPortManager.closePort(serialPort);
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Error:sleep error");
                System.out.println("ErrCause:" + e.getCause());
                System.out.println("ErrMsg:" + e.getMessage());
            }
        }


    }
}
