package com.craftland.engine.core;

import java.util.ArrayList;
import java.util.List;
import com.craftland.engine.gfx.Screen;

public class Stage
{
    public List<Object> objects;
    private Screen screen;

    public Stage(Screen screen)
    {
        this.screen = screen;
        objects = new ArrayList<>();
    }

    public void update()
    {
        for(int n = 0; n < objects.size(); n++){
            objects.get(n).update();
            if(objects.get(n).isDispose())
                objects.remove(n);
        }
    }

    public void draw()
    {
        for(int n = 0; n < objects.size(); n++)
            objects.get(n).draw(screen);
    }

    public void addObject(Object object){
        objects.add(object);
    }

    public void clear(){
        objects.clear();
    }
}
