package org.hbrs.se2.project.startupx.control;

import org.hbrs.se2.project.startupx.entities.Studiengang;
import org.hbrs.se2.project.startupx.repository.StudiengangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class StudiengangControl {

    @Autowired
    private StudiengangRepository studiengangRepository;

    public List<Studiengang> getAll() {
        return studiengangRepository.findAll();
    }
}
