package com.simtechdata;

public class Main {
    public static void main() {
        SQLite sqLite = new SQLite();
        sqLite.addRecord();
        sqLite.getRecord();
        sqLite.clear();
    }
}
