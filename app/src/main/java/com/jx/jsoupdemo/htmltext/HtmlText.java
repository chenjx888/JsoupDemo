package com.jx.jsoupdemo.htmltext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jx.jsoupdemo.ImageActivity;
import com.jx.jsoupdemo.R;
import com.jx.jsoupdemo.htmltext.span.ImageClickSpan;
import com.jx.jsoupdemo.htmltext.span.LinkClickSpan;

import java.util.ArrayList;
import java.util.List;

public class HtmlText {

    // 默认加载图片
    private static final int DEFAULT_IMAGE = R.drawable.ic_launcher_background;

    private HtmlImageLoader imageLoader;
    private OnTagClickListener onTagClickListener;
    private After after;
    private String source;

    private HtmlText(String source) {
        this.source = source;
    }

    /**
     * 设置源文本
     */
    public static HtmlText from(String source) {
        return new HtmlText(source);
    }

    /**
     * 设置加载器
     */
    public HtmlText setImageLoader(final Context context, final TextView tv) {
        imageLoader = new HtmlImageLoader() {
            @Override
            public void loadImage(String url, final Callback callback) {
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                callback.onLoadComplete(resource);
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                callback.onLoadFailed();
                            }
                        });
            }

            @Override
            public Drawable getDefaultDrawable() {
                return ContextCompat.getDrawable(context, DEFAULT_IMAGE);
            }

            @Override
            public Drawable getErrorDrawable() {
                return ContextCompat.getDrawable(context, DEFAULT_IMAGE);
            }

            @Override
            public int getMaxWidth() {
                return getTextWidth(context, tv);
            }

            @Override
            public boolean fitWidth() {
                return false;
            }
        };
        return this;
    }

    private int getTextWidth(Context context, TextView tv) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels - tv.getPaddingLeft() - tv.getPaddingRight();
    }

    /**
     * 设置图片、链接点击监听器
     */
    public HtmlText setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
        return this;
    }

    /**
     * 对处理完成的文本再次处理
     */
    public HtmlText after(After after) {
        this.after = after;
        return this;
    }

    /**
     * 注入TextView
     */
    public void into(TextView textView) {
        if (TextUtils.isEmpty(source)) {
            textView.setText("");
            return;
        }

        //设置图片可以点击
        textView.setMovementMethod(TextLinkMovementMethod.getInstance());

        HtmlImageGetter imageGetter = new HtmlImageGetter();
        HtmlTagHandler tagHandler = new HtmlTagHandler();
        List<String> imageUrls = new ArrayList<>();

        imageGetter.setTextView(textView);
        imageGetter.setImageLoader(imageLoader);
        imageGetter.getImageSize(source);

        tagHandler.setTextView(textView);
        source = tagHandler.overrideTags(source);

        Spanned spanned = Html.fromHtml(source, imageGetter, tagHandler);
        SpannableStringBuilder ssb;
        if (spanned instanceof SpannableStringBuilder) {
            ssb = (SpannableStringBuilder) spanned;
        } else {
            ssb = new SpannableStringBuilder(spanned);
        }

        // Hold image url link
        imageUrls.clear();
        ImageSpan[] imageSpans = ssb.getSpans(0, ssb.length(), ImageSpan.class);
        for (int i = 0; i < imageSpans.length; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String imageUrl = imageSpan.getSource();
            int start = ssb.getSpanStart(imageSpan);
            int end = ssb.getSpanEnd(imageSpan);
            imageUrls.add(imageUrl);

            ImageClickSpan imageClickSpan = new ImageClickSpan(textView.getContext(), imageUrls, i);
            imageClickSpan.setListener(new OnTagClickListener() {
                @Override
                public void onImageClick(Context context, List<String> imageUrlList, int position) {
                    ImageActivity.start(context, imageUrlList.get(position));
                }

                @Override
                public void onLinkClick(Context context, String url) {

                }
            });
            ClickableSpan[] clickableSpans = ssb.getSpans(start, end, ClickableSpan.class);
            if (clickableSpans != null) {
                for (ClickableSpan cs : clickableSpans) {
                    ssb.removeSpan(cs);
                }
            }
            ssb.setSpan(imageClickSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // Hold text url link
        URLSpan[] urlSpans = ssb.getSpans(0, ssb.length(), URLSpan.class);
        if (urlSpans != null) {
            for (URLSpan urlSpan : urlSpans) {
                int start = ssb.getSpanStart(urlSpan);
                int end = ssb.getSpanEnd(urlSpan);
                ssb.removeSpan(urlSpan);
                LinkClickSpan linkClickSpan = new LinkClickSpan(textView.getContext(), urlSpan.getURL());
                linkClickSpan.setListener(onTagClickListener);
                ssb.setSpan(linkClickSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }

        CharSequence charSequence = ssb;
        if (after != null) {
            charSequence = after.after(ssb);
        }

        textView.setText(charSequence);
    }

    public interface After {
        CharSequence after(SpannableStringBuilder ssb);
    }
}
