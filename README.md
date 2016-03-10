
SlidingTitleBar
===================================
  一款用来滑动的导航条组件，主要就是用于配合ViewPager实现滑动的导航条，以容纳无限多的Tab\<h1\><br />
 ![属性](https://github.com/heavenxue/SlidingTitleBar/tree/master/docs/device-2016-03-10-154106.mp4 "属性")
Features
-----------------------------------
  有4个属性控制这个组件，分别为\<h2\><br />
  ![属性](https://github.com/heavenxue/SlidingTitleBar/tree/master/docs/attr.png "属性")

      allowWidthFull 是否充满屏幕
      disableViewPager 是否禁用viewPager
      disableTensileSlidingBlock 是否禁用拉伸滑动块
      slidingBlock 滑动块
  
### 使用
  在eclipse和AndroidStudion中都可以使用\<h3\><br />
  小标题的格式如下 ### 小标题<br />
  注意#和标题字符中间要有空格

### 注意!!!下面所有语法的提示我都先用小标题提醒了!!! 

### 单行文本框
    这是一个单行的文本框,只要两个Tab再输入文字即可
        
### 多行文本框  
    这是一个有多行的文本框
    你可以写入代码等,每行文字只要输入两个Tab再输入文字即可
    这里你可以输入一段代码

### 比如我们可以在多行文本框里输入一段代码,来一个Java版本的HelloWorld吧
    public class HelloWorld {

      /**
      * @param args
	    */
	    public static void main(String[] args) {
		    System.out.println("HelloWorld!");

	    }

    }
### 链接
1.[点击这里你可以链接到www.google.com](http://www.google.com)<br />
2.[点击这里我你可以链接到我的博客](http://guoyunsky.iteye.com)<br />

###只是显示图片
![github](https://github.com/heavenxue/SlidingTitleBar/tree/master/docs/attr.png "github")

###想点击某个图片进入一个网页,比如我想点击github的icorn然后再进入www.github.com
[![image]](http://www.github.com/)
[image]: http://github.com/github.png "github"

### 文字被些字符包围
> 文字被些字符包围
>
> 只要再文字前面加上>空格即可
>
> 如果你要换行的话,新起一行,输入>空格即可,后面不接文字
> 但> 只能放在行首才有效

### 文字被些字符包围,多重包围
> 文字被些字符包围开始
>
> > 只要再文字前面加上>空格即可
>
>  > > 如果你要换行的话,新起一行,输入>空格即可,后面不接文字
>
> > > > 但> 只能放在行首才有效

### 特殊字符处理
有一些特殊字符如<,#等,只要在特殊字符前面加上转义字符\即可<br />
你想换行的话其实可以直接用html标签\<br /\>



* 在行首加点
行首输入*，空格后输入内容即可
    
