package ru.mrsu.myapplicationasynctask;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb;
    TextView tv;
    MyTask mt;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = (ProgressBar)findViewById(R.id.progressBar);
        tv = (TextView)findViewById(R.id.textView);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                old_task();

                //mt = new MyTask();
                //mt.execute(20);
            }
        });

        Button btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mt != null) {
                    mt.cancel(true);
                }
            }
        });

        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String s = String.valueOf(msg.what);
                tv.setText(s);
            }
        };
    }

    void old_task() {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                int r = MyUtil.fib(20);
                String s = String.valueOf(r);
                Log.d("FIB2", s);

                Message m = new Message();
                m.what = r;
                h.sendMessageDelayed(m, 1000);
            }
        };

        Thread t = new Thread(r);
        t.run();
    }

    class MyTask extends AsyncTask<Integer, Integer, Integer> {

        int count = 0;

        @Override
        protected Integer doInBackground(Integer... integers) {
            count++;
            publishProgress(count);
            if (count > 10000) {
                cancel(true);
            }
            return MyUtil.fib(integers[0]);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            String res = String.valueOf(values[0]);
            tv.setText(res);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            tv.setText("Canceled");
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            String res = String.valueOf(integer);
            tv.setText(res);
        }
    }

}
