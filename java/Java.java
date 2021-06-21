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
import java.util.List;
import java.util.stream.Collectors;

public class Java {

    public static void main(String[] args) {
	// write your code here
    }

    /**
     * Java16
     */
    public static void java16() {

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
