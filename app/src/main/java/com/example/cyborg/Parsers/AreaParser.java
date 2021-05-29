package com.example.cyborg.Parsers;

import android.os.AsyncTask;

import com.example.cyborg.Interface.OnParseCompleted;
import com.example.cyborg.Models.AreaModel;

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
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AreaParser extends AsyncTask<String,Void, ArrayList<AreaModel>> {

        private OnParseCompleted onParseCompleted;
        private DocumentBuilderFactory documentBuilderFactory;

        public AreaParser(OnParseCompleted onParseCompleted){
            this.onParseCompleted = onParseCompleted;
            this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
        }


        @Override
        protected ArrayList<AreaModel> doInBackground(String... strings) {
            try {
                ArrayList<AreaModel>  areaModels = new ArrayList<>();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(new InputSource(new StringReader(strings[0])));
                document.normalize();
                NodeList nodes = document.getElementsByTagName("DATA");
                for (int i = 0; i < nodes.getLength(); i++) {
                    if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element outS = (Element) nodes.item(i);
                        String area = outS.getElementsByTagName("BILLARYA").item(0).getTextContent();

                        double pending = Double.parseDouble(outS.getElementsByTagName("BILLCL").item(0).getTextContent()) * -1;
                        AreaModel areaM = new AreaModel(area,1,pending);
                        if(!areaModels.contains(areaM)) {
                            areaModels.add(areaM);
                        }
                        else {
                            for(int j=0; j<areaModels.size();j++) {
                                if(areaModels.get(j).getAreaName().equals(area)) {
                                    double amount = areaModels.get(j).getAreaAmount();
                                    int counter = areaModels.get(j).getAreaBills();
                                    areaModels.remove(j);
                                    amount = amount + pending;
                                    areaModels.add(new AreaModel(area,++counter,amount));
                                    break;
                                }
                            }
                        }

//                        String amount = outS.getElementsByTagName("BILLCL").item(0).getTextContent();
//                        amount = Double.parseDouble(amount)<0?String.valueOf(Double.parseDouble(amount) * (-1)):amount;
//                        areaModels.add(new AreaModel(outS.getElementsByTagName("BILLDATE").item(0).getTextContent(),
//                                outS.getElementsByTagName("BILLREF").item(0).getTextContent(),
//                                outS.getElementsByTagName("BILLPARTY").item(0).getTextContent(),
//                                amount,
//                                outS.getElementsByTagName("BILLDUE").item(0).getTextContent(),
//                                outS.getElementsByTagName("BILLOVERDUE").item(0).getTextContent(),
//                                outS.getElementsByTagName("BILLARYA").item(0).getTextContent(),
//                                outS.getElementsByTagName("BILLCONTACT").item(0).getTextContent()));

                    }
                }

                if (areaModels.size() > 0) {
                    Collections.sort(areaModels, (object1, object2) -> object1.getAreaName().compareTo(object2.getAreaName()));
                }

                return  areaModels;
            } catch (IOException | SAXException | ParserConfigurationException | NullPointerException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<AreaModel> models) {
            if(models != null) {
                onParseCompleted.OnParsed(models);
            }else{
                onParseCompleted.OnParseFailed("Error Occurred while parsing data");
            }
        }
}

