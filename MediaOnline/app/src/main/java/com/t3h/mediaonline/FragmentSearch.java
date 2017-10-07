package com.t3h.mediaonline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentController;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class FragmentSearch extends Fragment implements SongOnlineAdapter.ISongOnlineAdapter, TextWatcher {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SongOnlineAdapter adapter;
    private List<ItemSongOnline> songOnlines;
    private RecyclerView rcSong;
    private EditText edtNameSong;
    private Disposable disposable;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view
                = inflater.inflate(R.layout.fragment_search, container, false);

        initView(view);
        return view;
    }


    private void initView(View view) {
        rcSong = (RecyclerView) view.findViewById(R.id.rc_song);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        rcSong.setLayoutManager(manager);
        adapter = new SongOnlineAdapter(this);
        rcSong.setAdapter(adapter);

        edtNameSong = (EditText) view.findViewById(R.id.edt_name_song);
        edtNameSong.addTextChangedListener(this);
    }

    @Override
    public int getCount() {
        if (songOnlines == null) {
            return 0;
        }
        return songOnlines.size();
    }

    @Override
    public ItemSongOnline getData(int position) {
        return songOnlines.get(position);
    }

    @Override
    public void onClickItem(int position) {

        ((MainActivity)getActivity()).openPlayFragment(songOnlines.get(position));


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }

        final String content = edtNameSong.getText().toString().trim();
        final String link = "http://j.ginggong.com/jOut.ashx?k="
                + content + "&h=mp3.zing.vn&code=sdfdsf";

        Observable<List<ItemSongOnline>> observable = Observable.create(new ObservableOnSubscribe<List<ItemSongOnline>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<ItemSongOnline>> e) throws Exception {
                //lay danh sach nhac
                //thuc hien tren thread A
                //1

                try {
                    URL url = new URL(link);
                    InputStream in = url.openStream();
                    String content = "";
                    byte b[] = new byte[1024];
                    int le = in.read(b);

                    while (le >= 0) {
                        content = content + new String(b, 0, le);
                        le = in.read(b);
                    }
                    Log.d(TAG, "subscribe: content: " + content);
                    in.close();

                    if (content.equals("")) {
                        e.onNext(new ArrayList<ItemSongOnline>());
                        return;
                    }
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<ItemSongOnline>>() {
                    }.getType();
                    List<ItemSongOnline> songOnlines = gson.fromJson(content, type);
                    if (songOnlines == null) {
                        Log.d(TAG, "subscribe: null........................");
                    } else {
                        Log.d(TAG, "subscribe:size: " + songOnlines.size());
                        e.onNext(songOnlines);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.newThread())//thread A
                .observeOn(AndroidSchedulers.mainThread()); //thread B
        //kich hoat phuong thuc subscribe trong observable thuc hien

        disposable = observable.subscribe(new Consumer<List<ItemSongOnline>>() {

            @Override
            public void accept(List<ItemSongOnline> itemSongOnlines) throws Exception {

                //thuc hien tren thread B
                songOnlines = itemSongOnlines;
                adapter.notifyDataSetChanged();
            }
        });


    }
}
