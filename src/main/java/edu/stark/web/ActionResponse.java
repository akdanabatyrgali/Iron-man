package edu.stark.web;

public record ActionResponse (
        boolean success,
        String message,
        DashboardResponse dashboard
)  {
}
