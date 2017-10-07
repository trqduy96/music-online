package com.t3h.mediaonline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inits();
    }

    private void inits() {
        FragmentManager manager = getSupportFragmentManager();

        FragmentSearch search = new FragmentSearch();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.frame_layout, search, FragmentSearch.class.getName());
        transaction.commit();
    }

    public void openSearch() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(FragmentSearch.class.getName());

        if (fragment != null) {
            if (fragment.isVisible()) {
            } else {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame_layout, fragment, FragmentSearch.class.getName());
                transaction.addToBackStack(FragmentSearch.class.getName());
                transaction.commit();
            }
            return;
        }

        FragmentTransaction transaction = manager.beginTransaction();
        FragmentSearch search = new FragmentSearch();

        transaction.replace(R.id.frame_layout, search, FragmentSearch.class.getName());
        transaction.addToBackStack(FragmentSearch.class.getName());
        transaction.commit();


    }

    public void openPlayFragment(ItemSongOnline itemSongOnline) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(FragmentPlay.class.getName());

        if (fragment != null) {
            if (fragment.isVisible()) {
            } else {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frame_layout, fragment, FragmentPlay.class.getName());
                transaction.addToBackStack(FragmentSearch.class.getName());
                transaction.commit();
            }
            return;
        }

        FragmentTransaction transaction = manager.beginTransaction();
        FragmentPlay fragmentPlay = new FragmentPlay();

        fragmentPlay.setSongOnline(itemSongOnline);
        transaction.replace(R.id.frame_layout, fragmentPlay, FragmentPlay.class.getName());
        transaction.addToBackStack(FragmentSearch.class.getName());
        transaction.commit();


    }

}