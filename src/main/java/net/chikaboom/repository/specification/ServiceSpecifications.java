package net.chikaboom.repository.specification;

import net.chikaboom.model.database.Service;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.chikaboom.util.constant.EntityNamesConstant.*;

/**
 * Описывает спецификации для работы с услугами, типами услуг и подтипами услуг
 */
public class ServiceSpecifications {

    /**
     * Производит поиск всех услуг по массиву идентификаторов подтипов услуг. Для того, чтобы возвращать подтипы
     * только одного типа, принимает параметр idServiceType
     *
     * @param serviceSubtypeIdArray массив идентификаторов подтипов услуг
     * @param idServiceType         идентификатор типа услуги
     * @return спецификацию
     */
    public static Specification<Service> servicesByServiceSubtypeIdArray(int[] serviceSubtypeIdArray, int idServiceType) {
        return (root, query, criteriaBuilder) -> {
            if (serviceSubtypeIdArray != null && serviceSubtypeIdArray.length != 0) {
                List<Integer> serviceSubtypeIdList = new ArrayList<>();
                Arrays.stream(serviceSubtypeIdArray).forEach(serviceSubtypeIdList::add);

                return criteriaBuilder.and(
                        root
                                .get(SERVICE_SUBTYPE)
                                .get(ID_SERVICE_SUBTYPE).in(serviceSubtypeIdList),
                        criteriaBuilder.equal(
                                root
                                        .get(SERVICE_SUBTYPE)
                                        .get(SERVICE_TYPE)
                                        .get(ID_SERVICE_TYPE), idServiceType));
            } else {
                return criteriaBuilder.equal(root
                        .get(SERVICE_SUBTYPE)
                        .get(SERVICE_TYPE)
                        .get(ID_SERVICE_TYPE), idServiceType);
            }
        };
    }
}
