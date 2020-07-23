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
这种做法也带来了后续的问题，比如我们的购物车和订单都会需要存储销售属性，这样的话都会需要添加sp1、sp2、sp3的属性。

改进后的设计
由于商品的销售属性是动态的，没法确定到底有多少个，此时我们可以改用JSON格式来存储，在pms_sku_stock表中添加了sp_data字段。
![image](https://github.com/syllable2009/myserver/blob/master/screenShots/sku2.png)
sp_data存储的就是一个JSON数组，比如颜色为黑色，容量为32G的手机存储信息如下。
[
    {
        "key": "颜色",
        "value": "黑色"
    },
    {
        "key": "容量",
        "value": "32G"
    }
]

以前的做法
商品的SKU信息作为商品的关联信息，在修改商品信息时会同时进行修改。以前的做法是直接删除该商品的所有SKU信息，再重新添加。
这样就会导致商品SKU中的ID被修改，由于在购物车和订单商品中关联了商品SKU的ID，就会导致原来的ID失效的问题。
改进后的做法
首先我们需要和前端约定下，新增的商品SKU信息不传ID，要修改的商品SKU信息传ID，删除的直接不传SKU信息。
然后我们可以根据传入的SKU信息来确定需要新增、修改、删除的SKU信息，这样就可以做到在更新商品SKU信息时，不改变原来商品SKU的ID了，具体流程如下。
![image](https://github.com/syllable2009/myserver/blob/master/screenShots/sku3.png)


商品表product
id product_name ... 

商品sku表sku
id product_id sku_json price pic 

单位也可以看作是一种sku属性，但如果需要单位换算的话，还应该有特别表存对应关系

商品库存表stock
id product_id sku_id stock库存数量 lock数量





