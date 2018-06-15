package com.example.XXX.myroomwithrxjava;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Func0;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tvCount)
    TextView tvCount;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAge)
    EditText etAge;
    @BindView(R.id.bAdd)
    Button bAdd;
    @BindView(R.id.bGet)
    Button bGet;
    @BindView(R.id.bFind)
    Button bFind;
    @BindView(R.id.bCount)
    Button bCount;
    @BindView(R.id.bDelete)
    Button bDelete;
    @BindView(R.id.bDeleteAll)
    Button bDeleteAll;

    private MyDatabase myDatabase;
    private MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind the view using butterknife
        ButterKnife.bind(this);

        myDatabase = MyDatabase.getInstanceOfMyDataase(this);
        myViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        tvCount.setText(String.valueOf(myViewModel.getCount()));

    }

    private void addUserAsync(MyDatabase db) {
        User tmp = createUser();
        Observable.defer(new Func0<Observable<User>>() {
            @Override
            public Observable<User> call() {
                db.DbDao().insertAll(tmp);
                return Observable.just(tmp);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> Log.d("user added : ", t.toString()),
                        throwable -> throwable.printStackTrace());
    }

    private User createUser() {
        return new User(etName.getText().toString(),
                Integer.parseInt(etAge.getText().toString()));
    }

    private void getOneUser(MyDatabase db) {
        db.DbDao().findByName(etName.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> Log.d("user : ", t.toString()),
                        throwable -> throwable.printStackTrace());
    }

    private void getAllUsers(MyDatabase db) {
        db.DbDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            for (User user : t) {
                                Log.d("user : ", user.toString());
                            }
                        },
                        throwable -> throwable.printStackTrace());
    }

    private void countUsers(MyDatabase db) {
        db.DbDao().countUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                            Log.d("count users : ", t.toString());
                            myViewModel.setCount(t);
                            tvCount.setText(String.valueOf(myViewModel.getCount()));
                        },
                        throwable -> throwable.printStackTrace());
    }

    private void deleteUser(MyDatabase db) {
        String tmp = etName.getText().toString();
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                db.DbDao().deleteByName(tmp);
                return Observable.just(tmp);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> Log.d("user deleted : ", t.toString()),
                        throwable -> throwable.printStackTrace());
    }

    private void clearDatabase(MyDatabase db) {
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                db.DbDao().deleteAllUsers();
                return Observable.just(null);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> Log.d("all users : ", "deleted"),
                        throwable -> throwable.printStackTrace());
    }

    @Override
    protected void onDestroy() {
        MyDatabase.destroyInstance();
        super.onDestroy();
    }

    @OnClick(R.id.bAdd)
    public void onButtonClickAdd(View view) {
        Log.d("onClick : ", "You have clicked ");
        addUserAsync(myDatabase);
    }

    @OnClick(R.id.bGet)
    public void onButtonClickGet(View view) {
        Log.d("onClick : ", "You have clicked ");
        getAllUsers(myDatabase);
    }

    @OnClick(R.id.bFind)
    public void onButtonClickFind(View view) {
        Log.d("onClick : ", "You have clicked ");
        getOneUser(myDatabase);
    }

    @OnClick(R.id.bCount)
    public void onButtonClickCount(View view) {
        Log.d("onClick : ", "You have clicked ");
        countUsers(myDatabase);

    }

    @OnClick(R.id.bDelete)
    public void onButtonClickDelete(View view) {
        Log.d("onClick : ", "You have clicked ");
        deleteUser(myDatabase);
    }

    @OnClick(R.id.bDeleteAll)
    public void onButtonClickDAll(View view) {
        Log.d("onClick : ", "You have clicked ");
        clearDatabase(myDatabase);
    }


}
