#include <Servo.h> 
#include <LiquidCrystal_I2C.h>
#include <Wire.h>

Servo motor1; //서보 모터 
LiquidCrystal_I2C lcd(0x27, 16, 2); //LCD 모듈

int servoMotorPin = 9; //서보 모터
int red = 5; //rgb red
int green = 6; // rgb green
int blue = 7; //rgb blue
int trig = 13; //초음파 센서 음파를 발사
int echo = 12; //발사된 음파를 받음

int secureLevel = 0; //경계 레벨값


void setup() {    
  motor1.attach(servoMotorPin,600,2400); 
  Serial.begin(9600); //시리얼 통신 설정
   
  lcd.init();
  lcd.backlight();
  
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT);

  pinMode(red, OUTPUT);
  pinMode(green, OUTPUT);
  pinMode(blue, OUTPUT);
}

void loop(){
  int angle;

  //45도부터 135까지 회전
  for (angle = 45; angle < 135; angle++) {
    motor1.write(angle); //모터 회전
    getDistance(angle); //초음파 센서 거리 측정 함수 호출
    delay(200); //0.2s 딜레이
  }
  // 위의 동작 반대 각도로 실행
  for (angle = 135; angle > 45; angle--) {
    motor1.write(angle);
    getDistance(angle);
    delay(200);
  }
}

//거리 측정 함수
void getDistance(int value) {
  long duration, distance; //

  //secureLevel에 따른 RGB LED값 출력 
  if (secureLevel < 100) {
    //초록 불빛 출력 = SAFE
    digitalWrite(green,HIGH);
    digitalWrite(red, LOW);
    digitalWrite(blue, LOW);
  } else if (secureLevel < 500) {
    //노란 불빛 출력 = CAUTION
    digitalWrite(green,HIGH);
    digitalWrite(red,HIGH);
    digitalWrite(blue,LOW);
  } else {
    /빨간 불빛 출력 = DANGER!!
    digitalWrite(red,HIGH);
    digitalWrite(green,LOW);
    digitalWrite(blue,LOW);
  }

  //초음파를 발사하여 거리 측정
  digitalWrite(trig, LOW); 
  delayMicroseconds(2);
  digitalWrite(trig, HIGH); 
  delayMicroseconds(10);
  digitalWrite(trig, LOW); 

  //물체에 반사되어돌아온 초음파의 시간을 변수에 저장
  duration = pulseIn (echo, HIGH); 
  distance = duration * 17 / 1000; 

  //시리얼 모니터에 측정된 물체로부터 거리값(cm값)을 보여줌
  Serial.print("\ndistance : ");
  Serial.print(distance); 
  Serial.println(" Cm");

  //20cm내에서 물체가 감지 되었을 때
  //LCD에 물체가 감지된 각도와 거리를 출력
  if (distance < 20) {
    lcd.clear();
    lcd.setCursor(0,1);
    lcd.print("D: ");
    lcd.print(distance);
    lcd.print("cm");
    lcd.setCursor(8,1);
    lcd.print("A: ");
    lcd.print(value);
    lcd.print("d");

    //카운트값 5 증가
    secureLevel = secureLevel + 5;
  }

  //secureLevel이 0보다 크다면 0.2초마다 1씩 카운트 감소
  if (secureLevel > 0) {
    secureLevel--;
  }
}
