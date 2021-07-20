package timerApp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class timerStageController implements Initializable {
    private boolean isStopWatchOn = false, isCountDownOn = false, isCountDownValueValid = false;
    private Timer stopWatchTimer, countDownTimer, clocksTimer;
    private TimerTask stopWatchTimerTask, countDownTimerTask, clocksTimerTask;
    private int countDownMillis, stopWatchMillis;

    @FXML
    private ImageView longArrowMainCity, smallArrowMainCity, longArrowTokyo, smallArrowTokyo,
            longArrowNew_York, smallArrowNew_York, longArrowSydney, smallArrowSydney;

    @FXML
    private Text startStopWatchText, stopWatchTimerText, stopStopWatchText, resumeStopWatchText,
            countDownTimerText, startCountDownText, stopCountDownText, resumeCountDownText,
            mainCityTimeTxt, tokyoTimeTxt, new_yorkTimeTxt, sydneyTimeTxt;

    @FXML
    private TextField countDownMinutesField, countDownSecondsField;

    @FXML
    private Text validationCountDownText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeClocks();

        countDownMillis = 0;
        stopWatchMillis = 0;
        countDownSecondsField.textProperty().addListener(e ->
            isCountDownValueValid = countDownFieldsAreValid());

        countDownMinutesField.textProperty().addListener(e ->
            isCountDownValueValid = countDownFieldsAreValid());
    }

    private void initializeClocks() {
        clocksTimer = new Timer();
        clocksTimerTask = new TimerTask() {
            @Override
            public void run() {
                LocalTime WarsawTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Warsaw")).toLocalTime();
                LocalTime TokyoTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Asia/Tokyo")).toLocalTime();
                LocalTime New_YorkTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("America/New_York")).toLocalTime();
                LocalTime SydneyTime = ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("Australia/Sydney")).toLocalTime();
                mainCityTimeTxt.setText(WarsawTime.toString().substring(0,8));
                tokyoTimeTxt.setText(TokyoTime.toString().substring(0,8));
                new_yorkTimeTxt.setText(New_YorkTime.toString().substring(0,8));
                sydneyTimeTxt.setText(SydneyTime.toString().substring(0,8));
                synchronizeClock(WarsawTime, longArrowMainCity, smallArrowMainCity);
                synchronizeClock(TokyoTime, longArrowTokyo, smallArrowTokyo);
                synchronizeClock(New_YorkTime, longArrowNew_York, smallArrowNew_York);
                synchronizeClock(SydneyTime, longArrowSydney, smallArrowSydney);
            }
        };
        clocksTimer.scheduleAtFixedRate(clocksTimerTask,0,1000);
    }

    private void synchronizeClock(LocalTime localTime, ImageView minutesArrow, ImageView hoursArrow ) {
        int hours = Integer.parseInt(localTime.toString().substring(0,2));
        int minutes = Integer.parseInt(localTime.toString().substring(3,5));
        if(hours>=12) {
         hours -= 12;
        }
        double hoursAngle = hours * 30 + minutes * 0.5;
        double minutesAngle = minutes * 6;

        minutesArrow.rotateProperty().setValue(minutesAngle);
        hoursArrow.rotateProperty().setValue(hoursAngle);
    }

    @FXML
    private void onMouseStartStopCountDownCircle() {
        if(isCountDownValueValid) {
            countDownMinutesField.setEffect(null);
            countDownSecondsField.setEffect(null);
            countDownMinutesField.setDisable(true);
            countDownSecondsField.setDisable(true);
            if (!isCountDownOn) {
                startCountDownTimer();
                stopCountDownText.setVisible(true);
                startCountDownText.setVisible(false);
                resumeCountDownText.setVisible(false);
            } else {
                stopCountDownTimer();
                stopCountDownText.setVisible(false);
                resumeCountDownText.setVisible(true);
            }
            isCountDownOn = !isCountDownOn;
        }
    }

    @FXML
    private void onMouseResetCountDownCircle() {
        if(countDownSecondsField.isDisabled()) {
            countDownTimer.cancel();
            Platform.runLater(() -> countDownTimerText.setText("00:00:00"));
            startCountDownText.setVisible(true);
            stopCountDownText.setVisible(false);
            resumeCountDownText.setVisible(false);
            validationCountDownText.setVisible(false);
            countDownMillis = 0;
            countDownMinutesField.setText("");
            countDownSecondsField.setText("");
            countDownMinutesField.setDisable(false);
            countDownSecondsField.setDisable(false);
            countDownMinutesField.setEffect(null);
            countDownSecondsField.setEffect(null);

            if (isCountDownOn) {
                isCountDownOn = false;
            }
        }
    }

    private void startCountDownTimer() {
        countDownTimer = new Timer();
        countDownTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(countDownMillis > 0) {
                        countDownMillis -= 1000;
                } else {
                    countDownMillis = 0;
                    stopCountDownTimer();
                }
                updateCountDownText(countDownMillis);
            }
        };
        countDownTimer.scheduleAtFixedRate(countDownTimerTask, 0, 1000);
    }

    private void updateCountDownText(int countDownMillis) {
        String seconds = String.valueOf((int) Math.floor((float) countDownMillis /1000) % 60);
        String minutes = String.valueOf((int) Math.floor((float) countDownMillis /(1000*60)) % 60);
        String hours = String.valueOf((int) Math.floor((float) countDownMillis /(1000*60*60)));

        if(seconds.length()==1) {
            seconds = "0" + seconds;
        }

        if(minutes.length()==1) {
            minutes = "0" + minutes;
        }

        if(hours.length()==1) {
            hours = "0" + hours;
        }

        if(seconds.length()==2 && minutes.length()==2 && hours.length()==2) {
            String finalMinutes = minutes;
            String finalSeconds = seconds;
            String finalHours = hours;
            Platform.runLater(() -> countDownTimerText.setText(finalHours + ":" + finalMinutes + ":" + finalSeconds));
        }
    }

    public void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    private boolean countDownFieldsAreValid() {
        if(countDownMinutesField.getText().matches("([0-9][0-9])") &&
           countDownSecondsField.getText().matches("([0-5][0-9])|(60)")) {
            countDownMinutesField.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.web("#27b500"),5,0.7,0,0));
            countDownSecondsField.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.web("#27b500"),5,0.7,0,0));
            countDownMillis = Integer.parseInt(countDownMinutesField.getText())*60*1000 + Integer.parseInt(countDownSecondsField.getText())*1000 + 1000;
            validationCountDownText.setVisible(false);
            return true;
        } else {
            countDownMinutesField.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.web("#960000"),5,0.7,0,0));
            countDownSecondsField.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.web("#960000"),5,0.7,0,0));
            validationCountDownText.setVisible(true);
            validationCountDownText.setText("Required format: 00:00");
            return false;
        }
    }

    @FXML
    private void onMouseStartStopStopWatchCircle() {
            if (!isStopWatchOn) {
                startStopWatchText.setVisible(false);
                resumeStopWatchText.setVisible(false);
                stopStopWatchText.setVisible(true);
                startStopWatchTimer();
            } else {
                resumeStopWatchText.setVisible(true);
                stopStopWatchText.setVisible(false);
                stopStopWatchTimer();
            }
            isStopWatchOn = !isStopWatchOn;
    }

    @FXML
    private void onResetStopWatchTimerCircle() {
        stopWatchTimer.cancel();
        Platform.runLater(() -> stopWatchTimerText.setText("00:00:00"));
        resumeStopWatchText.setVisible(false);
        startStopWatchText.setVisible(true);
        stopStopWatchText.setVisible(false);
        stopWatchMillis = 0;
        if(isStopWatchOn) {
            isStopWatchOn = false;
        }
    }

    private void startStopWatchTimer() {
        stopWatchTimer = new Timer();
        stopWatchTimerTask = new TimerTask() {
            @Override
            public void run() {
                stopWatchMillis += 10;
                updateStopWatchText(stopWatchMillis);
            }
        };
        stopWatchTimer.scheduleAtFixedRate(stopWatchTimerTask, 10, 10);
    }

    public void stopStopWatchTimer() {
        stopWatchTimer.cancel();
    }

    private void updateStopWatchText(int stopWatchMillis) {
        String millis = String.valueOf((stopWatchMillis % 1000)/10);
        String seconds = String.valueOf((int) Math.floor((float)stopWatchMillis/1000) % 60);
        String minutes = String.valueOf((int) Math.floor((float)stopWatchMillis/(1000*60)));

        if(millis.length()==1) {
            millis = "0" + millis;
        }

        if(seconds.length()==1) {
            seconds = "0" + seconds;
        }

        if(minutes.length()==1) {
            minutes = "0" + minutes;
        }

        if(millis.length()==2 && seconds.length()==2 && minutes.length()==2) {
            String finalMinutes = minutes;
            String finalSeconds = seconds;
            String finalMillis = millis;
            Platform.runLater(() -> stopWatchTimerText.setText(finalMinutes + ":" + finalSeconds + ":" + finalMillis));
        }
    }
}
