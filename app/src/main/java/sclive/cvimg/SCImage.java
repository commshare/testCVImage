package sclive.cvimg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Administrator on 2016/9/4.
 */
public class SCImage {
    private String TAG="SCImage";
    Bitmap b;
    int w;
    int h;
    public SCImage(Bitmap b) {
        this.b = b;
        this.w=b.getWidth();
        this.h=b.getHeight();
        Log.i(TAG,"bitmap w["+w+"] h["+h+"]");
    }
    public  SCImage(String srcPath){
        Bitmap curBmp= BitmapFactory.decodeFile(srcPath);
        if(curBmp!=null) {
            this.b = curBmp;
            this.w = b.getWidth();
            this.h = b.getHeight();
            Log.i(TAG, "bitmap w[" + w + "] h[" + h + "]");
        }else
            Log.e(TAG,"create bitmap fail");
    }
    public Bitmap getB(){
        return b;
    }
    public int getW(){
        return w;
    }
    public int getH(){
        return h;
    }
}
