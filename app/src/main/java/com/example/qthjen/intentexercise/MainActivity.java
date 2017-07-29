package com.example.qthjen.intentexercise;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private       ImageView         ivAbove;
    private       ImageView         ivBelow;
    public  static ArrayList<String> arrayList;  // để class imageActivity có thể truy cập được
    private       String            imageSources = "";
            int                     MY_REQUEST_CODE = 13;
    private       TextView          tvScores;
    private int                     scores = 100;
    private       SharedPreferences myScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();

        myScores = getSharedPreferences("myscores", MODE_PRIVATE);

        // nhận điểm
        scores = myScores.getInt("scores", 100);

        tvScores.setText("Scores: " + scores);
        click();
        reloadImageAbove();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.reload, menu);
        return super.onCreateOptionsMenu(menu);

    }

    private void findView() {

        ivAbove  = (ImageView) findViewById(R.id.ivAbove);
        ivBelow  = (ImageView) findViewById(R.id.ivBelow);
        tvScores = (TextView)  findViewById(R.id.tvScores);

    }

    private void click() {

        ivBelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, ImageActivity.class), MY_REQUEST_CODE );
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            String imageSecond = data.getStringExtra("mydata");
            int idImageSecond = getResources().getIdentifier(imageSecond, "drawable", getPackageName());
            ivBelow.setImageResource(idImageSecond);

            if ( imageSecond.equals(imageSources)) {
                Toast.makeText(MainActivity.this, "You are choose exactly \n plus 10 scores", Toast.LENGTH_SHORT).show();

   //              nếu đúng tự động chuyển hình sau 2s
                new CountDownTimer(2000,100) {

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        reloadImageAbove();
                    }
                }.start();

                scores += 10;
                saveScores();
                tvScores.setText("Scores: " + scores);

            } else {
                scores -= 20;
                saveScores();
                Toast.makeText(MainActivity.this, "You are choose error \n minus 20 scores", Toast.LENGTH_SHORT).show();
                tvScores.setText("Scores: " + scores);
            }
        }

        /** thoát mà ko chọn bị trừ điểm **/
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "You are not choose image \n you minus 25 scores", Toast.LENGTH_SHORT).show();
            scores -= 25;
            saveScores();
            tvScores.setText("Scores: " + scores);
        }

        if ( scores <= -200 ) {

            /** alert dialog **/
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
//            alertDialog.setTitle("Message");
//            alertDialog.setMessage("You are playing again");
//
//            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    reloadImageAbove();
//                    scores = 100;
//                    tvScores.setText("Scores: " + scores);
//                }
//            });
//
//            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                }
//            });
//            alertDialog.show();

            /** custom dialog **/
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog);
            dialog.show();

            Button btYes = (Button) dialog.findViewById(R.id.btYes);
            Button btNo  = (Button) dialog.findViewById(R.id.btNo);

            btYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reloadImageAbove();
                    scores = 100;
                    tvScores.setText("Scores: " + scores);
                    dialog.cancel();
                }
            });

            btNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch( item.getItemId() ) {
            case R.id.mnReload:
                reloadImageAbove();
                scores = 100;
                tvScores.setText("Scores: " + scores);
                Toast.makeText(MainActivity.this, "Please choose again in image below", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void reloadImageAbove() {
        /** tên ảnh trong drawable phải trùng tên trong list_name trong string **/
        String[] listName = getResources().getStringArray(R.array.list_name);
        arrayList = new ArrayList<>(Arrays.asList(listName));

        // xáo trộn mảng
        Collections.shuffle(arrayList);
        imageSources = arrayList.get(5);
        /** tạo id cho image từ mảng tên **/
        int idImage = getResources().getIdentifier(arrayList.get(5), "drawable", getPackageName()); // 5 là ảnh thứ 5, key là drawable mặc định
        ivAbove.setImageResource(idImage);

    }

    private void saveScores() {

        SharedPreferences.Editor editor = myScores.edit();
        editor.putInt("scores", scores);
        editor.commit();

    }


}
