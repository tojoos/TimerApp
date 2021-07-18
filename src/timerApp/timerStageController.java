package timerApp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class timerStageController implements Initializable {
    private boolean isStopWatchOn = false;
    private Timer stopWatchTimer;
    private TimerTask stopWatchTimerTask;
    private int stopWatchMillis;


    @FXML
    private Text startStopWatchText, stopWatchTimerText, stopStopWatchText, resumeStopWatchText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stopWatchMillis = 0;
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

    private void updateStopWatchText(int stopWatchMillis) {
        String millis = String.valueOf((stopWatchMillis % 1000)/10);
        String seconds = String.valueOf((int) Math.floor((float)stopWatchMillis/1000));
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
