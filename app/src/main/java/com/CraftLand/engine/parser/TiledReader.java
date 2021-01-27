package com.craftland.engine.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;
import com.craftland.engine.core.Render;

public class TiledReader
{
    private XmlReader xmlReader;
    private Document doc;
    private byte tiles[][];
    private String imageSource;
    private int tileSize;
    private int arrayWidth;
    private int arrayHeight;

    public TiledReader(String filePath, boolean loadFromAssets) {
        xmlReader = new XmlReader(Render.context, filePath, loadFromAssets);
        doc = xmlReader.getDocument();
        // read map settings
        NodeList mapNodeList = doc.getElementsByTagName("map");
        tileSize = Integer.parseInt(mapNodeList.item(0).getAttributes().getNamedItem("tilewidth").getNodeValue());
        NodeList imageNodeList = doc.getElementsByTagName("image");
        imageSource = imageNodeList.item(0).getAttributes().getNamedItem("source").getNodeValue();
        NodeList layerNodeList = doc.getElementsByTagName("layer");
        arrayWidth = Integer.parseInt(layerNodeList.item(0).getAttributes().getNamedItem("width").getNodeValue());
        arrayHeight = Integer.parseInt(layerNodeList.item(0).getAttributes().getNamedItem("height").getNodeValue());
    }

    public List<TMXObject> getObjects()
    {
        List<TMXObject> objects = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName("objectgroup");
        if(nodeList != null && nodeList.getLength() > 0)
        {
            for(int index = 0; index < nodeList.getLength(); index++)
            {
                // Read objects values and properties for each object
                Element element;
                NodeList list;
                String name;
                String x;
                String y;
                element = (Element) nodeList.item(index);
                list = element.getElementsByTagName("object");
                if (list != null && list.getLength() > 0) {
                    for (int n = 0; n < list.getLength(); n++) {
                        name = list.item(n).getAttributes().getNamedItem("name").getNodeValue();
                        x = list.item(n).getAttributes().getNamedItem("x").getNodeValue();
                        y = list.item(n).getAttributes().getNamedItem("y").getNodeValue();
                        //System.out.println("Mob->" + name + " x:" + x + "-y:" + y);
                        // properties
                        /*Element e = (Element)list.item(n);
                        NodeList properties = e.getElementsByTagName("property");
                        if (properties != null && properties.getLength() > 0) {
                            for(int i = 0; i < properties.getLength(); i++){
                                String p_name = properties.item(i).getAttributes().getNamedItem("name").getNodeValue();
                                String p_value = properties.item(i).getAttributes().getNamedItem("value").getNodeValue();
                                //System.out.println("property->" + p_name);
                                //System.out.println("property->" + p_value);
                            }
                        }*/
                        objects.add(new TMXObject(name, x, y));
                    }
                }
            }
        }
        return objects;
    }

    public byte[][] getTiles()
    {
        tiles = new byte[arrayHeight][arrayWidth];
        int y = 0;
        String lines[] = null;

        NodeList nodeList = doc.getElementsByTagName("layer");
        if(nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                lines = xmlReader.getValue("data", element).split("\n");
            }
            for(int index = 1; index < lines.length; index++)
            {
                String data[] = lines[index].split(",");
                for(int x = 0; x < data.length; x++){
                    int value = Integer.parseInt(data[x]) - 129;
                    tiles[y][x] = (byte)value;
                }
                y++;
            }
        }
        return tiles;
    }

    public String getImageSource(){return imageSource;}

    public int getTileSize(){return tileSize;}

    public int getArrayWidth(){
        return arrayWidth;
    }

    public int getArrayHeight(){
        return arrayHeight;
    }
}
