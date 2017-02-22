package com.example.home.header;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    MyAdapter myAdapter;

    @BindView(R.id.listView)
    ListView listView;

    View header_View;
    // TextView textView; -> viewPager로 변경

    // 임시데이터
    String tmpData[] = {"A", "B", "Z", "X", "C", "V", "N", "M", "L", "K"};

    //======================================================================

    MyPagerAdapter myPagerAdapter;

    // 헤더 임시 데이터
    String[] poster =
            {
                    "http://t1.daumcdn.net/news/201510/19/starnews/20151019152807751vwmd.jpg",
                    "http://cfile89.uf.daum.net/image/156F1B10AC526411125687",
                    "http://t1.daumcdn.net/news/201510/19/seouleconomy/20151019180305115hzgv.jpg",
                    "http://postfiles1.naver.net/20160507_256/aka36o_1462623378648x1q3A_JPEG/%B7%B9%C1%F6%B4%F8%C6%AE%C0%CC%BA%ED6-4.jpg?type=w2"
            };

    ViewPager viewPager;

    //======================================================================

    TextView curDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // 리스트
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setDividerHeight(0);   // 셀 사이의 선을 없애준다.

        // 헤더
        header_View = getLayoutInflater().inflate(R.layout.header_layout, null);
        viewPager = (ViewPager) header_View.findViewById(R.id.viewPager);
        listView.addHeaderView(header_View);          // 리스트뷰에 헤더뷰를 추가

        myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);

        ImageProc.getInstance().getImageLoader(this);   // 이미지 초기화

        // 자동으로 뷰페이저 2초마다 사진 변경
        curDot = (TextView)header_View.findViewById(R.id.curDot);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("UI", position+" : "+positionOffset+", "+positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                // Toast.makeText(MainActivity.this, position+"번 선택", Toast.LENGTH_SHORT).show();
                ChangeDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("UI", "변경 : " + state);
            }
        });

        pagerCurPage = 0;
        ackHandler.sendEmptyMessageDelayed(0, 1000*2);
    }

    // 리스트 뷰
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if(tmpData == null) return 0;
            return tmpData.length;
        }

        @Override
        public String getItem(int position) {
            return tmpData[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                // 최초 화면을 구성할 때 최대로 필요한 수만큼 여기가 작동됨
                convertView =
                        // xml에 있는 내용을 view로 불러오는 것이 getLayoutInflater
                        MainActivity.this.getLayoutInflater().inflate(
                                R.layout.cell_layout,
                                parent,
                                false
                        );
                // holder 초기화
                // 셀을 구성원을 담을 그릇 생성
                holder = new ViewHolder(convertView);
                // 그릇에 뷰에 설정
                convertView.setTag(holder);
                Log.i("LIST", "셀 생성 : "+ position);
            }else{
                holder = (ViewHolder)convertView.getTag();
                Log.i("LIST", "ELSE 문");
            }
            // 데이터 세팅
            holder.cell_title.setText(getItem(position));
            return convertView;
        }
    }

    class ViewHolder {
        @BindView(R.id.cell_title)
        TextView cell_title;

        public ViewHolder(View view) {
            // 셀 뷰를 바인딩한다.
            ButterKnife.bind(this, view);
        }

    }

    //======================================================================

    // 헤더 뷰페이저
    class MyPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return poster.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("Header", "뷰페이저 호출 : " + position);

            // 요청 페이지에 해당하는 url 획득
            String url = poster[position];

            // 이미지 뷰 생성
            ImageView imageView = new ImageView(MainActivity.this);
            // 이미지 세팅
            ImageProc.getInstance().drawImage(
                    url, imageView
            );
            // 무조건 꽉 채우기
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ((ViewPager)container).addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
            ((ViewPager)container).removeView((ImageView)object);
        }
    }

    //======================================================================

    // 하단 페이지 도트 변경
    public void ChangeDot(int position){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<poster.length; i++){
            if(i == position){
                sb.append("● ");
            }else{
                sb.append("○ ");
            }
        }
        // 버퍼를 스트링으로 만들고 공백을 제거해서 화면에 반영시킨다.
        curDot.setText(sb.toString().trim());
    }

    int pagerCurPage;
    // 마지막페이지에서 첫번째 페이지로 넘김.
    Handler ackHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:{
                    pagerCurPage++;
                    int page = pagerCurPage % poster.length;
                    viewPager.setCurrentItem(page);
                    sendEmptyMessageDelayed(0, 1000*2);
                }
                break;
            }
        }
    };
}
