package sclive.cvimg;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String TAG = "Main Activity";
    private static int RESULT_LOAD_SOURCE = 1;
    //String SC_INTENT_ANALYSE =""

    Button mBtnLoadPic;
    Button mBtnAnalysePic;
//    ImageView mSrcImg;
    MyImageView mSrcImg;
    TextView mTextXY;
    SCImage curImg;
    private Drawable mDrawable;
    /**
     * 图片宽度（使用前判断mDrawable是否null）
     */
    int mDrawableWidth;
    /**
     * 图片高度（使用前判断mDrawable是否null）
     */
    int mDrawableHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mBtnLoadPic= (Button)findViewById(R.id.BtnLoadPic);
        mBtnAnalysePic=(Button)findViewById(R.id.BtnAnalysePic);
//        mSrcImg=(ImageView)findViewById(R.id.SrcImage);
        mSrcImg=(MyImageView)findViewById(R.id.SrcImage);
        mTextXY=(TextView)findViewById(R.id.TextXY);
        /*设置背景色为红色，为了方便确定图片是怎么缩放的，以及控件的大小*/
        mSrcImg.setBackgroundColor(Color.parseColor("#ff0000"));
        /**
         * 拿到src的图片
         */
          mDrawable=mSrcImg.getBackground();

        if (mDrawable == null)
            return;
        mDrawableWidth = mDrawable.getIntrinsicWidth();
        mDrawableHeight = mDrawable.getIntrinsicHeight();
        /*
        * Activity: mDrawableWidth[-1] mDrawableHeight[-1] mSrcImg.getWidth()[0] mSrcImg.getHeight[0]
        * */
        Log.d(TAG,"`1 mDrawableWidth["+mDrawableWidth+"] mDrawableHeight["+mDrawableHeight+"] mSrcImg.getWidth()["+mSrcImg.getWidth()+"] mSrcImg.getHeight["+mSrcImg.getHeight()+"]");

        setClick();
        /*
        * refer to http://stackoverflow.com/questions/33030933/android-6-0-open-failed-eacces-permission-denied
        * should ask for permission after Android 6
        * */
        if(shouldAskPermission()){
            verifyStoragePermissions(this);
        }else
            Log.d(TAG,"should not ask for permission");
    }
    private boolean shouldAskPermission(){
        return(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    void setClick(){
        mBtnLoadPic.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,RESULT_LOAD_SOURCE);
            }
        });
        mBtnAnalysePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,ImageProcActivity.class);
                startActivity(i);
            }
        });
        Log.d(TAG,"2 mDrawableWidth["+mDrawableWidth+"] mDrawableHeight["+mDrawableHeight+"] mSrcImg.getWidth()["+mSrcImg.getWidth()+"] mSrcImg.getHeight["+mSrcImg.getHeight()+"]");

//        mSrcImg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                int action = motionEvent.getAction();
//                int x = (int)motionEvent.getX();
//                int y=(int)motionEvent.getY();
//                switch (action){
//                    case MotionEvent.ACTION_DOWN:
//                        Log.d(TAG,"DOWN");
//                        mTextXY.setText("DOWN " + x + " : " + y);
//                        int width=mSrcImg.getWidth();
//                        Bitmap t=curImg.Scale(width);
//                        mSrcImg.setImageBitmap(t);
//                        break;
//                    case MotionEvent.ACTION_MOVE://(移动)
//                        break;
//                    case MotionEvent.ACTION_UP://(抬起)
//                        break;
//                    default:
//                        Log.d(TAG,"event:"+action);
//                        break;
//                }
//     /*
//     * Return 'true' to indicate that the event have been consumed.
//     * If auto-generated 'false', your code can detect ACTION_DOWN only,
//     * cannot detect ACTION_MOVE and ACTION_UP.
//     */
//                return true;
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String srcPath;
        if(data==null){
            Log.d("zb","not pick  pic");
            return ;
        }
        Log.d(TAG, "SRC Image selected .");
        Uri selectedImage = data.getData();
        /*数据库路径字段 这一列的数据存到 字符串数组*/
        String[] filePathColumn = { MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

        try {
            cursor.moveToFirst();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        /*找到了，拿到数组下标*/
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        /*根据下标拿到路径*/
        String picPath = cursor.getString(columnIndex);
        /*关闭迭代器*/
        cursor.close();
        if(requestCode == RESULT_LOAD_SOURCE) {
            srcPath=picPath;
            /*TODO 加一个判断或者异常，如果路径不对，等原因导致set失败*/
             curImg= new SCImage(srcPath);
            Bitmap curBmp=curImg.getB();//BitmapFactory.decodeFile(srcPath);
            if(curBmp!=null) {
/*                mSrcImg.setImageBitmap(curBmp);
                Log.d(TAG,"3 mDrawableWidth["+mDrawableWidth+"] mDrawableHeight["+mDrawableHeight+"] mSrcImg.getWidth()["+mSrcImg.getWidth()+"] mSrcImg.getHeight["+mSrcImg.getHeight()+"]");

                int w=mSrcImg.getWidth(); //984
                int h=mSrcImg.getHeight();//1293

                mTextXY.setText("W "+curImg.getW()+" H "+curImg.
                getH() +" view["+w+"*"+h+"]");*/

                int w=curImg.getW();
                int h=curImg.getH();
                Log.d("zb","src bitmap w "+w+"h "+h);
                mSrcImg.MyInit(w,h);
                mSrcImg.setImageBitmap(curBmp);


            }else
                mTextXY.setText("load image error ");
        }
    }
    }
