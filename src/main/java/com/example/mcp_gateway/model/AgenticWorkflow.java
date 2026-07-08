package com.example.mcp_gateway.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agentic_workflows")
public class AgenticWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String workflowName;

    // Adımların veritabanında sıralı bir alt tabloda (String listesi olarak) tutulmasını sağlar
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "workflow_steps", joinColumns = @JoinColumn(name = "workflow_id"))
    @Column(name = "step_description")
    @OrderColumn(name = "step_order") // Adımların sırasını korur (1. Adım, 2. Adım...)
    private List<String> steps = new ArrayList<>();

    // Boş Constructor (JPA için zorunlu)
    public AgenticWorkflow() {}

    public AgenticWorkflow(String workflowName, List<String> steps) {
        this.workflowName = workflowName;
        this.steps = steps;
    }

    // Getter ve Setter Metotları
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getWorkflowName() { return workflowName; }
    public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }

    public List<String> getSteps() { return steps; }
    public void setSteps(List<String> steps) { this.steps = steps; }
}