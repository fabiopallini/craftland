package com.craftland.engine.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import com.craftland.engine.core.Render;

public class CsvReader
{
    private List<String> lines = new ArrayList<>();

    public CsvReader(String fileName)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Render.context.getAssets().open(fileName)));
            String line;
            while((line = reader.readLine()) != null)
            {
                if(!line.startsWith("#") && !line.equals("")) //comments filter and blank lines
                {
                    lines.add(line.trim());
                }
            }
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String[] selectAll(String name, String element) {
        String result[];
        int size = 0;
        int index = rowIndex(element);
        for (int n = 1; n < lines.size(); n++) {
            String d[] = lines.get(n).split(",|;");
            if (d[0].equals(name)) {
                size++;
            }
        }
        result = new String[size];
        if(size > 0) {
            int k = 0;
            for (int n = 1; n < lines.size(); n++) {
                String d[] = lines.get(n).split(",|;");
                if (d[0].equals(name)) {
                    String elements[] = lines.get(n).split(",|;");
                    result[k] = elements[index];
                    k++;
                }
            }
        }
        return result;
    }

    public int[] selectAll_int(String name, String element)
    {
        String str[] = selectAll(name, element);
        int result[] = new int[str.length];
        for(int n = 0; n < str.length; n++){
            result[n] = Integer.parseInt(str[n]);
        }
        return result;
    }

    public String select(String name, String element)
    {
        String result = null;
        int index = rowIndex(element);
        if(lines != null && lines.size() > 0) {
            for (int n = 1; n < lines.size(); n++) {
                String d[] = lines.get(n).split(",|;");
                if (d[0].equals(name)) {
                    String elements[] = lines.get(n).split(",|;");
                    result = elements[index];
                    break;
                }
            }
        }
        if(result != null) {
            return result;
        }
        return "0";
    }

    public int select_int(String name, String element)
    {
        return Integer.parseInt(select(name, element));
    }

    public String[] select(String name)
    {
        String result[] = new String[lines.size()-1];
        int index = rowIndex(name);
        int k = 0;
        for(int n = 1; n < lines.size(); n++)
        {
            String element[] = lines.get(n).split(",|;");
            result[k] = element[index];
            k++;
        }

        return result;
    }

    public String[] select(int index){
        return lines.get(index).split(",|;");
    }

    public void close()
    {
        lines.clear();
    }

    private int rowIndex(String element)
    {
        int index = 0;
        if(lines != null && lines.size() > 0) {
            String header[] = lines.get(0).split(",|;");
            for (int n = 0; n < header.length; n++) {
                if (header[n].equals(element)) {
                    index = n;
                    break;
                }
            }
        }
        return index;
    }
}
