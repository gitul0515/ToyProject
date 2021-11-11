// 폰트
PFont font1;
PFont font2;

// 텍스트 투명도 조정
int text_opacity = 255;
int opacity_var = -7;

// 시간 초기화
int reset_time = 0;

// 스테이지와 정답
int stage = 1;
int answer;
boolean answer_status = false; // true: 정답인 경우, false: 정답이 아닌 경우

// 게임 오버 애니메이션
int i = 0;
PImage[] img_gameover = new PImage[9];

void setup() {
  // 화면 크기 (모니터 해상도)
  size(1366, 768);
  
  // 폰트
  font1 = createFont("맑은 고딕", 24);
  font2 = createFont("맑은 고딕 Bold", 24);
  
  // 게임 오버 애니메이션
  img_gameover[0] = loadImage("gameover_1-0.png");
  img_gameover[1] = loadImage("gameover_1-1.png");
  img_gameover[2] = loadImage("gameover_1-2.png");
  img_gameover[3] = loadImage("gameover_1-3.png");
  img_gameover[4] = loadImage("gameover_1-4.png");
  img_gameover[5] = loadImage("gameover_1-5.png");
  img_gameover[6] = loadImage("gameover_1-6.png");
  img_gameover[7] = loadImage("gameover_1-7.png");
  img_gameover[8] = loadImage("gameover_1-8.png");
}

void draw() {
  // 배경색 (옅은 회색)
  background(242, 242, 242);

  // 시간(time)은 1초 단위로 차감된다.
  int run_time = (millis() / 1000) - reset_time; // reset_time은 시간 초기화용 변수
  int time = 30 - run_time;
  
  // 시간(30초)가 지나지 않은 경우
  if (time > 0) {
    // 첫 번째 스테이지
    if (stage == 1) {
      answer = 1; // 정답
      if (answer_status == false) {
        quiz(time, answer);
      } else Answer();
    }
        
    // 두 번째 스테이지
    if (stage == 2) {
      answer = 3; // 정답
      if (answer_status == false) {
        quiz(time, answer);
      } else Answer();
    } 
    
    // 세 번째 스테이지
    if (stage == 3) {
      answer = 2; // 정답
      if (answer_status == false) {
        quiz(time, answer);
      } else Answer();
    }
    
    // 게임 클리어
    if (stage > 3) {
      
    }
  } else { // 시간(30초)가 지났다면, 게임 오버 화면을 출력한다
    gameOver();
  }
  println(mouseX);
  println(mouseY);
}

// 퀴즈 화면을 출력하는 함수
void quiz(int time, int answer) {
  // 문제 텍스트
  textFont(font2, 62);
  fill(51, 63, 80);
  text("QUIZ", width / 2 - 70, height / 2 - 230);
  textFont(font1, 48);
  text("사과를 영어로 뭐라고 하나요?", width / 2 - 300, height / 2 - 150);
  
  // 빨간색 원 도형
  stroke(255, 0, 0);
  strokeWeight(8);
  fill(242, 242, 242);
  ellipse(340, 440, 270, 270);
  
  // 빨간색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text("Apple", 280, 450);
  
  // 초록색 원 도형
  stroke(0, 176, 80);
  strokeWeight(8);
  fill(242, 242, 242);
  ellipse(340 + 350, 440, 270, 270);
  
  // 초록색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text("Banana", 280 + 340, 450);
  
  // 파란색 원 도형
  stroke(46, 117, 182);
  strokeWeight(8);
  fill(242, 242, 242);
  ellipse(340 + 700, 440, 270, 270);
  
  // 파란색 원 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text("Melon", 280 + 700, 450);
  
  // 남은 시간 표시
  textFont(font1, 30);
  fill(51, 63, 80);
  text("남은 시간", 15, 50);
  textFont(font2, 30);
  text(time, 155, 50);
  
  // 레벨 표시
  textFont(font1, 30);
  text("스테이지 1 / 3", width - 210, 50);
  
  // 하위 서브 텍스트
  textFont(font1, 34);
  fill(51, 63, 80, text_opacity);
  text("공을 손으로 던져주세요!", width / 2 - 180, height / 2 + 285);

  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
  
  // ============= 정답 및 오답 처리  =============
  // 정답이 1인 경우
  if (answer == 1 && mousePressed && mouseY >= 305 && mouseY <= 575) {
    if (mouseX >= 205 && mouseX <= 475) { // 정답 처리
      answer_status = true;
    } else if (mouseX >= 555 && mouseX <= 825) { // 오답 처리
      strokeWeight(10);
      stroke(51, 63, 80);
      line(605, 354, 775, 525);
      line(775, 354, 605, 525);
    }
  }
  
  // 정답이 2인 경우
  if (answer == 2 && mousePressed && mouseY >= 305 && mouseY <= 575) {
    // 정답 처리
    if (mouseX >= 555 && mouseX <= 825) {
      answer_status = true;
    } 
  }
  
  // 정답이 3인 경우
  if (answer == 3 && mousePressed && mouseY >= 305 && mouseY <= 575) {
    // 정답 처리
    if (mouseX >= 905 && mouseX <= 1175) {
      answer_status = true;
    } 
  }
}

// 정답 화면을 출력하는 함수
void Answer() {
  textFont(font2, 62);
  fill(51, 63, 80);
  text("정답입니다!", width / 2 - 150, height / 2 - 220);
  
  // 정답 도형
  stroke(255, 0, 0);
  strokeWeight(8);
  fill(242, 242, 242);
  ellipse(width / 2, height / 2 + 10, 320, 320);
  
  // 정답 텍스트
  textFont(font2, 42);
  fill(51, 63, 80);
  text("Apple", width / 2 - 70, height / 2 + 20);
  
  textFont(font1, 36);
  text("다음 스테이지로", width / 2 - 130, height / 2 + 250);
  
  fill(51, 63, 80, text_opacity);
  textFont(font1, 26);
  text("스페이스 바를 눌러주세요", width / 2 - 150, height / 2 + 300);
  
  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
  
  if (keyPressed) {
    if (key == ' ') { // 스페이스 바를 누르면 다음 스테이지로 이동한다
      stage++; // 스테이지 1 증가
      answer_status = false;
      reset_time = millis() / 1000; // 시간 초기화
    }
  }
}

// 게임오버 화면을 출력하는 함수
void gameOver() {  
  textFont(font2, 72);
  fill(51, 63, 80);
  text("Game Over", width / 2 - 190, height / 2 - 240);
  textFont(font1, 32);
  text("시간이 지났어요ㅠㅠ", width / 2 - 125, height / 2 - 180);
  
  // 애니메이션 출력
  if (i >= 0 && i < 10) {
    image(img_gameover[i / 10],  width / 2 - 180, height / 2 - 170, 380, 380);
    i++;
  } else if (i >= 10 && i < 20) {
    image(img_gameover[i / 10], width / 2 - 180, height / 2 - 170, 380, 380);
    i++;
  } else if (i >= 20 && i < 30) {
    image(img_gameover[i / 10], width / 2 - 180, height / 2 - 170, 380, 380);
    i++;
  } else if (i >= 30 && i < 40) {
    image(img_gameover[i / 10], width / 2 - 180, height / 2 - 170, 380, 380);
    i++;
  } else if (i >= 40 && i < 50) {
    image(img_gameover[i / 10], width / 2 - 180, height / 2 - 170, 380, 380);
    i++;
  } else if (i >= 50 && i < 60) {
    image(img_gameover[i / 10], width / 2 - 180, height / 2 - 170, 380, 380);
    i++;
  } else if (i >= 60 && i < 70) {
    image(img_gameover[i / 10], width / 2 - 180, height / 2 - 170, 380, 380);
    i++;
  } else if (i >= 70 && i < 80) {
    image(img_gameover[i / 10], width / 2 - 180, height / 2 - 170, 380, 380);
    i++;
  } else {
    image(img_gameover[i / 10], width / 2 - 180, height / 2 - 170, 380, 380);
    i = 0;
  } 
  
  textFont(font1, 36);
  text("다시 도전해 볼까요?", width / 2 - 160, height / 2 + 250);
  
  fill(51, 63, 80, text_opacity);
  textFont(font1, 26);
  text("스페이스 바를 눌러주세요", width / 2 - 150, height / 2 + 300);

  // 텍스트 투명도 조정
  text_opacity += opacity_var;
  if (text_opacity < 32 || text_opacity > 455) {
    opacity_var *= -1;
  }
  
  if (keyPressed) {
    if (key == ' ') { // 스페이스 바가 눌렸다면
      // 시간을 초기화하고, 첫 번째 퀴즈로 돌아간다
      reset_time = millis() / 1000;
    }
  }
}