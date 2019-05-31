package e.group.thesismanager.service;

import e.group.thesismanager.model.SupervisorRequestStatus;
import e.group.thesismanager.model.Thesis;
import e.group.thesismanager.model.User;
import e.group.thesismanager.repository.ThesisRepository;
import e.group.thesismanager.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {

    private final UserRepository userRepository;
    private final ThesisRepository thesisRepository;
    private final UserService userService;

    @Autowired
    public SearchServiceImpl(UserRepository userRepository, ThesisRepository thesisRepository, UserService userService) {

        this.userRepository = userRepository;
        this.thesisRepository = thesisRepository;
        this.userService = userService;
    }

    @ModelAttribute("thesis")
    public Thesis emptyThesis() {

        return new Thesis();
    }

    @Override
    public List<Thesis> searchThesisForOpponent(String firstName, String lastName) {

        List<Thesis> allFoundStudentThesesList = getAllFoundStudentThesesList(firstName, lastName);

        List<Thesis> allFoundOpponentThesisList = thesisRepository.findThesisByOpponent(userService.getCurrentUser());

        return searchedUserThesisList(allFoundStudentThesesList, allFoundOpponentThesisList);
    }

    @Override
    public List<Thesis> searchThesisForSupervisor(String firstName, String lastName, SupervisorRequestStatus supervisorRequestStatus) {

        List<Thesis> allFoundStudentThesesList = getAllFoundStudentThesesList(firstName, lastName);

        List<Thesis> allFoundSupervisorThesisList = thesisRepository.findThesesBySupervisorAndSupervisorRequestStatus(userService.getCurrentUser(), supervisorRequestStatus);

        return searchedUserThesisList(allFoundStudentThesesList, allFoundSupervisorThesisList);
    }

    @Override
    public List<Thesis> searchThesisForReader(String firstName, String lastName) {

        List<Thesis> allFoundStudentThesesList = getAllFoundStudentThesesList(firstName, lastName);

        List<Thesis> allFoundReaderThesisList = thesisRepository.findThesesByReaders(userService.getCurrentUser());

        return searchedUserThesisList(allFoundStudentThesesList, allFoundReaderThesisList);
}

    @Override
    public List<Thesis> searchPossibleThesisForReader(String firstName, String lastName) {

        List<Thesis> allFoundStudentThesesList = getAllFoundStudentThesesList(firstName, lastName);

        List<Thesis> allTheses = thesisRepository.findAll();
        List<Thesis> allFoundPossibleThesesList = new ArrayList<Thesis>();

        for(Thesis thesis: allTheses) {

            if(!thesis.getReaders().contains(userService.getCurrentUser()) && !thesis.getBidders().contains(userService.getCurrentUser())) {

                allFoundPossibleThesesList.add(thesis);
            }
        }

        List<Thesis> searchedReaderThesisList = new ArrayList<Thesis>();

        for(Thesis thesis : allFoundStudentThesesList) {

            if(allFoundStudentThesesList.contains(thesis) && allFoundPossibleThesesList.contains(thesis)) {

                searchedReaderThesisList.add(thesis);
            }
        }

        return searchedReaderThesisList;
    }

    @Override
    public List<Thesis> searchThesisForCoordinator(String firstName, String lastName) {

        List<Thesis> allFoundStudentThesesList = getAllFoundStudentThesesList(firstName, lastName);

        List<Thesis> allFoundCoordinatorThesisList = thesisRepository.findAll();

        return searchedUserThesisList(allFoundStudentThesesList, allFoundCoordinatorThesisList);
    }

    private List<Thesis> getAllFoundStudentThesesList(String firstName, String lastName) {

        List<User> allFoundUsersList = userRepository.findAllByFirstNameLikeIgnoreCaseAndLastNameLikeIgnoreCase(firstName, lastName);
        List<Thesis> allFoundStudentThesesList = new ArrayList<Thesis>();

        for(User user : allFoundUsersList) {

            allFoundStudentThesesList.addAll(thesisRepository.findThesesByStudent(user));
        }

        return allFoundStudentThesesList;
    }

    private List<Thesis> searchedUserThesisList(List<Thesis> allFoundStudentThesesList, List<Thesis> allFoundUserThesisList) {

        List<Thesis> searchedUserThesisList = new ArrayList<Thesis>();

        for(Thesis thesis : allFoundStudentThesesList) {

            if(allFoundUserThesisList.contains(thesis)) {

                searchedUserThesisList.add(thesis);
            }
        }
        return searchedUserThesisList;
    }
}