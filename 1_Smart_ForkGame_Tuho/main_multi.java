// 시리얼 통신
//import processing.serial.*;
//Serial myPort;
//int p;
int button_num = 0; // 몇 번째 버튼을 눌렀는가 (예: 1, 2, 3)
int who = 1; // 누가 눌렀는가 (예: 1, 2)

// 플레이어
int player1 = 1;
int player1_score = 0;
int player2 = 2;
int player2_score = 0;

// 사운드
import ddf.minim.*;
Minim minim;

// 폰트
PFont font1;
PFont font2;

// 이미지
PImage hand1;
PImage hand2;

// 텍스트 투명도 조정
int text_opacity = 255;
int opacity_var = -7;

// 텍스트 사이즈 조정
int text_size = 42;

// 시간 초기화
int reset_time = 0;

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

// 애니메이션
PImage[] img_time = new PImage[16]; // 시간 애니메이션
int time_idx = 0;
PImage[] img_answer_1 = new PImage[16]; // 승리 애니메이션
int answer_idx = 0;
PImage[] img_lose = new PImage[12]; // 패배 애니메이션
int lose_idx = 0;
PImage[] img_stars = new PImage[20]; // 별 뿌리기 애니메이션
int stars_idx = 0;
PImage[] img_gameClear = new PImage[16]; // 게임 클리어 애니메이션
int gameClear_idx = 0;
PImage[] img_gameOver = new PImage[9]; // 게임 오버 애니메이션
int gameOver_idx = 0;

// 공
int ball_diam = 330; // 공의 지름

//사운드 
AudioPlayer sound01;
AudioPlayer sound02;
AudioPlayer sound03;
AudioPlayer sound04;
//AudioPlayer sound05;
//AudioPlayer sound06;
//AudioPlayer sound07;

//class Player {
//  int score;
//  int ball_color;
//}

void setup() {
  // 화면 크기 (모니터 해상도)
  size(1366, 768);
  
  // 시리얼 통신
  //myPort = new Serial(this, "COM3", 9600);
  //myPort.bufferUntil(10);
  
  // 폰트
  font1 = createFont("맑은 고딕", 24);
  font2 = createFont("맑은 고딕 Bold", 24);
  
  // 이미지
  hand1 = loadImage("hand1.png");
  hand2 = loadImage("hand2.png");

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

  // 시간 애니메이션
  for (int i = 0; i < img_time.length; i++) {
    img_time[i] = loadImage("time-" + i + ".png");
  }
  
  // 정답(승리) 애니메이션
  for (int i = 0; i < img_answer_1.length; i++) {
    img_answer_1[i] = loadImage("answer-1-" + i + ".png");
  }
  
  // 패배 애니메이션
  for (int i = 0; i < img_lose.length; i++) {
    img_lose[i] = loadImage("lose-" + i + ".png");
  }
  
  // 별 뿌리기 애니메이션
  for (int i = 0; i < img_stars.length; i++) {
    img_stars[i] = loadImage("stars-" + i + ".png");
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
  //sound05 = minim.loadFile("apple.mp3");
  //sound06 = minim.loadFile("school.mp3");
  //sound07 = minim.loadFile("holiday.mp3");

  sound01.loop(); // BGM 재생
}

void draw() {
  // 배경색 (옅은 회색)
  background(255, 255, 255);

  // 시간은 1초 단위로 차감된다.
  int time = 15 - (millis() / 1000 - reset_time); // reset_time은 시간 초기화용 변수
  
  // 시간(15초)가 지나지 않은 경우
  if (time > 0) {
    if (cur_stage <= 7) {
        if (answer_status == false) {
        quizScreen(time, stage[cur_stage - 1]); // 퀴즈 화면을 출력한다
      } else answerScreen(stage[cur_stage - 1]); // 정답 화면을 출력한다
    } else {
      gameClear(); // 게임 클리어 화면을 출력한다
    }
  } else { // 시간(15초)가 지났다면, 게임 오버 화면을 출력한다
    gameOver();
  }
}

// 퀴즈 화면을 출력하는 함수
void quizScreen(int time, Stage stage) {
  textAlign(CENTER);
  
  // 문제 텍스트
  textFont(font2, 62);
  fill(51, 63, 80);
  text("QUIZ " + cur_stage, width / 2, height / 2 - 230);
  textFont(font1, 48);
  text(stage.question, width / 2, height / 2 - 150);
  
  // 빨간색 원 도형
  stroke(0, 191, 255);
  strokeWeight(8);
  fill(0, 191, 255, 30);
  ellipse(340, 440, 270, 270);
  
  // 빨간색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text(stage.text[0], 400, 450);

  // 초록색 원 도형
  stroke(255, 136, 73);
  strokeWeight(8);
  fill(255, 136, 73, 40);
  ellipse(width / 2, 440, 270, 270);
  
  // 초록색 원 텍스트
  textFont(font2, 52);
  fill(51, 63, 80);
  text(stage.text[1], width / 2, 450);
  
  // 파란색 원 도형
  stroke(105, 190, 40);
  strokeWeight(8);
  fill(0, 255, 0, 40);
  ellipse(1026, 440, 270, 270);
  
  // 파란색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 70);
  text(stage.text[2], 1026, 450);
  
  // 시간 애니메이션
  time_idx = time;
  image(img_time[15 - time_idx], 0, 0, img_time[0].width * 0.7, img_time[0].height * 0.7);
  
  // 레벨 표시
  textFont(font1, 30);
  text("스테이지 " + cur_stage + "/ 7", width - 110, 50);
  
  // 하위 서브 텍스트
  textFont(font1, 34);
  fill(51, 63, 80, text_opacity);
  text("공을 손으로 던져주세요!", width / 2, height / 2 + 285);

  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
    
  // 버튼을 클릭한 경우
  if (mousePressed && mouseY >= 305 && mouseY <= 575) {
    if (mouseX >= 205 && mouseX <= 475) { 
      button_num = 1; // 1번 버튼을 클릭
    } else if (mouseX >= 555 && mouseX <= 825) {
      button_num = 2; // 2번 버튼을 클릭
    } else if (mouseX >= 905 && mouseX <= 1175) {
      button_num = 3; // 3번 버튼을 클릭
    }
  }
  
  //image(hand1, width / 2 - 350, height / 2, 250, 250);
  //image(hand2, width / 2 + 350, height / 2, 250, 250);
  
  // if (button == 1) button_number = 1;
  // else if (button == 2) button_number = 2;
  // else if (button == 3) button_number = 3;
  
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

  if (who == player1) fill(244, 17, 95);
  else if (who == player2) fill(46, 117, 200);
  
  if (button_num == 1) {
    ellipse(340, 440, ball_diam, ball_diam);
  } else if (button_num == 2) {
    ellipse(width / 2, 440, ball_diam, ball_diam);
  } else if (button_num == 3){
    ellipse(1026, 440, ball_diam, ball_diam);
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
  }
}

// 정답 화면
void answerScreen(Stage stage) {
  // 사운드
  sound04.play();
  
  reset_time = millis() / 1000; // 시간을 멈춘다
  
  textFont(font2, 52);
  fill(51, 63, 80);
  if (who == player1) text("첫 번째 학생이", width / 2, height / 2 - 260);
  else text("두 번째 학생이", width / 2, height / 2 - 260);
  text("정답을 맞췄어요!", width / 2, height / 2 - 190);

  // 애니메이션
  if (who == player1) {
    // 별 뿌리기 애니메이션
    image(img_stars[stars_idx / 10], 90, 350);
    stars_idx++;
    if (stars_idx >= img_stars.length * 10) stars_idx = 0;
    
    // 정답(승리) 애니메이션
    image(img_answer_1[answer_idx / 10], width / 2 - 545, height / 2 + 40);
    answer_idx++;
    if (answer_idx >= img_answer_1.length * 10) answer_idx = 0;

  
    // 패배 애니메이션
    image(img_lose[lose_idx / 10], width / 2 + 315, height / 2 + 40, 280, 280);
    lose_idx++;
    if (lose_idx >= img_lose.length * 10) lose_idx = 0;
  } else {
    // 별 뿌리기 애니메이션
    image(img_stars[stars_idx / 10], width - 425, 350);
    stars_idx++;
    if (stars_idx >= img_stars.length * 10) stars_idx = 0;
    
    // 정답(승리) 애니메이션
    image(img_answer_1[answer_idx / 10], width / 2 + 315, height / 2 + 40);
    answer_idx++;
    if (answer_idx >= img_answer_1.length * 10) answer_idx = 0;
  
    // 패배 애니메이션
    image(img_lose[lose_idx / 10], width / 2 - 575, height / 2 + 40, 280, 280);
    lose_idx++;
    if (lose_idx >= img_lose.length * 10) lose_idx = 0;
  }
  
  // 천둥 애니메이션
  // image(img_storm[storm_idx / 10], 90, height / 2 - 130, 340, 250);
  // storm_idx++;
  // if (storm_idx >= img_storm.length * 10) storm_idx = 0;

  // 스코어 표시
  textFont(font2, 130);
  fill(244, 17, 95);
  text(player1_score, width / 2 - 430, height / 2 - 100);
  fill(46, 117, 200);
  text(player2_score, width / 2 + 430, height / 2 - 100);

  // 정답 도형
  if (stage.answer == 1) stroke(0, 191, 255); // 빨간색
  else if (stage.answer == 2) stroke(255, 136, 73); // 초록색
  else stroke(105, 190, 40); // 파란색

  strokeWeight(8);
  fill(255, 255, 255);
  ellipse(width / 2, height / 2 + 20, 320, 320);
  
  // 정답 텍스트
  textFont(font2, 56);
  fill(51, 63, 80);
  text(stage.text[stage.answer - 1], width / 2, height / 2 + 35);
  textFont(font1, 32);
  text(stage.text[3], width / 2, height / 2 + 85);
  
  textFont(font1, 36);
  text("다음 스테이지로", width / 2, height / 2 + 250);
  
  fill(51, 63, 80, text_opacity);
  textFont(font1, 26);
  text("스페이스 바를 눌러주세요", width / 2, height / 2 + 300);
  
  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
  
  if (keyPressed) {
    if (key == ' ') { // 스페이스 바를 누르면 다음 스테이지로 이동한다
      cur_stage++; // 스테이지 1 증가
      answer_status = false;
      wrong_number = 0;
      reset_time = millis() / 1000; // 시간 초기화

      who = 1;
    }
  }
}

// 오답을 X선으로 표시한다
void wrongAnswer(int wrong_number) {
  sound03.play();
  strokeWeight(10);
  stroke(51, 63, 80, 150);
  
  if (wrong_number == 1) {
    line(257, 354, 427, 525);
    line(427, 354, 257, 525);
  } else if (wrong_number == 2) {
    line(597, 354, 767, 525);
    line(767, 354, 597, 525);
  } else if (wrong_number == 3) {
    line(940, 354, 1110, 525);
    line(1110, 354, 940, 525);
  }
}

// 게임오버 화면을 출력한다
void gameOver() {
  sound01.pause();
  sound02.play();
  
  textFont(font2, 72);
  fill(51, 63, 80);
  text("Game Over", width / 2, height / 2 - 240);
  textFont(font1, 32);
  text("시간이 지났어요ㅠㅠ", width / 2, height / 2 - 180);
  
  // 게임 오버 애니메이션
  image(img_gameOver[gameOver_idx / 10],  width / 2 - 180, height / 2 - 170, 380, 380);
  gameOver_idx++;
  if (gameOver_idx >= img_gameOver.length * 10) gameOver_idx = 0;

  textFont(font1, 36);
  text("다시 도전해 볼까요?", width / 2, height / 2 + 250);
  
  fill(51, 63, 80, text_opacity);
  textFont(font1, 26);
  text("스페이스 바를 눌러주세요", width / 2, height / 2 + 300);

  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
  
  // 스페이스 바를 누른 경우
  if (keyPressed) {
    if (key == ' ') {
      // 변수들을 초기화하고, 첫 번째 퀴즈로 돌아간다
      cur_stage = 1;
      answer_status = false;
      wrong_number = 0;
      reset_time = millis() / 1000; // 시간 초기화
      
      sound01.rewind();
      sound01.loop();
      sound02.pause();
      sound02.rewind();  
      //sound05.rewind();
      //sound06.rewind();
      //sound07.rewind();
    }
  } 
}

// 게임 클리어 화면을 출력한다
void gameClear() {
  reset_time = millis() / 1000; // 시간을 멈춘다
  
  textFont(font2, 62);
  fill(51, 63, 80);
  text("최종 스코어", width / 2, height / 2 - 170);
  textFont(font2, 100);
  text(":", width / 2, height / 2 - 30);

  textFont(font2, 160);
  fill(244, 17, 95);
  text(player1_score, width / 2 - 110, height / 2);
  fill(46, 117, 200);
  text(player2_score, width / 2 + 110, height / 2);

  textFont(font2, 52);
  fill(51, 63, 80);
  if (player1_score > player2_score) text("첫 번째 학생이", width / 2, height / 2 + 120);
  else text("두 번째 학생이", width / 2, height / 2 + 120);
  text("우승했어요!", width / 2, height / 2 + 195);
  textFont(font1, 32);
  fill(51, 63, 80, text_opacity);
  text("정말 축하합니다 :)", width / 2, height / 2 + 270);
  
  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
  
  // 별 뿌리기 애니메이션
  image(img_stars[stars_idx / 10], width - 425, 70);
  image(img_stars[stars_idx / 10], 90, 70);
  stars_idx++;
  if (stars_idx >= img_stars.length * 10) stars_idx = 0;
      
  // 엔터 키를 누른 경우
  if (keyPressed) {
    if (key == ENTER) { // 엔터 키가 눌렸다면
      // 변수들을 초기화하고, 첫 번째 퀴즈로 돌아간다
      cur_stage = 1;
      answer_status = false;
      wrong_number = 0;
      reset_time = millis() / 1000; // 시간 초기화
      
      who = 1;
      player1_score = 0;
      player2_score = 0;
    }
  } 
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == UP) {
      who = 1;
    } else if (keyCode == DOWN) {
      who = 2;
    } 
  }
}

//void serialEvent(Serial myPort) {
//  String s = myPort.readString();
//  s = s.trim();
//  p = int(s);
//  myPort.clear();
  
//  if (p == 1) {
//    who = 1; button_num = 1;
//  } else if (p == 2) {
//    who = 1; button_num = 2;
//  } else if (p == 3) {
//    who = 1; button_num = 3;
//  } else if (p == 4) {
//    who = 2; button_num = 1;
//  } else if (p == 5) {
//    who = 2; button_num = 2;
//  } else if (p == 6) {
//    who = 2; button_num = 3;
//  }

//  println(button_num);
//  println(who);
//}