package ru.yandex.alexey.mylauncher;

public class App {
    public int iconNumber;
    public int name;
    public int clicks;

    public App() {
        this(0, 0);
    }

    public App(int iconNumber, int name) {
        this.iconNumber = iconNumber;
        this.name = name;
        clicks = 0;
    }

    public App(App app) {
        iconNumber = app.iconNumber;
        name = app.name;
        clicks = app.clicks;
    }
}
