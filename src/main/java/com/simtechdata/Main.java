package com.simtechdata;

public class Main {
    public static void main(String[] args) throws Exception {
        SQLite sqLite = new SQLite();
        sqLite.addRecord();
        sqLite.getRecord();
        sqLite.clear();
    }
}
