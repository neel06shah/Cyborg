package com.example.cyborg.Parsers;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.OutstandingModel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class OutStandingParser extends AsyncTask<String,Void, ArrayList<OutstandingModel>> {


    private final OnParseCompleted onParseCompleted;
    private final DocumentBuilderFactory documentBuilderFactory;
    private final String area;

    public OutStandingParser(OnParseCompleted onParseCompleted, String areaName){
        this.onParseCompleted = onParseCompleted;
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        this.area = areaName;
    }


    @Override
    protected ArrayList<OutstandingModel> doInBackground(String... strings) {
        try {
                ArrayList<OutstandingModel>  outstandingModels = new ArrayList<>();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(new InputSource(new StringReader(strings[0])));
                document.normalize();
                NodeList nodes = document.getElementsByTagName("DATA");
                for (int i = 0; i < nodes.getLength(); i++) {
                    if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element outS = (Element) nodes.item(i);
                        String a = outS.getElementsByTagName("BILLARYA").item(0).getTextContent();
                        if(a.equals(area) || area == null) {
                            String amount = outS.getElementsByTagName("BILLCL").item(0).getTextContent();
                            amount = Double.parseDouble(amount) < 0 ? String.valueOf(Double.parseDouble(amount) * (-1)) : amount;
                            String overdue = outS.getElementsByTagName("BILLOVERDUE").item(0).getTextContent();
                            overdue = overdue.equals("") ? "0" : overdue;
                            outstandingModels.add(new OutstandingModel(outS.getElementsByTagName("BILLDATE").item(0).getTextContent(),
                                    outS.getElementsByTagName("BILLREF").item(0).getTextContent(),
                                    outS.getElementsByTagName("BILLPARTY").item(0).getTextContent(),
                                    amount,
                                    outS.getElementsByTagName("BILLDUE").item(0).getTextContent(),
                                    overdue,
                                    outS.getElementsByTagName("BILLARYA").item(0).getTextContent(),
                                    outS.getElementsByTagName("BILLCONTACT").item(0).getTextContent()));

                        }
                    }
                }

            if (outstandingModels.size() > 0) {
                Collections.sort(outstandingModels, (object1, object2) -> object1.getLedgerName().compareTo(object2.getLedgerName()));
            }

            return  outstandingModels;
        } catch (IOException | SAXException | ParserConfigurationException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<OutstandingModel> models) {
        if(models != null) {
            onParseCompleted.OnParsed(models);
        }else{
            onParseCompleted.OnParseFailed("Error Occurred while parsing data");
        }
    }
}
