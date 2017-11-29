package beans.controller.util.pdf;

import beans.models.Ticket;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ItextPdfView extends AbstractITextPdfView {

    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);

    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        @SuppressWarnings("unchecked")
        List<Ticket> tickets = (List<Ticket>) model.get("tickets");

        PdfPTable table = new PdfPTable(2);
        table.setWidths(new int[]{10, 30});

        table.addCell("ID");
        table.addCell("Date");

        for (Ticket ticket : tickets){
            table.addCell(String.valueOf(ticket.getId()));
            table.addCell(DATE_FORMAT.format(Date.from(ticket.getDateTime().atZone(ZoneId.systemDefault()).toInstant())));
        }



        document.add(table);
    }

}
