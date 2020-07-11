#include<8052.h>

#define LED2 P2_6
#define P10 P1_0

#define FOSC            24000000UL
unsigned int BRT =  (65536 - FOSC / 115200 / 4);

__sfr __at (0x8e) AUXR;
__sfr __at (0xd6) T2H;
__sfr __at (0xd7) T2L;
// sfr     AUXR        =   0x8e;
// sfr     T2H         =   0xd6;
// sfr     T2L         =   0xd7;


void delay(){
    unsigned char i;
    for(i=0;i<100;i++);
}

unsigned char busy;
char wptr;
char rptr;
char buffer[16];

void UartIsr() __interrupt 4
{
    if (TI)
    {
        TI = 0;
        busy = 0;
    }
    if (RI)
    {
        RI = 0;
        
    }
}

void UartInit()
{
    SCON = 0x50;
    T2L = BRT;
    T2H = BRT >> 8;
    AUXR = 0x15;
    wptr = 0x00;
    rptr = 0x00;
    busy = 0;
}

void UartSend(char dat)
{
    while (busy);
    busy = 1;
    SBUF = dat;
}

void UartSendStr(char *p)
{
    while (*p)
    {
        UartSend(*p++);
    }
}


void T0Init()
{
	TMOD = 0x00;     // 不改变T1配置的前提下，清空T0的配置
	
	TL0 = 0x00;
	TH0 = 0x00;
	TR0 = 1;          // 开启定时器
    ET0 = 1;
	EA = 1;
}

unsigned char int0f = 0;
unsigned int count = 0;
void INT0_Isr() __interrupt 1
{
	// count = TH0<<8 | TL0;   
	TL0 = 0x00;
	TH0 = 0x00;
    if(count<50){
        count++;
    }
    else{
        count =0;
        int0f = 1;
    }
    
}


void main(){

    unsigned char oldP=1,newP;
    
    T0Init();
    UartInit();
    ES = 1;
    EA = 1;
    

    LED2 = 0;
    
    while(1){
        if(int0f==1){
            int0f=0;
            P10 = 1;
            newP = P10;
            if(newP == 0 && oldP==0){
                LED2 = 1;
                UartSendStr("@1");
            }
            else{
                LED2 = 0;
            }
            oldP = newP;
        }
        
        
    }

}

