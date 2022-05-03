package com.example.blog_api.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
//@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name="Posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    private Category category;
    private long noOfLikes;
    private long noOfComments;
    private LocalDate datePosted;
    private LocalTime timePosted;
    private LocalDate dateModified;
    private LocalTime timeModified;

}
