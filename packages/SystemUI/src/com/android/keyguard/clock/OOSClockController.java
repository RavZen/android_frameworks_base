package com.android.keyguard.clock;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.settingslib.Utils;
import com.android.systemui.R;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.plugins.ClockPlugin;
import java.util.TimeZone;
import android.graphics.BitmapFactory;
import android.text.Html;

public class OOSClockController implements ClockPlugin {
    private ClockLayout mBigView;
    private int mColor;
    private final SysuiColorExtractor mColorExtractor;
    private TextClock mDate;
    private TextClock mDay;
    private final LayoutInflater mLayoutInflater;
    private final ViewPreviewer mRenderer = new ViewPreviewer();
    private final Resources mResources;
    private TextClock mTimeClock;
    private TextClock mTimeClockAccented;
    private ClockLayout mView;
    private Context mContext;

    
    public String getName() {
        return "oos";
    }

    
    public String getTitle() {
        return "OxygenOS";
    }

    
    public void onTimeZoneChanged(TimeZone timeZone) {
    }

    
    public boolean usesPreferredY() {
        return true;
    }

    
    public void setStyle(Paint.Style style) {
    }

    
    public boolean shouldShowStatusArea() {
        return true;
    }

    public OOSClockController(Resources resources, LayoutInflater layoutInflater, SysuiColorExtractor sysuiColorExtractor, Context context) {
        mResources = resources;
        mLayoutInflater = layoutInflater;
        mColorExtractor = sysuiColorExtractor;
        mContext = context;
    }

    private void createViews() {
        mBigView = (ClockLayout) mLayoutInflater.inflate(R.layout.digital_clock_oos_big, (ViewGroup) null);
        ClockLayout clockLayout = (ClockLayout) mLayoutInflater.inflate(R.layout.digital_clock_oos, (ViewGroup) null);
        mView = clockLayout;
        setViews(clockLayout);
    }

    private void setViews(View view) {
        mTimeClock = (TextClock) view.findViewById(R.id.time_clock);
        mTimeClockAccented = (TextClock) view.findViewById(R.id.time_clock_accented);
        mDay = (TextClock) view.findViewById(R.id.clock_day);

        mTimeClockAccented.setFormat12Hour(Html.fromHtml("<strong>h</strong>"));
        mTimeClockAccented.setFormat24Hour(Html.fromHtml("<strong>kk</strong>"));
    }

    
    public void onDestroyView() {
        mView = null;
        mTimeClock = null;
        mDay = null;
        mTimeClockAccented = null;
    }


    
    public Bitmap getThumbnail() {
        return BitmapFactory.decodeResource(mResources, R.drawable.default_thumbnail);
    }

    
    public Bitmap getPreview(int width, int height) {
        View inflate = mLayoutInflater.inflate(R.layout.digital_clock_oos_preview, (ViewGroup) null);
        setViews(inflate);
        ColorExtractor.GradientColors colors = this.mColorExtractor.getColors(2);
        setColorPalette(colors.supportsDarkText(), colors.getColorPalette());
        onTimeTick();
        return mRenderer.createPreview(inflate, width, height);
    }

    
    public View getView() {
        if (mView == null) {
            createViews();
        }
        return mView;
    }

    
    public View getBigClockView() {
        return mBigView;
    }


    public int getPreferredY(int totalHeight) {
        return totalHeight / 2;
    }


    public void setTextColor(int color) {
        int mAccentColor = mContext.getResources().getColor(R.color.lockscreen_clock_accent_color);
        int mWhiteColor = mContext.getResources().getColor(R.color.lockscreen_clock_white_color);

        mTimeClock.setTextColor(mWhiteColor);
        mTimeClockAccented.setTextColor(mWhiteColor);
        mDay.setTextColor(mWhiteColor);
        mColor = color;
    }

    
    public void setColorPalette(boolean supportsDarkText, int[] colorPalette) {
    }

    
    public void onTimeTick() {
        ClockLayout clockLayout = mView;
        if (clockLayout != null) {
            clockLayout.onTimeChanged();
        }
        ClockLayout clockLayout2 = mBigView;
        if (clockLayout2 != null) {
            clockLayout2.onTimeChanged();
        }
        mTimeClock.refreshTime();
        mTimeClockAccented.refreshTime();
        mDay.refreshTime();
        setTextColor(mColor);
    }


    public void setDarkAmount(float darkAmount) {
        int mWhiteColor = mContext.getResources().getColor(R.color.lockscreen_clock_white_color);

        mTimeClock.setTextColor(mWhiteColor);
        mTimeClockAccented.setTextColor(mWhiteColor);
    }
}
