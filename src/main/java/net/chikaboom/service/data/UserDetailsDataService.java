package net.chikaboom.service.data;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.repository.AboutRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.repository.UserDetailsRepository;
import net.chikaboom.util.PhoneNumberUtils;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных пользовательской информации.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsDataService {

    private final UserDetailsRepository userDetailsRepository;
    private final AboutRepository aboutRepository;
    private final PhoneCodeRepository phoneCodeRepository;

    /**
     * Проверяет существование в базе пользовательской информации по номеру телефона
     *
     * @param phone      номер телефона
     * @param countryCut буквенный код страны
     * @return true - если существует, false - в противном случае
     */
    public boolean existsUserDetailsByPhone(String phone, String countryCut) {
        String formattedPhone = "";
        try {
            formattedPhone = PhoneNumberUtils.formatNumberInternational(phone, countryCut);
        } catch (NumberParseException e) {
            return false;
        }

        return userDetailsRepository.existsUserDetailsByPhone(formattedPhone);
    }

    /**
     * Производит поиск пользовательской информации по идентификатору
     *
     * @param idUserDetails идентификатор пользовательской информации
     * @return найденную пользовательскую информацию
     */
    public Optional<UserDetails> findUserDetailsById(int idUserDetails) {
        return userDetailsRepository.findById(idUserDetails);
    }

    /**
     * Производит поиск пользовательской информации по номеру телефона
     *
     * @param phone      номер телефона
     * @param countryCut буквенный код страны для приведения номера телефона к формату, в котором данные хранятся в базе
     * @return найденную пользовательскую информацию
     * @throws NumberParseException возникает, когда невозможно отформатировать номер телефона
     */
    public Optional<UserDetails> findUserDetailsByPhone(String phone, String countryCut) throws NumberParseException {
        String formedPhone = PhoneNumberUtils.formatNumberInternational(phone, countryCut);

        return userDetailsRepository.findUserDetailsByPhone(formedPhone);
    }

    /**
     * Создает пользователськую информацию в базе данных
     *
     * @param userDetails пользовательская информация, которая должна быть сохранена в базе
     * @return созданную пользовательскую информацию
     */
    public UserDetails create(UserDetails userDetails) {
        About about = userDetails.getAbout();
        if (about == null) {
            about = new About();
        }
        userDetails.setAbout(aboutRepository.saveAndFlush(about));

        if (userDetails.getPhoneCode() != null) {
            userDetails.setPhoneCode(phoneCodeRepository.findFirstByCountryCut(userDetails.getPhoneCode().getCountryCut()));
        }

        if (userDetails.getDisplayedPhone() != null && !userDetails.getDisplayedPhone().equals("")) {
            try {
                userDetails.setDisplayedPhone(PhoneNumberUtils.formatNumberInternational(
                        userDetails.getDisplayedPhone(),
                        userDetails.getPhoneCode().getCountryCut()));
            } catch (NumberParseException e) {
                throw new IllegalArgumentException("Cannot save user details. Phone is incorrect. " + e.getMessage());
            }
        }

        return userDetailsRepository.save(userDetails);
    }

    /**
     * Изменение данных пользовательской информации
     *
     * @param userDetails обновляемая пользователская информация
     * @return обновленная пользовательская информация
     */
    public UserDetails patch(UserDetails userDetails) {
        Optional<UserDetails> userDetailsFromDbOptional = userDetailsRepository.findById(userDetails.getIdUserDetails());

        if (!userDetailsFromDbOptional.isPresent()) {
            throw new NotFoundException("User details not found");
        }

        UserDetails patchedUserDetails = userDetailsFromDbOptional.get();

        if (userDetails.getLastName() != null && !userDetails.getLastName().isEmpty()) {
            patchedUserDetails.setLastName(userDetails.getLastName());
        }

        if (userDetails.getFirstName() != null && !userDetails.getFirstName().isEmpty()) {
            patchedUserDetails.setFirstName(userDetails.getFirstName());
        }

        if (userDetails.getDisplayedPhone() != null && !userDetails.getDisplayedPhone().isEmpty() &&
                userDetails.getPhoneCode() != null &&
                !userDetails.getPhoneCode().getCountryCut().isEmpty()) {
            try {
                PhoneCode newPhoneCode = phoneCodeRepository.findFirstByCountryCut(userDetails.getPhoneCode().getCountryCut());

                patchedUserDetails.setPhoneCode(newPhoneCode);
                patchedUserDetails.setDisplayedPhone(PhoneNumberUtils.formatNumberInternational(
                        userDetails.getDisplayedPhone(), newPhoneCode.getCountryCut()));
            } catch (NumberParseException e) {
                throw new IllegalArgumentException("Cannot save user details. Phone is incorrect. " + e.getMessage());
            }
        }

        if (userDetails.getAbout() != null) {
            About oldAbout = patchedUserDetails.getAbout();
            if (userDetails.getAbout().getText() != null && !userDetails.getAbout().getText().isEmpty()) {
                oldAbout.setText(userDetails.getAbout().getText());
            }

            if (userDetails.getAbout().getProfession() != null && !userDetails.getAbout().getProfession().isEmpty()) {
                oldAbout.setProfession(userDetails.getAbout().getProfession());
            }
        }

        return userDetailsRepository.save(patchedUserDetails);
    }

    /**
     * Производит поиск клиентов мастера, а также высчитывает общее количество посещений к этому мастеру и дату
     * последнего визита
     *
     * @param idMasterAccount идентификатор мастера
     * @return список клиентов мастера
     */
    public List<UserDetails> findClientsWithExtraInfo(int idMasterAccount) {
        List<UserDetailsRepository.ExtendedUserDetails> resultObjects = userDetailsRepository.getListExtendedUserDetails(idMasterAccount);

        List<Integer> idUserDetailsClientList = new ArrayList<>();
        for (UserDetailsRepository.ExtendedUserDetails resultObject : resultObjects) {
            idUserDetailsClientList.add(resultObject.getIdUserDetails());
        }

        List<UserDetails> userDetailsList = userDetailsRepository.findByIdUserDetailsIn(idUserDetailsClientList);

        List<UserDetails> resultUserDetailsList = new ArrayList<>();

        for (UserDetails userDetails : userDetailsList) {
            for (UserDetailsRepository.ExtendedUserDetails resultObject : resultObjects) {
                if (resultObject.getIdUserDetails() == userDetails.getIdUserDetails()) {
                    resultUserDetailsList.add(userDetails);
                    userDetails.setVisitCount(resultObject.getVisitCount());
                    userDetails.setLastVisitDate(resultObject.getLastVisitDate());
                }
            }
        }

        return resultUserDetailsList;
    }

    /**
     * Производит удаление пользовательской информации по идентификатору
     *
     * @param idUserService идентификатор пользовательской информации
     */
    public void deleteById(int idUserService) {
        userDetailsRepository.deleteById(idUserService);
    }
}
