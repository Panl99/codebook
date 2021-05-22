# 目录
- [数据结构](#数据结构)
    - [数组](#数组)，[集合](#集合)，[栈--TODO](#栈)，[队列--TODO](#队列)，[散列表--TODO](#散列表)，[链表](#链表)
    - [二叉树](#二叉树)，[红黑树](#红黑树)，[B Tree--TODO](#B-Tree)，[B+ Tree--TODO](#B+-Tree)
    - [图](#图)（[广度优先搜索--TODO](#广度优先搜索BFS)，[深度优先搜索--TODO](#深度优先搜索DFS)，[最短路径问题](#最短路径问题)）
    - [Bitmap](#Bitmap)

- [算法](#算法)
    - [二分查找](#二分查找)
    - [冒泡排序](#冒泡排序)，[选择排序](#选择排序)，[插入排序](#插入排序)
    - [快速排序](#快速排序)
    - [递归](#递归)

- [设计模式](#设计模式)
    - [设计模式的7个原则](#设计模式的7个原则)
    - 创建型模式：[单例模式 √](#单例模式)，[工厂模式 √](#工厂模式)，[抽象工厂模式 √](#抽象工厂模式)，[建造者模式](#建造者模式)，[原型模式](#原型模式)
    - 结构型模式：[适配器模式](#适配器模式)，[装饰者模式](#装饰者模式)，[代理模式 √](#代理模式)，[外观模式](#外观模式)，[桥接模式](#桥接模式)，[组合模式](#组合模式)，[享元模式](#享元模式)，[~~过滤器模式~~](#过滤器模式)
    - 行为型模式：[观察者模式 ](#观察者模式)，[责任链模式](#责任链模式)，[迭代器模式](#迭代器模式)，[命令模式](#命令模式)，[备忘录模式](#备忘录模式)，[中介者模式](#中介者模式)，[解释器模式](#解释器模式)，[策略模式](#策略模式)，[模板方法](#模板方法)，[状态模式](#状态模式)，[访问者模式](#访问者模式)

- [算法题](https://github.com/Panl99/leetcode)

- [逻辑推理](#逻辑推理)
    - [5个海盗分金](#5个海盗分金)
------

# 数据结构
> [数据结构可视化工具](https://www.cs.usfca.edu/~galles/visualization/Algorithms.html)

## 数组
[数组——leetcode](https://github.com/Panl99/leetcode/tree/master/java/src/leetcode/ArrayDemo.java)
```java
String[] array = {"apples", "bananas", "cucumbers", "dates", "elderberries"};
int[][] matrix = {{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}};
```
- **读取：** 查看某个索引所指的数据值。`String s = array[0];`
- **查找：** 找出某个数据值的所在的位置。
- **插入：** 增加一个数据值。(数组下标越界：`array[5] = "sss";`)
- **删除：** 移走一个数据值。

***不能直接操作***

## 集合
```java
Set<String> strs = new HashSet<>();
strs.add("apples");
strs.remove("apples");
strs.contains("bananas");
```
- 元素不重复

## 栈
- `Stack` 允许在同一端进行删除插入和删除操作的特殊线性表。
- 特性：先进后出(FILO)
- 栈的实现：
```java

```

## 队列

## 散列表
`Hash Table` 也叫哈希表

## 链表
[链表——leetcode](https://github.com/Panl99/leetcode/tree/master/java/src/leetcode/ListDemo.java)

每个结点除了保存数据，它还保存着链表里的下一结点的内存地址。

![链表结构](../resources/static/images/链表结构.png)

**读取：** 
- 链表的结点可以分布在内存的任何地方，程序只知道第1 个结点的内存地址，要读取一个节点的值，程序必须先读取索引0 的链，然后顺着该链去找索引1。接着再读取索引1 的链，去找索引2，以此类推，找到最终索引中的值。
- 时间复杂度：O(N)

**查找：** 从第一格开始逐个格子地找，直至找到。如果是最坏情况，即所找的值在列表末尾，或完全不在列表里，那就要花O(N)步。

**插入：** 
- 时间复杂度：理论上O(1)，实际上是：O(N)：理论上只需要将上一个节点索引指向自己，自己指向下一个节点。但是要找到上一个节点的查询效率需要O(N)

![](../resources/static/images/链表的插入效率.png)

**删除：** 跟插入类似
- 时间复杂度：O(N) ：删除链表的最后一个结点，其实际的删除动作只需1 步——令倒数第二的结点的链指向null。然而，要找出倒数第二的结点，得花N 步

**链表与数组性能对比：**

![链表与数组性能对比](../resources/static/images/链表与数组性能对比.png)

### 双向链表
每个结点都含有两个链：一个指向下一结点，另一个指向前一结点。此外，它还能直接访问第一个和最后一个结点。

![双向链表结构](../resources/static/images/双向链表结构.png)

应用：使用双向链表作为队列实现的底层，可以使队列的插入和删除都为O(1)。
- 队列插入数据只能在末尾。
- 若使用数组、链表插入比较：在数组的末尾插入时间复杂度为O(1)。链表则要O(N)。
- 若使用数组、链表删除比较：链表只要O(1)，而数组是O(N)。
- 使用双向链表：因为双向链表能直接访问前端和末端的结点，所以在两端插入的效率都为O(1)，在两端删除的效率也为O(1)。

[目录](#目录)

## 树
### 二叉树
[树——leetcode](https://github.com/Panl99/leetcode/tree/master/java/src/leetcode/TreeDemo.java)

![二叉树](../resources/static/images/二叉树.png)

**查找：** 二叉树的查找算法先从根结点开始。
1. 检视该结点的值。
2. 如果正是所要找的值，返回！
3. 如果要找的值小于当前结点的值，则在该结点的左子树查找。
4. 如果要找的值大于当前结点的值，则在该结点的右子树查找。

```java
// 树节点表示
public static class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

// 构造树
TreeNode tree = new TreeNode(3);
tree.left = new TreeNode(9);
tree.right = new TreeNode(20);
tree.right.left = new TreeNode(15);
tree.right.right = new TreeNode(7);

/**
 * 剑指offer23:层序遍历二叉树
 * @return [3, 9, 20, 15, 7]
 * 解法一：迭代，间复杂度：O(n)，空间复杂度：O(n)
 */
public static ArrayList<Integer> PrintFromTopToBottom(TreeNode root) {
    ArrayList<Integer> list = new ArrayList<>();
    if (root == null) {
        return list;
    }
    Queue<TreeNode> queue = new LinkedList<>();
    queue.offer(root);
    while (!queue.isEmpty()) {
        TreeNode node = queue.poll();
        list.add(node.val);
        if (node.left != null) {
            queue.offer(node.left);
        }
        if (node.right != null) {
            queue.offer(node.right);
        }
    }
    return list;
}

/**
 * 翻转一棵二叉树。
 * 示例：给定二叉树 [4,2,7,1,3,6,9]。返回 [4,7,2,9,6,3,1]
 * 思路：递归：从根节点开始，递归地对树进行遍历，并从叶子结点先开始翻转。如果当前遍历到的节点 root 的左右两棵子树都已经翻转，那么我们只需要交换两棵子树的位置，即可完成以 root 为根节点的整棵子树的翻转。
 * 时间复杂度：O(N)
 * 空间复杂度：O(logN)。而在最坏情况下，树形成链状，空间复杂度为 O(N)
 */
public static TreeNode invertTree(TreeNode root) {
    if (root == null) {
        return null;
    }
    TreeNode left = invertTree(root.left);
    TreeNode right = invertTree(root.right);
    root.left = right;
    root.right = left;
    return root;
}
```


### 红黑树
- Red-Black Tree，是一种自平衡二叉查找树。在红黑树每个节点上都多出一个存储位表示节点的颜色，颜色只能是**红** 或者 **黑**。
- **红黑树特性**：
    - 每个节点非黑即红。
    - 根节点是黑色的。
    - 每个叶子结点(NIL)都是黑色的空节点。
    - 如果一个节点是红色的，那它的子节点必须为黑色的。
    - 从一个节点到它的子孙节点的所有路径上都包含相同数量的黑色节点。

![红黑树](../resources/static/images/redblackTree.PNG)
    
### B Tree

![BTree](../resources/static/images/BTree.png)

### B+ Tree

![B+Tree](../resources/static/images/B+Tree.png)


[目录](#目录)

## 图
每个结点都是一个顶点，每条线段都是一条边。当两个顶点通过一条边联系在一起时，称这两个顶点是相邻的。

![图的结构](../resources/static/images/图的结构.png)

单向关联关系的图：有向图。相互关联关系的图：无向图。

### 广度优先搜索BFS


### 深度优先搜索DFS


### 最短路径问题
如何以最低的价钱从Atlanta 飞往ElPaso？：可以使用加权图来解决

![最短路径问题--如何花费最少从Atlanta飞到ElPaso](../resources/static/images/最短路径问题--如何花费最少从Atlanta飞到ElPaso.png)

**狄克斯特拉(Dijkstra)算法：** 可以应用于：地图软件、GPS等
1. 以起步的顶点为当前顶点。
2. 检查当前顶点的所有邻接点，计算起点到所有已知顶点的权重，并记录下来。
3. 从未访问过（未曾作为当前顶点）的邻接点中，选取一个起点能到达的总权重最小的顶点，作为下一个当前顶点。
4. 重复前3 步，直至图中所有顶点都被访问过。

**Dijkstra算法特点：**

Dijkstra算法使用了广度优先搜索解决赋权有向图或者无向图的单源最短路径问题，算法最终得到一个最短路径树。该算法常用于路由算法或者作为其他图算法的一个子模块。

该算法的时间复杂度是n的平方，可以使用堆优化。

但是，要注意一点，Dijkstra算法只能适用于权值为正的情况下；如果权值存在负数，则不能使用。


[Dijkstra--leetcode](https://github.com/Panl99/leetcode/tree/master/java/src/leetcode/Dijkstra.java)

[目录](#目录)

## Bitmap
- `Bitmap`：使用一个bit位标记某个元素对应的Value，Key即该元素。

- 作用：大数据查找、去重、快速排序。
    - 使用Bit为单位存储数据，可以大大节省存储空间。

- 举例：在20亿个随机整数中找出某个数是否存在，条件：32位操作系统，4G内存。

- 分析：在Java中，int占4个字节(Byte)，1 byte = 8 bit
    - 如果每个数字使用int存储，需要占空间：2000000000 * 4 / 1024 / 1024 / 1024 ≈ 7.45G
    - 如果数字按bit位存储，需要占空间：2000000000 / 8 / 1024 / 1024 / 1024 ≈ 0.24G
    
- 存储方式：1 bit表示一个数，value = 1表示存在，value = 0表示不存在。
    - 1个int占32位，那么需要申请int数组长度为：`int tmp[1 + N/32]`，N 表示要存储的数字中的最大值。
        - tmp[0]：可以表示0~31
        - tmp[1]：可以表示32~63
        - tmp[2]：可以表示64~95
        - ...
        - 给定任一整数M，那么M的下标为：M / 32，M的位置：M % 32。
    
    ![bitmap存储](../resources/static/images/bitmap存储.png)

- 添加：例如存放数字：5
    - 计算位置：5/32=0，5%32=5，即5应该在tmp[0]的第5个位置（00100000）。
    - 存放方法：把1向左移动5位，然后按位或。
    - 公式：`p + (i/8)|(1<<(i%8))` 其中，p表示现在的值，i表示待插入的数。
    ``` 
    相当于 86 | 32 = 118 (86为原数组 01010110)
    86 | (1<<5) = 118 （得到118 01110110）
    b[0] = b[0] + (5/8) | (1<<(5%8))
    ```
  
    ![bitmap添加一个数](../resources/static/images/bitmap添加一个数.png)

- 删除：例如删除数字：6
    - 删除方法：只需将该数所在的位置为0即可：1左移6位，就到达6这个数字所代表的位，然后按位取反，最后与原数按位与。
        - b[0] = b[0] & (~(1<<6))
    - 公式：`b[0] = b[0] & (~(1<<(i%8)))`
    
    ![删除一个数](../resources/static/images/bitmap删除一个数.png)
    
- 查找：例如查找数字：3
    - 查找方法：判断该数所在的位是0还是1。
    - 判断 b[0] & (1<<3)，值是0，表示不存在，值是1，表示存在。
    
- 快速排序：例如要对0-7内的5个元素(4,7,2,5,3)排序（假设元素不重复）,就可以采用Bit-map的方法来达到排序的目的。
    - 分析：要表示8个数，我们就只需要8个Bit（1Bytes），
        - 首先，开辟1Byte的空间，将这些空间的所有Bit位都置为0，然后将对应数字位置为1。
        - 最后，遍历一遍Bit区域，将该位是1的位的编号输出（2，3，4，5，7），这样就达到了排序的目的。
        - 时间复杂度：O(n)。
    - 优点:
        - 运算效率高，不需要进行比较和移位；
        - 占用内存少，比如N=10000000；只需占用内存为N/8=1250000Byte=1.25M
    - 缺点:
        - 所有的数据不能重复。即不可对重复的数据进行排序和查找。
        - 只有当数据比较密集时才有优势
        
- 快速去重：例如在20亿个整数中找出不重复的整数的个数，内存不足以容纳这20亿个整数。
    - 分析：首先，根据“内存空间不足以容纳这20亿个整数”可以联想到Bit-map。下边关键的问题就是怎么设计我们的Bit-map来表示这20亿个数字的状态了。
        - 一个数字的状态只有3种，分别为不存在、只有一个、有重复。因此，只需要2bit就可以对一个数字的状态进行存储了。
        - 设定一个数字不存在为00，存在一次01，存在两次及其以上为11。那我们大概需要存储空间2G左右。
        - 接下来把这20亿个数字存进去：如果对应的状态位为00，则将其变为01，表示存在一次；如果对应的状态位为01，则将其变为11，表示已经有一个了，即出现多次；如果为11，则对应的状态位保持不变，仍表示出现多次。
        - 最后，统计状态位为01的个数，就得到了不重复的数字个数。
        - 时间复杂度为：O(n)。
        
- 总结：
    - Bitmap主要用于快速检索关键字状态，通常要求关键字是一个连续的序列（或者关键字是一个连续序列中的大部分）。
    - 1bit可以表示一个关键字的状态（可标示两种状态）；2bit（表示4种状态）；3bit（表示8种状态）。

[目录](#目录)

------

# 算法
**时间复杂度：** `O(1) > O(log N) > O(N)`

![时间复杂度曲线](../resources/static/images/时间复杂度曲线.png)

![时间复杂度O(n)和O(logN)对比](../resources/static/images/时间复杂度O(n)和O(logN)对比.png)

## 二分查找
要求元素有序，否则要先排序
- 时间复杂度：`O(log N)`，对数时间：表示数据量翻倍时，查找步数+1。
```java
public static void main(String[] args) {
    int[] nums = {3,4,6,20,40,45,51,62,70,99,110};
    System.out.println(binarySearch(nums, 20)); //3
}
public static int binarySearch(int[] nums, int a) {
    int low = 0;
    int high = nums.length - 1;
    int mid;

    while (low <= high) {
        mid = (low + high) / 2;
        if (nums[mid] == a) {
            return mid;
        } else if (a > nums[mid]) {
            low = mid + 1;
        } else {
            high = mid - 1;
        }
    }
    return -1;
}
```

## 冒泡排序
时间复杂度：`O(N^2)`
```java
/**
 * 冒泡排序
 * 原理：依次比较相邻两个元素，如果左边大于右边，则二者交换位置。如此重复。
 * 示例：[4,5,6,3,2,1]
 *      第1趟：[4,5,3,2,1,6]
 *      第2趟：[4,3,2,1,5,6]
 *      第3趟：[3,2,1,4,5,6]
 *      第4趟：[2,1,3,4,5,6]
 *      第5趟：[1,2,3,4,5,6]
 */
public static void main(String[] args) {
    System.out.println(bubbleSort(nums));
}
public static int[] bubbleSort(int[] nums) {
    // 外层循环控制轮数
    for (int i = 0; i < nums.length - 1; i++) {
        // 内层循环控制每趟次数
        for (int j = 0; j < nums.length - 1 - i; j++) {
            if (nums[j] > nums[j + 1]) {
                int temp = nums[j];
                nums[j] = nums[j + 1];
                nums[j + 1] = temp;
            }
        }
    }
    for (int i = 0; i < nums.length; i++) {
        System.out.print(nums[i] + " ");
    }
    return nums;
}
```

## 选择排序
时间复杂度：`O(N^2 / 2) = O(N^2)` 比冒泡快一倍

![选择排序步骤](../resources/static/images/选择排序步骤.png)

```java
/**
 * 选择排序
 * 原理：从左至右检查数组的每个格子，找出值最小的那个。在此过程中，我们会用一个变量来记住检查过的数字的最小值（事实上记住的是索引，但为了看起来方便，下图就直接写出数值）。如果一个格子中的数字比记录的最小值还要小，就把变量改成该格子的索引
 *      知道哪个格子的值最小之后，将该格与本次检查的起点交换。第1 次检查的起点是索引0，第2 次是索引1，以此类推。
 *      重复第(1) (2)步，直至数组排好序。
 * 示例：[2, 6, 1, 3]
 *      第一趟：先从2开始跟后相邻比较，得到1是最小后，跟2交换位置：[1, 6, 2, 3]
 *      第二趟：从6开始跟后边相邻比较，得到2最小，交换位置：[1, 2, 6, 3]
 *      第三趟：[1, 2, 3, 6]
 */
public static int[] SelectSort(int[] nums) {
    // 外层循环控制轮数
    for (int i = 0; i < nums.length; i++) {
        // 最小值的索引
        int lowestNumberIndex = i;
        // 内层循环控制每趟比较
        for (int j = i + 1; j < nums.length; j++) {
            if (nums[j] < nums[lowestNumberIndex]) {
                lowestNumberIndex = j;
            }
        }

        if (lowestNumberIndex != i) {
            int temp = nums[i];
            nums[i] = nums[lowestNumberIndex];
            nums[lowestNumberIndex] = temp;
        }
    }
    for (int i = 0; i < nums.length; i++) {
        System.out.print(nums[i] + " ");
    }
    return nums;
}
```


## 插入排序
时间复杂度：`最好：O(N) 平均：O(N^2) 最坏：O(N^2)`  `O(N2+2N-2) = O(N^2)`
```java
/**
 * 插入排序
 * 原理：将序列分为两个子序列，L默认拿第一个数，然后每次从R中拿一个插入L中从右到左比自己大的后边，将L中比自己大的整体后移，L序总是有序的。
 * 将一个数据插入已经拍好序的序列中，适用于少量数据排序，稳定。
 * 示例：[ 6, 2, 5, 8, 7 ]
 *      将该数组分为两个子集，L:[6];R:[2, 5, 8, 7 ]
 *      每次从R中拿一个插入L中从右到左比自己大的后边，将L中比自己大的整体后移。
 *      第1趟：L：[ 6 ]；            R：[2, 5, 7, 8 ]
 *      第2趟：L：[ 2, 6 ]；         R：[5, 7, 8 ]
 *      第3趟：L：[ 2, 5, 6 ]；      R：[7, 8 ]
 *      第4趟：L：[ 2, 5, 6, 7 ]；   R：[ 8 ]
 *      第5趟：L：[ 6, 2, 5, 7, 8 ]；R：[ ]
 */
public static int[] insertSort(int[] nums) {
    for (int i = 1; i < nums.length; i++) {
        int insertVal = nums[i];
        //被插入位置(和前一个数比较)
        int index = i - 1;
        while (index >= 0 && insertVal < nums[index]) {
            //将nums[index]向后移动
            nums[index + 1] = nums[index];
            //将index向前移动
            index--;
        }
        nums[index + 1] = insertVal;
    }
    for (int i = 0; i < nums.length; i++) {
        System.out.print(nums[i] + " ");
    }
    return nums;
}
```

## 快速排序
时间复杂度：`最好：O(N log N) 平均：O(N log N) 最坏：O(N2)`
```java
/**
 * 快速排序
 * 原理：一趟排序将序列分为两部分，其中一部分的所有数据都比另一部分所有数据小。
 *      选择一个关键值作为基准值(一般第1个)，将比基准值大的放到右序列中，小的放到左序列中。
 *      （1）从后向前比较，用基准值和最后一个值进行比较。如果比基 准值小，则交换位置；如果比基准值大，则继续比较下一个值，直到 找到第1个比基准值小的值才交换位置。
 *      （2）在从后向前找到第1个比基准值小的值并交换位置后，从前向后开始比较。如果有比基准值大的，则交换位置；如果没有，则继续比较下一个，直到找到第1个比基准值大的值才交换位置。
 *      （3）重复执行以上过程，直到从前向后比较的索引大于等于从后向前比较的索引，则结束一次循环。这时对于基准值来说，左右两边都是有序的数据序列。
 *      （4）重复循环以上过程，分别比较左右两边的序列，直到整个数据序列有序。
 * 示例：[6,9,5,7,8]
 *      第一层递归：（基准值6前面的数据都比6小，6后面的数据都比6大）
 *          第1趟（5和6交换）：[5,9,6,7,8]
 *          第2趟（9和6交换）：[5,6,9,7,8]
 *      重复上述递归过程。
 */
public static int[] quickSort(int[] nums, int low, int high) {
    int start = low; //从前向后比较的索引
    int end = high; //从后向前比较的索引
    int key = nums[low]; //基准值

    while (end > start) {
        //从后向前比较
        while (end > start && nums[end] >= key){
            end--;
        }
        //如果没有比基准值小的，则比较下一个，直到有比基准值小的，就交换位置，然后又从前向后比较。
        if (nums[end] <= key){
            int temp = nums[end];
            nums[end] = nums[start];
            nums[start] = temp;
        }
        //从前向后比较
        while (end > start && nums[start] <= key) {
            start++;
        }
        //如果没有比基准值大的，则比较下一个，直到有比基准值大的，就交换位置。
        if (nums[start] >= key){
            int temp = nums[start];
            nums[start] = nums[end];
            nums[end] = temp;
        }
        //第一次循环比较结束
    }
    if (start > low) {
        quickSort(nums, low, start-1);
    }
    if (end < high) {
        quickSort(nums, end+1, high);
    }

    return nums;
}
```

## 递归
```java
/**
 * 返回一个数的阶乘
 */
public static String factorial(int num) {
    if (num == 1) {
        return Integer.toString(1);
    }
    return num + " * " + factorial(num - 1);
}
```

实战：遍历文件系统


[目录](#目录)

------

# 设计模式
设计模式是人们经过长期编程经验总结出来的一种编程思想。

## 设计模式的7个原则
1. 单一职责原则
    - 又称单一功能原则，规定一个类只能有一个职责（功能）。
2. 开闭原则
    - 规定软件中的对象(类、模块、函数等)对扩展开放，对修改封闭。意味着一个实体允许在不改变其源码的前提下改变其行为。
3. 里氏代换原则
    - 对开闭原则的补充，规定在任意父类出现的地方，子类都一定可以出现。
    - 实现开闭原则的关键就是抽象化，父类与子类的继承关系就是抽象化的具体表现，所以里氏代换原则是对实现抽象化的具体步骤的规范。
4. 依赖倒转原则
    - 指程序要依赖于抽象（Java中的抽象类和接口），而不依赖于具体的实现（Java中的实现类）。
    - 简单地说，就是要求对抽象进行编程，不要求对实现进行编程，这就降低了用户与实现模块之间的耦合度。
5. 接口隔离原则
    - 指通过将不同的功能定义在不同的接口中来实现接口的隔离，这样就避免了其他类在依赖该接口（接口上定义的功能）时依赖其不需要的接口，可减少接口之间依赖的冗余性和复杂性。
6. 合成/聚合复用原则
    - 指通过在一个新的对象中引入（注入）已有的对象以达到类的功能复用和扩展的目的。它的设计原则是要尽量使用合成或聚合而不要使用继承来扩展类的功能。
7. 迪米特法则
    - 指一个对象尽可能少地与其他对象发生相互作用，即一个对象对其他对象应该有尽可能少的了解或依赖。
    - 核心思想在于降低模块之间的耦合度，提高模块的内聚性。迪米特法则规定每个模块对其他模块都要有尽可能少的了解和依赖，因此很容易使系统模块之间功能独立，这使得各个模块的独立运行变得更简单，同时使得各个模块之间的组合变得更容易。

[目录](#目录)

## 单例模式
[单例模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/singletonpattern/Singleton.java)
   
## 工厂模式
[工厂模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern)

- FactoryPattern：提供了一种简单、快速、高效而安全地创建对象的方式。
- 工厂模式在接口中定义了创建对象的方法，而将具体的创建对象的过程在子类中实现，用户只需通过接口创建需要的对象即可，不用关注对象的具体创建过程。同时，不同的子类可根据需求灵活实现创建对象的不同方法。
- 工厂模式的本质就是用工厂方法代替new操作创建一种实例化对象的方式

#### 实现
以创建手机为例，假设手机的品牌有华为、小米、苹果三种类型，我们要实现的是根据不同的传入参数实例化不同的手机。

1. 定义接口 [Phone](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/Phone.java) ，在接口中定义品牌 brand()；
2. 定义实现类 [IPhone](https://github.com/Panl99/demo/blob/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/IPhone.java) 、[HuaWei](https://github.com/Panl99/demo/blob/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/HuaWei.java) 、[XiaoMi](https://github.com/Panl99/demo/blob/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/XiaoMi.java) ；
3. 定义工厂类 [Factory](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/Factory.java) ；
4. 测试 [Main](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/Main.java) 。
5. 优化3：使用枚举创建手机对象 [PhoneEnum]([HuaWei](https://github.com/Panl99/demo/blob/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/PhoneEnum.java))

#### 使用Lambda重构工厂模式
无需暴露实例化的逻辑就能完成对象的创建。  
```java
//1、创建接口和实现类
public interface Phone {
    String brand(); //品牌
}
public class IPhone implements Phone {
    @Override
    public String brand() {
        return "this is a iphone";
    }
}

//2、创建一个工厂类
public static Phone createPhone(PhoneEnum name) {
    Supplier<Phone> p = name.getPhone();
    if (p != null) {
        return p.get();
    } else {
        throw new IllegalArgumentException("no such phone " + name);
    }
}

//3、枚举产品
public enum PhoneEnum {
    HUAWEI(HuaWei::new),
    IPHONE(IPhone::new),
    XIAOMI(XiaoMi::new);

    private final Supplier<Phone> phone;

    PhoneEnum(Supplier<Phone> phone) {
        this.phone = phone;
    }

    public Supplier<Phone> getPhone() {
        return phone;
    }
}

//4、测试
public static void main(String[] args) {
    Phone iphone = Factory.createPhone(PhoneEnum.IPHONE);
    Phone xm = Factory.createPhone(PhoneEnum.XIAOMI);
    System.out.println(iphone.brand());
    System.out.println(xm.brand());

}
```

### 抽象工厂模式
[抽象工厂模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern)

- 抽象工厂模式（Abstract Factory Pattern）在工厂模式上添加了一个创建不同工厂的抽象接口（抽象类或接口实现），该接口可叫作超级工厂。在使用过程中，我们首先通过抽象接口创建出不同的工厂对象，然后根据不同的工厂对象创建不同的对象。
- 手机厂商还可能制造其他产品，减少实现多个工厂类

#### 实现
- 假设品牌有华为、小米两种，
- 创建手机产品
- 创建电脑产品

1. 定义抽象工厂类 [AbstractFactory](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/AbstractFactory.java) ,抽象方法`createPhone(String brand);``createComputer(String brand);`  
2. 定义手机接口 [Phone](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/Phone.java) ，在接口中定义功能 call()
3. 定义手机实现类 [HuaWeiPhone](https://github.com/Panl99/demo/blob/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/HuaWeiPhone.java) 、[XiaoMiPhone](https://github.com/Panl99/demo/blob/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/XiaoMiPhone.java)
4. 定义手机工厂类 [PhoneFactory](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/PhoneFactory.java)

5. 定义电脑接口 [Computer](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/Computer.java) ，在接口中定义功能 internet()
6. 定义电脑实现类 [HuaWeiComputer](https://github.com/Panl99/demo/blob/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/HuaWeiComputer.java) 、[XiaoMiComputer](https://github.com/Panl99/demo/blob/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/XiaoMiComputer.java)
7. 定义电脑工厂类 [ComputerFactory](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/ComputerFactory.java)

8. 测试 [Main](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/factorypattern/abstractfactorypattern/Main.java) 。


[目录](#目录)

## 建造者模式
[建造者模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/builderpattern)

## 原型模式
[原型模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/prototypepattern)

## 适配器模式
[适配器模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/adapterpattern)

## 装饰者模式
[装饰者模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/decoratorpattern)

## 代理模式
[代理模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/proxypattern)

- 代理模式指为对象提供一种通过代理的方式来访问并控制该对象行为的方法。在客户端不适合或者不能够直接引用一个对象时，可以通过该对象的代理对象来实现对该对象的访问，可以将该代理对象理解为客户端和目标对象之间的中介者。

### 代理模式实现
以招聘工作为例

1. 定义接口Company：[Company](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/proxypattern/Company.java)
2. 定义其实现类HR：[HR](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/proxypattern/HR.java)
3. 定义代理类Proxy：[Proxy](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/proxypattern/Proxy.java)
4. 测试 [Main](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/proxypattern/Main.java)

[目录](#目录)

## 外观模式
[外观模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/facadepattern)

## 桥接模式
[桥接模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/bridgepattern)

## 组合模式
[组合模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/compositepattern)

## 享元模式
[享元模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/flyweightpattern)

## 观察者模式
[观察者模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/observerpattern)

### 使用Lambda重构观察者模式  
某些事件发生时，需要自动地通知其他多个对象(观察者)  
```java
//通知系统，报纸机构订阅了新闻，当接收的新闻中包含的关键字时，能得到特别通知。
//1、创建一个观察者接口，且仅有一个名为notify的方法，一旦接收到一条新的新闻，该方法就会被调用：
interface Observer {
    void notify(String tweet);
}
//2、声明不同的观察者，依据新闻中不同的关键字分别定义不同的行为：
class NYTimes implements Observer{
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("money")){
            System.out.println("Breaking news in NY! " + tweet);
        }
    }
}
class Guardian implements Observer{
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("queen")){
            System.out.println("Yet another news in London... " + tweet);
        }
    }
}
class LeMonde implements Observer{
    public void notify(String tweet) {
        if(tweet != null && tweet.contains("wine")){
            System.out.println("Today cheese, wine and news! " + tweet);
        }
    }
}
//3、定义一个接口Subject，registerObserver方法可以注册一个新的观察者，notifyObservers方法通知它的观察者一个新闻的到来。
interface Subject{
    void registerObserver(Observer o);
    void notifyObservers(String tweet);
}
//4、实现Feed类
class Feed implements Subject{
    private final List<Observer> observers = new ArrayList<>();
    public void registerObserver(Observer o) {
        this.observers.add(o);
    }
    public void notifyObservers(String tweet) {
        observers.forEach(o -> o.notify(tweet));
    }
}
//5、Feed类在内部维护了一个观察者列表，一条新闻到达时，它就进行通知。
Feed f = new Feed();
f.registerObserver(new NYTimes());
f.registerObserver(new Guardian());
f.registerObserver(new LeMonde());
f.notifyObservers("The queen said her favourite book is Java 8 in Action!");

//使用Lambda
f.registerObserver((String tweet) -> {
    if(tweet != null && tweet.contains("money")){
        System.out.println("Breaking news in NY! " + tweet);
    }
});
f.registerObserver((String tweet) -> {
    if(tweet != null && tweet.contains("queen")){
        System.out.println("Yet another news in London... " + tweet);
    }
});
```

[目录](#目录)

## 责任链模式
[责任链模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/chain-of-responsibilitypattern)

### 使用Lambda重构责任链模式
一个处理对象可能需要在完成一些工作之后，将结果传递给另一个对象，这个对象接着做一些工作，再转交给下一个处理对象，以此类推。  
```java
//这种模式通常是通过定义一个代表处理对象的抽象类来实现的，在抽象类中会定义一个字段来记录后续对象。一旦对象完成它的工作，处理对象就会将它的工作转交给它的后继。
public abstract class ProcessingObject<T> {
    protected ProcessingObject<T> successor;
    public void setSuccessor(ProcessingObject<T> successor){
        this.successor = successor;
    }
    public T handle(T input){
        T r = handleWork(input);
        if(successor != null){
            return successor.handle(r);
        }
        return r;
    }
    abstract protected T handleWork(T input);
}
//创建两个处理对象，进行一些文本处理工作。
public class HeaderTextProcessing extends ProcessingObject<String> {
    public String handleWork(String text){
        return "From Raoul, Mario and Alan: " + text;
    }
}
public class SpellCheckerProcessing extends ProcessingObject<String> {
    public String handleWork(String text){
        return text.replaceAll("labda", "lambda");
    }
}
//将这两个处理对象结合起来，构造一个操作序列！
ProcessingObject<String> p1 = new HeaderTextProcessing();
ProcessingObject<String> p2 = new SpellCheckerProcessing();
p1.setSuccessor(p2);
String result = p1.handle("Aren't labdas really sexy?!!");
System.out.println(result);

//使用Lambda
UnaryOperator<String> headerProcessing = (String text) -> "From Raoul, Mario and Alan: " + text;
UnaryOperator<String> spellCheckerProcessing = (String text) -> text.replaceAll("labda", "lambda");
Function<String, String> pipeline = headerProcessing.andThen(spellCheckerProcessing);
String result = pipeline.apply("Aren't labdas really sexy?!!");
```

[目录](#目录)

## 迭代器模式
[迭代器模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/iteratorpattern)

## 命令模式
[命令模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/commandpattern)

## 备忘录模式
[备忘录模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/mementopattern)  

## 中介者模式
[中介者模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/mediatorpattern)

## 解释器模式
[解释器模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/interpreterpattern)

## 策略模式
[策略模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/strategypattern)

当if/else比较多的时候，可以考虑使用策略模式重构，比如以支付为例：支付宝、微信、京东、云闪付等
- 普通代码：
```java
@PostMapping("/makeOrder")
public ResultData makeOrder(@RequestBody  Order order){
    // 生成自己的订单，并且设置订单的失效时间，并且定时回滚
    //  ... 此处代码省略

    // 处理支付方式
    if(order.getType=="alipay"){ // 支付宝
        this.payService.alipay(order);
    }else if (order.getType=="weixin"){ // 微信
        this.payService.weixinpay(order);
    }else if (order.getType=="jd"){ // 京东支付
        this.payService.jtpay(order);
    }else if (order.getType=="yunshanfu"){ // 云闪付
        this.payService.yunshanfupay(order);
    }
    // 发送到mq，进行广播。
    return this.ok(order);
}
```

- 引入策略模式
1. 入口：
```java
private OrderService orderService;

@PostMapping("/makeOrder")
// 商品id
// 支付类型
public ResultData makeOrder(Long goodsId,String type){
    // 生成本地的订单
    Order order = this.orderService.makeOrder(goodsId);
    //选择支付方式
    PayType payType = PayType.getByCode(type);
    //进行支付
    payType.get().pay(order.getId(),order.getAmount());
    return this.ok();
}
```
2. 支付接口：
```java
public interface Payment {
    public void pay(Long order, double amount);
}
```
3. 支付实现：
```java
// 支付宝支付实现
public class AliPay implements Payment {
    @Override
    public void pay(Long order, double amount) {
        System.out.println("----支付宝支付----");
        System.out.println("支付宝支付111元");
    }
}

// 微信支付实现
public class WechatPay implements Payment {
    @Override
    public void pay(Long orderId, double amount) {
        System.out.println("---微信支付---");
        System.out.println("支付222元");
    }
}
```
4. 支付方式：
```java
public enum PayType {
    //支付宝        AliPay 是实现类
    ALI_PAY("1",new AliPay()),
    //微信
    WECHAT_PAY("2",new WechatPay());

    private String payType;
    // 这是一个接口
    private Payment payment;
    PayType(String payType,Payment payment){
        this.payment = payment;
        this.payType = payType;
    }
    //通过get方法获取支付方式
    public Payment get(){ return  this.payment;}

    public static PayType getByCode(String payType) {
        for (PayType e : PayType.values()) {
            if (e.payType.equals(payType)) {
                return e;
            }
        }
        return null;
    }
}
```

### 使用Lambda重构策略模式  
解决某类算法的通用方案，包含三部分内容：  
一个代表某个算法的接口（它是策略模式的接口）。  
一个或多个该接口的具体实现，它们代表了算法的多种实现（比如，实体类Concrete-StrategyA或者ConcreteStrategyB）。  
一个或多个使用策略对象的客户。  
```java
//假设希望验证输入的内容是否根据标准进行了恰当的格式化（比如只包含小写字母或数字）。
//1、可以从定义一个验证文本（以String的形式表示）的接口入手：
public interface ValidationStrategy {
    boolean execute(String s);
}
//2、定义了该接口的一个或多个具体实现：
public class IsAllLowerCase implements ValidationStrategy {
    public boolean execute(String s){
        return s.matches("[a-z]+");
    }
}
public class IsNumeric implements ValidationStrategy {
    public boolean execute(String s){
        return s.matches("\\d+");
    }
}
//3、在程序中使用这些略有差异的验证策略：
public class Validator{
    private final ValidationStrategy strategy;
    public Validator(ValidationStrategy v){
        this.strategy = v;
    }
    public boolean validate(String s){
        return strategy.execute(s);
    }
}
Validator numericValidator = new Validator(new IsNumeric());
boolean b1 = numericValidator.validate("aaaa");//返回false
Validator lowerCaseValidator = new Validator(new IsAllLowerCase ());
boolean b2 = lowerCaseValidator.validate("bbbb");//返回true

//使用Lambda（ValidationStrategy是一个函数接口）
Validator numericValidator = new Validator((String s) -> s.matches("[a-z]+"));
boolean b1 = numericValidator.validate("aaaa");
Validator lowerCaseValidator = new Validator((String s) -> s.matches("\\d+"));
boolean b2 = lowerCaseValidator.validate("bbbb");
```

[目录](#目录)

## 模板方法
[模板方法](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/templatepattern)

### 使用Lambda重构模板方法  
需要采用某个算法的框架，同时又希望有一定的灵活度，能对它的某些部分进行改进  
```java
//processCustomer方法搭建了在线银行算法的框架：获取客户提供的ID，然后提供服务给用户。不同的支行可以通过继承OnlineBanking类，对该方法提供差异化的实现。
abstract class OnlineBanking {
    public void processCustomer(int id){
        Customer c = Database.getCustomerWithId(id);
        makeCustomerHappy(c);
    }
    abstract void makeCustomerHappy(Customer c);
}

//使用Lambda
public void processCustomer(int id, Consumer<Customer> makeCustomerHappy){
    Customer c = Database.getCustomerWithId(id);
    makeCustomerHappy.accept(c);
}
//可以直接插入不同的行为，不再需要继承OnlineBanking类
new OnlineBankingLambda().processCustomer(1337, (Customer c) -> System.out.println("Hello " + c.getName());
```

[目录](#目录)

## 状态模式
[状态模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/statepattern)

## 访问者模式
[访问者模式](https://github.com/Panl99/demo/tree/master/demo-common/src/main/java/com/outman/democommon/designpatterns/visitorpattern)

[目录](#目录)


# 逻辑推理
## 5个海盗分金
   
5个海盗抢得100枚金币，他们按抽签的顺序依次提方案：首先由1号提出分配方案，然后5人表决，投票要超过半数同意方案才被通过，否则他将被扔入大海喂鲨鱼，依此类推。“海盗分金”其实是一个高度简化和抽象的模型，体现了博弈的思想。在“海盗分金”模型中，任何“分配者”想让自己的方案获得通过的关键是事先考虑清楚“挑战者”的分配方案是什么，并用最小的代价获取最大收益，拉拢“挑战者”分配方案中最不得意的人们。

**问：** 假定“每个海盗都是绝顶聪明且很理智”，那么“第一个海盗提出怎样的分配方案才能够使自己的收益最大化？”

**推理：** 从后向前推

1. 假设1-3号都喂了鲨鱼，只剩4号和5号的话，那么5号一定反对让4号喂了鲨鱼，这样5号可以拿到全部金币。所以：**4号必须要支持3号才能保命。**
2. 3号知道4号一定支持自己，所以他的分配方案就是：(100,0,0)，**3号自己拿全部金币，4号，5号没有。**
3. 2号知道3号的方案，就会提出：（98,0,1,1）的方案，即放弃3号，拉拢4号和5号，这样**2号得到98金币，3号没有，4号、5号各1个。**
4. 1号知道2号的方案，会提出：（97,0,1,0,2）或（97,0,1,2,0）的方案，即放弃2号，**自己拿97金币，给3号1金币，4号或者5号2金币**，这样可以拿到3票通过，是1号获取最多金币的最佳方案

**答案：** 1号自己拿97个，分给3号1个，分给4号 或者5号2个，即（97,0,1,0,2）或（97,0,1,2,0）。


[目录](#目录)