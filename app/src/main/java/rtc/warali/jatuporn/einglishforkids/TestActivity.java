package rtc.warali.jatuporn.einglishforkids;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    //Explicit
    private int timesAnInt, scoreAnInt;
    private MyConstant myConstant;
    private int[][] choiceInts;
    private ImageView firstImageView, secondImageView, thirdImageView;
    private TextView questionTextView, scoreTextView;
    private String[] questionStrings;
    private MyManage myManage;
    private int[] trueAnswerInts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        myManage = new MyManage(TestActivity.this);

        timesAnInt = getIntent().getIntExtra("Times", 0);
        Log.d("4decV1", "timesAnInt ที่รับได้ ==> " + timesAnInt);

        myConstant = new MyConstant();
        choiceInts = myConstant.getChoiceInts();
        trueAnswerInts = myConstant.getTrueAnserInts();

        bindWidget();

        //Show View
        showView();

        //ShowQuestion
        questionStrings = myConstant.getQurstionStrings();
        questionTextView.setText(questionStrings[timesAnInt]);

        //Image Controller
        firstImageView.setOnClickListener(this);
        secondImageView.setOnClickListener(this);
        thirdImageView.setOnClickListener(this);





    }   // Main Method

    private void showView() {

        firstImageView.setImageResource(choiceInts[timesAnInt][0]);
        secondImageView.setImageResource(choiceInts[timesAnInt][1]);
        thirdImageView.setImageResource(choiceInts[timesAnInt][2]);

        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM kidTABLE", null);
            cursor.moveToFirst();
            String strScore = cursor.getString(cursor.getColumnIndex(MyManage.column_score));
            scoreTextView.setText(strScore);
            scoreAnInt = Integer.parseInt(strScore);

            if (scoreAnInt == 10) {
                myAlert();
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void bindWidget() {

        firstImageView = (ImageView) findViewById(R.id.imageView4);
        secondImageView = (ImageView) findViewById(R.id.imageView5);
        thirdImageView = (ImageView) findViewById(R.id.imageView6);
        questionTextView = (TextView) findViewById(R.id.textView4);
        scoreTextView = (TextView) findViewById(R.id.textView3);

    }

    @Override
    public void onClick(View view) {

        int intChoose = 1;

        switch (view.getId()) {
            case R.id.imageView4:
                intChoose = 1;
                break;
            case R.id.imageView5:
                intChoose = 2;
                break;
            case R.id.imageView6:
                intChoose = 3;
                break;
        }   // switch

        Log.d("4decV2", "You Choose ==> " + intChoose);
        Log.d("4decV2", "True Answer ==> " + trueAnswerInts[timesAnInt]);

        if (intChoose == trueAnswerInts[timesAnInt]) {
            scoreAnInt += 1;

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            sqLiteDatabase.delete(MyManage.database_table, null, null);

            myManage.addValue(Integer.toString(scoreAnInt));

            Log.d("4decV3", "scoreAnInt ==> " + scoreAnInt);


        }

        finish();

    }   // onClick

    private void myAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(TestActivity.this);
        builder.setCancelable(false);
        builder.setIcon(R.drawable.doremon48);
        builder.setTitle("ยินดีด้วย ได้ 10 เหลียนแว้ว");
        builder.setMessage("ไป Shop ไหม ?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(TestActivity.this, Shop.class));
            }
        });
        builder.show();

    }

}   // Main Class
