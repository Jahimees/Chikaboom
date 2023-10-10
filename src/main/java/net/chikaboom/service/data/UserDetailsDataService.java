package net.chikaboom.service.data;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.PhoneCodeFacadeConverter;
import net.chikaboom.facade.converter.UserDetailsFacadeConverter;
import net.chikaboom.facade.dto.AboutFacade;
import net.chikaboom.facade.dto.PhoneCodeFacade;
import net.chikaboom.facade.dto.UserDetailsFacade;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.UserDetails;
import net.chikaboom.repository.UserDetailsRepository;
import net.chikaboom.util.PhoneNumberUtils;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис предоставляет возможность обработки данных пользовательской информации.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsDataService implements DataService<UserDetailsFacade> {

    private final UserDetailsRepository userDetailsRepository;
    private final AboutDataService aboutDataService;
    private final PhoneCodeDataService phoneCodeDataService;

    private final UserDetailsFacadeConverter userDetailsFacadeConverter;
    private final PhoneCodeFacadeConverter phoneCodeFacadeConverter;

    /**
     * Производит поиск деталей пользователя по их id
     *
     * @param idUserDetails идентификатор пользовательской информации
     * @return пользовательскую информацию
     */
    @Override
    public UserDetailsFacade findById(int idUserDetails) {
        Optional<UserDetails> userDetailsOptional = userDetailsRepository.findById(idUserDetails);

        if (!userDetailsOptional.isPresent()) {
            throw new NotFoundException("There is no found user details with id " + idUserDetails);
        }

        return userDetailsFacadeConverter.convertToDto(userDetailsOptional.get());
    }

    /**
     * Производит поиск все сущности пользовательских информаций
     *
     * @return список объектов пользовательской информации
     */
    @Override
    public List<UserDetailsFacade> findAll() {
        return userDetailsRepository.findAll().stream().map(
                userDetailsFacadeConverter::convertToDto).collect(Collectors.toList());
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
     * @param userDetailsFacade объект пользовательской информации
     * @return обновленный объект пользовательской информации
     */
    @Override
    public UserDetailsFacade update(UserDetailsFacade userDetailsFacade) {
        if (!userDetailsRepository.existsById(userDetailsFacade.getIdUserDetails())) {
            throw new NotFoundException("Target userDetails not found in database");
        }

        return userDetailsFacadeConverter.convertToDto(
                userDetailsRepository.saveAndFlush(
                        userDetailsFacadeConverter.convertToModel(userDetailsFacade)));
    }

    /**
     * Создает пользователськую информацию в базе данных
     *
     * @param userDetailsFacade пользовательская информация, которая должна быть сохранена в базе
     * @return созданную пользовательскую информацию
     */
    @Override
    public UserDetailsFacade create(UserDetailsFacade userDetailsFacade) {
        AboutFacade aboutFacade = userDetailsFacade.getAboutFacade();
        if (aboutFacade != null) {
            aboutDataService.create(userDetailsFacade.getAboutFacade());
        } else {
            userDetailsFacade.setAboutFacade(aboutDataService.create(new AboutFacade()));
        }

        if (userDetailsFacade.getPhoneCodeFacade() != null) {
            userDetailsFacade.setPhoneCodeFacade(
                    phoneCodeDataService.findFirstByCountryCut(userDetailsFacade.getPhoneCodeFacade().getCountryCut()));
        }

        if (userDetailsFacade.getDisplayedPhone() != null && !userDetailsFacade.getDisplayedPhone().equals("")
                && userDetailsFacade.getPhoneCodeFacade() != null) {
            try {
                userDetailsFacade.setDisplayedPhone(PhoneNumberUtils.formatNumberInternational(
                        userDetailsFacade.getDisplayedPhone(),
                        userDetailsFacade.getPhoneCodeFacade().getCountryCut()));
            } catch (NumberParseException e) {
                throw new IllegalArgumentException("Cannot save user details. Phone is incorrect. " + e.getMessage());
            }
        }

        return userDetailsFacadeConverter.convertToDto(
                userDetailsRepository.saveAndFlush(
                        userDetailsFacadeConverter.convertToModel(userDetailsFacade)));
    }

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
     * Производит поиск пользовательской информации по номеру телефона
     *
     * @param phone      номер телефона
     * @param countryCut буквенный код страны для приведения номера телефона к формату, в котором данные хранятся в базе
     * @return найденную пользовательскую информацию
     * @throws NumberParseException возникает, когда невозможно отформатировать номер телефона
     */
    public UserDetailsFacade findUserDetailsByPhone(String phone, String countryCut) throws NumberParseException {
        String formedPhone = PhoneNumberUtils.formatNumberInternational(phone, countryCut);
        Optional<UserDetails> userDetailsOptional = userDetailsRepository.findUserDetailsByPhone(formedPhone);

        if (!userDetailsOptional.isPresent()) {
            throw new NotFoundException("There is not found userDetails with phone " + phone);
        }

        return userDetailsFacadeConverter.convertToDto(userDetailsOptional.get());
    }

    /**
     * Изменение данных пользовательской информации
     *
     * @param userDetailsFacade обновляемая пользователская информация
     * @return обновленная пользовательская информация
     */
    public UserDetailsFacade patch(UserDetailsFacade userDetailsFacade) {
        Optional<UserDetails> userDetailsFromDbOptional = userDetailsRepository.findById(userDetailsFacade.getIdUserDetails());

        if (!userDetailsFromDbOptional.isPresent()) {
            throw new NotFoundException("User details not found");
        }

        UserDetails patchedUserDetails = userDetailsFromDbOptional.get();

        if (userDetailsFacade.getLastName() != null && !userDetailsFacade.getLastName().isEmpty()) {
            patchedUserDetails.setLastName(userDetailsFacade.getLastName());
        }

        if (userDetailsFacade.getFirstName() != null && !userDetailsFacade.getFirstName().isEmpty()) {
            patchedUserDetails.setFirstName(userDetailsFacade.getFirstName());
        }

        if (userDetailsFacade.getDisplayedPhone() != null && !userDetailsFacade.getDisplayedPhone().isEmpty() &&
                userDetailsFacade.getPhoneCodeFacade() != null &&
                !userDetailsFacade.getPhoneCodeFacade().getCountryCut().isEmpty()) {
            try {
                PhoneCodeFacade newPhoneCode = phoneCodeDataService.findFirstByCountryCut(
                        userDetailsFacade.getPhoneCodeFacade().getCountryCut());

                patchedUserDetails.setPhoneCode(phoneCodeFacadeConverter.convertToModel(newPhoneCode));
                patchedUserDetails.setDisplayedPhone(PhoneNumberUtils.formatNumberInternational(
                        userDetailsFacade.getDisplayedPhone(), newPhoneCode.getCountryCut()));
            } catch (NumberParseException e) {
                throw new IllegalArgumentException("Cannot save user details. Phone is incorrect. " + e.getMessage());
            }
        }

        if (userDetailsFacade.getAboutFacade() != null) {
            About oldAbout = patchedUserDetails.getAbout();
            if (userDetailsFacade.getAboutFacade().getText() != null &&
                    !userDetailsFacade.getAboutFacade().getText().isEmpty()) {

                oldAbout.setText(userDetailsFacade.getAboutFacade().getText());
            }

            if (userDetailsFacade.getAboutFacade().getProfession() != null &&
                    !userDetailsFacade.getAboutFacade().getProfession().isEmpty()) {

                oldAbout.setProfession(userDetailsFacade.getAboutFacade().getProfession());
            }
        }

        return userDetailsFacadeConverter.convertToDto(userDetailsRepository.save(patchedUserDetails));
    }

    /**
     * Производит поиск клиентов мастера, а также высчитывает общее количество посещений к этому мастеру и дату
     * последнего визита
     *
     * @param idMasterAccount идентификатор мастера
     * @return список клиентов мастера
     */
    public List<UserDetailsFacade> findClientsWithExtraInfo(int idMasterAccount) {
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
                }
            }
        }

        return resultUserDetailsList.stream().map(
                userDetailsFacadeConverter::convertToDto).collect(Collectors.toList());
    }
}
