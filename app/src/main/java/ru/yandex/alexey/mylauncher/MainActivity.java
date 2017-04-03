package ru.yandex.alexey.mylauncher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private final static int SIZE = 1000000;
    private final static String NAME_HEADERS[] = new String[3];
    private final Random rand = new Random(System.currentTimeMillis());
    private static ArrayList<Bitmap> iconList = new ArrayList<>();
    private static ArrayList<App> appList = new ArrayList<>(SIZE);
    private int columnCount;
    private RecyclerView.Adapter adapter;
    private ArrayList<App> saveNew;
    private ArrayList<App> savePopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(getIntent().getIntExtra("id_theme", R.style.AppLightTheme_NoActionBar));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        columnCount = getResources().getInteger(getIntent().getIntExtra("id_column_count", R.integer.column_count_standard_size));
        if (savedInstanceState == null)
            init();
        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        adapter = new MyAppRecyclerViewAdapter(NAME_HEADERS, iconList, appList);
        list.setAdapter(adapter);
        ((GridLayoutManager)list.getLayoutManager()).setSpanCount(columnCount);
        ((GridLayoutManager)list.getLayoutManager()).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case MyAppRecyclerViewAdapter.APP:
                        return 1;
                    case MyAppRecyclerViewAdapter.HEADER:
                        return columnCount;
                    default:
                        return 0;
                }
            }
        });
    }

    private void init() {
        initIcon();
        initHeaders();
        initAllApp();
        initNewApp();
        initPopularApp();
    }

    private void initHeaders() {
        NAME_HEADERS[0] = getResources().getString(R.string.popular_header);
        NAME_HEADERS[1] = getResources().getString(R.string.new_header);
        NAME_HEADERS[2] = getResources().getString(R.string.all_header);
    }

    private void initAllApp() {
        int iconNumber;
        boolean used[] = new boolean[iconList.size()];
        Arrays.fill(used, false);
        for (int i = 0; i < 7; ++i) {
            iconNumber = rand.nextInt(iconList.size());
            while (used[iconNumber]) {
                ++iconNumber;
                if (iconNumber == used.length)
                    iconNumber = 0;
            }
            used[iconNumber] = true;
            appList.add(new App(iconNumber, i + 1));
        }
        for (int i = 7; i < SIZE; ++i) {
            iconNumber = rand.nextInt(iconList.size());
            while (used[iconNumber]) {
                ++iconNumber;
                if (iconNumber == used.length)
                    iconNumber = 0;
            }
            used[appList.get(i - 7).iconNumber] = false;
            used[iconNumber] = true;
            appList.add(new App(iconNumber, i + 1));
        }
        appList.add(0, new App(-1, 2));
    }

    private void initNewApp() {
        HashSet<Integer> usedIcon = new HashSet<>();
        for (int i = 0; i < columnCount;) {
            int k = rand.nextInt(appList.size() - i - 1) + i + 1;
            while (usedIcon.contains(appList.get(k).iconNumber)) {
                ++k;
                if (k == appList.size())
                    k = 30;
            }
            usedIcon.add(appList.get(k).iconNumber);
            appList.add(0, new App(appList.get(k)));
            ++i;
        }
        appList.add(0, new App(-1, 1));
    }

    private void initPopularApp() {
        for (int i = 0; i < columnCount; ++i) {
            appList.add(0, new App(appList.get(1 + columnCount * 3)));
        }
        appList.add(0, new App(-1, 0));
    }

    private void initIcon() {
        Point sizeWindow = new Point();
        getWindowManager().getDefaultDisplay().getSize(sizeWindow);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inDensity = 512;
        options.inTargetDensity = sizeWindow.x / 4;
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.books, options));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.calculator, options));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.calendar, options));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.contacts, options));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.maps, options));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.market, options));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.messages, options));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.movie_studio, options));
        iconList.add(BitmapFactory.decodeResource(getResources(), R.drawable.you_tube, options));
    }

    @Override
    protected void onPause() {
        saveNew = new ArrayList<>();
        savePopular = new ArrayList<>();
        int i = 1;
        while (appList.get(i).iconNumber != -1 || appList.get(i).name != 1) {
            savePopular.add(appList.get(i));
            ++i;
        }
        ++i;
        HashSet<Integer> usedIcon = new HashSet<>();
        while (appList.get(i).iconNumber != -1 || appList.get(i).name != 2) {
            int k = rand.nextInt(appList.size() - 30) + 30;
            while (usedIcon.contains(appList.get(k).iconNumber)) {
                ++k;
                if (k == appList.size())
                    k = 30;
            }
            saveNew.add(appList.get(i));
            usedIcon.add(appList.get(k).iconNumber);
            appList.set(i, new App(appList.get(k)));
            ++i;
        }
        ++i;
        TreeMap<Integer, Integer> newPopular = new TreeMap<>();
        while (i < appList.size()) {
            if (appList.get(i).clicks != 0)
                newPopular.put(-appList.get(i).clicks, i);
            ++i;
        }
        i = 1;
        while ((appList.get(i).iconNumber != -1 || appList.get(i).name != 1) && !newPopular.isEmpty()) {
            appList.set(i, new App(appList.get(newPopular.firstEntry().getValue())));
            newPopular.remove(newPopular.firstKey());
            ++i;
        }
        adapter.notifyItemRangeChanged(0, columnCount * 2 + 2);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        int i = 1;
        while (appList.get(i).iconNumber != -1 || appList.get(i).name != 1) {
            appList.set(i, new App(savePopular.get(i - 1)));
            ++i;
        }
        ++i;
        int j = 0;
        while (appList.get(i).iconNumber != -1 || appList.get(i).name != 2) {
            appList.set(i, new App(saveNew.get(j)));
            ++i;
            ++j;
        }
        adapter.notifyItemRangeChanged(0, columnCount * 2 + 2);
        super.onDestroy();
    }
}
