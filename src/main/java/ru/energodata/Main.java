        package ru.energodata;

        import java.io.*;
        import java.util.ArrayList;
        import javax.xml.parsers.ParserConfigurationException;
        import javax.xml.parsers.SAXParserFactory;
        import javax.xml.parsers.SAXParser;
        import org.xml.sax.Attributes;
        import org.xml.sax.SAXException;
        import org.xml.sax.helpers.DefaultHandler;
        import ru.energodata.Model.Otpravlenie;

        import static ru.energodata.Main.otpravlenieList;

        public class Main {

    static ArrayList<Otpravlenie> otpravlenieList = new ArrayList<>();
    private static final String CSV_SEPARATOR = ",";
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: xml2csv <path to .xml files> <resulting .csv file name>");
            System.out.println("Example: xml2csv C:\\_data\\xml_arch C:\\_data\\xml_arch.csv");
        } else {
            convertxml2csv(args[0], args[1]);
        }
        //convertxml2csv("C:\\_data\\xml_arch", "C:\\_data\\xml_arch.csv");
    }
            private static void convertxml2csv(String xmlpathstring, String csvfilepathstring) {
                SAXParserFactory factory = SAXParserFactory.newInstance();
                factory.setValidating(true);
                factory.setNamespaceAware(false);
                SAXParser parser = null;
                File xmlpath = new File(xmlpathstring);
                try {
                    parser = factory.newSAXParser();
                } catch (ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
                if (xmlpath.listFiles() != null)
                {
                    for (File file : xmlpath.listFiles()) {
                        if (!file.isDirectory() && file.getName().endsWith("xml")) {
                            try {
                                parser.parse(file, new MyParser());
                            }
                            catch (IOException | SAXException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    writeToCSV(otpravlenieList, csvfilepathstring);
                }
            }
            private static void writeToCSV(ArrayList<Otpravlenie> otpravlenieList, String csvfilepath)
        {
            try
            {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvfilepath), "UTF-8"));
                for (Otpravlenie otpravlenie : otpravlenieList)
                {
                    StringBuilder oneLine = new StringBuilder();
                    oneLine.append(otpravlenie.getBarcode() !=null ? otpravlenie.getBarcode() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getReliability() != null ? otpravlenie.getReliability() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getZip() != null ? otpravlenie.getZip() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getRegion() != null ? otpravlenie.getRegion() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getRegion_socr() != null ? otpravlenie.getRegion_socr() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getArea() != null ? otpravlenie.getArea() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getArea_socr() != null ? otpravlenie.getArea_socr() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getCity() != null ? otpravlenie.getCity() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getCity_socr() != null ? otpravlenie.getCity_socr() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getSubcity() != null ? otpravlenie.getSubcity() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getSubcity_socr() != null ? otpravlenie.getSubcity_socr() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getStreet() != null ? otpravlenie.getStreet() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getStreet_socr() != null ? otpravlenie.getStreet_socr() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getBuilding() != null ? otpravlenie.getBuilding() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getBuilding_korp() != null ? otpravlenie.getBuilding_korp() : "");
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getApartment() != null ? otpravlenie.getApartment() : "");
                    oneLine.append(CSV_SEPARATOR);
                    /*oneLine.append(otpravlenie.getName()!= null  ? "" : otpravlenie.getName());
                    oneLine.append(CSV_SEPARATOR);
                    oneLine.append(otpravlenie.getPhone()!= null  ? "" : otpravlenie.getPhone());*/
                    bw.write(oneLine.toString());
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            }
            catch (UnsupportedEncodingException e) {e.printStackTrace();}
            catch (FileNotFoundException e){e.printStackTrace();}
            catch (IOException e){e.printStackTrace();}
        }
    }
    class MyParser extends DefaultHandler {
    private Otpravlenie otpravlenie;
    private boolean bBuilding = false;
    private boolean bBuilding_korp = false;
    private boolean bApartment = false;

    @Override
    public void startDocument() throws SAXException {
        otpravlenie = new Otpravlenie();
        super.startDocument();
    }
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        if (qName.equals("postPackageDescription")) {
            otpravlenie.setBarcode(attributes.getValue("barcode"));
            //System.out.println("barcode " + attributes.getValue("barcode"));
        }
        if (qName.equals("receiver")) {
            otpravlenie.setReliability(attributes.getValue("reliability"));
            //System.out.println("reliability " + attributes.getValue("reliability"));
        }
        if (qName.equals("receiver")) {
            otpravlenie.setZip(attributes.getValue("zip"));
            //System.out.println("zip " + attributes.getValue("zip"));
        }
        if (otpravlenie.getBarcode()==null||otpravlenie.getBarcode().length()<6)
        {
         if       (qName.equals("post_index")) {
            otpravlenie.setBarcode(attributes.getValue("value"));
            //System.out.println("post_index " + attributes.getValue("value"));
        }}
        if (qName.equals("region")) {
            otpravlenie.setRegion(attributes.getValue("value"));
            otpravlenie.setRegion_socr(attributes.getValue("socr"));
            //System.out.println("region " + attributes.getValue("value") + " " + attributes.getValue("socr"));
        }
        if (qName.equals("area")) {
            otpravlenie.setArea(attributes.getValue("value"));
            otpravlenie.setArea_socr(attributes.getValue("socr"));
            //System.out.println("area " + attributes.getValue("value") + " " + attributes.getValue("socr"));
        }
        if (qName.equals("city")) {
            otpravlenie.setCity(attributes.getValue("value"));
            otpravlenie.setCity_socr(attributes.getValue("socr"));
            //System.out.println("city " + attributes.getValue("value") + " " + attributes.getValue("socr"));
        }
        if (qName.equals("subcity")) {
            otpravlenie.setSubcity(attributes.getValue("value"));
            otpravlenie.setSubcity_socr(attributes.getValue("socr"));
            //System.out.println("subcity " + attributes.getValue("value") + " " + attributes.getValue("socr"));
        }
        if (qName.equals("street")) {
            otpravlenie.setStreet(attributes.getValue("value"));
            otpravlenie.setStreet_socr(attributes.getValue("socr"));
            //System.out.println("street " + attributes.getValue("value") + " " + attributes.getValue("socr"));
        }
        if (qName.equals("building")) {
            bBuilding = true;
        }
        if (qName.equals("building_korp")) {
            bBuilding_korp = true;
        }
        if (qName.equals("apartment")) {
            bApartment = true;
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void characters(char[] c, int start, int length)
            throws SAXException {
        super.characters(c, start, length);
        if (bBuilding) {
            otpravlenie.setBuilding(new String(c, start, length));
            //System.out.println("building " + new String(c, start, length));
            bBuilding = false;
        } else if (bBuilding_korp) {
            otpravlenie.setBuilding_korp(new String(c, start, length));
            //System.out.println("building_korp " + new String(c, start, length));
            bBuilding_korp = false;
        } else if (bApartment) {
            otpravlenie.setApartment(new String(c, start, length));
            //System.out.println("apartment " + new String(c, start, length));
            bApartment = false;
        }
    }

    @Override
    public void endDocument() throws SAXException {
        otpravlenieList.add(otpravlenie);
        super.endDocument();

    }
}
