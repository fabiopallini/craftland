package com.craftland.engine.parser;

public class TMXObject
{
    public int id;
    private String name;
    private String x;
    private String y;

    public TMXObject(String name, String x, String y)
    {
        id = 0;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName(){
        return name;
    }

    public int getX(){
        int xx = (int)Float.parseFloat(x);
        return xx;
    }

    public int getY(){
        int yy = (int)Float.parseFloat(y);
        return yy;
    }
}
