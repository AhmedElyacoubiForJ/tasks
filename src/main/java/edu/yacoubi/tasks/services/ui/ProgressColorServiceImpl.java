package edu.yacoubi.tasks.services.ui;

import org.springframework.stereotype.Service;

@Service
public class ProgressColorServiceImpl implements IProgressColorService {

    @Override
    public String getColorClass(int progress) {
        if (progress == 0) return "bg-secondary";
        if (progress < 50) return "bg-warning";
        if (progress < 100) return "bg-info";
        return "bg-success";
    }
}
