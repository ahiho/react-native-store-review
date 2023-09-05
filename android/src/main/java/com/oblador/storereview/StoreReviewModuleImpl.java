package com.oblador.storereview;

import android.os.Parcel;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.Map;
import java.util.HashMap;

import com.google.android.play.core.review.ReviewException;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.review.ReviewInfo;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.model.ReviewErrorCode;

public class StoreReviewModuleImpl {

    public static final String NAME = "RNStoreReview";

    public static void requestReview(ReactApplicationContext context) {
        ReviewManager manager = ReviewManagerFactory.create(context);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            WritableMap params = Arguments.createMap();
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                manager.launchReviewFlow(context.getCurrentActivity(), reviewInfo);
                params.putBoolean("success", true);
                params.putString("reviewInfo", reviewInfo.toString());
            } else {
                String errorMsg = task.getException().getMessage();
                params.putBoolean("success", false);
                params.putString("error", errorMsg);
            }
            context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit("EmitStoreReviewResult", params);
        });
    }

}
