package com.example.demo;

import static java.util.UUID.randomUUID;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book {

  @Id
  public UUID id = randomUUID();

  public String title;

  public Book() {
  }

  public Book(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "Book{" +
        "id=" + id +
        ", title='" + title + '\'' +
        '}';
  }
}