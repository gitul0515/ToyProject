// 시리얼 통신
import processing.serial.*;
Serial myPort;

// 아두이노로 값을 제어할 변수
int p = 0;
int button_num = 0; // 몇 번째 버튼을 눌렀는가 (예: 1, 2, 3)
int who = 1; // 누가 눌렀는가 (예: 1, 2)

// 플레이어
int player1 = 1;
int player1_score = 0;
int player2 = 2;
int player2_score = 0;

// 스테이지
Stage[] stage = new Stage[7]; // 스테이지는 총 7개
class Stage {
  String question; // 문제 텍스트
  String text[] = new String[4]; // 보기 텍스트
  int answer; // 정답

  // 생성자 함수
  Stage(String _question, String[] _text, int _answer) {
    question = _question;
    for (int i = 0; i < text.length; i++) {
      text[i] = _text[i];
    }
    answer = _answer;
  }
}

int cur_stage = 1; // 현재 스테이지
boolean answer_status = false; // 정답을 맞추었는지 판정하는 변수
int ring_number = 0; // 플레이어가 답을 고른 링 번호
int wrong_number = 0; // 오답인 경우 오답 번호

PImage[] img_stage = new PImage[7]; // 스테이지 표시 이미지

// 폰트
PFont font1;
PFont font2;

// 텍스트 투명도 조정
int text_opacity = 255;
int opacity_var = -7;

// 애니메이션
PImage[] img_time = new PImage[16]; // 시간 애니메이션
int time_idx = 0;
PImage[] img_happy = new PImage[28]; // 승자 애니메이션
int happy_idx = 0;
PImage[] img_sad = new PImage[28]; // 패자 애니메이션
int sad_idx = 0;
PImage[] img_stars = new PImage[20]; // 별 애니메이션
int stars_idx = 0;
PImage[] img_rain = new PImage[28]; // 비 애니메이션
int rain_idx = 0;
PImage[] img_gameClear = new PImage[16]; // 게임 클리어 애니메이션
int gameClear_idx = 0;
PImage[] img_gameOver = new PImage[9]; // 게임 오버 애니메이션
int gameOver_idx = 0;

// 공
int ball_diam = 330; // 공의 지름

// 사운드
import ddf.minim.*;
Minim minim;
AudioPlayer sound01;
AudioPlayer sound02;
AudioPlayer sound03;
AudioPlayer sound04;
AudioPlayer sound08;

void setup() {
  // 화면 크기 (모니터 해상도)
  size(1366, 768);
  
  // 시리얼 통신
  myPort = new Serial(this, "COM5", 115200);
  myPort.bufferUntil(10);
  
  // 폰트
  font1 = createFont("맑은 고딕", 24);
  font2 = createFont("맑은 고딕 Bold", 24);

  // 스테이지
  String[] text_0 = { "Apple", "Banana", "Melon", "사과" };
  stage[0] = new Stage("\"사과\"를 영어로 뭐라고 할까요?", text_0, 1);
  
  String[] text_1 = { "Swim", "Sport", "School", "학교" };
  stage[1] = new Stage("\"학교\"를 영어로 뭐라고 할까요?", text_1, 3);
  
  String[] text_2 = { "Monkey", "Elephant", "Lion", "코끼리" };
  stage[2] = new Stage("\"코끼리\"를 영어로 뭐라고 할까요?", text_2, 2);
  
  String[] text_3 = { "Friend", "Teacher", "Parent", "부모님" };
  stage[3] = new Stage("\"부모님\"을 영어로 뭐라고 할까요?", text_3, 3);
  
  String[] text_4 = { "Monday", "Holiday", "Birthday", "휴일" };
  stage[4] = new Stage("\"휴일\"을 영어로 뭐라고 할까요?", text_4, 2);
  
  String[] text_5 = { "Fall", "Spring", "Winter", "가을" };
  stage[5] = new Stage("\"가을\"을 영어로 뭐라고 할까요?", text_5, 1);
  
  String[] text_6 = { "Morning", "Evening", "Afternoon", "오후" };
  stage[6] = new Stage("\"오후\"를 영어로 뭐라고 할까요?", text_6, 3);

  for (int i = 0; i < img_stage.length; i++) {
    img_stage[i] = loadImage("stage"+(i+1)+".png");
  }

  // 시간 애니메이션
  for (int i = 0; i < img_time.length; i++) {
    img_time[i] = loadImage("time-" + i + ".png");
  }
  
  // 승자 애니메이션
  for (int i = 0; i < img_happy.length; i++) {
    img_happy[i] = loadImage("happy-" + i + ".png");
  }
  
  // 패자 애니메이션
  for (int i = 0; i < img_sad.length; i++) {
    img_sad[i] = loadImage("sad-" + i + ".png");
  }
  
  // 별 애니메이션
  for (int i = 0; i < img_stars.length; i++) {
    img_stars[i] = loadImage("stars-" + i + ".png");
  }
  
  // 비 애니메이션
  for (int i = 0; i < img_rain.length; i++) {
    img_rain[i] = loadImage("rain-" + i + ".png");
  }
  
  // 게임 클리어 애니메이션
  for (int i = 0; i < img_gameClear.length; i++) {
    img_gameClear[i] = loadImage("gameClear-" + i + ".png");
  }
  
  // 게임 오버 애니메이션
  for (int i = 0; i < img_gameOver.length; i++) {
    img_gameOver[i] = loadImage("gameover-" + i + ".png");
  }
  
  // 사운드
  minim = new Minim(this);
  sound01 = minim.loadFile("BGM.wav"); 
  sound02 = minim.loadFile("BGM_OVER.wav");
  sound03 = minim.loadFile("wrong.wav");
  sound04 = minim.loadFile("correct.wav");
  sound08 = minim.loadFile("goal.mp3");

  sound01.loop(); // BGM 재생
}

void draw() {
  // 배경색 (옅은 회색)
  background(242, 242, 242);

  if (cur_stage <= 7) {
      if (answer_status == false) {
      quizScreen(stage[cur_stage - 1]); // 퀴즈 화면을 출력한다
    } else answerScreen(stage[cur_stage - 1]); // 정답 화면을 출력한다
  } else {
    gameClear(); // 게임 클리어 화면을 출력한다
  }
}

// 퀴즈 화면을 출력하는 함수
void quizScreen(Stage stage) {
  textAlign(CENTER);
  
  // 문제 텍스트
  textFont(font2, 62);
  fill(51, 63, 80);
  text("QUIZ " + cur_stage, width / 2, height / 2 - 250);
  textFont(font1, 48);
  text(stage.question, width / 2, height / 2 - 170);
  
  // 빨간색 원 도형
  stroke(255, 50, 50);
  strokeWeight(8);
  fill(255, 50, 50, 40);
  ellipse(340, 420, 270, 270);
  
  // 빨간색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text(stage.text[0], 340, 430);

  // 파란색 원 도형
  stroke(46, 117, 182);
  strokeWeight(8);
  fill(46, 117, 182, 50);
  ellipse(width / 2, 420, 270, 270);
  
  // 파란색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text(stage.text[1], width / 2, 430);
  
  // 초록색 원 도형
  stroke(0, 176, 80);
  strokeWeight(8);
  fill(0, 176, 80, 50);
  ellipse(1026, 420, 270, 270);
  
  // 초록색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 70);
  text(stage.text[2], 1026, 430);

  // 스테이지 표시
  image(img_stage[cur_stage - 1], width - 405, 20, 144*2.6, 19*2.6);
  
  // 하위 서브 텍스트
  textFont(font1, 34);
  fill(51, 63, 80, text_opacity);
  text("손가락 인형을 넣어주세요!", width / 2, height / 2 + 265);

  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
    
  // 마우스 오른쪽 클릭 
  if (mouseButton == RIGHT && mouseY >= 305 && mouseY <= 575) {
    if (mouseX >= 205 && mouseX <= 475) { 
      button_num = 1; // 1번 버튼을 클릭
    } else if (mouseX >= 555 && mouseX <= 825) {
      button_num = 2; // 2번 버튼을 클릭
    } else if (mouseX >= 905 && mouseX <= 1175) {
      button_num = 3; // 3번 버튼을 클릭
    }
  }
  
  // 답을 판정한다
  if (boolean(button_num)) { // button_number 0이면 실행하지 않음. 1, 2, 3이면 실행함
    judgeAnswer(stage);
  }
  
  // 오답을 표시한다
  if (boolean(wrong_number)) { // wrong_number가 0이면 실행하지 않음. 1 2 3이면 실행함
    wrongAnswer(wrong_number);  
  }
}

// 답을 판정하는 함수
void judgeAnswer(Stage stage) {
  // 링에 공을 던지는 시각 효과
  noStroke();

  if (who == player1) fill(244, 17, 65);
  else if (who == player2) fill(0, 236, 30);
  
  if (button_num == 1) {
    ellipse(340, 420, ball_diam, ball_diam);
  } else if (button_num == 2) {
    ellipse(width / 2, 420, ball_diam, ball_diam);
  } else if (button_num == 3){
    ellipse(1026, 420, ball_diam, ball_diam);
  }
  ball_diam -= 10; // 공의 크기가 점점 작아진다
  delay(10);
  
  // 공의 지름이 90 미만으로 작아지면 답을 판정한다
  if (ball_diam < 90) {
    if (button_num == stage.answer) { // 정답인 경우
      // 정답을 맞춘 플레이어의 점수 1 증가
      if (who == player1) player1_score++;
      else if (who == player2) player2_score++;
      answer_status = true; // 정답 화면으로 넘어간다
          
      sound04.rewind();
      delay(10);
  } else { // 오답인 경우: 오답 표시
      sound03.rewind();
      if (button_num == 1) wrong_number = 1;
      else if (button_num == 2) wrong_number = 2;
      else if (button_num == 3) wrong_number = 3;
      
      // who = 1;
    }
    // 변수 초기화
    ball_diam = 330;
    button_num = 0;
    // p = 0;
  }
}

// 정답 화면
void answerScreen(Stage stage) {
  // 사운드
  sound04.play();
  
  textFont(font2, 52);
  fill(51, 63, 80);
  if (who == player1) text("첫 번째 학생이", width / 2, height / 2 - 280);
  else text("두 번째 학생이", width / 2, height / 2 - 280);
  text("정답을 맞췄어요!", width / 2, height / 2 - 210);

  // 애니메이션
  if (who == player1) {
    // 별 애니메이션
    image(img_stars[stars_idx / 10], 90, 330);
    stars_idx += 2;
    if (stars_idx >= img_stars.length * 10) stars_idx = 0;
    
    // 승자 애니메이션
    image(img_happy[happy_idx / 10], width / 2 - 547, height / 2 + 40, 240, 240);
    happy_idx += 4;
    if (happy_idx >= img_happy.length * 10) happy_idx = 0;

    // 비 애니메이션
    image(img_rain[rain_idx / 10],  width / 2 + 355, height / 2 - 60, 192 * 0.8, 192 * 0.8);
    rain_idx += 4;
    if (rain_idx >= img_rain.length * 10) rain_idx = 0;

    // 패자 애니메이션
    image(img_sad[sad_idx / 10], width / 2 + 315, height / 2 + 40, 240, 240);
    sad_idx += 4;
    if (sad_idx >= img_sad.length * 10) sad_idx = 0;
  } else {
    // 별 애니메이션
    image(img_stars[stars_idx / 10], width - 425, 330);
    stars_idx += 2;
    if (stars_idx >= img_stars.length * 10) stars_idx = 0;
    
    // 승자 애니메이션
    image(img_happy[happy_idx / 10], width / 2 + 315, height / 2 + 40, 240, 240);
    happy_idx += 4;
    if (happy_idx >= img_happy.length * 10) happy_idx = 0;
    
    // 비 애니메이션
    image(img_rain[rain_idx / 10], width / 2 - 505, height / 2 - 60, 192 * 0.8, 192 * 0.8);
    rain_idx += 4;
    if (rain_idx >= img_rain.length * 10) rain_idx = 0;
  
    // 패자 애니메이션
    image(img_sad[sad_idx / 10], width / 2 - 550, height / 2 + 40, 240, 240);
    sad_idx += 4;
    if (sad_idx >= img_sad.length * 10) sad_idx = 0;
  }

  // 스코어 표시
  textFont(font2, 130);
  fill(244, 17, 65);
  text(player1_score, width / 2 - 430, height / 2 - 120);
  fill(0, 236, 30);
  text(player2_score, width / 2 + 430, height / 2 - 120);

  // 정답 도형
  strokeWeight(8);
  if (stage.answer == 1) { 
    stroke(255, 50, 50); // 빨간색
    fill(255, 50, 50, 40);
  } else if (stage.answer == 2) { 
    stroke(46, 117, 182); // 파란색
    fill(46, 117, 182, 50);
  } else { 
    stroke(0, 176, 80); // 초록색
    fill(0, 176, 80, 50);
  }
  ellipse(width / 2, height / 2 + 10, 300, 300);
  
  // 정답 텍스트
  textFont(font2, 56);
  fill(51, 63, 80);
  text(stage.text[stage.answer - 1], width / 2, height / 2 + 15);
  textFont(font1, 32);
  text(stage.text[3], width / 2, height / 2 + 65);
  
  textFont(font1, 36);
  text("다음 스테이지로", width / 2, height / 2 + 240);
  
  fill(51, 63, 80, text_opacity);
  textFont(font1, 26);
  text("마우스 왼쪽 버튼 클릭!", width / 2, height / 2 + 290);
  
  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
}

// 오답을 X선으로 표시한다
void wrongAnswer(int wrong_number) {
  sound03.play();
  strokeWeight(10);
  stroke(51, 63, 80, 150);
  
  if (wrong_number == 1) {
    line(257, 334, 427, 505);
    line(427, 334, 257, 505);
  } else if (wrong_number == 2) {
    line(597, 334, 767, 505);
    line(767, 334, 597, 505);
  } else if (wrong_number == 3) {
    line(940, 334, 1110, 505);
    line(1110, 334, 940, 505);
  }
}

// 게임 클리어 화면을 출력한다
void gameClear() {
  sound01.pause();
  delay(10);
  sound08.play();

  textFont(font2, 62);
  fill(51, 63, 80);
  text("최종 스코어", width / 2, height / 2 - 220);
  textFont(font2, 90);
  text(":", width / 2, height / 2 - 80);

  textFont(font2, 160);
  fill(244, 17, 65);
  text(player1_score, width / 2 - 110, height / 2 - 50);
  fill(0, 236, 30);
  text(player2_score, width / 2 + 110, height / 2 - 50);

  textFont(font2, 52);
  fill(51, 63, 80);
  if (player1_score > player2_score) text("첫 번째 학생이", width / 2, height / 2 + 90);
  else text("두 번째 학생이", width / 2, height / 2 + 90);
  text("우승했어요!", width / 2, height / 2 + 165);
  textFont(font1, 32);
  fill(51, 63, 80, text_opacity);
  text("정말 축하합니다 :)", width / 2, height / 2 + 240);
  
  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
  
  // 별 뿌리기 애니메이션
  image(img_stars[stars_idx / 10], width - 425, 70);
  image(img_stars[stars_idx / 10], 90, 70);
  stars_idx += 2;
  if (stars_idx >= img_stars.length * 10) stars_idx = 0;
}

void mousePressed() {
    // 다음 스테이지로 전환
    if(mouseButton == LEFT && cur_stage <= 7 && answer_status) {
      // 변수들을 초기화하고, 다음 퀴즈로 이동한다
      cur_stage++; // 스테이지 1 증가
      answer_status = false;
      wrong_number = 0;

      who = 1;
    } else if (mouseButton == LEFT && cur_stage > 7) { // 게임 클리어 화면
      // 변수들을 초기화하고, 첫 번째 퀴즈로 돌아간다
      cur_stage = 1;
      answer_status = false;
      wrong_number = 0;
      
      who = 1;
      player1_score = 0;
      player2_score = 0;
      
      sound01.rewind();
      sound01.loop();
      sound08.pause();
      sound08.rewind(); 
    }
}

void keyPressed() {
  if (key == CODED && !answer_status) {
    if (keyCode == UP) {
      who = 1;
    } else if (keyCode == DOWN) {
      who = 2;
    } 
  }
}

void serialEvent(Serial myPort) {
  String s = myPort.readString();
  s = s.trim();
  p = int(s);
  myPort.clear();
  
  if (p == 1) {
    who = 1; button_num = 1;
  } else if (p == 2) {
    who = 1; button_num = 2;
  } else if (p == 3) {
    who = 1; button_num = 3;
  } else if (p == 4) {
    who = 2; button_num = 1;
  } else if (p == 5) {
    who = 2; button_num = 2;
  } else if (p == 6) {
    who = 2; button_num = 3;
  }
  p = 0; // p를 초기화
}