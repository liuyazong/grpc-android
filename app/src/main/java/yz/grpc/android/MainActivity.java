package yz.grpc.android;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;
import yz.grpc.proto.service.compute.ComputeServiceGrpc;
import yz.grpc.proto.service.compute.InputMessage;
import yz.grpc.proto.service.compute.OutputMessage;

public class MainActivity extends AppCompatActivity {

    private ManagedChannel managedChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView host = findViewById(R.id.host);
        TextView port = findViewById(R.id.port);

        managedChannel = OkHttpChannelBuilder.forAddress(host.getText().toString(), Integer.valueOf(port.getText().toString())).usePlaintext(true).build();

        findViewById(R.id.btn_blocking_call).setOnClickListener(new View.OnClickListener() {


            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                new AsyncTask<Void, Void, Double>() {

                    EditText num1;
                    EditText num2;
                    TextView result;
                    TextView operator;

                    @Override
                    protected void onPreExecute() {
                        num1 = findViewById(R.id.et_num1);
                        num2 = findViewById(R.id.et_num2);
                        result = findViewById(R.id.tv_result);
                        operator = findViewById(R.id.tv_operator);
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onPostExecute(Double r) {
                        result.setText(r.toString());
                        operator.setText("+");
                    }

                    @Override
                    protected Double doInBackground(Void... voids) {
                        ComputeServiceGrpc.ComputeServiceBlockingStub stub = ComputeServiceGrpc.newBlockingStub(managedChannel);
                        return
                                stub.add(
                                        InputMessage
                                                .newBuilder()
                                                .setNumA(Double.valueOf(num1.getText().toString()))
                                                .setNumB(Double.valueOf(num2.getText().toString()))
                                                .build()
                                ).getResult();
                    }
                }.execute();

            }
        });

        findViewById(R.id.btn_async_unary_call).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                new AsyncTask<Void, Void, Void>() {

                    EditText num1;
                    EditText num2;
                    TextView result;
                    TextView operator;

                    @Override
                    protected void onPreExecute() {
                        num1 = findViewById(R.id.et_num1);
                        num2 = findViewById(R.id.et_num2);
                        result = findViewById(R.id.tv_result);
                        operator = findViewById(R.id.tv_operator);
                    }

                    @Override
                    protected void onPostExecute(Void r) {
                        operator.setText("+");
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ComputeServiceGrpc.ComputeServiceStub stub = ComputeServiceGrpc.newStub(managedChannel);
                        stub.add(InputMessage
                                        .newBuilder()
                                        .setNumA(Double.valueOf(num1.getText().toString()))
                                        .setNumB(Double.valueOf(num2.getText().toString()))
                                        .build(),
                                new StreamObserver<OutputMessage>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onNext(OutputMessage value) {
                                        result.setText(value.getResult() + "");
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onCompleted() {

                                    }
                                });
                        return null;
                    }
                }.execute();
            }
        });
        findViewById(R.id.btn_async_client_streaming_call).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {

                    EditText num1;
                    EditText num2;
                    TextView result;
                    TextView operator;

                    @Override
                    protected void onPreExecute() {
                        num1 = findViewById(R.id.et_num1);
                        num2 = findViewById(R.id.et_num2);
                        result = findViewById(R.id.tv_result);
                        operator = findViewById(R.id.tv_operator);
                    }

                    @Override
                    protected void onPostExecute(Void r) {
                        operator.setText("-");
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ComputeServiceGrpc.ComputeServiceStub stub = ComputeServiceGrpc.newStub(managedChannel);
                        StreamObserver<InputMessage> subtract = stub.subtract(new StreamObserver<OutputMessage>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onNext(OutputMessage value) {
                                result.setText(value.getResult() + "");
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onCompleted() {

                            }
                        });
                        subtract.onNext(InputMessage
                                .newBuilder()
                                .setNumA(Double.valueOf(num1.getText().toString()))
                                .setNumB(Double.valueOf(num2.getText().toString()))
                                .build());
                        subtract.onCompleted();
                        return null;
                    }
                }.execute();
            }
        });

        findViewById(R.id.btn_async_server_streaming_call).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {

                    EditText num1;
                    EditText num2;
                    TextView result;
                    TextView operator;

                    @Override
                    protected void onPreExecute() {
                        num1 = findViewById(R.id.et_num1);
                        num2 = findViewById(R.id.et_num2);
                        result = findViewById(R.id.tv_result);
                        operator = findViewById(R.id.tv_operator);
                    }

                    @Override
                    protected void onPostExecute(Void r) {
                        operator.setText("*");
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ComputeServiceGrpc.ComputeServiceStub stub = ComputeServiceGrpc.newStub(managedChannel);

                        stub.multiply(InputMessage
                                        .newBuilder()
                                        .setNumA(Double.valueOf(num1.getText().toString()))
                                        .setNumB(Double.valueOf(num2.getText().toString()))
                                        .build(),
                                new StreamObserver<OutputMessage>() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onNext(OutputMessage value) {
                                        result.setText(value.getResult() + "");
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onCompleted() {

                                    }
                                });
                        return null;
                    }
                }.execute();
            }
        });

        findViewById(R.id.btn_async_bidi_streaming_call).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Void>() {

                    EditText num1;
                    EditText num2;
                    TextView result;
                    TextView operator;

                    @Override
                    protected void onPreExecute() {
                        num1 = findViewById(R.id.et_num1);
                        num2 = findViewById(R.id.et_num2);
                        result = findViewById(R.id.tv_result);
                        operator = findViewById(R.id.tv_operator);
                    }

                    @Override
                    protected void onPostExecute(Void r) {
                        operator.setText("/");
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        ComputeServiceGrpc.ComputeServiceStub stub = ComputeServiceGrpc.newStub(managedChannel);

                        StreamObserver<InputMessage> divide = stub.divide(new StreamObserver<OutputMessage>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onNext(OutputMessage value) {
                                result.setText(value.getResult() + "");
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onCompleted() {

                            }
                        });
                        divide.onNext(InputMessage
                                .newBuilder()
                                .setNumA(Double.valueOf(num1.getText().toString()))
                                .setNumB(Double.valueOf(num2.getText().toString()))
                                .build());
                        divide.onCompleted();
                        return null;
                    }
                }.execute();
            }
        });

        findViewById(R.id.btn_future_unary_call).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                new AsyncTask<Void, Void, Double>() {

                    EditText num1;
                    EditText num2;
                    TextView result;
                    TextView operator;

                    @Override
                    protected void onPreExecute() {
                        num1 = findViewById(R.id.et_num1);
                        num2 = findViewById(R.id.et_num2);
                        result = findViewById(R.id.tv_result);
                        operator = findViewById(R.id.tv_operator);
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onPostExecute(Double r) {
                        result.setText(r.toString());
                        operator.setText("+");
                    }

                    @Override
                    protected Double doInBackground(Void... voids) {
                        ComputeServiceGrpc.ComputeServiceFutureStub stub = ComputeServiceGrpc.newFutureStub(managedChannel);

                        try {
                            OutputMessage outputMessage = stub.add(InputMessage
                                    .newBuilder()
                                    .setNumA(Double.valueOf(num1.getText().toString()))
                                    .setNumB(Double.valueOf(num2.getText().toString()))
                                    .build()).get();
                            return outputMessage.getResult();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        return 0D;
                    }
                }.execute();
            }
        });

    }
}
