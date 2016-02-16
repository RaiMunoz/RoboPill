///MAKECU 2016 Hackathon

#include <TimeLib.h>
#include <U8glib.h>

#include <SPI.h>
#include <SD.h>

#include <Servo.h>

Servo myservo,myservo2;  // create servo object to control a servo
int pos = 0;    // variable to store the servo position


U8GLIB_SH1106_128X64 u8g(U8G_I2C_OPT_DEV_0|U8G_I2C_OPT_FAST);   // OLED screen
int mode = 0;//Oled screen mode

#define chipSelect 53///SD card CS pin
String text,TIMESTAMP,PILLDATA;
char receivedChar;

int pill_counter = 0;
const long interval = 100;  
unsigned long previousMillis = 0;

void splash() {///////////////////////////////start
    //u8g.drawBitmapP( 10, 12, 4, 28, pill);// draw logo
     //   u8g.drawBitmapP( 12, 40, 3, 24, makecu);// draw logo
    u8g.setFont(u8g_font_courB14);    // show text
    u8g.setFontRefHeightExtendedText();
    u8g.setFontPosTop();
    u8g.drawStr(5, 12, "MakeCU");//
    u8g.drawStr(5, 28, "Hackathon");//
    u8g.setFont(u8g_font_fixed_v0);
    u8g.setFontRefHeightExtendedText();
    u8g.setFontPosTop();
    u8g.drawStr(80, 45, "2016");//
}

void ScrnCheck()
{
  u8g.setPrintPos( 2, 4);
      u8g.print("MAKECU ");
          u8g.setPrintPos( 2, 16);
              u8g.print("Pills:"); u8g.print(pill_counter);
    u8g.setPrintPos( 2, 24);
              u8g.print("DATE"); u8g.print("");
                  u8g.setPrintPos( 2, 32);

}
void u8g_prepare(void) {
  u8g.setFont(u8g_font_6x10);
  u8g.setFontRefHeightExtendedText();
  u8g.setDefaultForegroundColor();
  u8g.setFontPosTop();
}

void draw(void) {
  u8g_prepare();
  switch(mode) {
    case 0: ScrnCheck(); break;///////home


  }
}
void redrawf(int m){
    u8g.firstPage();  
  do {
    if ( m == 1 ){ splash(); }else{ draw(); }
      } while( u8g.nextPage() );
}
void setup() {
    setTime(2,58,0,14,02,2016);/// intial start time
    u8g.setRot180();// rotate the screen
  redrawf(1);/// draw the boot screen
  
  Serial.begin(115200);
 Serial1.begin(9600);//bluetooth serial
  
  Serial.println("MAKECU");
    Serial1.println("MAKECU"); 
          
  SD.begin(chipSelect);
  
    myservo.attach(9);  // attaches the servo on pin 9 to the servo object
    myservo2.attach(8);
    pinMode(4,OUTPUT);// Led1
    pinMode(5,OUTPUT);// Led2
    pinMode(6,OUTPUT);// Piezo bjt pin
attachInterrupt(0,interrupt_count,FALLING);//IR sensor to detect
                 
  delay(2000);
}
int hr,minn,sc,M,D,Y;
String texttime;
void loop()
{
  hr=hour();  minn=minute();  sc=second();
  
  M=day();  D=month();  Y=year(); 
  
  // put your main code here, to run repeatedly:
   redrawf(1);/// redraw screen (for hackathon just redraw splash screen 0:draw screen 1)
   if (Serial1.available())
   {
  receivedChar = Serial1.read();
if(receivedChar == ';')
    {
PILLDATA=text;/// Store the text received from bluetooth
//M=text.substring(0,2).toInt();
  // Serial.println(M);
   
  if(text == "op1") {  
     DATALOG(0,"PILLS.txt",PILLDATA);
 //      Serial1.println("Pill #1");
       SERVODIS(1);
    }
     if(text == "op2") {
       DATALOG(0,"PILLS.txt",PILLDATA);
  //     Serial1.println("Pill #2");
       SERVODIS(0);
    }
text="";
  }
else {
text+=receivedChar;
      }
}
 // DATALOG(0,"PILLS.txt",PILLDATA);
 //TIMESTAMP = PILLDATA= "";  //clearr
}

void DATALOG(int LOG,String file,String dataString)///SD card log+serial print
{
  TIMESTAMP =String(hr)+":"+String(minn)+":"+String(sc);
    
  File dataFile = SD.open(file, FILE_WRITE);

  // if the file is available, write to it:
  if (dataFile) { 
    {      dataFile.print(String(M)+"/"+String(D)+"/20"+String(Y)+"\t");
    switch(LOG){
    case 0: { dataFile.print("HOME");dataFile.println("");
      dataFile.print("TIME\t #PILLS \t ");dataFile.println("");
      break;
            }
    }
    }
  
    dataFile.print(TIMESTAMP);dataFile.print("\t");
    dataFile.print(dataString);dataFile.println("");
    dataFile.close();
    
    Serial.print(TIMESTAMP);Serial.print("\t");
    Serial.print(dataString);Serial.println("");
  }
  // if the file isn't open, pop up an error:
  else {
   // Serial.println("Error opening .txt");
       Serial.print(TIMESTAMP);Serial.print("\t");
  Serial.print(dataString);Serial.println("");
  } 

}
void interrupt_count()
{
   unsigned long currentMillis = millis();

  if (currentMillis - previousMillis >= interval) {

    previousMillis = currentMillis;    // save the last time
  pill_counter = pill_counter + 1;
  return;
}
}

void SERVODIS(int i){
 
  if (i == 0){
    for (pos = 0; pos <= 90; pos += 5) { // goes from 0 degrees to 900 degrees
    // in steps of 1 degree
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
LEDBLINK();
  }        
  for (pos = 90; pos >= 0; pos -= 5) { // goes from 90 degrees to 0 degrees
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
  }

  PIEZOO(); 
   if (i == 1){ 
    for (pos = 180; pos >= 90; pos -= 5) { // goes from 180 degrees to 90 degrees
    myservo2.write(pos);              // tell servo to go to position in variable 'pos'
LEDBLINK();
  }
    for (pos = 90; pos <= 180; pos += 5) { // goes from 90 degrees to 180 degrees
    // in steps of 1 degree
    myservo2.write(pos);              // tell servo to go to position in variable 'pos'
    delay(15);                       // waits 15ms for the servo to reach the position
  }
  }
  
}
/// last minute hackthon stuff

void PIEZOO()
{
   digitalWrite(5,HIGH); 
  delay(50);
  digitalWrite(5,LOW);
  delay(20);
  digitalWrite(5,HIGH); 
  delay(50);
  digitalWrite(5,LOW); 
}
void LEDBLINK()
{
     digitalWrite(6,HIGH);digitalWrite(4,LOW);
     delay(15); // waits 15ms for the servo to reach the position
  digitalWrite(6,LOW);digitalWrite(4,HIGH);

}

