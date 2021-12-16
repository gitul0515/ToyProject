//1 
#define S0 3
#define S1 4
#define S2 5
#define S3 6
#define sensorOut 2
//2
#define S0_2 8
#define S1_2 9
#define S2_2 10
#define S3_2 11
#define sensorOut_2 7

//3
#define S0_3 A1
#define S1_3 A2
#define S2_3 A3
#define S3_3 A4
#define sensorOut_3 A0

int data=0;

int redFrequency = 0;
int greenFrequency = 0;
int blueFrequency = 0;
int delaysec = 10;          // 입력 받은 값들을 로드시킬 delay 값​


int redFrequency_2 = 0;
int greenFrequency_2 = 0;
int blueFrequency_2 = 0;

int redFrequency_3 = 0;
int greenFrequency_3 = 0;
int blueFrequency_3 = 0;

void setup() {

pinMode(S0, OUTPUT);
pinMode(S1, OUTPUT);
pinMode(S2, OUTPUT);
pinMode(S3, OUTPUT);

pinMode(S0_2, OUTPUT);
pinMode(S1_2, OUTPUT);
pinMode(S2_2, OUTPUT);  
pinMode(S3_2, OUTPUT);

pinMode(S0_3, OUTPUT);
pinMode(S1_3, OUTPUT);
pinMode(S2_3, OUTPUT);  
pinMode(S3_3, OUTPUT);

//Setting the sensorOut as an input
pinMode(sensorOut, INPUT);
pinMode(sensorOut_2, INPUT);
pinMode(sensorOut_3, INPUT);



// Setting frequency scaling to 20%
digitalWrite(S0,HIGH);
digitalWrite(S1,LOW);

digitalWrite(S0_2,HIGH);
digitalWrite(S1_2,LOW);

digitalWrite(S0_3,HIGH);
digitalWrite(S1_3,LOW);
// Begins serial communication 

Serial.begin(115200);

}


void loop() {
// Setting RED (R) filtered photodiodes to be read

digitalWrite(S2,LOW);
digitalWrite(S3,LOW);


// Reading the output frequency

redFrequency = pulseIn(sensorOut, LOW);

// Printing the RED (R) value

delay(delaysec);

// Setting GREEN (G) filtered photodiodes to be read

digitalWrite(S2,HIGH);
digitalWrite(S3,HIGH);
greenFrequency = pulseIn(sensorOut, LOW);


// Printing the GREEN (G) value 

delay(delaysec);

// Setting BLUE (B) filtered photodiodes to be read

digitalWrite(S2,LOW);
digitalWrite(S3,HIGH);

// Reading the output frequency

blueFrequency = pulseIn(sensorOut, LOW);
delay(500);



digitalWrite(S2_2,LOW);
digitalWrite(S3_2,LOW);
redFrequency_2 = pulseIn(sensorOut_2, LOW);
delay(delaysec);

digitalWrite(S2_2,HIGH);
digitalWrite(S3_2,HIGH);
greenFrequency_2 = pulseIn(sensorOut_2, LOW);
delay(delaysec);

digitalWrite(S2_2,LOW);
digitalWrite(S3_2,HIGH);
blueFrequency_2 = pulseIn(sensorOut_2, LOW);
delay(delaysec);


digitalWrite(S2_3,LOW);
digitalWrite(S3_3,LOW);
redFrequency_3 = pulseIn(sensorOut_3, LOW);
delay(delaysec);


digitalWrite(S2_2,LOW);
digitalWrite(S3_2,HIGH);
greenFrequency_3 = pulseIn(sensorOut_3, LOW);
delay(delaysec);

digitalWrite(S2_2,LOW);
digitalWrite(S3_2,HIGH);
blueFrequency_3 = pulseIn(sensorOut_3, LOW);
delay(delaysec);



// Reading the output frequency

// Serial.println("detected 1");
// Serial.print(redFrequency); 
// Serial.print(",");  
// Serial.print(greenFrequency);
// Serial.print(",");
// Serial.println(blueFrequency);//


 Serial.println("detected 2");
 Serial.print(redFrequency_2); 
 Serial.print(",");  
 Serial.print(greenFrequency_2);
 Serial.print(",");
 Serial.println(blueFrequency_2);//

//
// Serial.println("detected 3");
////
// Serial.print(redFrequency_3);
//  Serial.print(",");  
//  Serial.print(greenFrequency_3);
//  Serial.print(",");
// Serial.println(blueFrequency_3);

//
// Serial.println("detected 1");
//  Serial.print(redFrequency);
//  Serial.print(",");
//
//  Serial.print(greenFrequency);
//  Serial.print(",");
//  Serial.println(blueFrequency);
// Printing the BLUE (B) value 


//
//if(redFrequency <200 && greenFrequency < 200 && blueFrequency < 200){
//  Serial.println("detected 1");
//  Serial.print(redFrequency);
//  Serial.print(",");
//
//  Serial.print(greenFrequency);
//  Serial.print(",");
//  Serial.println(blueFrequency);
//
//  if(redFrequency < 100 && ((blueFrequency>75) && (blueFrequency<100))){
//    Serial.println("Red Detect");
//  }
//  else if(redFrequency < 60 && blueFrequency <70){
//     Serial.println("BLUE Detect");
//
//  }
//
//}


if(redFrequency <110 && greenFrequency < 150 && blueFrequency < 110){
//  Serial.println("detected 1");
//  Serial.print(redFrequency);
//  Serial.print(",");
//
//  Serial.print(greenFrequency);
//  Serial.print(","); 
//  Serial.println(blueFrequency);

  if( redFrequency<60 && greenFrequency > 100){
//    Serial.println("Red Detect");
    Serial.println("1");
    //첫 번째 플레이어 + 첫 번째 버튼: 1 송신

  }
  else if(blueFrequency < 75 && greenFrequency <75){
    // Serial.println("BLUE Detect");
    Serial.println("4");

   // 두 번째 플레이어 + 첫 번째 버튼: 4 송신

  }
}



if(redFrequency_2 <255 && greenFrequency_2<255  && blueFrequency_2 < 255){
//  Serial.println("detected 2");
//  Serial.print(redFrequency_2);
//  Serial.print(",");
//
//  Serial.print(greenFrequency_2);
//  Serial.print(",");
//  Serial.println(blueFrequency_2);

  if(redFrequency_2 <150){
//    Serial.println("Red Detect");
    Serial.println("2");

    //첫 번째 플레이어 + 두 번째 버튼: 2 송신

  }
  else if(greenFrequency_2 <165){
     //Serial.println("BLUE Detect");
         Serial.println("5");

    //두 번째 플레이어 + 두 번째 버튼: 5 송신


  }

}
//
if(redFrequency_3 <200 && greenFrequency_3 < 200 && blueFrequency_3 < 200){
//  Serial.println("detected 3");
//  Serial.print(redFrequency_3);
//  Serial.print(",");
//
//  Serial.print(greenFrequency_3);
//  Serial.print(",");
//  Serial.println(blueFrequency_3);

//  if(redFrequency_3<140 && blueFrequency_3<140){
//    //Serial.println("Red Detect");
//        Serial.println("3");
//
//    //첫 번째 플레이어 + 세 번째 버튼: 3 송신
//
//    
//  }
   if(blueFrequency_3 < 180 && greenFrequency_3 <180){
     //Serial.println("BLUE Detect");
   //두 번째 플레이어 + 세 번째 버튼: 6 송신
       Serial.println("6");


  }
}



delay(delaysec);





//if (redFrequency < 30 && greenFrequency < 30 && blueFrequency < 30) {
//
//Serial.println(" - WHITE detected!");
//
//}
//
//if (redFrequency > 95 && greenFrequency > 95) {
//
//Serial.println(" - BLACK detected!");
//
//}
//
//if (redFrequency > 70 && greenFrequency > 70 && blueFrequency > 30 && blueFrequency < 60) {
//
//Serial.println(" - BLUE detected!");
//
//}
//
//if (redFrequency > 44 && redFrequency < 70 && greenFrequency > 68 && blueFrequency > 40 && blueFrequency < 60) {
//
//Serial.println(" - PURPLE detected!");
//
//}
//
//if (redFrequency > 13 && redFrequency < 35 && greenFrequency > 45 && greenFrequency < 65 && blueFrequency > 32 && blueFrequency < 45) {
//
//Serial.println(" - PINK detected!");
//
//}
//
//delay(delaysec);
//
//
//if (redFrequency < 30 && greenFrequency < 30 && blueFrequency < 30) {
//
//Serial.println(" - WHITE detected!");
//
//}
//
//if (redFrequency > 95 && greenFrequency > 95) {
//
//Serial.println(" - BLACK detected!");
//
//}
//
//if (redFrequency > 70 && greenFrequency > 70 && blueFrequency > 30 && blueFrequency < 60) {
//
//Serial.println(" - BLUE detected!");
//
//}
//
//if (redFrequency > 44 && redFrequency < 70 && greenFrequency > 68 && blueFrequency > 40 && blueFrequency < 60) {
//
//Serial.println(" - PURPLE detected!");
//
//}
//
//if (redFrequency > 13 && redFrequency < 35 && greenFrequency > 45 && greenFrequency < 65 && blueFrequency > 32 && blueFrequency < 45) {
//
//Serial.println(" - PINK detected!");
//
//}
//
//delay(delaysec);

}