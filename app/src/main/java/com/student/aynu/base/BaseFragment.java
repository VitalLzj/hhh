package com.student.aynu.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.student.aynu.nohttp.CallServer;
import com.student.aynu.nohttp.HttpListener;
import com.yolanda.nohttp.rest.Request;

import java.util.List;

import butterknife.ButterKnife;

public class BaseFragment extends Fragment {


    public Gson gson;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        gson = new Gson();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public <T> void request(int what, Request<T> request, HttpListener<T> callback, boolean canCancel, boolean isLoading) {
        request.setCancelSign(this);
        CallServer.getRequestInstance().add(getActivity(), what, request, callback, canCancel, isLoading);
    }

}
