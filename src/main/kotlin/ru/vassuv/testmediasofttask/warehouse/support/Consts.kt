package ru.vassuv.testmediasofttask.warehouse.support

object Consts {

    /**
     * Константные значение для правил применяемых в контроллерах
     */
    object SecurityRoleRules {
        const val ROLE_ADMIN = "hasRole('ADMIN')"
        const val ROLE_USER = "hasRole('USER')"
        const val ROLE_MANAGER = "hasRole('MANAGER')"

        const val ROLES_ALL = "hasAnyRole('USER', 'MANAGER', 'ADMIN')"
        const val ROLES_NOT_USER = "hasAnyRole('MANAGER', 'ADMIN')"

        const val SERVICE_AUTHORITY = "hasAuthority('rhumb_api')"
    }
}
