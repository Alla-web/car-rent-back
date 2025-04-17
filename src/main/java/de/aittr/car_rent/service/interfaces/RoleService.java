package de.aittr.car_rent.service.interfaces;

import de.aittr.car_rent.domain.entity.Role;

import java.util.Optional;

public interface RoleService {

    Role getRoleUser();

    Role getRoleAdmin();

    /**
     * Интерфейс сервиса для работы с ролями клиентов.
     * <p>
     * Данный интерфейс предоставляет методы для поиска и сохранения ролей пользователей.
     * Реализация должна обеспечивать получение существующей роли по имени или создание новой,
     * если роль с таким именем не найдена.
     * </p>
     */
    interface CustomerRoleService {

        /**
         * Сохраняет новую роль или возвращает уже существующую роль с заданным именем.
         * <p>
         * Если роль с заданным именем не существует, должна быть создана новая роль и сохранена.
         * </p>
         *
         * @param roleName имя роли, которую необходимо сохранить или получить
         * @return объект {@link Role} с указанным именем
         */
        Role saveOrGet(String roleName);

        /**
         * Ищет роль по имени.
         *
         * @param roleName имя роли для поиска
         * @return {@link Optional} содержащий найденную роль, или пустой {@code Optional}, если роль не найдена
         */
        Optional<Role> findByName(String roleName);
    }
}
