package com.example.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.neo4j.cypher.internal.compiler.v2_2.replace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Status;
import com.example.model.Book;
import com.example.model.BookDetail;
import com.example.model.Category;
import com.example.model.Fine;
import com.example.model.Member;
import com.example.model.Quantity;
import com.example.repositories.BookDetailRepository;
import com.example.repositories.BookRepository;
import com.example.repositories.CategoryRepository;
import com.example.repositories.FineRepository;
import com.example.repositories.MemberRepository;
import com.example.repositories.QuantityRepository;

@RestController
public class BookController {
	
	@Autowired
	BookRepository bookrepository;
	@Autowired
	CategoryRepository categoryrepository;
	@Autowired
	FineRepository finerepository;
	@Autowired
	BookDetailRepository bookdetailrepository;
	@Autowired
	MemberRepository memberrespository;
	@Autowired
	QuantityRepository quantityrepository;
	
	@RequestMapping("/categories")
		public List<Category> getCategories() {
		return (List<Category>) categoryrepository.findAll();
	}	
	
	@RequestMapping("/books")
	public List<Book> getBooks() {
		return (List<Book>) bookrepository.findAll();
	}	
	
	@RequestMapping("/book/{bookid}")
	public Book get(@PathVariable("bookid") int bookid)
	{
		return bookrepository.findOne(bookid);
		
	}
	@RequestMapping("/deletecopy/{accountid}")
	public void delete(@PathVariable("bookid")String accountid)
	{
		quantityrepository.delete(accountid);
	}
	
	
	
	@RequestMapping("/viewfine")
	public List<Fine> getfine() {
		return (List<Fine>) finerepository.findAll();
	}	
	
	@RequestMapping("/books/{isbn}")
	public List<Book> getBooks(@PathVariable("isbn") int isbn) {
		return (List<Book>) bookrepository.findOne(isbn);
	}	
	
	/*@RequestMapping("/issuebook/{id}")
	public List<BookDetail> getBookDetail(@PathVariable("id") int id) {
		return (List<BookDetail>) bookdetailrepository.findOne(id);
	}	*/
	
	@RequestMapping("/member")
	public List<BookDetail> getBookDetails(@PathVariable("id") int id)
	{
		return (List<BookDetail>) bookdetailrepository.findOne(id);
	}
	
		
	
	@RequestMapping("/addCategory")
	public HashMap<String,Object> addcategory(@RequestBody Category category) {
		HashMap<String, Object> returnParams = new HashMap<String, Object>();
		
		try {
			categoryrepository.save(category);
			returnParams.put("status", true);
		} catch (Exception e) {
			returnParams.put("status", false);
			returnParams.put("msg", "Failed to add Category!!");
		}

		return returnParams;
	}
	
	

	
	@RequestMapping("/addcopy1/{bookid}")
	public HashMap<String,Object>addcopy(@PathVariable("bookid") int bookid) {
		HashMap<String, Object> returnParams = new HashMap<String, Object>();
		try
		{
			Quantity qu=new Quantity();
			UUID uuid=UUID.randomUUID();
			String randomNo = uuid.toString();
			randomNo=randomNo.replace("-", "");
			
			qu.setAccountId(randomNo);					
			qu.setStatus(Status.Available);
			Book book=bookrepository.findOne(bookid);
			int j=book.getCopies();
			j++;
			book.setCopies(j);
			qu.setBook(book);
			quantityrepository.save(qu);
			returnParams.put("status", true);
			
		} catch (Exception e) {
			returnParams.put("status", false);
			returnParams.put("msg", "Failed to add Category!!");
		}

		return returnParams;
		
	}	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/addBook")
	public HashMap<String,Object> addbook(@RequestBody Book book) {
		HashMap<String, Object> returnParams = new HashMap<String, Object>();
		
		try {
			
			
			
		
			
			
			int j=book.getCopies();
			List<Quantity>account=new ArrayList<Quantity>();
			int i=1;
			while(i<=j)
			{
				
				Quantity a=new Quantity();
					
					
				
					UUID uuid=UUID.randomUUID();
					String randomNo = uuid.toString();
					randomNo=randomNo.replace("-", "");
					
					a.setAccountId(randomNo);					
					a.setStatus(Status.Available);
					a.setBook(book);
					account.add(a);
				i++;
		}
			book.setQuantity(account);
						
			
			bookrepository.save(book);
			returnParams.put("status", true);
		} catch (Exception e) {
			returnParams.put("status", false);
			returnParams.put("msg", "Failed to add book!!!!!!");
		}

		return returnParams;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping("/savemember")
	public HashMap<String,Object> addmember(@RequestBody Member mem) {
		HashMap<String, Object> returnParams = new HashMap<String, Object>();
		
		try {
			memberrespository.save(mem);
			returnParams.put("status", true);
		} catch (Exception e) {
			returnParams.put("status", false);
			returnParams.put("msg", "Failed to Register the user!!!!!!");
		}

		return returnParams;
	}










@RequestMapping("/searchbytitle/{title}")
public Book searchbookbytitle(@PathVariable("title") String title)
{
	return bookrepository.findByTitle(title);	
}


@RequestMapping("/bookdetails")
public List<BookDetail> bookdetail()
{
	return (List<BookDetail>) bookdetailrepository.findAll();
}	






@RequestMapping("/quantity")
public List<Quantity> quantity()
{
	return (List<Quantity>) quantityrepository.findAll();
}
	














@RequestMapping("/abc")
public HashMap<String,Object> issubook(@RequestBody BookDetail bookdetail ) {
	HashMap<String, Object> returnParams = new HashMap<String, Object>();
	
	try {

		Date today = new Date();
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		    today = sdf.parse(sdf.format(today));
		    bookdetail.setIssueDate(today);
		    Calendar c = Calendar.getInstance();
		    c.setTime(today); // Now use today date.
		    c.add(Calendar.DATE, 5); // Adding 5 days
		    Date after = c.getTime();
		    bookdetail.setDueDate(after);
		   String s=bookdetail.getQuantity().getAccountId();
		   Quantity q=quantityrepository.findOne(s);
		   q.setStatus(Status.Unavailable);
		   quantityrepository.save(q);
		
		bookdetailrepository.save(bookdetail);
		returnParams.put("status", true);
	} catch (Exception e) {
		returnParams.put("status", false);
		returnParams.put("msg", "Failed o issue!!!!!!");
		e.printStackTrace();
	}

	return returnParams;
}



@RequestMapping("/members")
public List<Member>getMembers()
{
	return (List<Member>)memberrespository.findAll();
}

}

















@RequestMapping("/markabook/{accountid}")
public HashMap<String,Object>markbook(@PathVariable("accountid") String accountid) {
	HashMap<String, Object> returnParams = new HashMap<String, Object>();
	
	
		returnParams.put("status", true);
	} catch (Exception e) {
		returnParams.put("status", false);
		returnParams.put("msg", "Failed to add Category!!");
	}

	return returnParams;
	
}	





