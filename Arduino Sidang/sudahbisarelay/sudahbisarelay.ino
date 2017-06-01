#include <SoftwareSerial.h>
#define RELAY_DIGITAL_PIN 8

SoftwareSerial SIM800(3, 2); //rx tx

String readString;

void setup() { 

  pinMode(RELAY_DIGITAL_PIN, OUTPUT);
  Serial.begin(19200);
  SIM800.begin(19200);  
  Serial.println("Locomoto Team");
  delay(1000);
}

void loop(){
  Serial.println("SubmitHttpRequest - started" );
  kirimData();
  Serial.println("SubmitHttpRequest - finished" );
  delay(1000);
}

void kirimData()
{
  SIM800.println("AT+CSQ"); // Mengecek Kualitas Sinyal
  ShowSerialData();

  SIM800.println("AT+CGATT?"); //Melihat Support tidaknya GPRS
  delay(100);
  ShowSerialData();

  SIM800.println("AT+SAPBR=3,1,\"CONTYPE\",\"GPRS\"");
  delay(1000);
  ShowSerialData();

  SIM800.println("AT+SAPBR=3,1,\"APN\",\"internet\"");
  delay(1000);
  ShowSerialData();

  SIM800.println("AT+SAPBR=2,1");
  delay(1000);
  ShowSerialData();
  
  SIM800.println("AT+SAPBR=1,1");
  delay(8000);
  ShowSerialData();

  SIM800.println("AT+HTTPINIT"); //Persiapan HTTP request
  delay(1000); 
  ShowSerialData();

  SIM800.println("AT+HTTPPARA=\"CID\",1");
  delay(6000);
  ShowSerialData();

  //pengiriman data ke alamat URL web server
  SIM800.println("AT+HTTPPARA=\"URL\",\"http://8rainy.com/device/Ymq0PM\"");
  //SIM800.print(random(0,100));
  delay(6000);
  ShowSerialData();

  SIM800.println("AT+HTTPACTION=0"); //submit request 
  delay(6000);
  ShowSerialData();
  
  SIM800.println("AT+HTTPREAD");// read the data from the website you access
  delay(5000);
  statusrelay();
  ShowSerialData();

  SIM800.println("AT+HTTPTERM");
  delay(6000);
  ShowSerialData(); 

  SIM800.println("");
  delay(100);

}

void statusrelay()
{
 String content = "";
// String RedState = content.substring();
 while(SIM800.available()!=0)
 {  
    //Serial.write(SIM800.read());
    content = content + String(char (SIM800.read()));
 }
    Serial.println(content);
 if(content.indexOf("0") > 1)
 {
   digitalWrite(RELAY_DIGITAL_PIN, HIGH);
 }
 else if (content.indexOf("1") > 0)
 {
   digitalWrite(RELAY_DIGITAL_PIN, LOW);
 }
 content = "";
}

void ShowSerialData()
{
  while(SIM800.available()!=0)
    Serial.write(char (SIM800.read()));
}
