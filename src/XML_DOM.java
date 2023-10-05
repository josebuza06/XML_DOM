import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class XML_DOM {
    public static void main(String[] args) {
        try {
            // cargo el documento XML que ya existe
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File("sales.xml"));
            Element root = doc.getDocumentElement();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese el nombre del departamento: ");
            String departamento = reader.readLine();

            System.out.print("Ingrese el porcentaje (5% - 15%): ");
            double porcentaje = Double.parseDouble(reader.readLine());

            if (porcentaje < 5 || porcentaje > 15) {
                System.out.println("Porcentaje fuera del rango.");
                return;
            }
            // Obtener la lista de elementos "salesRecord" y buscar el departamento especificado
            NodeList salesList = root.getElementsByTagName("salesRecord");

            for (int i = 0; i < salesList.getLength(); i++) {
                Element sale_Record = (Element) salesList.item(i);
                Element departmentElement = (Element) sale_Record.getElementsByTagName("department").item(0);


                if (departmentElement.getTextContent().equals(departamento)) {
                    Element sales = (Element) sale_Record.getElementsByTagName("sales").item(0);
                    double ventasAnteriores = Double.parseDouble(sales.getTextContent());
                    double nuevasVentas = ventasAnteriores * (1 + porcentaje / 100);

                    // Actualizar el valor de las ventas en el elemento XML
                    sales.setTextContent(String.format("%.2f", nuevasVentas));
                }
            }

            // Guardar el documento modificado en un nuevo archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream("new_sales.xml"));
            transformer.transform(source, result);

            System.out.println("Documento XML actualizado y guardado como new_sales.xml.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}