// 폰트
PFont font1;
PFont font2;

// 텍스트 투명도 조정
int text_opacity = 255;
int opacity_var = -7;

// 시간 초기화
int reset_time = 0;

// 스테이지
Stage[] stage = new Stage[3]; // 스테이지는 총 3개
class Stage {
  String question; // 문제 텍스트
  String text[] = new String[3]; // 보기 텍스트
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
boolean answer_status = false; // 정답 판정
int wrong_status = 0; // 오답 판정

// 애니메이션
PImage[] img_stars = new PImage[20]; // 별 뿌리기 애니메이션
int stars_idx = 0;
PImage[] img_answer_1 = new PImage[16]; // 정답 애니메이션1
int answer_idx = 0;
PImage[] img_gameClear = new PImage[16]; // 게임 클리어 애니메이션
int gameClear_idx = 0;
PImage[] img_gameOver = new PImage[9]; // 게임 오버 애니메이션
int gameOver_idx = 0;

void setup() {
  // 화면 크기 (모니터 해상도)
  size(1366, 768);
  
  // 폰트
  font1 = createFont("맑은 고딕", 24);
  font2 = createFont("맑은 고딕 Bold", 24);
  
  // 스테이지
  String[] text_0 = { "Apple", "Banana", "Melon" };
  stage[0] = new Stage("\"사과\"를 영어로 뭐라고 할까요?", text_0, 1);
  
  String[] text_1 = { "Swim", "Sport", "School" };
  stage[1] = new Stage("\"학교\"를 영어로 뭐라고 할까요?", text_1, 3);
  
  String[] text_2 = { "Monday", "Holiday", "Birthday" };
  stage[2] = new Stage("\"휴일\"을 영어로 뭐라고 할까요?", text_2, 2);


  // 정답 애니메이션
  for (int i = 0; i < img_stars.length; i++) {
    img_stars[i] = loadImage("stars-" + i + ".png");
  }
  
  // 정답1 애니메이션
  for (int i = 0; i < img_answer_1.length; i++) {
    img_answer_1[i] = loadImage("answer-1-" + i + ".png");
  }
  
  // 게임 클리어 애니메이션
  for (int i = 0; i < img_gameClear.length; i++) {
    img_gameClear[i] = loadImage("gameClear-" + i + ".png");
  }
  
  // 게임 오버 애니메이션
  for (int i = 0; i < img_gameOver.length; i++) {
    img_gameOver[i] = loadImage("gameover-" + i + ".png");
  }
}

void draw() {
  // 배경색 (옅은 회색)
  background(242, 242, 242);

  // 시간(time)은 1초 단위로 차감된다.
  int time = 5 - (millis() / 1000 - reset_time); // reset_time은 시간 초기화용 변수
  
  // 시간(30초)가 지나지 않은 경우
  if (time > 0) {
    // 첫 번째 스테이지
    if (cur_stage == 1) {
      if (answer_status == false) {
        quiz(time, stage[cur_stage - 1]);
      } else Answer(stage[cur_stage - 1]);
    }
        
    // 두 번째 스테이지
    if (cur_stage == 2) {
      if (answer_status == false) {
        quiz(time, stage[cur_stage - 1]);
      } else Answer(stage[cur_stage - 1]);
    } 
    
    // 세 번째 스테이지
    if (cur_stage == 3) {
      if (answer_status == false) {
        quiz(time, stage[cur_stage - 1]);
      } else Answer(stage[cur_stage - 1]);
    }
    
    // 게임 클리어
    if (cur_stage > 3) {
      gameClear();
    }
  } else { // 시간(30초)가 지났다면, 게임 오버 화면을 출력한다
    gameOver();
  }
}

// 퀴즈 화면을 출력하는 함수
void quiz(int time, Stage stage) {
  textAlign(CENTER);
  
  // 문제 텍스트
  textFont(font2, 62);
  fill(51, 63, 80);
  text("QUIZ " + cur_stage, width / 2, height / 2 - 230);
  textFont(font1, 48);
  text(stage.question, width / 2, height / 2 - 150);
  
  // 빨간색 원 도형
  stroke(255, 0, 0);
  strokeWeight(8);
  fill(242, 242, 242);
  ellipse(340, 440, 270, 270);
  
  // 빨간색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text(stage.text[0], 340, 450);
  
  // 초록색 원 도형
  stroke(0, 176, 80);
  strokeWeight(8);
  fill(242, 242, 242);
  ellipse(width / 2, 440, 270, 270);
  
  // 초록색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text(stage.text[1], width / 2, 450);
  
  // 파란색 원 도형
  stroke(46, 117, 182);
  strokeWeight(8);
  fill(242, 242, 242);
  ellipse(1026, 440, 270, 270);
  
  // 파란색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text(stage.text[2], 1026, 450);
  
  // 남은 시간 표시
  textFont(font1, 30);
  text("남은 시간", 80, 50);
  textFont(font2, 30);
  text(time, 170, 50);
  
  // 레벨 표시
  textFont(font1, 30);
  text("스테이지 " + cur_stage + "/ 3", width - 110, 50);
  
  // 하위 서브 텍스트
  textFont(font1, 34);
  fill(51, 63, 80, text_opacity);
  text("공을 손으로 던져주세요!", width / 2, height / 2 + 285);

  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
  
  // ============= 정답 및 오답 처리 =============
  // 정답이 1인 경우
  if (stage.answer == 1 && mousePressed && mouseY >= 305 && mouseY <= 575) {
    if (mouseX >= 205 && mouseX <= 475) { // 정답 처리
      answer_status = true;
    } else if (mouseX >= 555 && mouseX <= 825) { // 오답 처리: 2
      wrong_status = 2;
    } else if (mouseX >= 905 && mouseX <= 1175) { // 오답 처리: 3
      wrong_status = 3;
    }
  }
  
  // 정답이 2인 경우
  if (stage.answer == 2 && mousePressed && mouseY >= 305 && mouseY <= 575) {
    // 정답 처리
    if (mouseX >= 555 && mouseX <= 825) {
      answer_status = true;
    } else if (mouseX >= 205 && mouseX <= 475) { // 오답 처리: 1
      wrong_status = 1;
    } else if (mouseX >= 905 && mouseX <= 1175) { // 오답 처리: 3
      wrong_status = 3;
    }
  }
  
  // 정답이 3인 경우
  if (stage.answer == 3 && mousePressed && mouseY >= 305 && mouseY <= 575) {
    // 정답 처리
    if (mouseX >= 905 && mouseX <= 1175) {
      answer_status = true;
    } else if (mouseX >= 205 && mouseX <= 475) { // 오답 처리: 1
      wrong_status = 1;
    } else if (mouseX >= 555 && mouseX <= 825) { // 오답 처리: 2
      wrong_status = 2;
    }
  }
  
  if (boolean(wrong_status)) { // wrong_status가 0이면 실행x, 1 2 3이면 실행o
    Wrong(wrong_status);  
  }
}

// 정답 화면
void Answer(Stage stage) {
  reset_time = 10000000; // 시간을 멈춘다
  
  textFont(font2, 62);
  fill(51, 63, 80);
  text("정답입니다!", width / 2, height / 2 - 220);
  
  // 정답 도형
  if (stage.answer == 1) stroke(255, 0, 0); // 빨간색
  else if (stage.answer == 2) stroke(0, 176, 80); // 초록색
  else stroke(46, 117, 182); // 파란색

  strokeWeight(8);
  fill(242, 242, 242);
  ellipse(width / 2, height / 2 + 10, 320, 320);
  
  // 정답 텍스트
  textFont(font2, 56);
  fill(51, 63, 80);
  text(stage.text[stage.answer - 1], width / 2, height / 2 + 20);
  
  // 별 뿌리기 애니메이션
  image(img_stars[stars_idx / 10], width - 425, 70);
  image(img_stars[stars_idx / 10], 90, 70);
  stars_idx++;
  if (stars_idx >= img_stars.length * 10) stars_idx = 0;
  
  // 축하 애니메이션
  image(img_answer_1[answer_idx / 10], width / 2 + 315, height / 2 + 40);
  image(img_answer_1[answer_idx / 10], width / 2 - 545, height / 2 + 40);
  answer_idx++;
  if (answer_idx >= img_answer_1.length * 10) answer_idx = 0;
  
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
      wrong_status = 0;
      reset_time = millis() / 1000; // 시간 초기화
    }
  }
}

// 오답을 X선으로 표시한다
void Wrong(int wrong_status) {
  strokeWeight(10);
  stroke(51, 63, 80, 150);
  
  if (wrong_status == 1) {
    line(257, 354, 427, 525);
    line(427, 354, 257, 525);
  } else if (wrong_status == 2) {
    line(597, 354, 767, 525);
    line(767, 354, 597, 525);
  } else if (wrong_status == 3) {
    line(940, 354, 1110, 525);
    line(1110, 354, 940, 525);
  }
}

// 게임오버 화면을 출력한다
void gameOver() {  
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
      wrong_status = 0;
      reset_time = millis() / 1000; // 시간 초기화
    }
  } 
}

// 게임 클리어 화면을 출력한다
void gameClear() {
  reset_time = 10000000; // 시간을 멈춘다
  
  textFont(font2, 72);
  fill(51, 63, 80);
  text("Game Clear", width / 2, height / 2 - 220);
  textFont(font1, 32);
  text("모든 문제를 다 맞혔어요!", width / 2, height / 2 - 160);
  
  // 별 뿌리기 애니메이션
  image(img_stars[stars_idx / 10], width - 425, 70);
  image(img_stars[stars_idx / 10], 90, 70);
  stars_idx++;
  if (stars_idx >= img_stars.length * 10) stars_idx = 0;
  
  // 게임 클리어 애니메이션
  image(img_gameClear[gameClear_idx / 10],  width / 2 - 180, height / 2 - 130);
  gameClear_idx++;
  if (gameClear_idx >= img_gameClear.length * 10) gameClear_idx = 0;
  
  text("정말 축하합니다 :)", width / 2, height / 2 + 280);
  
  // 엔터 키를 누른 경우
  if (keyPressed) {
    if (key == ENTER) { // 엔터 키가 눌렸다면
      // 변수들을 초기화하고, 첫 번째 퀴즈로 돌아간다
      cur_stage = 1;
      answer_status = false;
      wrong_status = 0;
      reset_time = millis() / 1000; // 시간 초기화
    }
  } 
}