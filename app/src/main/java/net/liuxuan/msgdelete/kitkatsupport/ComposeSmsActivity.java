package net.liuxuan.msgdelete.kitkatsupport;

import android.app.Activity;

/**
 * 本包中所有类均为实现对Android 4.4的支持。
 * <br>Android 4.4增加了短信权限的管理，用户可以选择一个“短信程序”作为默认的短信程序，而只有默认的短信程序可以执行
 * 短信数据库“写”的功能。
 * <br>为了使自己的程序能够出现在系统设置“默认信息程序”的列表中从而供用户选择，需要在Manifest中“声明”自己的程序具备
 * 完善的信息功能，如收发短信/发彩信，当然，这些功能可以不实现（只要“声明”了即可）。
 * <br>Created by hanj on 14-10-31.
 */
public class ComposeSmsActivity extends Activity {

}
