package e.group.thesismanager.service;

import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;

@Service
public interface CoordinatorService {
    Thesis assignSupervisor(User student, User supervisor);
    Thesis assignOpponent(User student, Set<User> opponent);
    Thesis evaluateProjectPlan(User student, Float grade);
    Thesis gradeFinalProject(User student, Float grade);

    Thesis initiateThesis(User student);
    void setThesis(List<Thesis> thesisList);
}
