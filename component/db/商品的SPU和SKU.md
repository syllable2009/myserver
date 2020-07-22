# 概念
小米8    红色    128G
        白色     256G
        蓝色     64G
手机商品叫小米8就是一个SPU，而小米8黑色64G就是一个SKU。
属性1个数*属性2个数。。。= sku数量
![image](https://github.com/syllable2009/myserver/blob/master/screenShots/%E6%89%8B%E6%9C%BAsku.jpg)
以前的设计
商品的SKU信息是存储在pms_sku_stock表中的，使用sp1、sp2、sp3这三个属性来存储商品的销售属性，这样做很不灵活，也难以扩展。
![image](https://github.com/syllable2009/myserver/blob/master/screenShots/sku1.png)
