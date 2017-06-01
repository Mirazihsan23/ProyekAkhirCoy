#include <SoftwareSerial.h>
#include "TinyGPS++.h"
#define RELAY_DIGITAL_PIN 8

SoftwareSerial Serial_sim800(3, 2); //rx tx
String apiKey = "serial=Ymq0PM";
String readString;


float lat,lng;
SoftwareSerial serial_connection(11, 10); //RX=pin 10, TX=pin 11
TinyGPSPlus gps;//This is the GPS object that will pretty much do all the grunt work with the NMEA data

void setup(){

  pinMode(RELAY_DIGITAL_PIN, OUTPUT);

  Serial.begin(9600);
  Serial.println("Locomoto Team");
  Serial_sim800.begin(9600);  
  delay(1000);
  serial_connection.begin(9600);//This opens up communications to the GPS
  Serial.println("GPS Start");//Just show to the monitor that the sketch has started
}


void loop(){
  
  Serial.println("SubmitHttpRequest - started" );
  gpsdat();
  Serial.println("SubmitHttpRequest - finished" );
  delay(1000);
  
}

void gpsdat()
{
  while(serial_connection.available())//While there are characters to come from the GPS
  {
    gps.encode(serial_connection.read());//This feeds the serial NMEA data into the library one char at a time
  }
  if(gps.location.isUpdated())//This will pretty much be fired all the time anyway but will at least reduce it to only after a package of NMEA data comes in
  {
    kirimData(gps.location.lat(), gps.location.lng());
  }
  
}
 
void kirimData(float lat, float lng)
{         
  gpsdat();
     String posStr = apiKey;
         posStr += "&latlng=";
         posStr += String(lat, 6);
         posStr += ", ";
         posStr += String(lng, 6);
  delay(500);
  
  Serial_sim800.println("AT+CSQ"); // Mengecek Kualitas Sinyal
  ShowSerialData();

  Serial_sim800.println("AT+CGATT?"); //Melihat Support tidaknya GPRS
  delay(100);
  ShowSerialData();

  Serial_sim800.println("AT+SAPBR=3,1,\"CONTYPE\",\"GPRS\"");
  delay(1000);
  ShowSerialData(); 

  Serial_sim800.println("AT+SAPBR=3,1,\"APN\",\"internet\"");
  delay(1000);
  ShowSerialData();

  Serial_sim800.println("AT+SAPBR=2,1");
  delay(1000);
  ShowSerialData();
  
  Serial_sim800.println("AT+SAPBR=1,1");
  delay(8000);
  ShowSerialData();

  Serial_sim800.println("AT+HTTPINIT"); //Persiapan HTTP request
  delay(1000); 
  ShowSerialData();

  Serial_sim800.println("AT+HTTPPARA=\"CID\",1");
  delay(6000);
  ShowSerialData();
         
  //pengiriman data ke alamat URL web server
  Serial.println(posStr);
  Serial_sim800.println("AT+HTTPPARA=\"URL\",\"http://8rainy.com/device/serial/update?"+posStr+"\"");
  Serial.println("AT+HTTPPARA=\"URL\",\"http://8rainy.com/device/serial/update?"+posStr+"\"");
  //Serial_sim800.print(random(0,100));
  delay(6000);
  ShowSerialData();

  Serial_sim800.println("AT+HTTPACTION=0"); //submit request 
  delay(6000);
  ShowSerialData();
  
  Serial_sim800.println("AT+HTTPREAD");// read the data from the website you access
  delay(5000);
  statusrelay();
  ShowSerialData();

  Serial_sim800.println("AT+HTTPTERM");
  delay(6000);
  ShowSerialData(); 

  Serial_sim800.println("");
  delay(100);
}

void ShowSerialData()
{
  while(Serial_sim800.available()!=0)
    Serial.write(char (Serial_sim800.read()));
}

void statusrelay()
{
 String content = "";
// String RedState = content.substring();
 while(Serial_sim800.available()!=0)
 {  
    // Serial.write(Serial_sim800.read());
    content = content + String(char (Serial_sim800.read()));
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
