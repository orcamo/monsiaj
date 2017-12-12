package org.montsuqi.monsiaj.util;

import java.awt.print.*;
import java.io.File;
import java.io.*;

//import java.lang.ClassNotFoundException;
import java.awt.image.BufferedImage;
import java.util.prefs.Preferences;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.montsuqi.monsiaj.client.PrinterConfig;

public class PDFPrint {
    private static final Logger logger = LogManager.getLogger(PDFPrint.class);
    private static final Preferences prefs = Preferences.userNodeForPackage(PDFPrint.class);

    public static void print(String file, int copies, PrintService ps) {
        try {
            try (PDDocument document = PDDocument.load(new File(file))) {
                MediaSizeName size = getMediaSizeName(document);
                
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintService(ps);
                job.setPageable(new PDFPageable(document));
                PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
                attr.add(size);
                attr.add(new Copies(copies));
                attr.add(new JobName(file, null));
                
                PageFormat pf = job.getPageFormat(attr);
                Paper paper = pf.getPaper();
                paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
                pf.setPaper(paper);
                
                job.print(attr);
            }
        } catch (IOException | PrinterException ex) {
            logger.warn(ex, ex);
        }
    }

    public static void print(String file) {
        try {
            try (PDDocument document = PDDocument.load(new File(file))) {
                MediaSizeName size = getMediaSizeName(document);
                
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPageable(new PDFPageable(document));
                PrintService ps = loadPrintService();
                if (ps != null) {
                  job.setPrintService(ps);
                }
                PrintRequestAttributeSet attr = loadPrintRequestAttributeSet();
                if (attr == null) {
                  attr = new HashPrintRequestAttributeSet();
                  attr.add(size);
                }
                attr.add(new JobName(file, null));
                
                if (!job.printDialog(attr)) {
                    return;
                }
                PageFormat pf = job.getPageFormat(attr);
                Paper paper = pf.getPaper();
                paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());
                pf.setPaper(paper);
                
                job.print(attr);
                savePrintService(job.getPrintService());
                savePrintRequestAttributeSet(attr);
            }
        } catch (IOException | PrinterException ex) {
            logger.warn(ex, ex);
        }
    }

    public static PrintService loadPrintService() {
      return PrinterConfig.getPrintService_(prefs.get("printService",""));
    }

    public static void savePrintService(PrintService ps) {
      prefs.put("printService",ps.getName());
    }

    public static PrintRequestAttributeSet loadPrintRequestAttributeSet() {
      try {
        byte[] array = prefs.getByteArray("printRequestAttributeSet",new byte[0]);
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        ObjectInputStream ois = new ObjectInputStream(bais);
        PrintRequestAttributeSet attr = (PrintRequestAttributeSet)ois.readObject();
        ois.close();
        return attr;
      } catch (IOException |ClassNotFoundException ex) {
        logger.warn(ex, ex);
      }
      return null;
    }

    public static void savePrintRequestAttributeSet(PrintRequestAttributeSet attr) {
      try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(attr);
        oos.flush();
        oos.close();
        prefs.putByteArray("printRequestAttributeSet",baos.toByteArray());
      } catch (IOException ex) {
        logger.warn(ex, ex);
      }
    }

    public static MediaSizeName getMediaSizeName(PDDocument document) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        BufferedImage image = renderer.renderImageWithDPI(0, 72f);
        float w, h, swp;
        w = image.getWidth() / 72f;
        h = image.getHeight() / 72f;
        if (w > h) {
            swp = w;
            w = h;
            h = swp;
        }
        return MediaSize.findMedia(w, h, Size2DSyntax.INCH);
    }

    public static void main(String args[]) throws Exception {
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        PrintService[] pss = PrintServiceLookup.lookupPrintServices(flavor, null);
        PrintService ps = null;
        System.out.println("---- print service");
        for (PrintService _ps : pss) {
            System.out.println(_ps.getName());
            if (_ps.getName().equals(args[0])) {
                ps = _ps;
            }
        }
        for (int i = 0; i < 1; i++) {
            if (ps == null) {
                PDFPrint.print(args[1]);
            } else {
                PDFPrint.print(args[1],1,ps);
            }
        }
    }

}
