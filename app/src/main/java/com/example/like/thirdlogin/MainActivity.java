package com.example.like.thirdlogin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

public class MainActivity extends AppCompatActivity {
    private Button btn_share,btn_login_qq,btn_login_weibo;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);
        findview();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void findview() {
        btn_share= (Button) findViewById(R.id.btn_share);
        btn_login_qq= (Button) findViewById(R.id.btn_login_qq);
        btn_login_weibo= (Button) findViewById(R.id.btn_login_weibo);
       loginthird();

    }
/*
* 要功能，不要数据
如果你的应用不具备用户系统，而且也不打算维护这个系统，那么你可以依照下面的步骤来做：

1、用户触发第三方登录事件
2、调用platform.getDb().getUserId()请求用户在此平台上的ID
3、如果用户ID存在，则认为用户是合法用户，允许进入系统；否则调用authorize()
4、authorize()方法将引导用户在授权页面输入帐号密码，然后目标平台将验证此用户
5、如果onComplete()方法被回调，表示授权成功，引导用户进入系统
6、否则提示错误，调用removeAccount(true)方法，删除可能的授权缓存数据*/
    private void loginthird(){
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        btn_login_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
            /*    qq.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        // TODO Auto-generated method stub
                        arg2.printStackTrace();
                    }
                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        // TODO Auto-generated method stub
                        //输出所有授权信息
                        arg0.getDb().exportData();
                    }
                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        // TODO Auto-generated method stub
                    }
                });*/
                //authorize与showUser单独调用一个即可
                qq.authorize();//单独授权,OnComplete返回的hashmap是空的(自身没有用户系统)
                //qq.showUser(null);//授权并获取用户信息（自身有用户系统）
                authorize(qq);
                //移除授权
                //qq.removeAccount(true);
            }
        });
        btn_login_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //授权
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                //授权回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
       /*         weibo.setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onError(Platform arg0, int arg1, Throwable arg2) {
                        // TODO Auto-generated method stub
                        arg2.printStackTrace();
                    }
                    @Override
                    public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                        //输出所有授权信息
                        arg0.getDb().exportData();
                        Intent intent=new Intent();
                        intent.setClass(getApplicationContext(),ThirdView.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancel(Platform arg0, int arg1) {
                        // TODO Auto-generated method stub
                    }
                });*/
                //authorize与showUser单独调用一个即可
               weibo.authorize();//单独授权,OnComplete返回的hashmap是空的(自身没有用户系统)
                //qq.showUser(null);//授权并获取用户信息（自身有用户系统）
               authorize(weibo);
                //移除授权
                //weibo.removeAccount(true);
            }
        });
    }
    private void authorize(Platform plat) {
        //ShareSDK.removeCookieOnAuthorize(true);
       /* if (plat == null) {
            return;
        }*/
        //判断指定平台是否已经完成授权
       /* if(plat.isAuthValid()) {
            //ShareSDK.removeCookieOnAuthorize(true);
            String userId = plat.getDb().getUserId();//使用getDb().getUserId()来获取此用户在此平台上的id
            if (userId != null) {//如果id不为空，就视为用户已经登录
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),ThirdView.class);
                startActivity(intent);
                return;
            }
        }  */
            ShareSDK.removeCookieOnAuthorize(true);
            plat.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    Intent intent=new Intent();
                    intent.setClass(getApplicationContext(),ThirdView.class);
                    startActivity(intent);
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(Platform platform, int i) {

                }
            });
            // true不使用SSO授权，false使用SSO授权
            plat.SSOSetting(false);//使用目标平台客户端(APP)来完成授权。
            //获取用户资料
            plat.showUser(null);
    }

    private void showShare() {
        /* ShareSDK.initSDK(this); */
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();
// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
// 启动分享GUI
        oks.show(this);
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
