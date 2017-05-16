package me.d3if4096.proto_pa.push.notification;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.google.gson.Gson;

import java.util.Map;

import me.d3if4096.proto_pa.R;
import me.d3if4096.proto_pa.SettingPref;

/**
 * Created by Miraz Nur Ihsan on 03/05/2017.
 */

public class TheftNotification {
    private int ID = 121;
    private Builder builder;

    public TheftNotification(Builder builder) {
        this.builder = builder;
    }

    public void show(){
        NotificationManagerCompat.from(builder.context)
                .notify(ID, builder.mBuilder.build());
    }

    public static class Builder{
        private NotificationCompat.Builder mBuilder;
        private Context context;
        public Builder(Context context){
            this.context = context;
            mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentTitle(context.getString(R.string.notification_theft_title));
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            mBuilder.setAutoCancel(false);
            new SettingPref(context).turnRelayDanger();
        }
        public Builder setPayload(Map<String, String> payload){
            Payload pload = new Gson().fromJson(new Gson().toJson(payload), Payload.class);
            mBuilder.setContentText(pload.getMessage());
            mBuilder.setPriority(Notification.PRIORITY_MAX);
            return this;
        }

        public TheftNotification create(){
            return new TheftNotification(this);
        }
    }

    private class Payload{
        private String message;

        public String getMessage() {
            return message;
        }
    }
}
