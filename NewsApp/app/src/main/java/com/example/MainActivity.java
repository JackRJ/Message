package com.example;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int ITEM_HEADER = 1;
    private static final int ITEM_SOCIETY = 2;
    private static final int ITEM_COUNTY = 3;
    private static final int ITEM_INTERNATION = 4;
    private static final int ITEM_FUN = 5;
    private static final int ITEM_SPORT = 6;
    private static final int ITEM_WAR = 7;
    private static final int ITEM_TECHNOLOGY = 8;
    private static final int ITEM_WEALTH = 9;
    private static final int ITEM_FASHION = 10;

    private List<Title> titleList = new ArrayList<Title>();
    private ListView listView;
    private TitleAdapter adapter;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.nav_menu);
        }
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("头条");

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new TitleAdapter(this, R.layout.list_view_item, titleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent intent = new Intent(MainActivity.this, ContentActivity.class);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Title title = titleList.get(position);
                intent.putExtra("title", actionBar.getTitle());
                intent.putExtra("uri", title.getUri());
                startActivity(intent);
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.head);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.head:
                        handleCurrentPage("头条", ITEM_HEADER);
                        break;
                    case R.id.society:
                        handleCurrentPage("社会新闻", ITEM_SOCIETY);
                        break;
                    case R.id.country:
                        handleCurrentPage("国内新闻", ITEM_COUNTY);
                        break;
                    case R.id.globle:
                        handleCurrentPage("国际新闻", ITEM_INTERNATION);
                        break;
                    case R.id.entertainment:
                        handleCurrentPage("娱乐新闻", ITEM_FUN);
                        break;
                    case R.id.sport:
                        handleCurrentPage("体育新闻", ITEM_SPORT);
                        break;
                    case R.id.technology:
                        handleCurrentPage("科技新闻", ITEM_TECHNOLOGY);
                        break;
                    case R.id.military:
                        handleCurrentPage("军事新闻", ITEM_WAR);
                        break;
                    case R.id.economy:
                        handleCurrentPage("财经新闻", ITEM_WEALTH);
                        break;
                    case R.id.fashion:
                        handleCurrentPage("时尚新闻", ITEM_FASHION);
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                int itemName = parseString((String) actionBar.getTitle());
                requestNew(itemName);
            }
        });

        requestNew(ITEM_HEADER);
    }

    private void handleCurrentPage(String text, int item) {
        ActionBar actionBar = getSupportActionBar();
        if (!text.equals(actionBar.getTitle().toString())) {
            actionBar.setTitle(text);
            requestNew(item);
            refreshLayout.setRefreshing(true);
        }
    }

    public void requestNew(int itemName) {

        // 根据返回到的 URL 链接进行申请和返回数据
        String address = response(itemName);    // key
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "新闻加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                NewsList newsList = Utility.parseJsonWithGson(responseText);

                if (newsList.msg.equals("成功的返回")) {
                    titleList.clear();
                    for (New news : newsList.mnewsList) {
                        Title title = new Title(news.title, news.author_name, news.thumbnail_pic_s, news.url, news.date);
                        titleList.add(title);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            refreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "数据错误返回", Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    private String response(int itemName) {
        String address = "http://v.juhe.cn/toutiao/index?type=top&key=42c9addb11f2144b9d03b36b30cf9013";
        switch (itemName) {
            case ITEM_HEADER:
                break;
            case ITEM_SOCIETY:
                address = address.replaceAll("top", "shehui");
                break;
            case ITEM_COUNTY:
                address = address.replaceAll("top", "guonei");
                break;
            case ITEM_INTERNATION:
                address = address.replaceAll("top", "guoji");
                break;
            case ITEM_FUN:
                address = address.replaceAll("top", "yule");
                break;
            case ITEM_SPORT:
                address = address.replaceAll("top", "tiyu");
                break;
            case ITEM_TECHNOLOGY:
                address = address.replaceAll("top", "keji");
                break;
            case ITEM_WAR:
                address = address.replaceAll("top", "junshi");
                break;
            case ITEM_WEALTH:
                address = address.replaceAll("top", "caijing");
                break;
            case ITEM_FASHION:
                address = address.replaceAll("top", "shishang");
            default:
        }
        return address;
    }

    private int parseString(String text) {
        if (text.equals("头条")) {
            return ITEM_HEADER;
        }
        if (text.equals("社会新闻")) {
            return ITEM_SOCIETY;
        }
        if (text.equals("国内新闻")) {
            return ITEM_COUNTY;
        }
        if (text.equals("国际新闻")) {
            return ITEM_INTERNATION;
        }
        if (text.equals("娱乐新闻")) {
            return ITEM_FUN;
        }
        if (text.equals("体育新闻")) {
            return ITEM_SPORT;
        }
        if (text.equals("科技新闻")) {
            return ITEM_TECHNOLOGY;
        }
        if (text.equals("军事新闻")) {
            return ITEM_WAR;
        }
        return ITEM_SOCIETY;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            finish();
        }
    }
}