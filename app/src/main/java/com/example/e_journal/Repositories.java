package com.example.e_journal;

import com.example.e_journal.model.auth.AuthRepository;
import com.example.e_journal.model.auth.FirebaseAuthRepository;
import com.example.e_journal.model.group_table.FirebaseGroupTableRepository;
import com.example.e_journal.model.groups.FirebaseGroupsRepository;
import com.example.e_journal.model.homework.FirebaseHomeworkRepository;
import com.example.e_journal.model.schedule.FirebaseScheduleRepository;

public class Repositories {

    private Repositories() {}
    private static final AuthRepository authRepository = new FirebaseAuthRepository();
    private static final FirebaseGroupsRepository groupsRepository = new FirebaseGroupsRepository();
    private static final FirebaseScheduleRepository scheduleRepository = new FirebaseScheduleRepository();
    private static final FirebaseHomeworkRepository homeworkRepository = new FirebaseHomeworkRepository();
    private static final FirebaseGroupTableRepository groupTableRepository = new FirebaseGroupTableRepository();

    public static AuthRepository getAuthRepository() {
        return authRepository;
    }
    public static FirebaseGroupsRepository getGroupsRepository() {
        return groupsRepository;
    }
    public static FirebaseScheduleRepository getScheduleRepository() { return scheduleRepository; }
    public static FirebaseHomeworkRepository getHomeworkRepository() { return homeworkRepository; }
    public static FirebaseGroupTableRepository getGroupTableRepository() { return groupTableRepository; }
}
