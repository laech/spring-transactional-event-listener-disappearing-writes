package com.example.demo;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionOperations;

@SpringBootApplication
public class DemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  public CommandLineRunner demo(
      BookRepository books,
      ApplicationEventPublisher publisher,
      TransactionOperations transactions) {
    return (args) -> {
      transactions.executeWithoutResult(status -> {
        var book = new Book("Old Title");
        books.save(book);
        publisher.publishEvent(new BookSavedEvent(book.id));
      });

      System.out.println(books.findAll() + " <- actual");
      System.out.println();
    };
  }

  @Component
  static class BookSavedListener {

    @Autowired
    private BookRepository books;

    @TransactionalEventListener
    void onEvent(BookSavedEvent event) {
      var book = books.findById(event.bookId).orElseThrow();
      book.title = "New Title";
      books.save(book);

      System.out.println();
      System.out.println(books.findAll() + " <- expected");
    }
  }

  static class BookSavedEvent {

    final UUID bookId;

    BookSavedEvent(UUID bookId) {
      this.bookId = bookId;
    }
  }

}
