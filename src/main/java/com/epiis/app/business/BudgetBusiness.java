package com.epiis.app.business;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.epiis.app.dataaccess.BudgetRepository;
import com.epiis.app.dataaccess.CategoryRepository;
import com.epiis.app.dataaccess.TransactionRepository;
import com.epiis.app.dataaccess.UserRepository;
import com.epiis.app.dto.DtoBudget;
import com.epiis.app.entity.Budget;
import com.epiis.app.entity.Category;
import com.epiis.app.entity.User;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


@Service
public class BudgetBusiness {
	
	@Autowired
	private BudgetRepository budgetRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	
	@Value("${report.year}")
	private int reportYear;

	public boolean insert(DtoBudget dtoBudget,String idUser,String idCategory) {
		
		User user=this.userRepository.findById(idUser).orElse(null);
		Category category=this.categoryRepository.findById(idCategory).orElse(null);
    	if(user == null || category==null) {
    		return false;
    	}
    	if (!"EXPENSE".equals(category.getType())) {
    	    throw new IllegalArgumentException("No se puede crear presupuesto para una categoría de ingreso");
    	}

    	System.out.println("idUser=" + idUser + ", idCategory=" + idCategory);
    	
    	System.out.println(" se lista income");
    	dtoBudget.setIdBudget(UUID.randomUUID().toString());
    	dtoBudget.setIdCategory(idCategory);
    	dtoBudget.setIdUser(idUser);
    	dtoBudget.setCreatedAt(new Date());
    	dtoBudget.setUpdatedAt(dtoBudget.getCreatedAt());
    
        Budget budget = new Budget();
        budget.setIdBudget(dtoBudget.getIdBudget());
        budget.setLimitAmount(dtoBudget.getLimitAmount());
        budget.setMonth(dtoBudget.getMonth());
        budget.setYear(reportYear);
        
        budget.setParentUser(user);
        budget.setParentCategory(category);
        
 
        budget.setCreatedAt(new java.sql.Timestamp(dtoBudget.getCreatedAt().getTime()));
        budget.setUpdatedAt(new java.sql.Timestamp(dtoBudget.getUpdatedAt().getTime()));

        budgetRepository.save(budget);
        return true;
	}
	
	
	public List<DtoBudget> getAll(String idUser) {
		
		
		List<Budget> listBudget = this.budgetRepository.findByUserWithCategory(idUser);
		
		List<DtoBudget> listDtoBudget = new ArrayList<>();
		
		for(Budget item: listBudget) {
			Double spent=transactionRepository.getTotalSpentByCategoryAndMonth(idUser,item.getParentCategory().getIdCategory(), item.getMonth(),item.getYear());
			if (spent == null) {
			    spent = 0.0;
			}
			Double remaining=item.getLimitAmount()-spent;
			
			if (remaining < 0) {
			    remaining = 0.0;
			}
			
			double percentage = (spent / item.getLimitAmount()) * 100;

			percentage = Math.round(percentage * 100.0) / 100.0;

			if (percentage > 100) percentage = 100.0;

			
			DtoBudget dtoBudgetsTemp = new DtoBudget();
			
			dtoBudgetsTemp.setLimitAmount(item.getLimitAmount());
			dtoBudgetsTemp.setMonth(item.getMonth());
			dtoBudgetsTemp.setYear(item.getYear());
			dtoBudgetsTemp.setIdCategory(item.getParentCategory().getIdCategory());
			dtoBudgetsTemp.setNameCategory(item.getParentCategory().getName());
			dtoBudgetsTemp.setIdBudget(item.getIdBudget());
			
			
			dtoBudgetsTemp.setSpent(spent);
			dtoBudgetsTemp.setRemaining(remaining);
			dtoBudgetsTemp.setPercentage(percentage);
			
			listDtoBudget.add(dtoBudgetsTemp);
		}
		
		return listDtoBudget;
	}
	public boolean delete(String idBudget, String idUser) {

	    Budget budget = budgetRepository.findById(idBudget).orElse(null);
	    if (budget == null) return false;

	    if (!budget.getParentUser().getIdUser().equals(idUser)) return false;

	    budgetRepository.delete(budget);
	    return true;
	}

	public boolean update(String idBudget,DtoBudget dtoBudget,String idUser) {
		   Budget budget = this.budgetRepository.findById(idBudget).orElse(null);
		   if (budget == null) return false;
		   
		   Category category = categoryRepository.findById(dtoBudget.getIdCategory()) .orElse(null);
		   if (category == null) return false;

	       
	       if (!budget.getParentUser().getIdUser().equals(idUser)) return false;
	       
	       if (dtoBudget.getLimitAmount() == null || dtoBudget.getLimitAmount() <= 0) {
		        throw new IllegalArgumentException("El monto debe ser mayor a 0");
	       }
		   if (dtoBudget.getMonth() == null) {
		       throw new IllegalArgumentException("el mes  es obligatoria");
		   }
	       
		   budget.setParentCategory(category);
		   budget.setLimitAmount(dtoBudget.getLimitAmount());
		   budget.setMonth(dtoBudget.getMonth());
		   budget.setYear(reportYear);
		   budget.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
	       
		   budgetRepository.save(budget);
	       return true;
	       
	   }

	public byte[] generatePdf(List<DtoBudget> budgets) throws Exception {

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    Document document = new Document(PageSize.A4, 36, 36, 36, 36);
	    PdfWriter.getInstance(document, out);

	    document.open();

	    
	    Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.BLACK);
	    Paragraph title = new Paragraph("REPORTE DE PRESUPUESTOS", titleFont);
	    title.setAlignment(Element.ALIGN_CENTER);
	    title.setSpacingAfter(20);
	    document.add(title);

	   
	    PdfPTable table = new PdfPTable(6);
	    table.setWidthPercentage(100);
	    table.setSpacingBefore(10f);
	    table.setSpacingAfter(10f);
	    table.setWidths(new float[]{2f, 1f, 1f, 1f, 1f, 2f});

	    Font headFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
	    Stream.of("Categoría", "Mes/Año", "Límite", "Gastado", "Restante", "Progreso")
	          .forEach(header -> {
	              PdfPCell hCell = new PdfPCell(new Phrase(header, headFont));
	              hCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	              hCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	              hCell.setPadding(5);
	              table.addCell(hCell);
	          });

	    Font cellFont = new Font(Font.FontFamily.HELVETICA, 11);
	    for (DtoBudget b : budgets) {
	        table.addCell(new PdfPCell(new Phrase(b.getNameCategory(), cellFont)));
	        table.addCell(new PdfPCell(new Phrase(b.getMonth() + "/" + b.getYear(), cellFont)));
	        table.addCell(new PdfPCell(new Phrase(String.format("S/ %.2f", b.getLimitAmount()), cellFont)));

	        PdfPCell spentCell = new PdfPCell(new Phrase(String.format("S/ %.2f", b.getSpent()), cellFont));
	        spentCell.setBackgroundColor(BaseColor.PINK);
	        table.addCell(spentCell);

	        PdfPCell remainingCell = new PdfPCell(new Phrase(String.format("S/ %.2f", b.getRemaining()), cellFont));
	        remainingCell.setBackgroundColor(BaseColor.GREEN);
	        table.addCell(remainingCell);

	        PdfPCell progressCell = new PdfPCell(new Phrase(String.format("%.2f%%", b.getPercentage()), cellFont));
	        progressCell.setBackgroundColor(new BaseColor(0, 123, 255));
	        progressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.addCell(progressCell);
	    }

	    document.add(table);
	    document.close();

	    return out.toByteArray();
	}

}
	
	
