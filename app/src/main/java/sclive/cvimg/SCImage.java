package sclive.cvimg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.graphics.Matrix;
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
        /*
        jpeg_decoder mode 1, colorType 4, w 1280, h 720, sample 1, bsLength 3d805!!
        sclive.cvimg D/skia: jpeg_decoder finish successfully, L:1886!!!*/
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
   public float getScale(int newW,int newH){

        //等比例缩放
         newH = (int) ( (   (float) newW / (float) w) * (float) h);   //以新宽度为基准的比例，计算新高度
       Log.d(TAG,"1 newH ["+newH+"]");
       return  ((float) newW) / w;  //新比例
       // imageMatrix.postScale(scale, scale, 0, 0);// 缩放图片大小
    }

    public Bitmap doScale(float scale,int w,int h){
        Bitmap bmp=this.b;
        //获得Bitmap的高和宽
        int bmpWidth=bmp.getWidth();
        int bmpHeight=bmp.getHeight();
        Log.d(TAG,"scale["+scale+"] w "+w+"h "+h +"bmpWidth " + bmpWidth+"bmpHeight "+bmpHeight);
//设置缩小比例
     //   double scale=0.8;
//计算出这次要缩小的比例
        float  scaleWidth=(float)(w*scale);
         float scaleHeight=(float)(h*scale);
        Log.d(TAG,"resize from w "+w+"h "+h +" to scaleWidth " + scaleWidth+"scaleHeight "+scaleHeight);
//产生resize后的Bitmap对象
        Matrix matrix=new Matrix();
        matrix.postScale(scale, scale, 0, 0);
     //   matrix.postScale(scaleWidth, scaleHeight);
      //  matrix.setScale(0.5f, 0.5f);
        Bitmap resizeBmp=Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
        return resizeBmp;
    }
    public Bitmap Scale(int newW){
        Log.d(TAG,"newW ["+newW+"]");
        int newH=0;
        //等比例缩放
        newH = (int) ( (   (float) newW / (float) w) * (float) h);   //以新宽度为基准的比例，计算新高度
        Log.d(TAG,"1 newH ["+newH+"]");
        float scale=  ((float) newW) / w;  //新比例
        Log.d(TAG,"2 newH ["+newH+"]");
        return doScale(scale,newW,newH);
    }
}
