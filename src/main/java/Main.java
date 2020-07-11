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

    public static void main(String[] args) throws PortInUseException {

        ArrayList<String> list = SerialPortManager.findPorts();
        System.out.println("start");
        System.out.println("com list is:");
        System.out.println(list);
        for(int i = 0;i<list.size();i++){

            if(list.get(i).indexOf("ttyUSB")!=-1){
                System.out.println("choose :"+list.get(i));
                SerialPort serialPort = SerialPortManager.openPort(list.get(i),115200);
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
                break;
            }

        }



    }
}
