package com.lp.javafeature;

import jdk.incubator.foreign.MemoryAddress;
import jdk.incubator.foreign.MemoryHandles;
import jdk.incubator.foreign.MemorySegment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.invoke.VarHandle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Java {

    public static void main(String[] args) {
	// write your code here
    }

    /**
     * Java21
     */
    public static void java21() {

        /**
         * 虚拟线程
         * JDK在少数操作系统线程上运行代码，可能只有一个线程。
         *
         * 示例，创建 10000个线程，然后都休眠 1 秒钟结束线程，如果使用传统的 Thread 线程，可能会因为线程数量不够而直接异常。
         * 如果是线程池的方式，会基于线程池的线程数并发，那么剩余线程只能等待；但是使用虚拟线程的方式，可以瞬间完成。
         */
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }  // executor.close() 被隐式调用，并等待

        /**
         * https://www.wdbyte.com/java/java-21/
         */
    }


    /**
     * Java17
     */
    public static void java17() {
        // nothing
    }

    /**
     * Java16
     */
    public static void java16() {

        /**
         * Collectors.teeing()
         * teeing 收集器已公开为静态方法Collectors::teeing。该收集器将其输入转发给其他两个收集器，然后将它们的结果使用函数合并。
         */
        List<Student> list = Arrays.asList(
                new Student("唐一", 55),
                new Student("唐二", 60),
                new Student("唐三", 90));

        //平均分 总分
        String result = list.stream().collect(Collectors.teeing(
                Collectors.averagingInt(Student::getScore),
                Collectors.summingInt(Student::getScore),
                (s1, s2) -> s1 + ":" + s2));

        //最低分  最高分
        String result2 = list.stream().collect(Collectors.teeing(
                Collectors.minBy(Comparator.comparing(Student::getScore)),
                Collectors.maxBy(Comparator.comparing(Student::getScore)),
                (s1, s2) -> s1.orElseThrow() + ":" + s2.orElseThrow()
        ));

        System.out.println(result);
        System.out.println(result2);


        /**
         * 添加Stream.toList方法
         */
        List<String> list = Arrays.asList("1", "2", "3");
        //之前这样写
        List<Integer> oneList = list.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        //现在可以这样写
        List<Integer> twoList = list.stream()
                .map(Integer::parseInt)
                .toList();

    }

    /**
     * Java15
     */
    public static void java15() {

        // https://stackoverflow.com/questions/64188082/what-is-the-point-of-a-sealed-interface-in-java
        // https://www.it610.com/article/1290457869514252288.htm
    }
    public sealed class Airport permits PrintingAirport {
        // ...
    }


    /**
     * Java14
     */
    public static void java14() {
        UserDTO user = new UserDTO("1697301681936888","空无","https://juejin.cn/user/1697301681936888");
        System.out.println(user.id);
        System.out.println(user.nickname);
        System.out.println(user.id);

        memoryOperation();
    }
    public record UserDTO(String id,String nickname,String homepage) { };
    public static void memoryOperation() {
        try {
            // 分配 200B 堆外内存
            MemorySegment memorySegment = MemorySegment.allocateNative(200);

            // 用 ByteBuffer 分配，然后包装为 MemorySegment
            MemorySegment memorySegment2 = MemorySegment.ofByteBuffer(ByteBuffer.allocateDirect(200));

            // MMAP 当然也可以
            MemorySegment memorySegment3 = MemorySegment.mapFile(
                    Path.of("/tmp/memory.txt"), 0, 200, FileChannel.MapMode.READ_WRITE);

            // 获取堆外内存地址
            MemoryAddress address = MemorySegment.allocateNative(100).address();//baseAddress();

            // 组合拳，堆外分配，堆外赋值
            long value = 10;
            MemoryAddress memoryAddress = MemorySegment.allocateNative(8).address();//baseAddress();
            // 获取句柄
            VarHandle varHandle = MemoryHandles.varHandle(long.class, ByteOrder.nativeOrder());
            varHandle.set(memoryAddress, value);

            // 释放就这么简单，想想 DirectByteBuffer 的释放……多奇怪
            memorySegment.close();
        } catch (IOException e) {

        }
    }

    /**
     * Java13
     */
    public static void java13() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        var typeOfDay = switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Working Day";
            case SATURDAY, SUNDAY -> {
                yield "Working Day";
            }
        };

        // String json = "{\"id\":\"1697301681936888\",\"nickname\":\"空无\",\"homepage\":\"https://juejin.cn/user/1697301681936888\"}";
        String json = """
                {
                    "id":"1697301681936888",
                    "nickname":"空无",
                    "homepage":"https://juejin.cn/user/1697301681936888"
                }
                """;
    }

    /**
     * Java12
     */
    public static void java12() {
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        var typeOfDay = switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "Working Day";
            case SATURDAY, SUNDAY -> "Day Off";
        };


        Object obj = "Hello Java 12!";
        if (obj instanceof String str) {
            int length = str.length();
        }
    }

    /**
     * Java11
     */
    public static void java11() {
        List<String> languages = Arrays.asList("Java", "Groovy");
        String language = languages.stream()
                .map((var x) -> x.toUpperCase())
                .collect(Collectors.joining(", "));

        assert(language).equals("Java, Groovy");
    }

    /**
     * Java10
     */
    public static void java10() {
        var message = "hello";
        var i = 13;
        var b = false;
        var ss = new int[]{1, 2, 3};
    }

    /**
     * Java9
     */
    public static void java9() throws IOException {
        //
        List<Integer> integers = new ArrayList<Integer>() {
            public void handle() {
                System.out.println("");
            }
        };


        //
        BufferedReader br = new BufferedReader(new InputStreamReader(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        }));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        }));
        try(br; bw) {

        }

        //
        interface InterfacePrivateMethodTest {
            default void wrapMethod() {
                innerMethod();
            }

            private void innerMethod() {
                System.out.println("interface private method...");
            }

        }
    }
}
