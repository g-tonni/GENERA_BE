package giadatonni.GENERA._BE.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue
    private UUID projectId;

    @Column(nullable = false)
    private String title;

    @Column(length = 700)
    private String description;

    private String howToInteract;

    @Column(nullable = false)
    private String cover;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String script;

    @Column(nullable = false)
    private LocalDate published;

    @Column(nullable = false)
    private LocalDate lastUpdated;

    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appreciation> appreciations;

    public Project() {
    }

    public Project(Category category, User author) {
        this.title = "My project";
        this.cover = "https://res.cloudinary.com/cloudgiada/image/upload/v1772897894/immagini_default-20_wqtzqb.png";
        this.script = """
                function setup() {
                  createCanvas(400, 400);
                  background(255);
                  noStroke();
                }
                
                function draw() {
                  background(255, 20);
                  stroke(0);
                  noFill()
                  ellipse(mouseX, mouseY, 20, 20);
                }
                """;
        this.published = LocalDate.now();
        this.lastUpdated = LocalDate.now();
        this.category = category;
        this.author = author;
    }

    public Project(String title, String description, String howToInteract, String cover, String script, Category category, User author) {
        this.title = title;
        this.description = description;
        this.howToInteract = howToInteract;
        this.cover = cover;
        this.script = script;
        this.published = LocalDate.now();
        this.lastUpdated = LocalDate.now();
        this.category = category;
        this.author = author;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHowToInteract() {
        return howToInteract;
    }

    public void setHowToInteract(String howToInteract) {
        this.howToInteract = howToInteract;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public LocalDate getPublished() {
        return published;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "Project {" +
                " projectId = " + projectId +
                ", title = " + title +
                ", description = " + description +
                ", howToInteract = " + howToInteract +
                ", script = " + script +
                ", published = " + published +
                ", lastUpdated = " + lastUpdated +
                ", category = " + category +
                ", author = " + author.getUserId() +
                " }";
    }
}
