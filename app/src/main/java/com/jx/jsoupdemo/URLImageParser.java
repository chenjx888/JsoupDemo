package com.jx.jsoupdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import java.net.URL;

public class URLImageParser implements Html.ImageGetter {
    Context context;
    TextView container;

    /***
     * 构建URLImageParser将运行AsyncTask,刷新容器
     * @param editText
     * @param c
     */
    public URLImageParser(TextView editText, Context c) {
        this.context = c;
        this.container = editText;
    }

    public Drawable getDrawable(String source) {
        URLDrawable urlDrawable = new URLDrawable();

        //TODO ImageLoader
        // 获得实际的源
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

        asyncTask.execute(source);

        //返回引用URLDrawable将改变从src与实际图像标记
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable drawable) {
            this.urlDrawable = drawable;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            // 设置正确的绑定依据HTTP调用的结果
            if (result != null) {

                urlDrawable.setBounds(0, 0, result.getIntrinsicWidth(), result.getIntrinsicHeight());
                urlDrawable.drawable = result;

                // 绘制图像容器
                URLImageParser.this.container.invalidate();

                URLImageParser.this.container.setHeight(URLImageParser.this.container.getHeight() + result.getIntrinsicHeight());

                URLImageParser.this.container.setEllipsize(null);

            }
        }

        /***
         * 得到Drawable的URL
         *
         * @param urlString
         * @return
         */
        public Drawable fetchDrawable(String urlString) {
            try {
                URL url = new URL(urlString);
                //使用Drawable.createFromResourceStream可以使图片按原图大小展示出来不会出现加载后图片变小的情况  ，使用Drawable.createFromStream会。
                Drawable drawable = Drawable.createFromResourceStream(context.getResources(), null, url.openStream(), "src", null);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    return drawable;
                } else {
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    public class URLDrawable extends BitmapDrawable {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}