package util.order;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * @author jiaxiaopeng
 * 2020-10-19 18:33
 **/
@Slf4j
public class TopKByHeap {

    public static void main(String[] args) {
        quickSort();
    }

    private static void heapSort() {
        // 准备一个长度为arrayLength的无序数组：
        int[] array = {2, 4, 66, 53, 775, 886, 879, 4554, 434, 10000, -33, 23, 343, 545, 656, 433, 323, 666, 232, 888, 555};
        int arrayLength = array.length;
        int topK = 5; // 获取最大的几个值
        // 准备一个总结点数为topK的小顶堆：Integer默认的排序规则
        PriorityQueue<Integer> heap = new PriorityQueue<>(topK);

        // 始终维持一个总结点个数为k的堆：
        for (int i = 0; i < array.length; i++) {
            if (i < topK) {
                heap.add(array[i]);
            } else {// 怎么维持堆的总结点个数，下面的代码是关键：
                //Queue 中 element() 和 peek()都是用来返回队列的头元素，不删除。
                //在队列元素为空的情况下，element() 方法会抛出NoSuchElementException异常，peek() 方法只会返回 null
                if (null != heap.peek() && array[i] < heap.peek()) {
                    //Queue 中 remove() 和 poll()都是用来从队列头部删除一个元素。
                    //在队列元素为空的情况下，remove() 方法会抛出NoSuchElementException异常，poll() 方法只会返回 null 。
                    heap.poll();
                    heap.add(array[i]);
                }
            }
        }
        Iterator<Integer> iterator = heap.iterator();
        while (iterator.hasNext()) {
            System.out.println(heap.poll());
        }
    }


    private static void quickSort() {
        int[] array = {7, 1, 3, 5, 13, 9, 3, 6, 11};
        int len;
        if (array == null
                || (len = array.length) == 0
                || len == 1) {
            return;
        }
        sort(array, 0, len - 1);
        for (int x : array) {
            System.out.println(x);

        }
    }

    /**
     * 快排核心算法，递归实现
     *
     * @param array
     * @param left
     * @param right
     */
    public static void sort(int[] array, int left, int right) {
        if (left > right) {
            return;
        }
        // base中存放基准数
        int base = array[left];
        int i = left, j = right;
        while (i != j) {
            // 顺序很重要，先从右边开始往左找，直到找到比base值小的数
            while (array[j] >= base && i < j) {
                j--;
            }

            // 再从左往右边找，直到找到比base值大的数
            while (array[i] <= base && i < j) {
                i++;
            }

            // 上面的循环结束表示找到了位置或者(i>=j)了，交换两个数在数组中的位置
            if (i < j) {
                int tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
            }
        }

        // 将基准数放到中间的位置（基准数归位）
        array[left] = array[i];
        array[i] = base;

        // 递归，继续向基准的左右两边执行和上面同样的操作
        // i的索引处为上面已确定好的基准值的位置，无需再处理
        sort(array, left, i - 1);
        sort(array, i + 1, right);
    }
}
