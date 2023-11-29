package bLL;

import java.util.List;

import dAL.BooksDAO.Book;
import dAL.IDALFascade;

public interface IBooks {
	public List<Book> getAllBooks();
	public void insertBook(String title, String author, int year);
	public void updateBook(int id, String newTitle, String newAuthor, int newYear);
	public void deleteBook(int id);
	public void setIDAL(IDALFascade dal);

}
