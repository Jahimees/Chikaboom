package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.repository.AboutRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.repository.UserDetailsRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.type.StandardBasicTypes.INTEGER;
import static org.hibernate.type.StandardBasicTypes.TIMESTAMP;

@Service
@RequiredArgsConstructor
public class UserDetailsDataService {

    private final UserDetailsRepository userDetailsRepository;
    private final AboutRepository aboutRepository;
    private final PhoneCodeRepository phoneCodeRepository;
    private final SessionFactory sessionFactory;

    public UserDetails create(UserDetails userDetails) {
        About about = userDetails.getAbout();
        if (about == null) {
            about = new About();
        }
        userDetails.setAbout(aboutRepository.saveAndFlush(about));

        if (userDetails.getPhoneCode() != null) {
            userDetails.setPhoneCode(phoneCodeRepository.findFirstByPhoneCode(userDetails.getPhoneCode().getPhoneCode()));
        }

        return userDetailsRepository.save(userDetails);
    }

    /**
     * Производит поиск клиентов мастера, а также высчитывает общее количество посещений к этому мастеру и дату
     * последнего визита
     *
     * @param idMasterAccount идентификатор мастера
     * @return список клиентов мастера
     */
    public List<UserDetails> findClientsWithExtraInfo(int idMasterAccount) {
        Session session = sessionFactory.openSession();

        Query query = session.createNativeQuery("SELECT count(appointment.idappointment) as visitCount, " +
                        "user_details.iduser_details as idUserDetails, max(appointment.appointment_date_time) as " +
                        "lastVisitDate from user_details left join appointment " +
                        "on user_details.iduser_details = appointment.iduser_details_client where " +
                        "appointment.idaccount_master = :idAccount or user_details.idaccount_owner = :idAccount " +
                        "group by user_details.iduser_details")
                .addScalar("visitCount", INTEGER)
                .addScalar("idUserDetails", INTEGER)
                .addScalar("lastVisitDate", TIMESTAMP);
        query.setParameter("idAccount", idMasterAccount);
        List<Object> resultObjects = query.getResultList();

        List<Integer> idUserDetailsClientList = new ArrayList<>();
        for (Object resultObject : resultObjects) {
            Object[] resultObjectArr = (Object[]) resultObject;
            idUserDetailsClientList.add((int) resultObjectArr[1]);
        }

        query = session.createQuery("from UserDetails where idUserDetails in :idUserDetailsList", UserDetails.class);
        query.setParameter("idUserDetailsList", idUserDetailsClientList);
        List<UserDetails> userDetailsList = query.list();
        session.close();

        List<UserDetails> resultUserDetailsList = new ArrayList<>();

        for (UserDetails userDetails : userDetailsList) {
            for (Object resultObject : resultObjects) {
                Object[] objectDetails = (Object[]) resultObject;
                if ((int) objectDetails[1] == userDetails.getIdUserDetails()) {
                    resultUserDetailsList.add(userDetails);
                    userDetails.setVisitCount((Integer) objectDetails[0]);
                    userDetails.setLastVisitDate((Timestamp) objectDetails[2]);
                }
            }
        }

        return resultUserDetailsList;
    }
}
