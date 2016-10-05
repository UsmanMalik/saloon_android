package com.example.android.materialdesigncodelab.UI.Utils;

import com.example.android.materialdesigncodelab.UI.Models.Division;

import java.util.ArrayList;

/**
 * Created by usman on 10/5/16.
 */
public class GlobalVariables {


    private ArrayList<Division> arrayList;

    private static GlobalVariables instance;

    private GlobalVariables(){
        arrayList = new ArrayList<Division>();
    }

    public static GlobalVariables getInstance(){
        if (instance == null){
            instance = new GlobalVariables();
        }
        return instance;
    }

    public ArrayList<Division> getArrayList() {

        return arrayList;
    }

    public void setArrayList(ArrayList<Division> list){
        this.arrayList = list;
    }



}
