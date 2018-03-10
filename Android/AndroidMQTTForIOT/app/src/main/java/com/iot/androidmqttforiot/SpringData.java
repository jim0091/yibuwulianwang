package com.iot.androidmqttforiot;

/**
 * Created by Administrator on 2017/12/11.
 */

public class SpringData {
    //家用电器
    static  String[][] domesticAppliance = {
            {"电视", "Dianshi"},
            {"冰箱", "Bingxiang"},
            {"洗衣机", "Xiyiji"},
            {"音响", "Yinxiang"},
            {"油烟机", "Youyanji"},
            {"热水器", "Reshuiqi"},
            {"消毒柜", "Xiaodugui"},
            {"洗碗机", "Xiwanji"},
            {"红酒柜", "Hongjiugui"},
            {"取暖器", "Qunuanqi"},
            {"空气净化器", "Jinghuaqi"},
            {"空气检测仪", "Kongqijianceyi"},
            {"加湿器", "Jiashiqi"},
            {"吸尘器", "Xichenqi"},
            {"电熨斗", "Dianyundou"},
            {"清洁机", "Qingjieji"},
            {"除湿机", "Chushiji"},
            {"干衣机", "Ganyiji"},
            {"收音机", "Shouyinji"},
            {"电风扇", "Dianfengshan"},
            {"冷风扇", "Lengfengshan"},
            {"净水器", "Jingshuiqi"},
            {"饮水机", "Yinhsuiji"},
            {"榨汁机", "Zhazhiji"},
            {"电饭煲", "Dianfanbao"},
            {"保温盒", "Baowenhe"},
            {"电压力锅", "Dianyaliguo"},
            {"面包机", "Mianbaoji"},
            {"咖啡机", "Kafeiji"},
            {"微波炉", "Weibolu"},
            {"电烤箱", "Diankaoxiang"},
            {"电磁炉", "Diancilu"},
            {"烧烤盘", "Shaokaopan"},
            {"煎蛋器", "Jiandanqi"},
            {"酸奶机", "Suannaiji"},
            {"电炖锅", "Siandunguo"},
            {"煮水壶", "Zhushuihu"},//电水壶
            {"解毒机", "Jieduji"},
            {"解冻机", "Jiedongji"},
            {"煎药壶", "Jianyaohu"},
            {"剃须刀", "Tixudao"},
            {"剃毛器", "Timaoqi"},
            {"电吹风", "Dianchuifeng"},
            {"美容器", "Meirongqi"},
            {"理发器", "Lifaqi"},
            {"美发器", "Meifaqi"},//弄卷发直发
            {"按摩椅", "Anmoyi"},
            {"按摩器", "Anmoqi"},
            {"足浴盆", "Zuyupen"},
            {"血压计", "Xueyaji"},
            {"身高仪", "Shengaoyi"},
            {"体重秤", "Tizhongcheng"},
            {"血糖仪", "Xuetangyi"},
            {"体温计", "Tiwenji"},
            {"排气扇", "Paiqishan"},
            {"洁身器", "Jieshenqi"},
            {"插座", "Chazuo"},
            {"床头灯", "Chuangtoudeng"},
            {"台灯", "Taideng"},
            {"吊灯", "Diaodeng"},
            {"电灯", "Diandeng"},
            {"温湿计", "Wenshiji"},
            {"水龙头", "Shuikoutou"},
            {"国家电网", "Guojiadianwang"},//控制
            {"自来水", "Zilaishui"},
            {"燃气", "Ranqi"},
            {"煤气", "Meiqi"},
            {"暖气", "Nuanqi"},
            {"电表", "Dianbiao"},//获取表的内容
            {"水表", "Shuibiao"},
            {"燃气表", "Ranqibiao"},
            {"煤气表", "Meiqibiao"},
            {"暖气表", "Nuanqibiao"},
            {"门铃", "Menling"},
            {"门锁", "Mensuo"},
            {"窗帘","Chuanglian"},
            {"安防", "Anfang"},
            {"门磁", "Menci"},
            {"充电器", "Chongdianqi"},
            {"红外报警", "Hongwaibaojing"},
            {"烟雾报警", "Yanwubaojing"},
            {"燃气报警", "Ranqibaojing"},
            {"声音报警", "Shengyinbaojing"},
            {"小度", "Xiaodu"},
            {"小白", "Xiaobai"}
    };
    //楼层
    static  String floors[][] = {
            {"一楼", "Yilou"},
            {"二楼", "Erlou"},
            {"三楼", "Sanlou"},
            {"四楼", "Silou"},
            {"五楼", "Wulou"},
            {"六楼", "Liulou"},
            {"七楼", "Qilou"},
            {"八楼", "Balou"},
            {"九楼", "Jiulou"},
            {"十楼", "Shilou"}
    };

    //房间
    static  String positions[][] = {
            {"宿舍", "Sushe"},
            {"卧室", "Woshi"},
            {"客厅", "Keting"},
            {"书房", "Shufang"},
            {"仓库", "Cangku"},
            {"工作室", "Gongzuoshi"},
            {"办公室", "Bangongshi"},
            {"浴室", "Yushi"},
            {"厨房", "Chufang"},
            {"阳台", "Yangtai"},
            {"厕所", "Cesuo"},
            {"走廊", "Zoulang"},
            {"楼梯", "Louti"},
    };

    //操作词
    static  String manipulate[][] = {
            {"关闭", "Close"},
            {"打开", "Open"},
            {"获取", "Get"},
            {"设置", "Set"},
            {"返回", "Return"},
            {"更新", "Updata"}
    };
}
