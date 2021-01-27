package com.craftland.engine.parser;

import android.content.Context;
import android.content.res.AssetManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlReader
{
    private Context context;
    private Document doc;
    public Document getDocument(){return doc;}

    public XmlReader(Context context, String filePath, boolean loadFromAssets)
    {
        this.context = context;
        readFile(filePath, loadFromAssets);
    }

    private void readFile(String filePath, boolean loadFromAssets)
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            File file;
            InputStream inputStream = null;
            if(loadFromAssets)
            {
                AssetManager assetManager = context.getAssets();
                try
                {
                    inputStream = assetManager.open(filePath);
                }catch (IOException e){
                    e.printStackTrace();
                }
                finally {
                    doc = builder.parse(inputStream);
                }
            }
            else
            {
                file = new File(filePath);
                doc = builder.parse(file);
            }

            if(doc != null)
            {
                doc.getDocumentElement().normalize();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getValue(String tag, Element element)
    {
        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodes.item(0);
        return node.getNodeValue();
    }

    /* parse example
    public void parseExample()
    {
        System.out.println("root of xml file" + doc.getDocumentElement().getNodeName());
        NodeList nodes = doc.getElementsByTagName("stock");
        System.out.println("==========================");

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                System.out.println("Stock Symbol: " + getValue("symbol", element));
                System.out.println("Stock Price: " + getValue("price", element));
                System.out.println("Stock Quantity: " + getValue("quantity", element));
            }
        }
    }*/
}
