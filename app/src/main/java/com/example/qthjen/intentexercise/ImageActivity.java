package com.example.qthjen.intentexercise;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.Collections;

public class ImageActivity extends Activity {

    private TableLayout tableLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);

        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        /** trộn mảng **/
        Collections.shuffle(MainActivity.arrayList);
        /** tạo table layout = java class **/
        int row = 5;
        int col = 3;
        /** lưu ý ảnh có 15 chiếc thì col*row phải bằng 15 **/
        for ( int i = 1; i <= row; i++) {

            TableRow tableRow = new TableRow(ImageActivity.this);
            /** tạo cột của table layout **/
            for ( int j = 1; j <= col; j++) {

                ImageView imageView = new ImageView(ImageActivity.this);
                /** convert dp sang pixel vì hàm Tablerow.LayoutParams chỉ hiểu được kích thước theo pixel
                Resources resources = getResources();
                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, resources.getDisplayMetrics()); **/

                /** Tạo các layout nhỏ trong table row với chiều cao 180 chiều rộng 180 **/
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(180,180);
                imageView.setLayoutParams(layoutParams);

                /** tạo vị trí ảnh từ 0 đến ảnh cuối cùng **/
                final int indexImage = col*(i - 1) + j - 1;
                // với i = 0 và j = 0 thì indexImage = 0; i = 0 va j = 1 thì indexImage = 1
                // với i = 6 và j = 3 thì indexImage = 17

                int idImage1 = getResources().getIdentifier(MainActivity.arrayList.get(indexImage), "drawable", getPackageName());
                imageView.setImageResource(idImage1);

                // add imageView vào tableRow
                tableRow.addView(imageView);

                /** sự kiện click **/
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("mydata", MainActivity.arrayList.get(indexImage));
                        setResult(RESULT_OK, intent); // gán kết quả
                        finish();
                    }
                });

            }
            // add tablerow vào tablelayout
            tableLayout.addView(tableRow);
        }

    }
}
