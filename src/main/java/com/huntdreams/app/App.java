package com.huntdreams.app;

/**
 * Author: Noprom <tyee.noprom@qq.com>
 * Date: 16/3/4 下午4:38.
 */
public class App {

    public void foo(int number, String name) {
        System.out.println("Inside App->foo(), number = " + number + ", name = " + name);
    }

    public void bar() {
        foo(123, "Like");
        System.out.println("Call bar()......");
    }

    public static void main(String[] args) {
        App app = new App();
        app.bar();
        app.foo(123, "Like");
    }
}