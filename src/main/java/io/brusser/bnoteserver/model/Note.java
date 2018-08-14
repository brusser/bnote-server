package io.brusser.bnoteserver.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Note {
  @JsonProperty("id")
  @Id
  @GeneratedValue
  private Long id = null;

  @JsonProperty("title")
  @Size(max = 255, message = "Title has a max length of 255 characters")
  private String title = null;

  @JsonProperty("content")
  @Size(max = 1024, message = "Title has a max length of 1024 characters")
  @Column(length = 1024)
  private String content = null;

  @JsonIgnore
  @CreationTimestamp
  private LocalDateTime createDateTime;

  @JsonIgnore
  @UpdateTimestamp
  private LocalDateTime updateDateTime;

  public Note() {
    super();
  }

  public Note(Long id, String title, String content) {
    super();
    this.id = id;
    this.title = title;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  /**
   * @param content the content to set
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  @java.lang.Override
  public java.lang.String toString() {
    return "Note{" + "id=" + id + ", title='" + title + '\'' + ", content='" + content + '\'' + '}';
  }

}
