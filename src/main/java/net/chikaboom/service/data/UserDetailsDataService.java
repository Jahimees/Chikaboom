package net.chikaboom.service.data;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.repository.UserDetailsRepository;
import net.chikaboom.util.PhoneNumberUtils;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных пользовательской информации.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsDataService implements DataService<UserDetails> {

    private final UserDetailsRepository userDetailsRepository;
    private final AboutDataService aboutDataService;
    private final PhoneCodeDataService phoneCodeDataService;

    /**
     * Производит поиск деталей пользователя по их id
     *
     * @param idUserDetails идентификатор пользовательской информации
     * @return пользовательскую информацию
     */
    @Override
    public Optional<UserDetails> findById(int idUserDetails) {
        return userDetailsRepository.findById(idUserDetails);
    }

    /**
     * Производит поиск все сущности пользовательских информаций
     *
     * @return список объектов пользовательской информации
     */
    @Override
    public List<UserDetails> findAll() {
        return userDetailsRepository.findAll();
    }

    /**
     * Производит удаление пользовательской информации по идентификатору
     *
     * @param idUserService идентификатор пользовательской информации
     */
    @Override
    public void deleteById(int idUserService) {
        userDetailsRepository.deleteById(idUserService);
    }

    /**
     * Производит обновление объекта в базе данных. Внимание! Полностью перезаписывает объект
     *
     * @param userDetails объект пользовательской информации
     * @return обновленный объект пользовательской информации
     */
    @Override
    public UserDetails update(UserDetails userDetails) {
        if (!userDetailsRepository.existsById(userDetails.getIdUserDetails())) {
            throw new NotFoundException("Target userDetails not found in database");
        }

        return userDetailsRepository.saveAndFlush(userDetails);
    }

    /**
     * Создает пользователськую информацию в базе данных
     *
     * @param userDetails пользовательская информация, которая должна быть сохранена в базе
     * @return созданную пользовательскую информацию
     */
    @Override
    public UserDetails create(UserDetails userDetails) {
        About about = userDetails.getAbout();
        if (about != null) {
            userDetails.setAbout(aboutDataService.create(userDetails.getAbout()));
        } else {
            userDetails.setAbout(aboutDataService.create(new About()));
        }

        if (userDetails.getPhoneCode() != null) {
            Optional<PhoneCode> phoneCodeOptional = phoneCodeDataService.findFirstByCountryCut(userDetails.getPhoneCode().getCountryCut());

            if (!phoneCodeOptional.isPresent()) {
                throw new NotFoundException("There not found phoneCode by country cut " +
                        userDetails.getPhoneCode().getCountryCut());
            }

            userDetails.setPhoneCode(phoneCodeOptional.get());
        }

        if (userDetails.getDisplayedPhone() != null && !userDetails.getDisplayedPhone().isEmpty()
                && userDetails.getPhoneCode() != null) {
            try {
                userDetails.setDisplayedPhone(PhoneNumberUtils.formatNumberInternational(
                        userDetails.getDisplayedPhone(),
                        userDetails.getPhoneCode().getCountryCut()));
            } catch (NumberParseException e) {
                throw new IllegalArgumentException("Cannot save user details. Phone is incorrect. " + e.getMessage());
            }
        }

        return userDetailsRepository.saveAndFlush(userDetails);
    }

    /**
     * Проверяет существование в базе пользовательской информации по номеру телефона
     *
     * @param phone      номер телефона
     * @param countryCut буквенный код страны
     * @return true - если существует, false - в противном случае
     */
    public boolean existsUserDetailsByPhone(String phone, String countryCut) {
        String formattedPhone;
        try {
            formattedPhone = PhoneNumberUtils.formatNumberInternational(phone, countryCut);
        } catch (NumberParseException e) {
            return false;
        }

        return userDetailsRepository.existsUserDetailsByPhone(formattedPhone);
    }

    /**
     * Производит поиск пользовательской информации по номеру телефона
     *
     * @param phone      номер телефона
     * @param countryCut буквенный код страны для приведения номера телефона к формату, в котором данные хранятся в базе
     * @return найденную пользовательскую информацию
     * @throws NumberParseException возникает, когда невозможно отформатировать номер телефона
     */
    public Optional<UserDetails> findUserDetailsByPhone(String phone, String countryCut) throws NumberParseException,
            BadCredentialsException {
        String formedPhone = PhoneNumberUtils.formatNumberInternational(phone, countryCut);

        return userDetailsRepository.findUserDetailsByPhone(formedPhone);
    }

    /**
     * Изменение данных пользовательской информации
     *
     * @param newUserDetails обновляемая пользователская информация
     * @return обновленная пользовательская информация
     */
    public UserDetails patch(UserDetails newUserDetails) {
        Optional<UserDetails> userDetailsOptional = findById(newUserDetails.getIdUserDetails());

        if (!userDetailsOptional.isPresent()) {
            throw new NotFoundException("There not found userDetails with id " + newUserDetails.getIdUserDetails());
        }

        UserDetails userDetailsFromDb = userDetailsOptional.get();


        if (newUserDetails.getLastName() != null && !newUserDetails.getLastName().isEmpty()) {
            userDetailsFromDb.setLastName(newUserDetails.getLastName());
        }

        if (newUserDetails.getFirstName() != null && !newUserDetails.getFirstName().isEmpty()) {
            userDetailsFromDb.setFirstName(newUserDetails.getFirstName());
        }

        if (newUserDetails.getDisplayedPhone() != null && !newUserDetails.getDisplayedPhone().isEmpty() &&
                newUserDetails.getPhoneCode() != null &&
                !newUserDetails.getPhoneCode().getCountryCut().isEmpty()) {
            try {
                Optional<PhoneCode> newPhoneCodeOptional = phoneCodeDataService.findFirstByCountryCut(
                        newUserDetails.getPhoneCode().getCountryCut());

                if (!newPhoneCodeOptional.isPresent()) {
                    throw new NotFoundException("There not found phoneCode by country cut " +
                            newUserDetails.getPhoneCode().getCountryCut());
                }

                PhoneCode newPhoneCode = newPhoneCodeOptional.get();

                userDetailsFromDb.setPhoneCode(newPhoneCode);
                userDetailsFromDb.setDisplayedPhone(PhoneNumberUtils.formatNumberInternational(
                        newUserDetails.getDisplayedPhone(), newPhoneCode.getCountryCut()));
            } catch (NumberParseException e) {
                throw new IllegalArgumentException("Cannot save user details. Phone is incorrect. " + e.getMessage());
            }
        }

        if (newUserDetails.getAbout() != null) {
            About oldAbout = userDetailsFromDb.getAbout();
            if (newUserDetails.getAbout().getText() != null &&
                    !newUserDetails.getAbout().getText().isEmpty()) {

                oldAbout.setText(newUserDetails.getAbout().getText());
            }

            if (newUserDetails.getAbout().getProfession() != null &&
                    !newUserDetails.getAbout().getProfession().isEmpty()) {

                oldAbout.setProfession(newUserDetails.getAbout().getProfession());
            }

            aboutDataService.update(oldAbout);
        }

        return userDetailsRepository.save(userDetailsFromDb);
    }

    /**
     * Производит поиск клиентов мастера, а также высчитывает общее количество посещений к этому мастеру и дату
     * последнего визита
     *
     * @param idMasterAccount идентификатор мастера
     * @return список клиентов мастера
     */
    public List<UserDetails> findClientsWithExtraInfo(int idMasterAccount) {
        List<UserDetailsRepository.ExtendedUserDetails> resultObjects =
                userDetailsRepository.getListExtendedUserDetails(idMasterAccount);

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
                    userDetails.setFirstVisitDate(resultObject.getFirstVisitDate());
                }
            }
        }

        return resultUserDetailsList;
    }
}
