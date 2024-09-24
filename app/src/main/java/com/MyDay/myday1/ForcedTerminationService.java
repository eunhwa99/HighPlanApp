package com.MyDay.myday1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * 서비스 구현 부분, 타이머 구현 시 사용되어서 작성하였습니다.
 */
public class ForcedTerminationService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
      sendMessage();
        stopSelf(); //서비스 종료
    }

    private void sendMessage() {
        Intent intent = new Intent("naminsik");
        intent.putExtra("message", "전달하고자 하는 데이터");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
